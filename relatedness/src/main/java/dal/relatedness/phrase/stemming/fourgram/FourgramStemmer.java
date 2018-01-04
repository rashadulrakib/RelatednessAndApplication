package dal.relatedness.phrase.stemming.fourgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.TreeMap;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.phrase.utils.common.Util;

public class FourgramStemmer {
	
	TreeMap<String, TreeMap<String, Integer>> bgPhraseWithContexts;
	
	public void StemPreocessed4grams(){
		try{
		
			StemFourgrams();
	    	System.out.println("StemPreocessed4grams finished");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	private void StemFourgrams() {
		try{
			for(char ch='a'; ch<='z';ch++){
				bgPhraseWithContexts = new TreeMap<String, TreeMap<String,Integer>>();
				String fourgmDir = PhraseDirectories.fourGramIndexedProcessedContextDir+String.valueOf(ch)+"/";
				
				StemRecursively(fourgmDir, ch);
				
				String outFilepath = PhraseDirectories.fourGramIndexedProcessedContextStemDir+String.valueOf(ch);
				SaveStemedPhraseWithContexts(outFilepath);
				
				bgPhraseWithContexts.clear();
				bgPhraseWithContexts = null;
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	private void SaveStemedPhraseWithContexts(String outFilepath) {
		try{
			System.out.println("Writing to "+ outFilepath);
			
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFilepath));
			
			for(String stemKey: bgPhraseWithContexts.keySet()){
				TreeMap<String, Integer> contexts = bgPhraseWithContexts.get(stemKey);
				
				bufferedWriter.write(stemKey+" "+contexts.size());
				
				for(String contextKey: contexts.keySet()){
					bufferedWriter.write(" "+ contextKey+" "+contexts.get(contextKey));
				}
				bufferedWriter.write("\n");
			}
			
			bufferedWriter.close();
			
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	private void StemRecursively(String path, char firstchar) {
		try{
			File root = new File( path );
	        File[] list = root.listFiles();

	        if (list == null) return;

	        for ( File f : list ) {
	            if ( f.isDirectory() ) {
	            	StemRecursively( f.getAbsolutePath(), firstchar );
	            }
	            else {
	            	//System.out.println( "File:" + f.getAbsoluteFile() );
	            	PopulateStemPharseWithContext(f, firstchar);
	            }
	        }
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	private void PopulateStemPharseWithContext(File file, char firstChar) {
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				String [] arr = sCurrentLine.split("\\s+");
				String bggram = arr[0]+" "+arr[1];
				
				if(!Util.IsValidTokenByAllCharNumber(bggram)){
					System.out.println("pattern pb="+bggram);
					continue;
				}
				
				String bgStem = StemmingUtil.stemPhrase(bggram);
				
				String steamArr [] = bgStem.split("\\s+");
				
				if(steamArr.length!=2 || steamArr[0].length()<2 || steamArr[1].length()<2 ){
					System.out.println("length pb="+bggram);
					continue;
				}
				
				if(bgStem.charAt(0)!=firstChar){
					continue;
				}
				
				int numberContexts = Integer.parseInt(arr[2]);
				TreeMap<String, Integer> hmContextFreq = new TreeMap<String, Integer>();
				for(int i=1;i<=numberContexts;i++){
					String context = arr[i*3]+" "+arr[i*3+1];
					int contextFreq = Integer.parseInt(arr[i*3+2]);
					hmContextFreq.put(context, contextFreq);
				}
				
				if(!bgPhraseWithContexts.containsKey(bgStem)){
					bgPhraseWithContexts.put(bgStem, hmContextFreq);
				}
				else{
					TreeMap<String, Integer> oldHmContextFreq = bgPhraseWithContexts.get(bgStem);
					TreeMap<String, Integer> mergedHmContextFreq = MergeContextFreq(oldHmContextFreq, hmContextFreq);
					bgPhraseWithContexts.put(bgStem, mergedHmContextFreq);
				}
			}
			
			br.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	private TreeMap<String, Integer> MergeContextFreq(TreeMap<String, Integer> oldHmContextFreq,
			TreeMap<String, Integer> hmContextFreq) {
		TreeMap<String, Integer> mergedHmContextFreq = new TreeMap<String, Integer>();
		
		try{
		
			for(String oldKey: oldHmContextFreq.keySet()){
				mergedHmContextFreq.put(oldKey, oldHmContextFreq.get(oldKey));
			}
			
			for(String newKey: hmContextFreq.keySet()){
				if(!mergedHmContextFreq.containsKey(newKey)){
					mergedHmContextFreq.put(newKey, hmContextFreq.get(newKey));
				}
				else{
					mergedHmContextFreq.put(newKey, mergedHmContextFreq.get(newKey)+hmContextFreq.get(newKey));
				}
			}
			
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
		return mergedHmContextFreq;
	}
}
