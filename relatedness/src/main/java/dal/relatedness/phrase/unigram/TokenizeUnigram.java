package dal.relatedness.phrase.unigram;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;

public class TokenizeUnigram {
	public boolean tokenize(){
		try{
			Map<String, Long> tokenCount = populateTokenCount();
			WriteTokenIntoFile(tokenCount);	
		}catch(FileNotFoundException ex){
			return false;
		}
		return true;
	}
	
	private Map<String, Long> populateTokenCount(){
		Map<String, Long> tokenCount = new LinkedHashMap<String, Long>();
		
		String uniGramFilePath = PhraseDirectories.uniGramDir+PhraseFileNames.uniGramFile;
		BufferedReader br = null;

		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(uniGramFilePath));

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				sCurrentLine = sCurrentLine.toLowerCase();
				String []arr = sCurrentLine.split("\\s+");
				
				String token = arr[0].trim();
				long count = Long.parseLong(arr[1].trim());
				
				if(!tokenCount.containsKey(token)){
					tokenCount.put(token, count);
				}else{
					tokenCount.put(token, tokenCount.get(token)+count);
				}
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
		
		return tokenCount;
	}
	
	private void WriteTokenIntoFile(Map<String, Long> tokenCount) throws FileNotFoundException{
		
		String tokenizedUniGramFilePath = PhraseDirectories.tokenizedUniGramDir+PhraseFileNames.tokenizedUniGramFile;
		PrintWriter pr = new PrintWriter(tokenizedUniGramFilePath);
		int tokenId=0;
		for(String token: tokenCount.keySet()){
			pr.println(token+" "+(tokenId++)+" "+tokenCount.get(token));
		}
		pr.close();
	}
}
