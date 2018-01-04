package dal.relatedness.phrase.load.tokenized.trigram;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Date;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.load.nontokenized.unigram.LoadNonTokenizeUnigram;

public class LoadTokenizedTrigram {
	private LoadNonTokenizeUnigram _loadNonTokenizeUnigram;
	
	public LoadTokenizedTrigram(LoadNonTokenizeUnigram loadNonTokenizeUnigram){
		_loadNonTokenizeUnigram = loadNonTokenizeUnigram;
	}

	private int[][] _wordIdContextIdFreqs;
	
	public int[][] getTriGmWordIdContextIdFreqs() {
		return _wordIdContextIdFreqs;
	}
	
	private final int MaxContextSie3gm = 50000;
	
	public void PopualteTokenizedTrigram(){
		try{
			
			int uniSize = _loadNonTokenizeUnigram.getAlStemmedCrossCheckedUniFreqs().size();
			
			_wordIdContextIdFreqs = new int[uniSize][];
			
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
					
					if(contextSize>=MaxContextSie3gm){
						skippedContxets++;
					}
					
					if(maxCtxSizeGlobal<contextSize){
						maxCtxSizeGlobal = contextSize;
					}
					
					lineCount++;
					
					totalContexts=totalContexts+contextSize;
					
					int maxCtxSize = contextSize>MaxContextSie3gm?MaxContextSie3gm:contextSize;
					
					int [] ctxIdFreqs = new  int[maxCtxSize*2];
				
					for(int i=0;i<contextSize;i++){
						int ctxId = in.readInt();
						int ctxFreq = in.readInt();
						
						if(i>=MaxContextSie3gm){
							continue;
						}
						
						ctxIdFreqs[i*2]=ctxId;
						ctxIdFreqs[i*2+1]=ctxFreq;
					}
					
					if(wordid<_wordIdContextIdFreqs.length){
						_wordIdContextIdFreqs[wordid] = ctxIdFreqs;
					}
					
					if(lineCount%1000000==0){
						System.out.println("lineCount="+lineCount);
					}
				}
				
				System.out.println("totalContexts="+totalContexts+", avg context length="
					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
					+", WordWithLargeContxets="+skippedContxets+", lineCount="+lineCount);
				
				in.close();
				
			}
			
			System.out.println("PopualteTokenizedTrigram finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
