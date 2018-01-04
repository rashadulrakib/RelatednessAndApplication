package dal.computationalintelligence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class WordPhrasePairProcessor {

	public void ExtractAndMergeWordPhrasePairWithSimScores(String inputSemEvalFileWithScore, String inputW2VecFileWithScore, 
			String outputWordPhrasePairWithScores ){
		try{
			BufferedReader br = new BufferedReader(new FileReader(inputSemEvalFileWithScore));
			PrintWriter pr = new PrintWriter(outputWordPhrasePairWithScores);
			
			String text;
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	if(text.equals("")) continue;
            	
            	String [] arr = text.split(",");
            	if(arr.length<3) continue;
            	
            	int tk1Length= arr[0].split("\\s+").length;
            	int tk2length= arr[1].split("\\s+").length;
            	
            	if(tk2length<2 && tk1Length<2){
            		continue;
            	}
            	
            	pr.println(arr[0]+","+arr[1]+","+arr[2]);
            }
			
			br.close();
			
			br = new BufferedReader(new FileReader(inputW2VecFileWithScore));
			
			while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	if(text.equals("")) continue;
            	
            	String [] arr = text.split(",");
            	if(arr.length<3) continue;
            	
            	int tk1Length= arr[0].split("\\s+").length;
            	int tk2length= arr[1].split("\\s+").length;
            	
            	if(tk2length!=1 || tk1Length!=1){
            		continue;
            	}
            	
            	pr.println(arr[0]+","+arr[1]+","+arr[2]);
            }
			
			br.close();
			
			pr.close();
			
			System.out.println("End ExtractAndMergeWordPhrasePairWithSimScores");
			
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
}

