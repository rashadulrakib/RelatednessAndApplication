package dal.clustering.document.shared.sparsification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dal.utils.common.general.UtilsShared;

public class SparsificationUtil {
	
	public SparsificationUtil(){
	}
	public double[][] SparsifyDocDisSimilarityMatrixAlgorithomic(double[][] docSimMatrix, int numberOfclusters) {
		double[][] sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);
		List<Alpha> alAlpha = new ArrayList<Alpha>();
		
		try{
			
			int itemsPerCluster = docSimMatrix.length/numberOfclusters;
			int itemsToKeep = itemsPerCluster*docSimMatrix.length;
			
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
					alAlpha.add(objAlpha);
				}
				
				
			}
			
			Collections.sort(alAlpha, new Comparator<Alpha>() {
			    @Override
			    public int compare(Alpha o1, Alpha o2) {
			        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
			    	// return lhs.customInt > rhs.customInt ? -1 : (lhs.customInt < rhs.customInt) ? 1 : 0;
			        return o1.getValue().compareTo(o2.getValue());
			    }
			});
			
			List<Alpha> AlAlphaSublist = alAlpha.subList(alAlpha.size()-itemsToKeep/3, alAlpha.size());
			//List<Alpha> AlAlphaSublist = alAlpha;
			
			for(int i=0;i<docSimMatrix.length;i++){
				sparsifyDistMatrix[i][i] = 0;
			}
			
			for(Alpha alObj: AlAlphaSublist){
				int row = alObj.getRow();
				int col = alObj.getCol();
				
				if(docSimMatrix[row][col]!=docSimMatrix[col][row]){
					System.out.println("not same="+row+","+col);
				}
				
				sparsifyDistMatrix[row][col] = (1-docSimMatrix[row][col])*SparsificationConstant.SimMultipleConstant;
				sparsifyDistMatrix[col][row] = (1-docSimMatrix[col][row])*SparsificationConstant.SimMultipleConstant;
			}
			
			double avgCount = 0;
			for(int i=0;i<sparsifyDistMatrix.length;i++){
				int count =0;
				for(int j=0;j<sparsifyDistMatrix.length;j++){
					if(sparsifyDistMatrix[i][j]!=SparsificationConstant.LargeDistValue){
						count++;
					}
				}
				avgCount=avgCount+count;
				System.out.println("sparsified count="+count);
			}
			
			System.out.println("avg spars count="+ avgCount/sparsifyDistMatrix.length);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifyDistMatrix;
	}
	
	public double[][] SparsifyDocDisSimilarityMatrix(double[][] docSimMatrix) {
		double[][] sparsifySimMatrix = new double[docSimMatrix.length][];
		
		try{
			int mulConst = 1;
			int pvalCount = 0;
			int WvalCount = 0;
			int bothCOunt =0;
			
			int pvalAndersonCount = 0;
			int wvalAndersonCount = 0;
			int bothAndersonCount = 0;
			
			for(int i=0;i<docSimMatrix.length;i++){
				double simArr [] = docSimMatrix[i];
				
				double simSim = 0.0;
				for(int j=0;j<simArr.length;j++){
					simSim= simSim + simArr[j]*mulConst;
				}
				
				double simArrTest [] = new double[simArr.length];
				ArrayList<Integer> alsimArrTest = new  ArrayList<Integer>();
				for(int j=0;j<simArr.length;j++){
					//simArrTest[j] = (int)(simArr[j]*mulConst);
					alsimArrTest.add((int)(simArr[j]*mulConst));
				}
				
				Collections.sort(alsimArrTest);
				
				for(int j=0;j<alsimArrTest.size();j++){
					simArrTest[j] = alsimArrTest.get(j);
				}
				
				//check normally distributed or not
				double Wval = 0;//NormalityTest.shapiro_wilk_statistic(simArrTest);
				double Pval = 0;//NormalityTest.shapiro_wilk_pvalue(Wval, simArr.length);
				
				
				double WvalAnderson = 0;//NormalityTest.anderson_darling_statistic(simArrTest);
				double PvalAnderson = 0;//NormalityTest.anderson_darling_pvalue(WvalAnderson, simArr.length);
				
				
				if(Pval>=0.05){
					pvalCount++;
				}
				if(Wval>=0.99){
					WvalCount++;
				}
				if(Pval>=0.05 && WvalCount>=0.99){
					bothCOunt++;
				}
				
				if(PvalAnderson>=0.05){
					pvalAndersonCount++;
				}
				if(WvalAnderson>=0.99){
					wvalAndersonCount++;
				}
				
				if(PvalAnderson>=0.05 && WvalAnderson>=0.99){
					bothAndersonCount++;
				}
				
				//
				
				double avgSimSum = simSim/ simArr.length;
				
				double varianceSum = 0;
				
				for(int j=0;j<simArr.length;j++){
					varianceSum = varianceSum + (simArr[j]*mulConst-avgSimSum)*(simArr[j]*mulConst-avgSimSum);
				}
				
				double sd = Math.sqrt(varianceSum/simArr.length);
				
				//filter sim
				int zeroCount = 0;
				for(int j=0;j<simArr.length;j++){
					
					//double newSim = simArr[j];
					//if(Pval>=0.5 || Wval>=0.99)
					
					//double newSim = simArr[j]*mulConst> avgSimSum*0.01+ sd*0 ? simArr[j]: 0; //0.7 best for stack
						//System.out.println("newSim="+newSim);
					
					//double newSim = simArr[j]*mulConst> avgSimSum+ sd*1.3 ? simArr[j]: 0; //0.7 best for stack
					//double newSim = simArr[j]*mulConst> avgSimSum- sd*0.0 ? Double.MAX_VALUE: simArr[j];
					 double newSim = simArr[j];
					 if( newSim==0){
						 zeroCount++;
					 }
					 simArr[j] = 1-newSim;
					 
					 //simArr[j] = newSim;
				}
				
				System.out.println("total="+simArr.length+", zero count="+ zeroCount+",Wval="+Wval+",Pval="+Pval);
				//System.out.println("total="+simArr.length+", zero count="+ zeroCount+",Wval="+Wval);
				sparsifySimMatrix[i] = simArr;
			}
			
			System.out.println("pcount="+ pvalCount+", wcount="+ WvalCount+", bothcount="+ bothCOunt+": pander="+  pvalAndersonCount+", wvalander="+ wvalAndersonCount+", bothAnder="+ bothAndersonCount);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifySimMatrix;
	}
}
