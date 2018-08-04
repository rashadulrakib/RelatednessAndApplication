package dal.clustering.document.dataset.trec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dal.clustering.document.dataset.biomedical.BioMedicalConstant;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.general.UtilsShared;

public class ClusterUnSupervisedTrec extends ClusterTrec{
	public void GenerateDocSimMatrixW2Vec() {
		try{
			ArrayList<String []> alDocLabelFlat =trecUtil.getTrecFlat();
			
			HashMap<String, double[]> hmW2Vec = trecUtil.docClusterUtil.PopulateW2Vec(trecUtil.getUniqueWords());
			ArrayList<InstanceW2Vec> testW2Vecs = trecUtil.docClusterUtil.CreateW2VecForTestData(alDocLabelFlat, hmW2Vec);			
			double [][] docSimMatrix= trecUtil.docClusterUtil.ComputeCosineMatrixW2VecParallel(testW2Vecs, 2);

			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\trec-glove-sim", docSimMatrix, " ");
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, TrecConstant.TrecDocsFile, "\t");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixFromFileSparsificationIterative() {
		try{
			String simFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\trec-glove-sim";
			double [][] docSimMatrix= UtilsShared.LoadMatrixFromFile(simFile);
			
			List<double[][]> alSparseDists = trecUtil.docClusterUtil.sparsificationUtilIterative.SparsifyDocDisSimilarityMatrixAlgorithomicExactIterative(docSimMatrix, TrecConstant.NumberOfClusters); //RAD
			double [][] docDisSimMatrixDense = UtilsShared.CopyMatrix(docSimMatrix, true);
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\trec\\trec-glove-dense-distmatrix", docDisSimMatrixDense, " ");
						
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
