package dal.utils.common.general;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.TreeSet;

import dal.clustering.document.shared.entities.InstanceText;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;

public class TextUtilShared<T> {
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
	
	public String performPreprocess(String doc) {
		
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
			HashSet<String> hmlist1 = ConvertStringListToHashSetStemming(phWordList1);
			
			for(int i=0;i< phWordList2.size();i++){
				if(hmlist1.contains(StemmingUtil.stemPhrase(phWordList2.get(i)))){
					commonPhWords.add(phWordList2.get(i));
				}
			}
			
//			for (int i = 0; i < phWordList1.size(); i++) {
//				for (int j = 0; j < phWordList2.size(); j++) {
//					if (equalStemmedPhrases(phWordList1.get(i),
//							phWordList2.get(j))) {
//						commonPhWords.add(phWordList1.get(i));
//						break;
//					}
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commonPhWords;
	}
	
	public HashSet<String> ConvertStringListToHashSetStemming(ArrayList<String> phWordList) {
		HashSet<String> hmlist = new HashSet<String>();
		try{
			for(String str: phWordList){
				hmlist.add(StemmingUtil.stemPhrase(str));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return hmlist;
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
			//pDoc = pDoc.replaceAll("[^a-zA-Z ]", " ").trim().replaceAll("\\s+", " ").trim();
			pDoc = pDoc.replaceAll("\\b[a-z]\\b|\\b\\d+\\b", " ").replaceAll("\\s+", " ").trim();		 
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
				String stemmed = StemmingUtil.stemPhrase(word);
				if(stemmed.isEmpty()) continue;
				doc.add(stemmed);
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

	public ArrayList<String[]> StatisticalPruneWordsByDocFreqs(HashMap<String, Integer> docFreqs, 
			ArrayList<String[]> alDocLabelFlat, int maxDocFreq) {
		
		ArrayList<String []> alDocLabelFlatPruned = new ArrayList<String[]>(); 
		
		try{
			
			ArrayList<Integer> aldocFreqs = UtilsShared.ConvertCollectionToArrayList(docFreqs.values());
			
			Double average = aldocFreqs.stream().mapToDouble(val -> val).average().orElse(0.0);
			
			double varainceSum = 0;
			for(Integer num: aldocFreqs){
				varainceSum = varainceSum + (num-average)*(num-average);
			}
			
			double sd = Math.sqrt(varainceSum/aldocFreqs.size());
			
			TreeSet<String> highDocFreqs = new TreeSet<String>();
			
			for(String key: docFreqs.keySet()){
				double freq = docFreqs.get(key);
				
				if(maxDocFreq>1){
					if(freq> maxDocFreq){
						highDocFreqs.add(key);
					}
				}else{
					if(freq>=average+sd){
						highDocFreqs.add(key);
					}
				}
			}
			
			for(String hword: highDocFreqs){
				System.out.println("high freq="+hword+","+docFreqs.get(hword));
			}
			
			for(String[] bodyLabel: alDocLabelFlat){
				String body = bodyLabel[0];
				String label = bodyLabel[1];
				
				String [] arr = body.split("\\s+");
			
				StringBuilder sbNew = new StringBuilder();
				
				for(String word: arr){
					if(highDocFreqs.contains(word))
						continue;					
					sbNew.append(word+" ");
				}
				
				String[] newbodyLabel = new String[2];
				newbodyLabel[0] = sbNew.toString().trim();
				newbodyLabel[1]= label;
				
				alDocLabelFlatPruned.add(newbodyLabel);
			}
			
		}catch (Exception e) {
	           e.printStackTrace();
	    }
		
		return alDocLabelFlatPruned;
	}
	
	public ArrayList<String[]> FixedPruneWordsByDocFreqs(HashMap<String, Integer> docFreqs, 
			ArrayList<String[]> alDocLabelFlat, int maxDocFreq) {
		
		ArrayList<String []> alDocLabelFlatPruned = new ArrayList<String[]>(); 
		
		try{
			for(String[] bodyLabel: alDocLabelFlat){
				
				if(!IsAllWordsHaveLessThanEqualMaxDocFreq(bodyLabel[0], maxDocFreq, docFreqs)) continue;
				
				alDocLabelFlatPruned.add(bodyLabel);
			}
			
		}catch (Exception e) {
	           e.printStackTrace();
	    }
		
		return alDocLabelFlatPruned;
	}
	
	public boolean IsAllWordsHaveLessThanEqualMaxDocFreq(String text, int maxDocFreq, HashMap<String, Integer> docFreqs) {
		try{
		
			String [] arr = text.split("\\s+");
			
			for(String word: arr){
				if(docFreqs.get(word)>maxDocFreq) return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public ArrayList<String> ReadClusterLabels(
			String externalClusteringResultFile) {
		ArrayList<String> clusterLables = new ArrayList<String>();
		
		try{
			BufferedReader br =  new BufferedReader(new FileReader(externalClusteringResultFile));
			String line="";
			
			while((line=br.readLine()) != null) {
		        line = line.trim();
		        
		        //if(line.isEmpty()){
		        //	line="20";
		        //}
		        
		        String clusterGroups [] = line.split(",");
		        clusterLables.addAll(Arrays.asList(clusterGroups));
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return clusterLables;
	}

	public String GetWordsWithMaxDocFreqByCluster(String text,
			int maxDocFreqByClusterTolerance,
			HashMap<String, Integer> docFreqByClusters) {
		
		String textWithDocFreqByCluster = "";
		
		try{
			String [] arr = text.split("\\s+");
			
			StringBuilder sb = new StringBuilder();
			
			for(String str: arr){
				if(!docFreqByClusters.containsKey(str) 
						|| docFreqByClusters.get(str)> maxDocFreqByClusterTolerance) continue;
				
				sb.append(str+" ");
			}
			
			textWithDocFreqByCluster = sb.toString().trim();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return textWithDocFreqByCluster;
	}

	public String[] RemoveCommonWords(String ftr1Tex, String ftr2Tex) {
		String[] texts = new String[2];
		
		try{
			String arr1 [] = ftr1Tex.split("\\s+");
			String arr2 [] = ftr2Tex.split("\\s+");
			
			HashSet<String> arr1set = new HashSet<String>();
			arr1set.addAll(Arrays.asList(arr1));
			
			HashSet<String> arr2set = new HashSet<String>();
			arr2set.addAll(Arrays.asList(arr2));
			
			HashSet<String> common = new HashSet<String>();
			
			for(String str: arr1set){
				if(arr2set.contains(str)){
					common.add(str);
				}
			}
			
			arr1set.removeAll(common);
			arr2set.removeAll(common);
			
			texts[0] = ConvertSetToString(arr1set);
			texts[1] = ConvertSetToString(arr2set);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return texts;
	}

	private String ConvertSetToString(HashSet<String> arrset) {
		StringBuilder sb = new StringBuilder();
		try{
			for(String s: arrset){
				sb.append(s+" ");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sb.toString().trim();
	}

	public ArrayList<String[]> ReadPredTrueTexts(String trainTestTextFile) {
		
		ArrayList<String[]> predTrueTexts = new ArrayList<String[]>();
		
		try{
			BufferedReader br =  new BufferedReader(new FileReader(trainTestTextFile));
			String line = "";
			
			while((line = br.readLine())!=null){				   
			   line = line.trim().toLowerCase();
			   String predTrueText [] = line.split("\\t+");			   
			   predTrueTexts.add(predTrueText);
			}			
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return predTrueTexts;
	}
	
	public void WriteTextsOfEachGroup(String dir,
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters) {
		try{
			for(String label: lastClusters.keySet()){
				java.io.BufferedWriter bw = new java.io.BufferedWriter(new FileWriter(dir+label));
				
				for(InstanceText inst: lastClusters.get(label)){
					bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t"+inst.Text+"\n");
				}
				
				bw.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public HashMap<String, ArrayList<String>> ReadPredOutliersAll(String dir) {
		HashMap<String, ArrayList<String>> outliersByLabel = new LinkedHashMap<String, ArrayList<String>>();
		try{
			for(int i=1;i<=4;i++){
				String outlierFile = dir+i+"_outlierpred";
				
				ArrayList<String> outliersPred = new ArrayList<String>();
				
				BufferedReader br =  new BufferedReader(new FileReader(outlierFile));
				String line = "";
				
				while((line = br.readLine())!=null){				   
				   line = line.trim().toLowerCase();
				   if(line.isEmpty()) continue;
				   
				   outliersPred.add(line);
				}
				br.close();
				
				outliersByLabel.put(Integer.toString(i), outliersPred);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return outliersByLabel;
	}

	public void WriteTrainTestInstances(ArrayList<InstanceText> trainInstTexts,
			String file) {
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));											
			for(InstanceText inst: trainInstTexts){
				bw.write(inst.ClusteredLabel+"\t"+inst.OriginalLabel+"\t" +inst.Text+"\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public ArrayList<T> FlatInsts(
			LinkedHashMap<String, ArrayList<T>> lastClustersCommon) {
		
		ArrayList<T> flat = new ArrayList<T>();
		
		try{
			for(String key: lastClustersCommon.keySet()){
				flat.addAll(lastClustersCommon.get(key));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return flat;
	}

}
