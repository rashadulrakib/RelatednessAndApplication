package dal.clustering.document.dataset.agnews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringText;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringW2Vec;
import dal.clustering.document.shared.entities.ClusterResultConatainerText;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.general.UtilsShared;

public class ClusterUnSupervisedAgNews {

	UnSupervisedClusteringText unSupervisedClusteringText;
	UnSupervisedClusteringW2Vec unSupervisedClusteringW2Vec;
	ClusterEvaluation clusterEvaluation;
	AgNewsUtil agNewsUtil;
	
	public ClusterUnSupervisedAgNews() throws IOException{
		agNewsUtil = new AgNewsUtil();	
		clusterEvaluation = new ClusterEvaluation(agNewsUtil.docClusterUtil);
//		unSupervisedClusteringW2Vec = new UnSupervisedClusteringW2Vec(agNewsUtil.getUniqueWords(),
//				agNewsUtil.getAgNewsFlat(), agNewsUtil.getAgNewsList(), 
//				agNewsUtil.docClusterUtil);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(agNewsUtil.docClusterUtil, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(agNewsUtil.docClusterUtil);
	}

	public void ClusterDocsNGramBasedSimilarityGtm() {
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = agNewsUtil.getAgNewsList();
			ArrayList<String []> alDocLabelFlat = agNewsUtil.getAgNewsFlat();
			
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = agNewsUtil
					.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);
			
//			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
//			hmTrainDocsLabelBody.put("2", new ArrayList<>(Arrays.asList("thechat lt em gt dean cain spent life uniform time american safety princeton established broken single season division aa record interceptions undrafted free agent buffalo bills preseason knee injury ended football career sportsman future aptly named movie futuresport iconic superhero television lois clark adventures superman set pinstripes cain plays star third baseman conrad dean cbs drama clubhouse scheduled debut month lt em gt")));
//			hmTrainDocsLabelBody.put("3", new ArrayList<>(Arrays.asList("baby bells rivals fewer phones washington reuters three nation dominant local telephone companies thursday sharp drop residential lines leased competitors corp lt href http investor reuters fullquote aspx ticker target stocks quickinfo fullquote gt lt gt announced retreat residential service july changing federal rules")));
//			hmTrainDocsLabelBody.put("1", new ArrayList<>(Arrays.asList("kidnappers iraq free truckers baghdad iraq militants iraq freed foreign truck drivers wednesday holding weeks muslims united calls release french reporters captured separate group demanding france revoke ban muslim head scarves schools wednesday gunmen shot convoy carrying iraqi governing council member ahmad chalabi returned najaf attend meeting iraq member national council watchdog interim government help shepherd nation elections")));
//			hmTrainDocsLabelBody.put("4", new ArrayList<>(Arrays.asList("legal expert joins open source screening firm roundup cray signs supercomputing customers msn messenger beta leaks web level buy sprint dial business cisco ceo salary shoots sandisk capacity flash memory cards")));
			
			ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringText.PerformUnSeupervisedSeededClusteringByGtmWordSimIterative(hmTrainDocsLabelBody, alDocLabelFlat);
			
