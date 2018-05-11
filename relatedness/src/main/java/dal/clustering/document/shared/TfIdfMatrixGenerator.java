package dal.clustering.document.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;


public class TfIdfMatrixGenerator {

	public ArrayList<HashMap<String, Double>> ConstructTfIdfList(List<List<String>> documents ){
		ArrayList<HashMap<String, Double>> tfIdfs= null;
		try{
			
			Set<String> uniqueterms = GetUniqueTerms(documents);
			HashMap<String, Double> docFreqs  = CalculateDocFrequency(uniqueterms, documents) ;
			tfIdfs = CalculateTfIdfMatrix(docFreqs, documents);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return tfIdfs;
	}
	
	public ArrayList<HashMap<String, Double>> ConstructTfIdfList(List<List<String>> documents, Set<String> uniqueterms){
		ArrayList<HashMap<String, Double>> tfIdfs= null;
		try{			
			HashMap<String, Double> docFreqs  = CalculateDocFrequency(uniqueterms, documents) ;
			tfIdfs = CalculateTfIdfMatrix(docFreqs, documents);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return tfIdfs;
	}

	private ArrayList<HashMap<String, Double>> CalculateTfIdfMatrix(HashMap<String, Double> docFreqs, List<List<String>> documents) {
		ArrayList<HashMap<String, Double>> tfIdfs = new ArrayList<HashMap<String,Double>>();
		try{
			for(List<String> doc: documents){
				HashMap<String, Double> termFreqs = new HashMap<String, Double>();
				
				for(String term: doc){
					if(termFreqs.containsKey(term)){
						termFreqs.put(term, termFreqs.get(term)+1 );
					}
					else{
						termFreqs.put(term, 1.0);
					}
				}
				
				double totalTerms = doc.size();
				
				HashMap<String, Double> tfidfSingleDoc = new HashMap<String, Double>();
				
				for(String term: termFreqs.keySet()){
					double tf = termFreqs.get(term);
					double  normalizedTfidf = tf/totalTerms*(1+ Math.log10((double)documents.size()/docFreqs.get(term)));
					tfidfSingleDoc.put(term, normalizedTfidf);
				}
				
				tfIdfs.add(tfidfSingleDoc);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return tfIdfs;
	}

	private Set<String> GetUniqueTerms(List<List<String>> documents) {
		Set<String> uniqueterms = new HashSet<String>();
		try{
			for(List<String> doc: documents){
				uniqueterms.addAll(doc);
			}
		}
		catch(Exception e){ 
			e.printStackTrace();
		}
		
		return uniqueterms;
	}

	public HashMap<String, Double> CalculateDocFrequency(Set<String> uniqueterms, List<List<String>> documents) {
		HashMap<String, Double> docFreqs = new HashMap<String, Double>();
		
		try{
			for(String term: uniqueterms){
				int docFreqCount = 0;
				for(List<String> doc : documents){
					if(doc.contains(term)) {
						docFreqCount++;
					}
				}
				
				docFreqs.put(term, (double)docFreqCount);
			}
		}
		catch(Exception e){ 
			e.printStackTrace();
		}
		
		return docFreqs;
	}

	public HashMap<String, Double> ConstructCenterVecTfIdf(	ArrayList<HashMap<String, Double>> docsTfIdfs) {
		HashMap<String, Double> hmCenter = new LinkedHashMap<String, Double>();
		
		try{
			System.out.println("ConstructCenterVecTfIdf start");
			
			for(HashMap<String, Double> docTfIdf: docsTfIdfs){
				for(String ftr: docTfIdf.keySet()){
					if(hmCenter.containsKey(ftr)){
						hmCenter.put(ftr, hmCenter.get(ftr) + docTfIdf.get(ftr));
					}else{
						hmCenter.put(ftr, docTfIdf.get(ftr));
					}
				}
			}
			
			int totalDoc = docsTfIdfs.size();
			
			for(String ftr: hmCenter.keySet()){
				hmCenter.put(ftr, hmCenter.get(ftr)/totalDoc);
			}
			
			System.out.println("ConstructCenterVecTfIdf end");
			
		}catch(Exception e){ 
			e.printStackTrace();
		}
		
		return hmCenter;
	}

	public HashMap<String, Double> ComputeMaxInstanceTfIdf(ArrayList<HashMap<String, Double>> docsTfIdfs) {
		HashMap<String, Double> maxVec = new LinkedHashMap<String, Double>();
		
		try{
			
			System.out.println("ComputeMaxInstanceTfIdf start");
		
			for(HashMap<String, Double> docTfIdf: docsTfIdfs){
				for(String ftr: docTfIdf.keySet()){
					if(maxVec.containsKey(ftr)){
						maxVec.put(ftr, Math.max(maxVec.get(ftr), docTfIdf.get(ftr))  );
					}else{
						maxVec.put(ftr, docTfIdf.get(ftr));
					}
				}
			}
			
			System.out.println("ComputeMaxInstanceTfIdf end");
			
		}catch(Exception e){ 
			e.printStackTrace();
		}
		
		return maxVec;
	}

	public HashMap<String, Double> ComputeWeightCenterInstanceTfIdf(HashMap<String, Double> hmCenterVecTfIdf,
			HashMap<String, Double> maxVec) {
		
		HashMap<String, Double> weightMaxVec = new LinkedHashMap<String, Double>();
		
		try{
			
			System.out.println("ComputeWeightCenterInstanceTfIdf start");
			
			for(String ftr: hmCenterVecTfIdf.keySet()){
				if(maxVec.containsKey(ftr)){
					double weightFtr = 2*maxVec.get(ftr)*hmCenterVecTfIdf.get(ftr)
							/(maxVec.get(ftr)+hmCenterVecTfIdf.get(ftr));
					weightMaxVec.put(ftr, weightFtr);
				}
				else{
					weightMaxVec.put(ftr, hmCenterVecTfIdf.get(ftr));
				}
			}
			
			System.out.println("ComputeWeightCenterInstanceTfIdf end");
			
		}catch(Exception e){ 
			e.printStackTrace();
		}

		return weightMaxVec;
	}
	
}
