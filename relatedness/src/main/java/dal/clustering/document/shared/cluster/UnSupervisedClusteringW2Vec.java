package dal.clustering.document.shared.cluster;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.DocClusterConstant;
import dal.clustering.document.shared.DocClusterUtil;
import dal.clustering.document.shared.DocClusterUtilW2Vec;
import dal.clustering.document.shared.entities.ClusterResultConatainerText;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;
import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.compute.ComputeUtil;

public class UnSupervisedClusteringW2Vec {
		
	public DocClusterUtilW2Vec docClusterUtilW2Vec;
	HashSet<String> uniqueWords;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	DocClusterUtil docClusterUtil;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	
	public UnSupervisedClusteringW2Vec(HashSet<String> uniqueWords, ArrayList<String[]> aldocsBodeyLabelFlat,
			LinkedHashMap<String, ArrayList<String>> docsLabelBodyList, DocClusterUtil docClusterUtil){
		this.uniqueWords = uniqueWords;
		this.aldocsBodeyLabelFlat = aldocsBodeyLabelFlat;
		this.docsLabelBodyList = docsLabelBodyList;
		this.docClusterUtil = docClusterUtil;
		docClusterUtilW2Vec = new DocClusterUtilW2Vec(uniqueWords, docClusterUtil);
	}

