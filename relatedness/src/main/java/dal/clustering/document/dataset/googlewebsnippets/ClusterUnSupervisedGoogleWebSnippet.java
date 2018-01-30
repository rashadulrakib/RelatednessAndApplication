package dal.clustering.document.dataset.googlewebsnippets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.cluster.SemiSupervisedClusteringW2Vec;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringText;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringW2Vec;
import dal.clustering.document.shared.entities.ClusterResultConatainerText;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.clustering.document.shared.entities.PreprocessedContainer;
import dal.utils.common.general.UtilsShared;

public class ClusterUnSupervisedGoogleWebSnippet {
	
	SemiSupervisedClusteringW2Vec semiSupervisedClusteringW2Vec;

	UnSupervisedClusteringText unSupervisedClusteringText;
	UnSupervisedClusteringW2Vec unSupervisedClusteringW2Vec;
	
	ClusterEvaluation clusterEvaluation;
	GooglewebSnippetUtil googlewebSnippetUtil;
	
	public ClusterUnSupervisedGoogleWebSnippet() throws IOException{
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		clusterEvaluation = new ClusterEvaluation(googlewebSnippetUtil.docClusterUtil);
		//semiSupervisedClusteringW2Vec = new SemiSupervisedClusteringW2Vec(googlewebSnippetUtil.docClusterUtil);
		
//		unSupervisedClusteringW2Vec = new UnSupervisedClusteringW2Vec(googlewebSnippetUtil.GetUniqueWords(),
//				googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat(), googlewebSnippetUtil.GetDocsGoogleWebSnippetList(), 
//				googlewebSnippetUtil.docClusterUtil);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(googlewebSnippetUtil.docClusterUtil, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
		unSupervisedClusteringText = new UnSupervisedClusteringText(googlewebSnippetUtil.docClusterUtil);
	}
	
	public void ClusterDocsNGramBasedSimilarityGtm() {
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = googlewebSnippetUtil.GetDocsGoogleWebSnippetList();
			ArrayList<String []> alDocLabelFlat = googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = googlewebSnippetUtil
					.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);
			
//			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
//			hmTrainDocsLabelBody.put("business", new ArrayList<>(Arrays.asList("sba gov oit business administration temp programoffice style breaking trade game business guide exporting export gov export legal assistance network elan african growth opportunity")));
//			hmTrainDocsLabelBody.put("computers", new ArrayList<>(Arrays.asList("slac stanford bfroot computing environment tools srt srtuser user guide software release tools instructions software release tools softreltools srt releases test versions")));
//			hmTrainDocsLabelBody.put("health", new ArrayList<>(Arrays.asList("prospects cms showpage explore types jobs types job eipal state showocc idno doctor hospital job description activities hospital doctors apply medical knowledge skills diagnosis prevention treatment illnesses diseases infections patients")));
//			hmTrainDocsLabelBody.put("engineering", new ArrayList<>(Arrays.asList("esr oxfordjournals cgi reprint ijkey xyywzrsofm keytype inheritance inequality theoretical reasoning empirical institution google indexer sign personal subscriber oxford journals social sciences european sociological review volume")));
//			hmTrainDocsLabelBody.put("education-science", new ArrayList<>(Arrays.asList("sciencemag current dtl science aaas table contents transitions high school pillars college science philip sadler american association advancement science")));
//			hmTrainDocsLabelBody.put("politics-society", new ArrayList<>(Arrays.asList("inter kdlp democratic labor party minjoo nodong dang labor party news releases vision statement overview history leadership")));
//			hmTrainDocsLabelBody.put("culture-arts-entertainment", new ArrayList<>(Arrays.asList("nytimes technology onyx adxnnl partner rssnyt emc adxnnlx pnq qbqxuuwjxymxgch movie audience picks scene york software program nav film onyx project viewers links click serve departure scene")));
//			hmTrainDocsLabelBody.put("sports", new ArrayList<>(Arrays.asList("arenafan arenafan online afl arena football resource arenafan premier community fans arena football league news scores standings statistics games")));

			ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringText
					.PerformUnSeupervisedSeededClusteringByGtmWordSimIterative(hmTrainDocsLabelBody, alDocLabelFlat);
			
