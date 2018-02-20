package dal.clustering.document.shared;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.relatedness.text.compute.w2vec.TextRelatednessW2VecConstant;
import dal.relatedness.text.utils.TextRelatednessW2VecUtil;

public class DocClusterUtilW2Vec {
	
	HashSet<String> uniqueWords;
	DocClusterUtil docClusterUtil;
	
	HashMap<String, double[]> hmW2vec;
	TextRelatednessW2VecUtil textRelatednessUtilW2Vec;
	
	public HashMap<String, double[]> GetW2Vec(){
		return hmW2vec;
	}
	
	public DocClusterUtilW2Vec(HashSet<String> uniqueWords,	DocClusterUtil docClusterUtil) {
		this.uniqueWords = uniqueWords;
		this.docClusterUtil = docClusterUtil;
		
		PopulateW2Vec();
		textRelatednessUtilW2Vec = new TextRelatednessW2VecUtil(hmW2vec);
	}

	private void PopulateW2Vec() {
		try{
			hmW2vec = new HashMap<String, double[]>();
			
			BufferedReader br = new BufferedReader(new FileReader(TextRelatednessW2VecConstant.InputGlobalWordEmbeddingFile));
	           
			String text="";
			
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	
            	String [] arr = text.split("\\s+");
            	String EmbeddingWord = arr[0];
            	
            	if(uniqueWords.contains(EmbeddingWord)){
            		String [] vecs = text.replaceAll(EmbeddingWord, "").trim().split("\\s+");
            		double [] vecDoubles = new double[vecs.length];
            		for(int i=0; i< vecs.length;i++){
            			vecDoubles[i] = Double.parseDouble(vecs[i]);
            		}
            		hmW2vec.put(EmbeddingWord, vecDoubles);
            	}
            }
           
            br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public LinkedHashMap<String, ArrayList<double[]>> populateW2VecDocsList(
			LinkedHashMap<String,ArrayList<String>> docsLabelBodyList) {
		
		LinkedHashMap<String, ArrayList<double[]>> docsW2VecList = new LinkedHashMap<String, ArrayList<double[]>>();
		
		try{
			for(String label : docsLabelBodyList.keySet()){
				ArrayList<String> docs = docsLabelBodyList.get(label);
				
				ArrayList<double[]> w2vecForDocs = new ArrayList<double[]>();
				
				for(String doc: docs){
					double[] w2vecForDoc = docClusterUtil.PopulateW2VecForSingleDoc(doc, hmW2vec);
					w2vecForDocs.add(w2vecForDoc);
				}
				docsW2VecList.put(label, w2vecForDocs);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return docsW2VecList;
	}
	
	public ArrayList<InstanceW2Vec> populateW2VecDocsFlat(ArrayList<String[]> aldocsBodeyLabelFlat) {
		
		ArrayList<InstanceW2Vec> docsW2VecFlat = new ArrayList<InstanceW2Vec>();
		
		try{
			for(String[] bodyLabel: aldocsBodeyLabelFlat){
				String body = bodyLabel[0];
				String label = bodyLabel[1];
				
				InstanceW2Vec instanceW2Vec = new InstanceW2Vec();
				
				instanceW2Vec.OriginalLabel = label;
				instanceW2Vec.Features =  docClusterUtil.PopulateW2VecForSingleDoc(body, hmW2vec);
				instanceW2Vec.Text = body;
				
				docsW2VecFlat.add(instanceW2Vec);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docsW2VecFlat;
	}

//	public double[] PopulateW2VecForSingleDoc(String doc) {
//		
//		double [] avgVec = new double[TextRelatednessW2VecConstant.W2VecDimension];
//		String arr[] = doc.split("\\s+");
//		
//		try{
//		
//			for(String word: arr){
//        		if(hmW2vec.containsKey(word)){
//        			double[] wordVec = hmW2vec.get(word); 
//        			for(int i=0;i<avgVec.length;i++){
//        				avgVec[i]=avgVec[i]+ wordVec[i];
//        			}
//        		}
//        	}
//        	
//        	//averaging avgvec
//        	for(int i=0;i<avgVec.length;i++){
//        		avgVec[i]=avgVec[i]/(double)arr.length;
//        	}
//        	//end averaging avgvec
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return avgVec;
//	}

	public double ComputeTextSimilarityByW2VecFollowingGtm(String centerText, String body) {
		double sim =0;
		try{
			ArrayList<String> doc1 = new ArrayList<String>(Arrays.asList(centerText.split("\\s+")));
			ArrayList<String> doc2 = new ArrayList<String>(Arrays.asList(body.split("\\s+")));
			
			if (doc1.size() > doc2.size()) {
				ArrayList<String> temp = doc1;
				doc1 = doc2;
				doc2 = temp;
			}
			
			HashSet<String> commonWords = docClusterUtil.textUtilShared.GetCommonWords(doc1, doc2);
			ArrayList<String> restDoc1 = docClusterUtil.textUtilShared.RemoveCommonWords(doc1, commonWords);
			ArrayList<String> restDoc2 = docClusterUtil.textUtilShared.RemoveCommonWords(doc2, commonWords);
			
			ArrayList<ArrayList<PairSim>> t1t2simPairList = null;
			
			if (restDoc1.size() > 0 && restDoc2.size() > 0) {
				t1t2simPairList = textRelatednessUtilW2Vec.GetWeightedSimilarityMatrix(restDoc1, restDoc2);

				sim = docClusterUtil.textRelatednessGoogleNgUtil.ComputeSimilarityFromWeightedMatrixBySTD(
						t1t2simPairList,
						commonWords.size(),
						doc1.size(),
						doc2.size(), 
						true);

				if (Double.isNaN(sim)) {
					sim = 0.0;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return sim;
	}
}
