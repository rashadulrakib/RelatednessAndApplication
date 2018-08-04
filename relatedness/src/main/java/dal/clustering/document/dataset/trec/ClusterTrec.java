package dal.clustering.document.dataset.trec;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.entities.ClusterUnSupervised;

public class ClusterTrec extends ClusterUnSupervised {
	protected TrecUtil trecUtil;
	
	public ClusterTrec(){
		trecUtil = new TrecUtil();
		clusterEvaluation = new ClusterEvaluation(trecUtil.docClusterUtil);
		tfIdfMatrixGenerator = new TfIdfMatrixGenerator();
	}
}
