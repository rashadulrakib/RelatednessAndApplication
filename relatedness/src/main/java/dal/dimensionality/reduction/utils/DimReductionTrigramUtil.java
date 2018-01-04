package dal.dimensionality.reduction.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Date;
import java.util.TreeSet;

import dal.dimensionality.reduction.tsne.DimReductionConstants;
import dal.relatedness.phrase.constants.PhraseDirectories;

public class DimReductionTrigramUtil {

	public TreeSet<Integer> CreateVsmTrigramDimension(){
		TreeSet<Integer> vsmDim = new TreeSet<Integer>();
		try{
			
			for(char ch='a'; ch<='z';ch++){
				String triGTokenizedFile = PhraseDirectories.tokenizedTriGramDir+String.valueOf(ch);
				
				System.out.println(new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory());
				
				System.out.println("load" + triGTokenizedFile);
				
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(triGTokenizedFile)));
				
				long totalContexts = 0;	
				long lineCount = 0;
				long maxCtxSizeGlobal = 0;
				
				long skippedContxets = 0;
				
				while(in.available()>0){
					int wordid = in.readInt();
					int contextSize = in.readInt();
					
					lineCount++;
					
					if(contextSize < DimReductionConstants.MinContextSize){
						continue;
					}
					
					if(contextSize>=DimReductionConstants.MaxContextSie3gm){
						skippedContxets++;
					}
					
					if(maxCtxSizeGlobal<contextSize){
						maxCtxSizeGlobal = contextSize;
					}
					
					totalContexts=totalContexts+contextSize;
					
					for(int i=0;i<contextSize;i++){
						int ctxId = in.readInt();
						int ctxFreq = in.readInt();
						
						if(i>=DimReductionConstants.MaxContextSie3gm){
							continue;
						}
						
						if(contextSize<DimReductionConstants.MinContextSize){
							continue;
						}

						vsmDim.add(ctxId);
						
					}
					
					if(lineCount%1000000==0){
						System.out.println("lineCount="+lineCount+", trigramVSMCtxSize="+vsmDim.size());
					}
				}
				
				System.out.println("totalContexts="+totalContexts+", avg context length="
					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
					+", WordWithLargeContxets="+skippedContxets+", lineCount="+lineCount+", trigramVSMCtxSize="+vsmDim.size());
				in.close();
			}	
			
			System.out.println("CreateVsmTrigramDimension finished.");
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return vsmDim;
	}
}
	

