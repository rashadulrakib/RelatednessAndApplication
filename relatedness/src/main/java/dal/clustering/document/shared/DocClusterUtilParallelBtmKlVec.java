package dal.clustering.document.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.UtilsShared;

public class DocClusterUtilParallelBtmKlVec {

	public double[][] ComputeDocumentSimMatrixBtmKLVecParallel(ArrayList<InstanceW2Vec> testW2Vecs, int totalThreads) {
		double[][] docSimMatrix = UtilsShared.InitializeMatrix(testW2Vecs.size(), testW2Vecs.size());
		
		try{
			 ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
			 List <Future<ArrayList<ArrayList<Double>>>> list = new ArrayList<Future<ArrayList<ArrayList<Double>>>>();
			 
			 int itemsPerThread = testW2Vecs.size()/totalThreads;
			 
			 for(int t=0;t<totalThreads;t++){
				 
				 int startIndex = t*itemsPerThread;
				 int endIndex = (startIndex+itemsPerThread-1)>=testW2Vecs.size()? 
						 testW2Vecs.size()-1: startIndex+itemsPerThread-1;
				
				System.out.println("startIndex="+startIndex+","+ " endIndex="+endIndex);
				
				 if(startIndex>=endIndex || startIndex<0 || endIndex<0) break;
						 
				 Future<ArrayList<ArrayList<Double>>> future = executor.submit(
						 new PartialDocSimMatrixCalculatorBtmKLVec(testW2Vecs, startIndex, endIndex, t));
	             list.add(future);
			 }
			 
			 for(Future<ArrayList<ArrayList<Double>>> fut : list){
	        	 ArrayList<ArrayList<Double>> listSims = fut.get();
	        	 System.out.println("listSims="+listSims.size());
	        	 for(ArrayList<Double> alSims: listSims){
	        		 
	        		 int stIndex = testW2Vecs.size()-alSims.size();
	        		 int enIndex = testW2Vecs.size()-1;
	        		 
	        		 for(int i=stIndex;i<=enIndex;i++){
	        			 double val = alSims.get(i-stIndex);
	        			 docSimMatrix[stIndex][i]= val;
	        			 docSimMatrix[i][stIndex]= val;
	        		 }
	        	 }
	         }
	         
	         executor.shutdown();
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}

}

class PartialDocSimMatrixCalculatorBtmKLVec implements Callable<ArrayList<ArrayList<Double>>>{

	ArrayList<InstanceW2Vec> testW2Vecs;
	int startDocIndex;
	int endDocIndex;
	int thId;
	
	public PartialDocSimMatrixCalculatorBtmKLVec(ArrayList<InstanceW2Vec> testW2Vecs, int startDocIndex,
			int endDocIndex, int thId) {
		this.testW2Vecs = testW2Vecs;
		this.startDocIndex = startDocIndex;
		this.endDocIndex = endDocIndex;
		this.thId = thId;		
	}

	@Override
	public ArrayList<ArrayList<Double>> call() throws Exception {
		ArrayList<ArrayList<Double>> listSimScores = new ArrayList<ArrayList<Double>>();
		try{
			
			int zeroCount =0;
	    	
	    	for(int i=startDocIndex; i<=endDocIndex;i++){
	    		System.out.println("doc="+i);
				double [] ftr1 = testW2Vecs.get(i).Features;
				
				ArrayList<Double> simScores = new ArrayList<Double>();
				simScores.add(1.0);
				//simScores.add(0.0);
				
				for(int j=i+1;j<testW2Vecs.size();j++){
					
					double [] ftr2 = testW2Vecs.get(j).Features;
					
					double score = ComputeUtil.ComputeKLDivergenceSimilarity(ftr1, ftr2);
					if(Double.isNaN(score)){
						score = 0;
					}
					if(score==0) zeroCount++;
					simScores.add(score);
				}
				
				listSimScores.add(simScores);
	    	}
	    	
	    	System.out.println("Zero count="+zeroCount+", thid="+thId);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return listSimScores;
	}
	
}