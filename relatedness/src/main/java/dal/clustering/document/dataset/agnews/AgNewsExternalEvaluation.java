package dal.clustering.document.dataset.agnews;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.entities.InstanceText;

public class AgNewsExternalEvaluation {
	AgNewsUtil agNewsUtil;
	ClusterEvaluation clusterEvaluation;
	
	public AgNewsExternalEvaluation(){
		agNewsUtil = new AgNewsUtil();
		clusterEvaluation = new ClusterEvaluation(agNewsUtil.docClusterUtil);
	}
	
	public void ExternalEvaluate() {
		try{
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\sparseMatrix-tfidf-agnews-weightCenterBased-8000-labels";
			
			ArrayList<String> clusterLables = agNewsUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
	
			ArrayList<String []> alBodyLabel = agNewsUtil.getAgNewsFlat();
			ArrayList<InstanceText> alInsts = new ArrayList<InstanceText>();
			
			if(clusterLables.size()== alBodyLabel.size()){
				
				//ArrayList<String> hmOrderedLabels = new ArrayList<String>();
				
				for(int i=0;i<alBodyLabel.size();i++ ){
					InstanceText newInst = new InstanceText();
					newInst.OriginalLabel = alBodyLabel.get(i)[1];
					newInst.Text = alBodyLabel.get(i)[0];
					newInst.ClusteredLabel = clusterLables.get(i);
					alInsts.add(newInst);
					
//					if(!hmOrderedLabels.contains(alBodyLabel.get(i)[1])){
//						hmOrderedLabels.add(alBodyLabel.get(i)[1]);
//					}
				}
				
				//convert clusteredlabels from index to label
//				for(InstanceText inst: alInsts){
//					int labelid = Integer.parseInt(inst.ClusteredLabel)-1;
//					inst.ClusteredLabel = hmOrderedLabels.get(labelid);
//				}
				
				for(InstanceText inst: alInsts){
					if(!lastClusters.containsKey(inst.ClusteredLabel)){
						ArrayList<InstanceText> al = new ArrayList<InstanceText>();
						al.add(inst);
						lastClusters.put(inst.ClusteredLabel, al);
					}else{
						ArrayList<InstanceText> al = lastClusters.get(inst.ClusteredLabel);
						al.add(inst);
						lastClusters.put(inst.ClusteredLabel, al);
					}
				}
				
				clusterEvaluation.ClusterEvaluationGeneratorTextExternal(lastClusters);
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			}
			else{
				System.out.println(clusterLables.size()+","+alBodyLabel.size());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ExternalEvaluateLoop() {
		try{
			
			double maxPurity = Double.MIN_VALUE;
			String maxFile = "";
			
			for(int fid=1;fid<=500;fid++){
				String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\BTM\\labels\\agnews-BTM-labels-8000-"+fid;
				
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
				
				LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
		
				ArrayList<String []> alBodyLabel = agNewsUtil.getAgNewsFlat();
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
						if(!lastClusters.containsKey(inst.ClusteredLabel)){
							ArrayList<InstanceText> al = new ArrayList<InstanceText>();
							al.add(inst);
							lastClusters.put(inst.ClusteredLabel, al);
						}else{
							ArrayList<InstanceText> al = lastClusters.get(inst.ClusteredLabel);
							al.add(inst);
							lastClusters.put(inst.ClusteredLabel, al);
						}
					}
					
					double purity = clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
					if(maxPurity<purity){
						clusterEvaluation.ClusterEvaluationGeneratorTextExternal(lastClusters);
						maxPurity = purity;
						maxFile = externalClusteringResultFile;
					}
				}
				else{
					System.out.println(clusterLables.size()+","+alBodyLabel.size());
				}
			}
			
			System.out.println("maxPurity="+maxPurity+",maxFile="+maxFile);
						
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
