package dal.clustering.document.shared.sparsification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.UtilsShared;

public class SparsificationUtil {
			
	public double[][] SparsifyDocDisSimilarityMatrixAlgorithomic(double[][] docSimMatrix, int numberOfclusters, boolean isSparsify) {
		double[][] sparsifyDistMatrix = null;
		double[][] lastSparsifyDistMatrix = null;
		List<Alpha> alAlpha = new ArrayList<Alpha>();
		
		try{
			
			System.out.println("Start SparsifyDocDisSimilarityMatrixAlgorithomic");
			
			int itemsPerCluster = docSimMatrix.length/numberOfclusters;
			int itemsToKeep = itemsPerCluster*docSimMatrix.length-docSimMatrix.length;
			//int itemsToKeep = itemsPerCluster*docSimMatrix.length;
			
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
			
			
			System.out.println("Sorting alpha values");
			
			Collections.sort(alAlpha, new Comparator<Alpha>() {
			    @Override
			    public int compare(Alpha o1, Alpha o2) {
			        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
			    	// return lhs.customInt > rhs.customInt ? -1 : (lhs.customInt < rhs.customInt) ? 1 : 0;
			        return o1.getValue().compareTo(o2.getValue());
			    }
			});
			
			System.out.println("end Sorting alpha values");
			
			List<Alpha> AlAlphaSublist = null;
			if(isSparsify){
				//
				
				boolean isGoodAvg = false;
				double alphaFactor = 1.0;
				HashSet<Double> uniqueDiffs = new  HashSet<Double>();
				while(!isGoodAvg){
					
					sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);
					
					for(int i=0;i<docSimMatrix.length;i++){
						sparsifyDistMatrix[i][i] = 0;
					}
					
					AlAlphaSublist = alAlpha.subList(alAlpha.size()-(int)(itemsToKeep*alphaFactor), alAlpha.size());
					
					System.out.println("populate dist matrix");
					
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
					
					double sumVar = 0;
					for(double val: arrAvgCount){
						sumVar = sumVar + (val-avgAvgCount)*(val-avgAvgCount);
					}
					
					double sdAvgCount = Math.sqrt(sumVar/arrAvgCount.length);
					
					System.out.println("sparsified avg count="+avgAvgCount+", alphaFactor="+alphaFactor+", sdAvgCount="+sdAvgCount+", Math.abs(avgAvgCount-(double)itemsPerCluster)="+Math.abs(avgAvgCount-(double)itemsPerCluster));
					
					double diff = Math.abs(avgAvgCount-(double)itemsPerCluster);
					
					if(SparsificationUtilHelper.IsEnd(diff, uniqueDiffs)){
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
						lastSparsifyDistMatrix = sparsifyDistMatrix;
						sparsifyDistMatrix = null;
					}
					
//					if(avgAvgCount>(double)itemsPerCluster){
//						alphaFactor=alphaFactor-0.05;
//						sparsifyDistMatrix = null;
//					}else{
//						isGoodAvg = true;
//					}
					
					uniqueDiffs.add( Math.ceil(diff));
					
				}
				
			}else{
				
				sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);

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
				
				lastSparsifyDistMatrix = sparsifyDistMatrix;
			}
			
			for(int i=0;i<sparsifyDistMatrix.length;i++){
				sparsifyDistMatrix[i][i] = 0;
			}
			
			for(int i=0;i<lastSparsifyDistMatrix.length;i++){
				lastSparsifyDistMatrix[i][i] = 0;
			}
			
