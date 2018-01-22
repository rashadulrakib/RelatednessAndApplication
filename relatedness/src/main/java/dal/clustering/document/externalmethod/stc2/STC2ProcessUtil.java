package dal.clustering.document.externalmethod.stc2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class STC2ProcessUtil {
	
	String vocabIndexFile;
	
	HashMap<String, Integer> hmVocabIndex = new HashMap<String, Integer>();
	
	public HashMap<String, Integer> getAlVocabIndex(){
		return hmVocabIndex;
	}
	
	public STC2ProcessUtil(String vocabIndexFile){
		this.vocabIndexFile = vocabIndexFile;
		
		Process();
	}
	
	
	private void Process(){
		try{
			BufferedReader br =  new BufferedReader(new FileReader(vocabIndexFile));
			
			String line="";
			while((line=br.readLine()) != null) {
		        line = line.trim().toLowerCase();
		        if(line.isEmpty()) continue;
		        
		        String [] arr = line.split("\\t");
		        
		        if(arr.length!=2) continue;
		        
		        if(arr[0].trim().isEmpty()) continue;
		        
		        hmVocabIndex.put(arr[0].trim(), Integer.parseInt(arr[1].trim()));
			}
			
			br.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
