package dal.clustering.document.externalmethod.stc2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class STC2ProcessUtil {
	
	String vocabIndexFile;
	
	ArrayList<String> allLinesBodyWordIndex;
	ArrayList<String> alLabels;
	ArrayList<String> alBodies;
	
	HashMap<String, Integer> hmVocabIndex = new HashMap<String, Integer>();
	
	public STC2ProcessUtil(String vocabIndexFile){
		this.vocabIndexFile = vocabIndexFile;
		
		Process();
	}
	
	public HashMap<String, Integer> getAlVocabIndex(){
		return hmVocabIndex;
	}
	
	public ArrayList<String> GetAllLinesBodyWordIndex(){
		return allLinesBodyWordIndex;
	}
	
	public ArrayList<String> GetAlLabels(){
		return alLabels;
	}
	
	public ArrayList<String> GetAlBodies(){
		return alBodies;
	}
	
	public void PopulateSTC2BodyGroundtruthLinewordindex(ArrayList<String[]> aldocsBodeyLabelFlat, 
			HashMap<String, Integer> hmVocabIndex, HashMap<String, String> hmLabelIds){
		try{
			
			allLinesBodyWordIndex = new ArrayList<String>();
			alLabels = new ArrayList<String>();
			alBodies = new ArrayList<String>();
			
			for(String [] bodyLabel: aldocsBodeyLabelFlat){
				String body = bodyLabel[0].trim();
				String label = bodyLabel[1].trim();
				
				if(body.isEmpty() || label.isEmpty()) continue;
				
				String arr [] = body.split("\\s+");
				
				StringBuilder lineBodyWordIndx = new StringBuilder(); 

				for(String word: arr){
					if(hmVocabIndex.containsKey(word)){
						lineBodyWordIndx.append(hmVocabIndex.get(word)+" ");
					}
				}
				
				String bodyIndexLine = lineBodyWordIndx.toString().trim();
				
				if(!bodyIndexLine.isEmpty() && bodyIndexLine.length()>0){
					allLinesBodyWordIndex.add(bodyIndexLine);
					alBodies.add(body);
					alLabels.add(hmLabelIds.get(label));
				}
			}
			
			System.out.println("bodys="+ alBodies.size());
			
		}catch(Exception e){
			e.printStackTrace();
		}
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
