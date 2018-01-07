package dal.clustering.document.googlewebsnippets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.entities.InstanceText;

public class WebSnippetExternalEvaluation {

	GooglewebSnippetUtil googlewebSnippetUtil;
	ClusterEvaluation clusterEvaluation;
	
	public WebSnippetExternalEvaluation(){
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		clusterEvaluation = new ClusterEvaluation();
	}
	
	public void ExternalEvaluate() {
		try{
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\2018\\jan\\web-snippet-2280-r-hc-label.txt";
			
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
	
			ArrayList<String []> alBodyLabel = googlewebSnippetUtil.getDocsGoogleWebSnippetFlat();
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
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
