package dal.relatedness.phrase.stemming.unigram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.TreeMap;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.phrase.utils.common.Util;

public class UnigramStemmer {
	
	
	public void StemUnigram(){
		try{
			
		
			TreeMap<String, Integer> hmUni = new TreeMap<String, Integer>();
			
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(PhraseDirectories.uniGramDir+PhraseFileNames.uniGramFile));

			while ((sCurrentLine = br.readLine()) != null) {
				
				sCurrentLine = sCurrentLine.toLowerCase();
				
				if(sCurrentLine.isEmpty()) continue;
				
				String [] arr = sCurrentLine.split("\\s+");
				
				if(arr.length!=2){
					continue;
				}
				
				if(!Util.IsValidTokenByAllCharNumber(arr[0])) {
					continue;
				}
				
				String stemmedUni = StemmingUtil.stemPhrase(arr[0]).trim();
				
				if(stemmedUni.length()<2){
					continue;
				}
				
				long freq = Long.parseLong(arr[1]);
				
				if(!hmUni.containsKey(stemmedUni)){
					hmUni.put(stemmedUni, (int)freq);
				}
				else{
					hmUni.put(stemmedUni, (int)(hmUni.get(stemmedUni) + freq));
				}
			}
			
			br.close();
			
			String outputFile = PhraseDirectories.unigramGramIndexedProcessedContextStemDir+PhraseFileNames.uniGramStemmedFile;
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
			
			for(String uni: hmUni.keySet()){
				int freq = hmUni.get(uni);
				
				if(freq<0){
					freq = Integer.MAX_VALUE;
					System.out.println(uni+" freq exceed");
				}
				bw.write(uni+" "+freq+"\n");
			}
			
			bw.close();
			
			System.out.println("StemUnigram finished");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
