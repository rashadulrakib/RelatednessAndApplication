package dal.relatedness.phrase.tokenize.fourgram;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.utils.encoding.VariableByteCode;

public class TokenizeFourgramFromTokenizedByVByteCoding {
	
	public void TokenizeFourgramByteCoding(){
		try{
			for(char ch='a'; ch<='z';ch++){
				String infilePath = PhraseDirectories.tokenizedFourGramDir+String.valueOf(ch);
				String outfilePath = PhraseDirectories.tokenizedFourGramVByteDir+String.valueOf(ch);
				
				System.out.println(infilePath+","+outfilePath+","+new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory());
				
				PopulateAndWriteTokenizedFourGram(infilePath, outfilePath);
			}
			
			System.out.println("TokenizeFourgramFromTokenizedByVByteCoding finished.");
		}catch(Exception e){
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

			int sameIdCount =0;
			
			while(in.available()>0){
				int phid = in.readInt();
				int contextSize = in.readInt();
				
				if(maxCtxSizeGlobal<contextSize){
					maxCtxSizeGlobal = contextSize;
				}
				
				lineCount++;
				
				totalContexts=totalContexts+contextSize;
				
				List<Integer> ctxIds = new ArrayList<Integer>();
				List<Integer> ctxFreqs = new ArrayList<Integer>();
				
				int lastCtxId = -1;
				
				for(int i=0;i<contextSize;i++){
					int ctxId = in.readInt();
					int ctxFreq = in.readInt();
					
					if(ctxId<=lastCtxId){
						sameIdCount++;
						continue;
					}
					
					lastCtxId = ctxId;
					
					ctxIds.add(ctxId);
					ctxFreqs.add(ctxFreq);
				}
				
				if(lineCount%1000000==0){
					System.out.println("lineCount="+lineCount);
				}
				
				//write phid
				//compress ctxIds and write 
				//write ctxFreqs
				
				out.writeInt(phid);
				
				byte [] encodeCtxIds = VariableByteCode.encodeInterpolate(ctxIds);
				byte [] encodeCtxFreqs = VariableByteCode.encode(ctxFreqs);
				
				out.writeInt(encodeCtxIds.length);
				for(int i=0;i<encodeCtxIds.length;i++){
					out.writeByte(encodeCtxIds[i]);
				}
				
				out.writeInt(encodeCtxFreqs.length);
				for(int i=0;i<encodeCtxFreqs.length;i++){
					out.writeByte(encodeCtxFreqs[i]);
				}
			}
			
			System.out.println("totalContexts="+totalContexts+", avg context length="
					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
					+", lineCount="+lineCount+",sameIdCount="+sameIdCount);
			
			in.close();
			out.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
