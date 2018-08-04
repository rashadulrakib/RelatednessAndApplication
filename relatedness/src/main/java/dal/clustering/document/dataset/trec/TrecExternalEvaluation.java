package dal.clustering.document.dataset.trec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.entities.InstanceText;

public class TrecExternalEvaluation {
	TrecUtil trecUtil;
	ClusterEvaluation clusterEvaluation;
	
	public TrecExternalEvaluation(){
		trecUtil = new TrecUtil();
		clusterEvaluation = new ClusterEvaluation(trecUtil.docClusterUtil);
	}
	
	public void ExternalEvaluate() {
		try{
			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\sparseMatrix-tfidf-agnews-weightCenterBased-8000-labels";
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\2n-trec-glove-sparse-0-labels";
			
			@SuppressWarnings("unchecked")
			ArrayList<String> clusterLables = trecUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
	
			ArrayList<String []> alBodyLabel = trecUtil.getTrecFlat();
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
				
				//clusterEvaluation.ClusterEvaluationGeneratorTextExternal(lastClusters);
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			}
			else{
				System.out.println(clusterLables.size()+","+alBodyLabel.size());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ExternalEvaluateRAD() {
		try{
			BufferedReader br =  new BufferedReader(new FileReader
					//("D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\4_1950"));
					("D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\trecraw_ensembele_traintest"));
			
			String line="";
			ArrayList<InstanceText> instTexts = new ArrayList<InstanceText>();
			
			while((line=br.readLine()) != null) {
		        line = line.trim();
		        if(line.isEmpty()) continue;
		        
		        String arr [] = line.split("\t");

		        String predl = arr[0];
		        String truel = arr[1];
		        String text = arr[2];
		        
		        InstanceText inst = new InstanceText();
		        inst.Text = text;
		        inst.OriginalLabel = truel;
		        inst.ClusteredLabel = predl;
		        
		        instTexts.add(inst);
			}
			br.close();
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = trecUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(instTexts, false);
						
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
