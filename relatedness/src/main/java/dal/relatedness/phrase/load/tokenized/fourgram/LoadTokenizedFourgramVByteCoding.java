package dal.relatedness.phrase.load.tokenized.fourgram;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Date;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;

public class LoadTokenizedFourgramVByteCoding {

	LoadNonTokenizeBigram loadNonTokenizeBigram;
	public LoadTokenizedFourgramVByteCoding(LoadNonTokenizeBigram loadNonTokenizeBigram) {
		this.loadNonTokenizeBigram = loadNonTokenizeBigram;
	}

	private byte[][]  phIdContextIdsByteCoding;
	
	public byte[][] getfourGmPhIdContextIdsByteCoding() {
		return phIdContextIdsByteCoding;
	}
	
	private byte[][]  phIdContextFreqsByteCoding;
	
	public byte[][] getfourGmPhIdContextFreqsByteCoding() {
		return phIdContextFreqsByteCoding;
	}
	
	public void PopualteTokenizedFourgramVByteCoding() {
		try{
			
			int bgSize = loadNonTokenizeBigram.getAlStemmedCrossCheckedBgFreqs().size();
			
			phIdContextIdsByteCoding = new byte[bgSize][];
			phIdContextFreqsByteCoding = new byte[bgSize][];
			
			for(char ch='a'; ch<='z';ch++){
				System.out.println(new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory()+", phIdContextIdsByteCoding.size="+phIdContextIdsByteCoding.length);
				
				String fourGTokenizedFile = PhraseDirectories.tokenizedFourGramVByteDir+String.valueOf(ch);
				
				System.out.println("load:" + fourGTokenizedFile);
				
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fourGTokenizedFile)));
				
				long totalContexts = 0;	
				long lineCount = 0;
				long maxCtxSizeGlobal = 0;
				
				while(in.available()>0){
					int phid = in.readInt();
					int contextSize = in.readInt();
				
					if(maxCtxSizeGlobal<contextSize){
						maxCtxSizeGlobal = contextSize;
					}
					
					lineCount++;
					
					totalContexts=totalContexts+contextSize;
				
					byte[] ctxIds = new byte[contextSize];
					
					for(int i=0;i<contextSize;i++){
						byte ctxId = in.readByte();					
						ctxIds[i] = ctxId;
					}
					
					if(phid<phIdContextIdsByteCoding.length){
						phIdContextIdsByteCoding[phid] = ctxIds;
					}
					else{
						System.out.println("ph index error "+phid);
					}
					
					int ctxFreqSize = in.readInt();
					byte[] ctxFreqs = new byte[ctxFreqSize];
					
					for(int i=0;i<ctxFreqSize;i++){
						byte ctxFreq = in.readByte();
						ctxFreqs[i] = ctxFreq;
					}
					
					if(phid<phIdContextFreqsByteCoding.length){
						phIdContextFreqsByteCoding[phid] = ctxFreqs;
					}
					else{
						System.out.println("ph index error "+phid);
					}
					
					if(lineCount%1000000==0){
						System.out.println("lineCount="+lineCount);
					}
				}
				
				System.out.println("totalContexts="+totalContexts+", avg context length="
					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal+
					", lineCount="+lineCount);
				
				in.close();
				
			}
			
			System.out.println("PopualteTokenizedFourgramVByteCoding finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
