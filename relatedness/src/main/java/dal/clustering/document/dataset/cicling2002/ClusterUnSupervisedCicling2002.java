package dal.clustering.document.dataset.cicling2002;

import java.io.IOException;
import java.util.ArrayList;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringText;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringW2Vec;
import dal.utils.common.general.UtilsShared;

public class ClusterUnSupervisedCicling2002 {
	UnSupervisedClusteringText unSupervisedClusteringText;
	UnSupervisedClusteringW2Vec unSupervisedClusteringW2Vec;
	ClusterEvaluation clusterEvaluation;
	Cicling2002Util cicling2002Util;
	
	public ClusterUnSupervisedCicling2002() throws IOException{
		cicling2002Util = new Cicling2002Util();
		clusterEvaluation = new ClusterEvaluation(cicling2002Util.docClusterUtil);
//		unSupervisedClusteringW2Vec = new UnSupervisedClusteringW2Vec(cicling2002Util.getUniqueWords(),
//				cicling2002Util.getDocsCicling2002Flat(), cicling2002Util.getDocsCicling2002List(), 
//				cicling2002Util.docClusterUtil);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(cicling2002Util.docClusterUtil, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
		unSupervisedClusteringText = new UnSupervisedClusteringText(cicling2002Util.docClusterUtil);
	}
	
	public void ClusterDocsBySimilarityMatrixCosineW2Vec(){
		try{
			ArrayList<String []> alDocLabelFlat = cicling2002Util.getDocsCicling2002Flat();
			
			double [][] docSimMatrix= cicling2002Util.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			
			double [][] saprsifyMatrix = cicling2002Util.docClusterUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterDocsBySimilarityMatrixGtm() {
		try{
			ArrayList<String []> alDocLabelFlat = cicling2002Util.getDocsCicling2002Flat();
			
			double [][] docSimMatrix= cicling2002Util.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilGtm);

			double [][] saprsifyMatrix = cicling2002Util.docClusterUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
