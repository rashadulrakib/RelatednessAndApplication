package dal.clustering.document.shared.cluster;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.DocClusterConstant;
import dal.clustering.document.shared.DocClusterUtil;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.compute.ComputeUtil;

public class SemiSupervisedClusteringW2Vec {
	
	DocClusterUtil docClusterUtil;
	
	public SemiSupervisedClusteringW2Vec(DocClusterUtil docClusterUtil){
		this.docClusterUtil = docClusterUtil;
	}

	public ClusterResultConatainerVector PerformSemiSeuperVisedClustering(LinkedHashMap<String, ArrayList<double[]>> trainW2Vecs, ArrayList<InstanceW2Vec> testW2Vecs) {
		try{
			LinkedHashMap<String, double[]> InitialCenters = docClusterUtil.ComputeInitialCenters(trainW2Vecs);
			
			return ComputeSemiSupervisedClusters(InitialCenters, testW2Vecs);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	private ClusterResultConatainerVector ComputeSemiSupervisedClusters(LinkedHashMap<String, double[]> centers, ArrayList<InstanceW2Vec> testW2Vecs) {
		
		ClusterResultConatainerVector clusterResultConatainer = new ClusterResultConatainerVector();
		ClusterEvaluation clusterEvaluation = new ClusterEvaluation(docClusterUtil);
		
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
					
					if(minLabel.trim().length()<=0){
						continue;
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
				lastClusters = newClusters;
				lastCenters = newCenetrs;
				
				System.out.println("totalMinDist="+totalMinDist);
				clusterEvaluation.EvalSemiSupervisedByAccOneToOneVector(newClusters);
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(newClusters);
				
				converge = docClusterUtil.IsConverge(newCenetrs, centers);
				centers = newCenetrs;
			}
			
			LinkedHashMap<String, InstanceW2Vec> InstanceClosestToCenters = getInstanceClosestToCenters(lastClusters, lastCenters);
			
			clusterResultConatainer.ClusteredInstancesTest = testW2Vecs;
			clusterResultConatainer.FinalCluster = lastClusters;
			clusterResultConatainer.InstanceClosestToCenter = InstanceClosestToCenters;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clusterResultConatainer;
	}

	private LinkedHashMap<String, InstanceW2Vec> getInstanceClosestToCenters(LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClusters,
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
	
}
