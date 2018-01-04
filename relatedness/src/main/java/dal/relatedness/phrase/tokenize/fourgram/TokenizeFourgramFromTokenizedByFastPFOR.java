package dal.relatedness.phrase.tokenize.fourgram;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

import me.lemire.integercompression.differential.IntegratedIntCompressor;
import dal.relatedness.phrase.constants.PhraseDirectories;

public class TokenizeFourgramFromTokenizedByFastPFOR {
	public void TokenizeFourgramFromTokenizedFileFastPFOR(){
		try{
			for(char ch='a'; ch<='z';ch++){
				String infilePath = PhraseDirectories.tokenizedFourGramDir+String.valueOf(ch);
				String outfilePath = PhraseDirectories.tokenizedFourGramFastpforDir+String.valueOf(ch);
				
				System.out.println(infilePath+","+outfilePath+","+new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory());
				
				PopulateAndWriteTokenizedFourGram(infilePath, outfilePath);
			}
			
			System.out.println("TokenizeFourgramFromTokenizedFileFastPFOR finished211.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void PopulateAndWriteTokenizedFourGram(String infilePath, String outfilePath) {
		try{
			
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(infilePath)));
			DataOutputStream  out = new DataOutputStream (new BufferedOutputStream(new FileOutputStream(outfilePath)));
			
			long totalContexts = 0;	
			long lineCount = 0;
			long maxCtxSizeGlobal = 0;
			
			IntegratedIntCompressor iic = new IntegratedIntCompressor();
			
			while(in.available()>0){
				int phid = in.readInt();
				int contextSize = in.readInt();
			
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
										
					ctxIds[i]=ctxId;
					ctxFreqs[i]=ctxFreq;
				}
				
				if(lineCount%1000000==0){
					System.out.println("lineCount="+lineCount);
				}
				
				//write phid
				//compress ctxIds and write 
				//write ctxFreqs
				
				out.writeInt(phid);
				
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
			
			System.out.println("totalContexts="+totalContexts+", avg context length="
				+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
				+", lineCount="+lineCount);
			
			in.close();
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
