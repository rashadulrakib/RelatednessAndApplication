package dal.clustering.document.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.textsim.textrt.proc.singlethread.TextInstance;

import dal.utils.common.general.UtilsShared;

public class DocClusterUtilTrWpParallel {
	
	public double[][] ComputeDocumentSimMatrixTrWpParallel(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilTrWP docClusterUtilTrWP, int totalThreads){
		double[][] docSimMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		
		try{
			
			//docClusterUtilTrWP.docClusterUtilGTM.textRelatednessGtm.tpp.createSingleTextInstance("").d
			ArrayList<TextInstance> altxtInsts = GetGTMTextInstances(alDocLabelFlat, docClusterUtilTrWP);
			
			
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
						 new PartialDocSimMatrixCalculatorTrWp(altxtInsts,docClusterUtilTrWP, startIndex, endIndex, t));
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

	private ArrayList<TextInstance> GetGTMTextInstances(ArrayList<String[]> alDocLabelFlat, DocClusterUtilTrWP docClusterUtilTrWP) {
		ArrayList<TextInstance> al = new ArrayList<TextInstance>();
		try{
			for(String [] docLabel: alDocLabelFlat){
				String text = docLabel[0];
				TextInstance ti = docClusterUtilTrWP.docClusterUtilGTM.textRelatednessGtm.tpp.createSingleTextInstance(text).deepClone();
				al.add(ti);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return al;
	}
}

class PartialDocSimMatrixCalculatorTrWp implements Callable<ArrayList<ArrayList<Double>>> {
    
	ArrayList<TextInstance> altxtInsts;
	DocClusterUtilTrWP docClusterUtilTrWP;
	int startDocIndex;
	int endDocIndex;
	int thId;
	
	public PartialDocSimMatrixCalculatorTrWp(ArrayList<TextInstance> altxtInsts,
			DocClusterUtilTrWP docClusterUtilTrWP, int startDocIndex, int endDocIndex, int thId){
		
		this.altxtInsts = altxtInsts;
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
    		TextInstance text1 = altxtInsts.get(i);
			
			ArrayList<Double> simScores = new ArrayList<Double>();
			simScores.add(1.0);
			
			for(int j=i+1;j<altxtInsts.size();j++){
				
				TextInstance text2 = altxtInsts.get(j);
				
				double score = docClusterUtilTrWP.ComputeTextSimTrWp(text1, text2);
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


