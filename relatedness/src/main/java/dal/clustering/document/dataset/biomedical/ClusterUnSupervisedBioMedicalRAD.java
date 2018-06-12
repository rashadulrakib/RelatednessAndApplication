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
import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;
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
			ArrayList<String []> alDocLabelFlatPruned = bioMedicalUtil.docClusterUtil.textUtilShared.PruneWordsByDocFreqs(docFreqs, alDocLabelFlat, 950); 
			
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
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersPred = new LinkedHashMap<String, ArrayList<InstanceText>>();
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersOrigi = new LinkedHashMap<String, ArrayList<InstanceText>>();
	
			ArrayList<String []> alBodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
			ArrayList<InstanceText> alInsts = new ArrayList<InstanceText>();
			
			if(clusterLables.size()== alBodyLabel.size()){
				
				for(int i=0;i<alBodyLabel.size();i++ ){
					InstanceText newInst = new InstanceText();
					newInst.OriginalLabel = alBodyLabel.get(i)[1];
					newInst.Text = alBodyLabel.get(i)[0];
					newInst.ClusteredLabel = clusterLables.get(i);
					alInsts.add(newInst);
				}
				
				for(InstanceText inst: alInsts){
					
					if(!lastClustersPred.containsKey(inst.ClusteredLabel)){
						ArrayList<InstanceText> al = new ArrayList<InstanceText>();
						al.add(inst);
						lastClustersPred.put(inst.ClusteredLabel, al);
					}else{
						ArrayList<InstanceText> al = lastClustersPred.get(inst.ClusteredLabel);
						al.add(inst);
						lastClustersPred.put(inst.ClusteredLabel, al);
					}
					
					if(!lastClustersOrigi.containsKey(inst.OriginalLabel)){
						ArrayList<InstanceText> al = new ArrayList<InstanceText>();
						al.add(inst);
						lastClustersOrigi.put(inst.OriginalLabel, al);
					}else{
						ArrayList<InstanceText> al = lastClustersOrigi.get(inst.OriginalLabel);
						al.add(inst);
						lastClustersOrigi.put(inst.OriginalLabel, al);
					}
				}
				
				///find unique terms in each cluster
				HashMap<String, Integer> clusterDisFreqPred = new HashMap<String, Integer>();
				
				for(String label: lastClustersPred.keySet()){
					
					HashSet<String> uniqWordsPerCluster = new HashSet<String>();
					ArrayList<InstanceText> al = lastClustersPred.get(label);
					
					for(InstanceText inst: al){
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
					ArrayList<InstanceText> al = lastClustersOrigi.get(label);
					
					for(InstanceText inst: al){
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
				LinkedHashMap<String, ArrayList<InstanceText>> pureClustersPred = new LinkedHashMap<String, ArrayList<InstanceText>>();
				LinkedHashMap<String, ArrayList<InstanceText>> pureClustersPredTolerated = new LinkedHashMap<String, ArrayList<InstanceText>>();
				
				for(String label: lastClustersPred.keySet()){
					ArrayList<InstanceText> al = lastClustersPred.get(label);
					
					for(InstanceText inst: al){
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
							ArrayList<InstanceText> all = new ArrayList<InstanceText>();
							all.add(inst);
							pureClustersPred.put(inst.ClusteredLabel, all);
						}else{
							ArrayList<InstanceText> all = pureClustersPred.get(inst.ClusteredLabel);
							all.add(inst);
							pureClustersPred.put(inst.ClusteredLabel, all);
						}
						
					}
					
					if(pureClustersPred.containsKey(label)){
						bw.write(label+", pure texts="+ pureClustersPred.get(label).size()+"\n");
						//for(ArrayList<InstanceText> values: pureClustersPred.get(label))
						ArrayList<InstanceText> pureTexts = pureClustersPred.get(label);
						
						for(InstanceText inst: pureTexts){
							bw.write(inst.Text+"\n");
						}
						
						if(pureClustersPred.get(label).size()== maxDocFreqTolerance){
							pureClustersPredTolerated.put(label, pureClustersPred.get(label));
						}
					}
				}
				//end
			
				//find most similar docs to the pure texts
				for(String pureLabel: pureClustersPredTolerated.keySet()){
					ArrayList<InstanceText> pureTexts = pureClustersPredTolerated.get(pureLabel);
					System.out.println(pureLabel+","+pureTexts.size()+",lastClustersPred="+lastClustersPred.size());
					//find lastClustersPred.size() most similar items to pureLabeltext
					
				}
				//end
				
				bw.close();
				
			}	
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
