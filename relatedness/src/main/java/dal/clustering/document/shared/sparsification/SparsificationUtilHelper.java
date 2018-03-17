package dal.clustering.document.shared.sparsification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import dal.utils.common.general.UtilsShared;

public class SparsificationUtilHelper {

	public static boolean IsEnd(double diff, HashSet<Double> uniqueDiffs) {
		try{
			if((diff>=0 && diff<=5) || uniqueDiffs.contains(diff)){
				return true;
			}
			
			for(double val: uniqueDiffs){
				if(diff>=val){
					return true;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean IsEndNbyK(double diff, HashSet<Double> uniqueDiffs,
			double alphaFactor) {
		
		if(alphaFactor<0) return true;
		
		if(uniqueDiffs.contains(diff)){
			return true;
		}
		
		for(double val: uniqueDiffs){
			if(diff>=val){
				return true;
			}
		}
		
		return false;
	}

	public static boolean IsEnd(double diff, HashSet<Double> uniqueDiffs, double alphaFactor) {
		
		if(alphaFactor<=0) return false;
		
		return IsEnd(diff, uniqueDiffs);
	}
	
	public static List<Alpha> PrepareAlphaValues(double[][] docSimMatrix, boolean sortOrderAsc) {
		
		List<Alpha> sortedAlphaValues = new ArrayList<Alpha>();
		
		try{
			int mulConst = 1;
			
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
					sortedAlphaValues.add(objAlpha);
				}
			}
			
			System.out.println("Sorting alpha values");
			
			if(sortOrderAsc){
				Collections.sort(sortedAlphaValues, new Comparator<Alpha>() {
				    @Override
				    public int compare(Alpha o1, Alpha o2) { 
				        return o1.getValue().compareTo(o2.getValue());
				    }
				});
			}else{
				Collections.sort(sortedAlphaValues, new Comparator<Alpha>() {
				    @Override
				    public int compare(Alpha o1, Alpha o2) {
				        return o2.getValue().compareTo(o1.getValue());
				    }
				});
			}
			
			System.out.println("end Sorting alpha values");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sortedAlphaValues;
	}
	
	public static List<Alpha> PrepareAlphaValuesDiagonal(double[][] docSimMatrix, boolean sortOrderAsc) {
		
		List<Alpha> sortedAlphaValues = new ArrayList<Alpha>();
		
		try{
			int mulConst = 1;
			
			///			
			double [][] rijMatrix = new double[docSimMatrix.length][];
			
			for(int i=0;i<docSimMatrix.length;i++){
				double simArr [] = docSimMatrix[i];
				
				rijMatrix[i] = new double[docSimMatrix.length];
				
				double simSim = 0.0;
				for(int j=0;j<simArr.length;j++){
					if(j==i) continue;
					simSim= simSim + simArr[j]*mulConst;
				}
				
				double avgSimSum = simSim / simArr.length;
				
				double varianceSum = 0;
				
				for(int j=0;j<simArr.length;j++){
					if(j==i) continue;
					varianceSum = varianceSum + (simArr[j]*mulConst-avgSimSum)*(simArr[j]*mulConst-avgSimSum);
				}
				
				double sd = Math.sqrt(varianceSum/simArr.length);
				
				for(int j=0;j<simArr.length;j++){
					if(j==i) continue;
					
					double alphaVal = (simArr[j]-avgSimSum)/sd;
					//Alpha objAlpha = new Alpha(alphaVal, i, j);
					rijMatrix[i][j] = alphaVal;
				}
			}
			
			///
			
			
			
			for(int i=0;i<docSimMatrix.length;i++){
				
				/*double simArr [] = docSimMatrix[i];
				
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
				
				
				for(int j=i+1;j<simArr.length;j++)
				//for(int j=0;j<simArr.length;j++)
				{
					//if(j==i) continue;
					double alphaVal = (simArr[j]-avgSimSum)/sd;
					Alpha objAlpha = new Alpha(alphaVal, i, j);
					sortedAlphaValues.add(objAlpha);
				}*/
				
				for(int j=i+1;j<docSimMatrix.length;j++){
					if(rijMatrix[i][j]>=rijMatrix[j][i]){
						Alpha objAlpha = new Alpha(rijMatrix[i][j], i, j);
						sortedAlphaValues.add(objAlpha);
					}else{
						Alpha objAlpha = new Alpha(rijMatrix[j][i], j, i);
						sortedAlphaValues.add(objAlpha);
					}
				}					
			}
			
			System.out.println("Sorting alpha values");
			
			if(sortOrderAsc){
				Collections.sort(sortedAlphaValues, new Comparator<Alpha>() {
				    @Override
				    public int compare(Alpha o1, Alpha o2) { 
				        return o1.getValue().compareTo(o2.getValue());
				    }
				});
			}else{
				Collections.sort(sortedAlphaValues, new Comparator<Alpha>() {
				    @Override
				    public int compare(Alpha o1, Alpha o2) {
				        return o2.getValue().compareTo(o1.getValue());
				    }
				});
			}
			
			System.out.println("end Sorting alpha values");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sortedAlphaValues;
	}
	
	public static List<Alpha> PrepareAlphaValuesAllUpperTriangle(double[][] docSimMatrix, boolean sortOrderAsc) {
		
		List<Alpha> sortedAlphaValues = new ArrayList<Alpha>();
		
		try{
		
			int totalElements = 0;
			
			double simSim = 0.0;
			
			for(int i=0;i<docSimMatrix.length;i++){
				for(int j=i+1;j<docSimMatrix.length;j++){
					simSim = simSim + docSimMatrix[i][j];
					totalElements ++;
				}
			}
			
			double avgSimSum = simSim/ totalElements;
			double varianceSum = 0;
		
			for(int i=0;i<docSimMatrix.length;i++){
				for(int j=i+1;j<docSimMatrix.length;j++){
					varianceSum = varianceSum + Math.pow(docSimMatrix[i][j]-avgSimSum, 2);
				}
			}
			
			
			double sd = Math.sqrt(varianceSum/totalElements);
			
			for(int i=0;i<docSimMatrix.length;i++){
				for(int j=i+1;j<docSimMatrix.length;j++){
					double sim = docSimMatrix[i][j];
					double alphaVal = (sim-avgSimSum)/sd;
					Alpha objAlpha = new Alpha(alphaVal, i, j);
					sortedAlphaValues.add(objAlpha);
				}
			}
			
			System.out.println("Sorting alpha values");
			
			if(sortOrderAsc){
				Collections.sort(sortedAlphaValues, new Comparator<Alpha>() {
				    @Override
				    public int compare(Alpha o1, Alpha o2) { 
				        return o1.getValue().compareTo(o2.getValue());
				    }
				});
			}else{
				Collections.sort(sortedAlphaValues, new Comparator<Alpha>() {
				    @Override
				    public int compare(Alpha o1, Alpha o2) {
				        return o2.getValue().compareTo(o1.getValue());
				    }
				});
			}
			
			System.out.println("end Sorting alpha values");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sortedAlphaValues;
	}
	
	public static double[][] PrepareSparsifiedMatrix(double[][] docSimMatrix,
			List<Alpha> alAlphaSelected) {
		double [][] sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);
		
		try{
			for(Alpha ap: alAlphaSelected){
				int row = ap.getRow();
				int col = ap.getCol();
				
				sparsifyDistMatrix[row][col] = 1-docSimMatrix[row][col];
				sparsifyDistMatrix[col][row] = 1-docSimMatrix[row][col];
			}
			
			for(int i=0;i<sparsifyDistMatrix.length;i++){
				sparsifyDistMatrix[i][i]=0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifyDistMatrix;
	}

	public static double ComputeAverageSparsifyCount(double[][] sparsifyDistMatrix) {
		double sumAvgCount = 0;
		
		try{
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
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sumAvgCount/sparsifyDistMatrix.length;
	}
}
