package dal.relatedness.phrase.fileindexing.fourgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.utils.common.Util;

public class FourgramFileIndexing {
	public void IndexFilesByTokenPrefix() throws IOException{

		File [] files1234 = Util.loadFiles(PhraseDirectories.fourgramDir1234);
		File [] files2314 = Util.loadFiles(PhraseDirectories.fourgramDir2314);
		File [] files3412 = Util.loadFiles(PhraseDirectories.fourgramDir3412);

		File [] files = Util.concatenate(files1234, files2314);
		files = Util.concatenate(files, files3412);
		
		Map<String, Map<String,Boolean>> tokensInFiles = new HashMap<String, Map<String,Boolean>>(); 
		
		for(File file: files){
			
			System.out.println(file.getAbsolutePath());
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.toLowerCase();
				
				String []arr = sCurrentLine.split("\\s+");
				
				if(arr[0].charAt(0)<'a' || arr[0].charAt(0) >'z' ) continue;
				
				String fch = Character.toString(arr[0].charAt(0));
				
				if(!tokensInFiles.containsKey(fch)){
					
					Map<String,Boolean> fileMap = new HashMap<String, Boolean>();
					fileMap.put(file.getAbsolutePath(), true);
					tokensInFiles.put(fch, fileMap);
					
				}else{
					
					Map<String,Boolean> fileMap = tokensInFiles.get(fch);
					fileMap.put(file.getAbsolutePath(), true);
					tokensInFiles.put(fch, fileMap);
				}
				
			}
			
			br.close();
			System.out.println(tokensInFiles);
		}
		
		System.out.println(tokensInFiles);
		
		for(String tokensInFile: tokensInFiles.keySet() ){
			
			Map<String, Boolean> indexFile = tokensInFiles.get(tokensInFile);
			Writer writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream(PhraseDirectories.fourGramFileIndexingDir+tokensInFile), "utf-8"));
			
		    for(String fourgramFileName: indexFile.keySet()){
		    	writer.write(fourgramFileName+"\n");
		    }
		    
		    writer.close();
		    
		}
	}
}
