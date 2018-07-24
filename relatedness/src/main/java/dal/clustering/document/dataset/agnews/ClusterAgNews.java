package dal.clustering.document.dataset.agnews;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.entities.ClusterUnSupervised;

public class ClusterAgNews extends ClusterUnSupervised {
	protected AgNewsUtil agNewsUtil;
	
	public ClusterAgNews(){
		agNewsUtil = new AgNewsUtil();
		clusterEvaluation = new ClusterEvaluation(agNewsUtil.docClusterUtil);
		tfIdfMatrixGenerator = new TfIdfMatrixGenerator();
	}
}
