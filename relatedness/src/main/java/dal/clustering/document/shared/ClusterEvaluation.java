package dal.clustering.document.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.general.UtilsShared;

public class ClusterEvaluation {

	DocClusterUtil docClusterUtil;
	
	public ClusterEvaluation(DocClusterUtil docClusterUtil) {
		this.docClusterUtil = docClusterUtil; 
	}

	public void EvalSemiSupervisedByAccOneToOneVector(LinkedHashMap<String, ArrayList<InstanceW2Vec>> finalCluster){
		try{
			int trueClustered = 0;
			int totalItems = 0;
			
			for(String label: finalCluster.keySet()){
				ArrayList<InstanceW2Vec> instants = finalCluster.get(label);
				totalItems = totalItems+ instants.size();
				for(InstanceW2Vec inst: instants){
					if(inst.ClusteredLabel.equals(inst.OriginalLabel)){
						trueClustered++;
					}
				}
			}

			
			System.out.println("Semi-supervised acc-one-to-one-vector="+ (double)trueClustered/totalItems);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void EvalSemiSupervisedByAccOneToOneText(LinkedHashMap<String, ArrayList<InstanceText>> finalCluster){
		try{
			int trueClustered = 0;
			int totalItems = 0;
			
			for(String label: finalCluster.keySet()){
				ArrayList<InstanceText> instants = finalCluster.get(label);
				totalItems = totalItems+ instants.size();
				for(InstanceText inst: instants){
					if(inst.ClusteredLabel.equals(inst.OriginalLabel)){
						trueClustered++;
					}
				}
			}

			
			System.out.println("Semi-supervised acc-one-to-one-text="+ (double)trueClustered/totalItems);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public double EvalSemiSupervisedByPurityMajorityVotingText(LinkedHashMap<String, ArrayList<InstanceText>> finalCluster) {
		
		double purity = 0;
		
		try{
			int matchInCluster = 0;
			int totalItems = 0;
			int maxGroupSizeSum =0;
			for(String label: finalCluster.keySet()){
				ArrayList<InstanceText> instants = finalCluster.get(label);
				totalItems = totalItems+ instants.size();
				
				HashMap<String, ArrayList<InstanceText>> clusterGroupsOriginalLabel = docClusterUtil.GetClusterGroupsTextByLabel(instants, true);
				int maxMemInGroupSize = Integer.MIN_VALUE;
				String maxMemOriginalLabel = "";
				for(String afterClusterLabel: clusterGroupsOriginalLabel.keySet()){
					if(maxMemInGroupSize<clusterGroupsOriginalLabel.get(afterClusterLabel).size()){
						maxMemInGroupSize= clusterGroupsOriginalLabel.get(afterClusterLabel).size();
						maxMemOriginalLabel = afterClusterLabel;
					}
					//System.out.println(afterClusterLabel+","+clusterGroupsOriginalLabel.get(afterClusterLabel).size());
				}
				
				maxGroupSizeSum=maxGroupSizeSum+ maxMemInGroupSize;
				
				for(InstanceText inst: instants){
					if(maxMemOriginalLabel.equals(inst.ClusteredLabel)){
						matchInCluster++;
					}
				}
				System.out.println("label="+label+",items="+finalCluster.get(label).size()+", acc="+(double)maxMemInGroupSize/finalCluster.get(label).size()+",max label="+maxMemOriginalLabel);
			}
			
			System.out.println("acc -majority-text="+ (double)maxGroupSizeSum/totalItems);
			purity = (double)maxGroupSizeSum/totalItems;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return purity;
	}
	
	public double EvalSemiSupervisedByPurityMajorityVotingTextExternal(LinkedHashMap<String, ArrayList<InstanceText>> lastClusters) {
		double purity = 0;
		try{
			purity = EvalSemiSupervisedByPurityMajorityVotingText(lastClusters);
		}catch(Exception e){
			e.printStackTrace();
		}
		return purity;
	}
	
	
	public void EvalSemiSupervisedByPurityMajorityVotingVectorExternal(
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> finalCluster) {
		try{
			EvalSemiSupervisedByPurityMajorityVotingVector(finalCluster);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void EvalSemiSupervisedByPurityMajorityVotingVector(LinkedHashMap<String, ArrayList<InstanceW2Vec>> finalCluster) {
		try{
			
			//int matchInCluster = 0;
			int totalItems = 0;
			int maxGroupSizeSum =0;
			for(String label: finalCluster.keySet()){
				ArrayList<InstanceW2Vec> instants = finalCluster.get(label);
				totalItems = totalItems+ instants.size();
				
				HashMap<String, ArrayList<InstanceW2Vec>> clusterGroups = docClusterUtil.GetClusterGroupsVectorByLabel(instants, true);
				int maxMemInGroupSize = Integer.MIN_VALUE;
				//String maxMemOriginalLabel = "";
				for(String afterClusterLabel: clusterGroups.keySet()){
					if(maxMemInGroupSize<clusterGroups.get(afterClusterLabel).size()){
						maxMemInGroupSize= clusterGroups.get(afterClusterLabel).size();
						//maxMemOriginalLabel = afterClusterLabel;
					}
				}
				
				maxGroupSizeSum=maxGroupSizeSum+ maxMemInGroupSize;
				
//				for(InstanceW2Vec inst: instants){
//					if(maxMemOriginalLabel.equals(inst.ClusteredLabel)){
//						matchInCluster++;
//					}
//				}
				//System.out.println("label="+label+",items="+finalCluster.get(label).size()+", acc="+(double)maxMemInGroupSize/finalCluster.get(label).size());
			}
			
			System.out.println("acc-majority-vector="+ (double)maxGroupSizeSum/totalItems);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterEvaluationGeneratorText(LinkedHashMap<String, ArrayList<InstanceText>> finalCluster) {
		try{
			//int matchInCluster = 0;
			int totalItems = 0;
			int maxGroupSizeSum =0;
			
			TreeMap<String, ArrayList<InstanceText>> sortedFinalCluster = new TreeMap<String, ArrayList<InstanceText>>(finalCluster);
			
			TreeMap<String, Integer> hmSortedLabelInds = new TreeMap<String, Integer>();
			int labelInd =0;
			for(String label: sortedFinalCluster.keySet()){
				hmSortedLabelInds.put(label, labelInd++);
			}
			
			ArrayList<Integer> preds = new  ArrayList<Integer>();
			ArrayList<Integer> trues = new ArrayList<Integer>();
			
			for(String label: sortedFinalCluster.keySet()){
				ArrayList<InstanceText> instants = sortedFinalCluster.get(label);
				totalItems = totalItems+ instants.size();
				
				HashMap<String, ArrayList<InstanceText>> clusterGroupsOriginalLabel = docClusterUtil.GetClusterGroupsTextByLabel(instants, true);
				int maxMemInGroupSize = Integer.MIN_VALUE;
				String maxMemOriginalLabel = "";
				for(String afterClusterLabel: clusterGroupsOriginalLabel.keySet()){
					if(maxMemInGroupSize<clusterGroupsOriginalLabel.get(afterClusterLabel).size()){
						maxMemInGroupSize= clusterGroupsOriginalLabel.get(afterClusterLabel).size();
						maxMemOriginalLabel = afterClusterLabel;
					}
				}
				
				maxGroupSizeSum=maxGroupSizeSum+ maxMemInGroupSize;
				
				for(InstanceText instTtx: instants){
					trues.add(hmSortedLabelInds.get(maxMemOriginalLabel));
					preds.add(hmSortedLabelInds.get( label));
				}
				
//				for(InstanceText inst: instants){
//					if(maxMemOriginalLabel.equals(inst.ClusteredLabel)){
//						matchInCluster++;
//					}
//				}
			}
			
			
			System.out.println("labels_pred = "+preds);
			System.out.println("labels_true = "+trues);
			
			//cooccurrence_matrix = np.array([[ 5,  1,  2], [ 1,  4,  0], [ 0,  1,  3]])
			
			//create matrix
			double [][] matrixColRow = new double[sortedFinalCluster.keySet().size()][];
			int rowId = 0;
			for(String label: sortedFinalCluster.keySet()){
				ArrayList<InstanceText> instants = sortedFinalCluster.get(label);
				
				HashMap<String, ArrayList<InstanceText>> clusterGroupsOriginalLabel = docClusterUtil.GetClusterGroupsTextByLabel(instants, true);
				TreeMap<String, ArrayList<InstanceText>> sortedClusterGroupsOriginalLabel = new TreeMap<String, ArrayList<InstanceText>>(clusterGroupsOriginalLabel);
				
				double [] col = new double[sortedFinalCluster.keySet().size()];
				for(String sortedLabel: sortedClusterGroupsOriginalLabel.keySet()){
					col[hmSortedLabelInds.get(sortedLabel)] = sortedClusterGroupsOriginalLabel.get(sortedLabel).size();
				}
				matrixColRow[rowId++] = col;
			}
			
			double [][] transposed = UtilsShared.TransposeMatrix(matrixColRow);
			
			System.out.print("cooccurrence_matrix=np.array([");
			
			for(int i=0;i<transposed.length;i++){
				System.out.print("[");
				for(int j=0;j<transposed[i].length;j++){
					System.out.print(transposed[i][j]+",");
				}
				System.out.println("]");
			}
			
			System.out.print("])");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterEvaluationGeneratorTextExternal(LinkedHashMap<String, ArrayList<InstanceText>> finalCluster) {
		try{
			//int matchInCluster = 0;
			int totalItems = 0;
			int maxGroupSizeSum =0;
			
			TreeMap<String, ArrayList<InstanceText>> sortedFinalCluster = new TreeMap<String, ArrayList<InstanceText>>(finalCluster);
			
			TreeMap<String, Integer> hmSortedLabelInds = new TreeMap<String, Integer>();
			
			ArrayList<InstanceText> mergedInstants = new ArrayList<InstanceText>();
			//merge the instances
			for(String label: sortedFinalCluster.keySet()){
				ArrayList<InstanceText> instants = sortedFinalCluster.get(label);
				mergedInstants.addAll(instants);
			}
			
			TreeMap<String, ArrayList<InstanceText>> sortedClusterGroupsOriginalLabelByMerged = new TreeMap<String, ArrayList<InstanceText>>(docClusterUtil.GetClusterGroupsTextByLabel(mergedInstants, true));
			//end merge the instances
			
			int labelInd =0;
			for(String label: sortedClusterGroupsOriginalLabelByMerged.keySet()){
				hmSortedLabelInds.put(label, labelInd++);
			}
			
			ArrayList<Integer> preds = new  ArrayList<Integer>();
			ArrayList<Integer> trues = new ArrayList<Integer>();
			
			for(String label: sortedFinalCluster.keySet()){
				ArrayList<InstanceText> instants = sortedFinalCluster.get(label);
				totalItems = totalItems+ instants.size();
				
				HashMap<String, ArrayList<InstanceText>> clusterGroupsOriginalLabel = docClusterUtil.GetClusterGroupsTextByLabel(instants, true);
				int maxMemInGroupSize = Integer.MIN_VALUE;
				String maxMemOriginalLabel = "";
				for(String afterClusterLabel: clusterGroupsOriginalLabel.keySet()){
					if(maxMemInGroupSize<clusterGroupsOriginalLabel.get(afterClusterLabel).size()){
						maxMemInGroupSize= clusterGroupsOriginalLabel.get(afterClusterLabel).size();
						maxMemOriginalLabel = afterClusterLabel;
					}
				}
				
				maxGroupSizeSum=maxGroupSizeSum+ maxMemInGroupSize;
				
				for(InstanceText instTtx: instants){
					trues.add(hmSortedLabelInds.get(instTtx.OriginalLabel));
					preds.add(hmSortedLabelInds.get( maxMemOriginalLabel));
				}
			}
			
			System.out.println("labels_pred = "+preds);
			System.out.println("labels_true = "+trues);
			
			if(sortedFinalCluster.keySet().size()== sortedClusterGroupsOriginalLabelByMerged.keySet().size()){

				//create matrix
				double [][] matrixColRow = new double[sortedClusterGroupsOriginalLabelByMerged.keySet().size()][];
				int rowId = 0;
				for(String label: sortedFinalCluster.keySet()){
					ArrayList<InstanceText> instants = sortedFinalCluster.get(label);
					
					TreeMap<String, ArrayList<InstanceText>> clusterGroupsOriginalLabel = new TreeMap<String, ArrayList<InstanceText>>(docClusterUtil.GetClusterGroupsTextByLabel(instants, true));
					//TreeMap<String, ArrayList<InstanceW2Vec>> sortedClusterGroupsOriginalLabel = new TreeMap<String, ArrayList<InstanceW2Vec>>(clusterGroupsOriginalLabel);
					
					double [] col = new double[sortedClusterGroupsOriginalLabelByMerged.keySet().size()];
					
//					for(String keyInd: hmSortedLabelInds.keySet()){
//						System.out.println(keyInd+","+hmSortedLabelInds.get(keyInd));
//					}
					
					for(String sortedLabel: clusterGroupsOriginalLabel.keySet()){
						//col[hmSortedLabelInds.get(sortedLabel)] = sortedClusterGroupsOriginalLabel.get(sortedLabel).size();
//						System.out.println("sortedLabel="+sortedLabel+","+clusterGroupsOriginalLabel.get(sortedLabel).size()+
//								","+hmSortedLabelInds.get(sortedLabel)+","+hmSortedLabelInds.containsKey(sortedLabel));
						//System.out.println();
						col[hmSortedLabelInds.get(sortedLabel)] = clusterGroupsOriginalLabel.get(sortedLabel).size();
						
					}
					matrixColRow[rowId++] = col;
					//System.out.println();
				}
				
				double [][] transposed = UtilsShared.TransposeMatrix(matrixColRow);
				
//				System.out.print("cooccurrence_matrix=np.array([");
//				
//				for(int i=0;i<transposed.length;i++){
//					System.out.print("[");
//					for(int j=0;j<transposed[i].length;j++){
//						System.out.print(transposed[i][j]+",");
//					}
//					System.out.println("]");
//				}
//				
//				System.out.print("])");
				
			}else{
				System.out.println("predicted clusters="+sortedFinalCluster.keySet().size()+", original clusters= "+sortedClusterGroupsOriginalLabelByMerged.keySet().size());
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterEvaluationGeneratorVectorExternal(LinkedHashMap<String, ArrayList<InstanceW2Vec>> finalCluster) {
		try{
			
				//int matchInCluster = 0;
				int totalItems = 0;
				int maxGroupSizeSum =0;
				
				TreeMap<String, ArrayList<InstanceW2Vec>> sortedFinalCluster = new TreeMap<String, ArrayList<InstanceW2Vec>>(finalCluster);
				
				TreeMap<String, Integer> hmSortedLabelInds = new TreeMap<String, Integer>();
				
				ArrayList<InstanceW2Vec> mergedInstants = new ArrayList<InstanceW2Vec>();
				//merge the instances
				for(String label: sortedFinalCluster.keySet()){
					ArrayList<InstanceW2Vec> instants = sortedFinalCluster.get(label);
					mergedInstants.addAll(instants);
				}
				
				TreeMap<String, ArrayList<InstanceW2Vec>> sortedClusterGroupsOriginalLabelByMerged = new TreeMap<String, ArrayList<InstanceW2Vec>>(docClusterUtil.GetClusterGroupsVectorByLabel(mergedInstants, true));
				//end merge the instances
				
				int labelInd =0;
				for(String label: sortedClusterGroupsOriginalLabelByMerged.keySet()){
					hmSortedLabelInds.put(label, labelInd++);
				}
				
				ArrayList<Integer> preds = new  ArrayList<Integer>();
				ArrayList<Integer> trues = new ArrayList<Integer>();
				
				for(String label: sortedFinalCluster.keySet()){
					ArrayList<InstanceW2Vec> instants = sortedFinalCluster.get(label);
					totalItems = totalItems+ instants.size();
					
					HashMap<String, ArrayList<InstanceW2Vec>> clusterGroupsOriginalLabel = docClusterUtil.GetClusterGroupsVectorByLabel(instants, true);
					int maxMemInGroupSize = Integer.MIN_VALUE;
					String maxMemOriginalLabel = "";
					for(String afterClusterLabel: clusterGroupsOriginalLabel.keySet()){
						if(maxMemInGroupSize<clusterGroupsOriginalLabel.get(afterClusterLabel).size()){
							maxMemInGroupSize= clusterGroupsOriginalLabel.get(afterClusterLabel).size();
							maxMemOriginalLabel = afterClusterLabel;
						}
					}
					
					maxGroupSizeSum=maxGroupSizeSum+ maxMemInGroupSize;
					
					for(InstanceW2Vec instVec: instants){
						trues.add(hmSortedLabelInds.get(instVec.OriginalLabel));
						preds.add(hmSortedLabelInds.get( maxMemOriginalLabel));
					}
				}
				
				System.out.println("labels_pred = "+preds);
				System.out.println("labels_true = "+trues);
				
				if(sortedFinalCluster.keySet().size()== sortedClusterGroupsOriginalLabelByMerged.keySet().size()){

					//create matrix
					double [][] matrixColRow = new double[sortedClusterGroupsOriginalLabelByMerged.keySet().size()][];
					int rowId = 0;
					for(String label: sortedFinalCluster.keySet()){
						ArrayList<InstanceW2Vec> instants = sortedFinalCluster.get(label);
						
						TreeMap<String, ArrayList<InstanceW2Vec>> clusterGroupsOriginalLabel = new TreeMap<String, ArrayList<InstanceW2Vec>>(docClusterUtil.GetClusterGroupsVectorByLabel(instants, true));
						//TreeMap<String, ArrayList<InstanceW2Vec>> sortedClusterGroupsOriginalLabel = new TreeMap<String, ArrayList<InstanceW2Vec>>(clusterGroupsOriginalLabel);
						
						double [] col = new double[sortedClusterGroupsOriginalLabelByMerged.keySet().size()];
						
						for(String keyInd: hmSortedLabelInds.keySet()){
							System.out.println(keyInd+","+hmSortedLabelInds.get(keyInd));
						}
						
						for(String sortedLabel: clusterGroupsOriginalLabel.keySet()){
							//col[hmSortedLabelInds.get(sortedLabel)] = sortedClusterGroupsOriginalLabel.get(sortedLabel).size();
//							System.out.println("sortedLabel="+sortedLabel+","+clusterGroupsOriginalLabel.get(sortedLabel).size()+
//									","+hmSortedLabelInds.get(sortedLabel)+","+hmSortedLabelInds.containsKey(sortedLabel));
							//System.out.println();
							col[hmSortedLabelInds.get(sortedLabel)] = clusterGroupsOriginalLabel.get(sortedLabel).size();
							
						}
						matrixColRow[rowId++] = col;
						System.out.println();
					}
					
					double [][] transposed = UtilsShared.TransposeMatrix(matrixColRow);
					
					System.out.print("cooccurrence_matrix=np.array([");
					
					for(int i=0;i<transposed.length;i++){
						System.out.print("[");
						for(int j=0;j<transposed[i].length;j++){
							System.out.print(transposed[i][j]+",");
						}
						System.out.println("]");
					}
					
					System.out.print("])");
					
				}else{
					System.out.println("predicted clusters="+sortedFinalCluster.keySet().size()+", original clusters= "+sortedClusterGroupsOriginalLabelByMerged.keySet().size());
				}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void ClusterEvaluationGeneratorVector(LinkedHashMap<String, ArrayList<InstanceW2Vec>> finalCluster) {
		try{
			//int matchInCluster = 0;
			int totalItems = 0;
			int maxGroupSizeSum =0;
			
			TreeMap<String, ArrayList<InstanceW2Vec>> sortedFinalCluster = new TreeMap<String, ArrayList<InstanceW2Vec>>(finalCluster);
			
			TreeMap<String, Integer> hmSortedLabelInds = new TreeMap<String, Integer>();
			int labelInd =0;
			for(String label: sortedFinalCluster.keySet()){
				hmSortedLabelInds.put(label, labelInd++);
			}
			
			ArrayList<Integer> preds = new  ArrayList<Integer>();
			ArrayList<Integer> trues = new ArrayList<Integer>();
			
			for(String label: sortedFinalCluster.keySet()){
				ArrayList<InstanceW2Vec> instants = sortedFinalCluster.get(label);
				totalItems = totalItems+ instants.size();
				
				HashMap<String, ArrayList<InstanceW2Vec>> clusterGroupsOriginalLabel = docClusterUtil.GetClusterGroupsVectorByLabel(instants, true);
				int maxMemInGroupSize = Integer.MIN_VALUE;
				String maxMemOriginalLabel = "";
				for(String afterClusterLabel: clusterGroupsOriginalLabel.keySet()){
					if(maxMemInGroupSize<clusterGroupsOriginalLabel.get(afterClusterLabel).size()){
						maxMemInGroupSize= clusterGroupsOriginalLabel.get(afterClusterLabel).size();
						maxMemOriginalLabel = afterClusterLabel;
					}
				}
				
				maxGroupSizeSum=maxGroupSizeSum+ maxMemInGroupSize;
				
				for(InstanceW2Vec instVec: instants){
					trues.add(hmSortedLabelInds.get(maxMemOriginalLabel));
					preds.add(hmSortedLabelInds.get( label));
				}
				
//				for(InstanceText inst: instants){
//					if(maxMemOriginalLabel.equals(inst.ClusteredLabel)){
//						matchInCluster++;
//					}
//				}
			}
			
			
			System.out.println("labels_pred = "+preds);
			System.out.println("labels_true = "+trues);
			
			//cooccurrence_matrix = np.array([[ 5,  1,  2], [ 1,  4,  0], [ 0,  1,  3]])
			
			//create matrix
			double [][] matrixColRow = new double[sortedFinalCluster.keySet().size()][];
			int rowId = 0;
			for(String label: sortedFinalCluster.keySet()){
				ArrayList<InstanceW2Vec> instants = sortedFinalCluster.get(label);
				
				HashMap<String, ArrayList<InstanceW2Vec>> clusterGroupsOriginalLabel = docClusterUtil.GetClusterGroupsVectorByLabel(instants, true);
				//TreeMap<String, ArrayList<InstanceW2Vec>> sortedClusterGroupsOriginalLabel = new TreeMap<String, ArrayList<InstanceW2Vec>>(clusterGroupsOriginalLabel);
				
				double [] col = new double[sortedFinalCluster.keySet().size()];
				
				for(String keyInd: hmSortedLabelInds.keySet()){
					System.out.println(keyInd+","+hmSortedLabelInds.get(keyInd));
				}
				
				for(String sortedLabel: clusterGroupsOriginalLabel.keySet()){
					//col[hmSortedLabelInds.get(sortedLabel)] = sortedClusterGroupsOriginalLabel.get(sortedLabel).size();
//					System.out.println("sortedLabel="+sortedLabel+","+clusterGroupsOriginalLabel.get(sortedLabel).size()+
//							","+hmSortedLabelInds.get(sortedLabel)+","+hmSortedLabelInds.containsKey(sortedLabel));
					//System.out.println();
					col[hmSortedLabelInds.get(sortedLabel)] = clusterGroupsOriginalLabel.get(sortedLabel).size();
					
				}
				matrixColRow[rowId++] = col;
				System.out.println();
			}
			
			double [][] transposed = UtilsShared.TransposeMatrix(matrixColRow);
			
			System.out.print("cooccurrence_matrix=np.array([");
			
			for(int i=0;i<transposed.length;i++){
				System.out.print("[");
				for(int j=0;j<transposed[i].length;j++){
					System.out.print(transposed[i][j]+",");
				}
				System.out.println("]");
			}
			
			System.out.print("])");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
}
