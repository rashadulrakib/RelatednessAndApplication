package dal.relatedness.phrase.load.tokenized.fourgram;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Date;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;

public class LoadTokenizedFourgramFastPFOR {
	private LoadNonTokenizeBigram _loadNonTokenizeBigram;
	
	public LoadTokenizedFourgramFastPFOR(LoadNonTokenizeBigram loadNonTokenizeBigram){
		_loadNonTokenizeBigram = loadNonTokenizeBigram;
	}

	private int[][] _PhIdContextIdsFastPFOR;
	
	public int[][] getfourGmPhIdContextIdsFastPFOR() {
		return _PhIdContextIdsFastPFOR;
	}
	
	private int[][] _PhIdContextFreqsFastPFOR;
	
	public int[][] getfourGmPhIdContextFreqsFastPFOR() {
		return _PhIdContextFreqsFastPFOR;
	}
	
	public void PopualteTokenizedFourgramFastPFOR(){
		try{
			
			int bgSize = _loadNonTokenizeBigram.getAlStemmedCrossCheckedBgFreqs().size();
			
			_PhIdContextIdsFastPFOR = new int[bgSize][];
			_PhIdContextFreqsFastPFOR = new int[bgSize][];
			
			for(char ch='a'; ch<='z';ch++){
				System.out.println(new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory());
				
				String fourGTokenizedFile = PhraseDirectories.tokenizedFourGramFastpforDir+String.valueOf(ch);
				
				System.out.println("load" + fourGTokenizedFile);
				
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fourGTokenizedFile)));
				
				long totalContexts = 0;	
				long lineCount = 0;
				long maxCtxSizeGlobal = 0;
				
				//long skippedContxets = 0;
				
				while(in.available()>0){
					int phid = in.readInt();
					int compressedContextSize = in.readInt();
					
//					if(contextSize>=8000){
//						skippedContxets++;
//					}
					
					if(maxCtxSizeGlobal<compressedContextSize){
						maxCtxSizeGlobal = compressedContextSize;
					}
					
					lineCount++;
					
					totalContexts=totalContexts+compressedContextSize;
					
					//int maxCtxSize = contextSize>8000?8000:contextSize;
					
					int [] compressedCtxIds = new  int[compressedContextSize];
					
					for(int i=0;i<compressedContextSize;i++){
						int ctxId = in.readInt();
						//int ctxFreq = in.readInt();
						
//						if(i>=8000){
//							continue;
//						}
						
						compressedCtxIds[i]=ctxId;
					}
					
					if(phid<_PhIdContextIdsFastPFOR.length){
						_PhIdContextIdsFastPFOR[phid] = compressedCtxIds;
					}
					
					int ctxFreqSize = in.readInt();
					int [] ctxFreqs = new  int[ctxFreqSize];
					
					for(int i=0;i<ctxFreqSize;i++){
						int ctxFreq = in.readInt();
						
//						if(i>=8000){
//							continue;
//						}
						
						ctxFreqs[i]=ctxFreq;
					}
					
					if(phid<_PhIdContextFreqsFastPFOR.length){
						_PhIdContextFreqsFastPFOR[phid] = ctxFreqs;
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
			
			System.out.println("PopualteTokenizedFourgramFastPFOR finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
