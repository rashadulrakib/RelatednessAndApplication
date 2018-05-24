package dal.clustering.document.shared.sparsification;

import java.util.Date;
import java.util.List;

import dal.utils.common.general.UtilsShared;

public class SparsificationUtilRAD {
	public double[][] SparsifyDocDisSimilarityMatrixAlgorithomicExactIterative(
			double[][] docSimMatrix, int numberofclusters) {
		
		double [][] sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);
		
		try{
			System.out.println("Start SparsifyDocDisSimilarityMatrixAlgorithomicExact " + new Date().toLocaleString());
			
			int itemsPerCluster = docSimMatrix.length/numberofclusters;
			int itemsToKeep = (itemsPerCluster*docSimMatrix.length-docSimMatrix.length)/1;
			
			List<Alpha> alAlpha = SparsificationUtilHelper.PrepareAlphaValuesDiagonal(docSimMatrix, false);
			
			List<Alpha> alAlphaSublist = alAlpha.subList(0, itemsToKeep);
			
			sparsifyDistMatrix = SparsificationUtilHelper.PrepareSparsifiedMatrix(docSimMatrix, alAlphaSublist);
			
			for(int i=0;i<docSimMatrix.length;i++){
				sparsifyDistMatrix[i][i] = 0;
			}
			
			System.out.println("populate dist matrix, AlAlphaSublist="+alAlphaSublist.size()+" "+ new Date().toLocaleString());
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifyDistMatrix;
	}
	
}
