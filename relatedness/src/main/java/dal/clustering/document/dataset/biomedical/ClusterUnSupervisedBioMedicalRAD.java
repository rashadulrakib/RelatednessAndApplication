package dal.clustering.document.dataset.biomedical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import dal.clustering.document.shared.cluster.UnSupervisedClusteringW2Vec;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.general.UtilsShared;

public class ClusterUnSupervisedBioMedicalRAD extends ClusterBioMedical{
	
	public ClusterUnSupervisedBioMedicalRAD(){
		unSupervisedClusteringW2Vec = new UnSupervisedClusteringW2Vec(bioMedicalUtil.getUniqueWords(),
		bioMedicalUtil.getDocsBiomedicalFlat(), bioMedicalUtil.getDocsBiomedicalList(), 
		bioMedicalUtil.docClusterUtil);
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
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification(){
		try{
			String simFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedical-w2vec-bioASQ-sim-20000-stemmed";	
			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
			
			double [][] docDisSimMatrixDense = UtilsShared.CopyMatrix(docSimMatrix, true);
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/2n-biomedical-w2vec-bioasq-dense-20000-stemmed", docDisSimMatrixDense, " ");
			
			double [][] docDisSimMatrixSparse = bioMedicalUtil.docClusterUtil.sparsificationUtilRAD.SparsifyDocDisSimilarityMatrixAlgorithomicExactIterative(docSimMatrix, BioMedicalConstant.NumberOfClusters);
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/2n-biomedical-w2vec-bioasq-sparse-20000-stemmed", docDisSimMatrixSparse, " ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
