package dal.clustering.document.dataset.biomedical;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.entities.ClusterUnSupervised;

public class ClusterBioMedical extends ClusterUnSupervised {
	protected BioMedicalUtil bioMedicalUtil;
	
	public ClusterBioMedical(){
		bioMedicalUtil = new BioMedicalUtil();
		clusterEvaluation = new ClusterEvaluation(bioMedicalUtil.docClusterUtil);
		tfIdfMatrixGenerator = new TfIdfMatrixGenerator();
	}
}
