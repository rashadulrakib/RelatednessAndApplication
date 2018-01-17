package dal.clustering.document.dataset.agnews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.cluster.SemiSupervisedClusteringW2Vec;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.clustering.document.shared.entities.PreprocessedContainer;

public class ClusterSemiSupervisedAgNews {
	AgNewsUtil agNewsUtil;
	SemiSupervisedClusteringW2Vec semiSupervisedClustering;
	ClusterEvaluation clusterEvaluation;
	
	public ClusterSemiSupervisedAgNews(){
		agNewsUtil = new AgNewsUtil();
		semiSupervisedClustering = new SemiSupervisedClusteringW2Vec();
		clusterEvaluation = new ClusterEvaluation(agNewsUtil.docClusterUtil);
	}
	
	public void ClusterDocsW2VecBasedSimilarity(){
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = agNewsUtil.getAgNewsList();
			
			PreprocessedContainer preprocessedContainer = agNewsUtil.docClusterUtil.GetTrainTestDocsLabelBodyAndUniqueWords(0.05, docsLabelBody);

			HashMap<String, double[]> hmW2Vec = agNewsUtil.docClusterUtil.PopulateW2Vec(preprocessedContainer.UniqueWords);
			LinkedHashMap<String, ArrayList<double []>> trainW2Vecs = agNewsUtil.docClusterUtil.CreateW2VecForTrainData( preprocessedContainer.HmTrainDocsLabelBody, hmW2Vec);
			ArrayList<InstanceW2Vec> testW2Vecs = agNewsUtil.docClusterUtil.CreateW2VecForTestData(preprocessedContainer.AlTestDocsBodyLabel, hmW2Vec);
			
			ClusterResultConatainerVector clusterResultConatainer = semiSupervisedClustering.PerformSemiSeuperVisedClustering(trainW2Vecs, testW2Vecs);
			
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(clusterResultConatainer.FinalCluster);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterDocsNGramBasedSimilarity() {
		try{
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
