package dal.clustering.document.shared.entities;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringText;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringW2Vec;

public class ClusterUnSupervised {
	protected UnSupervisedClusteringText unSupervisedClusteringText;
	protected UnSupervisedClusteringW2Vec unSupervisedClusteringW2Vec;
	protected TfIdfMatrixGenerator tfIdfMatrixGenerator;
	protected ClusterEvaluation clusterEvaluation;
}
