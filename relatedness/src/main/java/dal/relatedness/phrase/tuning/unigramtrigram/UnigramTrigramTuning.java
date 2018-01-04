package dal.relatedness.phrase.tuning.unigramtrigram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.TreeMap;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;

public class UnigramTrigramTuning {
	
	TreeMap<String, Integer> crossedUnis;
	
	public void TuneTrigramUnigram(){
		try{
			TuneAndSave();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void TuneAndSave() {
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(PhraseDirectories.unigramGramIndexedProcessedContextStemDir+PhraseFileNames.uniGramStemmedFile));
			System.out.println("load "+PhraseDirectories.unigramGramIndexedProcessedContextStemDir+PhraseFileNames.uniGramStemmedFile);
			HashMap<String, Integer> unis = new HashMap<String, Integer>();
			
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				String [] arr = sCurrentLine.split("\\s+");
				
				if(arr.length!=2){
					System.out.println("bad uni="+sCurrentLine);
					continue;
				}
				
				unis.put(arr[0], Integer.parseInt(arr[1]));
			}
			
			br.close();
			
			crossedUnis = new TreeMap<String, Integer>();
			
			for(char ch='a';ch<='z';ch++)
			{
				String inputFile = PhraseDirectories.triGramIndexedProcessedContextStemDir+String.valueOf(ch);
				
				String outFilepath=PhraseDirectories.trigramCrossCheckDir+String.valueOf(ch);
				
				System.out.println("Input="+inputFile+", output="+outFilepath);
				
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFilepath));
				
				BufferedReader br1 = new BufferedReader(new FileReader(inputFile) );
			
				String sCurrentLine1;
				int lineCountOriginal3gm = 0;
				int lineCountCrosschecked = 0;
				while ((sCurrentLine1 = br1.readLine()) != null) {
					
					if(sCurrentLine1.isEmpty()) continue;
					
					lineCountOriginal3gm++;
					
					String arr[] = sCurrentLine1.split("\\s+");
					
					String wordIn3g = arr[0];
					
					if(unis.containsKey(wordIn3g)){
						
						bufferedWriter.write(sCurrentLine1+"\n");
						
						lineCountCrosschecked++;
						
						crossedUnis.put(wordIn3g, unis.get(wordIn3g));
					}
					else{
						System.out.println("not exist in uni="+wordIn3g);
					}
				}
				
				br1.close();
				
				System.out.println("lineCountOriginal3gm="+lineCountOriginal3gm+", lineCountCrosschecked="+lineCountCrosschecked);
				
				bufferedWriter.close();
				
			}
			
			SaveTunedUniGrams();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void SaveTunedUniGrams() {
		try{
			String outFile = PhraseDirectories.unigramCrossCheckDir+PhraseFileNames.uniGramStemmedCrossChecekedFile;
			
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFile));
			
			for(String crossUni: crossedUnis.keySet()){
				bufferedWriter.write(crossUni+" "+crossedUnis.get(crossUni)+"\n");
			}
			
			bufferedWriter.close();
			
			System.out.println("finish SaveTunedUniGrams");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
