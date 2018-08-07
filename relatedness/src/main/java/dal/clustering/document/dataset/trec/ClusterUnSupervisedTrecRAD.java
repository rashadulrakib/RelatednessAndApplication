package dal.clustering.document.dataset.trec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.entities.InstanceText;

public class ClusterUnSupervisedTrecRAD extends ClusterTrec {
	public void GenerateTrainTest() {
		try{
			//String externalClusteringResultFile= "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\2n-biomedical-w2vec-add-sparse-20000-0-labels";
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\2n-trec-glove-sparse-0-labels";
			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedical-sparse-gtm-alpha-20000-0-labels";  //2n-biomedical-w2vecitr-bioasq2018-sparse-20000-0-labels
			
			
			ArrayList<String []> alBodyLabel = trecUtil.getTrecFlat();
			
			ArrayList<String> clusterLables = trecUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			ArrayList<InstanceText> allInstTexts = trecUtil.docClusterUtil.CreateW2VecForTrainData(alBodyLabel, clusterLables);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = trecUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();
			
			
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			trecUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlier.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = trecUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\textsperlabel\\", 6); 
			
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
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = trecUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(testInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = trecUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(trainInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			trecUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(trainInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\trecraw_ensembele_train");
			
			trecUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(testInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\trecraw_ensembele_test");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateTrainTest2List() {
		try{
			
			int maxIteration = 10;
			TrecExternalEvaluation obj = new TrecExternalEvaluation();
			
			boolean first= true;
			
			for(int i=0;i<maxIteration;i++){
				System.out.println("iteration="+i);
				//for(int items = 1400; items<=2000;items=items+50)
				for(int items = 700; items<=900;items=items+50)
				{					
					Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\improvedclassification.py");
					//Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\improvedclassification_embedd.py");
					int exitVal = p.waitFor();
					System.out.println("Process status code="+exitVal);
					p.destroy();
					
					obj.ExternalEvaluateRAD();	
					
					if(first) items=750;
					first = false;
					
					GenerateTrainTest2(items);
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public double GenerateTrainTest2(int portion) {
		try{
			
			String trainTestTextFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\trecraw_ensembele_traintest";			
			ArrayList<String[]> predTrueTexts = trecUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(trainTestTextFile);			
			ArrayList<InstanceText> allInstTexts = trecUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = trecUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();			
			
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			trecUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlier.py");
			//Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlierembed.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = trecUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\textsperlabel\\", 6); 
			
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
				
				if(insts.size()>=portion){
					subInsts.addAll(insts.subList(0, portion));
					testInstTexts.addAll(insts.subList(portion, insts.size()));
				}else{
					subInsts.addAll(insts);
				}
				trainInstTexts.addAll(subInsts);	
				lastClusters.put(label, subInsts);
			}
			
			//if((double)trainInstTexts.size()/(double)allInstTexts.size() > 0.80) return;
			
			//LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = bioMedicalUtil.docClusterUtil
			//		.GetClusterGroupsTextByLabel(testInstTexts, false);
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
			
			//LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = agNewsUtil.docClusterUtil
			//		.GetClusterGroupsTextByLabel(trainInstTexts, false);
			//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			double trainDataRatio = (double)trainInstTexts.size()/(trainInstTexts.size()+testInstTexts.size());
			System.out.println("trainDataRatio="+trainDataRatio);
			
			if(trainDataRatio<=0.9){
				trecUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(trainInstTexts, 
						"D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\trecraw_ensembele_train");
				
				trecUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(testInstTexts, 
						"D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\trecraw_ensembele_test");
			}
			
			return trainDataRatio;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return 1.0;
	}

	public void GenerateTrainTest2List1() {
		try{
			int iterations = 10;
			ArrayList<String[]> predTrueTexts = trecUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts("D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\trecraw_ensembele_traintest");
			int N= predTrueTexts.size();
			int N_K = N/TrecConstant.NumberOfClusters;
			
			int texts_Within_Training_Range = (int)(N_K*1.0 - N_K*0.7);
			int Texts_Each_Block = texts_Within_Training_Range/5;
			if((N_K*0.7)%50==0 && N_K%50==0 && texts_Within_Training_Range>=200){
				Texts_Each_Block = 50;
			}
			
			TrecExternalEvaluation obj = new TrecExternalEvaluation();
			
			for(int itr= 0; itr<iterations; itr++){
				for (int text_train = (int)(N_K*0.7); text_train<=(int)(N_K*1.0); text_train=text_train+Texts_Each_Block){
                    double trainDataRatio = GenerateTrainTest2(text_train);
					
					if(trainDataRatio>0.9) continue;
					
					System.out.println("itr="+itr+", text_train="+text_train);
					
					Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\improvedclassification.py");
					//Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\improvedclassification_embedd.py");
					int exitVal = p.waitFor();
					System.out.println("Process status code="+exitVal);
					p.destroy();
					
					obj.ExternalEvaluateRAD();	
				}
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void MergeAndWriteTrainTest() {
		try{
			//String externalClusteringResultFile= "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\2n-biomedical-w2vec-add-sparse-20000-0-labels";
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\2n-trec-glove-sparse-0-labels";
			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedical-sparse-gtm-alpha-20000-0-labels";  //2n-biomedical-w2vecitr-bioasq2018-sparse-20000-0-labels
			
			
			ArrayList<String []> alBodyLabel = trecUtil.getTrecFlat();
			
			ArrayList<String> clusterLables = trecUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			ArrayList<InstanceText> allInstTexts = trecUtil.docClusterUtil.CreateW2VecForTrainData(alBodyLabel, clusterLables);
			
			trecUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(allInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\semisupervised\\trecraw_ensembele_traintest");
			
			TrecExternalEvaluation obj = new TrecExternalEvaluation();
			obj.ExternalEvaluateRAD();
		}catch(Exception e){
			
		}
	}
}