			clusterEvaluation.ClusterEvaluationGeneratorText(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneText(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(clusterResultConatainerText.FinalCluster);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterDocsSimilarityByW2VecFollowingGtm() {
		try{
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = agNewsUtil.getAgNewsList();
			ArrayList<String []> alDocLabelFlat = agNewsUtil.getAgNewsFlat();
			
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = agNewsUtil
					.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);
			
			ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringW2Vec.PerformUnSeuperVisedSeededClusteringByW2VecFollowingGtm
					(hmTrainDocsLabelBody, alDocLabelFlat);
			
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneText(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(clusterResultConatainerText.FinalCluster);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterDocsSimilarityByW2VecAverageVector() {
		try{
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = agNewsUtil.getAgNewsList();
			ArrayList<String []> alDocLabelFlat = agNewsUtil.getAgNewsFlat();
			
//			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = agNewsUtil
//					.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);
			
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
			hmTrainDocsLabelBody.put("1", new ArrayList<>(Arrays.asList("commentary peoplesoft ease inevitable businessweek online businessweek online year peoplesoft ceo craig conway battled prevent oracle corp larry ellison grabbing company hostile takeover conway spirited quest absorbed fatal blow sept district court judge vaughn walker ruled justice dept grounds block acquisition european community legal case halting shotgun wedding weaker day")));
			hmTrainDocsLabelBody.put("2", new ArrayList<>(Arrays.asList("hello canada reuters reports ottawa reuters number citizens visiting canada main immigration web site shot fold americans flirt idea abandoning homeland president george bush election win week looked day election november web site hit high double previous record high immigration ministry spokeswoman maria iadinardi friday average day people united states log web site cic gc figure rocketed wednesday number visits settled thursday norm pr")));
			hmTrainDocsLabelBody.put("3", new ArrayList<>(Arrays.asList("baby bells rivals fewer phones washington reuters three nation dominant local telephone companies thursday sharp drop residential lines leased competitors corp lt href http investor reuters fullquote aspx ticker target stocks quickinfo fullquote gt lt gt announced retreat residential service july changing federal rules")));
			hmTrainDocsLabelBody.put("4", new ArrayList<>(Arrays.asList("man bill gates businessweek online businessweek online saga computing industry rich outsize characters surprising plot turns story risen time mythic proportions tale software pioneer gary kildall missed opportunity supply ibm operating system pc essentially handing chance lifetime control tech future rival bill gates microsoft corp process missed richest man")));
			
			ClusterResultConatainerVector clusterResultConatainer = unSupervisedClusteringW2Vec.PerformUnSeuperVisedSeededClusteringByW2VecAverageVec
					(hmTrainDocsLabelBody, alDocLabelFlat);
			
			clusterEvaluation.ClusterEvaluationGeneratorVector(clusterResultConatainer.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneVector(clusterResultConatainer.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(clusterResultConatainer.FinalCluster);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterDocsBySimilarityMatrixGtm() {
		try{
			ArrayList<String []> alDocLabelFlat = agNewsUtil.getAgNewsFlat();
			
			alDocLabelFlat = agNewsUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 800, 0);
			
			double [][] docSimMatrix= agNewsUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilGtm);

			double [][] saprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			
			UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, AgNewsConstant.AgNewsDocsFile, "\t");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterDocsBySimilarityMatrixCosineW2Vec() {
		try{
			ArrayList<String []> alDocLabelFlat = agNewsUtil.getAgNewsFlat();
			
			//alDocLabelFlat = agNewsUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 800, 0);
			
			double [][] docSimMatrix= agNewsUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			
			double [][] saprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\agnews\\sparseMatrix-w2vec-sd-0", saprsifyMatrix, " ");
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, AgNewsConstant.AgNewsDocsFile, "\t");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification(){
		try{

			ArrayList<String []> alDocLabelFlat =agNewsUtil.getAgNewsFlat();
			
			HashMap<String, double[]> hmW2Vec = agNewsUtil.docClusterUtil.PopulateW2Vec(agNewsUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = agNewsUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);
			
			//alDocLabelFlat = agNewsUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 2000, 0);
			
			//double [][] docSimMatrix= agNewsUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			double [][] docSimMatrix= agNewsUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);
			
			alDocLabelFlat.clear();
			alDocLabelFlat = null;
			hmW2Vec.clear();
			hmW2Vec=null;
			testW2Vecs.clear();
			testW2Vecs=null;
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-w2vec-sim-8000", docSimMatrix, " ");

			double [][] nonSaprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, AgNewsConstant.NumberOfClusters, false);			
//			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-w2vec-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/sparseMatrix-w2vec-sd-nonAlpha-8000-Fixed", nonSaprsifyMatrix, " ");
			
			
			double [][] saprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, AgNewsConstant.NumberOfClusters, true);			
//			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-w2vec-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/sparseMatrix-w2vec-sd-Alpha-8000-Fixed", saprsifyMatrix, " ");
			
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, AgNewsConstant.AgNewsDocsFile, "\t");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixGtmFixedSparsification(){
		try{
			ArrayList<String []> alDocLabelFlat =agNewsUtil.getAgNewsFlat();
			//ArrayList<String []> alDocLabelFlat = new ArrayList<String []>(googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat().subList(0, 20));

			//double [][] docSimMatrix= agNewsUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilGtm);
			double [][] docSimMatrix= agNewsUtil.docClusterUtil.ComputeSimilarityMatrixTrWPParallel(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilTrWP, 10);
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-gtm-sim-8000", docSimMatrix, " ");
			
			double [][] nonSaprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, AgNewsConstant.NumberOfClusters, false);			
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-gtm-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/sparseMatrix-gtm-sd-nonAlpha-8000-Fixed", nonSaprsifyMatrix, " ");
			
			double [][] saprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, AgNewsConstant.NumberOfClusters, true);			
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-gtm-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/sparseMatrix-gtm-sd-Alpha-8000-Fixed", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecCenterBasedSparsification() {
		try{

			ArrayList<String []> alDocLabelFlat =agNewsUtil.getAgNewsFlat();
			
			HashMap<String, double[]> hmW2Vec = agNewsUtil.docClusterUtil.PopulateW2Vec(agNewsUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = agNewsUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			
			InstanceW2Vec centerVec = agNewsUtil.docClusterUtil.ComputeCenterInstanceW2Vec(testW2Vecs);
			
			double [][] saprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil
					.SparsifyDocDisSimilarityMatrixByCenterVector(centerVec, testW2Vecs);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/sparseMatrix-w2vec-google-CenterBased-8000", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecWeightCenterBasedSparsification(){
		try{

			ArrayList<String []> alDocLabelFlat =agNewsUtil.getAgNewsFlat();
			
			HashMap<String, double[]> hmW2Vec = agNewsUtil.docClusterUtil.PopulateW2Vec(agNewsUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = agNewsUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			
			InstanceW2Vec centerVec = agNewsUtil.docClusterUtil.ComputeCenterInstanceW2Vec(testW2Vecs);
			InstanceW2Vec maxVec = agNewsUtil.docClusterUtil.ComputeMaxInstanceW2Vec(testW2Vecs);
			InstanceW2Vec weightCenterVec = agNewsUtil.docClusterUtil.ComputeWeightCenterInstanceW2Vec(centerVec, maxVec);
			
			double [][] saprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil
					.SparsifyDocDisSimilarityMatrixByCenterVector(weightCenterVec, testW2Vecs);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/sparseMatrix-w2vec-google-wightCenterBased-8000", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineTfIdfCenterBasedSparsification() {
		try{
			
			ArrayList<HashMap<String, Double>> docsTfIdfs = new TfIdfMatrixGenerator().ConstructTfIdfList(agNewsUtil.GetAgNewsDocuments(), agNewsUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = agNewsUtil.docClusterUtil.CreateTfIdfVecForTestData(docsTfIdfs, agNewsUtil.getUniqueWords());
			
			InstanceW2Vec centerVec = agNewsUtil.docClusterUtil.ComputeCenterInstanceW2Vec(testW2Vecs);
			
			double [][] saprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil
					.SparsifyDocDisSimilarityMatrixByCenterVector(centerVec, testW2Vecs);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/sparseMatrix-tfidf-CenterBased", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixFromFileSparsificationBFixedNbyKSimilarities() {
		try{
			String simFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-w2vec-sim-google-8000";
			//String simFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\web-snippet-gtm-sim-2280";
			
			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
			
			double [][] saprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixFixedNbyKSimilarities(docSimMatrix, AgNewsConstant.NumberOfClusters);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/sparseMatrix-w2vec-google-Alpha-8000-NbyK", saprsifyMatrix, " ");
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-gtm-sd-Alpha-2280-NbyK", saprsifyMatrix, " "); 
			
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

	public void GenerateDocsDisSimilarityMatrixCosineTfIdf() {
		try{
			ArrayList<HashMap<String, Double>> docsTfIdfs = new TfIdfMatrixGenerator().ConstructTfIdfList(agNewsUtil.GetAgNewsDocuments(), agNewsUtil.getUniqueWords());
			double [][] docSimMatrix = agNewsUtil.docClusterUtil.ComputeSimilarityMatrixTfIdfParallel(docsTfIdfs, 10);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-tfidf-sim-8000", docSimMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateDocsDisSimilarityMatrixFromFileSparsification() {
		try{
			String simFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-w2vec-sim-google-8000";
			//String simFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\web-snippet-w2vec-sim-2280";
			
			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
			
			//double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, GoogleWebSnippetConstant.NumberOfClusters, true);
			double [][] saprsifyMatrix = agNewsUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, AgNewsConstant.NumberOfClusters, false);
			
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-w2vec-sd-Alpha-2280-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/sparseMatrix-w2vec-google-nonAlpha-8000", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixFromFileSparsificationIterative() {
		try{
			String simFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-w2vec-sim-8000";
			//String simFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\web-snippet-gtm-sim-2280";
			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
			
			//List<double[][]> alSparseDists = agNewsUtil.docClusterUtil.sparsificationUtilIterative.SparsifyDocDisSimilarityMatrixAlgorithomicIterative(docSimMatrix, AgNewsConstant.NumberOfClusters);
			List<double[][]> alSparseDists = agNewsUtil.docClusterUtil.sparsificationUtilIterative.SparsifyDocDisSimilarityMatrixAlgorithomicExactIterative(docSimMatrix, AgNewsConstant.NumberOfClusters);

//			for(int i=0;i< alSparseDists.size();i++){
//				double[][] sparseDistMatrix = alSparseDists.get(i);
//				UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-sparse-w2vec-alpha-8000-"+i, sparseDistMatrix, " ");
//			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocSimMatrixW2Vec() {
		try{
			ArrayList<String []> alDocLabelFlat =agNewsUtil.getAgNewsFlat();
			//double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			
			HashMap<String, double[]> hmW2Vec = agNewsUtil.docClusterUtil.PopulateW2VecGoogle(agNewsUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = agNewsUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			double [][] docSimMatrix= agNewsUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);

			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-w2vec-sim-google-8000", docSimMatrix, " ");

		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
