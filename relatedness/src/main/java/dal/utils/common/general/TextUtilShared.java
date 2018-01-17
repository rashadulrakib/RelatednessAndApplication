package dal.utils.common.general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import dal.relatedness.phrase.stemmer.porter.StemmingUtil;

public class TextUtilShared {
	public HashMap<String, Double> hmGtmPairSim;
	public HashMap<String, Double> hmTrwpPairSim;
	HashSet<String> listStopWords ;
	
	public TextUtilShared(){		
		hmGtmPairSim = new HashMap<String, Double>();
		hmTrwpPairSim = new HashMap<String, Double>();
		listStopWords = new HashSet<String>();
		
		PopulateStopWords(TextSharedUtilConstant.StopWordFile);
	}
	
	public HashSet<String> GetListStopWords(){
		return listStopWords;
	}
	
	public void LoadGTMWordPairSimilarities() {
		try{
			
			String line;
			BufferedReader br = new BufferedReader(new FileReader(TextSharedUtilConstant.GTMScores));
			
			while ((line = br.readLine()) != null) {
				line = line.trim();
				
				String arr [] = line.split(",");
				
				if(arr.length!=3){
					continue;
				}
				
				double score = 0;
				
				try{
					score = Double.parseDouble(arr[2]);
				}				
				catch(Exception e1){
					score = 0;
					System.out.println(line);
					e1.printStackTrace();
					continue;
				}
				
				hmGtmPairSim.put(arr[0]+","+ arr[1], score);
			}
            
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void PopulateStopWords(String stopWordFile) {
	      
        try {
        	
        	BufferedReader brsw = new BufferedReader(new FileReader(new File(stopWordFile)));
            String line;
            while ((line = brsw.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.isEmpty()) {
                    continue;
                }
                listStopWords.add(line);
            }
            brsw.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	public ArrayList<String> ConvertAarryToArrayList(String arr[]){
		ArrayList<String> al = new ArrayList<String>();
		try{
			for (String s: arr){
				if(!s.isEmpty()){
					al.add(s);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
       }
		return al;
	}
	
	public ArrayList<String> RemoveStopWord(ArrayList<String> document) {
		ArrayList<String> doc = new ArrayList<String>();
		try{
			for(String word: document){
				if(listStopWords.contains(word)){
					continue;
				}
				doc.add(word);
			}
		}
		catch (Exception e) {
        	e.printStackTrace();
        }
		return doc;
	}
	
	public ArrayList<String> RemoveStopWord(String text) {
		ArrayList<String> doc = null;
		try{
			ArrayList<String> list = new ArrayList<String>(Arrays.asList(text.split("\\s+")));
			doc = RemoveStopWord(list);
		}
		catch (Exception e) {
        	e.printStackTrace();
        }
		return doc;
	}
	
	public ArrayList<String> PreProcessDocs(ArrayList<String> docs) {
		
		ArrayList<String> preprocessedDocs = new ArrayList<String>();
		
		try{
			for(String doc: docs){
				String doc1 = performPreprocess(doc);
				
				if(!doc1.isEmpty()){
					preprocessedDocs.add(doc1);
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return preprocessedDocs;
	}
	
	private String performPreprocess(String doc) {
		
		String pDoc = "";
		try{
			doc = doc.toLowerCase();
			pDoc = doc.replaceAll("&amp;", " ").trim();
			pDoc = pDoc.replaceAll("[^a-zA-Z ]", " ").trim().replaceAll("\\s+", " ").trim();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return pDoc;
	}
	
	public ArrayList<String> convertAarryToArrayList(String arr[]){
		ArrayList<String> al = new ArrayList<String>();
		try{
			for (String s: arr){
				if(!s.isEmpty()){
					al.add(s);
				}
			}
		}catch (Exception e) {
           e.printStackTrace();
        }
		return al;
	}
	
	public ArrayList<String> GetCommonPhWordsByStemming(ArrayList<String> phWordList1, ArrayList<String> phWordList2) {
		ArrayList<String> commonPhWords = new ArrayList<String>();
		try {
			for (int i = 0; i < phWordList1.size(); i++) {
				for (int j = 0; j < phWordList2.size(); j++) {
					if (equalStemmedPhrases(phWordList1.get(i),
							phWordList2.get(j))) {
						commonPhWords.add(phWordList1.get(i));
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commonPhWords;
	}
	
	public ArrayList<String> GetRestPhWords(ArrayList<String> phWordList, ArrayList<String> commonPhWords) {
        ArrayList<String> restPhWords = new ArrayList<String>();
        try {
            for (String phWord : phWordList) {
                if (!commonPhWords.contains(phWord)) {
                    restPhWords.add(phWord);
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return restPhWords;
    }

	private boolean equalStemmedPhrases(String ph1, String ph2) {
		boolean isEqual = true;
		try {
			ph1 = StemmingUtil.stemPhrase(ph1);
			ph2 = StemmingUtil.stemPhrase(ph2);

			isEqual = ph1.equals(ph2);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isEqual;
	}
	
public  String PerformPreprocess(String doc) {
		
		String pDoc = "";
		try{
			doc = doc.toLowerCase().trim();
			pDoc = doc.replaceAll("&amp;", " ").trim();
			pDoc = pDoc.replaceAll("[^a-zA-Z ]", " ").trim().replaceAll("\\s+", " ").trim();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return pDoc;
	}
	
	public String GetSubStr(String[] ws, int start, int end) {
        String str = "";
        try {
            for (int i = start; i < end; i++) {
                str = str + " " + ws[i];
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return str.trim();
    }

	public ArrayList<String> StemByEachWord(ArrayList<String>words) {
		ArrayList<String> doc = new ArrayList<String>();
		
		try{
			for(String word: words){
				doc.add(StemmingUtil.stemPhrase(word));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return doc;
	}
	
	public String StemByEachWord(String doc) {
		StringBuilder stemdoc = new StringBuilder();
		try{
			String arr[] = doc.split("\\s+");
			for(String word: arr){
				stemdoc.append(StemmingUtil.stemPhrase(word)+" ");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return stemdoc.toString().trim();
	}
	
	public String ConvertArrayListToString(ArrayList<String> words){
		StringBuilder sb = new StringBuilder();
		try{
			
			for(String word: words){
				sb.append(word+" ");
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return sb.toString().trim();
	}

	public HashSet<String> GetCommonWords(ArrayList<String> doc1, ArrayList<String> doc2) {
		HashSet<String> commonWords = new HashSet<String>();
		try{
			HashSet<String> comm  = new HashSet<String>();
			
			for(String word: doc1){
				comm.add(word);
			}
			
			for(String word: doc2){
				if(comm.contains(word)){
					commonWords.add(word);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return commonWords;
	}

	public ArrayList<String> RemoveCommonWords(ArrayList<String> doc, HashSet<String> commonWords) {
		 ArrayList<String> afterRemovedWords = new ArrayList<String>();
		try{
			for(String word: doc){
				if(commonWords.contains(word)){
					continue;
				}
				afterRemovedWords.add(word);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return afterRemovedWords;
	}
	
	public Collection<? extends String> GetUniqeWordsFromListOfDocs(ArrayList<String> docs) {
		HashSet<String> uniqueWords = new HashSet<String>();
		try{
			for(String doc: docs){
				uniqueWords.addAll(Arrays.asList(doc.split("\\s+")));
			}
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
		return uniqueWords;
	}
	
	public ArrayList<String> CombineWordPhs(ArrayList<String> phs, ArrayList<String> cands) {
        ArrayList<String> wPhs = new ArrayList<String>();
        try {
            for (String s : cands) {
                s = s.trim();
                if (s.isEmpty()) {
                    continue;
                }
                for (String p : phs) {
                    s = s.replaceAll("\\b" + p + "\\b", "").trim();
                }
                if (!s.isEmpty()) {
                    //wPhs.addAll(Arrays.asList(s.split("\\s+")));
					wPhs.addAll(convertAarryToArrayList(s.split("\\s+")));
                }
            }

            wPhs.addAll(phs);
        } catch (Exception e) {
           e.printStackTrace();
        }

        return wPhs;
    }
	
}
