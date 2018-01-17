package dal.relatedness.text.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

import dal.clustering.document.shared.DocClusterConstant;

public class TextRelatednessUtil {
	public TextRelatednessUtil(){		
	}
	
	public HashMap<String, Double> hmGTMWordPairSim;
	
	public void LoadGTMWordPairSimilarities() {
		try{
			hmGTMWordPairSim = new HashMap<String, Double>();
			
			String line;
			BufferedReader br = new BufferedReader(new FileReader(DocClusterConstant.GTMScores));
			
			while ((line = br.readLine()) != null) {
				line = line.trim();
				
				String arr [] = line.split(",");
				
				if(arr.length!=3){
					continue;
				}
				
				double score = 0;
				
				try{
					score = Double.parseDouble(arr[2]);
				}				
				catch(Exception e1){
					score = 0;
					System.out.println(line);
					e1.printStackTrace();
					continue;
				}
				
				hmGTMWordPairSim.put(arr[0]+","+ arr[1], score);
			}
            
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
