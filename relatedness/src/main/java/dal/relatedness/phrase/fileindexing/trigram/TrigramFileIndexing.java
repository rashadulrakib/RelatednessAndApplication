package dal.relatedness.phrase.fileindexing.trigram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.utils.common.Util;

public class TrigramFileIndexing {
	public void IndexFilesByTokenPrefix() throws IOException{

		File [] files123 = Util.loadFiles(PhraseDirectories.trigramgramDir123);
		File [] files213 = Util.loadFiles(PhraseDirectories.trigramgramDir213);
		File [] files312 = Util.loadFiles(PhraseDirectories.trigramgramDir312);

		File [] files = Util.concatenate(files123, files213);
		files = Util.concatenate(files, files312);
		
		TreeMap<String, TreeSet<String>> tokensInFiles = new TreeMap<String, TreeSet<String>>(); 
		
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
					
					TreeSet<String>  fileList = new TreeSet<String>();
					fileList.add(file.getAbsolutePath());
					tokensInFiles.put(fch, fileList);
					
				}else{
					
					TreeSet<String> fileList = tokensInFiles.get(fch);
					fileList.add(file.getAbsolutePath());
					tokensInFiles.put(fch, fileList);
				}
			}
			
			br.close();
			System.out.println(tokensInFiles);
		}
		
		System.out.println(tokensInFiles);
		
		for(String tokensInFile: tokensInFiles.keySet() ){
			
			TreeSet<String> fileList = tokensInFiles.get(tokensInFile);
			Writer writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream(PhraseDirectories.triGramFileIndexingDir+tokensInFile), "utf-8"));
			
		    Iterator<String> itr = fileList.iterator();
		    
		    while(itr.hasNext()){
		    	writer.write(itr.next()+"\n");
		    }
		    
		    writer.close();
		    
		}
	}
	
}
