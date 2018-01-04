package dal.relatedness.phrase.load.tokenized.fourgram;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Date;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;

public class LoadTokenizedFourgram {
	
	private LoadNonTokenizeBigram _loadNonTokenizeBigram;
	
	public LoadTokenizedFourgram(LoadNonTokenizeBigram loadNonTokenizeBigram){
		_loadNonTokenizeBigram = loadNonTokenizeBigram;
	}

	private int[][] _PhIdContextIdFreqs;
	
	public int[][] getfourGmPhIdContextIdFreqs() {
		return _PhIdContextIdFreqs;
	}
	
	private final int MaxContextSize4gm = 50000;
	
	public void PopualteTokenizedFourgram(){
		try{
			
			int bgSize = _loadNonTokenizeBigram.getAlStemmedCrossCheckedBgFreqs().size();
			
			_PhIdContextIdFreqs = new int[bgSize][];
			
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
					
					if(contextSize>=MaxContextSize4gm){
						skippedContxets++;
					}
					
					if(maxCtxSizeGlobal<contextSize){
						maxCtxSizeGlobal = contextSize;
					}
					
					lineCount++;
					
					totalContexts=totalContexts+contextSize;
					
					int maxCtxSize = contextSize>MaxContextSize4gm?MaxContextSize4gm:contextSize;
					
					int [] ctxIdFreqs = new  int[maxCtxSize*2];
					
					for(int i=0;i<contextSize;i++){
						int ctxId = in.readInt();
						int ctxFreq = in.readInt();
						
						if(i>=MaxContextSize4gm){
							continue;
						}
						
						ctxIdFreqs[i*2]=ctxId;
						ctxIdFreqs[i*2+1]=ctxFreq;
					}
					
					if(phid<_PhIdContextIdFreqs.length){
						_PhIdContextIdFreqs[phid] = ctxIdFreqs;
					}
					
					if(lineCount%1000000==0){
						System.out.println("lineCount="+lineCount);
					}
						
					
				}
				
				System.out.println("totalContexts="+totalContexts+", avg context length="
					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
					+", PhWithLargeContxets="+skippedContxets+", lineCount="+lineCount);
				
				in.close();
				
			}
			
			System.out.println("PopualteTokenizedFourgram finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
