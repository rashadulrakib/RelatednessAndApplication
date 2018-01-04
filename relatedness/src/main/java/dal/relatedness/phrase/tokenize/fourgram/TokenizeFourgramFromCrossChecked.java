package dal.relatedness.phrase.tokenize.fourgram;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

import dal.relatedness.phrase.common.ContextDictionaryLoader;
import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;

public class TokenizeFourgramFromCrossChecked {
	public void TokenizeFourGram(){
		try{
			//load all the only unigrams, not freq
			//load all contexts like unigrams
			//iterate each 3-g files (a,b,c,...) and binary tokenize
			
			LoadNonTokenizeBigram loadbi = new LoadNonTokenizeBigram();
			ContextDictionaryLoader ctxLoader = new ContextDictionaryLoader();
			
			loadbi.PopulateStemmedBiGramsList();
			ctxLoader.PopulateStemmedContextDictionary();
			
			ArrayList<String> bgs = loadbi.getAlStemmedCrossCheckedBgs();
			ArrayList<String> contexts = ctxLoader.getStemmedCrossCheckedContexts();
			
			for(char ch='a';ch<='z';ch++){
				String inFile = PhraseDirectories.fourgramCrossCheckDir+String.valueOf(ch);
				String outFile = PhraseDirectories.tokenizedFourGramDir+String.valueOf(ch);
				
				System.out.println("inFile="+inFile+", outFile="+outFile);
				
				DataOutputStream  out = new DataOutputStream (new BufferedOutputStream(new FileOutputStream(outFile)));
				
				String sCurrentLine;
				BufferedReader br = new BufferedReader(new FileReader(inFile));		

				while ((sCurrentLine = br.readLine()) != null) {
					if(sCurrentLine.isEmpty()) continue;
					
					String [] arr = sCurrentLine.split("\\s+");
					
					String phrase = arr[0]+" "+arr[1];
					int phId = Collections.binarySearch(bgs, phrase);
					
					if(phId<0){
						System.out.println("bad phrase="+phrase);
						continue;
					}
					
					int contextSize = Integer.parseInt(arr[2]);
					
					out.writeInt(phId);
					out.writeInt(contextSize);
					
					for(int i=1;i<=contextSize;i++){
						String bgContext = arr[i*3]+" "+arr[i*3+1];
						int ctxFreq = Integer.parseInt(arr[i*3+2]);
						int ctxId = Collections.binarySearch(contexts, bgContext);
						
						if(ctxId<0 || ctxFreq<0){
							System.out.println("bad phrase="+phrase+", bad ctx="+bgContext);
							continue;
						}
						
						out.writeInt(ctxId);
						out.writeInt(ctxFreq);
					}
				}
				
				br.close();
				
				out.close();
			}
			
			System.out.println("TokenizeFourGram finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
