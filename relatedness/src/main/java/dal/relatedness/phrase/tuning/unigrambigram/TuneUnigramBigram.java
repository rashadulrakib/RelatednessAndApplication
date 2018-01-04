package dal.relatedness.phrase.tuning.unigrambigram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;

///
// cross check all the stemmed unigram exist in stemmed bi-gram
//before this, we need to stem unigram
///
public class TuneUnigramBigram {
	public void TuneUniBigram(){
		try{
			//load unigrams
			//load bigrams as unigrams
			
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(PhraseDirectories.unigramGramIndexedProcessedContextStemDir+PhraseFileNames.uniGramStemmedFile));
			System.out.println("load "+PhraseDirectories.unigramGramIndexedProcessedContextStemDir+PhraseFileNames.uniGramStemmedFile);
			HashSet<String> unis = new HashSet<String>();
			
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				unis.add(sCurrentLine.split("\\s+")[0]);
			}
			
			br.close();
			
			br = new BufferedReader(new FileReader(PhraseDirectories.bigramCrossCheckDir+PhraseFileNames.bgStemmedCrossedCheckedFile));
			System.out.println("load "+PhraseDirectories.bigramCrossCheckDir+PhraseFileNames.bgStemmedCrossedCheckedFile);
			
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				String [] arr = sCurrentLine.split("\\s+");
				
				if(!unis.contains(arr[0])){
					System.out.println("Missing in bg="+arr[0]);
				}
				
				if(!unis.contains(arr[1])){
					System.out.println("Missing in bg="+arr[1]);
				}
			}
			
			br.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		
		System.out.println("TuneUniBigram finished");
	}
}
