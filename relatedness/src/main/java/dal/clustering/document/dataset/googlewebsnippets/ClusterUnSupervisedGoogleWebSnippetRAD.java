package dal.clustering.document.dataset.googlewebsnippets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.entities.InstanceText;

public class ClusterUnSupervisedGoogleWebSnippetRAD extends ClusterGoogleWebSnippet {

	public void GenerateTrainTest() {
		try{
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\2n-websnippet-w2vec-glove-sparse-2280-0-labels"; //2n-web-snippet-glove-add-sparse-12340-0-labels";
			
			ArrayList<String []> alBodyLabel = googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			
			@SuppressWarnings("unchecked")
			ArrayList<String> clusterLables = googlewebSnippetUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			ArrayList<InstanceText> allInstTexts = googlewebSnippetUtil.docClusterUtil.CreateW2VecForTrainData(alBodyLabel, clusterLables);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = googlewebSnippetUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();
						
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			googlewebSnippetUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlier.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = googlewebSnippetUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\textsperlabel\\", 8); 
			
			//filter instants by outlier..
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
				ArrayList<String> outlierpreds = outliersByLabel.get(label);
				
				if(insts.size()!=outlierpreds.size()){
					System.out.println("Size not match for="+label);
					continue;
				}
				
				//if(insts.size()<=1540) continue;
				
				ArrayList<InstanceText> instOutLier = new ArrayList<InstanceText>();
				
				for(int i=0;i<outlierpreds.size();i++){
					String outPred = outlierpreds.get(i);
					if(outPred.equals("-1")){
						instOutLier.add(insts.get(i));
					}
				}
				
				insts.removeAll(instOutLier);
				testInstTexts.addAll(instOutLier);
				
				lastClusters.put(label, insts);
			}
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
			
				ArrayList<InstanceText> subInsts = new ArrayList<InstanceText>();;
				//if(insts.size()>1080)
				if(insts.size()>200)
				{
					subInsts.addAll(insts.subList(0, 200));
					testInstTexts.addAll(insts.subList(200, insts.size()));
					//testInstTexts.addAll(insts.subList(200, insts.size()));
					//testInstTexts.addAll(insts.subList(1540, insts.size()));
				}else{
					subInsts.addAll(insts);
				}
				trainInstTexts.addAll(subInsts);				
				lastClusters.put(label, subInsts);				
			}			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = googlewebSnippetUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(testInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = googlewebSnippetUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(trainInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\data-web-snippetsraw_ensembele_train"));											
			for(InstanceText inst: trainInstTexts){
				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
			}
			bw.close();
			
			bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\data-web-snippetsraw_ensembele_test"));											
			for(InstanceText inst: testInstTexts){
				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateTrainTest2List() {
		try{
			int maxIteration = 5; //15
			WebSnippetExternalEvaluation obj = new WebSnippetExternalEvaluation();
			
			for(int i=0;i<maxIteration;i++){
				System.out.println("iteration="+i);
				
				//for(int items = 1100; items<=1550;items=items+50)
				for(int items = 200; items<=280;items=items+20)
				{
					Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\improvedclassification.py");
					int exitVal = p.waitFor();
					System.out.println("Process status code="+exitVal);
					p.destroy();
					
					obj.ExternalEvaluateRAD();	
					
					GenerateTrainTest2(items);
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateTrainTest2(int portion) {
		try{
			
			String trainTestTextFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\data-web-snippetsraw_ensembele_traintest";			
			ArrayList<String[]> predTrueTexts = googlewebSnippetUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(trainTestTextFile);			
			ArrayList<InstanceText> allInstTexts = googlewebSnippetUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = googlewebSnippetUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();			
			
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			googlewebSnippetUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlier.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = googlewebSnippetUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\textsperlabel\\", 8); 
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
				ArrayList<String> outlierpreds = outliersByLabel.get(label);
				
				if(insts.size()!=outlierpreds.size()){
					System.out.println("Size not match for="+label);
					continue;
				}
				
				//if(insts.size()<=portion) continue;
				
				ArrayList<InstanceText> instOutLier = new ArrayList<InstanceText>();
				
				for(int i=0;i<outlierpreds.size();i++){
					String outPred = outlierpreds.get(i);
					if(outPred.equals("-1")){
						instOutLier.add(insts.get(i));
					}
				}
				
				insts.removeAll(instOutLier);
				testInstTexts.addAll(instOutLier);
				
				lastClusters.put(label, insts);
			}
			
			
			for(String label: lastClusters.keySet()){
				
				ArrayList<InstanceText> insts = lastClusters.get(label);
				ArrayList<InstanceText> subInsts = new ArrayList<InstanceText>();;
				
				if(insts.size()>portion){
					subInsts.addAll(insts.subList(0, portion));
					testInstTexts.addAll(insts.subList(portion, insts.size()));
				}else{
					subInsts.addAll(insts);
				}
				trainInstTexts.addAll(subInsts);	
				lastClusters.put(label, subInsts);
			}			
			
			//LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = bioMedicalUtil.docClusterUtil
			//		.GetClusterGroupsTextByLabel(testInstTexts, false);
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = googlewebSnippetUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(trainInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			googlewebSnippetUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(trainInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\data-web-snippetsraw_ensembele_train");
			
			googlewebSnippetUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(testInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\semisupervised\\data-web-snippetsraw_ensembele_test");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
