package dal.clustering.document.dataset.agnews;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.general.UtilsShared;

public class ClusterUnSupervisedAgNewsRAD extends ClusterAgNews {

//	public void GenerateDocTermMatrixW2Vec() {
//		try{
//			ArrayList<String []> alBodyTitleLabel = agNewsUtil.getAldocsBodyTitleLabelFlat();
//			System.out.println(alBodyTitleLabel.size());
//			
//			//for(String [] bodyLabel: alBodyTitleLabel){
//			//	System.out.println(bodyLabel[1]+"\t"+bodyLabel[0]);
//			//}
//			
//			//remove inst
////			int []lineNumbersToBeRemoved = new int[]{26173};
////			
////			ArrayList<String []> toBeremoved = new ArrayList<String[]>();
////			for(int indxRemove: lineNumbersToBeRemoved){
////				int ind = indxRemove-1;
////				toBeremoved.add(alBodyTitleLabel.get(ind));
////			}
////			
////			alBodyTitleLabel.removeAll(toBeremoved);
//			
//			//agNewsUtil.docClusterUtil.textUtilShared.WriteTrainTestInstancesBodyLabel(alBodyTitleLabel, AgNewsConstant.AgNewsDocsFile);
//			//end
//
//			HashMap<String, double[]> hmW2Vec = agNewsUtil.docClusterUtil.PopulateW2Vec(agNewsUtil.getUniqueWordsTitle());			
//			ArrayList<InstanceW2Vec> testW2Vecs = agNewsUtil.docClusterUtil.CreateW2VecForTestData(alBodyTitleLabel, hmW2Vec);			
//		
//			
//			UtilsShared.WriteVectorWithLabel("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-w2vec-glove-vector-127600", testW2Vecs, " ");
//			
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	public void GenerateDocTermMatrixW2Vec() {
		try{
			ArrayList<String []> alBodyLabel = agNewsUtil.getAgNewsFlat();
			System.out.println(alBodyLabel.size());
			
			//for(String [] bodyLabel: alBodyTitleLabel){
			//	System.out.println(bodyLabel[1]+"\t"+bodyLabel[0]);
			//}
			
			//remove inst
//			int []lineNumbersToBeRemoved = new int[]{26173};
//			
//			ArrayList<String []> toBeremoved = new ArrayList<String[]>();
//			for(int indxRemove: lineNumbersToBeRemoved){
//				int ind = indxRemove-1;
//				toBeremoved.add(alBodyTitleLabel.get(ind));
//			}
//			
//			alBodyTitleLabel.removeAll(toBeremoved);
			
			//agNewsUtil.docClusterUtil.textUtilShared.WriteTrainTestInstancesBodyLabel(alBodyTitleLabel, AgNewsConstant.AgNewsDocsFile);
			//end

			HashMap<String, double[]> hmW2Vec = agNewsUtil.docClusterUtil.PopulateW2Vec(agNewsUtil.getUniqueWords());			
			ArrayList<InstanceW2Vec> testW2Vecs = agNewsUtil.docClusterUtil.CreateW2VecForTestData(alBodyLabel, hmW2Vec);			
		
			
			UtilsShared.WriteVectorWithLabel("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-w2vec-glove-vector-body-title-127600", testW2Vecs, " ");
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void GenerateTrainTest() {
		try{

			//String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\agnews-sparse-w2vec-alpha-8000-0-labels";
			String externalClusteringResultFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\kmLabels-127275";
			
			ArrayList<String []> alBodyLabel = agNewsUtil.getAgNewsFlat();
			
			@SuppressWarnings("unchecked")
			ArrayList<String> clusterLables = agNewsUtil.docClusterUtil.textUtilShared.ReadClusterLabels(externalClusteringResultFile);
			ArrayList<InstanceText> allInstTexts = agNewsUtil.docClusterUtil.CreateW2VecForTrainData(alBodyLabel, clusterLables);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = agNewsUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();
			
			
			/*//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			agNewsUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlier.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = agNewsUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\textsperlabel\\", 4); 
			
			//filter instants by outlier..
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
				ArrayList<String> outlierpreds = outliersByLabel.get(label);
				
				if(insts.size()!=outlierpreds.size()){
					System.out.println("Size not match for="+label);
					continue;
				}
				
				//if(insts.size()<=1400) continue;
				
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
			}*/
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
			
				ArrayList<InstanceText> subInsts = new ArrayList<InstanceText>();;
				if(insts.size()>22000){
					subInsts.addAll(insts.subList(0, 22000));
					testInstTexts.addAll(insts.subList(22000, insts.size()));
				}else{
					subInsts.addAll(insts);
				}
				trainInstTexts.addAll(subInsts);				
				lastClusters.put(label, subInsts);				
			}			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTest = agNewsUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(testInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTest);
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = agNewsUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(trainInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			
//			BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_train"));											
//			for(InstanceText inst: trainInstTexts){
//				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
//			}
//			bw.close();
//			
//			bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_test"));											
//			for(InstanceText inst: testInstTexts){
//				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
//			}
//			bw.close();
			
			agNewsUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(trainInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_train");
			
			agNewsUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(testInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_test");
			
		}catch(Exception e){
			
		}
	}
	
	public void GenerateTrainTest2List() {
		try{
			
			int maxIteration = 1;
			AgNewsExternalEvaluation obj = new AgNewsExternalEvaluation();
			
			for(int i=0;i<maxIteration;i++){
				System.out.println("iteration="+i);
				//for(int items = 1400; items<=2000;items=items+50)
				for(int items = 22000; items<=30000;items=items+1000){					
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
			
			String trainTestTextFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_traintest";			
			ArrayList<String[]> predTrueTexts = agNewsUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(trainTestTextFile);			
			ArrayList<InstanceText> allInstTexts = agNewsUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = agNewsUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts, false);
			
			
			ArrayList<InstanceText> testInstTexts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> trainInstTexts = new ArrayList<InstanceText>();			
			
			/*//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters);
			//write texts of each group in  
			agNewsUtil.docClusterUtil.textUtilShared.WriteTextsOfEachGroup("D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\textsperlabel\\"
					,lastClusters);
			
			//call python code to get the outliers in each cluster
			Process p = Runtime.getRuntime().exec("python D:\\PhD\\SupervisedFeatureSelection\\outlier.py");
			int exitVal = p.waitFor();
			System.out.println("Process status code="+exitVal);
			p.destroy();
			
			//read outliers 
			HashMap<String,ArrayList<String>> outliersByLabel = agNewsUtil.docClusterUtil.textUtilShared.ReadPredOutliersAll(
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\textsperlabel\\", 4); 
			
			for(String label: lastClusters.keySet()){
				ArrayList<InstanceText> insts = lastClusters.get(label);
				ArrayList<String> outlierpreds = outliersByLabel.get(label);
				
				if(insts.size()!=outlierpreds.size()){
					System.out.println("Size not match for="+label);
					continue;
				}
				
				if(insts.size()<=portion) continue;
				
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
			}*/
			
			
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
			
			LinkedHashMap<String, ArrayList<InstanceText>> lastClustersTrain = agNewsUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(trainInstTexts, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClustersTrain);
			
			agNewsUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(trainInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_train");
			
			agNewsUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(testInstTexts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_test");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void FindCommonGenerateTrainTest() {
		try{
			
			ArrayList<String[]> predTrueTexts1 = agNewsUtil.docClusterUtil.textUtilShared.
					ReadPredTrueTexts("D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_traintest");			
			ArrayList<InstanceText> allInstTexts1 = agNewsUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts1);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters1 = agNewsUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts1, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters1);
			
			
			
			/*String traintest1 = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_traintest";
			String traintest2 = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_traintest - Copy (2)";
			
			ArrayList<String[]> predTrueTexts1 = agNewsUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(traintest1);			
			ArrayList<InstanceText> allInstTexts1 = agNewsUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts1);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters1 = agNewsUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts1, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters1);
						
			ArrayList<String[]> predTrueTexts2 = agNewsUtil.docClusterUtil.textUtilShared.ReadPredTrueTexts(traintest2);			
			ArrayList<InstanceText> allInstTexts2 = agNewsUtil.docClusterUtil.CreateW2VecForTrainData(predTrueTexts2);
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters2 = agNewsUtil.docClusterUtil
					.GetClusterGroupsTextByLabel(allInstTexts2, false);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(lastClusters2);
			
			LinkedHashMap<String, ArrayList<InstanceText>> commonLastCluster = new LinkedHashMap<String, ArrayList<InstanceText>>();
			ArrayList<InstanceText> notCommonTestInsts = new ArrayList<InstanceText>();
			ArrayList<InstanceText> commonTrainInsts = new ArrayList<InstanceText>();
			
			for(String label1: lastClusters1.keySet()){
				
				ArrayList<InstanceText> instSet1 = lastClusters1.get(label1);
				ArrayList<InstanceText> instSet2 = lastClusters2.get(label1);
				
				HashSet<String> textsSet1 = new HashSet<String>();				
				for(InstanceText inst1: instSet1){
					textsSet1.add(inst1.Text);
				}
				
				ArrayList<InstanceText> commonInsts = new ArrayList<InstanceText>();
				for(InstanceText inst2: instSet2){
					if(textsSet1.contains(inst2.Text) && commonInsts.size()<30000){
						commonInsts.add(inst2);
						commonTrainInsts.add(inst2);
					}
					else{
						notCommonTestInsts.add(inst2);
					}
				}
				
				commonLastCluster.put(label1, commonInsts);
			}
			
			System.out.println(notCommonTestInsts.size()+commonTrainInsts.size());
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(commonLastCluster);
			
			agNewsUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(commonTrainInsts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_train");
			
			agNewsUtil.docClusterUtil.textUtilShared.WriteTrainTestInstances(notCommonTestInsts, 
					"D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\semisupervised\\agnewsraw_ensembele_test");*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
