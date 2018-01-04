package dal.relatedness.phrase.utils.fourgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class FourgramUtil {
	
	public static String Get4GramSubDirFromFilePath(String path){
		String subDir = "";
		subDir = path.substring(path.length()-17, path.length()-18+9);
		return subDir;
	}
	
	public static String Get4GramFileName(String path) {
		String fileName = "";
		
		fileName = path.substring(path.length()-9, path.length()-9+4);
		return fileName;
	}
	
	public static String Get4GramFileSuffix(String path) {
		return path.substring(path.lastIndexOf('-')+1);
	}
	
	public static LinkedHashMap<String, Integer> uniqueFourGrams(String filename) throws IOException{
		LinkedHashMap<String, Integer> uniques = new LinkedHashMap<String, Integer>();
		BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
           line = line.trim().toLowerCase();
           if(line.isEmpty()) continue;

           String arr[] = line.split("\\s+");
           if(arr.length!=5) continue;
           
           String fourgm = arr[0]+" "+arr[1]+" "+arr[2]+" "+arr[3];
           if(!uniques.containsKey(fourgm)){
        	   	long fr = Long.parseLong(arr[4].trim());
        	   	int freq= (fr>=Integer.MAX_VALUE) ? Integer.MAX_VALUE: (int)fr;
        	   	uniques.put(fourgm, freq);
           }else{
        	   	long fr = Long.parseLong(arr[4].trim()) + uniques.get(fourgm);
        	   	int freq= (fr>=Integer.MAX_VALUE) ? Integer.MAX_VALUE: (int)fr;
        	   	uniques.put(fourgm, freq);
           }
        }
        br.close();
		return uniques;
	}
	
	public static LinkedHashMap<String, LinkedHashMap<String, Integer>> mapPhraseWithContext(LinkedHashMap<String, Integer> uniques){
		LinkedHashMap<String, LinkedHashMap<String, Integer>> phrasesWithContext = new LinkedHashMap<String, LinkedHashMap<String,Integer>>();
		
		for(String fourGram: uniques.keySet()){
			int freq = uniques.get(fourGram);
			String arr[] = fourGram.split("\\s+");
			
			String bgKey = arr[0] + " " +arr[1];
			String context = arr[2] + " " +arr[3];
			
			if(!phrasesWithContext.containsKey(bgKey)){
				LinkedHashMap<String, Integer> contxtCont = new LinkedHashMap<String, Integer>();
				contxtCont.put(context, freq);
				phrasesWithContext.put(bgKey, contxtCont);
			}else{
				LinkedHashMap<String, Integer> contxtCont = phrasesWithContext.get(bgKey);
				contxtCont.put(context, freq);
				phrasesWithContext.put(bgKey, contxtCont);
			}
		}
		
		return phrasesWithContext;
	}
}
