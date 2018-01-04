package dal.relatedness.phrase.unigram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;

public class LoadUnigram {
	public Map<String, Integer> loadTokenizedUniGramsWithId() {
		String tokenizedUniGramFilePath = PhraseDirectories.tokenizedUniGramDir+PhraseFileNames.tokenizedUniGramFile;
		Map<String, Integer> tokenWithIds = new LinkedHashMap<String, Integer>();
		BufferedReader br = null;

		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(tokenizedUniGramFilePath));

			while ((sCurrentLine = br.readLine()) != null) {
				
				sCurrentLine = sCurrentLine.toLowerCase();
				String []arr = sCurrentLine.split("\\s+");
				
				String token = arr[0].trim();
				int id = Integer.parseInt(arr[1].trim());
				tokenWithIds.put(token, id);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println(tokenWithIds.size());
		return tokenWithIds;
	}
	
	public Map<String, Integer> loadStemmedTokenizedUniGramsWithId() {
		String tokenizedUniGramFilePath = PhraseDirectories.tokenizedUniGramDir+PhraseFileNames.tokenizedUniGramFile;
		Map<String, Integer> tokenWithIds = new LinkedHashMap<String, Integer>();
		BufferedReader br = null;

		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(tokenizedUniGramFilePath));

			while ((sCurrentLine = br.readLine()) != null) {
				
				sCurrentLine = sCurrentLine.toLowerCase();
				String []arr = sCurrentLine.split("\\s+");
				
				String token = arr[0].trim();
				int id = Integer.parseInt(arr[1].trim());
				tokenWithIds.put(token, id);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("loadStemmedTokenizedUniGramsWithId="+tokenWithIds.size());
		return tokenWithIds;
	}
	
	public ArrayList<String> loadStemmedTokenizedUniGrams(){
		ArrayList<String> alUnis = new ArrayList<String>();
		
		String tokenizedUniGramFilePath = PhraseDirectories.tokenizedUniGramDir+PhraseFileNames.tokenizedUniGramFile;
		
		BufferedReader br = null;

		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(tokenizedUniGramFilePath));

			while ((sCurrentLine = br.readLine()) != null) {
				
				sCurrentLine = sCurrentLine.toLowerCase();
				String []arr = sCurrentLine.split("\\s+");
				
				String token = arr[0].trim();
				int id = Integer.parseInt(arr[1].trim());
				alUnis.add(token);
			}
			
			br.close();

		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("loadStemmedTokenizedUniGramsWithId="+alUnis.size());
		
		
		return alUnis;
	}
}
