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

import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.clustering.document.shared.entities.PreprocessedContainer;
import dal.relatedness.text.compute.w2vec.TextRelatednessW2VecConstant;
import dal.relatedness.text.utils.TextRelatednessGoogleNgUtil;
import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.TextUtilShared;
import dal.utils.common.general.UtilsShared;

public class DocClusterUtil {
	
	public TextUtilShared textUtilShared;
	public TextRelatednessGoogleNgUtil textRelatednessGoogleNgUtil;
	
	public DocClusterUtil(){
		textUtilShared = new TextUtilShared();
		textRelatednessGoogleNgUtil = new TextRelatednessGoogleNgUtil(textUtilShared);
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

	private double[] PopulateW2VecForSingleDoc(String doc, HashMap<String, double[]> hmW2Vec) {
		
		double [] avgVec = new double[TextRelatednessW2VecConstant.W2VecDimension];
		String arr[] = doc.split("\\s+");
		
		try{
		
			for(String word: arr){
        		if(hmW2Vec.containsKey(word)){
        			double[] wordVec = hmW2Vec.get(word); 
        			for(int i=0;i<avgVec.length;i++){
        				avgVec[i]=avgVec[i]+ wordVec[i];
        			}
        		}
        	}
        	
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
		}
		catch(Exception e){
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

	public double[][] SparsifyDocDisSimilarityMatrix(double[][] docSimMatrix) {
		double[][] sparsifySimMatrix = new double[docSimMatrix.length][];
		
		try{
			int mulConst = 1;
			int pvalCount = 0;
			int WvalCount = 0;
			int bothCOunt =0;
			
			int pvalAndersonCount = 0;
			int wvalAndersonCount = 0;
			int bothAndersonCount = 0;
			
			for(int i=0;i<docSimMatrix.length;i++){
				double simArr [] = docSimMatrix[i];
				
				double simSim = 0.0;
				for(int j=0;j<simArr.length;j++){
					simSim= simSim + simArr[j]*mulConst;
				}
				
				double simArrTest [] = new double[simArr.length];
				ArrayList<Integer> alsimArrTest = new  ArrayList<Integer>();
				for(int j=0;j<simArr.length;j++){
					//simArrTest[j] = (int)(simArr[j]*mulConst);
					alsimArrTest.add((int)(simArr[j]*mulConst));
				}
				
				Collections.sort(alsimArrTest);
				
				for(int j=0;j<alsimArrTest.size();j++){
					simArrTest[j] = alsimArrTest.get(j);
				}
				
				//check normally distributed or not
				double Wval = 0;//NormalityTest.shapiro_wilk_statistic(simArrTest);
				double Pval = 0;//NormalityTest.shapiro_wilk_pvalue(Wval, simArr.length);
				
				
				double WvalAnderson = 0;//NormalityTest.anderson_darling_statistic(simArrTest);
				double PvalAnderson = 0;//NormalityTest.anderson_darling_pvalue(WvalAnderson, simArr.length);
				
				
				if(Pval>=0.05){
					pvalCount++;
				}
				if(Wval>=0.99){
					WvalCount++;
				}
				if(Pval>=0.05 && WvalCount>=0.99){
					bothCOunt++;
				}
				
				if(PvalAnderson>=0.05){
					pvalAndersonCount++;
				}
				if(WvalAnderson>=0.99){
					wvalAndersonCount++;
				}
				
				if(PvalAnderson>=0.05 && WvalAnderson>=0.99){
					bothAndersonCount++;
				}
				
				//
				
				double avgSimSum = simSim/ simArr.length;
				
				double varianceSum = 0;
				
				for(int j=0;j<simArr.length;j++){
					varianceSum = varianceSum + (simArr[j]*mulConst-avgSimSum)*(simArr[j]*mulConst-avgSimSum);
				}
				
				double sd = Math.sqrt(varianceSum/simArr.length);
				
				//filter sim
				int zeroCount = 0;
				for(int j=0;j<simArr.length;j++){
					
					//double newSim = simArr[j];
					//if(Pval>=0.5 || Wval>=0.99)
					
					//double newSim = simArr[j]*mulConst> avgSimSum*0.01+ sd*0 ? simArr[j]: 0; //0.7 best for stack
						//System.out.println("newSim="+newSim);
					
					//double newSim = simArr[j]*mulConst> avgSimSum+ sd*1.3 ? simArr[j]: 0; //0.7 best for stack
					//double newSim = simArr[j]*mulConst> avgSimSum- sd*0.0 ? Double.MAX_VALUE: simArr[j];
					 double newSim = simArr[j];
					 if( newSim==0){
						 zeroCount++;
					 }
					 simArr[j] = 1-newSim;
					 //simArr[j] = newSim;
				}
				
				System.out.println("total="+simArr.length+", zero count="+ zeroCount+",Wval="+Wval+",Pval="+Pval);
				//System.out.println("total="+simArr.length+", zero count="+ zeroCount+",Wval="+Wval);
				sparsifySimMatrix[i] = simArr;
			}
			
			System.out.println("pcount="+ pvalCount+", wcount="+ WvalCount+", bothcount="+ bothCOunt+": pander="+  pvalAndersonCount+", wvalander="+ wvalAndersonCount+", bothAnder="+ bothAndersonCount);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifySimMatrix;
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

}
