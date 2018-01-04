package dal.relatedness.phrase.compute.zerosimilarity;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;

import dal.relatedness.phrase.compute.tokenized.PhRelComputeUtil;
import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.utils.common.general.UtilsShared;

public class PhraseRelCountNonZeroSimilarity {

	private final int MaxContextSize4gm = 50000;
	PhRelComputeUtil phRelComputeUtil;
	
	public long CountNonZeroSimilarityPairs(){
		try{		
			
			phRelComputeUtil = new PhRelComputeUtil(null, null);
			
			//CountNonZeroSimPairs(PhraseDirectories.tokenizedFourGramDir+"z",
			//		PhraseDirectories.tokenizedFourGramDir+"z");
			
			CountNonZeroSimPairs(PhraseDirectories.tokenizedFourGramDir+"y");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return 0;
	}

	private void CountNonZeroSimPairs(String fileA) {
		try{
			ArrayList<int[]> alCtxRangeA = LoadCtxRange(fileA);
			
			long nonZeroCount = 0; 
			
			String stTime = new Date().toString();
			
			for(int i=0;i<alCtxRangeA.size();i++){
				for(int j=i+1;j<alCtxRangeA.size();j++){
					if(UtilsShared.AnyOverlapBetweenTwoRanges(
							alCtxRangeA.get(i)[1], alCtxRangeA.get(i)[2], 
							alCtxRangeA.get(j)[1], alCtxRangeA.get(j)[2])){
						
						
						
						nonZeroCount++;
					}
				}
			}
			
			System.out.println("alCtxRangeA.size()="+alCtxRangeA.size()+","+fileA+","+(long)alCtxRangeA.size()*(long)alCtxRangeA.size()+", nonZeroCount="+nonZeroCount+
					"stTime,"+stTime+" endTime,"+new Date().toString() );
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void CountNonZeroSimPairs(String fileA, String fileB) {
		try{
			ArrayList<int[]> alCtxRangeA = LoadCtxRange(fileA);
			ArrayList<int[]> alCtxRangeB = LoadCtxRange(fileB);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private ArrayList<int[]> LoadCtxRange(String file){
		ArrayList<int[]> alCtxRange = new ArrayList<int[]>();
		
		try{
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			
			long totalContexts = 0;	
			long lineCount = 0;
			long maxCtxSizeGlobal = 0;
			
			long skippedContxets = 0;
			
			while(in.available()>0){
				int phid = in.readInt();
				int contextSize = in.readInt();
				
				if(contextSize>=MaxContextSize4gm){
					skippedContxets++;
				}
				
				if(maxCtxSizeGlobal<contextSize){
					maxCtxSizeGlobal = contextSize;
				}
				
				lineCount++;
				
				totalContexts=totalContexts+contextSize;
				
				int maxCtxSize = contextSize>MaxContextSize4gm?MaxContextSize4gm:contextSize;
				
				int [] ctxIdFreqs = new  int[maxCtxSize*2];
				
				for(int i=0;i<contextSize;i++){
					int ctxId = in.readInt();
					int ctxFreq = in.readInt();
					
					if(i>=MaxContextSize4gm){
						continue;
					}
					
					ctxIdFreqs[i*2]=ctxId;
					ctxIdFreqs[i*2+1]=ctxFreq;
				}
								
				if(lineCount%1000000==0){
					System.out.println("lineCount="+lineCount);
				}
				
				alCtxRange.add(new int[]{phid, ctxIdFreqs[0],  ctxIdFreqs[ctxIdFreqs.length-2]});
				
				ctxIdFreqs = null;
				
			}
			
			System.out.println("totalContexts="+totalContexts+", avg context length="
				+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
				+", PhWithLargeContxets="+skippedContxets+", lineCount="+lineCount);
			
			in.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return alCtxRange;
	}
	
}
