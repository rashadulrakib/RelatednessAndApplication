package dal.clustering.document.shared;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import dal.clustering.document.dataset.biomedical.BioMedicalConstant;
import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.clustering.document.shared.entities.PreprocessedContainer;
import dal.clustering.document.shared.sparsification.SparsificationUtil;
import dal.clustering.document.shared.sparsification.SparsificationUtilIterative;
import dal.clustering.document.shared.sparsification.SparsificationUtilRAD;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.text.compute.w2vec.TextRelatednessW2VecConstant;
import dal.relatedness.text.utils.TextRelatednessGoogleNgUtil;
import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.TextUtilShared;
import dal.utils.common.general.UtilsShared;

public class DocClusterUtil {
	
	public TextUtilShared textUtilShared;
	public TextRelatednessGoogleNgUtil textRelatednessGoogleNgUtil;
	public DocClusterUtilTrWpParallel docClusterUtilParallelTrwp;
	public DocClusterUtilTrWpByGtmParallel docClusterUtilTrWpByGtmParallel;
	public DocClusterUtilTfIdfParallel docClusterUtilTfIdfParallel;
	public DocClusterUtilW2VecParallel docClusterUtilW2vecParallel;
	public DocClusterUtilParallelBtmKlVec docClusterUtilParallelBtmKlVec;
	public SparsificationUtil sparsificationUtil;
	public SparsificationUtilIterative sparsificationUtilIterative;
	public SparsificationUtilRAD sparsificationUtilRAD;
	
	
	public DocClusterUtil(){
		textUtilShared = new TextUtilShared();
		textRelatednessGoogleNgUtil = new TextRelatednessGoogleNgUtil(textUtilShared);
		docClusterUtilParallelTrwp = new DocClusterUtilTrWpParallel();
		docClusterUtilW2vecParallel = new DocClusterUtilW2VecParallel();
		docClusterUtilParallelBtmKlVec = new DocClusterUtilParallelBtmKlVec();
		docClusterUtilTrWpByGtmParallel = new DocClusterUtilTrWpByGtmParallel();
		docClusterUtilTfIdfParallel = new DocClusterUtilTfIdfParallel();
		sparsificationUtil = new SparsificationUtil(); 
		sparsificationUtilIterative = new SparsificationUtilIterative();
		sparsificationUtilRAD = new SparsificationUtilRAD();
	}
	
//	public double ComputeSimilarityFromWeightedMatrixBySTD(ArrayList<ArrayList<PairSim>> t1t2simPairList, double common,
//            double t1Size, double t2Size) {
//        double score = 0.0;
//        try {
//            double allMeanSum = 0.0;
//            double cellValue = 0.0;
//
//            for (ArrayList<PairSim> simPairList : t1t2simPairList) {
//
//                double sum = 0.0;
//                for (PairSim ocPS : simPairList) {
//                    double phWordsim = ocPS.value;
//                    sum = sum + phWordsim;
//                }
//                double mean = sum / simPairList.size();
//
//                double sumVariance = 0.0;
//                for (PairSim ocPS : simPairList) {
//                    sumVariance = sumVariance + Math.pow(mean - ocPS.value, 2);
//                }
//
//                double sd = Math.sqrt(sumVariance / simPairList.size());
//
//                double filteredMeanSum = 0.0;
//                double count = 0.0;
//                for (PairSim ocPS : simPairList) {
//                    cellValue = cellValue + ocPS.value;
//                    if (ocPS.value > (mean + sd)) {
//                        filteredMeanSum = filteredMeanSum + ocPS.value;
//                        count++;
//                    }
//                }
//                if (count > 0.0) {
//                    allMeanSum = allMeanSum + filteredMeanSum / count;
//                }
//            }
//            score = (allMeanSum + common) * (t1Size + t2Size) / 2.0 / t1Size / t2Size;
//        } catch (Exception e) {
//        	e.printStackTrace();
//        }
//
//        return score;
//    }
//	
	public LinkedHashMap<String, double[]> ComputeInitialCenters(LinkedHashMap<String, ArrayList<double[]>> trainW2Vecs) {
		LinkedHashMap<String, double[]> InitialCenters = new LinkedHashMap<String, double[]>();

		try{
			for(String label: trainW2Vecs.keySet()){
				int centerSize = trainW2Vecs.get(label).get(0).length;
				double totalDocVecs = trainW2Vecs.get(label).size();
				double [] center = new double[centerSize];
				
				//sum all the vecs
				for(double[] docVec: trainW2Vecs.get(label)){
					for(int i=0;i<docVec.length;i++){
						center [i] = center[i]+docVec[i];
					}
				}
				
				//average centers;
				for(int i=0;i<center.length;i++){
					center[i]=center[i]/totalDocVecs;
				}
				
				InitialCenters.put(label, center);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return InitialCenters;
	}
	

	public double[] FindMinMaxDocFrequency(HashMap<String, Double> docFreqs, int totalDocs) {
		double [] minMaxDocFreq = new double [2];
		
		try{
			double sumFreq = 0;
			
			for(Double docFreq: docFreqs.values()){
				sumFreq=sumFreq+ docFreq;
			}
			
			double avg = sumFreq/docFreqs.size();
			
			double sumOfSquare = 0;
			
			for(Double docFreq: docFreqs.values()){
				sumOfSquare = sumOfSquare + (docFreq-avg)*(docFreq-avg);
			}
			
			 double sd = Math.sqrt(sumOfSquare / docFreqs.size());
			 double upperBound = totalDocs;//Math.sqrt(totalDocs);//(avg + sd);
			 double lowerBound = 1.0; //(avg - sd); //<=2 ? 2: (avg - sd);
			 
			 minMaxDocFreq[0] = lowerBound;
			 minMaxDocFreq[1] = upperBound;
			
		}catch (Exception e) {
            e.printStackTrace();
        }
		return minMaxDocFreq;
	}

	public PreprocessedContainer GetTrainTestDocsLabelBodyAndUniqueWords(double percentageFromEcahCategory, LinkedHashMap<String, ArrayList<String>> hmDocsLabelBody) {
	
		PreprocessedContainer preprocessedContainer = new PreprocessedContainer();
		
		try{
			
			LinkedHashMap<String, ArrayList<String>>  hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
			
			ArrayList<String[]> alTestDocsBodyLabel = new ArrayList<String[]>();
			
			HashSet<String> uniqueWords = new HashSet<String>();
			
			for(String label: hmDocsLabelBody.keySet()){
				ArrayList<String> docs = textUtilShared.PreProcessDocs(hmDocsLabelBody.get(label)) ;
				
				uniqueWords.addAll(textUtilShared.GetUniqeWordsFromListOfDocs(docs));
				
				Collections.shuffle(docs);
				
				double totalDocs = docs.size();
				
				int totalTrainDocs = (int)(percentageFromEcahCategory*totalDocs);
				
				List<String> trainDocs = docs.subList(0, totalTrainDocs);
				hmTrainDocsLabelBody.put(label, new ArrayList<String>( trainDocs));
				
				List<String> testDocs = docs.subList(totalTrainDocs, (int)totalDocs);
				for(String testDoc: testDocs){
					String [] bodyLabel = new String[2];
					bodyLabel[0] = testDoc;
					bodyLabel[1] = label;
					alTestDocsBodyLabel.add(bodyLabel);
				}
			}
			
			Collections.shuffle(alTestDocsBodyLabel);
			
			preprocessedContainer.HmTrainDocsLabelBody = hmTrainDocsLabelBody;
			preprocessedContainer.AlTestDocsBodyLabel = alTestDocsBodyLabel;
			preprocessedContainer.UniqueWords = uniqueWords;
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
		return preprocessedContainer;
	}
	
	public PreprocessedContainer GetTrainTestDocsLabelBodyAndUniqueWords(LinkedHashMap<String, ArrayList<String>> docsLabelBody) {
		PreprocessedContainer preprocessedContainer = new PreprocessedContainer();
		
		try{
			LinkedHashMap<String, ArrayList<String>>  hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
			ArrayList<String[]> alTestDocsBodyLabel = new ArrayList<String[]>();
			HashSet<String> uniqueWords = new HashSet<String>();
			
			for(String label: docsLabelBody.keySet()){
				ArrayList<String> bodies = new ArrayList <String>( docsLabelBody.get(label));
				
				uniqueWords.addAll(textUtilShared.GetUniqeWordsFromListOfDocs(bodies));
				
				Collections.shuffle(bodies);
					
				double totalDocs = bodies.size();
				int totalTrainDocs = 1;
				
				List<String> trainDocs = bodies.subList(0, totalTrainDocs);
				hmTrainDocsLabelBody.put(label, new ArrayList<String>( trainDocs));
				
				List<String> testDocs = bodies.subList(totalTrainDocs, (int)totalDocs);
				for(String testDoc: testDocs){
					String [] bodyLabel = new String[2];
					bodyLabel[0] = testDoc;
					bodyLabel[1] = label;
					alTestDocsBodyLabel.add(bodyLabel);
				}
			}
			
			Collections.shuffle(alTestDocsBodyLabel);
			
			preprocessedContainer.HmTrainDocsLabelBody = hmTrainDocsLabelBody;
			preprocessedContainer.AlTestDocsBodyLabel = alTestDocsBodyLabel;
			preprocessedContainer.UniqueWords = uniqueWords;
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
		return preprocessedContainer;
	}

	public HashMap<String, double[]> PopulateW2Vec(HashSet<String> uniqueWords) {
		HashMap<String, double[]> w2vec = new HashMap<String, double[]>();
		try{
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
	
	public HashMap<String, double[]> PopulateW2VecGoogle(HashSet<String> uniqueWords) {
		HashMap<String, double[]> w2vec = new HashMap<String, double[]>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(TextRelatednessW2VecConstant.InputGoogleWordEmbeddingFile));
	           
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
	
	
	public HashMap<String, double[]> PopulateW2VecBioMedical(HashSet<String> uniqueWords) {
		HashMap<String, double[]> w2vec = new HashMap<String, double[]>();
		try{
			//BufferedReader br = new BufferedReader(new FileReader(TextRelatednessW2VecConstant.InputGlobalWordEmbeddingFile));
			BufferedReader br = new BufferedReader(new FileReader(BioMedicalConstant.BioMedicalBioASQ2018));
	           
			String text="";
			HashSet<String> notFound = new HashSet<String>();
			
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	
            	String [] arr = text.split("\\s+");
            	
            	if(arr.length<20) continue; 
            	
            	String EmbeddingWord = arr[0];
            	
            	if(uniqueWords.contains(EmbeddingWord)){
            		//String [] vecs = text.replaceAll(EmbeddingWord, "").trim().split("\\s+");
            		double [] vecDoubles = new double[arr.length-1];
            		for(int i=1; i< arr.length;i++){
            			vecDoubles[i-1] = Double.parseDouble(arr[i]);
            		}
            		w2vec.put(EmbeddingWord, vecDoubles);
            	}else{
            		notFound.add(EmbeddingWord);
            	}
            }
           
            br.close();
            System.out.println("Ttotal words="+uniqueWords.size()+", notFoundWords="+notFound.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return w2vec;
	}
	
	
	public HashMap<String, double[]> PopulateW2VecBioMedicalStemmed(HashSet<String> uniqueWordsStemmed) {
		HashMap<String, double[]> w2vec = new HashMap<String, double[]>();
		String errorText = "";
		String errorEmbedding="";
		try{
			//BufferedReader br = new BufferedReader(new FileReader(TextRelatednessW2VecConstant.InputGlobalWordEmbeddingFile));
			BufferedReader br = new BufferedReader(new FileReader(BioMedicalConstant.BioMedicalBioASQCombined));
	           
			String text="";

            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	
            	errorText = text;
            	
            	String [] arr = text.split("\\s+");
            	
            	if(arr.length<20) continue; //not a good vector in the embedding file
            	
            	String EmbeddingWord = arr[0];
            	
            	String EmbeddingWordStemmed = StemmingUtil.stemPhrase(EmbeddingWord);
            	
            	if(EmbeddingWordStemmed.isEmpty() || !uniqueWordsStemmed.contains(EmbeddingWordStemmed)) continue;
            	
            	errorEmbedding = EmbeddingWord;
            	//String [] vecs = text.replaceAll(EmbeddingWord, "").trim().split("\\s+");
        		
        		double [] vecDoubles = new double[arr.length-1];
        		for(int i=1; i< arr.length;i++){
        			vecDoubles[i-1] = Double.parseDouble(arr[i]);
        		}
        		w2vec.put(EmbeddingWordStemmed, vecDoubles);            	
            }
           
            br.close();
		}
		catch(Exception e){
			System.out.println("Error="+errorEmbedding+","+errorText);
			e.printStackTrace();
		}
		
		System.out.println("w2vec="+w2vec.size());
		return w2vec;
	}

	public LinkedHashMap<String, ArrayList<double[]>> CreateW2VecForTrainData(LinkedHashMap<String, ArrayList<String>> hmTrainPerLabelBodyPreprocesed,
			HashMap<String, double[]> hmW2Vec) {
		
		LinkedHashMap<String, ArrayList<double[]>> trainW2Vecs = new LinkedHashMap<String, ArrayList<double[]>>();
		
		try{
			for(String label : hmTrainPerLabelBodyPreprocesed.keySet()){
				ArrayList<String> docs = hmTrainPerLabelBodyPreprocesed.get(label);
				
				ArrayList<double[]> w2vecForDocs = new ArrayList<double[]>();
				
				for(String doc: docs){
					double[] w2vecForDoc = PopulateW2VecForSingleDoc(doc, hmW2Vec);
					w2vecForDocs.add(w2vecForDoc);
				}
				trainW2Vecs.put(label, w2vecForDocs);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return trainW2Vecs;
	}

	public double[] PopulateW2VecForSingleDoc(String doc, HashMap<String, double[]> hmW2Vec) {
		
		double [] tempVec = hmW2Vec.values().iterator().next();
		double [] avgVec = new double[tempVec.length];
		
		String arr[] = doc.split("\\s+");
		
		try{
		
			for(String word: arr){
        		if(hmW2Vec.containsKey(word)){
        			double[] wordVec = hmW2Vec.get(word); 
        			for(int i=0;i<avgVec.length;i++){
        				avgVec[i]=avgVec[i]+ wordVec[i];
        			}
        			
        			sumFound++;
        		}
        	}
			
			textLengtSum = textLengtSum+arr.length;
			
			//averaging avgvec
        	for(int i=0;i<avgVec.length;i++){
        		avgVec[i]=avgVec[i]/(double)arr.length;
        	}
        	//end averaging avgvec
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return avgVec;
	}
	
	public double[] PopulateW2VecIDFForSingleDoc(String doc, HashMap<String, double[]> hmW2Vec, 
			HashMap<String, Double> docFreqs, double totalDocs){
		
		double [] tempVec = hmW2Vec.values().iterator().next();
		double [] avgVec = new double[tempVec.length];
		
		String arr[] = doc.split("\\s+");
		
		try{
			
			for(String word: arr){
        		if(hmW2Vec.containsKey(word)){
        			double[] wordVec = hmW2Vec.get(word);
        			//double idfVal = totalDocs/ docFreqs.containsKey(word) ? docFreqs.get(word) ?  1.00;
        			double idfVal = 1;
        			if(docFreqs.containsKey(word)){
        				idfVal = Math.log10(totalDocs/docFreqs.get(word));
        			}
        			for(int i=0;i<avgVec.length;i++){
        				avgVec[i]=avgVec[i]+ wordVec[i]*idfVal;
        			}
        			
        			sumFound++;
        		}
        	}
			
			textLengtSum = textLengtSum+arr.length;
			
			//averaging avgvec
        	for(int i=0;i<avgVec.length;i++){
        		avgVec[i]=avgVec[i]/(double)arr.length;
        	}
        	//end averaging avgvec
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return avgVec;
	}

	double sumFound = 0;
	int textLengtSum =0;
	
	public ArrayList<InstanceW2Vec> CreateW2VecForTestData(ArrayList<String[]> alTestDocsBodyLabelPreprocesed, HashMap<String, double[]> hmW2Vec) {
		
		ArrayList<InstanceW2Vec> testW2Vecs = new ArrayList<InstanceW2Vec>();
		
		try{
			for(String[] bodyLabel: alTestDocsBodyLabelPreprocesed){
				String body = bodyLabel[0];
				String label = bodyLabel[1];
				
				InstanceW2Vec instanceW2Vec = new InstanceW2Vec();
				
				instanceW2Vec.OriginalLabel = label;
				instanceW2Vec.Features = PopulateW2VecForSingleDoc(body, hmW2Vec);
				instanceW2Vec.Text = body;
				
				testW2Vecs.add(instanceW2Vec);
			}
			
			System.out.println("textLengtSum="+textLengtSum+", sumFound="+sumFound);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return testW2Vecs;
	}
	
	public ArrayList<InstanceW2Vec> CreateW2VecIDFForTestData(ArrayList<String[]> alDocLabelFlat,
			HashMap<String, double[]> hmW2Vec, HashMap<String, Double> docFreqs) {
		
		ArrayList<InstanceW2Vec> testW2Vecs = new ArrayList<InstanceW2Vec>();
		
		try{
		
			double totalDocs = alDocLabelFlat.size();
			
			for(String[] bodyLabel: alDocLabelFlat){
				String body = bodyLabel[0];
				String label = bodyLabel[1];
				
				InstanceW2Vec instanceW2Vec = new InstanceW2Vec();
				
				instanceW2Vec.OriginalLabel = label;
				instanceW2Vec.Features = PopulateW2VecIDFForSingleDoc(body, hmW2Vec, docFreqs, totalDocs);
				instanceW2Vec.Text = body;
				
				testW2Vecs.add(instanceW2Vec);
			}
			
			System.out.println("textLengtSum="+textLengtSum+", sumFound="+sumFound);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return testW2Vecs;
	}
	
	public ArrayList<InstanceW2Vec> CreateW2VecForTestData(ArrayList<double[]> vecs) {
		ArrayList<InstanceW2Vec> testW2Vecs = new ArrayList<InstanceW2Vec>();
		
		try{
		
			for(double [] vec: vecs){
				InstanceW2Vec instanceW2Vec = new InstanceW2Vec();
				instanceW2Vec.Features = vec;
				testW2Vecs.add(instanceW2Vec);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return testW2Vecs;
	}

	public LinkedHashMap<String, double[]> ReComputeCenters(LinkedHashMap<String, ArrayList<InstanceW2Vec>> newClusters) {
		
		LinkedHashMap<String, double[]> centers = new LinkedHashMap<String, double[]>();
		
		try{
			for(String label: newClusters.keySet()){
				int centerSize = newClusters.get(label).get(0).Features.length;
				double totalDocVecs = newClusters.get(label).size();
				double [] center = new double[centerSize];
				
				//sum all the vecs
				for(InstanceW2Vec docVec: newClusters.get(label)){
					for(int i=0;i<docVec.Features.length;i++){
						center [i] = center[i]+docVec.Features[i];
					}
				}
				
				//average centers;
				for(int i=0;i<center.length;i++){
					center[i]=center[i]/totalDocVecs;
				}
				
				centers.put(label, center);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return centers;
	}

	public boolean IsConverge(LinkedHashMap<String, double[]> newCenetrs, LinkedHashMap<String, double[]> centers) {
		try{
			for(String label: centers.keySet()){
				
				if(newCenetrs.containsKey(label)){
					double[] newCeneterValues = newCenetrs.get(label);
					double[] centerValues = centers.get(label);
					
					for(int i=0;i<newCeneterValues.length;i++){
						if(newCeneterValues[i]!=centerValues[i]){
							return false;
						}
					}
					
				}else{
					return false;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	//it can be used for single seed or multiple seed
	public LinkedHashMap<String, ArrayList<String>> GetTrainSeedDocuments(
			LinkedHashMap<String, ArrayList<String>> docsLabelBody, int numberOfTrainDocs , int seed) {
		
		LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody  = new LinkedHashMap<String, ArrayList<String>>();
		
		try{
			for(String label: docsLabelBody.keySet()){
				ArrayList<String> bodies = docsLabelBody.get(label);
				Collections.shuffle(bodies, new Random(seed));
				
				ArrayList<String> trainbodies = new ArrayList<String>( bodies.subList(0, numberOfTrainDocs));
			    hmTrainDocsLabelBody.put(label, trainbodies);
			    
			    System.out.println("Seed lable="+label+","+trainbodies.get(trainbodies.size()-1));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return hmTrainDocsLabelBody;
	}
	
//	public LinkedHashMap<String, ArrayList<String>> GetTrainRandomDocuments(
//			ArrayList<String[]> alDocLabelFlat, int numberOfClusters, int seed) {
//		LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody  = new LinkedHashMap<String, ArrayList<String>>();
//		
//		try{
//			Collections.shuffle(alDocLabelFlat, new Random(seed));
//			
//			for(int i=0;i< numberOfClusters;i++){
//				ArrayList<String> doc1 = new ArrayList<String>();
//				doc1.add(alDocLabelFlat.get(i)[0]);
//				hmTrainDocsLabelBody.put(alDocLabelFlat.get(i)[1], doc1);
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return hmTrainDocsLabelBody;
//	}

	public LinkedHashMap<String, InstanceW2Vec> GetInstanceClosestToCentersW2Vec(LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClusters,
			LinkedHashMap<String, double[]> lastCenters) {
		
		LinkedHashMap<String, InstanceW2Vec> instanceClosestToCenters = new LinkedHashMap<String, InstanceW2Vec>();
		
		try{
			for(String label: lastCenters.keySet()){
				double[] center = lastCenters.get(label);
				ArrayList<InstanceW2Vec> insts = lastClusters.get(label);
				
				double minDist = Double.MAX_VALUE;
				InstanceW2Vec minInst = new InstanceW2Vec();
				
				for(InstanceW2Vec inst: insts){
					double dist = ComputeUtil.ComputeEuclidianDistance(center, inst.Features);
					if(minDist>dist){
						minDist = dist;
						minInst = inst;
					}
				}
				
				instanceClosestToCenters.put(label, minInst);
				System.out.println("label="+label+", closest inst="+minInst.Text);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		return instanceClosestToCenters;
	}

	public LinkedHashMap<String, ArrayList<String>> GetInstanceClosestToCentersText(
			ArrayList<InstanceW2Vec> alTestDocsBodyLabel,
			LinkedHashMap<String, double[]> newCenetrsW2Vec, int x) {
		
		LinkedHashMap<String, ArrayList<String>> oneCenters = new LinkedHashMap<String, ArrayList<String>>();
		
		try{
			for(String label: newCenetrsW2Vec.keySet()){
				double[] center = newCenetrsW2Vec.get(label);
				
				double minDist = Double.MAX_VALUE;
				InstanceW2Vec minInst = new InstanceW2Vec();
				
				for(InstanceW2Vec inst: alTestDocsBodyLabel){
					double dist = ComputeUtil.ComputeEuclidianDistance(center, inst.Features);
					if(minDist>dist){
						minDist = dist;
						minInst = inst;
					}
				}
				
//				double maxSim = Double.MIN_VALUE;
//				InstanceW2Vec minInst = new InstanceW2Vec();
//				
//				for(InstanceW2Vec inst: alTestDocsBodyLabel){
//					double sim = ComputeUtil.ComputeCosineSimilarity(center, inst.Features);
//					if(maxSim<sim){
//						maxSim = sim;
//						minInst = inst;
//					}
//				}
				
				ArrayList<String> oneCenter = new ArrayList<String>();
				oneCenter.add(minInst.Text);
				
				oneCenters.put(label, oneCenter);
				System.out.println("label="+label+", closest inst="+minInst.Text);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return oneCenters;
	}
	
	public LinkedHashMap<String, ArrayList<String>> GetInstanceClosestToCentersText(
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> newClustersW2Vec,
			LinkedHashMap<String, double[]> newCenetrsW2Vec) {
		
		LinkedHashMap<String, ArrayList<String>> oneCenters = new LinkedHashMap<String, ArrayList<String>>();
		
		try{
			for(String label: newCenetrsW2Vec.keySet()){
				double[] center = newCenetrsW2Vec.get(label);
				ArrayList<InstanceW2Vec> insts = newClustersW2Vec.get(label);
				
				double minDist = Double.MAX_VALUE;
				InstanceW2Vec minInst = new InstanceW2Vec();
				
				for(InstanceW2Vec inst: insts){
					double dist = ComputeUtil.ComputeEuclidianDistance(center, inst.Features);
					if(minDist>dist){
						minDist = dist;
						minInst = inst;
					}
				}
				
				ArrayList<String> oneCenter = new ArrayList<String>();
				oneCenter.add(minInst.Text);
				
				oneCenters.put(label, oneCenter);
				System.out.println("label="+label+", closest inst="+minInst.Text);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return oneCenters;
	}

	public double[][] ComputeSimilarityMatrixGtm(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilGTM docClusterUtilTextGtm, double[] clusterAssignments) {
		
		double[][] docSimMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		
		try{
			for(int i=0;i< alDocLabelFlat.size();i++){
				System.out.println("doc="+i);
				String text1 = alDocLabelFlat.get(i)[0];
				
				docSimMatrix[i][i] = 1;
				
				for(int j=i+1;j<alDocLabelFlat.size();j++){
					
					if(clusterAssignments[i]!=clusterAssignments[j]){
						continue;
					}
					
					String text2 = alDocLabelFlat.get(j)[0];
					
					docSimMatrix[i][j] = docClusterUtilTextGtm.ComputeTextSimGTM(text1, text2);
					
					docSimMatrix[j][i] = docSimMatrix[i][j];
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
	
	public double[][] ComputeSimilarityMatrixGtm(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilGTM docClusterUtilTextGtm) {
		
		double[][] docSimMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		
		try{
			for(int i=0;i< alDocLabelFlat.size();i++){
				System.out.println("doc="+i);
				String text1 = alDocLabelFlat.get(i)[0];
				
				docSimMatrix[i][i] = 1;
				
				for(int j=i+1;j<alDocLabelFlat.size();j++){
					
					String text2 = alDocLabelFlat.get(j)[0];
					
					docSimMatrix[i][j] = docClusterUtilTextGtm.ComputeTextSimGTM(text1, text2);
					
					docSimMatrix[j][i] = docSimMatrix[i][j];
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
	
	public double[][] ComputeSimilarityMatrixTrWP(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilTrWP docClusterUtilTrWP){
		
		double[][] docSimMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		try{
			
			for(int i=0;i< alDocLabelFlat.size();i++){
				System.out.println("doc="+i);
				String text1 = alDocLabelFlat.get(i)[0];
				
				docSimMatrix[i][i] = 1;
				
				for(int j=i+1;j<alDocLabelFlat.size();j++){
					
					String text2 = alDocLabelFlat.get(j)[0];
					
					docSimMatrix[i][j] = docClusterUtilTrWP.ComputeTextSimTrWp(text1, text2);
					
					docSimMatrix[j][i] = docSimMatrix[i][j];
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
	
	public double[][] ComputeSimilarityMatrixTrWPParallel(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilTrWP docClusterUtilTrWP, int threads){
		
		double[][] docSimMatrix = null;
		try{
			docSimMatrix = docClusterUtilParallelTrwp.ComputeDocumentSimMatrixTrWpParallel(alDocLabelFlat, docClusterUtilTrWP, threads);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
	
	public double[][] ComputeSimilarityMatrixTrWPParallelByGtm(ArrayList<ArrayList<String>> wordsPhrasesEachText,
			DocClusterUtilTrWP docClusterUtilTrWP, int threads){
		double[][] docSimMatrix = null;
		
		try{
			docSimMatrix = docClusterUtilTrWpByGtmParallel.ComputeDocumentSimMatrixTrWpParallelByGtm(wordsPhrasesEachText, docClusterUtilTrWP, threads);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
	
	public double[][] ComputeCosineMatrixW2VecParallel(ArrayList<InstanceW2Vec> testW2Vecs, int threads){
		
		double[][] docSimMatrix = null;
		try{
			docSimMatrix = docClusterUtilW2vecParallel.ComputeDocumentSimMatrixW2VecParallel(testW2Vecs, threads);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
	
	public double[][] ComputeSimilarityMatrixTfIdfParallel(ArrayList<HashMap<String, Double>> docsTfIdfs, int threads){
		
		double[][] docSimMatrix = null;
		try{
			docSimMatrix = docClusterUtilTfIdfParallel.ComputeDocumentSimMatrixTfIdfParallel(docsTfIdfs, threads);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
	
	public double[][] ComputeBTMKLVecParallel(ArrayList<InstanceW2Vec> testW2Vecs, int threads){
		
		double[][] docSimMatrix = null;
		try{
			docSimMatrix = docClusterUtilParallelBtmKlVec.ComputeDocumentSimMatrixBtmKLVecParallel(testW2Vecs, threads);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
	
	public double[][] ComputeEuclidianDistanceMatrixW2Vec(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilW2Vec docClusterUtilW2Vec) {
		
		double[][] docDistanceMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		
		try{
			ArrayList<InstanceW2Vec> insts = docClusterUtilW2Vec.populateW2VecDocsFlat(alDocLabelFlat);
			
			for(int i=0;i< insts.size();i++){
				
				InstanceW2Vec inst1 = insts.get(i);
				
				docDistanceMatrix[i][i] = 0;
				
				for(int j=i+1;j<insts.size();j++){
					
					InstanceW2Vec inst2 = insts.get(j);
					
					docDistanceMatrix[i][j] = ComputeUtil.ComputeEuclidianDistance(inst1.Features, inst2.Features);
					
					docDistanceMatrix[j][i] = docDistanceMatrix[i][j];
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docDistanceMatrix;
	}
	
	
	public double[][] ComputeCosineMatrixW2Vec(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilW2Vec docClusterUtilW2Vec) {
		
		double[][] docDistanceMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		
		try{
			ArrayList<InstanceW2Vec> insts = docClusterUtilW2Vec.populateW2VecDocsFlat(alDocLabelFlat);
			
			for(int i=0;i< insts.size();i++){
				
				InstanceW2Vec inst1 = insts.get(i);
				
				System.out.println("doc="+i);
				
				docDistanceMatrix[i][i] = 1;
				
				for(int j=i+1;j<insts.size();j++){
					
					InstanceW2Vec inst2 = insts.get(j);
					
					docDistanceMatrix[i][j] = ComputeUtil.ComputeCosineSimilarity(inst1.Features, inst2.Features);
					
					docDistanceMatrix[j][i] = docDistanceMatrix[i][j];
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docDistanceMatrix;
	}
	
	public double[][] ComputeCosineMatrixW2Vec(ArrayList<String[]> alDocLabel,
			DocClusterUtilW2Vec docClusterUtilW2Vec, double[] clusterAssignments) {
		
		double[][] docDistanceMatrix = UtilsShared.InitializeMatrix(alDocLabel.size(), alDocLabel.size());
		
		try{
			
			ArrayList<InstanceW2Vec> insts = docClusterUtilW2Vec.populateW2VecDocsFlat(alDocLabel);
			
			for(int i=0;i< insts.size();i++){
				
				System.out.println("doc="+i);
				
				InstanceW2Vec inst1 = insts.get(i);
				
				docDistanceMatrix[i][i] = 1;
				
				for(int j=i+1;j<insts.size();j++){
					
					if(clusterAssignments[i]!=clusterAssignments[j]){
						continue;
					}
					
					InstanceW2Vec inst2 = insts.get(j);
					
					docDistanceMatrix[i][j] = ComputeUtil.ComputeCosineSimilarity(inst1.Features, inst2.Features);
					
					docDistanceMatrix[j][i] = docDistanceMatrix[i][j];
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docDistanceMatrix;
	}
	
	public ArrayList<String[]> SampledDocsPerCategory(ArrayList<String[]> alDocLabelFlat, int docsPerCategory, int seed) {
		
		ArrayList<String[]> sampledList = new ArrayList<String[]>();
		
		HashMap<String, ArrayList<String[]>> hmBodyLabel = new LinkedHashMap<String, ArrayList<String[]>>();
		
		try{
			for(String[] bodyLabel: alDocLabelFlat){
				String body = bodyLabel[0];
				String label = bodyLabel[1];
				
				if(!hmBodyLabel.containsKey(label)){
					ArrayList<String[]> al = new ArrayList<String[]>();
					al.add(bodyLabel);
					hmBodyLabel.put(label, al);
				}else{
					ArrayList<String[]> al = hmBodyLabel.get(label);
					al.add(bodyLabel);
					hmBodyLabel.put(label, al);
				}
			}
			
			Random rd = new Random(seed);
			
			for(String label: hmBodyLabel.keySet()){
				ArrayList<String[]> al = hmBodyLabel.get(label);
				Collections.shuffle(al, rd);
				docsPerCategory = al.size()>= docsPerCategory? docsPerCategory: al.size();
				sampledList.addAll(al.subList(0, docsPerCategory));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sampledList;
	}
	
	public HashMap<String, ArrayList<InstanceW2Vec>> GetClusterGroupsVectorOriginalLabel(ArrayList<InstanceW2Vec> instants) {
		
		HashMap<String, ArrayList<InstanceW2Vec>> clusterGroups = new HashMap<String, ArrayList<InstanceW2Vec>>();
		
		try{
			for(InstanceW2Vec instant: instants){
				if(!clusterGroups.containsKey(instant.OriginalLabel)){
					ArrayList<InstanceW2Vec> al = new ArrayList<InstanceW2Vec>();
					al.add(instant);
					clusterGroups.put(instant.OriginalLabel, al);
				}
				else{
					ArrayList<InstanceW2Vec> al = clusterGroups.get(instant.OriginalLabel);
					al.add(instant);
					clusterGroups.put(instant.OriginalLabel, al);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clusterGroups;
	}
	
	public HashMap<String, ArrayList<InstanceText>> GetClusterGroupsTextOriginalLabel(ArrayList<InstanceText> instants) {
		
		HashMap<String, ArrayList<InstanceText>> clusterGroups = new HashMap<String, ArrayList<InstanceText>>();

		try{
			for(InstanceText instant: instants){
				if(!clusterGroups.containsKey(instant.OriginalLabel)){
					ArrayList<InstanceText> al = new ArrayList<InstanceText>();
					al.add(instant);
					clusterGroups.put(instant.OriginalLabel, al);
				}
				else{
					ArrayList<InstanceText> al = clusterGroups.get(instant.OriginalLabel);
					al.add(instant);
					clusterGroups.put(instant.OriginalLabel, al);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clusterGroups;
	}

	public LinkedHashMap<String, ArrayList<String>> GetRandomDocuments(	ArrayList<String []> alDocLabelFlat, int seed, int numberofclusters) {
		LinkedHashMap<String, ArrayList<String>> trainDocs = new LinkedHashMap<String, ArrayList<String>>();
		
		try{
			
			ArrayList<String []> newList = new ArrayList<String[]>(alDocLabelFlat);
			Collections.shuffle(newList, new Random(seed));
			
			for(int i=0;i<numberofclusters;i++){
				
				ArrayList<String> al = new ArrayList<String>();
				al.add(newList.get(i)[0]);
				
				trainDocs.put(Integer.toString(i), al);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return trainDocs;
	}

	public double[] PopulateClusterAssignment(Set<String> keySet,
			ArrayList<InstanceText> alInstanceText) {
		
		double[] assigns = new double[alInstanceText.size()];
		
		try{
			HashMap<String, Integer> hmKeyInds = new LinkedHashMap<String, Integer>();
			int kid = 0;
			for(String key: keySet){
				hmKeyInds.put(key, kid++);
			}
			
			int insId = 0;
			for(InstanceText instTex: alInstanceText){
				int keyId = hmKeyInds.get(instTex.ClusteredLabel);
				assigns[insId++] = keyId;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return assigns;
	}

	public double[] PopulateClusterAssignment(ArrayList<InstanceW2Vec> alInstanceW2Vec, Set<String> keySet) {
		double[] assigns = new double[alInstanceW2Vec.size()];
		
		try{
			HashMap<String, Integer> hmKeyInds = new LinkedHashMap<String, Integer>();
			int kid = 0;
			for(String key: keySet){
				hmKeyInds.put(key, kid++);
			}
			
			int insId = 0;
			for(InstanceW2Vec instW2Vec: alInstanceW2Vec){
				int keyId = hmKeyInds.get(instW2Vec.ClusteredLabel);
				assigns[insId++] = keyId;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return assigns;
	}

	public InstanceW2Vec ComputeCenterInstanceW2Vec(ArrayList<InstanceW2Vec> instancew2Vecs) {
		InstanceW2Vec center = new InstanceW2Vec();
		try{
			int vecSize = instancew2Vecs.get(0).Features.length;
			double centerVec [] = new double[vecSize];
			for(InstanceW2Vec inst: instancew2Vecs){
				for(int i=0;i<inst.Features.length;i++){
					centerVec[i]=centerVec[i]+inst.Features[i];
				}
			}
			
			for(int i=0;i<vecSize;i++){
				centerVec[i]=centerVec[i]/instancew2Vecs.size();
			}
			
			center.Features = centerVec;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return center;
	}

	public InstanceW2Vec ComputeMaxInstanceW2Vec(ArrayList<InstanceW2Vec> instancew2Vecs) {
		InstanceW2Vec maxVec = new InstanceW2Vec();
		try{
			int vecSize = instancew2Vecs.get(0).Features.length;
			double max [] = new double[vecSize];
			
			for(InstanceW2Vec inst: instancew2Vecs){
				for(int i=0;i<inst.Features.length;i++){
					if(max[i]<inst.Features[i]){
						max[i]= inst.Features[i];
					}
				}
			}
			
			maxVec.Features = max;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return maxVec;
	}

	public InstanceW2Vec ComputeWeightCenterInstanceW2Vec(InstanceW2Vec centerVec, InstanceW2Vec maxVec) {
		InstanceW2Vec avgMaxVec = new InstanceW2Vec();
		try{
			int vecSize = centerVec.Features.length;
			double vec [] = new double[vecSize];
			
			for(int i=0;i<centerVec.Features.length && i< maxVec.Features.length;i++){
				vec[i] = 2*centerVec.Features[i]*maxVec.Features[i]/(centerVec.Features[i]+maxVec.Features[i]);
			}
			
			avgMaxVec.Features = vec;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return avgMaxVec;
	}

	public ArrayList<InstanceW2Vec> CreateTfIdfVecForTestData(ArrayList<HashMap<String, Double>> docsTfIdfs,
			HashSet<String> uniqueWords) {
		
		ArrayList<InstanceW2Vec> insts = new ArrayList<InstanceW2Vec>();
		
		try{
		
			for(int i=0;i<docsTfIdfs.size();i++){
				HashMap<String, Double> hmtfidfs = docsTfIdfs.get(i);
				double ftrs [] = new double [uniqueWords.size()];
				
				int j=0;
				for(String ftrWord: uniqueWords){
					if(hmtfidfs.containsKey(ftrWord)){
						ftrs[j] = hmtfidfs.get(ftrWord); 
					}
					j++;
				}
				
				InstanceW2Vec inst = new InstanceW2Vec();
				inst.Features = ftrs;
				
				insts.add(inst);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return insts;
	}

	public double[] ComputeSimCenterToDocsTfIdf(ArrayList<HashMap<String, Double>> docsTfIdfs, HashMap<String, Double> hmCenterVecTfIdf) {
		double [] centTodocs = new double [docsTfIdfs.size()];
		try{
			
			double centSqrt = ComputeUtil.ComputeVecSqrt(hmCenterVecTfIdf);
			int i=0;
			for(HashMap<String, Double> docTfIdf : docsTfIdfs){
				double sim = ComputeUtil.ComputeCosineSimilarity(docTfIdf, hmCenterVecTfIdf, centSqrt);
				centTodocs[i++] = sim;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return centTodocs;
	}

}
