package dal.clustering.document.agnews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringText;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringW2Vec;
import dal.clustering.document.shared.entities.ClusterResultConatainerText;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;

public class ClusterUnSupervisedAgNews {

	UnSupervisedClusteringText unSupervisedClusteringText;
	UnSupervisedClusteringW2Vec unSupervisedClusteringW2Vec;
	ClusterEvaluation clusterEvaluation;
	AgNewsUtil agNewsUtil;
	
	public ClusterUnSupervisedAgNews() throws IOException{
		agNewsUtil = new AgNewsUtil();
		
		clusterEvaluation = new ClusterEvaluation(agNewsUtil.docClusterUtil);
		unSupervisedClusteringW2Vec = new UnSupervisedClusteringW2Vec(agNewsUtil.getUniqueWords(),
				agNewsUtil.getDocsBiomedicalFlat(), agNewsUtil.getDocsBiomedicalList(), 
				agNewsUtil.docClusterUtil);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(agNewsUtil.docClusterUtil, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
	}

	public void ClusterDocsNGramBasedSimilarityGtm() {
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = agNewsUtil.getDocsBiomedicalList();
			ArrayList<String []> alDocLabelFlat = agNewsUtil.getDocsBiomedicalFlat();
			
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
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = agNewsUtil.getDocsBiomedicalList();
			ArrayList<String []> alDocLabelFlat = agNewsUtil.getDocsBiomedicalFlat();
			
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
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = agNewsUtil.getDocsBiomedicalList();
			ArrayList<String []> alDocLabelFlat = agNewsUtil.getDocsBiomedicalFlat();
			
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

}
