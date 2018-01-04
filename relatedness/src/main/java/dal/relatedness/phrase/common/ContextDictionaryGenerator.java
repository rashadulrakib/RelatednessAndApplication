package dal.relatedness.phrase.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.TreeSet;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;

public class ContextDictionaryGenerator {
	TreeSet<String> contextsBg;
	
	public void AccumulateContextDictionaryWithId(){
		try{
			contextsBg = new TreeSet<String>();
			
			Accumulate3gmContextRecursively(PhraseDirectories.trigramCrossCheckDir);
			Accumulate4gmContextRecursively(PhraseDirectories.fourgramCrossCheckDir);
						
			SaveContexts();
			
			System.out.println("AccumulateContextDictionaryWithId finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void SaveContexts() {
		try{
			
			String accumulatedContextFile = PhraseDirectories.nGramIndexedProcessedContextStemDictionaryDir+PhraseFileNames.nGmAllContextFile;
			
			System.out.println("Writing to "+ accumulatedContextFile+","+contextsBg.size());
			
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(accumulatedContextFile));
			
			Iterator<String> itr = contextsBg.iterator();
			
			while(itr.hasNext()){
				bufferedWriter.write(itr.next()+"\n");
			}
			
			bufferedWriter.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void Accumulate3gmContextRecursively(String path) {
		try{
			File root = new File( path );
	        File[] list = root.listFiles();

	        if (list == null) return;

	        for ( File f : list ) {
	            if ( f.isDirectory() ) {
	            	Accumulate3gmContextRecursively( f.getAbsolutePath() );
	            }
	            else {
	                System.out.println( "File:" + f.getAbsoluteFile() );
	                Populate3gmContexts(f);
	                System.out.println(contextsBg.size());
	            }
	        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void Populate3gmContexts(File file) {
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				String [] arr = sCurrentLine.split("\\s+");
				
				int numberContexts = Integer.parseInt(arr[1]);
				
				for(int i=1;i<=numberContexts;i++){
					String context = arr[i*3-1]+" "+arr[i*3];
					contextsBg.add(context);
				}
			}
			
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void Accumulate4gmContextRecursively(String path) {
		try{
			File root = new File( path );
	        File[] list = root.listFiles();

	        if (list == null) return;

	        for ( File f : list ) {
	            if ( f.isDirectory() ) {
	            	Accumulate4gmContextRecursively( f.getAbsolutePath() );
	            }
	            else {
	                System.out.println( "File:" + f.getAbsoluteFile() );
	                Populate4gmContexts(f);
	                System.out.println(contextsBg.size());
	            }
	        }
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void Populate4gmContexts(File file) {
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				String [] arr = sCurrentLine.split("\\s+");
				
				int numberContexts = Integer.parseInt(arr[2]);
				
				for(int i=1;i<=numberContexts;i++){
					String context = arr[i*3]+" "+arr[i*3+1];
					contextsBg.add(context);
				}
			}
			
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
