package dal.clustering.document.shared.sparsification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import dal.utils.common.general.UtilsShared;

public class SparsificationUtilIterative {

	public List<double[][]> SparsifyDocDisSimilarityMatrixAlgorithomicIterative(double[][] docSimMatrix, int numberOfclusters) {
		
		List<double[][]> alSparseDists = new ArrayList<double[][]>();
		
		try{
			
			System.out.println("Start SparsifyDocDisSimilarityMatrixAlgorithomicIterative");
			
			int itemsPerCluster = docSimMatrix.length/numberOfclusters;
			int itemsToKeep = itemsPerCluster*docSimMatrix.length-docSimMatrix.length;
			//int itemsToKeep = itemsPerCluster*docSimMatrix.length;
			
			int mulConst = 1;

			List<Alpha> alAlpha = new ArrayList<Alpha>();
			
			for(int i=0;i<docSimMatrix.length;i++){
				double simArr [] = docSimMatrix[i];
				
				double simSim = 0.0;
				for(int j=0;j<simArr.length;j++){
					if(j==i) continue;
					simSim= simSim + simArr[j]*mulConst;
				}
				
				double avgSimSum = simSim/ simArr.length;
				
				double varianceSum = 0;
				
				for(int j=0;j<simArr.length;j++){
					if(j==i) continue;
					varianceSum = varianceSum + (simArr[j]*mulConst-avgSimSum)*(simArr[j]*mulConst-avgSimSum);
				}
				
				double sd = Math.sqrt(varianceSum/simArr.length);
				
				for(int j=0;j<simArr.length;j++){
					if(j==i) continue;
					double alphaVal = (simArr[j]-avgSimSum)/sd;
					Alpha objAlpha = new Alpha(alphaVal, i, j);
					alAlpha.add(objAlpha);
				}
			}
			
			
			System.out.println("Sorting alpha values");
			
			Collections.sort(alAlpha, new Comparator<Alpha>() {
			    @Override
			    public int compare(Alpha o1, Alpha o2) {
			        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
			    	// return lhs.customInt > rhs.customInt ? -1 : (lhs.customInt < rhs.customInt) ? 1 : 0;
			        return o2.getValue().compareTo(o1.getValue());
			    }
			});
			
			System.out.println("end Sorting alpha values");
			
			List<Alpha> AlAlphaSublist = null;
				
			boolean isGoodAvg = false;
			double alphaFactor = 1.0;
			HashSet<Double> uniqueDiffs = new  HashSet<Double>();
			while(!isGoodAvg){
				
				double [][] sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);
				
				for(int i=0;i<docSimMatrix.length;i++){
					sparsifyDistMatrix[i][i] = 0;
				}
				
				AlAlphaSublist = alAlpha.subList(0, (int)(itemsToKeep*alphaFactor));
				
				System.out.println("populate dist matrix, AlAlphaSublist="+AlAlphaSublist.size());
				
				for(Alpha alObj: AlAlphaSublist){
					int row = alObj.getRow();
					int col = alObj.getCol();
					
					if(docSimMatrix[row][col]!=docSimMatrix[col][row] || row==col){
						System.out.println("not same="+row+","+col);
					}
					
					sparsifyDistMatrix[row][col] = (1-docSimMatrix[row][col])*SparsificationConstant.SimMultipleConstant;
					sparsifyDistMatrix[col][row] = (1-docSimMatrix[col][row])*SparsificationConstant.SimMultipleConstant;
				}
				
				System.out.println("end populate dist matrix");
				
				double sumAvgCount = 0;
				double arrAvgCount [] = new double [sparsifyDistMatrix.length];
				for(int i=0;i<sparsifyDistMatrix.length;i++){
					int count =0;
					for(int j=0;j<sparsifyDistMatrix.length;j++){
						if(sparsifyDistMatrix[i][j]!=SparsificationConstant.LargeDistValue){
							count++;
						}
					}
					arrAvgCount[i]= count;
					sumAvgCount=sumAvgCount+count;
					//System.out.println("sparsified count="+count);
				}
				
				double avgAvgCount = sumAvgCount/sparsifyDistMatrix.length;
				
				double diff = Math.abs(avgAvgCount-(double)itemsPerCluster);
				
				System.out.println("sparsified avg count="+avgAvgCount+", alphaFactor="+alphaFactor+", diff="+diff);

				if(SparsificationUtilHelper.IsEnd(diff, uniqueDiffs, alphaFactor)){
					isGoodAvg = true;
				}else{
					//if(avgAvgCount>(double)itemsPerCluster && avgAvgCount-(double)itemsPerCluster>400){
					//if(avgAvgCount>(double)itemsPerCluster && avgAvgCount-(double)itemsPerCluster>400){	
					if(avgAvgCount>(double)itemsPerCluster){
						//if(avgAvgCount>(double)itemsPerCluster && avgAvgCount-(double)itemsPerCluster>itemsPerCluster ){
							alphaFactor=alphaFactor-0.05;
					}else if(avgAvgCount<(double)itemsPerCluster){
							alphaFactor=alphaFactor+0.01;
					}
				
					alSparseDists.add(sparsifyDistMatrix);
				}
				uniqueDiffs.add( Math.ceil(diff));
			}
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return alSparseDists;
	}