	public ClusterResultConatainerText PerformUnSeuperVisedSeededClusteringByW2VecFollowingGtm(
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody, ArrayList<String[]> alDocLabelFlat) {
		
		ClusterResultConatainerText clusterResultConatainerText = new ClusterResultConatainerText();
		
		try{
			
			ClusterEvaluation tempclusterEvaluation = new ClusterEvaluation();
			
			boolean converge = false;
			int iter = 0;
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
			
			double minimumSSE = Double.MAX_VALUE;
			
			HashSet<String> usedMedoidTexts = new HashSet<String>();
			
			while(!converge && iter++< DocClusterConstant.KMedoidMaxIteration){
				LinkedHashMap<String, ArrayList<InstanceText>> newClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
			
				double newSSE = 0;
				
				for(String key: hmTrainDocsLabelBody.keySet()){
					usedMedoidTexts.addAll(hmTrainDocsLabelBody.get(key));
				}
				
				for(String[] bodyLabel: alDocLabelFlat){
					String body = bodyLabel[0];
					String label = bodyLabel[1];
					
					//last item is the center-medoid
					String closestLabel = "";
					double closestDist = Double.MAX_VALUE;
					
					for(String centerLabel: hmTrainDocsLabelBody.keySet()){
						ArrayList<String> allCentes = hmTrainDocsLabelBody.get(centerLabel);
						String centerText = allCentes.get(allCentes.size()-1);
						
						double dist = 1.0-docClusterUtilW2Vec.ComputeTextSimilarityByW2VecFollowingGtm(centerText, body);
						
						if(closestDist>dist){
							closestDist = dist;
							closestLabel = centerLabel;
						}
					}
					
					if(closestLabel.trim().length()<=0){
						continue;
					}
					
					newSSE = newSSE + closestDist;
					
					InstanceText newInst = new InstanceText(); 
					newInst.OriginalLabel = label;
					newInst.Text = body;
					newInst.ClusteredLabel = closestLabel;
					
					if(!newClusters.containsKey(closestLabel)){
						ArrayList<InstanceText> newAl = new ArrayList<InstanceText>();
						newAl.add(newInst);
						newClusters.put(closestLabel, newAl);
					}
					else{
						ArrayList<InstanceText> newAl = newClusters.get(closestLabel);
						newAl.add(newInst);
						newClusters.put(closestLabel, newAl);
					}
				}
				
				//temp
				
				tempclusterEvaluation.EvalSemiSupervisedByAccOneToOneText(newClusters);
				tempclusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(newClusters);
				//temp
				
				if(minimumSSE>newSSE){
					System.out.println("newSSE="+newSSE+",minimumSSE="+minimumSSE);
					
					minimumSSE = newSSE;
					lastClusters = newClusters;
					
				}
				
				converge = true;
			}
			
			clusterResultConatainerText.FinalCluster = lastClusters;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clusterResultConatainerText;
	}

	public ClusterResultConatainerVector PerformUnSeuperVisedSeededClusteringByW2VecAverageVec(
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody,
			ArrayList<String[]> alDocLabelFlat) {
	
		try{
			LinkedHashMap<String, ArrayList<double[]>> trainW2Vecs = docClusterUtilW2Vec.populateW2VecDocsList(hmTrainDocsLabelBody);
			ArrayList<InstanceW2Vec> testW2Vecs = docClusterUtilW2Vec.populateW2VecDocsFlat(alDocLabelFlat);
			
			LinkedHashMap<String, double[]> initialCenters = ComputeUtil.ComputeInitialCenters(trainW2Vecs);
			
			return ComputeSemiSupervisedClusters(initialCenters, testW2Vecs);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	private ClusterResultConatainerVector ComputeSemiSupervisedClusters(LinkedHashMap<String, double[]> centers, ArrayList<InstanceW2Vec> testW2Vecs) {
		
		ClusterResultConatainerVector clusterResultConatainer = new ClusterResultConatainerVector();
		ClusterEvaluation clusterEvaluation = new ClusterEvaluation();
		
		try{
			boolean converge = false;
			int iter = 0;
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
			LinkedHashMap<String, double[]> lastCenters = new LinkedHashMap<String, double[]>();
			
			while(!converge && iter++< DocClusterConstant.KMeansMaxIteration){
				
				LinkedHashMap<String, ArrayList<InstanceW2Vec>> newClusters = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
				
				double totalMinDist = 0;
				
				for(InstanceW2Vec instanceW2Vec: testW2Vecs){
					
					double minDist = Double.MAX_VALUE;
					String minLabel = "";
					for(String label: centers.keySet()){
						double distFromInstToCenter = ComputeUtil.ComputeEuclidianDistance(centers.get(label), instanceW2Vec.Features);
						if(minDist>distFromInstToCenter){
							minDist= distFromInstToCenter;
							minLabel = label;
						}
					}
					
					instanceW2Vec.ClusteredLabel = minLabel;
					
					totalMinDist = totalMinDist + minDist;
					
					if(!newClusters.containsKey(minLabel)){
						ArrayList<InstanceW2Vec> newAl = new ArrayList<InstanceW2Vec>();
						newAl.add(instanceW2Vec);
						newClusters.put(minLabel, newAl);
					}
					else{
						ArrayList<InstanceW2Vec> newAl = newClusters.get(minLabel);
						newAl.add(instanceW2Vec);
						newClusters.put(minLabel, newAl);
					}
				}
				
				//recalculate centers;
				LinkedHashMap<String, double[]> newCenetrs = docClusterUtil.ReComputeCenters(newClusters);
				
				System.out.println("totalMinDist="+totalMinDist+",itr="+iter);
				clusterEvaluation.EvalSemiSupervisedByAccOneToOneVector(newClusters);
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(newClusters);
				
				converge = docClusterUtil.IsConverge(newCenetrs, centers);
				centers = newCenetrs;
				
				lastClusters = newClusters;
				lastCenters = newCenetrs;
			}
			
			LinkedHashMap<String, InstanceW2Vec> InstanceClosestToCenters = docClusterUtil.GetInstanceClosestToCentersW2Vec(lastClusters, lastCenters);
			
			clusterResultConatainer.ClusteredInstancesTest = testW2Vecs;
			clusterResultConatainer.FinalCluster = lastClusters;
			clusterResultConatainer.InstanceClosestToCenter = InstanceClosestToCenters;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clusterResultConatainer;
	}
	
}

