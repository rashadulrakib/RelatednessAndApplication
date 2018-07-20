package dal.clustering.document.dataset.biomedical;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.entities.InstanceText;

public class BioMedicalExternalEvaluation {
	BioMedicalUtil bioMedicalUtil;
	ClusterEvaluation clusterEvaluation;
	
	public BioMedicalExternalEvaluation(){
		bioMedicalUtil = new BioMedicalUtil();
		clusterEvaluation = new ClusterEvaluation(bioMedicalUtil.docClusterUtil);
	}
	
	public void ExternalEvaluate() {
		try{
			//2n-biomedical-w2vecitr-bioasq2018-sparse-20000-15-labels 41.8
			//2n-biomedical-w2vecitr-bioasq2018-sparse-20000-6-labels 41.215
			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedical-bioasq2018-400-add-dense-12265-labels";
			//String externalClusteringResultFile = "D:\\PhD\\Python+PCA\\out";
			
			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\2n-biomedical-w2vec-add-sparse-20000-0-labels";
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\2n-biomedical-w2vec-bioasq-sparse-20000-0-labels";
			
			ArrayList<String> clusterLables = bioMedicalUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
	
			ArrayList<String []> alBodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
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
				
//				lastClusters.remove("11");
//				lastClusters.remove("9");
//				lastClusters.remove("5");
//				lastClusters.remove("10");
//				lastClusters.remove("13");
//				lastClusters.remove("16");
//				lastClusters.remove("3");
//				lastClusters.remove("1");
//				lastClusters.remove("6");
//				lastClusters.remove("4");
				
				//clusterEvaluation.ClusterEvaluationGeneratorTextExternal(lastClusters);
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
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
				String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\BTM\\labels\\biomedical-BTM-labels-20000-"+fid;
			
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
		
				ArrayList<String []> alBodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
				ArrayList<InstanceText> alInsts = new ArrayList<InstanceText>();
				
				if(clusterLables.size()== alBodyLabel.size()){
					
					//ArrayList<String> hmOrderedLabels = new ArrayList<String>();
					
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
			}
			
			System.out.println("maxPurity="+maxPurity+",maxFile="+maxFile);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ExternalEvaluateRAD() {
		try{
			BufferedReader br =  new BufferedReader(new FileReader
					("D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\biomedicalraw_ensembele_traintest"));
			
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
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = bioMedicalUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(instTexts, false);
			
//			ArrayList<InstanceText> rejected = new ArrayList<InstanceText>();
//			
//			for(String key: lastClusters.keySet()){
//				ArrayList<InstanceText> commonIns = lastClusters.get(key);
//				if(commonIns.size()>1000){
//					List<InstanceText> commonInsSub = commonIns.subList(1000, commonIns.size()-1);
//					rejected.addAll(commonInsSub);
//					commonIns.removeAll(commonInsSub);
//					lastClusters.put(key, commonIns);
//				}
//			}
			
			
			
//			String [] targetClsuter = new String[] {"1","2","3","4", "5", "7", "8", "10",
//					"11","12","13","14","16","17","18","20","19","15","9","6","11","2"};
//			
//			Random r = new Random();
//			
//			for(InstanceText instText: rejected){
//				
//				int ind=  r.nextInt((19 - 0) + 1) + 0;
//				String label = targetClsuter[ind];
//				lastClusters.get(label).add(instText);
//				lastClusters.put(label, lastClusters.get(label)); 
//			}
			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
}
