package dal.clustering.document.dataset.googlewebsnippets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.entities.InstanceText;

public class WebSnippetExternalEvaluation {

	GooglewebSnippetUtil googlewebSnippetUtil;
	ClusterEvaluation clusterEvaluation;
	
	public WebSnippetExternalEvaluation(){
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		clusterEvaluation = new ClusterEvaluation(googlewebSnippetUtil.docClusterUtil);
	}
	
	public void ExternalEvaluate() {
		try{
			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\websnippet-BTM-labels-2208";
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\2n-websnippet-w2vec-glove-sparse-2280-0-labels"; //\\STC2\\stc2-le_searchsnippet12340-labels";
			
			ArrayList<String> clusterLables = googlewebSnippetUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
	
			ArrayList<String []> alBodyLabel = googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
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
	
	public void ExternalEvaluateList(){
		try{
			ArrayList<ArrayList<String[]>> aldocsBodeyLabelFlatList = googlewebSnippetUtil.GetDocsGoogleWebSnippetFlatList();
			
			for(int i=5;i<aldocsBodeyLabelFlatList.size();i++){
				
				String labelFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\websnippet-w2vec-alpha-2052-"+i+"-labels";
				BufferedReader br =  new BufferedReader(new FileReader(labelFile));
				
				String line="";
				ArrayList<String> clusterLables = new ArrayList<String>();
				
				while((line=br.readLine()) != null) {
			        line = line.trim();
			        if(line.isEmpty()) continue;
			        
			        String clusterGroups [] = line.split(",");
			        
			        ArrayList<String> labelGr = new ArrayList<String>();
			        for(String gr: clusterGroups){
			        	labelGr.add(Integer.toString((int)Double.parseDouble(gr)));
			        }
			        
			        clusterLables.addAll(labelGr);
				}
				br.close();
				
				LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
				ArrayList<InstanceText> alInsts = new ArrayList<InstanceText>();
				ArrayList<String[]> aldocsBodeyLabelFlat = aldocsBodeyLabelFlatList.get(i);
				
				if(clusterLables.size()== aldocsBodeyLabelFlat.size()){
					
					for(int ii=0;ii<aldocsBodeyLabelFlat.size();ii++ ){
						InstanceText newInst = new InstanceText();
						newInst.OriginalLabel = aldocsBodeyLabelFlat.get(ii)[1];
						newInst.Text = aldocsBodeyLabelFlat.get(ii)[0];
						newInst.ClusteredLabel = clusterLables.get(ii);
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
					
					clusterEvaluation.ClusterEvaluationGeneratorTextExternal(lastClusters);
					//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ExternalEvaluateLoop() {
		try{
			
			double maxPurity = Double.MIN_VALUE;
			String maxFile = "";
			
			for(int fid=1;fid<=2000;fid++){
				//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\web-snippets\\BTM\\labels\\web-snippets-BTM-labels-12340-"+fid;
				String externalClusteringResultFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/BTM/labels/websnippet-BTM-labels-2280-"+fid;
				
				BufferedReader br =  new BufferedReader(new FileReader(externalClusteringResultFile));
				
				String line="";
				ArrayList<String> clusterLables = new ArrayList<String>();
				
				while((line=br.readLine()) != null) {
			        line = line.trim();
			        if(line.isEmpty()) continue;
			        
			        String clusterGroups [] = line.split(",");
			        
			        ArrayList<String> labelGr = new ArrayList<String>();
			        for(String gr: clusterGroups){
			        	labelGr.add(Integer.toString((int)Double.parseDouble(gr)));
			        }
			        
			        //clusterLables.addAll(Arrays.asList(clusterGroups));
			        clusterLables.addAll(labelGr);
				}
				br.close();
				
				LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
		
				ArrayList<String []> alBodyLabel = googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
				ArrayList<InstanceText> alInsts = new ArrayList<InstanceText>();
				
				if(clusterLables.size()== alBodyLabel.size()){
					
					//ArrayList<String> hmOrderedLabels = new ArrayList<String>();
					
					for(int i=0;i<alBodyLabel.size();i++ ){
						InstanceText newInst = new InstanceText();
						newInst.OriginalLabel = alBodyLabel.get(i)[1];
						newInst.Text = alBodyLabel.get(i)[0];
						newInst.ClusteredLabel = clusterLables.get(i);
						alInsts.add(newInst);
						
//						if(!hmOrderedLabels.contains(alBodyLabel.get(i)[1])){
//							hmOrderedLabels.add(alBodyLabel.get(i)[1]);
//						}
					}
					
					//convert clusteredlabels from index to label
//					for(InstanceText inst: alInsts){
//						int labelid = Integer.parseInt(inst.ClusteredLabel)-1;
//						inst.ClusteredLabel = hmOrderedLabels.get(labelid);
//					}
					
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
					//
				}
			}
			
			System.out.println("maxPurity="+maxPurity+",maxFile="+maxFile);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ExternalEvaluateRAD() {
		try{
			BufferedReader br =  new BufferedReader(new FileReader
					("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\data-web-snippetsraw_ensembele_traintest"));
			
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
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = googlewebSnippetUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(instTexts, false);
						
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
