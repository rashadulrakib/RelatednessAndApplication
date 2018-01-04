package dal.computationalintelligence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import dal.utils.common.compute.ComputeUtil;

public class WordEmbeddingVectorProcessor {

	public void ExtractWordEmbeddingVector(String inputAllWordsFile, String InputWordEmbeddingFile, String outputVectorFile){
		try{
			BufferedReader br = new BufferedReader(new FileReader(inputAllWordsFile));
			
			HashSet<String> words = new HashSet<String>(); 
			
			String text;
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	if(text.equals("")) continue;
            	
            	words.add(text);
            }
			
            br.close();
            
            br = new BufferedReader(new FileReader(InputWordEmbeddingFile));
            
            PrintWriter pr = new PrintWriter(outputVectorFile);
            
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	
            	String [] arr = text.split(" ");
            	String EmbeddingWord = arr[0];
            	
            	if(words.contains(EmbeddingWord)){
            		pr.println(text);
            	}
            }
            
            pr.close();
            br.close();
            
            System.out.println("End ExtractWordEmbeddingVector");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public void GenerateWordSimScores(String inputAllWordPairFile, String inputWordVectorFile, String outputWordSimScoreFile){
		try{
			HashMap<String, ArrayList<Double>> wordVectors = new HashMap<String, ArrayList<Double>>();
			
			BufferedReader br = new BufferedReader(new FileReader(inputWordVectorFile));
			
			String text;
			while ((text = br.readLine()) != null) {
				text = text.trim().toLowerCase();
				String [] arr = text.split("\\s+");
				
				String key = arr[0];
				
				ArrayList<Double> numbers = new ArrayList<Double>();
				for(int i=1;i<arr.length;i++){
					numbers.add(Double.parseDouble(arr[i]));
				}
				
				wordVectors.put(key, numbers);
			}
			
			br.close();
			
			br = new BufferedReader(new FileReader(inputAllWordPairFile));
			PrintWriter pr = new PrintWriter(outputWordSimScoreFile);
			
			while ((text = br.readLine()) != null) {
				text = text.trim().toLowerCase();
				String [] arr = text.split(",");
				
				if(arr.length!=2) continue;
				
				String word1 = arr[0];
				String word2 = arr[1];
				
				if(!wordVectors.containsKey(word1) || !wordVectors.containsKey(word2))
				{
					pr.println(word1+","+word2+",0");
					continue;
				}
				
				ArrayList<Double> vec1 = wordVectors.get(word1);
				ArrayList<Double> vec2 = wordVectors.get(word2);
				
				double simScore = ComputeUtil.ComputeCosineSimilarity(vec1, vec2);
				pr.println(word1+","+word2+","+simScore);
				
				if(simScore<=0){
					System.out.println(word1+","+word2+","+simScore);
				}
				
			}
			
			pr.close();
			br.close();
			
			System.out.println("End GenerateWordSimScores");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	
}