			clusterEvaluation.ClusterEvaluationGeneratorText(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneText(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(clusterResultConatainerText.FinalCluster);
			
			clusterEvaluation.ClusterEvaluationGeneratorTextExternal(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingTextExternal(clusterResultConatainerText.FinalCluster);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterDocsSimilarityByW2VecFollowingGtm() {
		try{
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = googlewebSnippetUtil.GetDocsGoogleWebSnippetList();
			ArrayList<String []> alDocLabelFlat = googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = googlewebSnippetUtil
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
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = googlewebSnippetUtil.GetDocsGoogleWebSnippetList();
			ArrayList<String []> alDocLabelFlat = googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = googlewebSnippetUtil
					.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);

//seed for small dataset
//			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
//			hmTrainDocsLabelBody.put("business", new ArrayList<>(Arrays.asList("whitepaper wstonline keyword cmpwstonline programming bank transaction software programming bank transaction software programming bank transaction software wall street technology bank edwardsville chose echo solve disaster recovery backup issues echo itera")));
//			hmTrainDocsLabelBody.put("computers", new ArrayList<>(Arrays.asList("islandnet kpolsson comphist chronology personal computers gray founds amateur computer society publishing acs newsletter birth personal computing")));
//			hmTrainDocsLabelBody.put("health", new ArrayList<>(Arrays.asList("eurekalert pub releases uoc lhp hiv prevention programs cheaper ucsf led team researchers hiv prevention programs middle income countries efficiency program")));
//			hmTrainDocsLabelBody.put("engineering", new ArrayList<>(Arrays.asList("microsoft barc mediapresence mylifebits mylifebits project microsoft barc media presence researcher records life computer cbs evening news perfect memory wattnow lifeblogging virtual brain")));
//			hmTrainDocsLabelBody.put("education-science", new ArrayList<>(Arrays.asList("dbwilson exact web random sampling markov chains ggstr wrote course book finite markov chains algorithmic applications chapter cftp")));
//			hmTrainDocsLabelBody.put("politics-society", new ArrayList<>(Arrays.asList("tomcruisefan tomcruisefan tomcruiseweb tom cruise resource tomcruisefan tomcruiseweb news media resource tom cruise web news pictures")));
//			hmTrainDocsLabelBody.put("culture-arts-entertainment", new ArrayList<>(Arrays.asList("drewbarfield drew barfield news album purchase online sale drew solo los pacaminos gigs")));
//			hmTrainDocsLabelBody.put("sports", new ArrayList<>(Arrays.asList("wbaonline wbaonline boxing news schedule ratings history boxing association sanctioning body comprehensive website features champions")));			
			
//seed for large-dataset			
//			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
//			hmTrainDocsLabelBody.put("business", new ArrayList<>(Arrays.asList("psdblog worldbank psdblog predicting predicting category psd blog bank knowledge services sector development reading lists toolkits journals surveys business database")));
//			hmTrainDocsLabelBody.put("computers", new ArrayList<>(Arrays.asList("interstrength curriculum selfdiscoveryprocess discovery process interstrength associates temperament institute leader language organizations multiple model")));
//			hmTrainDocsLabelBody.put("health", new ArrayList<>(Arrays.asList("cde state stateinfo slstpschbib school topics weekly entries tips topics health history outdoors politics http cospl blogspot")));
//			hmTrainDocsLabelBody.put("engineering", new ArrayList<>(Arrays.asList("memory loc gov ammem mcchtml scihm science medicine exploration schematics diagrams papers forest inventor vacuum tube electronic devices development radio")));
//			hmTrainDocsLabelBody.put("education-science", new ArrayList<>(Arrays.asList("sciencemag current dtl science aaas table contents search journal keyword transitions high school pillars college science philip sadler tai")));
//			hmTrainDocsLabelBody.put("politics-society", new ArrayList<>(Arrays.asList("inter kdlp democratic labor party minjoo nodong dang labor party news releases vision statement overview history leadership")));
//			hmTrainDocsLabelBody.put("culture-arts-entertainment", new ArrayList<>(Arrays.asList("mangadownload mangadownload mangadownload downloads archive manga web people signed")));
//			hmTrainDocsLabelBody.put("sports", new ArrayList<>(Arrays.asList("missouristatebears sportselect dbml oem spid swimming news missouristatebears official web missouri women swimming diving teams season nation mid major programs")));
			
			ClusterResultConatainerVector clusterResultConatainer = unSupervisedClusteringW2Vec.PerformUnSeuperVisedSeededClusteringByW2VecAverageVec
					(hmTrainDocsLabelBody, alDocLabelFlat);
			
			clusterEvaluation.ClusterEvaluationGeneratorVector(clusterResultConatainer.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneVector(clusterResultConatainer.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(clusterResultConatainer.FinalCluster);
			
			clusterEvaluation.ClusterEvaluationGeneratorVectorExternal(clusterResultConatainer.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVectorExternal(clusterResultConatainer.FinalCluster);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterDocsNGramBasedSimilarityGtmAndW2Vec() {
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = googlewebSnippetUtil.GetDocsGoogleWebSnippetList();
			
			PreprocessedContainer preprocessedContainer = googlewebSnippetUtil.docClusterUtil.GetTrainTestDocsLabelBodyAndUniqueWords(docsLabelBody);

			ClusterResultConatainerVector clusterResultConatainer = PerformUnsupervisedSeededClusteringByW2Vec(preprocessedContainer);
			
			//PerformUnsupervisedSeededClusteringByGTM(preprocessedContainer);
			PerformUnsupervisedKemansSeededClusteringByGTM(clusterResultConatainer, docsLabelBody);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterDocsBySimilarityMatrixGtm(){
		try{
			//for small-dataset-2280
//			ArrayList<String []> alDocLabelFlat = googlewebSnippetUtil.getDocsGoogleWebSnippetFlat();
//			
//			double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilText);
//
//			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
//			
//			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			//end
			
			ArrayList<String []> alDocLabelFlat =googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			//ArrayList<String []> alDocLabelFlat = new ArrayList<String []>(googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat().subList(0, 40));
			
			//alDocLabelFlat = googlewebSnippetUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 700, 0);
			
			double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilGtm);

			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix----", saprsifyMatrix, " ");
			
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, GoogleWebSnippetConstant.GoogleWebSnippetDocsFile, "\t");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
//	public void ClusterDocsBySimilarityMatrixTrWp(){
//		try{
//			//ArrayList<String []> alDocLabelFlat = googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
//			
//			ArrayList<ArrayList<String>> docsByPhWords = unSupervisedClusteringText.docClusterUtilTrWP.textRelatednessTrwp.GetPhWordLists(googlewebSnippetUtil.GetBodies());
//			
//			System.out.println(docsByPhWords.size());
//			
//			unSupervisedClusteringText.docClusterUtilTrWP.textRelatednessTrwp.GeneratePhWordPairs(docsByPhWords);
//			
//			//double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeSimilarityMatrixTrWP(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilTrWP);
//			//double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeSimilarityMatrixTrWP(docsByPhWords, unSupervisedClusteringText.docClusterUtilTrWP);
//
//			//double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
//			
//			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
//			//UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/sparseMatrix-small-trwp-sd-0", saprsifyMatrix , " ");
//
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
	
	public void ClusterDocsBySimilarityMatrixTrWp(){
		try{
			//ArrayList<String []> alDocLabelFlat = new ArrayList<String []>(googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat().subList(0, 40));
			ArrayList<String []> alDocLabelFlat = googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			
			//double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeSimilarityMatrixTrWP(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilTrWP);
			
			double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeSimilarityMatrixTrWPParallel(alDocLabelFlat, 
					unSupervisedClusteringText.docClusterUtilTrWP,10);
			
			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix--", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/sparseMatrix-small-trwp-sd-0", saprsifyMatrix , " ");

			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterDocsBySimilarityMatrixEuclidianW2Vec(){
		try{
//			ArrayList<String []> alDocLabelFlat = googlewebSnippetUtil.getDocsGoogleWebSnippetFlat();
//			
//			double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeEuclidianDistanceMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
//			
//			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
//			
//			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			
			ArrayList<String []> alDocLabelFlat =googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			
			alDocLabelFlat = googlewebSnippetUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 700, 0);
			
			double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeEuclidianDistanceMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);

			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, GoogleWebSnippetConstant.GoogleWebSnippetDocsFile, "\t");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification(){
		try{
			ArrayList<String []> alDocLabelFlat =googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			//double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			
			HashMap<String, double[]> hmW2Vec = googlewebSnippetUtil.docClusterUtil.PopulateW2Vec(googlewebSnippetUtil.GetUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = googlewebSnippetUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 10);
			
			/*double [][] nonSaprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, GoogleWebSnippetConstant.NumberOfClusters, false);
			//UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/sparseMatrix-w2vec-sd-nonAlpha-Fixed", nonSaprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-w2vec-sd-nonAlpha-Fixed", nonSaprsifyMatrix, " ");
			*/
			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, GoogleWebSnippetConstant.NumberOfClusters, true);
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/sparseMatrix-w2vec-sd-Alpha-Fixed", saprsifyMatrix, " ");
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-w2vec-sd-Alpha-Fixed", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixGtmFixedSparsification(){
		try{
			ArrayList<String []> alDocLabelFlat =googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			//alDocLabelFlat = googlewebSnippetUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 380, 0);
			//ArrayList<String []> alDocLabelFlat = new ArrayList<String []>(googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat().subList(0, 20));

			double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilGtm);
			
			double [][] nonSaprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, GoogleWebSnippetConstant.NumberOfClusters, false);
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-gtm-sd-nonAlpha-Fixed", nonSaprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/sparseMatrix-gtm-sd-nonAlpha-12340-Fixed", nonSaprsifyMatrix, " ");
			
			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, GoogleWebSnippetConstant.NumberOfClusters, true);
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-gtm-sd-Alpha-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/sparseMatrix-gtm-sd-Alpha-12340-Fixed", saprsifyMatrix, " ");
			//UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/sparseMatrix-gtm-sd-0-Fixed", saprsifyMatrix, " ");
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, GoogleWebSnippetConstant.GoogleWebSnippetDocsFile, "\t");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecCenterBasedSparsification() {
		try{

			ArrayList<String []> alDocLabelFlat =googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			
			HashMap<String, double[]> hmW2Vec = googlewebSnippetUtil.docClusterUtil.PopulateW2Vec(googlewebSnippetUtil.GetUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = googlewebSnippetUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			
			InstanceW2Vec centerVec = googlewebSnippetUtil.docClusterUtil.ComputeCenterInstanceW2Vec(testW2Vecs);
			
			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil
					.SparsifyDocDisSimilarityMatrixByCenterVector(centerVec, testW2Vecs);
			
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/sparseMatrix-w2vec-CenterBased", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterDocsBySimilarityMatrixCosineW2Vec(){
		try{
//			ArrayList<String []> alDocLabelFlat = googlewebSnippetUtil.getDocsGoogleWebSnippetFlat();
//			
//			double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
//			
//			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
//			
//			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			
			ArrayList<String []> alDocLabelFlat =googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat();
			
//			alDocLabelFlat = googlewebSnippetUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 700, 0);
//			
			double [][] docSimMatrix= googlewebSnippetUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			double [][] saprsifyMatrix = googlewebSnippetUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix", saprsifyMatrix, " ");
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, GoogleWebSnippetConstant.GoogleWebSnippetDocsFile, "\t");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void PerformUnsupervisedKemansSeededClusteringByGTM(
			ClusterResultConatainerVector clusterResultConatainer,
			LinkedHashMap<String, ArrayList<String>> docsLabelBody) {
		
		try{
			LinkedHashMap<String, ArrayList<String>> HmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
			
			ArrayList<String[]> AlTestDocsBodyLabel =new ArrayList<String[]>();
			
			for(String label: docsLabelBody.keySet()){
				if(clusterResultConatainer.InstanceClosestToCenter.get(label)!=null){
					InstanceW2Vec inst = clusterResultConatainer.InstanceClosestToCenter.get(label);
					ArrayList<String> docs = docsLabelBody.get(label);
					
					ArrayList<String> singleAl = new ArrayList<String>();
					singleAl.add(inst.Text);
					HmTrainDocsLabelBody.put(label, singleAl);
					
					docs.remove(inst.Text);
					
					for(String doc: docs){
						String bodyLabel [] = new String[2];
						bodyLabel[0] = doc;
						bodyLabel[1] = label;
						
						AlTestDocsBodyLabel.add(bodyLabel);
					}
				}
			}
			
			ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringText.PerformUnSeupervisedSeededClusteringByGtmWordSim
					(HmTrainDocsLabelBody, AlTestDocsBodyLabel);
			
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneText(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(clusterResultConatainerText.FinalCluster);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void PerformUnsupervisedSeededClusteringByGTM(PreprocessedContainer preprocessedContainer) {
		try{
			ClusterResultConatainerText clusterResultConatainer = unSupervisedClusteringText.PerformUnSeupervisedSeededClusteringByGtmWordSim
					(preprocessedContainer.HmTrainDocsLabelBody, preprocessedContainer.AlTestDocsBodyLabel);
			
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneText(clusterResultConatainer.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(clusterResultConatainer.FinalCluster);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private ClusterResultConatainerVector PerformUnsupervisedSeededClusteringByW2Vec(PreprocessedContainer preprocessedContainer) {
		ClusterResultConatainerVector clusterResultConatainer = null;
		
		try{
			HashMap<String, double[]> hmW2Vec = googlewebSnippetUtil.docClusterUtil.PopulateW2Vec(preprocessedContainer.UniqueWords);
			LinkedHashMap<String, ArrayList<double []>> trainW2Vecs = googlewebSnippetUtil.docClusterUtil.CreateW2VecForTrainData( preprocessedContainer.HmTrainDocsLabelBody, hmW2Vec);
			
			ArrayList<InstanceW2Vec> testW2Vecs = googlewebSnippetUtil.docClusterUtil.CreateW2VecForTestData(preprocessedContainer.AlTestDocsBodyLabel, hmW2Vec);
			
			clusterResultConatainer = semiSupervisedClusteringW2Vec.PerformSemiSeuperVisedClustering(trainW2Vecs, testW2Vecs);
		
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneVector(clusterResultConatainer.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(clusterResultConatainer.FinalCluster);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return clusterResultConatainer;
	}	
}
