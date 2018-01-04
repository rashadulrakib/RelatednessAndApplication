package dal.relatedness.phrase.utils.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dal.relatedness.phrase.constants.PhraseFileNames;

public class Util {
	
	public static HashSet<String> populateStopWords() throws Exception {
		HashSet<String> stopWordsList = new HashSet<String>();
        try {
            BufferedReader brsw = new BufferedReader(new FileReader(new File(PhraseFileNames.StopWordFile)));
            String line;
            while ((line = brsw.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.isEmpty()) {
                    continue;
                }
                stopWordsList.add(line);
            }
            brsw.close();
        } catch (Exception e) {
            throw e;
        }
        return stopWordsList;
    }
	
	public static File []loadFiles(String dir){
		return new File(dir).listFiles();
	}
	
	public static boolean containsAnyNonAlpha(String str){
		
		for(int i=0;i<str.length();i++){
			if(str.charAt(i)<'a' || str.charAt(i)>'z')
				return true;
		}
		
		return false;
	}

	public static int getIndexFromSingleCharFile(String name) {
		if(name.charAt(0) == '_') return 26;
		int ind = name.charAt(0)-'a';
		return ind;
	}
	
	public static <T> T[] concatenate (T[] a, T[] b) {
	    int aLen = a.length;
	    int bLen = b.length;

	    @SuppressWarnings("unchecked")
	    T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen+bLen);
	    System.arraycopy(a, 0, c, 0, aLen);
	    System.arraycopy(b, 0, c, aLen, bLen);

	    return c;
	}
	
	public static boolean IsValidTokenByFirstChar(String s){
		
		s = s.toLowerCase();
		
		if(s.length()>=1){
			if((s.charAt(0)<'a' || s.charAt(0)>'z') 
					&& (s.charAt(0)<'0' || s.charAt(0)>'9')){
				return false;
			}
		}
		
//		if(s.length()>=2){
//			if(s.charAt(1)<'a' || s.charAt(1)>'z'){
//				return false;
//			}
//		}
		
		return true;
	}
	
	public static boolean IsValidTokenByAllChar(String str){
		
		String arr[] = str.split("\\s+");
		Pattern ptn = Pattern.compile("^[a-zA-Z]+$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
       
		for(String s: arr){
			Matcher mtr = ptn.matcher(s);
			if(!mtr.find()){
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean IsValidTokenByAllCharNumber(String str){
		
		String arr[] = str.split("\\s+");
		Pattern ptn = Pattern.compile("^[a-zA-Z0-9]+$", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
       
		for(String s: arr){
			Matcher mtr = ptn.matcher(s);
			if(!mtr.find()){
				return false;
			}
		}
		
		return true;
	}

	public static boolean AllCharSame(String stemWord) {

		String arr[] = stemWord.split("\\s+");
		
		for(String str: arr){
			char prevChar = str.charAt(0);
			
			for(int i=1;i<str.length();i++){
				if(str.charAt(i)!=prevChar){
					return false;
				}
				prevChar = str.charAt(i);
			}
			
		}
		
		return true;
	}

	public static List<List<Byte>> InitializeList(int size){
		List<List<Byte>> list = new ArrayList<List<Byte>>();
		
		for(int i=0;i<size;i++){
			list.add(null);
		}
		return list;
	}

	public static boolean IsValidInteger(String str) {
		
		try{
			Integer.parseInt(str);
		}
		catch(Exception e){
			return false;
		}
		
		return true;
	}
}
