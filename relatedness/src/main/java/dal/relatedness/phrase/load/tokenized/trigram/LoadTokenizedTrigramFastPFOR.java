package dal.relatedness.phrase.load.tokenized.trigram;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Date;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.load.nontokenized.unigram.LoadNonTokenizeUnigram;

public class LoadTokenizedTrigramFastPFOR {
	private LoadNonTokenizeUnigram _loadNonTokenizeUnigram;
	
	public LoadTokenizedTrigramFastPFOR(LoadNonTokenizeUnigram loadNonTokenizeUnigram){
		_loadNonTokenizeUnigram = loadNonTokenizeUnigram;
	}

	private int[][] _wordIdContextIdsFastPFOR;
	
	public int[][] getTriGmWordIdContextIdsFastPFOR() {
		return _wordIdContextIdsFastPFOR;
	}
	
	private int[][] _wordIdContextFreqsFastPFOR;
	
	public int[][] getTriGmWordIdContextFreqsFastPFOR() {
		return _wordIdContextFreqsFastPFOR;
	}
	
	public void PopualteTokenizedTrigramFastPFOR(){
		try{
			
			int uniSize = _loadNonTokenizeUnigram.getAlStemmedCrossCheckedUniFreqs().size();
			
			_wordIdContextIdsFastPFOR = new int[uniSize][];
			_wordIdContextFreqsFastPFOR = new int[uniSize][];
			
			for(char ch='a'; ch<='z';ch++){
				String triGTokenizedFile = PhraseDirectories.tokenizedTriGramFastpforDir+String.valueOf(ch);
				
				System.out.println(new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory());
				
				System.out.println("load" + triGTokenizedFile);
				
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(triGTokenizedFile)));
				
				long totalContexts = 0;	
				long lineCount = 0;
				long maxCtxSizeGlobal = 0;
				
				//long skippedContxets = 0;
				
				while(in.available()>0){
					int wordid = in.readInt();
					int compressedContextSize = in.readInt();
					
//					if(contextSize>=3000){
//						skippedContxets++;
//					}
					
					if(maxCtxSizeGlobal<compressedContextSize){
						maxCtxSizeGlobal = compressedContextSize;
					}
					
					lineCount++;
					
					totalContexts=totalContexts+compressedContextSize;
					
					//int maxCtxSize = contextSize>3000?3000:contextSize;
					
					int [] ctxIds = new  int[compressedContextSize];
				
					for(int i=0;i<compressedContextSize;i++){
						int ctxId = in.readInt();
						
						ctxIds[i] = ctxId;
					}
					
					if(wordid<_wordIdContextIdsFastPFOR.length){
						_wordIdContextIdsFastPFOR[wordid] = ctxIds;
					}
					
					int ctxFreqSize = in.readInt();
					int [] ctxFreqs = new  int[ctxFreqSize];
					
					for(int i=0;i<ctxFreqSize;i++){
						int ctxFreq = in.readInt();
						
						ctxFreqs[i]=ctxFreq;
					}
					
					if(wordid<_wordIdContextFreqsFastPFOR.length){
						_wordIdContextFreqsFastPFOR[wordid] = ctxFreqs;
					}
					
					
					if(lineCount%1000000==0){
						System.out.println("lineCount="+lineCount);
					}
				}
				
				System.out.println("totalContexts="+totalContexts+", avg context length="
					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
					+", lineCount="+lineCount);
				
				in.close();
				
			}
			
			System.out.println("PopualteTokenizedTrigramFastPFOR finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
