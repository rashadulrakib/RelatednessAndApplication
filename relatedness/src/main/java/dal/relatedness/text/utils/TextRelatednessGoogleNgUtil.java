package dal.relatedness.text.utils;

import java.util.ArrayList;
import java.util.HashSet;

import dal.clustering.document.shared.PairSim;
import dal.relatedness.text.compute.trwp.TextRelatednessTrwpConstant;
import dal.utils.common.general.TextUtilShared;

public class TextRelatednessGoogleNgUtil {
	TextUtilShared textUtilShared;
	
	public TextRelatednessGoogleNgUtil (TextUtilShared textUtilShared){
		this.textUtilShared = textUtilShared;
	}
	
	public double ComputeSimilarityFromWeightedMatrixBySTD(
			ArrayList<ArrayList<PairSim>> t1t2simPairList, double commonSize, double t1Size,
			double t2Size, boolean isGTM) {
		double score = 0.0;
        try {
            double allMeanSum = 0.0;
            double cellValue = 0.0;

            for (ArrayList<PairSim> simPairList : t1t2simPairList) {

                double sum = 0.0;
                for (PairSim ocPS : simPairList) {
                    double phWordsim = ocPS.value;
                    sum = sum + phWordsim;
                }
                double mean = sum / simPairList.size();

                double sumVariance = 0.0;
                for (PairSim ocPS : simPairList) {
                    sumVariance = sumVariance + Math.pow(mean - ocPS.value, 2);
                }

                double sd = Math.sqrt(sumVariance / simPairList.size());

                double filteredMeanSum = 0.0;
                double count = 0.0;
                for (PairSim ocPS : simPairList) {
                    cellValue = cellValue + ocPS.value;
                    if (ocPS.value > (mean + sd)) {
                        filteredMeanSum = filteredMeanSum + ocPS.value;
                        count++;
                    }
                }
                if (count > 0.0) {
                    allMeanSum = allMeanSum + filteredMeanSum / count;
                }
            }
            
            if(isGTM){
            	score = (allMeanSum + commonSize) * (t1Size + t2Size) / t1Size / t2Size;
            }
            else{
            	score = (allMeanSum + commonSize) * (t1Size + t2Size) / 2.0 / t1Size / t2Size;
            }
            
            if(score>1){
            	System.out.println("bad score="+ score);
            	score = 1;
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return score;
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
					phs.addAll(textUtilShared.ConvertAarryToArrayList(s.split("\\s+")));
					
	           }
	       } catch (Exception e) {
	    	   e.printStackTrace();
	       }
	       return phs;
	   }
	
	public boolean PhHasLessThanEqualLimitedWords(String phWord) {
	       try {
	           String[] arrP = phWord.split("\\s");
	           if (arrP.length > TextRelatednessTrwpConstant.MaxWordsInPhrase) {
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
	               if (w.length() < TextRelatednessTrwpConstant.MinLettersInWord) {
	                   return true;
	               }
	           }
	       } catch (Exception e) {
	    	   e.printStackTrace();
	       }
	       return false;
	   }

		public ArrayList<String> splitByStopWord(String s) {
	        
			HashSet<String> listStopWords = textUtilShared.GetListStopWords();
			
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
		
		
		public ArrayList<String> SplitSuperPhrases(ArrayList<String> ls1, ArrayList<String> ls2) {
	        ArrayList<String> phs = ls1;
	        ArrayList<String> supers = new ArrayList<String>();

	        try {

	            for (String s1 : ls1) {
	                for (String s2 : ls2) {
	                    //ArrayList<String> arr1 = new ArrayList(Arrays.asList(s1.split("\\s+")));
	                    //ArrayList<String> arr2 = new ArrayList(Arrays.asList(s2.split("\\s+")));
						
						ArrayList<String> arr1 = textUtilShared.convertAarryToArrayList(s1.split("\\s+"));
	                    ArrayList<String> arr2 = textUtilShared.convertAarryToArrayList(s2.split("\\s+"));

	                    ArrayList<String> commonPhWords = textUtilShared.GetCommonPhWordsByStemming(arr1, arr2);
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
	               
					ArrayList<String> arr = textUtilShared.convertAarryToArrayList(s.split("\\s+"));
	                phs.addAll(arr);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return phs;
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

}
