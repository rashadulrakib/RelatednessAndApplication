package dal.clustering.document.dataset.stackoverflow;

import java.io.IOException;
import java.util.ArrayList;
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

public class ClusterUnSupervisedStackOverflow {
	
	UnSupervisedClusteringText unSupervisedClusteringText;
	ClusterEvaluation clusterEvaluation;
	StackOverflowUtil stackOverflowUtil;
	UnSupervisedClusteringW2Vec unSupervisedClusteringW2Vec;

	public ClusterUnSupervisedStackOverflow() throws IOException{
		
		stackOverflowUtil = new StackOverflowUtil();
		clusterEvaluation = new ClusterEvaluation(stackOverflowUtil.docClusterUtil);
//		unSupervisedClusteringW2Vec = new UnSupervisedClusteringW2Vec(stackOverflowUtil.getUniqueWords(),
//				stackOverflowUtil.getDocsStackOverflowFlat(), stackOverflowUtil.getDocsStackOverflowList(), 
//				stackOverflowUtil.docClusterUtil);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(stackOverflowUtil.docClusterUtil, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(stackOverflowUtil.docClusterUtil);
		
	}
	
	public void ClusterDocsNGramBasedSimilarityGtm(){
		try{
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = stackOverflowUtil.getDocsStackOverflowList();
			ArrayList<String []> alDocLabelFlat = stackOverflowUtil.getDocsStackOverflowFlat();
						
//			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
//			
//			hmTrainDocsLabelBody.put("18", new ArrayList<>(Arrays.asList("adding data multiple linq sql datacontexts")));
//			hmTrainDocsLabelBody.put("9", new ArrayList<>(Arrays.asList("monitor cocoa apps execution external utilities ffmpeg mac os")));
//			hmTrainDocsLabelBody.put("7", new ArrayList<>(Arrays.asList("function updating database table spring jdbctemplate reason exception connection read update database changes resolve problem")));
//			hmTrainDocsLabelBody.put("8", new ArrayList<>(Arrays.asList("visual studio addin development resolve process access file problem exiting visual studio")));
//			hmTrainDocsLabelBody.put("15", new ArrayList<>(Arrays.asList("problem calling asp net ajax webserviceproxy invoke javascript method")));
//			hmTrainDocsLabelBody.put("3", new ArrayList<>(Arrays.asList("svn external subdirectory sync head revision svn update parent working copy")));
//			hmTrainDocsLabelBody.put("14", new ArrayList<>(Arrays.asList("accessing list data site sharepoint designer workflow")));
//			hmTrainDocsLabelBody.put("19", new ArrayList<>(Arrays.asList("observevalueforkeypath ofobject change context work properly arrays")));
//			hmTrainDocsLabelBody.put("4", new ArrayList<>(Arrays.asList("help apache mod rewrite fetch file based url")));
//			hmTrainDocsLabelBody.put("10", new ArrayList<>(Arrays.asList("nsstepper setminimum setmaximum set")));
//			hmTrainDocsLabelBody.put("13", new ArrayList<>(Arrays.asList("scala example implicit paremeter working")));
//			hmTrainDocsLabelBody.put("2", new ArrayList<>(Arrays.asList("good alternative sql plusfor oracle")));
//			hmTrainDocsLabelBody.put("5", new ArrayList<>(Arrays.asList("ms excel simple unselect question excel vba dealing shapes")));
//			hmTrainDocsLabelBody.put("6", new ArrayList<>(Arrays.asList("matlab fminsearch function")));
//			hmTrainDocsLabelBody.put("16", new ArrayList<>(Arrays.asList("qt visual vtudio add subset qt visual vtudio integration")));
//			hmTrainDocsLabelBody.put("1", new ArrayList<>(Arrays.asList("modifying wordpress post php file custom blog entries")));
//			hmTrainDocsLabelBody.put("17", new ArrayList<>(Arrays.asList("adding zend framework php ini include path drupal site blank")));
//			hmTrainDocsLabelBody.put("12", new ArrayList<>(Arrays.asList("hibernate createcriteria result")));
//			hmTrainDocsLabelBody.put("11", new ArrayList<>(Arrays.asList("spring initbinder webbindinginitializer example")));
//			hmTrainDocsLabelBody.put("20", new ArrayList<>(Arrays.asList("problem fetch product url custom module magento")));
			
			//for(int seed=0;seed<=20;seed++)
			{
				LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = stackOverflowUtil
						.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);
				
				ArrayList<InstanceW2Vec> testW2Vecs = unSupervisedClusteringW2Vec.docClusterUtilW2Vec.populateW2VecDocsFlat(alDocLabelFlat);
				
				ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringText.PerformUnSeupervisedSeededClusteringByGtmWordSimIterative(hmTrainDocsLabelBody, alDocLabelFlat);
				//ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringText.PerformUnSeupervisedSeededClusteringByGtmWordSimIterative(hmTrainDocsLabelBody, testW2Vecs, 0);
				
				clusterEvaluation.ClusterEvaluationGeneratorTextExternal(clusterResultConatainerText.FinalCluster);
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(clusterResultConatainerText.FinalCluster);
				
				//clusterEvaluation.ClusterEvaluationGeneratorText(clusterResultConatainerText.FinalCluster);
				clusterEvaluation.EvalSemiSupervisedByAccOneToOneText(clusterResultConatainerText.FinalCluster);
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(clusterResultConatainerText.FinalCluster);
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterDocsSimilarityByW2VecFollowingGtm() {
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = stackOverflowUtil.getDocsStackOverflowList();
			ArrayList<String []> alDocLabelFlat = stackOverflowUtil.getDocsStackOverflowFlat();
			
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = stackOverflowUtil
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
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = stackOverflowUtil.getDocsStackOverflowList();
			ArrayList<String []> alDocLabelFlat = stackOverflowUtil.getDocsStackOverflowFlat();
			
			//for(int seed=1;seed<=20;seed++)
			{
				//System.out.println("Seed="+0);
//				LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = stackOverflowUtil
//						.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);
				
				LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = stackOverflowUtil
						.docClusterUtil.GetRandomDocuments(alDocLabelFlat, 0, StackOverflowConstant.NumberOfClusters);
				
//				LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
//				
//				hmTrainDocsLabelBody.put("18", new ArrayList<>(Arrays.asList("linq sql loadwith limiting fields returned")));
//				hmTrainDocsLabelBody.put("9", new ArrayList<>(Arrays.asList("check command key held java swt startup mac os")));
//				hmTrainDocsLabelBody.put("7", new ArrayList<>(Arrays.asList("visual studio addin development resolve process access file problem exiting visual studio")));
//				hmTrainDocsLabelBody.put("8", new ArrayList<>(Arrays.asList("safe keyboard shortcut activating code completion style functionality mac os browsers")));
//				hmTrainDocsLabelBody.put("15", new ArrayList<>(Arrays.asList("changing highlighcolor default option ajax ruby rails")));
//				hmTrainDocsLabelBody.put("3", new ArrayList<>(Arrays.asList("svn external subdirectory sync head revision svn update parent working copy")));
//				hmTrainDocsLabelBody.put("14", new ArrayList<>(Arrays.asList("change sharepoint extended webapplicaion web config file")));
//				hmTrainDocsLabelBody.put("19", new ArrayList<>(Arrays.asList("simple library utf haskell streams longer compile")));
//				hmTrainDocsLabelBody.put("4", new ArrayList<>(Arrays.asList("help apache mod rewrite fetch file based url")));
//				hmTrainDocsLabelBody.put("10", new ArrayList<>(Arrays.asList("pass long strings search replace bash sed rpl script")));
//				hmTrainDocsLabelBody.put("13", new ArrayList<>(Arrays.asList("scala example implicit paremeter working")));
//				hmTrainDocsLabelBody.put("2", new ArrayList<>(Arrays.asList("returning multiple columns case select satement oracle")));
//				hmTrainDocsLabelBody.put("5", new ArrayList<>(Arrays.asList("process current excel throught vba relying finding window caption")));
//				hmTrainDocsLabelBody.put("6", new ArrayList<>(Arrays.asList("problem array type dampar matlab deconvolucy")));
//				hmTrainDocsLabelBody.put("16", new ArrayList<>(Arrays.asList("import qt resources main cpp gui classes dependend static lib project")));
//				hmTrainDocsLabelBody.put("1", new ArrayList<>(Arrays.asList("wordpress plug allow edit meta tags individual post")));
//				hmTrainDocsLabelBody.put("17", new ArrayList<>(Arrays.asList("adding zend framework php ini include path drupal site blank")));
//				hmTrainDocsLabelBody.put("12", new ArrayList<>(Arrays.asList("hibernaqte exception org hibernate exception sqlgrammarexception execute query")));
//				hmTrainDocsLabelBody.put("11", new ArrayList<>(Arrays.asList("spring security problem error creating bean org springframework web servlet mvc annotation defaultannotationhandlermapping")));
//				hmTrainDocsLabelBody.put("20", new ArrayList<>(Arrays.asList("problem fetch product url custom module magento")));
				
				ClusterResultConatainerVector clusterResultConatainer = unSupervisedClusteringW2Vec.PerformUnSeuperVisedSeededClusteringByW2VecAverageVec
						(hmTrainDocsLabelBody, alDocLabelFlat);
				
				double [] clusterAssignments = unSupervisedClusteringW2Vec.GetClusterAssignments();
				
//				if(alDocLabelFlat.size()==clusterAssignments.length){
//					//System.out.println(alDocLabel.size());
//					
//					double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec, clusterAssignments);
//					//double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilText, clusterAssignments);
//					
//					double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
//					UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix", saprsifyMatrix, " ");
//				}
				
				//clusterEvaluation.ClusterEvaluationGeneratorVector(clusterResultConatainer.FinalCluster);
				//clusterEvaluation.EvalSemiSupervisedByAccOneToOneVector(clusterResultConatainer.FinalCluster);
				//clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(clusterResultConatainer.FinalCluster);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterDocsBySimilarityMatrixGtm(){
		try{
			ArrayList<String []> alDocLabelFlat = stackOverflowUtil.getDocsStackOverflowFlat();
			
			//alDocLabelFlat = stackOverflowUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 250, 0);
			
			double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilGtm);

			double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, StackOverflowConstant.StackOverflowDocsFile, "\t");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterDocsBySimilarityMatrixCosineW2Vec(){
		try{
			ArrayList<String []> alDocLabelFlat = stackOverflowUtil.getDocsStackOverflowFlat();
			
			//alDocLabelFlat = stackOverflowUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 250, 0);
			
			double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			
			double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, StackOverflowConstant.StackOverflowDocsFile, "\t");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification(){
		try{
			ArrayList<String []> alDocLabelFlat = stackOverflowUtil.getDocsStackOverflowFlat();
			
			HashMap<String, double[]> hmW2Vec = stackOverflowUtil.docClusterUtil.PopulateW2Vec(stackOverflowUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = stackOverflowUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);

			double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);
			/*double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			 */
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/stackoverflow-w2vec-sim-20000", docSimMatrix, " ");
			 
			double [][] nonSaprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, StackOverflowConstant.NumberOfClusters, false);
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-w2vec-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-w2vec-sd-nonAplha-20000-Fixed", nonSaprsifyMatrix, " ");
			
			double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, StackOverflowConstant.NumberOfClusters, true);
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-w2vec-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-w2vec-sd-Aplha-20000-Fixed", saprsifyMatrix, " ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixGtmFixedSparsification(){
		try{
						
			ArrayList<String []> alDocLabelFlat =stackOverflowUtil.getDocsStackOverflowFlat();
			//ArrayList<String []> alDocLabelFlat = new ArrayList<String []>(googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat().subList(0, 20));

			//double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilGtm);
			double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeSimilarityMatrixTrWPParallel(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilTrWP, 10);
			
			unSupervisedClusteringText= null;
			alDocLabelFlat.clear();
			alDocLabelFlat = null;
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/stackoverflow-gtm-sim-20000", docSimMatrix, " ");
			
			double [][] nonSaprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, StackOverflowConstant.NumberOfClusters, false);			
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\sparseMatrix-gtm-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-gtm-sd-nonAlpha-20000-Fixed", nonSaprsifyMatrix, " ");
			
			double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, StackOverflowConstant.NumberOfClusters, true);			
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\sparseMatrix-gtm-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-gtm-sd-Alpha-20000-Fixed", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecCenterBasedSparsification() {
		try{

			ArrayList<String []> alDocLabelFlat =stackOverflowUtil.getDocsStackOverflowFlat();
			
			HashMap<String, double[]> hmW2Vec = stackOverflowUtil.docClusterUtil.PopulateW2Vec(stackOverflowUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = stackOverflowUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			
			InstanceW2Vec centerVec = stackOverflowUtil.docClusterUtil.ComputeCenterInstanceW2Vec(testW2Vecs);
			
			double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil
					.SparsifyDocDisSimilarityMatrixByCenterVector(centerVec, testW2Vecs);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-w2vec-google-CenterBased-20000", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecWeightCenterBasedSparsification(){
		try{

			ArrayList<String []> alDocLabelFlat =stackOverflowUtil.getDocsStackOverflowFlat();
			
			HashMap<String, double[]> hmW2Vec = stackOverflowUtil.docClusterUtil.PopulateW2Vec(stackOverflowUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = stackOverflowUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			
			InstanceW2Vec centerVec = stackOverflowUtil.docClusterUtil.ComputeCenterInstanceW2Vec(testW2Vecs);
			InstanceW2Vec maxVec = stackOverflowUtil.docClusterUtil.ComputeMaxInstanceW2Vec(testW2Vecs);
			InstanceW2Vec weightCenterVec = stackOverflowUtil.docClusterUtil.ComputeWeightCenterInstanceW2Vec(centerVec, maxVec);
			
			double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil
					.SparsifyDocDisSimilarityMatrixByCenterVector(weightCenterVec, testW2Vecs);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-w2vec-google-wightCenterBased-20000", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateDocsDisSimilarityMatrixFromFileSparsification() {
		try{
			String simFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/stackoverflow-w2vec-sim-20000";
			
			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
			
			double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, StackOverflowConstant.NumberOfClusters, false);
			//double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomicExact(docSimMatrix, StackOverflowConstant.NumberOfClusters);
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\sparseMatrix-gtm-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-w2vec-nonAlpha-20000", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//	public void GenerateDocsDisSimilarityMatrixFromFileSparsificationFixedNumberOfSimilarities() {
//		try{
//			String simFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-gtm-sd-nonAlpha-Fixed";
//			
//			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
//			
//			double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixFixedNumberOfSimilarities(docSimMatrix, StackOverflowConstant.NumberOfClusters);
//			
//			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-gtm-Fixed-Number-Similarities", saprsifyMatrix, " ");
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}

	public void GenerateDocsDisSimilarityMatrixFromFileSparsificationBFixedNbyKSimilarities() {
		try{
			String simFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/stackoverflow-w2vec-sim-google-20000";
			
			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
			
			double [][] saprsifyMatrix = stackOverflowUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixFixedNbyKSimilarities(docSimMatrix, StackOverflowConstant.NumberOfClusters);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/sparseMatrix-w2vec-google-Alpha-20000-NbyK", saprsifyMatrix, " ");
						
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineTfIdf() {
		try{
			ArrayList<HashMap<String, Double>> docsTfIdfs = new TfIdfMatrixGenerator().ConstructTfIdfList(stackOverflowUtil.GetStackOverflowDocuments(), stackOverflowUtil.getUniqueWords());
			double [][] docSimMatrix = stackOverflowUtil.docClusterUtil.ComputeSimilarityMatrixTfIdfParallel(docsTfIdfs, 10);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/stackoverflow-tfidf-sim", docSimMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocSimMatrixW2Vce() {
		try{
			ArrayList<String []> alDocLabelFlat =stackOverflowUtil.getDocsStackOverflowFlat();
			//double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			
			HashMap<String, double[]> hmW2Vec = stackOverflowUtil.docClusterUtil.PopulateW2VecGoogle(stackOverflowUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = stackOverflowUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			double [][] docSimMatrix= stackOverflowUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);

			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/stackoverflow-w2vec-sim-google-20000", docSimMatrix, " ");

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void GenerateDocsDisSimilarityMatrixFromFileSparsificationIterative() {
		try{
			String simFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/stackoverflow-w2vec-sim-20000";
			//String simFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\web-snippet-w2vec-sim-google-2280";
			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
			
			//List<double[][]> alSparseDists = googlewebSnippetUtil.docClusterUtil.sparsificationUtilIterative.SparsifyDocDisSimilarityMatrixAlgorithomicIterative(docSimMatrix, GoogleWebSnippetConstant.NumberOfClusters);
			List<double[][]> alSparseDists = stackOverflowUtil.docClusterUtil.sparsificationUtilIterative.SparsifyDocDisSimilarityMatrixAlgorithomicExactIterative(docSimMatrix, StackOverflowConstant.NumberOfClusters);

//			for(int i=0;i< alSparseDists.size();i++){
//				double[][] sparseDistMatrix = alSparseDists.get(i);
//				UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/stackoverflow/stackoverflow-sparse-w2vec-google-alpha-20000-"+i, sparseDistMatrix, " ");
//				//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-w2vec-google-Alpha-2280-"+i, sparseDistMatrix, " ");
//			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
