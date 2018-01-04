package dal.relatedness.phrase.tokenize.trigram;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

import dal.relatedness.phrase.common.ContextDictionaryLoader;
import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.load.nontokenized.unigram.LoadNonTokenizeUnigram;

public class TokenizeTrigramFromCrossChecked {
	public void TokenizeTrigram(){
		try{
			//load all the only unigrams, not freq
			//load all contexts like unigrams
			//iterate each 3-g files (a,b,c,...) and binary tokenize
			
			LoadNonTokenizeUnigram loadUni = new LoadNonTokenizeUnigram();
			ContextDictionaryLoader ctxLoader = new ContextDictionaryLoader();
			
			loadUni.PopulateStemmedUniGramsList();
			ctxLoader.PopulateStemmedContextDictionary();
			
			ArrayList<String> unis = loadUni.getAlStemmedCrossCheckedUnis();
			ArrayList<String> contexts = ctxLoader.getStemmedCrossCheckedContexts();
			
			for(char ch='a';ch<='z';ch++){
				String inFile = PhraseDirectories.trigramCrossCheckDir+String.valueOf(ch);
				String outFile = PhraseDirectories.tokenizedTriGramDir+String.valueOf(ch);
				
				System.out.println("inFile="+inFile+", outFile="+outFile);
				
				
				DataOutputStream  out = new DataOutputStream (new BufferedOutputStream(new FileOutputStream(outFile)));
				
				String sCurrentLine;
				BufferedReader br = new BufferedReader(new FileReader(inFile));		

				while ((sCurrentLine = br.readLine()) != null) {
					if(sCurrentLine.isEmpty()) continue;
					
					String [] arr = sCurrentLine.split("\\s+");
					
					String word = arr[0];
					int wordId = Collections.binarySearch(unis, word);
					
					if(wordId<0){
						System.out.println("bad word="+word);
						continue;
					}
					
					int contextSize = Integer.parseInt(arr[1]);

					out.writeInt(wordId);
					out.writeInt(contextSize);
					
					for(int i=1;i<=contextSize;i++){
						String bgContext = arr[i*3-1]+" "+arr[i*3];
						int ctxFreq = Integer.parseInt(arr[i*3+1]);
						int ctxId = Collections.binarySearch(contexts, bgContext);
						
						if(ctxId<0 || ctxFreq<0){
							System.out.println("bad word="+word+", bad ctx="+bgContext);
							continue;
						}
						
						out.writeInt(ctxId);
						out.writeInt(ctxFreq);
					}

				}
				
				br.close();
				out.close();
			}
			
			System.out.println("TokenizeTrigram finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
