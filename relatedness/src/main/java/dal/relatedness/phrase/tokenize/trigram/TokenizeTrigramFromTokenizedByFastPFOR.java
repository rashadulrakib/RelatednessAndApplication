package dal.relatedness.phrase.tokenize.trigram;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import me.lemire.integercompression.differential.IntegratedIntCompressor;
import dal.relatedness.phrase.constants.PhraseDirectories;

public class TokenizeTrigramFromTokenizedByFastPFOR {
	public void TokenizeTriramFromTokenizedFileFastPFOR(){
		try{
			for(char ch='a'; ch<='z';ch++){
				String infilePath = PhraseDirectories.tokenizedTriGramDir+String.valueOf(ch);
				String outfilePath = PhraseDirectories.tokenizedTriGramFastpforDir+String.valueOf(ch);
				
				System.out.println(infilePath+","+outfilePath+","+new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory());
				
				PopulateAndWriteTokenizedTriGram(infilePath, outfilePath);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void PopulateAndWriteTokenizedTriGram(String infilePath, String outfilePath) {
		try{
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(infilePath)));
			DataOutputStream  out = new DataOutputStream (new BufferedOutputStream(new FileOutputStream(outfilePath)));
			
			long totalContexts = 0;	
			long lineCount = 0;
			long maxCtxSizeGlobal = 0;
			
			//long skippedContxets = 0;
			IntegratedIntCompressor iic = new IntegratedIntCompressor();
			
			while(in.available()>0){
				
				int wordid = in.readInt();
				int contextSize = in.readInt();
				
//				if(contextSize>=3000){
//					skippedContxets++;
//				}
				
				if(maxCtxSizeGlobal<contextSize){
					maxCtxSizeGlobal = contextSize;
				}
				
				lineCount++;
				
				totalContexts=totalContexts+contextSize;
				
				int maxCtxSize = contextSize;
				
				int [] ctxFreqs = new  int[maxCtxSize];
				int [] ctxIds = new int[maxCtxSize];
			
				for(int i=0;i<contextSize;i++){
					int ctxId = in.readInt();
					int ctxFreq = in.readInt();
					
//					if(i>=3000){
//						continue;
//					}
					
					ctxIds[i]=ctxId;
					ctxFreqs[i]=ctxFreq;
				}
				
				
				if(lineCount%1000000==0){
					System.out.println("lineCount="+lineCount);
				}
				
				out.writeInt(wordid);
				
				int[] compressed = iic.compress(ctxIds);
				out.writeInt(compressed.length);
				for(int i=0;i<compressed.length;i++){
					out.writeInt(compressed[i]);
				}
				
				out.writeInt(ctxFreqs.length);
				for(int i=0;i<ctxFreqs.length;i++){
					out.writeInt(ctxFreqs[i]);
				}
			}
			
			in.close();
			out.close();
			
			System.out.println("totalContexts="+totalContexts+", avg context length="
					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
					+", lineCount="+lineCount);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
