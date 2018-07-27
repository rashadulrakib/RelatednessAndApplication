package dal.clustering.document.dataset.stackoverflow;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.entities.ClusterUnSupervised;

public class ClusterStackOverflow extends ClusterUnSupervised {
	protected StackOverflowUtil stackOverflowUtil;
	
	public ClusterStackOverflow(){
		stackOverflowUtil = new StackOverflowUtil();
		clusterEvaluation = new ClusterEvaluation(stackOverflowUtil.docClusterUtil);
		tfIdfMatrixGenerator = new TfIdfMatrixGenerator();
	}
}
