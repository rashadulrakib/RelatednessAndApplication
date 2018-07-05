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

public class DocClusterUtilW2VecNoCommonWordParallel {
	
	public double[][] ComputeDocumentSimMatrixW2VecParallel(ArrayList<String[]> alDocLabelFlat, int totalThreads,
			HashMap<String, double[]> hmW2Vec, DocClusterUtil docClusterUtil) {
		double[][] docSimMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		try{
			ExecutorService executor = Executors.newFixedThreadPool(totalThreads);
			List <Future<ArrayList<ArrayList<Double>>>> list = new ArrayList<Future<ArrayList<ArrayList<Double>>>>();
			 
			 int itemsPerThread = alDocLabelFlat.size()/totalThreads;
			 
			 for(int t=0;t<totalThreads;t++){
				 
				 int startIndex = t*itemsPerThread;
				 int endIndex = (startIndex+itemsPerThread-1)>=alDocLabelFlat.size()? 
						 alDocLabelFlat.size()-1: startIndex+itemsPerThread-1;
				
				System.out.println("startIndex="+startIndex+","+ " endIndex="+endIndex);
				
				 if(startIndex>=endIndex || startIndex<0 || endIndex<0) break;
						 
				 Future<ArrayList<ArrayList<Double>>> future = executor.submit(
						 new PartialDocSimMatrixCalculatorW2VecNoCommonWord(alDocLabelFlat, startIndex, endIndex, t,
								 docClusterUtil, hmW2Vec));
	             list.add(future);
			 }
			 
			 for(Future<ArrayList<ArrayList<Double>>> fut : list){
	        	 ArrayList<ArrayList<Double>> listSims = fut.get();
	        	 System.out.println("listSims="+listSims.size());
	        	 for(ArrayList<Double> alSims: listSims){
	        		 //System.out.println("alSims="+alSims.size()+"="+alSims);
	        		 int stIndex = alDocLabelFlat.size()-alSims.size();
	        		 int enIndex = alDocLabelFlat.size()-1;
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


class PartialDocSimMatrixCalculatorW2VecNoCommonWord implements Callable<ArrayList<ArrayList<Double>>> {
	ArrayList<String[]> alDocLabelFlat;
	int startDocIndex;
	int endDocIndex;
	int thId;
	DocClusterUtil docClusterUtil;
	HashMap<String, double[]> hmW2Vec;
	
	public PartialDocSimMatrixCalculatorW2VecNoCommonWord(ArrayList<String[]> alDocLabelFlat,
			 int startDocIndex, int endDocIndex, int thId, DocClusterUtil docClusterUtil, 
			 HashMap<String, double[]> hmW2Vec){
		
		this.alDocLabelFlat = alDocLabelFlat;
		this.startDocIndex = startDocIndex;
		this.endDocIndex = endDocIndex;
		this.thId = thId;
		this.docClusterUtil = docClusterUtil;
		this.hmW2Vec = hmW2Vec;
	}
	
	@Override
    public ArrayList<ArrayList<Double>> call() throws Exception {
    	ArrayList<ArrayList<Double>> listSimScores = new ArrayList<ArrayList<Double>>();
    	
    	int zeroCount =0;
    	
    	for(int i=startDocIndex; i<=endDocIndex;i++){
    		System.out.println("doc="+i);
			String ftr1Tex = alDocLabelFlat.get(i)[0];
			
			ArrayList<Double> simScores = new ArrayList<Double>();
			simScores.add(1.0);
			
			for(int j=i+1;j<alDocLabelFlat.size();j++){
				
				String ftr2Tex = alDocLabelFlat.get(j)[0];
				
				String newftr1Tex = ftr1Tex;
				String newftr2Tex = ftr2Tex;
				
				String[] textsWithNoComonWords= docClusterUtil.textUtilShared.RemoveCommonWords(newftr1Tex, newftr2Tex);
				
				if(!textsWithNoComonWords[0].isEmpty() && !textsWithNoComonWords[1].isEmpty()){
					newftr1Tex = textsWithNoComonWords[0];
					newftr2Tex = textsWithNoComonWords[1];
				}
				
				double [] ftr1= docClusterUtil.PopulateW2VecForSingleDoc(newftr1Tex, hmW2Vec);  
				double [] ftr2= docClusterUtil.PopulateW2VecForSingleDoc(newftr2Tex, hmW2Vec);
				
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
