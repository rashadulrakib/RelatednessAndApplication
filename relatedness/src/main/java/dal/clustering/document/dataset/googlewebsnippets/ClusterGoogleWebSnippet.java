package dal.clustering.document.dataset.googlewebsnippets;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.entities.ClusterUnSupervised;

public class ClusterGoogleWebSnippet extends ClusterUnSupervised {
	protected GooglewebSnippetUtil googlewebSnippetUtil;
	
	public ClusterGoogleWebSnippet(){
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		clusterEvaluation = new ClusterEvaluation(googlewebSnippetUtil.docClusterUtil);
		tfIdfMatrixGenerator = new TfIdfMatrixGenerator();
	}
}