			System.out.println("mat cmp="+lastSparsifyDistMatrix.equals(sparsifyDistMatrix));
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//return sparsifyDistMatrix;
		return lastSparsifyDistMatrix;
	}
	
	public double[][] SparsifyDocDisSimilarityMatrixAlgorithomicExact(double[][] docSimMatrix, int numberOfclusters){
		double [][] sparsifyDistMatrix = null;
		
		try{
			System.out.println("Start SparsifyDocDisSimilarityMatrixAlgorithomicExact");
			
			int itemsPerCluster = docSimMatrix.length/numberOfclusters;
			int itemsToKeep = (itemsPerCluster*docSimMatrix.length-docSimMatrix.length)/1;
			
			List<Alpha> alAlpha = SparsificationUtilHelper.PrepareAlphaValuesDiagonal(docSimMatrix, false);
			
			List<Alpha> selectedAlpha = alAlpha.subList(0, itemsToKeep);
			
			System.out.println("alAlpha="+alAlpha.size()+", selectedAlpha="+selectedAlpha.size());
			
			sparsifyDistMatrix = SparsificationUtilHelper.PrepareSparsifiedMatrix(docSimMatrix, selectedAlpha);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifyDistMatrix;
	}
	
	public double[][] SparsifyDocDisSimilarityMatrixAlgorithomicAllUpperTriangleAlpha(double[][] docSimMatrix, int numberOfclusters){
		double [][] sparsifyDistMatrix = null;
		
		try{
			System.out.println("Start SparsifyDocDisSimilarityMatrixAlgorithomicAllUpperTriangleAlpha");
			
			int itemsPerCluster = docSimMatrix.length/numberOfclusters;
			int itemsToKeep = (itemsPerCluster*docSimMatrix.length-docSimMatrix.length)/1;
			
			List<Alpha> alAlpha = SparsificationUtilHelper.PrepareAlphaValuesAllUpperTriangle(docSimMatrix, false);
			
			List<Alpha> selectedAlpha = alAlpha.subList(0, itemsToKeep);
			
			System.out.println("alAlpha="+alAlpha.size()+", selectedAlpha="+selectedAlpha.size());
			
			sparsifyDistMatrix = SparsificationUtilHelper.PrepareSparsifiedMatrix(docSimMatrix, selectedAlpha);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifyDistMatrix;
	}
	
	public double [][] SparsifyDocDisSimilarityMatrixAlgorithomicClever(double[][] docSimMatrix, int numberOfclusters){

		double [][] sparsifyDistMatrix = null;
		double[][] lastSparsifyDistMatrix = null;
		try{
			
			System.out.println("Start SparsifyDocDisSimilarityMatrixAlgorithomicClever");
			
			int itemsPerCluster = docSimMatrix.length/numberOfclusters;
			int itemsToKeep = itemsPerCluster*docSimMatrix.length-docSimMatrix.length;
			
			List<Alpha> alAlpha = SparsificationUtilHelper.PrepareAlphaValues(docSimMatrix, false);
		
			boolean isGoodAvg = false;
			double alphaFactor = 1.0;
			HashSet<Double> uniqueDiffs = new  HashSet<Double>();
			
			//while(!isGoodAvg)
			{
				
				sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);	
			
				for(int i=0;i<sparsifyDistMatrix.length;i++){
					sparsifyDistMatrix[i][i]= 0;
				}
				
				List<Alpha> alAlphaSelected = SelectAlphaValues(alAlpha, itemsToKeep);
				
				sparsifyDistMatrix = SparsificationUtilHelper.PrepareSparsifiedMatrix(docSimMatrix, alAlphaSelected);
				
				lastSparsifyDistMatrix = sparsifyDistMatrix;
				
				double sumAvgCount = 0;
				//double arrAvgCount [] = new double [sparsifyDistMatrix.length];
				for(int i=0;i<sparsifyDistMatrix.length;i++){
					int count =0;
					for(int j=0;j<sparsifyDistMatrix.length;j++){
						if(sparsifyDistMatrix[i][j]!=SparsificationConstant.LargeDistValue){
							count++;
						}
					}
					//arrAvgCount[i]= count;
					sumAvgCount=sumAvgCount+count;
					//System.out.println("sparsified count="+count);
				}
				
				double avgAvgCount = sumAvgCount/sparsifyDistMatrix.length;
				
				double diff = Math.abs(avgAvgCount-(double)itemsPerCluster);
				
				System.out.println("sparsified avg count="+avgAvgCount+", alphaFactor="+alphaFactor+", Math.abs(avgAvgCount-(double)itemsPerCluster)="+diff);
				
				if(SparsificationUtilHelper.IsEnd(diff, uniqueDiffs)){
					isGoodAvg = true;
				}else{
					if(avgAvgCount>(double)itemsPerCluster){
							alphaFactor=alphaFactor-0.05;
					}else if(avgAvgCount<(double)itemsPerCluster){
							alphaFactor=alphaFactor+0.01;
					}
					lastSparsifyDistMatrix = sparsifyDistMatrix;
					sparsifyDistMatrix = null;
				}
				uniqueDiffs.add( Math.ceil(diff));
				
				itemsToKeep = (int)(itemsToKeep*alphaFactor);
			}
			
			for(int i=0;i<lastSparsifyDistMatrix.length;i++){
				lastSparsifyDistMatrix[i][i]=0;
			}
			
			System.out.println("End SparsifyDocDisSimilarityMatrixAlgorithomicClever");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//return sparsifyDistMatrix;
		return lastSparsifyDistMatrix;
	}
	
	private List<Alpha> SelectAlphaValues(List<Alpha> alAlpha, int itemsToKeep) {
		List<Alpha> alAlphaSelected = new ArrayList<Alpha>();
		
		try{
			LinkedHashMap<String, Integer> hmCellCount = new LinkedHashMap<String, Integer>();
			
			for(int i=0;i<alAlpha.size();i++){
				Alpha ap = alAlpha.get(i);
				
				int row = ap.getRow();
				int col = ap.getCol();
				
				if(row == col){
					System.out.println("bad alpha row==col"+row);
				}
				
				if(row> col){
					int temp = row;
					row = col;
					col = temp;
				}
				
				String celInd = row+","+col;
				
				if(hmCellCount.containsKey(celInd)){
					hmCellCount.put(celInd, hmCellCount.get(celInd)+1);
				}else{
					hmCellCount.put(celInd, 1);
				}
				
				if(hmCellCount.get(celInd)==2)
				{
					alAlphaSelected.add(ap);
				}
				else if(hmCellCount.get(celInd)>2){
					System.out.println("celInd="+celInd+","+hmCellCount.get(celInd));
				}
				
				if(alAlphaSelected.size()>=itemsToKeep*2){
					break;
				}
			}
			
			System.out.println("alAlphaSelected="+alAlphaSelected.size()+ ", itemsToKeep="+itemsToKeep*2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return alAlphaSelected;
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
				
				//System.out.println("total="+simArr.length+", zero count="+ zeroCount+",Wval="+Wval+",Pval="+Pval);
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
	
	public double[][] SparsifyDocDisSimilarityMatrixByCenterVectorTfIdf(HashMap<String, Double> hmCenterVecTfIdf,
			ArrayList<HashMap<String, Double>> docsTfIdfs) {
		
		double[][] sparsifyDistMatrix = UtilsShared.InitializeMatrix(docsTfIdfs.size(), docsTfIdfs.size());
		
		try{
			for(int i=0; i<docsTfIdfs.size();i++){
				
				HashMap<String, Double> inst1 = docsTfIdfs.get(i);
				sparsifyDistMatrix[i][i]=0;
				
				double sim1ToCenter = ComputeUtil.ComputeCosineSimilarity(inst1, hmCenterVecTfIdf);
				
				System.out.println("doc="+i);
				
				for(int j=i+1;j<docsTfIdfs.size();j++){
					
					HashMap<String, Double> inst2 = docsTfIdfs.get(j);
					
					double sim2ToCenter = ComputeUtil.ComputeCosineSimilarity(inst2, hmCenterVecTfIdf);
					double sim1To2 = ComputeUtil.ComputeCosineSimilarity(inst1, inst2);
					
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
	
	public double[][] SparsifyDocDisSimilarityMatrixByCenterVectorTfIdf(
			HashMap<String, Double> hmCenterVecTfIdf,
			ArrayList<HashMap<String, Double>> docsTfIdfs,
			double[][] docSimMatrix, double[] centTodocs) {
		
		double[][] sparsifyDistMatrix = UtilsShared.InitializeMatrix(docsTfIdfs.size(), docsTfIdfs.size());
		
		try{
			for(int i=0; i<docsTfIdfs.size();i++){
				
				//HashMap<String, Double> inst1 = docsTfIdfs.get(i);
				sparsifyDistMatrix[i][i]=0;
				
				double sim1ToCenter = centTodocs[i];
				
				System.out.println("doc="+i);
				
				for(int j=i+1;j<docsTfIdfs.size();j++){
					
					//HashMap<String, Double> inst2 = docsTfIdfs.get(j);
					
					double sim2ToCenter = centTodocs[j];
					double sim1To2 = docSimMatrix[i][j];
					
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
	
	public double[][] SparsifyDocDisSimilarityMatrixFixedNbyKSimilarities(
			double[][] docSimMatrix, int numberofclusters) {
		
		System.out.println("Start SparsifyDocDisSimilarityMatrixFixedNbyKSimilarities " + new Date().toLocaleString());
		
		double[][] sparsifyDistMatrix = null;
		//double[][] lastsparsifyDistMatrix = null;
		int numberOfItemsPerCluster = 2*(docSimMatrix.length/numberofclusters-1);
		//int constNumberOfItemsPerCluster =numberOfItemsPerCluster; 
		//int totalItemsToBeSparsified = numberOfItemsPerCluster*docSimMatrix.length;
		
		try{
			
			//boolean isGoodTotalSparsified = false;
			//double alphaFactor = 0.5;
			
			//HashSet<Double> uniqueDiffs = new  HashSet<Double>();
			
			List<Alpha> allalkeptAlpha = new ArrayList<Alpha>();
			
			
			{
				
			
				//isGoodTotalSparsified = true;
				//sparsifyDistMatrix = UtilsShared.CopyMatrix(docSimMatrix, true);
				sparsifyDistMatrix = UtilsShared.InitializeMatrix(docSimMatrix.length, docSimMatrix.length, SparsificationConstant.LargeDistValue);
				
				//HashMap<String, Integer> hmCellCount= new HashMap<String, Integer>();
				
				
				
				for(int i=0;i<docSimMatrix.length;i++){
					
					List<Alpha> alAlpha = new ArrayList<Alpha>();
					
					for(int j=0;j<docSimMatrix.length;j++){
						if(j==i) continue;
						Alpha alpha = new Alpha(docSimMatrix[i][j], i, j);
						alAlpha.add(alpha);
					}
					
					Collections.sort(alAlpha, new Comparator<Alpha>() {
					    @Override
					    public int compare(Alpha o1, Alpha o2) {
					        return o2.getValue().compareTo(o1.getValue());
					    }
					});
					
					//List<Alpha> alAlphaSublist = alAlpha.subList(0, numberOfItemsPerCluster);
					allalkeptAlpha.addAll(alAlpha.subList(0, numberOfItemsPerCluster));
					
//					for(Alpha alp: alAlphaSublist){
//						int minInd = alp.getRow();
//						int maxInd = alp.getCol();
//						
//						if(minInd> maxInd){
//							int temp = minInd;
//							minInd = maxInd;
//							maxInd = temp;
//						}
//						
//						String cellIndex = minInd+","+maxInd;
//						
//						if(hmCellCount.containsKey(cellIndex)){
//							hmCellCount.put(cellIndex, hmCellCount.get(cellIndex)+1);
//						}else{
//							hmCellCount.put(cellIndex,1);
//						}
//					}
					
					//alAlpha.clear();
					//alAlpha=null;
				}
				
				//int totalRealSparsed = 0;
				
//				for(String cellIndex: hmCellCount.keySet()){
//					if(hmCellCount.get(cellIndex)==2)
//					{
//						int row = Integer.parseInt(cellIndex.split(",")[0]);
//						int col = Integer.parseInt(cellIndex.split(",")[1]);
//						
////						sparsifyDistMatrix[row][col] = 0;
////						sparsifyDistMatrix[col][row] = 0;
//						
//						sparsifyDistMatrix[row][col] = (1-docSimMatrix[row][col])*SparsificationConstant.SimMultipleConstant;
//						sparsifyDistMatrix[col][row] = (1-docSimMatrix[col][row])*SparsificationConstant.SimMultipleConstant;
//						
//						totalRealSparsed++;
//					}
//				}
				
				
				
				//not used
//				if(totalRealSparsed>=totalItemsToBeSparsified){
//					isGoodTotalSparsified= true;
//				}else{
//					numberOfItemsPerCluster=constNumberOfItemsPerCluster+ (int)(constNumberOfItemsPerCluster*alphaFactor);
//					alphaFactor=alphaFactor+ alphaFactor;
//					sparsifyDistMatrix = null;
//				}
				//not used
				
//				double diff = Math.ceil(Math.abs(totalRealSparsed- totalItemsToBeSparsified));
//				
//				System.out.println("total keycell="+hmCellCount.size()+", totalRealSparsed="+totalRealSparsed
//						+", totalItemsToBeSparsified="+totalItemsToBeSparsified+" ,alphaFactor="+alphaFactor+", diff="+diff);
//				
//				if(SparsificationUtilHelper.IsEndNbyK(diff, uniqueDiffs, alphaFactor)){
//					isGoodTotalSparsified= true;
//				}else{
//					
//					if(totalRealSparsed<totalItemsToBeSparsified){
//						alphaFactor=alphaFactor + 0.5;
//						numberOfItemsPerCluster=constNumberOfItemsPerCluster+ (int)(constNumberOfItemsPerCluster*alphaFactor);
//					}else{
//						alphaFactor=alphaFactor - 0.2 ;
//						numberOfItemsPerCluster=constNumberOfItemsPerCluster+ (int)(constNumberOfItemsPerCluster*alphaFactor);
//					}
//					
//					lastsparsifyDistMatrix = sparsifyDistMatrix;
//					sparsifyDistMatrix = null;
//				}
//				
//				hmCellCount.clear();
//				hmCellCount=null;
//				
//				uniqueDiffs.add(diff);
			}
			
			HashSet<String> distinctIJ = new HashSet<String>();
			
			for(Alpha alph :allalkeptAlpha){
				int row = alph.getRow();
				int col = alph.getCol();
				
				int minInd = row;
				int maxInd = col;
				
				if(minInd> maxInd){
					int temp = minInd;
					minInd = maxInd;
					maxInd = temp;
				}
				
				String cellIndex = minInd+","+maxInd;
				distinctIJ.add(cellIndex);
				
				sparsifyDistMatrix[row][col] = 1-docSimMatrix[row][col];
				sparsifyDistMatrix[col][row] = 1-docSimMatrix[col][row];
			}
			
			System.out.println("distinctIJ="+distinctIJ.size()+", allalkeptAlpha="+allalkeptAlpha.size());
			
			for(int i=0;i<sparsifyDistMatrix.length;i++){
				sparsifyDistMatrix[i][i] = 0;
			}
			
			System.out.println("End SparsifyDocDisSimilarityMatrixFixedNbyKSimilarities " + new Date().toLocaleString());
			
//			for(int i=0;i<lastsparsifyDistMatrix.length;i++){
//				lastsparsifyDistMatrix[i][i] = 0;
//			}
			
			//System.out.println("mat cmp="+lastsparsifyDistMatrix.equals(sparsifyDistMatrix));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifyDistMatrix;
		//return lastsparsifyDistMatrix;
	}

}
