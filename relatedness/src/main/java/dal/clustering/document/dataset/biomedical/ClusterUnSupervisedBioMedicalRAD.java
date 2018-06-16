package dal.clustering.document.dataset.biomedical;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.UtilsShared;

public class ClusterUnSupervisedBioMedicalRAD extends ClusterBioMedical{
	
	TfIdfMatrixGenerator tfIdfMatrixGenerator;
	
	public ClusterUnSupervisedBioMedicalRAD(){
		tfIdfMatrixGenerator = new TfIdfMatrixGenerator();
	}
	
	public void ClusterDocsByFrequentTerms(){
		try{
			
			int itemsPerCluster = bioMedicalUtil.GetBiomedicalStemmedDocuments().size() / BioMedicalConstant.NumberOfClusters;
			
			//List<Set<String>> stemmedDocumnetsUniqueTerms = bioMedicalUtil.GetBiomedicalStemmedDocumnetsUniqueTerms();
			
			for(List<String> stemeddoc: bioMedicalUtil.GetBiomedicalStemmedDocuments()){
				System.out.println(stemeddoc);
			}
			
			HashMap<String, Double> docFreqs = tfIdfMatrixGenerator.CalculateDocFrequency(bioMedicalUtil.getUniqueWords(), bioMedicalUtil.GetBiomedicalStemmedDocuments());

			for(String term: docFreqs.keySet()){
				System.out.println(term+"="+docFreqs.get(term));
			}
			
	        Set<Entry<String, Double>> entries = docFreqs.entrySet();
	        
	        Comparator<Entry<String, Double>> valueComparator = new Comparator<Entry<String,Double>>() {
	            
	            @Override
	            public int compare(Entry<String, Double> e1, Entry<String, Double> e2) {
	            	Double v1 = e1.getValue();
	            	Double v2 = e2.getValue();
	                return v1.compareTo(v2);
	            }
	        };
	
	        List<Entry<String, Double>> listOfEntries = new ArrayList<Entry<String, Double>>(entries);
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
			ArrayList<InstanceW2Vec> testW2Vecs = bioMedicalUtil.doc2VecUtil.CreateDoc2VecInstances("/users/grad/rakib/doc2vec/doc2vec-100", bioMedicalUtil.getDocsBiomedicalFlat(), ",");
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);

			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedical-doc2vec100-sim-20000", docSimMatrix, " ");

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
			
			HashMap<String, Double> docFreqs = tfIdfMatrixGenerator.CalculateDocFrequency(bioMedicalUtil.getUniqueWords(), bioMedicalUtil.GetBiomedicalDocuments());			
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
			
			HashMap<String, Double> docFreqs = tfIdfMatrixGenerator.CalculateDocFrequency(bioMedicalUtil.getUniqueWords(), bioMedicalUtil.GetBiomedicalDocuments());
			
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\2n-biomedical-w2vecitr-bioasq2018-sparse-20000-15-labels";
			
			BufferedReader br =  new BufferedReader(new FileReader(externalClusteringResultFile));
			
			String line="";
			ArrayList<String> clusterLables = new ArrayList<String>();
			
			while((line=br.readLine()) != null) {
		        line = line.trim();
		        if(line.isEmpty()) continue;
		        
		        String clusterGroups [] = line.split(",");
		        clusterLables.addAll(Arrays.asList(clusterGroups));
			}
			br.close();
			
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClustersPred = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
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
				
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVectorExternal(lastClustersPred);
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
					
//					ArrayList<InstanceW2Vec> closestPureTexts = FindClosestPureTexts(pureTexts, pureLabel, 
//							lastClustersPred.get(pureLabel).size(), alInsts, docFreqs, maxDocFreqTolerance);
					ArrayList<InstanceW2Vec> closestPureTexts = FindClosestPureTexts(pureTexts, pureLabel, 
							1000, alInsts, docFreqs, maxDocFreqTolerance);
					
					for(InstanceW2Vec closestPureText: closestPureTexts){
						if(!closestPureText.ClusteredLabel.equals(pureLabel)){
							//remove closestPureText from prev group of lastClustersPred;
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
			al.remove(closestPureText);
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
			ArrayList<InstanceW2Vec> alInsts, HashMap<String, Double> docFreqs, int maxDocFreqTolerance) {
		
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
	
}
