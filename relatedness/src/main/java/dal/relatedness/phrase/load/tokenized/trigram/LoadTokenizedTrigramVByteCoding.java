package dal.relatedness.phrase.load.tokenized.trigram;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Date;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.load.nontokenized.unigram.LoadNonTokenizeUnigram;

public class LoadTokenizedTrigramVByteCoding {

	LoadNonTokenizeUnigram loadNonTokenizeUnigram;
	
	public LoadTokenizedTrigramVByteCoding(LoadNonTokenizeUnigram loadNonTokenizeUnigram) {
		this.loadNonTokenizeUnigram = loadNonTokenizeUnigram;
	}
	
	private byte[][]  wordIdContextIdsByteCoding;
	
	public byte[][] getTriGmWordIdContextIdsByteCoding() {
		return wordIdContextIdsByteCoding;
	}
	
	private byte[][] wordIdContextFreqsByteCoding;
	
	public byte[][] getTriGmWordIdContextFreqsByteCoding() {
		return wordIdContextFreqsByteCoding;
	}

	public void PopualteTokenizedTrigramVByteCoding() {
		try{
			int wordSize = loadNonTokenizeUnigram.getAlStemmedCrossCheckedUniFreqs().size();
			
			wordIdContextIdsByteCoding = new byte[wordSize][];
			wordIdContextFreqsByteCoding = new byte[wordSize][];
			
			for(char ch='a'; ch<='z';ch++){
				System.out.println(new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory()+", wordIdContextIdsByteCoding.size="+wordIdContextIdsByteCoding.length);
				
				String triGTokenizedFile = PhraseDirectories.tokenizedTriGramVByteDir+String.valueOf(ch);
				
				System.out.println("load:" + triGTokenizedFile);
				
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(triGTokenizedFile)));
				
				long totalContexts = 0;	
				long lineCount = 0;
				long maxCtxSizeGlobal = 0;
				
				while(in.available()>0){
					int wordid = in.readInt();
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
					
					if(wordid<wordIdContextIdsByteCoding.length){
						wordIdContextIdsByteCoding[wordid] = ctxIds;
					}
					else{
						System.out.println("word index error "+wordid);
					}
					
					int ctxFreqSize = in.readInt();
					byte[] ctxFreqs = new byte[ctxFreqSize];
					
					for(int i=0;i<ctxFreqSize;i++){
						byte ctxFreq = in.readByte();
						ctxFreqs[i] = ctxFreq;
					}
					
					if(wordid<wordIdContextFreqsByteCoding.length){
						wordIdContextFreqsByteCoding[wordid] = ctxFreqs;
					}
					else{
						System.out.println("word index error "+wordid);
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
			
			System.out.println("PopualteTokenizedTrigramVByteCoding finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
