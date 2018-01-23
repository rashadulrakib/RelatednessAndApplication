package dal.clustering.document.dataset.googlewebsnippets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.cluster.SemiSupervisedClusteringW2Vec;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.clustering.document.shared.entities.PreprocessedContainer;

public class ClusterSemiSupervisedGoogleWebSnippet {

	GooglewebSnippetUtil googlewebSnippetUtil;
	SemiSupervisedClusteringW2Vec semiSupervisedClusteringVector;
	ClusterEvaluation clusterEvaluation;
	
	public ClusterSemiSupervisedGoogleWebSnippet(){
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		semiSupervisedClusteringVector = new SemiSupervisedClusteringW2Vec(googlewebSnippetUtil.docClusterUtil);
		clusterEvaluation = new ClusterEvaluation(googlewebSnippetUtil.docClusterUtil);
	}
	
	public void ClusterDocsW2VecBasedSimilarity(){
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = googlewebSnippetUtil.GetDocsGoogleWebSnippetList();
			
			PreprocessedContainer preprocessedContainer = googlewebSnippetUtil.docClusterUtil.GetTrainTestDocsLabelBodyAndUniqueWords(0.02, docsLabelBody);

			HashMap<String, double[]> hmW2Vec = googlewebSnippetUtil.docClusterUtil.PopulateW2Vec(preprocessedContainer.UniqueWords);
			LinkedHashMap<String, ArrayList<double []>> trainW2Vecs = googlewebSnippetUtil.docClusterUtil.CreateW2VecForTrainData( preprocessedContainer.HmTrainDocsLabelBody, hmW2Vec);
			ArrayList<InstanceW2Vec> testW2Vecs = googlewebSnippetUtil.docClusterUtil.CreateW2VecForTestData(preprocessedContainer.AlTestDocsBodyLabel, hmW2Vec);
			
			ClusterResultConatainerVector clusterResultConatainer = semiSupervisedClusteringVector.PerformSemiSeuperVisedClustering(trainW2Vecs, testW2Vecs);
		
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(clusterResultConatainer.FinalCluster);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
