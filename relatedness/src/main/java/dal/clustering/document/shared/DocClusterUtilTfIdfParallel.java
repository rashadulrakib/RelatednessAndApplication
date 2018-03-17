package dal.clustering.document.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.UtilsShared;

public class DocClusterUtilTfIdfParallel {
	public double[][] ComputeDocumentSimMatrixTfIdfParallel(ArrayList<HashMap<String, Double>> docsTfIdfs, int totalThreads){
		
		double[][] docSimMatrix = UtilsShared.InitializeMatrix(docsTfIdfs.size(), docsTfIdfs.size());
	
		try{
			 ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
			 List <Future<ArrayList<ArrayList<Double>>>> list = new ArrayList<Future<ArrayList<ArrayList<Double>>>>();
			 
			 int itemsPerThread = docsTfIdfs.size()/totalThreads;
			 
			 for(int t=0;t<totalThreads;t++){
				 
				 int startIndex = t*itemsPerThread;
				 int endIndex = (startIndex+itemsPerThread-1)>=docsTfIdfs.size()? 
						 docsTfIdfs.size()-1: startIndex+itemsPerThread-1;
				
				System.out.println("startIndex="+startIndex+","+ " endIndex="+endIndex);
				
				 if(startIndex>=endIndex || startIndex<0 || endIndex<0) break;
						 
				 Future<ArrayList<ArrayList<Double>>> future = executor.submit(
						 new PartialDocSimMatrixCalculatorTfIdf(docsTfIdfs, startIndex, endIndex, t));
	             list.add(future);
			 }
			 
			 for(Future<ArrayList<ArrayList<Double>>> fut : list){
	        	 ArrayList<ArrayList<Double>> listSims = fut.get();
	        	 System.out.println("listSims="+listSims.size());
	        	 for(ArrayList<Double> alSims: listSims){
	        		 //System.out.println("alSims="+alSims.size()+"="+alSims);
	        		 int stIndex = docsTfIdfs.size()-alSims.size();
	        		 int enIndex = docsTfIdfs.size()-1;
	        		 //System.out.println("st="+stIndex+", en="+ enIndex);
	        		 for(int i=stIndex;i<=enIndex;i++){
	        			 double val = alSims.get(i-stIndex);
	        			 docSimMatrix[stIndex][i]= val;
	        			 docSimMatrix[i][stIndex]= val;
	        		 }
	        		 
//	        		 for(int i=0;i<=enIndex;i++){
//	        			 System.out.print(docSimMatrix[stIndex][i]+" ");
//	        		 }
//	        		 System.out.println();
	        	 }
	        	 
	        	 
	         }
	         
	         executor.shutdown();
			 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
}


class PartialDocSimMatrixCalculatorTfIdf implements Callable<ArrayList<ArrayList<Double>>> {
    
	ArrayList<HashMap<String, Double>> docsTfIdfs;
	int startDocIndex;
	int endDocIndex;
	int thId;
	
	public PartialDocSimMatrixCalculatorTfIdf(ArrayList<HashMap<String, Double>> docsTfIdfs,
			 int startDocIndex, int endDocIndex, int thId){
		
		this.docsTfIdfs = docsTfIdfs;
		this.startDocIndex = startDocIndex;
		this.endDocIndex = endDocIndex;
		this.thId = thId;
	}
	
	@Override
    public ArrayList<ArrayList<Double>> call() throws Exception {
    	ArrayList<ArrayList<Double>> listSimScores = new ArrayList<ArrayList<Double>>();
    	
    	int zeroCount =0;
    	
    	for(int i=startDocIndex; i<=endDocIndex;i++){
    		System.out.println("doc="+i+", thid="+thId);
			HashMap<String, Double> ftr1 = docsTfIdfs.get(i);
			
			ArrayList<Double> simScores = new ArrayList<Double>();
			simScores.add(1.0);
			
			for(int j=i+1;j<docsTfIdfs.size();j++){
				
				HashMap<String, Double> ftr2 = docsTfIdfs.get(j);
				
				double score = ComputeUtil.ComputeCosineSimilarity(ftr1, ftr2);
				if(Double.isNaN(score)){
					score = 0;
				}
				if(score==0) zeroCount++;
				simScores.add(score);
			}
			
			listSimScores.add(simScores);
    	}
    	
    	System.out.println("Zeroa="+zeroCount+", thid="+thId);
    	
        return listSimScores;
    }
}