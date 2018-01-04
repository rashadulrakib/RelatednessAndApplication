package dal.relatedness.phrase.utils.bigram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import dal.relatedness.phrase.constants.PhraseDirectories;

public class BigramUtil {
	
	public File [] loadBigramFiles(String firstIndexChar) throws IOException{
		String firstIndexCharFileName =  PhraseDirectories.biGramFileIndexingDir+firstIndexChar;
		
		ArrayList<String> bgFiles = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(firstIndexCharFileName));
		String sCurrentLine;
		 
		while ((sCurrentLine = br.readLine()) != null) {
			sCurrentLine = sCurrentLine.toLowerCase().trim();
			if(sCurrentLine.isEmpty()) continue;
			
			bgFiles.add(sCurrentLine);
		}
		
		br.close();
		
		File files[] = new File[bgFiles.size()];
		
		for(int i=0;i<bgFiles.size();i++){
			files[i] = new File(bgFiles.get(i));
		}
		
		return files;
	}
}
