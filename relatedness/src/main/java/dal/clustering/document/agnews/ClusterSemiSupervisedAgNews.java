package dal.clustering.document.agnews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.DocClusterUtil;
import dal.clustering.document.shared.cluster.SemiSupervisedClusteringVector;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.clustering.document.shared.entities.PreprocessedContainer;

public class ClusterSemiSupervisedAgNews {
	AgNewsUtil agNewsUtil;
	DocClusterUtil docClusterUtil;
	SemiSupervisedClusteringVector semiSupervisedClustering;
	ClusterEvaluation clusterEvaluation;
	
	public ClusterSemiSupervisedAgNews(){
		agNewsUtil = new AgNewsUtil();
		docClusterUtil = new DocClusterUtil();
		semiSupervisedClustering = new SemiSupervisedClusteringVector();
		clusterEvaluation = new ClusterEvaluation();
	}
	
	public void ClusterDocsW2VecBasedSimilarity(){
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = agNewsUtil.getDocsBiomedicalList();
			
			PreprocessedContainer preprocessedContainer = docClusterUtil.GetTrainTestDocsLabelBodyAndUniqueWords(0.05, docsLabelBody);

			HashMap<String, double[]> hmW2Vec = docClusterUtil.PopulateW2Vec(preprocessedContainer.UniqueWords);
			LinkedHashMap<String, ArrayList<double []>> trainW2Vecs = docClusterUtil.CreateW2VecForTrainData( preprocessedContainer.HmTrainDocsLabelBody, hmW2Vec);
			ArrayList<InstanceW2Vec> testW2Vecs = docClusterUtil.CreateW2VecForTestData(preprocessedContainer.AlTestDocsBodyLabel, hmW2Vec);
			
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
