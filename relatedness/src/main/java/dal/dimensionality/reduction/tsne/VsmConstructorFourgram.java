package dal.dimensionality.reduction.tsne;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import dal.relatedness.phrase.constants.PhraseDirectories;

public class VsmConstructorFourgram {

	public List<byte[]> ConstructVsmFourGram(ArrayList<Integer> vsmSingleDim){
		
		List<byte[]> lstVsm4g = new ArrayList<byte[]>();
		
		try{
			
			for(char ch='a'; ch<='z';ch++){
				System.out.println(new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory());
				
				String fourGTokenizedFile = PhraseDirectories.tokenizedFourGramDir+String.valueOf(ch);
				
				System.out.println("load" + fourGTokenizedFile);
				
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fourGTokenizedFile)));
				
				long totalContexts = 0;	
				long lineCount = 0;
				long maxCtxSizeGlobal = 0;
				
				long skippedContxets = 0;
				
				while(in.available()>0){
					int phid = in.readInt();
					int contextSize = in.readInt();
					
					lineCount++;
					
					byte[] sparseVector = new byte[vsmSingleDim.size()]; 
					
					if(contextSize>=DimReductionConstants.MaxContextSize4gm){
						skippedContxets++;
					}
					
					if(maxCtxSizeGlobal<contextSize){
						maxCtxSizeGlobal = contextSize;
					}
					
					totalContexts=totalContexts+contextSize;
							
					for(int i=0;i<contextSize;i++){
						int ctxId = in.readInt();
						int ctxFreq = in.readInt();
						
						if(i>=DimReductionConstants.MaxContextSize4gm){
							continue;
						}
						
						int ctxIdIndexinSingleVsm = Collections.binarySearch(vsmSingleDim, ctxId);
						if(ctxIdIndexinSingleVsm>=0){
							sparseVector[ctxIdIndexinSingleVsm] = 1;
						}
					}
					
					lstVsm4g.add(sparseVector);
					
					if(lineCount%1000000==0){
						System.out.println("lineCount="+lineCount);
					}
						
					
				}
				
				System.out.println("totalContexts="+totalContexts+", avg context length="
					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
					+", PhWithLargeContxets="+skippedContxets+", lineCount="+lineCount);
				
				in.close();
				
			}
			
			System.out.println("CreateVsmFourgramDimension finished.");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return lstVsm4g;
	}
	
}
