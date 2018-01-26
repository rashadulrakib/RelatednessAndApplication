package dal.clustering.document.shared.sparsification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.UtilsShared;

public class SparsificationUtil {
	
	public SparsificationUtil(){
	}
	public double[][] SparsifyDocDisSimilarityMatrixAlgorithomic(double[][] docSimMatrix, int numberOfclusters, boolean isSparsify) {
		double[][] sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);
		List<Alpha> alAlpha = new ArrayList<Alpha>();
		
		try{
			
			int itemsPerCluster = docSimMatrix.length/numberOfclusters;
			//int itemsToKeep = itemsPerCluster*docSimMatrix.length-docSimMatrix.length;
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
			
			for(int i=0;i<docSimMatrix.length;i++){
				sparsifyDistMatrix[i][i] = 0;
			}
			
			List<Alpha> AlAlphaSublist = null;
			if(isSparsify){
				//
				
				boolean isGoodAvg = false;
				double alphaFactor = 1.0;
				while(!isGoodAvg){

					sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);
					
					for(int i=0;i<docSimMatrix.length;i++){
						sparsifyDistMatrix[i][i] = 0;
					}
					
					AlAlphaSublist = alAlpha.subList(alAlpha.size()-(int)(itemsToKeep*alphaFactor), alAlpha.size());
					
					for(Alpha alObj: AlAlphaSublist){
						int row = alObj.getRow();
						int col = alObj.getCol();
						
						if(docSimMatrix[row][col]!=docSimMatrix[col][row]){
							System.out.println("not same="+row+","+col);
						}
						
						sparsifyDistMatrix[row][col] = (1-docSimMatrix[row][col])*SparsificationConstant.SimMultipleConstant;
						sparsifyDistMatrix[col][row] = (1-docSimMatrix[col][row])*SparsificationConstant.SimMultipleConstant;
					}
					
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
					
					double sumVar = 0;
					for(double val: arrAvgCount){
						sumVar = sumVar + (val-avgAvgCount)*(val-avgAvgCount);
					}
					
					double sdAvgCount = Math.sqrt(sumVar/arrAvgCount.length);
					
					System.out.println("sparsified avg count="+avgAvgCount+", alphaFactor="+alphaFactor+", sdAvgCount="+sdAvgCount);
					
					//if(avgAvgCount>(double)itemsPerCluster && avgAvgCount-(double)itemsPerCluster>400){
					if(avgAvgCount>(double)itemsPerCluster){
						alphaFactor=alphaFactor-0.05;
					}else{
						isGoodAvg = true;
					}
				}
				
			}else{
				
				AlAlphaSublist = alAlpha;
				
				for(Alpha alObj: AlAlphaSublist){
					int row = alObj.getRow();
					int col = alObj.getCol();
					
					if(docSimMatrix[row][col]!=docSimMatrix[col][row]){
						System.out.println("not same="+row+","+col);
					}
					
					sparsifyDistMatrix[row][col] = (1-docSimMatrix[row][col])*SparsificationConstant.SimMultipleConstant;
					sparsifyDistMatrix[col][row] = (1-docSimMatrix[col][row])*SparsificationConstant.SimMultipleConstant;
				}
				
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
				
				System.out.println("sparsified avg count="+avgAvgCount);
			}
			
			//System.out.println("avg spars count="+ sumAvgCount/sparsifyDistMatrix.length);
			
//			double avgAvgCount = sumAvgCount/sparsifyDistMatrix.length;
//			
//			double sumVar = 0;
//			for(double val: arrAvgCount){
//				sumVar = sumVar + (val-avgAvgCount)*(val-avgAvgCount);
//			}
//			
//			double sdAvgCount = Math.sqrt(sumVar/arrAvgCount.length); 
//			
//			for(int i=0;i< arrAvgCount.length;i++){
//				if(arrAvgCount[i]> avgAvgCount+ sdAvgCount){
//					System.out.println("arrAvgCount[i]="+arrAvgCount[i]+",i="+i+", mean="+avgAvgCount+", sd="+ sdAvgCount);
//				}
//			}
			
			
			
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
	
	public double[][] SparsifyDocDisSimilarityMatrixByCenterVector(
			InstanceW2Vec centerVec, ArrayList<InstanceW2Vec> testW2Vecs) {
		
		double[][] sparsifyDistMatrix = UtilsShared.InitializeMatrix(testW2Vecs.size(), testW2Vecs.size());
		
		try{
			
			for(int i=0;i<testW2Vecs.size();i++){
				
				InstanceW2Vec inst1 = testW2Vecs.get(i);
				sparsifyDistMatrix[i][i]=0;
				
				double sim1ToCenter = ComputeUtil.ComputeCosineSimilarity(inst1.Features, centerVec.Features);
				
				System.out.println("doc="+i);
				
				for(int j=i+1;j<testW2Vecs.size();j++){
					
					InstanceW2Vec inst2 = testW2Vecs.get(j);
					
					double sim2ToCenter = ComputeUtil.ComputeCosineSimilarity(inst2.Features, centerVec.Features);
					double sim1To2 = ComputeUtil.ComputeCosineSimilarity(inst1.Features, inst2.Features);
					
					double sim = 0;
					if(sim1To2 > Math.max(sim1ToCenter, sim2ToCenter) ){
						sim = sim1To2;
					}
					
					sparsifyDistMatrix[i][j]= 1-sim;
					sparsifyDistMatrix[j][i]= 1-sim;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifyDistMatrix;
	}
	
}
