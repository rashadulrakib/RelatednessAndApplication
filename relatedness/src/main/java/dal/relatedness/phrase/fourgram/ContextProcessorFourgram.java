package dal.relatedness.phrase.fourgram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dal.relatedness.phrase.constants.ContextFilterMode;
import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.utils.common.Util;
import dal.relatedness.phrase.utils.fourgram.FourgramUtil;

public class ContextProcessorFourgram {
	
	HashSet<String> stopWords;

	public void ProcessLeftRightMiddleContextForPhraseByRegx(){
		try{
			stopWords = Util.populateStopWords();
			Load4GramFilesRecursiveAndProcess(PhraseDirectories.fourGramIndexedDir);
			System.out.println("Finished ProcessLeftRightMiddleContextForPhraseByRegx");
		}
		catch(Exception e){
			
		}
	}

	private void Load4GramFilesRecursiveAndProcess(String dir) {
		try{
			File root = new File( dir );
	        File[] list = root.listFiles();

	        if (list == null) return;

	        if(AllFilesTypes(list)){
	        	
	        	HashSet<String> phraseAtLeftContextFiltered4gms = null; 
	        	HashSet<String> phraseAtRightContextFiltered4gms = null; 
	        	HashSet<String> phraseAtMiddleContextFiltered4gms = null; 
	        	
	        	String subDir="";
	        	String fileName="";
	        	
	        	for ( File f : list ) {
	        		String absFileName = f.getAbsoluteFile().toString();
	        		//System.out.println( "File:" + f.getAbsoluteFile() );
	        		
	        		subDir = FourgramUtil.Get4GramSubDirFromFilePath(absFileName);
	        		//System.out.println(Directories.fourGramIndexedProcessedContextDir+subDir);
	        		
	        		fileName = FourgramUtil.Get4GramFileName(absFileName);
	        		String fileSuffix = FourgramUtil.Get4GramFileSuffix(absFileName);
	        		
	        		if(fileSuffix.equals("1234")){
	        			phraseAtLeftContextFiltered4gms = ExtractFilteredContext4Grams(f, ".+\\s[a-zA-Z0-9]+", ContextFilterMode.SUFFIX);
	        		}
	        		else if(fileSuffix.equals("3412")){
	        			phraseAtRightContextFiltered4gms = ExtractFilteredContext4Grams(f, "[a-zA-Z0-9]+\\s.+", ContextFilterMode.PREFIX);
	        		}
	        		else if(fileSuffix.equals("2314")){
	        			phraseAtMiddleContextFiltered4gms = ExtractFilteredContext4Grams(f, "[a-zA-Z0-9]+\\s[a-zA-Z0-9]+", ContextFilterMode.INFIX);
	        		}
	        	}
	        	
	        	HashSet<String> mergedContexts4gms = new HashSet<String>();
	        	mergedContexts4gms.addAll(phraseAtLeftContextFiltered4gms);
	        	mergedContexts4gms.addAll(phraseAtRightContextFiltered4gms);
	        	mergedContexts4gms.addAll(phraseAtMiddleContextFiltered4gms);
	        	
	        	TreeMap<String, TreeMap<String, Integer>> bgPhraseWithContexts = ProcessContextsOfPhrases(mergedContexts4gms);
	        	
	        	File processedContextDir = new File(PhraseDirectories.fourGramIndexedProcessedContextDir+subDir);
	        	
	        	if(!processedContextDir.exists()){
	        		processedContextDir.mkdirs();
	        	}
	        	
	        	SavePhraseWithContexts(bgPhraseWithContexts, PhraseDirectories.fourGramIndexedProcessedContextDir+subDir+fileName);
	        	
	        	return;
	        }
	        
	        
	        for ( File f : list ) {
	            if ( f.isDirectory() ) {
	            	Load4GramFilesRecursiveAndProcess( f.getAbsolutePath() );
	                //System.out.println( "Dir:" + f.getAbsoluteFile() );
	            }
	        }
		}
		catch(Exception e){
			
		}
		
	}

	private void SavePhraseWithContexts(
			TreeMap<String, TreeMap<String, Integer>> bgPhraseWithContexts, String fileName) {
		
		try{
			PrintWriter pr = new PrintWriter(fileName);
			for(String bgKey: bgPhraseWithContexts.keySet()){
				TreeMap<String, Integer> contexts = bgPhraseWithContexts.get(bgKey);
				pr.print(bgKey+" "+contexts.size());
				for(String contextKey: contexts.keySet()){
					pr.print(" "+contextKey+" "+contexts.get(contextKey));
				}
				pr.println();
			}
			
			pr.close();
		}
		catch(Exception e){
		}
	}

	private TreeMap<String, TreeMap<String, Integer>> ProcessContextsOfPhrases(
			HashSet<String> mergedContexts4gms) {
		TreeMap<String, TreeMap<String, Integer>> bgPhraseWithContexts = new TreeMap<String, TreeMap<String, Integer>>();
		
		for(String mergedContext4gm: mergedContexts4gms){
			String arr[] = mergedContext4gm.split("\\s+");
			String phraseKey = arr[0]+" "+arr[1];
			String context =  arr[2]+" "+arr[3];
			long freq = Long.parseLong(arr[4]);
			if(freq> Integer.MAX_VALUE){
				System.out.println("INT_MAX_VAL="+phraseKey+","+freq);
				freq = Integer.MAX_VALUE;
			}
			
			if(!bgPhraseWithContexts.containsKey(phraseKey)){
				TreeMap<String, Integer> contextFreq = new TreeMap<String, Integer>();
				contextFreq.put(context, (int)freq);
				bgPhraseWithContexts.put(phraseKey, contextFreq);
			}
			else{
				TreeMap<String, Integer> contextFreq = bgPhraseWithContexts.get(phraseKey);
				if(!contextFreq.containsKey(context)){
					contextFreq.put(context, (int)freq);
				}
				else{
					contextFreq.put(context, contextFreq.get(context)+(int)freq);
				}
				bgPhraseWithContexts.put(phraseKey, contextFreq);
			}
		}
		
		return bgPhraseWithContexts;
	}

	private HashSet<String> ExtractFilteredContext4Grams(File file, String bgContextRegx, ContextFilterMode contextFilterMode) {
		HashSet<String> filteredContexts = new HashSet<String>();
		
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.toLowerCase();
				
				String [] arr = sCurrentLine.split("\\s+");
				
				if(arr.length!=5) continue;
				
				String context = arr[2]+" "+arr[3];
				
				Matcher mtr = Pattern.compile(bgContextRegx, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(context);
                if (!mtr.find()) continue; 
                
                if(contextFilterMode == ContextFilterMode.SUFFIX && stopWords.contains(arr[3])){
                	continue;
                }
                else if(contextFilterMode == ContextFilterMode.PREFIX && stopWords.contains(arr[2])){
                	continue;
                }
                else if(contextFilterMode == ContextFilterMode.INFIX 
                		&& stopWords.contains(arr[2]) 
                		&& stopWords.contains(arr[3])){
                	continue;
                }
                
                filteredContexts.add(sCurrentLine);
			}
			
			br.close();
			
		}catch(Exception e){
			
		}
		
		return filteredContexts;
	}

	private boolean AllFilesTypes(File[] list) {
		
		for ( File f : list ) {
            if ( f.isDirectory() ) {
            	return false;
            }
		}
		
		return true;
	}
	
}
