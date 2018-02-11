package dal.clustering.document.dataset.all.process.w2vec;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;

import dal.clustering.document.dataset.agnews.AgNewsUtil;
import dal.clustering.document.dataset.biomedical.BioMedicalUtil;
import dal.clustering.document.dataset.googlewebsnippets.GooglewebSnippetUtil;
import dal.clustering.document.dataset.stackoverflow.StackOverflowUtil;

public class W2VecAggregator {
	
	AgNewsUtil agNewsUtil;
	BioMedicalUtil bioMedicalUtil;
	GooglewebSnippetUtil googlewebSnippetUtil;
	StackOverflowUtil stackOverflowUtil;
	
	public W2VecAggregator(){
		agNewsUtil = new AgNewsUtil();
		bioMedicalUtil = new BioMedicalUtil();
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		stackOverflowUtil = new StackOverflowUtil();
	}
	
	public void AggregateW2VecsForDatasets(){
		try{
			Set<String> allUniqueWords = agNewsUtil.getUniqueWords();
			allUniqueWords.addAll(bioMedicalUtil.getUniqueWords());
			allUniqueWords.addAll(googlewebSnippetUtil.GetUniqueWords());
			allUniqueWords.addAll(stackOverflowUtil.getUniqueWords());
			
			HashMap<String, double[]> w2vecs = PopulateW2VecGoogle(allUniqueWords);
			
			Writew2vecs(w2vecs);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void Writew2vecs(HashMap<String, double[]> w2vecs) {
		try{
			
			System.out.println("Start writing");
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(W2VecAggregatorConstant.outW2VecGoogleFile));
			
			for(String word: w2vecs.keySet()){
				bw.write(word);
				for(double val: w2vecs.get(word)){
					bw.write(" "+val);
				}
				bw.write("\n");
			}
			
			bw.close();
			
			System.out.println("End writing");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private HashMap<String, double[]> PopulateW2VecGoogle(Set<String> uniqueWords) {
		HashMap<String, double[]> w2vec = new HashMap<String, double[]>();
		try{
			
			System.out.println("Start populating");
			
			BufferedReader br = new BufferedReader(new FileReader(W2VecAggregatorConstant.inputW2VecGoogleFile));
	           
			String text="";
			
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	
            	String [] arr = text.split("\\s+");
            	
            	if(arr.length != W2VecAggregatorConstant.W2VecGoogleDimension + 1){
            		System.out.println("main="+text);
            		continue;
            	}
            	
            	String EmbeddingWord = arr[0];
            	
            	if(uniqueWords.contains(EmbeddingWord)){
            		String [] vecs = text.replaceAll(EmbeddingWord, "").trim().split("\\s+");
            		if(vecs.length!=W2VecAggregatorConstant.W2VecGoogleDimension){
            			System.out.println("vector="+text);
            			continue;
            		}
            		double [] vecDoubles = new double[vecs.length];
            		for(int i=0; i< vecs.length;i++){
            			vecDoubles[i] = Double.parseDouble(vecs[i]);
            		}
            		w2vec.put(EmbeddingWord, vecDoubles);
            	}
            }
           
            br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return w2vec;
	}
}
