package dal.relatedness.phrase.fourgram;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import dal.relatedness.phrase.bigram.LoadBigram;
import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.unigram.LoadUnigram;

public class TokenizeFourGram {
	
	Map<Integer, Map<Integer, Integer>> fourgramsIdsAndContexts;
	private Map<String, Integer> hmUnigramIds;
	private ArrayList<Map<dal.relatedness.phrase.entities.bigram.BigramKey, Integer>> listHmBigramIds;
	ArrayList<Map<Integer, Integer>> allBgOnlyIdFreqList;
	Map<Integer, Integer> allBgOnlyIdFreqMap;
	
	public void tokenizeByIdFreq() throws IOException, InterruptedException, ExecutionException{
		//load bigram by id, freq
		allBgOnlyIdFreqList = new LoadBigram().populateBigramsWithOnlyIdFreqInParallel();
		allBgOnlyIdFreqMap = new HashMap<Integer, Integer>();
		
		for(Map<Integer, Integer> map : allBgOnlyIdFreqList){
			allBgOnlyIdFreqMap.putAll(map);
		}
		
		allBgOnlyIdFreqList.clear();
		allBgOnlyIdFreqList=null;
		
		char ch;
  		for( ch = 'a' ; ch <= 'z' ; ch++ ){
     		String dir = PhraseDirectories.fourGramIndexedTokenizedDir + Character.toString(ch);
			System.out.println(dir);	
			
			fourgramsIdsAndContexts = new HashMap<Integer, Map<Integer,Integer>>();
			
			tokenizeFourgramsToFileByIdFreq(dir);
			System.out.println("Loading TOtal bigram phrases in 4gram with contexts for "+ ch+"="+ fourgramsIdsAndContexts.size());
			
			String fileName = PhraseDirectories.moreTokenizedFourGramDir + Character.toString(ch);
			
			FileOutputStream fileOut = new FileOutputStream(fileName);
    		DataOutputStream  out = new DataOutputStream (fileOut);
    		
    		out.writeInt(fourgramsIdsAndContexts.size());
    		
    		PrintWriter pr = new PrintWriter(PhraseDirectories.googleNgramDir+"bg-only-used-in-4g-"+ch);
    		
    		for(Integer bgId: fourgramsIdsAndContexts.keySet()){
    			
    			pr.println(bgId);
    			
    			out.writeInt(bgId);
    			out.writeInt(allBgOnlyIdFreqMap.get(bgId));
    			
    			out.writeInt(fourgramsIdsAndContexts.get(bgId).keySet().size());
    			
    			for(Integer contextId: fourgramsIdsAndContexts.get(bgId).keySet()){
    				out.writeInt(contextId);
    				out.writeInt(fourgramsIdsAndContexts.get(bgId).get(contextId));
    			}
    		}
    		
    		out.close();
            fileOut.close();
            
            pr.close();
			
            System.out.println("Finished 4g tokenized Writing to" + fileName);
            
			fourgramsIdsAndContexts.clear();
			fourgramsIdsAndContexts=null;
		}
	}
	
	private void tokenizeFourgramsToFileByIdFreq(String dir) {
		
		recursivelyTokenizeFilesByIdFreq(dir);
		
	}
	