	public List<double[][]> SparsifyDocDisSimilarityMatrixAlgorithomicExactIterative(
			double[][] docSimMatrix, int numberofclusters) {
		
		List<double[][]> alSparseDists = new ArrayList<double[][]>();
		
		try{
			System.out.println("Start SparsifyDocDisSimilarityMatrixAlgorithomicExact");
			
			int itemsPerCluster = docSimMatrix.length/numberofclusters;
			int itemsToKeep = (itemsPerCluster*docSimMatrix.length-docSimMatrix.length)/1;
			
			List<Alpha> alAlpha = SparsificationUtilHelper.PrepareAlphaValuesDiagonal(docSimMatrix, false);
			
			List<Alpha> AlAlphaSublist = null;
			
			boolean isGoodAvg = false;
			double alphaFactor = 1;
			HashSet<Double> uniqueDiffs = new  HashSet<Double>();
			
			int whileCount = 0;

			while(!isGoodAvg){
				
				double [][] sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);
				
				for(int i=0;i<docSimMatrix.length;i++){
					sparsifyDistMatrix[i][i] = 0;
				}
				
				AlAlphaSublist = alAlpha.subList(0, (int)(itemsToKeep*alphaFactor));
				
				System.out.println("populate dist matrix, AlAlphaSublist="+AlAlphaSublist.size());
				
				sparsifyDistMatrix = SparsificationUtilHelper.PrepareSparsifiedMatrix(docSimMatrix, AlAlphaSublist);
				
				double avgAvgCount = SparsificationUtilHelper.ComputeAverageSparsifyCount(sparsifyDistMatrix);
				
				double diff = Math.abs(avgAvgCount-(double)itemsPerCluster);
				
				System.out.println("sparsified avg count="+avgAvgCount+", alphaFactor="+alphaFactor+", diff="+diff);

				UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedical-sparse-gtm-alpha-20000-"+whileCount, sparsifyDistMatrix, " ");
				
				if(SparsificationUtilHelper.IsEnd(diff, uniqueDiffs, alphaFactor)){
					isGoodAvg = true;
				}else{
					
					if(avgAvgCount>(double)itemsPerCluster){
							alphaFactor=alphaFactor-0.05;
					}else if(avgAvgCount<(double)itemsPerCluster){
							alphaFactor=alphaFactor+0.01;
					}
					//alSparseDists.add(sparsifyDistMatrix);
				}

				whileCount++;				
				uniqueDiffs.add( Math.ceil(diff));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return alSparseDists;
	}
	
}
