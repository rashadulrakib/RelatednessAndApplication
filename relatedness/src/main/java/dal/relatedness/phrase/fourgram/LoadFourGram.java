package dal.relatedness.phrase.fourgram;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.entities.fourgram.FourgramAsBigramContext;

//tokenized four-gram: bg-id number-of-contexts bg-context1 bg-freq1 bg-context2 bg-freq2  
public class LoadFourGram {
	
	public Map<Integer, Map<Integer, Integer>> populateFourGramsWithIdAndContexts() throws IOException{
		Map<Integer, Map<Integer, Integer>> fourgramsIdsAndContexts = new HashMap<Integer, Map<Integer,Integer>>();
		
		
		return fourgramsIdsAndContexts;
	}
	
	public ArrayList<HashMap<Integer, FourgramAsBigramContext>> populateFourGramsWithIdAndContextsInParallel() throws IOException{
		ArrayList<HashMap<Integer, FourgramAsBigramContext>> fourgramsIdsAndContexts = new ArrayList<HashMap<Integer,FourgramAsBigramContext>>();
		
		File files [] = new File(PhraseDirectories.moreTokenizedFourGramDir).listFiles();
		int threads = files.length;
		
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		List<Future<HashMap<Integer, FourgramAsBigramContext>>> list = new ArrayList<Future<HashMap<Integer,FourgramAsBigramContext>>>();
		
		for(File file: files){
			
			Callable<HashMap<Integer, FourgramAsBigramContext>> callable = new LoadFourGramParallel(file);
			Future<HashMap<Integer, FourgramAsBigramContext>> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
            list.add(future);
			
		}
		
		for(Future<HashMap<Integer, FourgramAsBigramContext>> fut : list){
            try {
                //print the return value of Future, notice the output delay in console
                // because Future.get() waits for task to get completed
                //System.out.println(fut.get().size());
            	fourgramsIdsAndContexts.add(fut.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		System.out.println(fourgramsIdsAndContexts.size());
		
		executor.shutdownNow();
		
		System.out.println("total size="+fourgramsIdsAndContexts.size()+","+new Date().toString());
		
		return fourgramsIdsAndContexts;
	}
	
	/*public ArrayList<HashMap<Integer, HashMap<Integer, Integer>>> populateFourGramsWithIdAndContextsInParallel() throws IOException{
		ArrayList<HashMap<Integer, HashMap<Integer, Integer>>> fourgramsIdsAndContexts = new ArrayList<HashMap<Integer,HashMap<Integer,Integer>>>();
		
		File files [] = new File(Directories.fourGramIndexedTokenizedDir).listFiles();
		int threads = files.length;
		
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		List<Future<HashMap<Integer, HashMap<Integer, Integer>>>> list = new ArrayList<Future<HashMap<Integer,HashMap<Integer,Integer>>>>();
		
		for(File file: files){
			
			Callable<HashMap<Integer, HashMap<Integer, Integer>>> callable = new LoadFourGramParallel(file);
			Future<HashMap<Integer, HashMap<Integer, Integer>>> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
            list.add(future);
			
		}
		
		for(Future<HashMap<Integer, HashMap<Integer, Integer>>> fut : list){
            try {
                //print the return value of Future, notice the output delay in console
                // because Future.get() waits for task to get completed
                //System.out.println(fut.get().size());
            	fourgramsIdsAndContexts.add(fut.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		System.out.println(fourgramsIdsAndContexts.size());
		
		executor.shutdownNow();
		
		System.out.println("total size="+fourgramsIdsAndContexts.size()+","+new Date().toString());
		
		return fourgramsIdsAndContexts;
	}*/
	
	/*public HashMap<Integer, HashMap<Integer, Integer>> populateFourGramsWithIdAndContextsInParallel() throws IOException{
		HashMap<Integer, HashMap<Integer, Integer>> fourgramsIdsAndContexts = new HashMap<Integer,HashMap<Integer,Integer>>();
		
		File dirs [] = new File(Directories.fourGramIndexedTokenizedDir).listFiles();
		int threads = dirs.length;
		
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		List<Future<HashMap<Integer, HashMap<Integer, Integer>>>> list = new ArrayList<Future<HashMap<Integer,HashMap<Integer,Integer>>>>();
		
		for(File dir: dirs){
			
			Callable<HashMap<Integer, HashMap<Integer, Integer>>> callable = new LoadFourGramParallel(dir.getAbsolutePath());
			Future<HashMap<Integer, HashMap<Integer, Integer>>> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
            list.add(future);
			
		}
		
		for(Future<HashMap<Integer, HashMap<Integer, Integer>>> fut : list){
            try {
                //print the return value of Future, notice the output delay in console
                // because Future.get() waits for task to get completed
                //System.out.println(fut.get().size());
            	fourgramsIdsAndContexts.putAll(fut.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		System.out.println(fourgramsIdsAndContexts.size());
		
		executor.shutdownNow();
		
		System.out.println("total size="+fourgramsIdsAndContexts.size()+","+new Date().toString());
		
		return fourgramsIdsAndContexts;
	}*/
}

/*class LoadFourGramParallel implements Callable<HashMap<Integer, HashMap<Integer, Integer>>> {
	
	String subDir;
	HashMap<Integer, HashMap<Integer, Integer>> fourGramTokenWithIds;
	
	public LoadFourGramParallel(String subDir){
		this.subDir = subDir;
		this.fourGramTokenWithIds = new HashMap<Integer, HashMap<Integer,Integer>>();
	}
	
	public HashMap<Integer, HashMap<Integer, Integer>> call() throws Exception {
		
		System.out.println(subDir+","+fourGramTokenWithIds.toString()+","+new Date().toString());
		recursivelyLoadTokenizedFiles(subDir);
		return fourGramTokenWithIds;
    }
	
	private void recursivelyLoadTokenizedFiles(String dir) {
        
		try {
            File fDir = new File(dir);
            File[] allFiles = fDir.listFiles();
            if (allFiles == null || allFiles.length == 0) {
                if (fDir.isDirectory()) {
                    fDir.delete();
                }
                return;
            }
            
            List<String> fileNames = new ArrayList<String>();

            for (File f : allFiles) {
                if (f.isDirectory()) {
                	recursivelyLoadTokenizedFiles(f.getAbsolutePath());
                } else {
                    fileNames.add(f.getAbsolutePath());
                }
            }
            
            //fourGramIndexedUnified
            if(fileNames.size()==1){
            	String filename = fileNames.get(0);
            	
            	File fourgFIle = new File(filename);
            	
            	FileInputStream fileIn = new FileInputStream(fourgFIle);
        		DataInputStream in = new DataInputStream(fileIn);
        		
        		int numberOfRecords = in.readInt();
        		//System.out.println("total tokens="+numberOfRecords+","+fourgFIle.getAbsolutePath()+","+new Date().toString());
        		for(int i=0;i<numberOfRecords;i++){
        			
        			int bgId = in.readInt();
        			fourGramTokenWithIds.put(bgId, new HashMap<Integer, Integer>());
        			
        			int numberOfcontexs = in.readInt(); 
        			
        			for(int j=0;j<numberOfcontexs;j++){
        				int bgContextId = in.readInt();
        				int bgContextFreq = in.readInt();
        				
        				fourGramTokenWithIds.get(bgId).put(bgContextId, bgContextFreq);
        			}
        		}
        		
        		in.close();
        		fileIn.close();
            }
            
        } catch (Exception e) {
            System.out.println("getFileSizes=" + e.toString());
        }
    }
	
	
}*/

/*class LoadFourGramParallel implements Callable<HashMap<Integer, HashMap<Integer, Integer>>> {
	
	File file;
	
	public LoadFourGramParallel(File file){
		this.file = file;
	}
	
	public HashMap<Integer, HashMap<Integer, Integer>> call() throws Exception {
		
		HashMap<Integer, HashMap<Integer, Integer>> fourGramTokenWithIds = new HashMap<Integer, HashMap<Integer,Integer>>();
		
		FileInputStream fileIn = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fileIn);
			
		int numberOfRecords = in.readInt();
		System.out.println("total tokens="+numberOfRecords+","+file.getAbsolutePath()+","+new Date().toString());
		
		for(int i=0;i<numberOfRecords;i++){
			
			int bgId = in.readInt();
			fourGramTokenWithIds.put(bgId, new HashMap<Integer, Integer>());
			
			int numberOfcontexs = in.readInt(); 
			
			for(int j=0;j<numberOfcontexs;j++){
				int bgContextId = in.readInt();
				int bgContextFreq = in.readInt();
				
				fourGramTokenWithIds.get(bgId).put(bgContextId, bgContextFreq);
			}
		}
		
		in.close();
		fileIn.close();
		
		
		return fourGramTokenWithIds;
    }
	  
}*/

class LoadFourGramParallel implements Callable<HashMap<Integer, FourgramAsBigramContext>> {
	
	File file;
	
	public LoadFourGramParallel(File file){
		this.file = file;
	}
	
	public HashMap<Integer, FourgramAsBigramContext> call() throws Exception {
		
		HashMap<Integer, FourgramAsBigramContext> fourGramTokenWithIds = new HashMap<Integer, FourgramAsBigramContext>();
		
		FileInputStream fileIn = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fileIn);
			
		int numberOfRecords = in.readInt();
		System.out.println("total tokens="+numberOfRecords+","+file.getAbsolutePath()+","+new Date().toString());
		
		for(int i=0;i<numberOfRecords;i++){
			
			int bgId = in.readInt();
			int bgFreq = in.readInt();
			
			int numberOfcontexs = in.readInt();
			
			FourgramAsBigramContext obj = new FourgramAsBigramContext();
			obj.bgFrq = bgFreq;
			obj.contextIdFreq = new HashMap<Integer, Integer>();
			
			for(int j=0;j<numberOfcontexs;j++){
				int bgContextId = in.readInt();
				int bgContextFreq = in.readInt();
				
				obj.contextIdFreq.put(bgContextId, bgContextFreq);
			}
			
			fourGramTokenWithIds.put(bgId, obj);
		}
		
		in.close();
		fileIn.close();
		
		return fourGramTokenWithIds;
    }
	
}
