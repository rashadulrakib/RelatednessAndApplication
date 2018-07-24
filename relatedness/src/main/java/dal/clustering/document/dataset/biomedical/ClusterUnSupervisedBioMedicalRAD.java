package dal.clustering.document.dataset.biomedical;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.UtilsShared;

public class ClusterUnSupervisedBioMedicalRAD extends ClusterBioMedical{
	
	public void ClusterDocsByFrequentTerms(){
		try{
			
			int itemsPerCluster = bioMedicalUtil.GetBiomedicalStemmedDocuments().size() / BioMedicalConstant.NumberOfClusters;
			
			//List<Set<String>> stemmedDocumnetsUniqueTerms = bioMedicalUtil.GetBiomedicalStemmedDocumnetsUniqueTerms();
			
			for(List<String> stemeddoc: bioMedicalUtil.GetBiomedicalStemmedDocuments()){
				System.out.println(stemeddoc);
			}
			
			HashMap<String, Integer> docFreqs = tfIdfMatrixGenerator.CalculateDocFrequency(bioMedicalUtil.getUniqueWords(), bioMedicalUtil.GetBiomedicalStemmedDocuments());

			for(String term: docFreqs.keySet()){
				System.out.println(term+"="+docFreqs.get(term));
			}
			
	        Set<Entry<String, Integer>> entries = docFreqs.entrySet();
	        
	        Comparator<Entry<String, Double>> valueComparator = new Comparator<Entry<String,Double>>() {
	            
	            @Override
	            public int compare(Entry<String, Double> e1, Entry<String, Double> e2) {
	            	Double v1 = e1.getValue();
	            	Double v2 = e2.getValue();
	                return v1.compareTo(v2);
	            }
	        };
	
	        List<Entry<String, Double>> listOfEntries = new ArrayList<Entry<String, Double>>();
	        Collections.sort(listOfEntries, valueComparator);
	        LinkedHashMap<String, Double> sortedByValue = new LinkedHashMap<String, Double>(listOfEntries.size());
	        for(Entry<String, Double> entry : listOfEntries){
	            sortedByValue.put(entry.getKey(), entry.getValue());
	        }
	        
	        TreeMap<Double, String> freqDiff = new TreeMap<Double, String>();
	        
	        System.out.println("HashMap after sorting entries by values ");
	        Set<Entry<String, Double>> entrySetSortedByValue = sortedByValue.entrySet();	        
	        for(Entry<String, Double> mapping : entrySetSortedByValue){
	            System.out.println(mapping.getKey() + " ==> " + mapping.getValue());
	            freqDiff.put(Math.abs(mapping.getValue()-itemsPerCluster), mapping.getKey());
	        }
	        
	        
	        System.out.println("HashMap after sorting diff by values ");
	        for(Double diff: freqDiff.keySet()){
	        	System.out.println(freqDiff.get(diff)+","+diff);
	        }
	        
	        ///select top N=#clustersX10  terms from freqDiff
	        ///take a term and generate pairs with previous top items 
	        ///for that term, if support of any pair > 1% support, ignore that term. otherwise accept that term and kepp continuing untill we get #terms=#clusters
	        ///among the selected 20 terms, we can use top suitable terms to fix some clusters.
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//	public void GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification() {
//		try{
//			LinkedHashMap<String, ArrayList<String>> docsLabelBody = bioMedicalUtil.getDocsBiomedicalList();
//			ArrayList<String []> alDocLabelFlat = bioMedicalUtil.getDocsBiomedicalFlat();
//			
//			
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	public void GenerateDocSimMatrixW2VecStemmed() {
		try{
			ArrayList<String []> alDocLabelFlatStemmed =bioMedicalUtil.GetDocsBiomedicalFlatStemmed();
			
			HashMap<String, double[]> hmW2VecStemmed = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedicalStemmed(bioMedicalUtil.GetUniqueWordsStemmed());
			
			ArrayList<InstanceW2Vec> testW2Vecs = bioMedicalUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlatStemmed, hmW2VecStemmed);			
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);

			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedical-w2vec-bioASQ-sim-20000-stemmed", docSimMatrix, " ");

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocSimMatrixDoc2Vec(){
		try{
			//ArrayList<InstanceW2Vec> testW2Vecs = bioMedicalUtil.doc2VecUtil.CreateDoc2VecInstances("/users/grad/rakib/doc2vec/doc2vec-100", bioMedicalUtil.getDocsBiomedicalFlat(), ",");
			ArrayList<InstanceW2Vec> testW2Vecs = bioMedicalUtil.doc2VecUtil.CreateDoc2VecInstances("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/t80-800", bioMedicalUtil.getDocsBiomedicalFlat(), " ");
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);

			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/biomedical-tfidf-sim-16000-800", docSimMatrix, " ");

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification(){
		try{
			String simFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedical-doc2vec100-sim-20000";	
			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
			
			double [][] docDisSimMatrixDense = UtilsShared.CopyMatrix(docSimMatrix, true);
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/2n-biomedical-doc2vec100-dense-20000", docDisSimMatrixDense, " ");
			
			double [][] docDisSimMatrixSparse = bioMedicalUtil.docClusterUtil.sparsificationUtilRAD.SparsifyDocDisSimilarityMatrixAlgorithomicExactIterative(docSimMatrix, BioMedicalConstant.NumberOfClusters);
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/2n-biomedical-doc2vec100-sparse-20000", docDisSimMatrixSparse, " ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateDocSimMatrixW2VecPrunedFtrs() {
		try{
			ArrayList<String []> alDocLabelFlat =bioMedicalUtil.getDocsBiomedicalFlat();			
			HashMap<String, double[]> hmW2Vec = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedical(bioMedicalUtil.getUniqueWords());
			
			HashMap<String, Integer> docFreqs = tfIdfMatrixGenerator.CalculateDocFrequency(bioMedicalUtil.getUniqueWords(), bioMedicalUtil.GetBiomedicalDocuments());			
			ArrayList<String []> alDocLabelFlatPruned = bioMedicalUtil.docClusterUtil.textUtilShared.StatisticalPruneWordsByDocFreqs(docFreqs, alDocLabelFlat, 950); 
			
			ArrayList<InstanceW2Vec> testW2Vecs = bioMedicalUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlatPruned, hmW2Vec);			
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedical-w2vecwordpruned-bioASQ2018-sim-20000", docSimMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateDocSimMatrixW2VecMaxMin() {
		try{
			ArrayList<String []> alDocLabelFlat =bioMedicalUtil.getDocsBiomedicalFlat();			
			HashMap<String, double[]> hmW2Vec = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedical(bioMedicalUtil.getUniqueWords());
			
			ArrayList<InstanceW2Vec> testW2Vecs = bioMedicalUtil.docClusterUtil.CreateW2VecMinMaxForTestData(alDocLabelFlat, hmW2Vec);
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedical-w2vecMaxftr-bioASQ2018-sim-20000", docSimMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateDocSimMatrixW2VecAvgMaxHarmonicMean() {
		try{
			ArrayList<String []> alDocLabelFlat =bioMedicalUtil.getDocsBiomedicalFlat();			
			HashMap<String, double[]> hmW2Vec = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedical(bioMedicalUtil.getUniqueWords());
			
			ArrayList<InstanceW2Vec> testW2Vecs = bioMedicalUtil.docClusterUtil.CreateW2VecAvgMaxHarmonicForTestData(alDocLabelFlat, hmW2Vec);
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedical-w2vecAvgMaxHarmonic-bioASQ2018-sim-20000", docSimMatrix, " ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void AddhocClustering() {
		try{

			int maxDocFreqTolerance =1;
			
			HashMap<String, Integer> docFreqs = tfIdfMatrixGenerator.CalculateDocFrequency(bioMedicalUtil.getUniqueWords(), bioMedicalUtil.GetBiomedicalDocuments());
			
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\2n-biomedical-w2vecitr-bioasq2018-sparse-20000-15-labels";
			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\2n-biomedical-w2vec-bioasq2018-sparse-20000-labels";

			ArrayList<String> clusterLables = bioMedicalUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClustersPred = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
			//just to print and compare result
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClustersOrigi = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
	
			ArrayList<String []> alBodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
			ArrayList<InstanceW2Vec> alInsts = new ArrayList<InstanceW2Vec>();
			
			if(clusterLables.size()== alBodyLabel.size()){

				HashMap<String, double[]> hmW2Vec = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedical(bioMedicalUtil.getUniqueWords());
				
				for(int i=0;i<alBodyLabel.size();i++ ){
					InstanceW2Vec newInst = new InstanceW2Vec();
					newInst.OriginalLabel = alBodyLabel.get(i)[1];
					newInst.Text = alBodyLabel.get(i)[0];
					newInst.ClusteredLabel = clusterLables.get(i);
					newInst.Features = bioMedicalUtil.docClusterUtil.PopulateW2VecForSingleDoc(newInst.Text, hmW2Vec);
					alInsts.add(newInst);
				}
				
				hmW2Vec.clear();
				
				for(InstanceW2Vec inst: alInsts){
					
					if(!lastClustersPred.containsKey(inst.ClusteredLabel)){
						ArrayList<InstanceW2Vec> al = new ArrayList<InstanceW2Vec>();
						al.add(inst);
						lastClustersPred.put(inst.ClusteredLabel, al);
					}else{
						ArrayList<InstanceW2Vec> al = lastClustersPred.get(inst.ClusteredLabel);
						al.add(inst);
						lastClustersPred.put(inst.ClusteredLabel, al);
					}
					
					if(!lastClustersOrigi.containsKey(inst.OriginalLabel)){
						ArrayList<InstanceW2Vec> al = new ArrayList<InstanceW2Vec>();
						al.add(inst);
						lastClustersOrigi.put(inst.OriginalLabel, al);
					}else{
						ArrayList<InstanceW2Vec> al = lastClustersOrigi.get(inst.OriginalLabel);
						al.add(inst);
						lastClustersOrigi.put(inst.OriginalLabel, al);
					}
				}
				
				
				//LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClustersPredSubList = GetSublist(new String[]{"2", "6"}, lastClustersPred);
				
				//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVectorExternal(lastClustersPred);
				//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(lastClustersPredSubList);
				//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVectorExternal(lastClustersOrigi);
				
				///find unique terms in each cluster
				HashMap<String, Integer> clusterDisFreqPred = new HashMap<String, Integer>();
				
				for(String label: lastClustersPred.keySet()){
					
					HashSet<String> uniqWordsPerCluster = new HashSet<String>();
					ArrayList<InstanceW2Vec> al = lastClustersPred.get(label);
					
					for(InstanceW2Vec inst: al){
						String [] textArr= inst.Text.split("\\s+");
						for(String w: textArr){
							uniqWordsPerCluster.add(w);
						}
					}
					
					for(String uniqword: uniqWordsPerCluster){
						if(!clusterDisFreqPred.containsKey(uniqword)){
							clusterDisFreqPred.put(uniqword, 1);
						}else{
							clusterDisFreqPred.put(uniqword, clusterDisFreqPred.get(uniqword)+ 1);
						}
					}
				}
				
				
				//just to print and compare result
				HashMap<String, Integer> clusterDisFreqOrigi = new HashMap<String, Integer>();
				
				for(String label: lastClustersOrigi.keySet()){
					
					HashSet<String> uniqWordsPerCluster = new HashSet<String>();
					ArrayList<InstanceW2Vec> al = lastClustersOrigi.get(label);
					
					for(InstanceW2Vec inst: al){
						String [] textArr= inst.Text.split("\\s+");
						for(String w: textArr){
							uniqWordsPerCluster.add(w);
						}
					}
					
					for(String uniqword: uniqWordsPerCluster){
						if(!clusterDisFreqOrigi.containsKey(uniqword)){
							clusterDisFreqOrigi.put(uniqword, 1);
						}else{
							clusterDisFreqOrigi.put(uniqword, clusterDisFreqOrigi.get(uniqword)+ 1);
						}
					}
				}
				
				//just to print and compare result
				BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\word-org-pred-distribution"));
				int disPredCountGreat2 = 0;
				for(String word: clusterDisFreqPred.keySet()){
					if(clusterDisFreqPred.get(word)>maxDocFreqTolerance){
						disPredCountGreat2++;
					}
					bw.write(word+", doc freq="+ docFreqs.get(word)
							+", cluster distribution pred="+ clusterDisFreqPred.get(word)
							+", cluster distribution origi="+clusterDisFreqOrigi.get(word)+"\n");
				}
				
				bw.write("disPredCountGreat2="+disPredCountGreat2+", total word="+docFreqs.size()+ "\n");
				///end
				
				//find the pure text
				LinkedHashMap<String, ArrayList<InstanceW2Vec>> pureClustersPred = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
				LinkedHashMap<String, ArrayList<InstanceW2Vec>> pureClustersPredTolerated = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
				
				for(String label: lastClustersPred.keySet()){
					ArrayList<InstanceW2Vec> al = lastClustersPred.get(label);
					
					for(InstanceW2Vec inst: al){
						String [] textArr= inst.Text.split("\\s+");
						
						boolean flag = false;
						for(String word: textArr){
							if(clusterDisFreqPred.get(word)>maxDocFreqTolerance){
								flag = true;
								break;
							}
						}
						
						if(flag) continue;
						
						if(!pureClustersPred.containsKey(inst.ClusteredLabel)){
							ArrayList<InstanceW2Vec> all = new ArrayList<InstanceW2Vec>();
							all.add(inst);
							pureClustersPred.put(inst.ClusteredLabel, all);
						}else{
							ArrayList<InstanceW2Vec> all = pureClustersPred.get(inst.ClusteredLabel);
							all.add(inst);
							pureClustersPred.put(inst.ClusteredLabel, all);
						}
						
					}
					
					if(pureClustersPred.containsKey(label)){
						bw.write(label+", pure texts="+ pureClustersPred.get(label).size()+"\n");
						//for(ArrayList<InstanceText> values: pureClustersPred.get(label))
						ArrayList<InstanceW2Vec> pureTexts = pureClustersPred.get(label);
						
						for(InstanceW2Vec inst: pureTexts){
							bw.write(inst.Text+"\n");
						}
						
						if(pureClustersPred.get(label).size()== maxDocFreqTolerance){
							pureClustersPredTolerated.put(label, pureClustersPred.get(label));
							System.out.println("filter-pureClustersPred.get(label)="+pureClustersPred.get(label).get(0).Text);
						}
					}
				}
				//end
			
				//find most similar docs to the pure texts
//				int loop=0;
				for(String pureLabel: pureClustersPredTolerated.keySet()){
					ArrayList<InstanceW2Vec> pureTexts = pureClustersPredTolerated.get(pureLabel);
					System.out.println(pureLabel+","+pureTexts.size()
							+",lastClustersPredSize="+lastClustersPred.get(pureLabel).size());
					//find lastClustersPred.size() most similar items to pureLabeltext
					
					ArrayList<InstanceW2Vec> closestPureTexts = FindClosestPureTexts(pureTexts, pureLabel, 
							lastClustersPred.get(pureLabel).size(), alInsts, docFreqs, maxDocFreqTolerance);
//					ArrayList<InstanceW2Vec> closestPureTexts = FindClosestPureTexts(pureTexts, pureLabel, 
//							1000, alInsts, docFreqs, maxDocFreqTolerance);

					
					
					
					//remove closestPureText from prev group of lastClustersPred;
					for(InstanceW2Vec closestPureText: closestPureTexts){
						if(!closestPureText.ClusteredLabel.equals(pureLabel)){
							lastClustersPred = RemoveClosestPureTextFromGroup(closestPureText, lastClustersPred);
						}
						closestPureText.ClusteredLabel = pureLabel;
					}
					
					lastClustersPred.put(pureLabel, closestPureTexts);
					//loop++;
//					if(loop>1){
//						break;
//					}
				}
				//end
				
				bw.close();
				
				//LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClustersPredSubList1 = GetSublist(new String[]{"2", "6"}, lastClustersPred);
				
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVectorExternal(lastClustersPred);
				//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(lastClustersPredSubList1);
			}	
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private LinkedHashMap<String, ArrayList<InstanceW2Vec>> RemoveClosestPureTextFromGroup(
			InstanceW2Vec closestPureText,
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClustersPred) {
		try{
			String label = closestPureText.ClusteredLabel;
			ArrayList<InstanceW2Vec> al = lastClustersPred.get(label);
			System.out.println("prev size="+al.size());
			
			int index = -1;
			for(int i=0;i<al.size();i++){
				if(al.get(i).Text.equals(closestPureText.Text)){
					index = i;
					break;
				}
			}
			
			if(index>=0){
				al.remove(index);
			}
			
			//temp remove
			for(int i=0;i<100;i++){
				if(al.size()>1){
					al.remove(0);
				}
			}
			
			System.out.println("new size="+al.size());
			lastClustersPred.put(label, al);
		}catch(Exception e){
			e.printStackTrace();
		}
		return lastClustersPred;
	}

	private LinkedHashMap<String, ArrayList<InstanceW2Vec>> GetSublist(
			String[] labels,
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClustersPred) {
		LinkedHashMap<String, ArrayList<InstanceW2Vec>> sublist = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
		
		try{
			for(String label: labels){
				sublist.put(label, lastClustersPred.get(label));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sublist;
	}

	private ArrayList<InstanceW2Vec> FindClosestPureTexts(
			ArrayList<InstanceW2Vec> pureTexts, String pureLabel, int numberOfClosestTexts,
			ArrayList<InstanceW2Vec> alInsts, HashMap<String, Integer> docFreqs, int maxDocFreqTolerance) {
		
		ArrayList<InstanceW2Vec> finalClosestTexts = new ArrayList<InstanceW2Vec>();
		
		try{
			
			ArrayList<HashSet<InstanceW2Vec>> listListClosets = new ArrayList<HashSet<InstanceW2Vec>>();
			
			for(InstanceW2Vec pureText: pureTexts){
				
				HashSet<InstanceW2Vec> closets = new HashSet<InstanceW2Vec>();
				
				ArrayList<double[]> simIndex = new ArrayList<double[]>();
				
				for(int i=0;i<alInsts.size();i++){
					
					boolean allHaveMaxDocFreq = bioMedicalUtil.docClusterUtil.textUtilShared.IsAllWordsHaveLessThanEqualMaxDocFreq(alInsts.get(i).Text, maxDocFreqTolerance, docFreqs);
					if(!allHaveMaxDocFreq) continue;
					
					double sim = ComputeUtil.ComputeCosineSimilarity(pureText.Features, alInsts.get(i).Features);
					simIndex.add(new double[]{sim, i});
				}
				
				System.out.println("simIndex.size="+simIndex.size());
				
				Collections.sort(simIndex, new Comparator<double[]>(){

					@Override
					public int compare(double[] arg0, double[] arg1) {
						return Double.compare(arg1[0], arg0[0]);
					}
				});
				
				for(int i=0;i<simIndex.size() && i<numberOfClosestTexts;i++){
					int ind = (int)simIndex.get(i)[1];
					
					InstanceW2Vec newInst = new InstanceW2Vec();
					newInst.ClusteredLabel = alInsts.get(ind).ClusteredLabel;
					newInst.OriginalLabel = alInsts.get(ind).OriginalLabel;
					newInst.Text = alInsts.get(ind).Text;
					newInst.Features = alInsts.get(ind).Features;
					
				    closets.add(newInst);
				}
				
				listListClosets.add(closets);
			}
			
			finalClosestTexts.addAll(listListClosets.get(0));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return finalClosestTexts;
	}

	public void AddhocClusteringTuned() {
		try{
			int maxDocFreqByClusterTolerance =1;
			int maxDocFreqByDocumentTolerance =1;
			int numberOfCenterTextsInEachLabel = 1;
			int maxCenterTextInAClass = 1;
			HashMap<String, Integer> docFreqs = tfIdfMatrixGenerator.CalculateDocFrequency(bioMedicalUtil.getUniqueWords(), bioMedicalUtil.GetBiomedicalDocuments());
		
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\2n-biomedical-w2vec-bioasq2018-sparse-20000-labels-final";
			
			HashMap<String, double[]> hmW2Vec = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedical(bioMedicalUtil.getUniqueWords());
			
			ArrayList<String> clusterLables = bioMedicalUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			
			ArrayList<InstanceW2Vec> data = bioMedicalUtil.docClusterUtil
					.CreateW2VecForTrainData(bioMedicalUtil.getDocsBiomedicalFlat(), hmW2Vec, clusterLables);			
			//hmW2Vec.clear();
			
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> labelWiseInstancesPred = 
					bioMedicalUtil.docClusterUtil.GetClusterGroupsVectorByLabel(data, false);
			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVectorExternal(labelWiseInstancesPred);
			
			HashMap<String, Integer> docFreqByClusters = bioMedicalUtil.docClusterUtil.GetDocFreqByClusters(labelWiseInstancesPred);
	
			HashMap<String, ArrayList<InstanceW2Vec>> centerTexts = bioMedicalUtil.docClusterUtil.GetCenterTextsDocFreqByClusters(docFreqByClusters, labelWiseInstancesPred, 
					numberOfCenterTextsInEachLabel, maxDocFreqByClusterTolerance);			
			HashMap<String, ArrayList<InstanceW2Vec>> filterCenterTexts = bioMedicalUtil.docClusterUtil
					.GetFilterCenterTextsByDocFreq(centerTexts, docFreqs, maxDocFreqByDocumentTolerance, maxCenterTextInAClass);
			
			//labelWiseInstancesPred = bioMedicalUtil.docClusterUtil
			//		.FindclosestTextsToCenters(labelWiseInstancesPred, filterCenterTexts, data,
			//				docFreqs, maxDocFreqByDocumentTolerance);
			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVectorExternal(labelWiseInstancesPred);
			
			labelWiseInstancesPred = bioMedicalUtil.docClusterUtil.FindclosestTextsToCentersFixed(filterCenterTexts, 
					1000, data, labelWiseInstancesPred, docFreqByClusters, hmW2Vec, maxDocFreqByClusterTolerance);
			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVectorExternal(labelWiseInstancesPred);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateDocSimMatrixW2VecNoCommonWords() {
		try{
			ArrayList<String []> alDocLabelFlat =bioMedicalUtil.getDocsBiomedicalFlat();
			
			HashMap<String, double[]> hmW2Vec = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedical(bioMedicalUtil.getUniqueWords());
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2VecRemoveCommonWordParallel(alDocLabelFlat, 10, hmW2Vec
					, bioMedicalUtil.docClusterUtil);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedical-w2vecnocommonword-bioASQ2018-400D-sim-20000", docSimMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterByRemovingTopLengthVectors() {
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocSimMatrixW2VecPrunedFtrsPreDefined() {
		try{
			ArrayList<String []> alDocLabelFlat =bioMedicalUtil.getDocsBiomedicalFlat();
			
			HashMap<String, double[]> hmW2Vec = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedical(bioMedicalUtil.getUniqueWords());
			
			ArrayList<InstanceW2Vec> testW2Vecs = bioMedicalUtil.docClusterUtil.CreateW2VecForTestDataByKeptFtrs(alDocLabelFlat, hmW2Vec, bioMedicalUtil.GetKeptFtrList());			
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);

			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/biomedical-w2vec-bioASQ2018-400D-keptFtrs-sim-20000", docSimMatrix, " ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateEnsembleClusters(){
		try{
			
			//HashMap<String, double[]> hmW2Vec = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedical(bioMedicalUtil.getUniqueWords());
			
			ArrayList<String> clusterLabelFiles = new ArrayList<String>();
			//clusterLabelFiles.add("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\2n-biomedical-word2vecnostopword-sparse-20000-0-labels");
			clusterLabelFiles.add("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedical-w2vec-bioasq-sparse-alpha-20000-0-labels");
			clusterLabelFiles.add("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\2n-biomedical-w2vec-add-sparse-20000-0-labels");
			//clusterLabelFiles.add("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedical-sparse-tfidf-alpha-20000-0-labels");
			
			List<LinkedHashMap<String, ArrayList<InstanceText>>> lastClustersList = new ArrayList<LinkedHashMap<String,ArrayList<InstanceText>>>(); 
			
			ArrayList<String []> alBodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
			
			for(String externalClusteringResultFile: clusterLabelFiles){
				ArrayList<String> clusterLables = bioMedicalUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
				ArrayList<InstanceText> trainInstTexts = bioMedicalUtil.docClusterUtil.CreateW2VecForTrainData(alBodyLabel, clusterLables);
				LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = bioMedicalUtil.docClusterUtil
						.GetClusterGroupsTextByLabel(trainInstTexts, false);
				
				lastClustersList.add(lastClusters);
			}
			
			LinkedHashMap<String, ArrayList<InstanceText>> commonlastClusters = bioMedicalUtil.docClusterUtil.GetCommonLastClusters(lastClustersList);			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(commonlastClusters);
			
			//rough
			//int n_K = 1000;
			for(LinkedHashMap<String, ArrayList<InstanceText>> alltextsPerLable: lastClustersList){
				HashSet<String> labelsToRemove = new HashSet<String>();
				//HashMap<String, double[]> labelCenters = new LinkedHashMap<String, double[]>();
				
				for(String label: alltextsPerLable.keySet()){
					if(!commonlastClusters.containsKey(label)){
						commonlastClusters.put(label, alltextsPerLable.get(label));
						labelsToRemove.add(label);
					}
					//System.out.println(label+","+commonlastClusters.get(label).size());
					//if(commonlastClusters.get(label).size()<n_K)
					{
						//ArrayList<InstanceW2Vec> instancew2Vecs = bioMedicalUtil.docClusterUtil.ConvertInsTextToW2Vec(commonlastClusters.get(label), hmW2Vec);
						//InstanceW2Vec center = bioMedicalUtil.docClusterUtil.ComputeCenterInstanceW2Vec(instancew2Vecs);
						//labelCenters.put(label, center.Features);
					}
				}
				
				
				//rough
				LinkedHashMap<String, ArrayList<InstanceText>> rough = alltextsPerLable;
				for(String key: commonlastClusters.keySet()){
					if(commonlastClusters.get(key).size()<500){
						ArrayList<InstanceText> commonIns = commonlastClusters.get(key);
						List<InstanceText> tempList = rough.get(key).subList(0, rough.get(key).size()>500? 500 : rough.get(key).size());
						commonIns.addAll(tempList);
						commonlastClusters.put(key, commonIns);
					}
				}
				
				//remove larger groups
				for(String key: commonlastClusters.keySet()){
					ArrayList<InstanceText> commonIns = commonlastClusters.get(key);
					if(commonIns.size()>800){
						List<InstanceText> commonInsSub = commonIns.subList(800, commonIns.size()-1);
						commonIns.removeAll(commonInsSub);
						commonlastClusters.put(key, commonIns);
					}
				}
				
				//end rough
				
				
				//ArrayList<InstanceText> remainInstTexts = bioMedicalUtil.docClusterUtil.FindRemainInstances(alltextsPerLable, commonlastClusters);
				//ArrayList<InstanceW2Vec> remainInstsW2Vec = bioMedicalUtil.docClusterUtil.ConvertInsTextToW2Vec(remainInstTexts, hmW2Vec);

//				LinkedHashMap<String, ArrayList<InstanceText>> remainInstTextsClusters = bioMedicalUtil.docClusterUtil
//						.GetClusterGroupsTextByLabel(remainInstTexts, false);
//				System.out.println("\nramina="+remainInstTexts.size()+"\n");
//				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(remainInstTextsClusters);
				
				//LinkedHashMap<String, ArrayList<InstanceW2Vec>> remainPredictedCluster = bioMedicalUtil.docClusterUtil.GetInstanceClosestToCentersW2Vec(remainInstsW2Vec, labelCenters, commonlastClusters);  
				//LinkedHashMap<String, ArrayList<InstanceText>> remainPredictedClusterText = bioMedicalUtil.docClusterUtil.ConvertInsW2VecToText(remainPredictedCluster);
				
				System.out.println("\n\ncommon\n\n");
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(commonlastClusters);
				
				//System.out.println("ramanin\n\n");
				//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(remainPredictedClusterText);
				
				//merge commonlastClusters and remainPredictedCluster
				//evaluate merged clusters
				
//				LinkedHashMap<String, ArrayList<InstanceText>> mergedCluster = new LinkedHashMap<String, ArrayList<InstanceText>>();
//				for(String lable: commonlastClusters.keySet()){
//					ArrayList<InstanceText> al = commonlastClusters.get(lable);
//					if(remainPredictedClusterText.containsKey(lable)){
//						al.addAll(remainPredictedClusterText.get(lable));
//					}
//					mergedCluster.put(lable, al);
//				}
//				
//				System.out.println("merged\n\n");
//				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(mergedCluster);
				
				BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_train"));				
				LinkedHashMap<String, String[]> uniqueTrainSetPredTrue=new LinkedHashMap<String, String[]>();				
				for(String key: commonlastClusters.keySet()){
					for(InstanceText instTxt: commonlastClusters.get(key)){
						uniqueTrainSetPredTrue.put(instTxt.Text, new String []{instTxt.ClusteredLabel, instTxt.OriginalLabel});
					}
				}				
				for(String text: uniqueTrainSetPredTrue.keySet()){
					String [] predTrue = uniqueTrainSetPredTrue.get(text);
					bw.write(predTrue[0]+"\t"+predTrue[1]+"\t" +text+"\n");
				}
				bw.close();
				
				
				bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_test"));				
				for(String [] bodyLabel: alBodyLabel){
					String body = bodyLabel[0];
					String originalLabel = bodyLabel[1];
					
					if(uniqueTrainSetPredTrue.containsKey(body)) continue;
					bw.write(originalLabel+"\t"+originalLabel+"\t" +body+"\n");
				}
				bw.close();
				
				for(String label: labelsToRemove){
					commonlastClusters.remove(label);
					System.out.println("to remove label="+label);
				}
			}
			
			//end rough
			
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void GenerateTrainTest2(int portion) {
		try{
			
			String trainTestTextFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_traintest";			
			ArrayList<String[]> predTrueTexts = bioMedicalUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(trainTestTextFile);			
			ArrayList<InstanceText> allInstTexts = bioMedicalUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = bioMedicalUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();			
			
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			bioMedicalUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlier.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = bioMedicalUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\textsperlabel\\"); 
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
				ArrayList<String> outlierpreds = outliersByLabel.get(label);
				
				if(insts.size()!=outlierpreds.size()){
					System.out.println("Size not match for="+label);
					continue;
				}
				
				if(insts.size()<=portion) continue;
				
				ArrayList<InstanceText> instOutLier = new ArrayList<InstanceText>();
				
				for(int i=0;i<outlierpreds.size();i++){
					String outPred = outlierpreds.get(i);
					if(outPred.equals("-1")){
						instOutLier.add(insts.get(i));
					}
				}
				
				insts.removeAll(instOutLier);
				testInstTexts.addAll(instOutLier);
				
				lastClusters.put(label, insts);
			}
			
			
			for(String label: lastClusters.keySet()){
				
				ArrayList<InstanceText> insts = lastClusters.get(label);
				ArrayList<InstanceText> subInsts = new ArrayList<InstanceText>();;
				
				if(insts.size()>portion){
					subInsts.addAll(insts.subList(0, portion));
					testInstTexts.addAll(insts.subList(portion, insts.size()));
				}else{
					subInsts.addAll(insts);
				}
				trainInstTexts.addAll(subInsts);	
				lastClusters.put(label, subInsts);
			}			
			
			//LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = bioMedicalUtil.docClusterUtil
			//		.GetClusterGroupsTextByLabel(testInstTexts, false);
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = bioMedicalUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(trainInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			bioMedicalUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(trainInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_train");
			
			bioMedicalUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(testInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_test");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	public void GenerateTrainTest2(int portion) {
//		try{
//			
//			String trainTestTextFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_traintest";			
//			ArrayList<String[]> predTrueTexts = bioMedicalUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(trainTestTextFile);			
//			ArrayList<InstanceText> allInstTexts = bioMedicalUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts);
//			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = bioMedicalUtil.docClusterUtil
//					.GetClusterGroupsTextByLabel(allInstTexts, false);
//			
//			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
//			
//			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
//			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();
//			
//			
//			
//			for(String label: lastClusters.keySet()){
//				
//				ArrayList<InstanceText> insts = lastClusters.get(label);
//				ArrayList<InstanceText> subInsts = new ArrayList<InstanceText>();;
//				
//				if(insts.size()>portion){
//					subInsts.addAll(insts.subList(0, portion));
//					testInstTexts.addAll(insts.subList(portion, insts.size()));
//				}else{
//					subInsts.addAll(insts);
//				}
//				trainInstTexts.addAll(subInsts);	
//				lastClusters.put(label, subInsts);
//			}			
//			
//			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = bioMedicalUtil.docClusterUtil
//					.GetClusterGroupsTextByLabel(testInstTexts, false);
//			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
//			
//			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = bioMedicalUtil.docClusterUtil
//					.GetClusterGroupsTextByLabel(trainInstTexts, false);
//			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
//			
//			BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_train"));											
//			for(InstanceText inst: trainInstTexts){
//				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
//			}
//			bw.close();
//			
//			bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_test"));											
//			for(InstanceText inst: testInstTexts){
//				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
//			}
//			bw.close();
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}

//	public void GenerateTrainTest() {
//		try{
//			//String externalClusteringResultFile= "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\2n-biomedical-w2vec-add-sparse-20000-0-labels";
//			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\2n-biomedical-w2vec-bioasq-sparse-20000-0-labels"; //2017-bio-asq
//			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedical-sparse-gtm-alpha-20000-0-labels"; 
//			
//			ArrayList<String []> alBodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
//			
//			ArrayList<String> clusterLables = bioMedicalUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
//			ArrayList<InstanceText> allInstTexts = bioMedicalUtil.docClusterUtil.CreateW2VecForTrainData(alBodyLabel, clusterLables);
//			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = bioMedicalUtil.docClusterUtil
//					.GetClusterGroupsTextByLabel(allInstTexts, false);
//			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
//			//write texts of each group in  
//			//bioMedicalUtil.docClusterUtil.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\textsperlabel\\"
//			//		,lastClusters);
//			//call python code to get the outliers in each cluster
//			
//			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
//			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();
//			for(String label: lastClusters.keySet()){
//				ArrayList<InstanceText> insts = lastClusters.get(label);
//			
//				ArrayList<InstanceText> subInsts = new ArrayList<InstanceText>();;
//				if(insts.size()>700){
//					subInsts.addAll(insts.subList(0, 700));
//					testInstTexts.addAll(insts.subList(700, insts.size()));
//				}else{
//					subInsts.addAll(insts);
//				}
//				trainInstTexts.addAll(subInsts);				
//				lastClusters.put(label, subInsts);				
//			}			
//			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
//			
//			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = bioMedicalUtil.docClusterUtil
//					.GetClusterGroupsTextByLabel(testInstTexts, false);
//			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
//			
//			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = bioMedicalUtil.docClusterUtil
//					.GetClusterGroupsTextByLabel(trainInstTexts, false);
//			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
//			
//			
//			BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_train"));											
//			for(InstanceText inst: trainInstTexts){
//				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
//			}
//			bw.close();
//			
//			bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_test"));											
//			for(InstanceText inst: testInstTexts){
//				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
//			}
//			bw.close();
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	public void GenerateTrainTest() {
		try{
			//String externalClusteringResultFile= "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\2n-biomedical-w2vec-add-sparse-20000-0-labels";
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\2n-biomedical-w2vec-bioasq-sparse-20000-0-labels"; //2n-biomedical-w2vecitr-bioasq2018-sparse-20000-0-labels;   //2n-biomedical-w2vec-bioasq-sparse-20000-0-labels; //2017-bio-asq
			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedical-sparse-gtm-alpha-20000-0-labels";  //2n-biomedical-w2vecitr-bioasq2018-sparse-20000-0-labels
			
			
			ArrayList<String []> alBodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
			
			ArrayList<String> clusterLables = bioMedicalUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			ArrayList<InstanceText> allInstTexts = bioMedicalUtil.docClusterUtil.CreateW2VecForTrainData(alBodyLabel, clusterLables);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = bioMedicalUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();
			
			
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			bioMedicalUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlier.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = bioMedicalUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\textsperlabel\\"); 
			
			//filter instants by outlier..
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
				ArrayList<String> outlierpreds = outliersByLabel.get(label);
				
				if(insts.size()!=outlierpreds.size()){
					System.out.println("Size not match for="+label);
					continue;
				}
				
				if(insts.size()<=700) continue;
				
				ArrayList<InstanceText> instOutLier = new ArrayList<InstanceText>();
				
				for(int i=0;i<outlierpreds.size();i++){
					String outPred = outlierpreds.get(i);
					if(outPred.equals("-1")){
						instOutLier.add(insts.get(i));
					}
				}
				
				insts.removeAll(instOutLier);
				testInstTexts.addAll(instOutLier);
				
				lastClusters.put(label, insts);
			}
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
			
				ArrayList<InstanceText> subInsts = new ArrayList<InstanceText>();;
				if(insts.size()>700){
					subInsts.addAll(insts.subList(0, 700));
					testInstTexts.addAll(insts.subList(700, insts.size()));
				}else{
					subInsts.addAll(insts);
				}
				trainInstTexts.addAll(subInsts);				
				lastClusters.put(label, subInsts);				
			}			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = bioMedicalUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(testInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = bioMedicalUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(trainInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_train"));											
			for(InstanceText inst: trainInstTexts){
				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
			}
			bw.close();
			
			bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_test"));											
			for(InstanceText inst: testInstTexts){
				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
			}
			bw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void FilterOutliers() {
		try{
			String externalClusteringResultFile= "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\2n-biomedical-w2vec-add-sparse-20000-0-labels";
			ArrayList<String []> alBodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
			
			ArrayList<String> clusterLables = bioMedicalUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			ArrayList<InstanceText> allInstTexts = bioMedicalUtil.docClusterUtil.CreateW2VecForTrainData(alBodyLabel, clusterLables);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = bioMedicalUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
			for(int i=1;i<=20;i++){
				
				String label = Integer.toString(i);
				ArrayList<InstanceText> insts = lastClusters.get(label);
				
				String outlierFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\4118\\"+i+"_outlierpred";
				
				ArrayList<InstanceText> tobeRemovedInsts = new ArrayList<InstanceText>();
				
				BufferedReader br =  new BufferedReader(new FileReader(outlierFile));
				String line = "";
				int lc = 0;
				while((line = br.readLine())!=null){				   
				   line = line.trim().toLowerCase();
				   if(line.isEmpty()) continue;
				   
				   if(line.equals("-1")){
					   tobeRemovedInsts.add(insts.get(lc));
				   }
				   lc++;
				}
				br.close();
				
				insts.removeAll(tobeRemovedInsts);
				lastClusters.put(label, insts);					
			}
			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateTrainTest2List() {
		try{
			
			int maxIteration = 5;
			BioMedicalExternalEvaluation obj = new BioMedicalExternalEvaluation();
			
			for(int i=0;i<maxIteration;i++){
				System.out.println("iteration="+i);
				for(int items = 700; items<=1000;items=items+50){
					
					Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\improvedclassification.py");
					int exitVal = p.waitFor();
					System.out.println("Process status code="+exitVal);
					p.destroy();
					
					obj.ExternalEvaluateRAD();	
					
					GenerateTrainTest2(items);
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void GenerateCommonCluster() {
		try{
			ArrayList<String> files = new ArrayList<String>();
			files.add("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\bioasq-2017-2n-sparse-4867\\biomedicalraw_ensembele_traintest");
			files.add("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\bioasq-2018-2n-sparse-4829\\biomedicalraw_ensembele_traintest");
			
			String mergedTrainTestFile = "";
			
			for(int i=0;i<files.size()-1;i++){
				String file0 = i>0 ? mergedTrainTestFile: files.get(i);
				String file1 = files.get(i+1);
				
				ArrayList<String[]> predTrueTexts0 = bioMedicalUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(file0);			
				ArrayList<InstanceText> allInstTexts0 = bioMedicalUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts0);
				LinkedHashMap<String, ArrayList<InstanceText>> lastClusters0 = bioMedicalUtil.docClusterUtil
						.GetClusterGroupsTextByLabel(allInstTexts0, false);
				
				
				ArrayList<String[]> predTrueTexts1 = bioMedicalUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(file1);			
				ArrayList<InstanceText> allInstTexts1 = bioMedicalUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts1);
				LinkedHashMap<String, ArrayList<InstanceText>> lastClusters1 = bioMedicalUtil.docClusterUtil
						.GetClusterGroupsTextByLabel(allInstTexts1, false);
				
				
				LinkedHashMap<String, ArrayList<InstanceText>> lastClustersCommon = bioMedicalUtil.docClusterUtil.FindCommonInstancePerCluster(lastClusters0, lastClusters1);
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersCommon);
				
				ArrayList<InstanceText> commonTrainInsts = bioMedicalUtil.docClusterUtil.textUtilShared.FlatInsts(lastClustersCommon);
				
				mergedTrainTestFile="D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_traintest";
				bioMedicalUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(commonTrainInsts,mergedTrainTestFile);
				
				int portion = 450;
				
				ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();;
				ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();;
				
				for(String label: lastClustersCommon.keySet()){
					
					ArrayList<InstanceText> insts = lastClustersCommon.get(label);
					ArrayList<InstanceText> subInsts = new ArrayList<InstanceText>();;
					
					if(insts.size()>portion){
						subInsts.addAll(insts.subList(0, portion));
						testInstTexts.addAll(insts.subList(portion, insts.size()));
					}else{
						subInsts.addAll(insts);
					}
					trainInstTexts.addAll(subInsts);	
					lastClustersCommon.put(label, subInsts);
				}
				
				String mergedTrainFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_train";
				String mergedTestFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_test";
				
				bioMedicalUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(trainInstTexts,mergedTrainFile);
				bioMedicalUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(testInstTexts,mergedTestFile);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}



