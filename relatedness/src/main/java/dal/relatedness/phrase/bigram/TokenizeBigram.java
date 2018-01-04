package dal.relatedness.phrase.bigram;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.entities.bigram.BigramKey;
import dal.relatedness.phrase.unigram.LoadUnigram;
import dal.relatedness.phrase.utils.bigram.BigramUtil;
import dal.relatedness.phrase.utils.common.Base62;
import dal.relatedness.phrase.utils.common.Util;

public class TokenizeBigram {
	
	public void tokenizeByFilteredId() throws IOException{
		File files [] = new File(PhraseDirectories.tokenizedBiGramDir).listFiles();
		
		for(File file: files){
			
			String bgonlyusedin4gFile = PhraseDirectories.googleNgramDir + "bg-only-used-in-4g-"+file.getName();
			
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(bgonlyusedin4gFile));

			Map<Integer, Boolean> bgonlyusedin4gList = new HashMap<Integer, Boolean>();
			
			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.trim();
				if(sCurrentLine.isEmpty()) continue;
				
				bgonlyusedin4gList.put(Integer.parseInt(sCurrentLine), true);
			}
			
			br.close();
			
			FileInputStream fileIn = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fileIn);
			
			int numberOfRecords = in.readInt();
			System.out.println("total tokens="+numberOfRecords+","+file.getAbsolutePath()+","+new Date().toString());
			
			List<Integer[]> filtered = new ArrayList<Integer[]>();
			
			for(int i=0;i<numberOfRecords;i++){
				int keyUni1 = in.readInt();
				int keyUni2 = in.readInt();
				int tokenIndex = in.readInt();
				int frequency = in.readInt();
				
				if(!bgonlyusedin4gList.containsKey(tokenIndex)){
					continue;
				}
				
				Integer[] arr = new Integer[3];
				
				arr[0] = keyUni1;
				arr[1] = keyUni2;
				arr[2] = tokenIndex;
				
				filtered.add(arr);
				
			}
			
			in.close();
			fileIn.close();
			
			String tokenizedBiGramFile= PhraseDirectories.moreTokenizedBiGramDir+ file.getName();
			FileOutputStream fileOut = new FileOutputStream(tokenizedBiGramFile);
			DataOutputStream  out = new DataOutputStream (fileOut);
			
			out.writeInt(filtered.size());
			
			for(Integer[] arr: filtered){

				out.writeInt(arr[0]);
				out.writeInt(arr[1]);
				out.writeInt(arr[2]);
			}
			
			
			System.out.println(tokenizedBiGramFile);
			
			out.close();
			fileOut.close();
		}
	}
	
	public void tokenizeById() throws IOException{
		File files [] = new File(PhraseDirectories.tokenizedBiGramDir).listFiles();
		
		for(File file: files){
			
			String tokenizedBiGramFile= PhraseDirectories.moreTokenizedBiGramDir+ file.getName();
			FileOutputStream fileOut = new FileOutputStream(tokenizedBiGramFile);
			DataOutputStream  out = new DataOutputStream (fileOut);
			
			FileInputStream fileIn = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fileIn);
			
			int numberOfRecords = in.readInt();
			System.out.println("total tokens="+numberOfRecords+","+file.getAbsolutePath()+","+new Date().toString());
			
			out.writeInt(numberOfRecords);
			System.out.println(tokenizedBiGramFile);
			
			for(int i=0;i<numberOfRecords;i++){
				int keyUni1 = in.readInt();
				int keyUni2 = in.readInt();
				int tokenIndex = in.readInt();
				int frequency = in.readInt();
				
				out.writeInt(keyUni1);
				out.writeInt(keyUni2);
				out.writeInt(tokenIndex);
			}
			
			in.close();
			fileIn.close();
			
			out.close();
			fileOut.close();
		}
	}
	
	public boolean tokenize(){
		try{
			
			Map<String, Integer> tokenWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();

			char ch;
			int totalToken=173213354;
      		for( ch = '_' ; ch <= '_' ; ch++ ){
         		String firstIndexChar = Character.toString(ch);
				System.out.println(firstIndexChar);	
			
				totalToken = tokenizeBigramsToFile(totalToken,firstIndexChar , tokenWithIds);
				System.out.println(totalToken);
			}	

			
			//int totalToken = tokenizeBigramsToFile(173213354,"nonAlpha" , tokenWithIds); //173213354
			System.out.println(totalToken);	
			//total tokens=174192864
			
		}catch(Exception e){
			return false;
		}
		
		return true;
	}

	public void TokenizeBySumId() throws Exception{
		try{
			
			Map<String, Integer> unigramTokenWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();

			char ch;
			String firstIndexChar;
			long tokenIndex=0;
			long totalToken;
			
      		for( ch = 'a' ; ch <= 'a' ; ch++ ){
         		firstIndexChar = Character.toString(ch);
				System.out.println(firstIndexChar);	
			
				totalToken = tokenizeBigramsToFileBySumId(tokenIndex,firstIndexChar , unigramTokenWithIds);
				tokenIndex = tokenIndex + totalToken;
				System.out.println(totalToken);
			}
      		
//      		ch='o';
//      		firstIndexChar = Character.toString(ch);
//      		System.out.println(firstIndexChar);	
//      		totalToken = tokenizeBigramsToFileBySumId(tokenIndex,firstIndexChar , unigramTokenWithIds, true);
//      		tokenIndex = tokenIndex + totalToken;
//			System.out.println(totalToken);
			
//			for( ch = 'p' ; ch <= 'z' ; ch++ ){
//         		firstIndexChar = Character.toString(ch);
//				System.out.println(firstIndexChar);	
//			
//				totalToken = tokenizeBigramsToFileBySumId(tokenIndex,firstIndexChar , unigramTokenWithIds, true);
//				tokenIndex = tokenIndex + totalToken;
//				System.out.println(totalToken);
//			}
//      		
//      		ch = '_';
//      		firstIndexChar = Character.toString(ch);
//      		System.out.println(firstIndexChar);	
//      		totalToken = tokenizeBigramsToFileBySumId(tokenIndex,firstIndexChar , unigramTokenWithIds, true);
//      		tokenIndex = tokenIndex + totalToken;
//			System.out.println(totalToken);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	public void TokenizeByDoubleKey(){
		Map<String, Integer> unigramTokenWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();

		char ch;
		String firstIndexChar;
		long tokenIndex=0;
		long totalToken;
		
  		for( ch = 'a' ; ch <= 'z' ; ch++ ){
     		firstIndexChar = Character.toString(ch);
			System.out.println(firstIndexChar);	
		
			totalToken = tokenizeBigramsToFileByDoubleKey(tokenIndex,firstIndexChar , unigramTokenWithIds);
			tokenIndex = tokenIndex + totalToken;
			System.out.println(totalToken);
		}
  		
		ch = '_';
		firstIndexChar = Character.toString(ch);
		System.out.println(firstIndexChar);	
		totalToken = tokenizeBigramsToFileByDoubleKey(tokenIndex,firstIndexChar , unigramTokenWithIds);
		tokenIndex = tokenIndex + totalToken;
		System.out.println(totalToken);
	}
	
	private long tokenizeBigramsToFileByDoubleKey(long tokenIndex,
			String firstIndexChar, Map<String, Integer> unigramTokenWithIds) {
		
		int totalToken=0;
		
		try{
			
			File [] files = new BigramUtil().loadBigramFiles(firstIndexChar);
			System.out.println("start token inddex="+tokenIndex);
			System.out.println(files.length+","+PhraseDirectories.biGramDir);
			
			TreeMap<String, Integer> bgTokenWithCountsInt = new TreeMap<String, Integer>(); 
					
			long maxFreq =  0;
			String maxBg = "";
			
			for(int i=0;i<files.length;i++){
				BufferedReader br = new BufferedReader(new FileReader(files[i]));
				String sCurrentLine;
				
				while ((sCurrentLine = br.readLine()) != null) {
					sCurrentLine = sCurrentLine.toLowerCase();
					
					if(sCurrentLine.isEmpty()) continue;
					
					String []arr = sCurrentLine.split("\\s+");
					
					String bgKey = arr[0]+" "+arr[1];
					long freq = Long.parseLong(arr[2]);
					
					if(firstIndexChar.charAt(0)>='a' && firstIndexChar.charAt(0)<='z'){
						if(sCurrentLine.charAt(0)!=firstIndexChar.charAt(0)) continue;
					}
					else{
						if(sCurrentLine.charAt(0)>='a' && sCurrentLine.charAt(0)<='z') continue;
						
						if(!Util.IsValidTokenByFirstChar(arr[0]) &&
								!Util.IsValidTokenByFirstChar(arr[1])){
							
							//System.out.println("discard="+bgKey+","+freq);
							
							continue;
						}
					}
					
					if(freq>Integer.MAX_VALUE){
						System.out.println("SINGLE Max_Int Bg="+bgKey+ ",MAX_INT Freq="+freq);
					}
					
					if(freq>maxFreq){ 
						maxFreq = freq;
						maxBg = bgKey;
					}
						
					if(!bgTokenWithCountsInt.containsKey(bgKey)){
						int freqVal = freq > Integer.MAX_VALUE ? Integer.MAX_VALUE: (int)freq;
						bgTokenWithCountsInt.put(bgKey, freqVal);
					}
					else{
						long freqSum =  bgTokenWithCountsInt.get(bgKey) + freq;
						int freqVal = freqSum > Integer.MAX_VALUE ? Integer.MAX_VALUE: (int)freqSum;
						bgTokenWithCountsInt.put(bgKey, freqVal);
					}
				}
				
				br.close();
			}
			
			System.out.println("Max Bg="+maxBg+",Max freq="+maxFreq);
			
			String tokenizedBiGramFile= PhraseDirectories.tokenizedBiGramDir+ firstIndexChar;
			FileOutputStream fileOut = new FileOutputStream(tokenizedBiGramFile);
			DataOutputStream  out = new DataOutputStream (fileOut);
			
			
			totalToken = bgTokenWithCountsInt.size();
			
			out.writeInt(bgTokenWithCountsInt.size());
			
			int i=0;
			for(String bgKey:bgTokenWithCountsInt.keySet()){
				
				String bgKeyArr [] = bgKey.split("\\s+");
				String ugKey1 = bgKeyArr[0];
				String ugKey2 = bgKeyArr[1];
				
				int ugId1 =  unigramTokenWithIds.get(ugKey1);
				int ugId2 =  unigramTokenWithIds.get(ugKey2);
				int bgFreq = bgTokenWithCountsInt.get(bgKey);
				
				int bgId = (int)tokenIndex+i;
				
				out.writeInt(ugId1);
				out.writeInt(ugId2);
				out.writeInt(bgId);
				out.writeInt(bgFreq);
				
				i++;
			}
			
			bgTokenWithCountsInt.clear();	
			
			
			fileOut.close();
			out.close();
			
		}
		catch(Exception e){
			
		}
		
		
		return totalToken;
	}

	private long tokenizeBigramsToFileBySumId(long tokenIndex,
			String firstIndexChar, Map<String, Integer> unigramTokenWithIds) throws Exception {
		
		int totalToken;
		
		try{
			File [] files = new BigramUtil().loadBigramFiles(firstIndexChar);
			System.out.println("start token inddex="+tokenIndex);
			System.out.println(files.length+","+PhraseDirectories.biGramDir);
			
//			TreeMap<String, Long> bgTokenWithCountsLong= null;
//			TreeMap<String, Integer> bgTokenWithCountsInt = null;
//			
			HashMap<BigramKey, Integer> bgTokenDoubleKeys = new HashMap<BigramKey, Integer>();
			
//			if(isCountInt){
//				bgTokenWithCountsInt = new TreeMap<String, Integer>();
//			}
//			else{
//				bgTokenWithCountsLong = new TreeMap<String, Long>();
//			}
			
			long maxFreq =  0;
			String maxBg = "";
			
			
			for(int i=0;i<files.length;i++){
				BufferedReader br = new BufferedReader(new FileReader(files[i]));
				String sCurrentLine;
				
				while ((sCurrentLine = br.readLine()) != null) {
					sCurrentLine = sCurrentLine.toLowerCase();
					
					if(sCurrentLine.isEmpty()) continue;
					
					String []arr = sCurrentLine.split("\\s+");
					
					String bgKey = arr[0]+" "+arr[1];
					long freq = Long.parseLong(arr[2]);
					BigramKey bgDoubleKey = new BigramKey(unigramTokenWithIds.get(arr[0]),
							unigramTokenWithIds.get(arr[1]));
					
					if(firstIndexChar.charAt(0)>='a' && firstIndexChar.charAt(0)<='z'){
						if(sCurrentLine.charAt(0)!=firstIndexChar.charAt(0)) continue;
					}
					else{
						if(sCurrentLine.charAt(0)>='a' && sCurrentLine.charAt(0)<='z') continue;
						
						if(!Util.IsValidTokenByFirstChar(arr[0]) &&
								!Util.IsValidTokenByFirstChar(arr[1])){
							
							//System.out.println("discard="+bgKey+","+freq);
							
							continue;
						}
					}
					
					if(freq>Integer.MAX_VALUE){
						System.out.println("SINGLE Max_Int Bg="+bgKey+ ",MAX_INT Freq="+freq);
					}
					
					if(freq>maxFreq){ 
						maxFreq = freq;
						maxBg = bgKey;
					}
					
//					if(bgTokenWithCountsLong!=null){
//						if(!bgTokenWithCountsLong.containsKey(bgKey)){
//							bgTokenWithCountsLong.put(bgKey, freq);
//						}
//						else{
//							long freqSum =  bgTokenWithCountsLong.get(bgKey) + freq;
//							bgTokenWithCountsLong.put(bgKey, freqSum);
//						}
//						
//						//long finalFreq = bgTokenWithCountsLong.get(bgKey);
//						
////						if(finalFreq>Integer.MAX_VALUE){
////							System.out.println("FINAL Max_Int Bg="+bgKey+ ",MAX_INT Freq="+finalFreq);
////						}
////						
////						if(finalFreq>maxFreq){ 
////							maxFreq = finalFreq;
////							maxBg = bgKey;
////						}
//					}
					//else
//					{
//						if(!bgTokenWithCountsInt.containsKey(bgKey)){
//							int freqVal = freq > Integer.MAX_VALUE ? Integer.MAX_VALUE: (int)freq;
//							bgTokenWithCountsInt.put(bgKey, freqVal);
//						}
//						else{
//							long freqSum =  bgTokenWithCountsInt.get(bgKey) + freq;
//							int freqVal = freqSum > Integer.MAX_VALUE ? Integer.MAX_VALUE: (int)freqSum;
//							bgTokenWithCountsInt.put(bgKey, freqVal);
//						}
//						
//						//long finalFreq = bgTokenWithCountsInt.get(bgKey);
//						
////						if(finalFreq>Integer.MAX_VALUE){
////							System.out.println("FINAL Max_Int Bg="+bgKey+ ",MAX_INT Freq="+finalFreq);
////						}
////						
////						if(finalFreq>maxFreq){ 
////							maxFreq = finalFreq;
////							maxBg = bgKey;
////						}
//						
//					}
					
					if(!bgTokenDoubleKeys.containsKey(bgDoubleKey)){
						int freqVal = freq > Integer.MAX_VALUE ? Integer.MAX_VALUE: (int)freq;
						bgTokenDoubleKeys.put(bgDoubleKey, freqVal);
					}
					else{
						long freqSum =  bgTokenDoubleKeys.get(bgDoubleKey) + freq;
						int freqVal = freqSum > Integer.MAX_VALUE ? Integer.MAX_VALUE: (int)freqSum;
						bgTokenDoubleKeys.put(bgDoubleKey, freqVal);
					}
					
					
				}
				
				br.close();
			}
			
			System.out.println("Max Bg="+maxBg+",Max freq="+maxFreq);
			
			String tokenizedBiGramFile= PhraseDirectories.tokenizedBiGramDir+ firstIndexChar;
			FileOutputStream fileOut = new FileOutputStream(tokenizedBiGramFile);
			DataOutputStream  out = new DataOutputStream (fileOut);
			
			//HashMap<Integer, Boolean> hmDuplicateKey = new HashMap<Integer, Boolean>();
			HashSet<BigramKey> hmDuplicateKey = new HashSet<BigramKey>();
			
//			if(bgTokenWithCountsLong!=null){
//				totalToken = bgTokenWithCountsLong.keySet().size();
//				
//				out.writeInt(bgTokenWithCountsLong.keySet().size());
//				
//				int i=0;
//				for(String bgKey:bgTokenWithCountsLong.keySet()){
//					
//					String bgKeyArr [] = bgKey.split("\\s+");
//					String ugKey1 = bgKeyArr[0];
//					String ugKey2 = bgKeyArr[1];
//					
//					int ugId1 =  unigramTokenWithIds.get(ugKey1);
//					int ugId2 =  unigramTokenWithIds.get(ugKey2);
//					long bgFreq = bgTokenWithCountsLong.get(bgKey);
//					
//					//int bgKeyHash = (ugId1+1)+211*(ugId2+1);
//					int bgKeyHash = bgKey.hashCode();
//
//					
//					int bgId = (int)tokenIndex+i;
//					
//					out.writeInt(bgKeyHash);
//					out.writeInt(bgId);
//					out.writeLong(bgFreq);
//					
//					i++;
//				}
//				
//				bgTokenWithCountsLong.clear();
//			}
//			else
//			{
//				totalToken = bgTokenWithCountsInt.keySet().size();
//				
//				out.writeInt(bgTokenWithCountsInt.keySet().size());
//				
//				
//				int i=0;
//				for(String bgKey:bgTokenWithCountsInt.keySet()){
//					
//					String bgKeyArr [] = bgKey.split("\\s+");
//					String ugKey1 = bgKeyArr[0];
//					String ugKey2 = bgKeyArr[1];
//					
//					int ugId1 =  unigramTokenWithIds.get(ugKey1);
//					int ugId2 =  unigramTokenWithIds.get(ugKey2);
//					int bgFreq = bgTokenWithCountsInt.get(bgKey);
//					
//					int bgKeyHash = hashCode(ugKey1, ugKey2);//bgKey.hashCode();
//					int bgId = (int)tokenIndex+i;
//					
////					if(hmDuplicateKey.containsKey(bgKeyHash)){
////						System.out.println("key exist="+bgKeyHash+", str key="+bgKey);
////					}
////				
////					hmDuplicateKey.put(bgKeyHash, true);
//					
//					out.writeInt(bgKeyHash);
//					out.writeInt(bgId);
//					out.writeInt(bgFreq);
//					
//					i++;
//				}
//				
//				System.out.println("Total bgTokenWithCountsInt="+bgTokenWithCountsInt.keySet().size());
//				
//				bgTokenWithCountsInt.clear();
//			}
			
			totalToken = bgTokenDoubleKeys.keySet().size();
			
			out.writeInt(bgTokenDoubleKeys.keySet().size());
			
			int i=0;
			for(BigramKey bgKey:bgTokenDoubleKeys.keySet()){
				
				int bgFreq = bgTokenDoubleKeys.get(bgKey);
				
				int bgKeyHash = bgKey.hashCode();
				int bgId = (int)tokenIndex+i;
				
				if(hmDuplicateKey.contains(bgKey)){
					System.out.println("key exist="+bgKeyHash+", str key="+bgKey);
				}
			
				hmDuplicateKey.add(bgKey);
				
				out.writeInt(bgKeyHash);
				out.writeInt(bgId);
				out.writeInt(bgFreq);
				
				i++;
			}
			
			System.out.println("Total bgTokenWithCountsInt="+bgTokenDoubleKeys.keySet().size());
			
			bgTokenDoubleKeys.clear();
			
			out.close();
	        fileOut.close();
			
		}
		catch(Exception e){
			throw e;
		}
		
		return totalToken;
	}
	
//	private int hashCode(String keyUni1, String keyUni2) {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((keyUni1 == null) ? 0 : keyUni1.hashCode());
//		result = prime * result + ((keyUni2 == null) ? 0 : keyUni2.hashCode());
//		return result;
//	}
	
	
	////
		///does not work
		////
	public void TokenizeByBase62Ids() throws Exception{
		try{
			
			Map<String, Integer> unigramTokenWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();

			char ch;
			String firstIndexChar;
			long tokenIndex=0;
			long totalToken;
			
      		for( ch = 'a' ; ch <= 'b' ; ch++ ){
         		firstIndexChar = Character.toString(ch);
				System.out.println(firstIndexChar);	
			
				totalToken = tokenizeBigramsToFileByBase62Ids(tokenIndex,firstIndexChar , unigramTokenWithIds);
				tokenIndex = tokenIndex + totalToken;
				System.out.println(totalToken);
			}
      		
//      		ch='o';
//      		firstIndexChar = Character.toString(ch);
//      		System.out.println(firstIndexChar);	
//      		totalToken = tokenizeBigramsToFileBySumId(tokenIndex,firstIndexChar , unigramTokenWithIds, true);
//      		tokenIndex = tokenIndex + totalToken;
//			System.out.println(totalToken);
			
//			for( ch = 'p' ; ch <= 'z' ; ch++ ){
//         		firstIndexChar = Character.toString(ch);
//				System.out.println(firstIndexChar);	
//			
//				totalToken = tokenizeBigramsToFileBySumId(tokenIndex,firstIndexChar , unigramTokenWithIds, true);
//				tokenIndex = tokenIndex + totalToken;
//				System.out.println(totalToken);
//			}
//      		
//      		ch = '_';
//      		firstIndexChar = Character.toString(ch);
//      		System.out.println(firstIndexChar);	
//      		totalToken = tokenizeBigramsToFileBySumId(tokenIndex,firstIndexChar , unigramTokenWithIds, true);
//      		tokenIndex = tokenIndex + totalToken;
//			System.out.println(totalToken);
		}
		catch(Exception e){
			throw e;
		}
	}
	
	////
	///does not work
	////
	private long tokenizeBigramsToFileByBase62Ids(long tokenIndex,
			String firstIndexChar, Map<String, Integer> unigramTokenWithIds) throws Exception {
		
		int totalToken;
		
		try{
			File [] files = new BigramUtil().loadBigramFiles(firstIndexChar);
			System.out.println("start token inddex="+tokenIndex);
			System.out.println(files.length+","+PhraseDirectories.biGramDir);
			
			TreeMap<String, Integer> bgTokenWithCountsInt = new TreeMap<String, Integer>();
			
			
			long maxFreq =  0;
			String maxBg = "";
			
			
			for(int i=0;i<files.length;i++){
				BufferedReader br = new BufferedReader(new FileReader(files[i]));
				String sCurrentLine;
				
				while ((sCurrentLine = br.readLine()) != null) {
					sCurrentLine = sCurrentLine.toLowerCase();
					
					if(sCurrentLine.isEmpty()) continue;
					
					String []arr = sCurrentLine.split("\\s+");
					
					String bgKey = arr[0]+" "+arr[1];
					long freq = Long.parseLong(arr[2]);
					
					if(firstIndexChar.charAt(0)>='a' && firstIndexChar.charAt(0)<='z'){
						if(sCurrentLine.charAt(0)!=firstIndexChar.charAt(0)) continue;
					}
					else{
						if(sCurrentLine.charAt(0)>='a' && sCurrentLine.charAt(0)<='z') continue;
						
						if(!Util.IsValidTokenByFirstChar(arr[0]) &&
								!Util.IsValidTokenByFirstChar(arr[1])){
							
							//System.out.println("discard="+bgKey+","+freq);
							
							continue;
						}
					}
					
					if(freq>Integer.MAX_VALUE){
						System.out.println("SINGLE Max_Int Bg="+bgKey+ ",MAX_INT Freq="+freq);
					}
					
					if(freq>maxFreq){ 
						maxFreq = freq;
						maxBg = bgKey;
					}
						
					if(!bgTokenWithCountsInt.containsKey(bgKey)){
						int freqVal = freq > Integer.MAX_VALUE ? Integer.MAX_VALUE: (int)freq;
						bgTokenWithCountsInt.put(bgKey, freqVal);
					}
					else{
						long freqSum =  bgTokenWithCountsInt.get(bgKey) + freq;
						int freqVal = freqSum > Integer.MAX_VALUE ? Integer.MAX_VALUE: (int)freqSum;
						bgTokenWithCountsInt.put(bgKey, freqVal);
					}
				}
				
				br.close();
			}
			
			System.out.println("Max Bg="+maxBg+",Max freq="+maxFreq);
			
			String tokenizedBiGramFile= PhraseDirectories.tokenizedBiGramDir+ firstIndexChar;
			
			PrintWriter pr = new PrintWriter(tokenizedBiGramFile);
			
			totalToken = bgTokenWithCountsInt.size();
			
			pr.println(bgTokenWithCountsInt.size());
			
			int i=0;
			for(String bgKey:bgTokenWithCountsInt.keySet()){
				
				String bgKeyArr [] = bgKey.split("\\s+");
				String ugKey1 = bgKeyArr[0];
				String ugKey2 = bgKeyArr[1];
				
				int ugId1 =  unigramTokenWithIds.get(ugKey1);
				int ugId2 =  unigramTokenWithIds.get(ugKey2);
				int bgFreq = bgTokenWithCountsInt.get(bgKey);
				
				String bgKeyHash = Base62.fromBase10(ugId1)+Base62.fromBase10(ugId2);
				int bgId = (int)tokenIndex+i;
				
				pr.print(bgKeyHash+" ");
				pr.print(bgId+" ");
				pr.println(bgFreq);
				
				i++;
			}
			
			pr.close();
			
			bgTokenWithCountsInt.clear();
			
			
			
			
		}
		catch(Exception e){
			throw e;
		}
		
		return totalToken;
	}

	private int tokenizeBigramsToFile(int tokenIndex,String firstIndexChar, Map<String, Integer> tokenWithIds) throws IOException {
		
		File [] files = new BigramUtil().loadBigramFiles(firstIndexChar);
		
		Map<BigramKey, Integer> bgTokenWithCounts = new LinkedHashMap<BigramKey, Integer>();
		System.out.println(files.length+","+PhraseDirectories.biGramDir);
		for(int i=0;i<files.length;i++){
		//for(File file: files){
			BufferedReader br = new BufferedReader(new FileReader(files[i]));
			String sCurrentLine;
			 
			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.toLowerCase();
				
				String []arr = sCurrentLine.split("\\s+");
				
				if(!firstIndexChar.equals("_")){
					if(sCurrentLine.indexOf(firstIndexChar,0)!=0) continue;
				}else{
					char c = sCurrentLine.charAt(0);
					if(c >= 'a' && c <= 'z') continue;
					if(Util.containsAnyNonAlpha(arr[1])) continue; 
				}
				
				int ug1Id = tokenWithIds.get(arr[0].trim());
				int ug2Id = tokenWithIds.get(arr[1].trim());
				long fr = Long.parseLong(arr[2].trim());
				
				int freq= (fr>=Integer.MAX_VALUE) ? Integer.MAX_VALUE: (int)fr;
				BigramKey bgKey = new BigramKey(ug1Id,ug2Id);
				
				if(!bgTokenWithCounts.containsKey(bgKey)){
					bgTokenWithCounts.put(bgKey, freq);
				}else{
					long frr =  bgTokenWithCounts.get(bgKey) + freq;
					bgTokenWithCounts.put(bgKey, frr>=Integer.MAX_VALUE ? Integer.MAX_VALUE: (int)frr);
				}
			}
			
			br.close();

			System.out.println(files[i].getName()+","+bgTokenWithCounts.size());
		}
		
		String tokenizedBiGramFile= PhraseDirectories.tokenizedBiGramDir+ firstIndexChar;
		FileOutputStream fileOut = new FileOutputStream(tokenizedBiGramFile);
		DataOutputStream  out = new DataOutputStream (fileOut);
		
		out.writeInt(bgTokenWithCounts.size());
		System.out.println(tokenizedBiGramFile);
		
		for(BigramKey bgKey: bgTokenWithCounts.keySet()){
			out.writeInt(bgKey.keyUni1.intValue());
			out.writeInt(bgKey.keyUni2.intValue());
			out.writeInt(tokenIndex);
			out.writeInt(bgTokenWithCounts.get(bgKey).intValue());
			tokenIndex++;
		}
		
		System.out.println(tokenIndex+","+bgTokenWithCounts.size());
		out.close();
        fileOut.close();
		
		return tokenIndex;
	}
}

