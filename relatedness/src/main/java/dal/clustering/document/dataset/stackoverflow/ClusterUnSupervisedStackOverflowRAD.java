package dal.clustering.document.dataset.stackoverflow;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;

public class ClusterUnSupervisedStackOverflowRAD extends ClusterStackOverflow {

	public void GenerateTrainTest() {
		try{
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\stackoverflow-w2vec-glove-sparse-alpha-20000-0-labels";
			
			ArrayList<String []> alBodyLabel = stackOverflowUtil.getDocsStackOverflowFlat();
			
			@SuppressWarnings("unchecked")
			ArrayList<String> clusterLables = stackOverflowUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			ArrayList<InstanceText> allInstTexts = stackOverflowUtil.docClusterUtil.CreateW2VecForTrainData(alBodyLabel, clusterLables);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = stackOverflowUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();
						
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			stackOverflowUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlierembed.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = stackOverflowUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\textsperlabel\\", 20); 
			
			//filter instants by outlier..
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
				ArrayList<String> outlierpreds = outliersByLabel.get(label);
				
				if(insts.size()!=outlierpreds.size()){
					System.out.println("Size not match for="+label);
					continue;
				}
				
				//if(insts.size()<=700) continue;
				
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
				if(insts.size()>700){
					subInsts.addAll(insts.subList(0, 700));
					testInstTexts.addAll(insts.subList(700, insts.size()));
				}else{
					subInsts.addAll(insts);
				}
				trainInstTexts.addAll(subInsts);				
				lastClusters.put(label, subInsts);				
			}			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = stackOverflowUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(testInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = stackOverflowUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(trainInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			stackOverflowUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(trainInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\stackoverflowraw_ensembele_train");
			
			stackOverflowUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(testInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\stackoverflowraw_ensembele_test");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateTrainTest2List() {
		try{
			int maxIteration = 5;
			StackOverflowExternalEvaluation obj = new StackOverflowExternalEvaluation();
			
			for(int i=0;i<maxIteration;i++){
				System.out.println("iteration="+i);
				for(int items = 700; items<=1000;items=items+50){
					
					Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\improvedclassification_embedd.py");
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
			
			String trainTestTextFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\stackoverflowraw_ensembele_traintest";			
			ArrayList<String[]> predTrueTexts = stackOverflowUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(trainTestTextFile);			
			ArrayList<InstanceText> allInstTexts = stackOverflowUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = stackOverflowUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();			
			
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			stackOverflowUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlierembed.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = stackOverflowUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\textsperlabel\\", 20); 
			
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
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = stackOverflowUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(trainInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			stackOverflowUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(trainInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\stackoverflowraw_ensembele_train");
			
			stackOverflowUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(testInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\stackoverflowraw_ensembele_test");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void SaveDataToEmbedding() {
		try{
			ArrayList<String []> alDocLabelFlat =stackOverflowUtil.getDocsStackOverflowFlat();	
			HashMap<String, double[]> hmW2Vec = stackOverflowUtil.docClusterUtil.PopulateW2Vec(stackOverflowUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = stackOverflowUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			
			stackOverflowUtil.docClusterUtil.textUtilShared.WriteTrainTestInstancesTextVec(testW2Vecs, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\semisupervised\\stackoverflow_20000_vecs");
		}catch(Exception e){
			
		}
	}
}
