package dal.clustering.document.dataset.stackoverflow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.cluster.UnSupervisedClusteringText;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringW2Vec;
import dal.clustering.document.tools.weka.KMeansClusteringArff;
import dal.utils.common.compute.ComputeUtil;

public class ClusterUnSupervisedStackOverflowWEKA {
	
	StackOverflowUtil stackOverflowUtil;
	KMeansClusteringArff kMeansClusteringArff;
	//UnSupervisedClusteringText unSupervisedClusteringText;
	//ClusterEvaluation clusterEvaluation;
	
	UnSupervisedClusteringW2Vec unSupervisedClusteringW2Vec;
	UnSupervisedClusteringText unSupervisedClusteringText;
	
	double [] clusterAssignments;
	
	public double [] GetClusterAssignments(){
		return clusterAssignments;
	}
	
	public ClusterUnSupervisedStackOverflowWEKA() throws IOException{
		stackOverflowUtil = new StackOverflowUtil();
		kMeansClusteringArff = new KMeansClusteringArff();
		unSupervisedClusteringW2Vec = new UnSupervisedClusteringW2Vec(stackOverflowUtil.getUniqueWords(),
				stackOverflowUtil.getDocsStackOverflowFlat(), stackOverflowUtil.getDocsStackOverflowList(), 
				stackOverflowUtil.docClusterUtil);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(stackOverflowUtil.docClusterUtil);
	}
	
//	public void ConstructDocsSimilarityMatrixSparsificationByKMeansW2VecGtm(){
//		try{
//			ArrayList<String []> alDocLabel = stackOverflowUtil.getDocsStackOverflowFlat();
//			
//			initializeKMeans();
//			
//			String res = kMeansClusteringArff.ClusterAndEvaluate();
//			
//			ArrayList<double []> instances = kMeansClusteringArff.getInstances();
//			clusterAssignments =  kMeansClusteringArff.getClusterAssignments();
//			
////			if(alDocLabel.size()==clusterAssignments.length && clusterAssignments.length ==instances.size()){
////				
////				//double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabel, unSupervisedClusteringW2Vec.docClusterUtilW2Vec, clusterAssignments);
////				double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabel, unSupervisedClusteringText.docClusterUtilText, clusterAssignments);
////				
////				double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
////				UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\sparseMatrix", saprsifyMatrix, " ");
////				
////				System.out.println(alDocLabel.size());
////			}
//			
//			System.out.println(res);
//			System.out.println("ConstructDocsSimilarityMatrixSparsificationByKMeansW2Vec finished.");
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}

	public void ClusterDocsNGramBasedSimilarityGtmAndW2VecByWEKA() {
		try{
			ArrayList<String []> alDocLabel = stackOverflowUtil.getDocsStackOverflowFlat();
			
			initializeKMeans();
			
			kMeansClusteringArff.ClusterAndEvaluate();
			
			ArrayList<double []> centroids = kMeansClusteringArff.getCentroids();
			ArrayList<double []> instances = kMeansClusteringArff.getInstances();
			double [] clusterAssignments =  kMeansClusteringArff.getClusterAssignments();
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
			
			if(alDocLabel.size()==clusterAssignments.length && clusterAssignments.length ==instances.size()){
				System.out.println(alDocLabel.size());
				
				HashMap<Integer, ArrayList<Integer>> hmClusterItems = new LinkedHashMap<Integer, ArrayList<Integer>>();
				
				int itemIndex =0;
				for(double classIndex: clusterAssignments){
					int cind = (int)classIndex;
					
					if(!hmClusterItems.containsKey(cind)){
						ArrayList<Integer> itemIndexs = new ArrayList<Integer>();
						itemIndexs.add(itemIndex);
						hmClusterItems.put(cind, itemIndexs);
					}else{
						ArrayList<Integer> itemIndexs = hmClusterItems.get(cind);
						itemIndexs.add(itemIndex);
						hmClusterItems.put(cind, itemIndexs);
					}
					itemIndex++;
				}
				
				
				//find the closest w2vec inside a cluster
				for(int clusterIndex=0;clusterIndex<centroids.size();clusterIndex++){
					
					if(hmClusterItems.containsKey(clusterIndex)){
						ArrayList<Integer> itemIndexs = hmClusterItems.get(clusterIndex);
						
						double minDist = Double.MAX_VALUE;
						int minItemInd = 0;
						double [] centroidVec = centroids.get(clusterIndex);
						for(int itemInd: itemIndexs){
							double itemVec [] =instances.get(itemInd);
							double dist = ComputeUtil.ComputeEuclidianDistance(centroidVec, itemVec);
							if(minDist>dist){
								minDist=dist;
								minItemInd= itemInd;
							}
						}
						
						String [] minDocLabel = alDocLabel.get(minItemInd);
						ArrayList<String> alOneSeedList = new ArrayList<String>();
						alOneSeedList.add(minDocLabel[0]);
						hmTrainDocsLabelBody.put(minDocLabel[1], alOneSeedList);
						System.out.println(minDocLabel[1]+","+minDocLabel[0]);
					}
				}
				
//				ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringText.PerformUnSeupervisedSeededClusteringByGtmWordSim
//						(hmTrainDocsLabelBody, alDocLabel);
				
				//clusterEvaluation.EvalSemiSupervisedByAccOneToOneText(clusterResultConatainerText.FinalCluster);
				//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(clusterResultConatainerText.FinalCluster);
				
			}
			else{
				throw new Exception("Missmatch..");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void initializeKMeans() {
		try{
			kMeansClusteringArff.setArffFile(StackOverflowConstant.StackOverflowW2VecArffFile);
			kMeansClusteringArff.setK(StackOverflowConstant.NumberOfClusters);
			kMeansClusteringArff.setSeed(0);
			kMeansClusteringArff.setSeedInitializationMethod(1); //0,1,2,3
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