	private void recursivelyTokenizeFilesByIdFreq(String dir) {
        
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
                	recursivelyTokenizeFilesByIdFreq(f.getAbsolutePath());
                } else {
                    fileNames.add(f.getAbsolutePath());
                }
            }
            
            //fourGramIndexedUnified
            if(fileNames.size()==1){
            	File file = new File(fileNames.get(0));
            	FileInputStream fileIn = new FileInputStream(file);
        		DataInputStream in = new DataInputStream(fileIn);
        			
        		int numberOfRecords = in.readInt();
        		//System.out.println("total tokens="+numberOfRecords+","+file.getAbsolutePath()+","+new Date().toString());
        		
        		for(int i=0;i<numberOfRecords;i++){
        			
        			int bgId = in.readInt();
        			fourgramsIdsAndContexts.put(bgId, new HashMap<Integer, Integer>());
        			
        			int numberOfcontexs = in.readInt(); 
        			
        			for(int j=0;j<numberOfcontexs;j++){
        				int bgContextId = in.readInt();
        				int bgContextFreq = in.readInt();
        				
        				fourgramsIdsAndContexts.get(bgId).put(bgContextId, bgContextFreq);
        			}
        		}
        		
        		in.close();
        		fileIn.close();
            }
            
        } catch (Exception e) {
            System.out.println("getFileSizes=" + e.toString());
        }
    }

	public boolean tokenize(){
		try{
			
			hmUnigramIds = new LoadUnigram().loadTokenizedUniGramsWithId();
			listHmBigramIds = new LoadBigram().populateBigramsWithIdsInParallel();
			
			//System.out.println(obtainBigramId("zoo keeper"));
			
			//recursively iterate 4m-indexed-unified
			//create map<tokenStr, map<contextSTr, freq>>
			//write a binary file
			
			//recursivelyTokenizeFiles(Directories.fourGramIndexedUnifiedDir);
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}
	
//	private int obtainBigramId(String bigram) {
//		String arr [] = bigram.split("\\s+");
//		
//		if(hmUnigramIds.get(arr[0])==null || hmUnigramIds.get(arr[1])==null){
//			return -1;
//		}
//		
//		int uniId1 = hmUnigramIds.get(arr[0]);
//		int uniId2 = hmUnigramIds.get(arr[1]);
//		
//		for( Map<BigramKey, Integer> bgIds: listHmBigramIds){
//			if(bgIds.get(new BigramKey(uniId1, uniId2))!=null){
//				return bgIds.get(new BigramKey(uniId1, uniId2));
//			}
//		}
//		
//		return -1;
//	}

//	private void recursivelyTokenizeFiles(String dir) {
//        
//		try {
//            File fDir = new File(dir);
//            File[] allFiles = fDir.listFiles();
//            if (allFiles == null || allFiles.length == 0) {
//                if (fDir.isDirectory()) {
//                    fDir.delete();
//                }
//                return;
//            }
//            
//            List<String> fileNames = new ArrayList<String>();
//            String lastDir=dir;
//            for (File f : allFiles) {
//                if (f.isDirectory()) {
//                	recursivelyTokenizeFiles(f.getAbsolutePath());
//                } else {
//                    fileNames.add(f.getAbsolutePath());
//                }
//            }
//            
//            //fourGramIndexedUnified
//            if(fileNames.size()==1){
//            	String filename = fileNames.get(0);
//            	LinkedHashMap<String, Integer> uniques = FourgramUtil.uniqueFourGrams(filename);
//            	LinkedHashMap<String, LinkedHashMap<String, Integer>> phrasesWithContext =
//            			FourgramUtil.mapPhraseWithContext(uniques);
//            	
//                String newDir = lastDir.replaceAll("n-gram-indexed-unified", "n-gram-indexed-tokenized");
//                File fnewDir = new File(newDir);
//                
//                if(!fnewDir.exists()){
//                    fnewDir.mkdirs();
//                }
//                
//                String firstFile = fileNames.get(0);
//                //String newFile = newDir+"\\"+ firstFile.substring(firstFile.lastIndexOf("\\")+1, firstFile.lastIndexOf("\\")+5);
//                String newFile = newDir+"/"+ firstFile.substring(firstFile.lastIndexOf("/")+1, firstFile.lastIndexOf("/")+5);
//                System.out.println(newFile);
//                /*PrintWriter pr = new PrintWriter(newFile);
//                pr.println(phrasesWithContext.size());
//                for(String bgKey: phrasesWithContext.keySet()){
//                	pr.print(bgKey);
//                	LinkedHashMap<String, Integer> contexts = phrasesWithContext.get(bgKey);
//                	pr.print(contexts.size());
//                	for(String context: contexts.keySet()){
//                		pr.print(" "+context+" "+contexts.get(context));
//                	}
//                	pr.println();
//                }
//                
//                pr.close();*/
//                
//                FileOutputStream fileOut = new FileOutputStream(newFile);
//        		DataOutputStream  out = new DataOutputStream (fileOut);
//        		
//        		out.writeInt(phrasesWithContext.size());
//        		
//        		for(String bgKey: phrasesWithContext.keySet()){
//        			out.writeInt(obtainBigramId(bgKey));
//                	LinkedHashMap<String, Integer> contexts = phrasesWithContext.get(bgKey);
//                	out.writeInt(contexts.size());
//                	for(String context: contexts.keySet()){
//                		out.writeInt(obtainBigramId(context));
//                		out.writeInt(contexts.get(context));
//                	}
//                }
//        		
//        		out.close();
//                fileOut.close();
//            }
//            
//        } catch (Exception e) {
//            System.out.println("getFileSizes=" + e.toString());
//        }
//    }
}
