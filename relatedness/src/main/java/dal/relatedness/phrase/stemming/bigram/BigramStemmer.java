package dal.relatedness.phrase.stemming.bigram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.phrase.utils.common.Util;

public class BigramStemmer {

	TreeMap<String, Integer> hmBgStemmed;
	
	public void StemRegularBigrams(){
		try{
			for(char ch='a';ch<='z';ch++){
				
				hmBgStemmed = new TreeMap<String, Integer>();
				
				System.out.println("Processing="+ch);
				List<String> files = GetIndexedFiles(ch); 
				
				ProcessAndStemFromFiles(files, ch);
				
				SaveStemPhrases(ch);
				
				files.clear();
				files=null;
				
				hmBgStemmed.clear();
				hmBgStemmed = null;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private List<String> GetIndexedFiles(char ch) {
		try{
			List<String> files = new ArrayList<String>();
			
			String filePath = PhraseDirectories.biGramFileIndexingDir+String.valueOf(ch);
			
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(filePath));

			while ((sCurrentLine = br.readLine()) != null) {
				
				if(sCurrentLine.isEmpty()) continue;
				files.add(sCurrentLine);
			}
			
			br.close();
			
			return files;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private void SaveStemPhrases(char ch) {
		try{
			String stemBgFile = PhraseDirectories.biGramIndexedProcessedContextStemDir+String.valueOf(ch);
			System.out.println("Writing to "+ stemBgFile);
			
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(stemBgFile));
		        
		    for(String bgKey: hmBgStemmed.keySet() ){
		    	int freq = hmBgStemmed.get(bgKey);
		    	if(freq<0){
		    		System.out.println(bgKey+" "+freq);
		    		freq = Integer.MAX_VALUE;
		    	}
		    	bufferedWriter.write(bgKey+" "+freq+"\n");
		    }
		    
			bufferedWriter.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void ProcessAndStemFromFiles(List<String> files, char ch) {
		try{
			for(String filePath: files){
				
				String sCurrentLine;
				BufferedReader br = new BufferedReader(new FileReader(filePath));
				System.out.println("Process file:"+filePath);
				
				while ((sCurrentLine = br.readLine()) != null) {
					if(sCurrentLine.isEmpty()) continue;
					
					sCurrentLine = sCurrentLine.toLowerCase();
					
					String arr[] = sCurrentLine.split("\\s+");
					if(arr.length!=3) continue;
					
					if(!Util.IsValidTokenByAllCharNumber(arr[0]) || !Util.IsValidTokenByAllCharNumber(arr[1])) {
						continue;
					}
					
					String stemmedPh = StemmingUtil.stemPhrase(arr[0]+" "+arr[1]).trim();
					
					String stemArr [] = stemmedPh.split("\\s+");
					
					if(stemArr.length!=2){
						continue;
					}
					
					if(stemArr[0].length()<2 || stemArr[1].length()<2){
						continue;
					}
					
					if(stemmedPh.charAt(0)!=ch){
						continue;
					}
					
					if(!Util.IsValidInteger(arr[2])){
						continue;
					}
					
					if(!hmBgStemmed.containsKey(stemmedPh)){
						hmBgStemmed.put(stemmedPh, Integer.parseInt(arr[2]));
					}
					else{
						hmBgStemmed.put(stemmedPh, hmBgStemmed.get(stemmedPh) + Integer.parseInt(arr[2]));
					}
				}
				
				br.close();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

//	public void StemPreocessedBigramsFrom4gram(){
//		try{
//			StemPreocessedBigrams();
//		}
//		catch(Exception e){
//			System.out.println(e.toString());
//		}
//	}

//	private void StemPreocessedBigrams() {
//		try{
//			
//			TreeMap<String, Integer> hmBg = new TreeMap<String, Integer>(); 
//			
//			for(char ch='z' ; ch<='z';ch++){
//				
//				String sCurrentLine;
//				BufferedReader br = new BufferedReader(new FileReader(PhraseDirectories.biGramIndexedProcessedContextDir+String.valueOf(ch)));
//				System.out.println(PhraseDirectories.biGramIndexedProcessedContextDir+String.valueOf(ch));
//				
//				while ((sCurrentLine = br.readLine()) != null) {
//					sCurrentLine = sCurrentLine.trim();
//					if(sCurrentLine.isEmpty()) continue;
//					
//					String arr[] = sCurrentLine.split("\\s+");
//					if(arr.length!=3) continue;
//					
//					String phrase = arr[0]+" "+arr[1];
//					String stemmedPh = StemmingUtil.stemPhrase(phrase);
//					
//					if(!hmBg.containsKey(stemmedPh)){
//						hmBg.put(stemmedPh, Integer.parseInt(arr[2]));
//					}
//					else{
//						hmBg.put(stemmedPh, hmBg.get(stemmedPh) + Integer.parseInt(arr[2]));
//					}
//				}
//				
//				br.close();
//			}
//			
//			SaveStemmedPhrase(hmBg);
//		}
//		catch(Exception e){
//			System.out.println(e.toString());
//		}
//	}

//	private void SaveStemmedPhrase(TreeMap<String, Integer> hmBg) {
//		try{
//			String stemBgFile = PhraseDirectories.biGramIndexedProcessedContextStemDir+PhraseFileNames.bgStemmedFile;
//			System.out.println("Writing to "+ stemBgFile);
//			FileWriter writer = new FileWriter(stemBgFile);
//			BufferedWriter bufferedWriter = new BufferedWriter(writer);
//		        
//		    for(String bgKey: hmBg.keySet() ){
//		    	int freq = hmBg.get(bgKey);
//		    	if(freq<0){
//		    		System.out.println(bgKey+" "+freq);
//		    		freq = Integer.MAX_VALUE;
//		    	}
//		    	writer.write(bgKey+" "+freq+"\n");
//		    }
//		    
//			bufferedWriter.close();
//			writer.close();
//		}
//		catch(Exception e){
//			System.out.println(e.toString());
//		}
//	}
}
