package dal.relatedness.phrase.fileindexing.bigram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.utils.common.Util;

public class BigramFileIndexing {
	
	private final String nonAlpha="_";
	
	public void IndexFilesByTokenPrefix() throws IOException{

		File [] files = Util.loadFiles(PhraseDirectories.biGramDir);

		LinkedHashMap<String, LinkedHashMap<String,Boolean>> tokensInFiles = new LinkedHashMap<String, LinkedHashMap<String,Boolean>>(); 
		
		for(File file: files){
			
			System.out.println(file.getAbsolutePath());
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.toLowerCase();
				
				String []arr = sCurrentLine.split("\\s+");
				
//				for(char ch = 'a' ; ch <= 'z' ; ch++ ){
//	         		String firstIndexChar = Character.toString(ch);
//	         		if(arr[0].indexOf(firstIndexChar, 0)==0){
//						if(!tokensInFiles.containsKey(firstIndexChar)){
//							Map<String, Boolean> indexFile = new HashMap<String, Boolean>();
//							indexFile.put(file.getAbsolutePath(), true);
//							tokensInFiles.put(firstIndexChar, indexFile);
//						}else{
//							Map<String, Boolean> indexFile = tokensInFiles.get(firstIndexChar);
//							indexFile.put(file.getAbsolutePath(), true);
//							tokensInFiles.put(firstIndexChar, indexFile);
//						}
//						
//						break;
//					}
//				}
				
				if(arr[0].charAt(0)>='a' && arr[0].charAt(0)<='z'){
					String firstIndexChar = Character.toString(arr[0].charAt(0));
					if(!tokensInFiles.containsKey(firstIndexChar)){
						LinkedHashMap<String, Boolean> indexFile = new LinkedHashMap<String, Boolean>();
						indexFile.put(file.getAbsolutePath(), true);
						tokensInFiles.put(firstIndexChar, indexFile);
					}else{
						LinkedHashMap<String, Boolean> indexFile = tokensInFiles.get(firstIndexChar);
						indexFile.put(file.getAbsolutePath(), true);
						tokensInFiles.put(firstIndexChar, indexFile);
					}
				}
				else //(arr[0].charAt(0)<'a' || arr[0].charAt(0)>'z'){
				{
					if(!tokensInFiles.containsKey(nonAlpha)){
						LinkedHashMap<String, Boolean> indexFile = new LinkedHashMap<String, Boolean>();
						indexFile.put(file.getAbsolutePath(), true);
						tokensInFiles.put(nonAlpha, indexFile);
					}else{
						LinkedHashMap<String, Boolean> indexFile = tokensInFiles.get(nonAlpha);
						indexFile.put(file.getAbsolutePath(), true);
						tokensInFiles.put(nonAlpha, indexFile);
					}
				}
				
				
			}
			
			br.close();
			System.out.println(tokensInFiles);
		}
		
		System.out.println(tokensInFiles);
		
		for(String tokensInFile: tokensInFiles.keySet() ){
			LinkedHashMap<String, Boolean> indexFile = tokensInFiles.get(tokensInFile);
			Writer writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream(PhraseDirectories.biGramFileIndexingDir+tokensInFile), "utf-8"));
			
		    for(String bigramFileName: indexFile.keySet()){
		    	writer.write(bigramFileName+"\n");
		    }
		    
		    writer.close();
		}
	}
}
