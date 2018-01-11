package dal.clustering.document.dataset.googlewebsnippets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.DocClusterUtil;
import dal.clustering.document.shared.cluster.SemiSupervisedClusteringVector;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.clustering.document.shared.entities.PreprocessedContainer;

public class ClusterSemiSupervisedGoogleWebSnippet {

	GooglewebSnippetUtil googlewebSnippetUtil;
	DocClusterUtil docClusterUtil;
	SemiSupervisedClusteringVector semiSupervisedClusteringVector;
	ClusterEvaluation clusterEvaluation;
	
	public ClusterSemiSupervisedGoogleWebSnippet(){
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		docClusterUtil = new DocClusterUtil();
		semiSupervisedClusteringVector = new SemiSupervisedClusteringVector();
		clusterEvaluation = new ClusterEvaluation(docClusterUtil);
	}
	
	public void ClusterDocsW2VecBasedSimilarity(){
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = googlewebSnippetUtil.getDocsGoogleWebSnippetList();
			
			PreprocessedContainer preprocessedContainer = docClusterUtil.GetTrainTestDocsLabelBodyAndUniqueWords(0.02, docsLabelBody);

			HashMap<String, double[]> hmW2Vec = docClusterUtil.PopulateW2Vec(preprocessedContainer.UniqueWords);
			LinkedHashMap<String, ArrayList<double []>> trainW2Vecs = docClusterUtil.CreateW2VecForTrainData( preprocessedContainer.HmTrainDocsLabelBody, hmW2Vec);
			ArrayList<InstanceW2Vec> testW2Vecs = docClusterUtil.CreateW2VecForTestData(preprocessedContainer.AlTestDocsBodyLabel, hmW2Vec);
			
			ClusterResultConatainerVector clusterResultConatainer = semiSupervisedClusteringVector.PerformSemiSeuperVisedClustering(trainW2Vecs, testW2Vecs);
		
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(clusterResultConatainer.FinalCluster);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
