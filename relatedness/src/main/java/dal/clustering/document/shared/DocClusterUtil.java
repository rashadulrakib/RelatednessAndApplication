package dal.clustering.document.shared;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import net.sourceforge.jdistlib.disttest.NormalityTest;
import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.clustering.document.shared.entities.PreprocessedContainer;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.UtilsShared;

public class DocClusterUtil {
	
	public HashSet<String> listStopWords ;
	
	public HashMap<String, Double> hmGTMWordPairSim;
	
	public DocClusterUtil(){
		PopulateStopWords(DocClusterConstant.StopWordFile);
	}
	
	public double ComputeCosineSImilarity(HashMap<String, Double> v1, 	HashMap<String, Double> v2) {
		double sim = 0;
		try{
			
			double v1v2Sum = 0.0;
			
			double v1Sq = 0.0;
			for(String v1Key: v1.keySet()){
				if(v2.containsKey(v1Key)){
					v1v2Sum = v1v2Sum+ v1.get(v1Key)* v2.get(v1Key);
				}
				
				v1Sq = v1Sq+Math.pow( v1.get(v1Key), 2);
			}
			
			double v2Sq = 0.0;
			for(String v2Key: v2.keySet()){
				v2Sq = v2Sq+Math.pow( v2.get(v2Key), 2);
			}
			
			return v1v2Sum/Math.sqrt(v1Sq)/Math.sqrt(v2Sq);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sim;
	}
	
	public void LoadGTMWordPairSimilarities() {
		try{
			hmGTMWordPairSim = new HashMap<String, Double>();
			
			String line;
			BufferedReader br = new BufferedReader(new FileReader(DocClusterConstant.GTMScores));
			
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
				
				hmGTMWordPairSim.put(arr[0]+","+ arr[1], score);
			}
            
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	 
   public ArrayList<String> SplitPhrases(ArrayList<String> phs) {
       ArrayList<String> bads = new ArrayList<String>();
       try {
           for (String s : phs) {
               s = s.trim();
               if (s.isEmpty()) {
                   continue;
               }
               if (!PhHasLessThanEqualLimitedWords(s) || PhHasLessWorldLength(s)) {
                   bads.add(s);
               }
           }

           for (String s : bads) {
               phs.remove(s);
               //phs.addAll(new ArrayList(Arrays.asList(s.split("\\s+"))));
				phs.addAll(ConvertAarryToArrayList(s.split("\\s+")));
				
           }
       } catch (Exception e) {
    	   e.printStackTrace();
       }
       return phs;
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
   
   public boolean PhHasLessThanEqualLimitedWords(String phWord) {
       try {
           String[] arrP = phWord.split("\\s");
           if (arrP.length > DocClusterConstant.MaxWordsInPhrase) {
               return false;
           }
       } catch (Exception e) {
    	   e.printStackTrace();
       }
       return true;
   }

   public boolean PhHasLessWorldLength(String phWord) {
       try {
           String[] arrP = phWord.split("\\s");
           for (String w : arrP) {
               w = w.trim();
               if (w.length() < DocClusterConstant.MinLettersInWord) {
                   return true;
               }
           }
       } catch (Exception e) {
    	   e.printStackTrace();
       }
       return false;
   }

   
	private void PopulateStopWords(String stopWordFile) {
      
        try {
        	listStopWords = new HashSet<String>();
        	
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
	
	public ArrayList<String> splitByStopWord(String s) {
        ArrayList<String> ps = new ArrayList<String>();
        try {
            String[] arr = s.split("\\s+");

            String phrase = "";
            for (String w : arr) {
                w = w.trim();
                if (w.isEmpty()) {
                    continue;
                }
                if (!listStopWords.contains(w)) {
                    phrase = phrase + " " + w;
                } else {
                    phrase = phrase.trim();
                    if (!phrase.isEmpty()) {
                        ps.add(phrase);
                    }
                    phrase = "";
                }
            }

            phrase = phrase.trim();
            if (!phrase.isEmpty()) {
                ps.add(phrase);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ps;
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
	
	private ArrayList<String> convertAarryToArrayList(String arr[]){
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
	
	public ArrayList<String> SplitSuperPhrases(ArrayList<String> ls1, ArrayList<String> ls2) {
        ArrayList<String> phs = ls1;
        ArrayList<String> supers = new ArrayList<String>();

        try {

            for (String s1 : ls1) {
                for (String s2 : ls2) {
                    //ArrayList<String> arr1 = new ArrayList(Arrays.asList(s1.split("\\s+")));
                    //ArrayList<String> arr2 = new ArrayList(Arrays.asList(s2.split("\\s+")));
					
					ArrayList<String> arr1 = convertAarryToArrayList(s1.split("\\s+"));
                    ArrayList<String> arr2 = convertAarryToArrayList(s2.split("\\s+"));

                    ArrayList<String> commonPhWords = GetCommonPhWordsByStemming(arr1, arr2);
                    if (commonPhWords.size() > 0) {
                        //phs.remove(s1);
                        //phs.addAll(arr1);
                        supers.add(s1);
                        break;
                    }
                }
            }

            for (String s : supers) {
                phs.remove(s);
                //ArrayList<String> arr = new ArrayList(Arrays.asList(s.split("\\s+")));
				ArrayList<String> arr = convertAarryToArrayList(s.split("\\s+"));
                phs.addAll(arr);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return phs;
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
	
	public double GetCommonWeight(ArrayList<String> commonPhWords) {
        double com = 0;
        try {
            for (String s : commonPhWords) {
                com = com + s.split("\\s+").length * 2;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return com;
    }
	
	public double GetTextSize(ArrayList<String> phWords) {
        double size = 0;
        try {
            for (String s : phWords) {
                size = size + s.split("\\s+").length * 2;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return size;
    }
	
//	public double ComputeSimilarityFromWeightedMatrixBySTD(ArrayList<ArrayList<PairSim>> t1t2simPairList, double common,
//            double t1Size, double t2Size) {
//        double score = 0.0;
//        try {
//            double allMeanSum = 0.0;
//            double cellValue = 0.0;
//
//            for (ArrayList<PairSim> simPairList : t1t2simPairList) {
//
//                double sum = 0.0;
//                for (PairSim ocPS : simPairList) {
//                    double phWordsim = ocPS.value;
//                    sum = sum + phWordsim;
//                }
//                double mean = sum / simPairList.size();
//
//                double sumVariance = 0.0;
//                for (PairSim ocPS : simPairList) {
//                    sumVariance = sumVariance + Math.pow(mean - ocPS.value, 2);
//                }
//
//                double sd = Math.sqrt(sumVariance / simPairList.size());
//
//                double filteredMeanSum = 0.0;
//                double count = 0.0;
//                for (PairSim ocPS : simPairList) {
//                    cellValue = cellValue + ocPS.value;
//                    if (ocPS.value > (mean + sd)) {
//                        filteredMeanSum = filteredMeanSum + ocPS.value;
//                        count++;
//                    }
//                }
//                if (count > 0.0) {
//                    allMeanSum = allMeanSum + filteredMeanSum / count;
//                }
//            }
//            score = (allMeanSum + common) * (t1Size + t2Size) / 2.0 / t1Size / t2Size;
//        } catch (Exception e) {
//        	e.printStackTrace();
//        }
//
//        return score;
//    }
//	
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
			//pDoc = StemmingUtil.stemPhrase(pDoc);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return pDoc;
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

	public double ComputeDocSImilarityByPhraseSimNotionFast(LinkedHashMap<Integer, Integer> lhmIdFreqWord1, 	LinkedHashMap<Integer, Integer> lhmIdFreqWord2, double sumOfFreqWord1, double sumOfFreqWord2) {
		double score = 0;
		try{
			
			HashMap<Integer, Double> commonCunts = new HashMap<Integer, Double>();
            double sumOfWeight = 0.0;
            
            for(Integer id1: lhmIdFreqWord1.keySet()){
            	if(lhmIdFreqWord2.containsKey(id1)){
            		double min = lhmIdFreqWord1.get(id1) / sumOfFreqWord1;
                    double max = lhmIdFreqWord2.get(id1) / sumOfFreqWord2;
                    if (min > max) {
                        double temp = min;
                        min = max;
                        max = temp;
                    }
                    double weight = (min / max) * ( lhmIdFreqWord1.get(id1) + lhmIdFreqWord2.get(id1));

                    sumOfWeight = sumOfWeight + weight;
                    commonCunts.put(id1, weight);
            	}
            }
            
            double meanWeight = sumOfWeight / commonCunts.size();
            double sumOfSquare = 0.0;
            for (Integer key : commonCunts.keySet()) {
                sumOfSquare = sumOfSquare + Math.pow(commonCunts.get(key) - meanWeight, 2.0);
            }
            
            double sd = Math.sqrt(sumOfSquare / commonCunts.size());
            double upperBound = (meanWeight + sd);
            double lowerBound = (meanWeight - sd);

            int nonCommonCount = 0;
            int filteredCommonCount = 0;
            double commonSumRatioWeight =0;
            
            for (Integer key : commonCunts.keySet()) {
                if (commonCunts.get(key) >= lowerBound && commonCunts.get(key) <= upperBound) {
                    
                    double min = lhmIdFreqWord1.get(key);
                    double max = lhmIdFreqWord2.get(key);
                    if (min > max) {
                        double temp = min;
                        min = max;
                        max = temp;
                    }
                    commonSumRatioWeight = commonSumRatioWeight + (min / max) * (lhmIdFreqWord1.get(key) + lhmIdFreqWord2.get(key));
                    
                    filteredCommonCount++;
                }
                else{
                	nonCommonCount++;
                }
            }
            
            int smallSize = lhmIdFreqWord1.size()-nonCommonCount;
            int bigSize = lhmIdFreqWord2.size()-nonCommonCount;
            
            double cosSim =(double) filteredCommonCount/(Math.sqrt(smallSize)*Math.sqrt(bigSize));
            score = normalizeSimilarity(sumOfFreqWord1, sumOfFreqWord2, cosSim * commonSumRatioWeight);
            
            if(cosSim>1){
            	System.out.println("Error: cosSim > 1 " +sumOfFreqWord1+","+sumOfFreqWord2);
            }

            commonCunts.clear();
            lhmIdFreqWord1.clear();
            lhmIdFreqWord2.clear();
            
            lhmIdFreqWord1 = null;
            lhmIdFreqWord2 = null;
            commonCunts = null;
            
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return score;
	}
	
	public double normalizeSimilarity( double cp1, double cp2, double comCount)  {
        double score = 0.0;
        try {
            score = normalizeByNGD(cp1, cp2, comCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
	 }
	 
	 private double normalizeByNGD(double countPh1, double countPh2, double commonCount) throws Exception {
        double score = 0.0;
        double N = 3107547215.0;
        try {
            if (countPh1 == 0 || countPh2 == 0 || commonCount == 0) {
                score = 0.0;
            } else {
                score = (Math.max(Math.log(countPh1) / Math.log(2), Math.log(countPh2) / Math.log(2)) - Math.log(commonCount) / Math.log(2))
                        / (Math.log(N) / Math.log(2) - Math.min(Math.log(countPh1) / Math.log(2), Math.log(countPh2) / Math.log(2)));
                score = Math.exp(-2 * score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
	 }

	public double[] FindMinMaxDocFrequency(HashMap<String, Double> docFreqs, int totalDocs) {
		double [] minMaxDocFreq = new double [2];
		
		try{
			double sumFreq = 0;
			
			for(Double docFreq: docFreqs.values()){
				sumFreq=sumFreq+ docFreq;
			}
			
			double avg = sumFreq/docFreqs.size();
			
			double sumOfSquare = 0;
			
			for(Double docFreq: docFreqs.values()){
				sumOfSquare = sumOfSquare + (docFreq-avg)*(docFreq-avg);
			}
			
			 double sd = Math.sqrt(sumOfSquare / docFreqs.size());
			 double upperBound = totalDocs;//Math.sqrt(totalDocs);//(avg + sd);
			 double lowerBound = 1.0; //(avg - sd); //<=2 ? 2: (avg - sd);
			 
			 minMaxDocFreq[0] = lowerBound;
			 minMaxDocFreq[1] = upperBound;
			
		}catch (Exception e) {
            e.printStackTrace();
        }
		return minMaxDocFreq;
	}

	public PreprocessedContainer GetTrainTestDocsLabelBodyAndUniqueWords(double percentageFromEcahCategory, LinkedHashMap<String, ArrayList<String>> hmDocsLabelBody) {
	
		PreprocessedContainer preprocessedContainer = new PreprocessedContainer();
		
		try{
			
			LinkedHashMap<String, ArrayList<String>>  hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
			
			ArrayList<String[]> alTestDocsBodyLabel = new ArrayList<String[]>();
			
			HashSet<String> uniqueWords = new HashSet<String>();
			
			for(String label: hmDocsLabelBody.keySet()){
				ArrayList<String> docs = PreProcessDocs(hmDocsLabelBody.get(label)) ;
				
				uniqueWords.addAll(GetUniqeWordsFromListOfDocs(docs));
				
				Collections.shuffle(docs);
				
				double totalDocs = docs.size();
				
				int totalTrainDocs = (int)(percentageFromEcahCategory*totalDocs);
				
				List<String> trainDocs = docs.subList(0, totalTrainDocs);
				hmTrainDocsLabelBody.put(label, new ArrayList<String>( trainDocs));
				
				List<String> testDocs = docs.subList(totalTrainDocs, (int)totalDocs);
				for(String testDoc: testDocs){
					String [] bodyLabel = new String[2];
					bodyLabel[0] = testDoc;
					bodyLabel[1] = label;
					alTestDocsBodyLabel.add(bodyLabel);
				}
			}
			
			Collections.shuffle(alTestDocsBodyLabel);
			
			preprocessedContainer.HmTrainDocsLabelBody = hmTrainDocsLabelBody;
			preprocessedContainer.AlTestDocsBodyLabel = alTestDocsBodyLabel;
			preprocessedContainer.UniqueWords = uniqueWords;
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
		return preprocessedContainer;
	}
	
	public PreprocessedContainer GetTrainTestDocsLabelBodyAndUniqueWords(LinkedHashMap<String, ArrayList<String>> docsLabelBody) {
		PreprocessedContainer preprocessedContainer = new PreprocessedContainer();
		
		try{
			LinkedHashMap<String, ArrayList<String>>  hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
			ArrayList<String[]> alTestDocsBodyLabel = new ArrayList<String[]>();
			HashSet<String> uniqueWords = new HashSet<String>();
			
			for(String label: docsLabelBody.keySet()){
				ArrayList<String> bodies = new ArrayList <String>( docsLabelBody.get(label));
				
				uniqueWords.addAll(GetUniqeWordsFromListOfDocs(bodies));
				
				Collections.shuffle(bodies);
					
				double totalDocs = bodies.size();
				int totalTrainDocs = 1;
				
				List<String> trainDocs = bodies.subList(0, totalTrainDocs);
				hmTrainDocsLabelBody.put(label, new ArrayList<String>( trainDocs));
				
				List<String> testDocs = bodies.subList(totalTrainDocs, (int)totalDocs);
				for(String testDoc: testDocs){
					String [] bodyLabel = new String[2];
					bodyLabel[0] = testDoc;
					bodyLabel[1] = label;
					alTestDocsBodyLabel.add(bodyLabel);
				}
			}
			
			Collections.shuffle(alTestDocsBodyLabel);
			
			preprocessedContainer.HmTrainDocsLabelBody = hmTrainDocsLabelBody;
			preprocessedContainer.AlTestDocsBodyLabel = alTestDocsBodyLabel;
			preprocessedContainer.UniqueWords = uniqueWords;
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		
		return preprocessedContainer;
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

	public HashMap<String, double[]> PopulateW2Vec(HashSet<String> uniqueWords) {
		HashMap<String, double[]> w2vec = new HashMap<String, double[]>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(DocClusterConstant.InputGlobalWordEmbeddingFile));
	           
			String text="";
			
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	
            	String [] arr = text.split("\\s+");
            	String EmbeddingWord = arr[0];
            	
            	if(uniqueWords.contains(EmbeddingWord)){
            		String [] vecs = text.replaceAll(EmbeddingWord, "").trim().split("\\s+");
            		double [] vecDoubles = new double[vecs.length];
            		for(int i=0; i< vecs.length;i++){
            			vecDoubles[i] = Double.parseDouble(vecs[i]);
            		}
            		w2vec.put(EmbeddingWord, vecDoubles);
            	}
            }
           
            br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return w2vec;
	}

	public LinkedHashMap<String, ArrayList<double[]>> CreateW2VecForTrainData(LinkedHashMap<String, ArrayList<String>> hmTrainPerLabelBodyPreprocesed,
			HashMap<String, double[]> hmW2Vec) {
		
		LinkedHashMap<String, ArrayList<double[]>> trainW2Vecs = new LinkedHashMap<String, ArrayList<double[]>>();
		
		try{
			for(String label : hmTrainPerLabelBodyPreprocesed.keySet()){
				ArrayList<String> docs = hmTrainPerLabelBodyPreprocesed.get(label);
				
				ArrayList<double[]> w2vecForDocs = new ArrayList<double[]>();
				
				for(String doc: docs){
					double[] w2vecForDoc = PopulateW2VecForSingleDoc(doc, hmW2Vec);
					w2vecForDocs.add(w2vecForDoc);
				}
				trainW2Vecs.put(label, w2vecForDocs);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return trainW2Vecs;
	}

	private double[] PopulateW2VecForSingleDoc(String doc, HashMap<String, double[]> hmW2Vec) {
		
		double [] avgVec = new double[DocClusterConstant.W2VecDimension];
		String arr[] = doc.split("\\s+");
		
		try{
		
			for(String word: arr){
        		if(hmW2Vec.containsKey(word)){
        			double[] wordVec = hmW2Vec.get(word); 
        			for(int i=0;i<avgVec.length;i++){
        				avgVec[i]=avgVec[i]+ wordVec[i];
        			}
        		}
        	}
        	
        	//averaging avgvec
        	for(int i=0;i<avgVec.length;i++){
        		avgVec[i]=avgVec[i]/(double)arr.length;
        	}
        	//end averaging avgvec
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return avgVec;
	}

	public ArrayList<InstanceW2Vec> CreateW2VecForTestData(ArrayList<String[]> alTestDocsBodyLabelPreprocesed, HashMap<String, double[]> hmW2Vec) {
		
		ArrayList<InstanceW2Vec> testW2Vecs = new ArrayList<InstanceW2Vec>();
		
		try{
			for(String[] bodyLabel: alTestDocsBodyLabelPreprocesed){
				String body = bodyLabel[0];
				String label = bodyLabel[1];
				
				InstanceW2Vec instanceW2Vec = new InstanceW2Vec();
				
				instanceW2Vec.OriginalLabel = label;
				instanceW2Vec.Features = PopulateW2VecForSingleDoc(body, hmW2Vec);
				instanceW2Vec.Text = body;
				
				testW2Vecs.add(instanceW2Vec);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return testW2Vecs;
	}

	public LinkedHashMap<String, double[]> ReComputeCenters(LinkedHashMap<String, ArrayList<InstanceW2Vec>> newClusters) {
		
		LinkedHashMap<String, double[]> centers = new LinkedHashMap<String, double[]>();
		
		try{
			for(String label: newClusters.keySet()){
				int centerSize = newClusters.get(label).get(0).Features.length;
				double totalDocVecs = newClusters.get(label).size();
				double [] center = new double[centerSize];
				
				//sum all the vecs
				for(InstanceW2Vec docVec: newClusters.get(label)){
					for(int i=0;i<docVec.Features.length;i++){
						center [i] = center[i]+docVec.Features[i];
					}
				}
				
				//average centers;
				for(int i=0;i<center.length;i++){
					center[i]=center[i]/totalDocVecs;
				}
				
				centers.put(label, center);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return centers;
	}

	public boolean IsConverge(LinkedHashMap<String, double[]> newCenetrs, LinkedHashMap<String, double[]> centers) {
		try{
			for(String label: centers.keySet()){
				
				if(newCenetrs.containsKey(label)){
					double[] newCeneterValues = newCenetrs.get(label);
					double[] centerValues = centers.get(label);
					
					for(int i=0;i<newCeneterValues.length;i++){
						if(newCeneterValues[i]!=centerValues[i]){
							return false;
						}
					}
					
				}else{
					return false;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;
	}
	
	//it can be used for single seed or multiple seed
	public LinkedHashMap<String, ArrayList<String>> GetTrainSeedDocuments(
			LinkedHashMap<String, ArrayList<String>> docsLabelBody, int numberOfTrainDocs , int seed) {
		
		LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody  = new LinkedHashMap<String, ArrayList<String>>();
		
		try{
			for(String label: docsLabelBody.keySet()){
				ArrayList<String> bodies = docsLabelBody.get(label);
				Collections.shuffle(bodies, new Random(seed));
				
				ArrayList<String> trainbodies = new ArrayList<String>( bodies.subList(0, numberOfTrainDocs));
			    hmTrainDocsLabelBody.put(label, trainbodies);
			    
			    System.out.println("Seed lable="+label+","+trainbodies.get(trainbodies.size()-1));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return hmTrainDocsLabelBody;
	}
	
//	public LinkedHashMap<String, ArrayList<String>> GetTrainRandomDocuments(
//			ArrayList<String[]> alDocLabelFlat, int numberOfClusters, int seed) {
//		LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody  = new LinkedHashMap<String, ArrayList<String>>();
//		
//		try{
//			Collections.shuffle(alDocLabelFlat, new Random(seed));
//			
//			for(int i=0;i< numberOfClusters;i++){
//				ArrayList<String> doc1 = new ArrayList<String>();
//				doc1.add(alDocLabelFlat.get(i)[0]);
//				hmTrainDocsLabelBody.put(alDocLabelFlat.get(i)[1], doc1);
//			}
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return hmTrainDocsLabelBody;
//	}

	public LinkedHashMap<String, InstanceW2Vec> GetInstanceClosestToCentersW2Vec(LinkedHashMap<String, ArrayList<InstanceW2Vec>> lastClusters,
			LinkedHashMap<String, double[]> lastCenters) {
		
		LinkedHashMap<String, InstanceW2Vec> instanceClosestToCenters = new LinkedHashMap<String, InstanceW2Vec>();
		
		try{
			for(String label: lastCenters.keySet()){
				double[] center = lastCenters.get(label);
				ArrayList<InstanceW2Vec> insts = lastClusters.get(label);
				
				double minDist = Double.MAX_VALUE;
				InstanceW2Vec minInst = new InstanceW2Vec();
				
				for(InstanceW2Vec inst: insts){
					double dist = ComputeUtil.ComputeEuclidianDistance(center, inst.Features);
					if(minDist>dist){
						minDist = dist;
						minInst = inst;
					}
				}
				
				instanceClosestToCenters.put(label, minInst);
				System.out.println("label="+label+", closest inst="+minInst.Text);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		return instanceClosestToCenters;
	}

	public LinkedHashMap<String, ArrayList<String>> GetInstanceClosestToCentersText(
			ArrayList<InstanceW2Vec> alTestDocsBodyLabel,
			LinkedHashMap<String, double[]> newCenetrsW2Vec, int x) {
		
		LinkedHashMap<String, ArrayList<String>> oneCenters = new LinkedHashMap<String, ArrayList<String>>();
		
		try{
			for(String label: newCenetrsW2Vec.keySet()){
				double[] center = newCenetrsW2Vec.get(label);
				
				double minDist = Double.MAX_VALUE;
				InstanceW2Vec minInst = new InstanceW2Vec();
				
				for(InstanceW2Vec inst: alTestDocsBodyLabel){
					double dist = ComputeUtil.ComputeEuclidianDistance(center, inst.Features);
					if(minDist>dist){
						minDist = dist;
						minInst = inst;
					}
				}
				
//				double maxSim = Double.MIN_VALUE;
//				InstanceW2Vec minInst = new InstanceW2Vec();
//				
//				for(InstanceW2Vec inst: alTestDocsBodyLabel){
//					double sim = ComputeUtil.ComputeCosineSimilarity(center, inst.Features);
//					if(maxSim<sim){
//						maxSim = sim;
//						minInst = inst;
//					}
//				}
				
				ArrayList<String> oneCenter = new ArrayList<String>();
				oneCenter.add(minInst.Text);
				
				oneCenters.put(label, oneCenter);
				System.out.println("label="+label+", closest inst="+minInst.Text);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return oneCenters;
	}
	
	public LinkedHashMap<String, ArrayList<String>> GetInstanceClosestToCentersText(
			LinkedHashMap<String, ArrayList<InstanceW2Vec>> newClustersW2Vec,
			LinkedHashMap<String, double[]> newCenetrsW2Vec) {
		
		LinkedHashMap<String, ArrayList<String>> oneCenters = new LinkedHashMap<String, ArrayList<String>>();
		
		try{
			for(String label: newCenetrsW2Vec.keySet()){
				double[] center = newCenetrsW2Vec.get(label);
				ArrayList<InstanceW2Vec> insts = newClustersW2Vec.get(label);
				
				double minDist = Double.MAX_VALUE;
				InstanceW2Vec minInst = new InstanceW2Vec();
				
				for(InstanceW2Vec inst: insts){
					double dist = ComputeUtil.ComputeEuclidianDistance(center, inst.Features);
					if(minDist>dist){
						minDist = dist;
						minInst = inst;
					}
				}
				
				ArrayList<String> oneCenter = new ArrayList<String>();
				oneCenter.add(minInst.Text);
				
				oneCenters.put(label, oneCenter);
				System.out.println("label="+label+", closest inst="+minInst.Text);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return oneCenters;
	}

	public double[][] ComputeSimilarityMatrixGtm(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilText docClusterUtilText) {
		
		double[][] docSimMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		
		try{
			for(int i=0;i< alDocLabelFlat.size();i++){
				
				String text1 = alDocLabelFlat.get(i)[0];
				
				docSimMatrix[i][i] = 1;
				
				for(int j=i+1;j<alDocLabelFlat.size();j++){
					
					String text2 = alDocLabelFlat.get(j)[0];
					
					docSimMatrix[i][j] = docClusterUtilText.ComputeTextSimGTM(text1, text2);
					
					docSimMatrix[j][i] = docSimMatrix[i][j];
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docSimMatrix;
	}
	
	public double[][] ComputeEuclidianDistanceMatrixW2Vec(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilW2Vec docClusterUtilW2Vec) {
		
		double[][] docDistanceMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		
		try{
			ArrayList<InstanceW2Vec> insts = docClusterUtilW2Vec.populateW2VecDocsFlat(alDocLabelFlat);
			
			for(int i=0;i< insts.size();i++){
				
				InstanceW2Vec inst1 = insts.get(i);
				
				docDistanceMatrix[i][i] = 0;
				
				for(int j=i+1;j<insts.size();j++){
					
					InstanceW2Vec inst2 = insts.get(j);
					
					docDistanceMatrix[i][j] = ComputeUtil.ComputeEuclidianDistance(inst1.Features, inst2.Features);
					
					docDistanceMatrix[j][i] = docDistanceMatrix[i][j];
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docDistanceMatrix;
	}
	
	
	public double[][] ComputeCosineMatrixW2Vec(ArrayList<String[]> alDocLabelFlat,
			DocClusterUtilW2Vec docClusterUtilW2Vec) {
		
		double[][] docDistanceMatrix = UtilsShared.InitializeMatrix(alDocLabelFlat.size(), alDocLabelFlat.size());
		
		try{
			ArrayList<InstanceW2Vec> insts = docClusterUtilW2Vec.populateW2VecDocsFlat(alDocLabelFlat);
			
			for(int i=0;i< insts.size();i++){
				
				InstanceW2Vec inst1 = insts.get(i);
				
				docDistanceMatrix[i][i] = 1;
				
				for(int j=i+1;j<insts.size();j++){
					
					InstanceW2Vec inst2 = insts.get(j);
					
					docDistanceMatrix[i][j] = ComputeUtil.ComputeCosineSimilarity(inst1.Features, inst2.Features);
					
					docDistanceMatrix[j][i] = docDistanceMatrix[i][j];
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return docDistanceMatrix;
	}

	public double[][] SparsifyDocDisSimilarityMatrix(double[][] docSimMatrix) {
		double[][] sparsifySimMatrix = new double[docSimMatrix.length][];
		
		try{
			int mulConst = 1;
			int pvalCount = 0;
			int WvalCount = 0;
			int bothCOunt =0;
			
			int pvalAndersonCount = 0;
			int wvalAndersonCount = 0;
			int bothAndersonCount = 0;
			
			for(int i=0;i<docSimMatrix.length;i++){
				double simArr [] = docSimMatrix[i];
				
				double simSim = 0.0;
				for(int j=0;j<simArr.length;j++){
					simSim= simSim + simArr[j]*mulConst;
				}
				
				double simArrTest [] = new double[simArr.length];
				ArrayList<Integer> alsimArrTest = new  ArrayList<Integer>();
				for(int j=0;j<simArr.length;j++){
					//simArrTest[j] = (int)(simArr[j]*mulConst);
					alsimArrTest.add((int)(simArr[j]*mulConst));
				}
				
				Collections.sort(alsimArrTest);
				
				for(int j=0;j<alsimArrTest.size();j++){
					simArrTest[j] = alsimArrTest.get(j);
				}
				
				//check normally distributed or not
				double Wval = 0;//NormalityTest.shapiro_wilk_statistic(simArrTest);
				double Pval = 0;//NormalityTest.shapiro_wilk_pvalue(Wval, simArr.length);
				
				
				double WvalAnderson = 0;//NormalityTest.anderson_darling_statistic(simArrTest);
				double PvalAnderson = 0;//NormalityTest.anderson_darling_pvalue(WvalAnderson, simArr.length);
				
				
				if(Pval>=0.05){
					pvalCount++;
				}
				if(Wval>=0.99){
					WvalCount++;
				}
				if(Pval>=0.05 && WvalCount>=0.99){
					bothCOunt++;
				}
				
				if(PvalAnderson>=0.05){
					pvalAndersonCount++;
				}
				if(WvalAnderson>=0.99){
					wvalAndersonCount++;
				}
				
				if(PvalAnderson>=0.05 && WvalAnderson>=0.99){
					bothAndersonCount++;
				}
				
				//
				
				double avgSimSum = simSim/ simArr.length;
				
				double varianceSum = 0;
				
				for(int j=0;j<simArr.length;j++){
					varianceSum = varianceSum + (simArr[j]*mulConst-avgSimSum)*(simArr[j]*mulConst-avgSimSum);
				}
				
				double sd = Math.sqrt(varianceSum/simArr.length);
				
				//filter sim
				int zeroCount = 0;
				for(int j=0;j<simArr.length;j++){
					
					//double newSim = simArr[j];
					//if(Pval>=0.5 || Wval>=0.99)
					
					//double newSim = simArr[j]*mulConst> avgSimSum*0.01+ sd*0 ? simArr[j]: 0; //0.7 best for stack
						//System.out.println("newSim="+newSim);
					
					//double newSim = simArr[j]*mulConst> avgSimSum+ sd*1.3 ? simArr[j]: 0; //0.7 best for stack
					//double newSim = simArr[j]*mulConst> avgSimSum- sd*0.0 ? Double.MAX_VALUE: simArr[j];
					 double newSim = simArr[j];
					 if( newSim==0){
						 zeroCount++;
					 }
					 simArr[j] = 1-newSim;
					 //simArr[j] = newSim;
				}
				
				System.out.println("total="+simArr.length+", zero count="+ zeroCount+",Wval="+Wval+",Pval="+Pval);
				//System.out.println("total="+simArr.length+", zero count="+ zeroCount+",Wval="+Wval);
				sparsifySimMatrix[i] = simArr;
			}
			
			System.out.println("pcount="+ pvalCount+", wcount="+ WvalCount+", bothcount="+ bothCOunt+": pander="+  pvalAndersonCount+", wvalander="+ wvalAndersonCount+", bothAnder="+ bothAndersonCount);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sparsifySimMatrix;
	}

	public ArrayList<String[]> SampledDocsPerCategory(ArrayList<String[]> alDocLabelFlat, int docsPerCategory, int seed) {
		
		ArrayList<String[]> sampledList = new ArrayList<String[]>();
		
		HashMap<String, ArrayList<String[]>> hmBodyLabel = new LinkedHashMap<String, ArrayList<String[]>>();
		
		try{
			for(String[] bodyLabel: alDocLabelFlat){
				String body = bodyLabel[0];
				String label = bodyLabel[1];
				
				if(!hmBodyLabel.containsKey(label)){
					ArrayList<String[]> al = new ArrayList<String[]>();
					al.add(bodyLabel);
					hmBodyLabel.put(label, al);
				}else{
					ArrayList<String[]> al = hmBodyLabel.get(label);
					al.add(bodyLabel);
					hmBodyLabel.put(label, al);
				}
			}
			
			Random rd = new Random(seed);
			
			for(String label: hmBodyLabel.keySet()){
				ArrayList<String[]> al = hmBodyLabel.get(label);
				Collections.shuffle(al, rd);
				docsPerCategory = al.size()>= docsPerCategory? docsPerCategory: al.size();
				sampledList.addAll(al.subList(0, docsPerCategory));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sampledList;
	}
	
	public HashMap<String, ArrayList<InstanceW2Vec>> GetClusterGroupsVectorOriginalLabel(ArrayList<InstanceW2Vec> instants) {
		
		HashMap<String, ArrayList<InstanceW2Vec>> clusterGroups = new HashMap<String, ArrayList<InstanceW2Vec>>();
		
		try{
			for(InstanceW2Vec instant: instants){
				if(!clusterGroups.containsKey(instant.OriginalLabel)){
					ArrayList<InstanceW2Vec> al = new ArrayList<InstanceW2Vec>();
					al.add(instant);
					clusterGroups.put(instant.OriginalLabel, al);
				}
				else{
					ArrayList<InstanceW2Vec> al = clusterGroups.get(instant.OriginalLabel);
					al.add(instant);
					clusterGroups.put(instant.OriginalLabel, al);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clusterGroups;
	}
	
	public HashMap<String, ArrayList<InstanceText>> GetClusterGroupsTextOriginalLabel(ArrayList<InstanceText> instants) {
		
		HashMap<String, ArrayList<InstanceText>> clusterGroups = new HashMap<String, ArrayList<InstanceText>>();

		try{
			for(InstanceText instant: instants){
				if(!clusterGroups.containsKey(instant.OriginalLabel)){
					ArrayList<InstanceText> al = new ArrayList<InstanceText>();
					al.add(instant);
					clusterGroups.put(instant.OriginalLabel, al);
				}
				else{
					ArrayList<InstanceText> al = clusterGroups.get(instant.OriginalLabel);
					al.add(instant);
					clusterGroups.put(instant.OriginalLabel, al);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clusterGroups;
	}

	
	

}
