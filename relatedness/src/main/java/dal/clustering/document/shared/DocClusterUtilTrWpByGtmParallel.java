package dal.clustering.document.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dal.utils.common.general.UtilsShared;

public class DocClusterUtilTrWpByGtmParallel {

	public double[][] ComputeDocumentSimMatrixTrWpParallelByGtm(
			ArrayList<ArrayList<String>> wordsPhrasesEachText,
			DocClusterUtilTrWP docClusterUtilTrWP, int totalThreads) {
		
		double[][] docSimMatrix = UtilsShared.InitializeMatrix(wordsPhrasesEachText.size(), wordsPhrasesEachText.size());
		
		try{
			
			ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
			 List <Future<ArrayList<ArrayList<Double>>>> list = new ArrayList<Future<ArrayList<ArrayList<Double>>>>();
			 
			 int itemsPerThread = wordsPhrasesEachText.size()/totalThreads;
			 
			 for(int t=0;t<totalThreads;t++){
				 
				 int startIndex = t*itemsPerThread;
				 int endIndex = (startIndex+itemsPerThread-1)>=wordsPhrasesEachText.size()? 
						 wordsPhrasesEachText.size()-1: startIndex+itemsPerThread-1;
				
				System.out.println("startIndex="+startIndex+","+ " endIndex="+endIndex);
				
				 if(startIndex>=endIndex || startIndex<0 || endIndex<0) break;
						 
				 Future<ArrayList<ArrayList<Double>>> future = executor.submit(
						 new PartialDocSimMatrixCalculatorTrWpByGtm(wordsPhrasesEachText,docClusterUtilTrWP, startIndex, endIndex, t));
	             list.add(future);
			 }
			 
            
	         for(Future<ArrayList<ArrayList<Double>>> fut : list){
	        	 ArrayList<ArrayList<Double>> listSims = fut.get();
	        	 System.out.println("listSims="+listSims.size());
	        	 for(ArrayList<Double> alSims: listSims){
	        		 //System.out.println("alSims="+alSims.size()+"="+alSims);
	        		 int stIndex = wordsPhrasesEachText.size()-alSims.size();
	        		 int enIndex = wordsPhrasesEachText.size()-1;
	        		 //System.out.println("st="+stIndex+", en="+ enIndex);
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

class PartialDocSimMatrixCalculatorTrWpByGtm implements Callable<ArrayList<ArrayList<Double>>>{

	ArrayList<ArrayList<String>> wordsPhrasesEachText;
	DocClusterUtilTrWP docClusterUtilTrWP;
	int startDocIndex;
	int endDocIndex;
	int thId;
	
	public PartialDocSimMatrixCalculatorTrWpByGtm(
			ArrayList<ArrayList<String>> wordsPhrasesEachText,
			DocClusterUtilTrWP docClusterUtilTrWP, int startDocIndex,
			int endDocIndex, int thId) {
		
		this.wordsPhrasesEachText = wordsPhrasesEachText;
		this.docClusterUtilTrWP = docClusterUtilTrWP;
		this.startDocIndex = startDocIndex;
		this.endDocIndex = endDocIndex;
		this.thId = thId;
	}
	
	@Override
    public ArrayList<ArrayList<Double>> call() throws Exception {
    	ArrayList<ArrayList<Double>> listSimScores = new ArrayList<ArrayList<Double>>();
    	
    	int zeroCount =0;
    	
    	for(int i=startDocIndex; i<=endDocIndex;i++){
    		System.out.println("doc="+i+", thId="+thId);
    		ArrayList<String> wordsPhs1 = wordsPhrasesEachText.get(i);
			
			ArrayList<Double> simScores = new ArrayList<Double>();
			simScores.add(1.0);
			
			for(int j=i+1;j<wordsPhrasesEachText.size();j++){
				
				ArrayList<String> wordsPhs2 = wordsPhrasesEachText.get(j);
				
				double score = docClusterUtilTrWP.ComputeTextSimTrWp(wordsPhs1, wordsPhs2);
				if(Double.isNaN(score)){
					score = 0;
				}
				if(score==0) zeroCount++;
				simScores.add(score);
			}
			
			listSimScores.add(simScores);
    	}
    	
    	System.out.println("Zerocount="+zeroCount+", thid="+thId);
    	
        return listSimScores;
    }
}
