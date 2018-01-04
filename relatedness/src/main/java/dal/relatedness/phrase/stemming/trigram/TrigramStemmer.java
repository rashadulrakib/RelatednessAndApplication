package dal.relatedness.phrase.stemming.trigram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dal.relatedness.phrase.constants.ContextFilterMode;
import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.phrase.utils.common.Util;
import dal.relatedness.phrase.utils.trigram.TrigramUtil;

public class TrigramStemmer {

	
	HashSet<String> stopWords;
	
	TreeMap<String, TreeMap<String, Integer>> wordContexts;
	
	public void ProcessContextAndStem3grams(){
		try{
			
			stopWords = Util.populateStopWords();
			
			ProcessAndStem3grams();
	    	System.out.println("StemPreocessed4grams finished");
	    	
	    	stopWords.clear();
	    	stopWords = null;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void ProcessAndStem3grams() {
		try{
			
			for(char ch='a';ch<='z';ch++){
				
				wordContexts = new TreeMap<String, TreeMap<String,Integer>>();
				System.out.println("Processing="+ch);
				List<String> files = GetIndexedFiles(ch); 
				
				ProcessAndStemFromFiles(files, ch);
				
				SaveStemAndProcessedContextsWithWords(ch);
				
				files.clear();
				files=null;
				
				wordContexts.clear();
				wordContexts = null;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void SaveStemAndProcessedContextsWithWords(char ch) {
		try{
			String filePath = PhraseDirectories.triGramIndexedProcessedContextStemDir+String.valueOf(ch);
			
			System.out.println("Writing to "+ filePath);
	
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
			
			for(String stemKey: wordContexts.keySet()){
				TreeMap<String, Integer> contexts = wordContexts.get(stemKey);
				
				bufferedWriter.write(stemKey+" "+contexts.size());
				
				for(String contextKey: contexts.keySet()){
					bufferedWriter.write(" "+ contextKey+" "+contexts.get(contextKey));
				}
				bufferedWriter.write("\n");
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
				//String fileOrder = TrigramUtil.GetTriGramOrderFromFilePath(filePath);
				ContextFilterMode contxtFltMode = TrigramUtil.GetTriGramContextFilterMode(filePath);
				String contextRegx = TrigramUtil.GetContextRegx(contxtFltMode);

				while ((sCurrentLine = br.readLine()) != null) {
					if(sCurrentLine.isEmpty()) continue;
					
					sCurrentLine = sCurrentLine.toLowerCase();
					
					String arr[] = sCurrentLine.split("\\s+");
					if(arr.length!=4) continue;
					
					if(!Util.IsValidTokenByAllCharNumber(arr[0])) {
						continue;
					}
					
					String stemWord = StemmingUtil.stemPhrase(arr[0]);
					
					if(stemWord.length()<2) continue;
					
					if(!Util.IsValidInteger(arr[3])){
						continue;
					}
					
					if(stemWord.charAt(0)!=ch) continue;
					
					String context = arr[1]+" "+arr[2];
					boolean isValidContext = IsValidFilteredContextBasedOnFileSuffix(context, contextRegx, contxtFltMode);
					if(!isValidContext) continue;
					
					long freq = Long.parseLong(arr[3]);
					if(freq> Integer.MAX_VALUE){
						System.out.println("INT_MAX_VAL="+stemWord+","+freq);
						freq = Integer.MAX_VALUE;
					}
					
					if(!wordContexts.containsKey(stemWord)){
						TreeMap<String, Integer> contextFreq = new TreeMap<String, Integer>();
						contextFreq.put(context, (int)freq);
						wordContexts.put(stemWord, contextFreq);
					}
					else{
						TreeMap<String, Integer> contextFreq = wordContexts.get(stemWord);
						if(!contextFreq.containsKey(context)){
							contextFreq.put(context, (int)freq);
						}
						else{
							contextFreq.put(context, contextFreq.get(context)+(int)freq);
						}
						wordContexts.put(stemWord, contextFreq);
					}
					
				}
				
				br.close();
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private boolean IsValidFilteredContextBasedOnFileSuffix(String context, String contextRegx, ContextFilterMode contxtFltMode) {
		
		Matcher mtr = Pattern.compile(contextRegx, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(context);
        if (!mtr.find()) return false; 
		
        String bgArr [] = context.split("\\s+");
        
        if(bgArr.length!=2) return false;
        
        if(contxtFltMode == ContextFilterMode.SUFFIX && stopWords.contains(bgArr[1])){
        	return false;
        }
        else if(contxtFltMode == ContextFilterMode.PREFIX && stopWords.contains(bgArr[0])){
        	return false;
        }
        else if(contxtFltMode == ContextFilterMode.INFIX 
        		&& stopWords.contains(bgArr[0]) 
        		&& stopWords.contains(bgArr[1])){
        	return false;
        }
		
		return true;
	}

	private List<String> GetIndexedFiles(char ch) throws IOException {
		List<String> files = new ArrayList<String>();
		
		String filePath = PhraseDirectories.triGramFileIndexingDir+String.valueOf(ch);
		
		String sCurrentLine;
		BufferedReader br = new BufferedReader(new FileReader(filePath));

		while ((sCurrentLine = br.readLine()) != null) {
			
			if(sCurrentLine.isEmpty()) continue;
			files.add(sCurrentLine);
		}
		
		br.close();
		
		return files;
	}
	
}
