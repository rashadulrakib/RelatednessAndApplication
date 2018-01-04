package dal.relatedness.phrase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import dal.relatedness.phrase.stemmer.porter.StemmingUtil;

public class RelatednessProcessedContext {

	
	String allPhraseFreqFile = "D:\\Google-n-gram\\n-gram-indexed-processedcontext-stem\\2gm\\AllBgStemmedFile";
	String phPairsFile = "D:\\phrasePairs.txt";
	String phraseContextDir= "D:\\Google-n-gram\\n-gram-indexed-processedcontext-stem\\4gm\\";
	
	HashMap<String, Integer> phFreqs = new HashMap<String, Integer>();
	HashMap<String, HashMap<String, Double>> hmPhContexts = new HashMap<String, HashMap<String,Double>>(); 
	
	public void Compute(){
		try{
			
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(phPairsFile));
		
			while ((sCurrentLine = br.readLine()) != null) {
				
				if(sCurrentLine.isEmpty()) continue;
				
				sCurrentLine = sCurrentLine.toLowerCase();
				
				String [] arr = sCurrentLine.split(",");
				String ph1 = StemmingUtil.stemPhrase(arr[0]);
				String ph2 = StemmingUtil.stemPhrase(arr[1]);
				
				phFreqs.put(ph1, null);
				phFreqs.put(ph2, null);
				
				hmPhContexts.put(ph1, null);
				hmPhContexts.put(ph2, null);
			}
			
			br.close();
			
			FindPharseFreq();
			FindPhraseContexts();
			
			ComputeSimilarity();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}

	private void ComputeSimilarity() {
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(phPairsFile));
			
			while ((sCurrentLine = br.readLine()) != null) {
				
				if(sCurrentLine.isEmpty()) continue;
				
				sCurrentLine = sCurrentLine.toLowerCase();
				
				String [] arr = sCurrentLine.split(",");
				String ph1 = StemmingUtil.stemPhrase(arr[0]);
				String ph2 = StemmingUtil.stemPhrase(arr[1]);
				
				double countPh1 = phFreqs.get(ph1);
				double countPh2 = phFreqs.get(ph2);
				
				HashMap<String, Double> hmPh1 = hmPhContexts.get(ph1);
				HashMap<String, Double> hmPh2 = hmPhContexts.get(ph2);
				
				System.out.println(ph1+"="+countPh1);
				System.out.println(ph2+"="+countPh2);
				
				System.out.println(hmPh1.size());
				System.out.println(hmPh2.size());
				
				HashMap<String, Double> hmPhCos1 = hmPh1;
	            HashMap<String, Double> hmPhCos2 = hmPh2;

	            ArrayList<HashMap<String, Double>> lhm = filterNormalizedWeightBySTD(hmPh1, hmPh2, countPh1, countPh2);
	            hmPh1 =  lhm.get(0);
	            hmPh2 =  lhm.get(1);

	            ArrayList<HashMap<String, Double>> lhmCos = getCommonVectors(hmPhCos1, hmPhCos2);
	            HashMap<String, Double> hmPh1Common = lhmCos.get(0);

	            ArrayList<String> comMinusFiltered = getComMinusFilteredKeys(hmPh1Common, hmPh1);
	            hmPhCos1 = removeKeys(hmPhCos1, comMinusFiltered);
	            hmPhCos2 = removeKeys(hmPhCos2, comMinusFiltered);

	            double cosSim = getCosineSimilarity(hmPhCos1, hmPhCos2, countPh1, countPh2);
	            //score=cosSim;
	            double commonCount = getCommonCount(hmPh1, hmPh2);
	            commonCount = commonCount * cosSim;

	            double score = normalizeSimilarity( countPh1, countPh2, commonCount);
	            
	            System.out.println(ph1+","+ph2+","+score); 
			}
			
			br.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	 private double normalizeSimilarity( double cp1, double cp2, double comCount) throws Exception {
	        double score = 0.0;
	        try {
	            
	            score = normalizeByNGD(cp1, cp2, comCount);
	        } catch (Exception e) {
	            throw e;
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
	            throw e;
	        }
	        return score;
	    }
	
	private double getCommonCount(HashMap<String, Double> hmPh1Key, HashMap<String, Double> hmPh2Key) throws Exception {
        double commonCount = 0.0;
        try {
            
            for (String key : hmPh1Key.keySet()) {
                if (hmPh2Key.containsKey(key)) {
                    double min = hmPh1Key.get(key);
                    double max = hmPh2Key.get(key);
                    if (min > max) {
                        double temp = min;
                        min = max;
                        max = temp;
                    }
                    commonCount = commonCount + (min / max) * (min + max);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return commonCount;
    }
	
	private double getCosineSimilarity(HashMap<String, Double> hmSmall,
            HashMap<String, Double> hmBig, double cp1, double cp2) throws Exception {
        try {

            Set<String> smallTerms = hmSmall.keySet();
            boolean anyCommonTerm = false;
            double sumSamllSquare = 0;
            double sum = 0;
            for (String term : smallTerms) {
                //double termWeightSmall = 1 - Math.log(hmSmall.get(term))/(1+Math.log(countmax)) ;
                //double termWeightSmall = Math.log(hmSmall.get(term))/Math.log(countmax);
                //double termWeightSmall = Math.log(hmSmall.get(term)/countmax);
                //double termWeightSmall = 1;
                double termWeightSmall = Math.log(hmSmall.get(term)) / Math.log(cp1);

                sumSamllSquare = sumSamllSquare + termWeightSmall * termWeightSmall;

                if (hmBig.containsKey(term)) {
                    anyCommonTerm = true;
                    //double termWeightBig=1- Math.log(hmBig.get(term))/(1+Math.log(countmax)) ;
                    //double termWeightBig = Math.log(hmBig.get(term)/countmax);
                    //double termWeightBig = 1;
                    double termWeightBig = Math.log(hmBig.get(term)) / Math.log(cp2);
                    sum = sum + termWeightSmall * termWeightBig;
                    //sum = sum + termWeightSmall * 1;
                }
            }

            if (anyCommonTerm) {
                double sumBigSquare = 0;
                Set<String> bigTerms = hmBig.keySet();
                for (String term : bigTerms) {
                    //double termWeightBig =1- Math.log(hmBig.get(term))/(1+Math.log(countmax)) ;
                    //double termWeightBig = Math.log(hmBig.get(term))/Math.log(countmax) ;
                    //double termWeightBig = Math.log(hmBig.get(term)/countmax);
                    //double termWeightBig = 1;
                    double termWeightBig = Math.log(hmBig.get(term)) / Math.log(cp2);
                    sumBigSquare = sumBigSquare + termWeightBig * termWeightBig;
                }
                return sum / (Math.sqrt(sumSamllSquare) * Math.sqrt(sumBigSquare));
            }
        } catch (Exception e) {
            throw e;
        }

        return 0;
    }
	
	private HashMap<String, Double> removeKeys(HashMap<String, Double> hmPhCos, ArrayList<String> keys) throws Exception {
        try {
            for (String key : keys) {
                hmPhCos.remove(key);
            }
        } catch (Exception e) {
            throw e;
        }
        return hmPhCos;
    }
	
	private ArrayList<String> getComMinusFilteredKeys(HashMap<String, Double> hmBig, HashMap<String, Double> hmSmall) throws Exception {
        ArrayList<String> commonNotInFilteredList = new ArrayList<String>();
        try {
            for (String key : hmBig.keySet()) {
                if (!hmSmall.containsKey(key)) {
                    commonNotInFilteredList.add(key);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return commonNotInFilteredList;
    }
	
	private ArrayList<HashMap<String, Double>> getCommonVectors(HashMap<String, Double> hmSmall, HashMap<String, Double> hmBig) throws Exception {
        ArrayList<HashMap<String, Double>> lhm = new ArrayList<HashMap<String, Double>>();
        try {

            HashMap<String, Double> hm1= new HashMap<String, Double>();
            HashMap<String, Double> hm2= new HashMap<String, Double>();
            
            Set<String> smallTerms = hmSmall.keySet();

            for (String term : smallTerms) {
                if (hmBig.containsKey(term)) {
                    hm1.put(term, hmSmall.get(term));
                    hm2.put(term, hmBig.get(term));
                }
            }
            
            lhm.add(hm1);
            lhm.add(hm2);

        } catch (Exception e) {
            throw e;
        }
        return lhm;
    }
	
	private ArrayList<HashMap<String, Double>> filterNormalizedWeightBySTD(HashMap<String, Double> hmSmall,
            HashMap<String, Double> hmBig, double cp1, double cp2) throws Exception {
        ArrayList<HashMap<String, Double>> lhm = new ArrayList<HashMap<String, Double>>();
        try {

            Set<String> smallTerms = hmSmall.keySet();

            HashMap<String, Double> hm1 = new HashMap<String, Double>();
            HashMap<String, Double> hm2 = new HashMap<String, Double>();

            HashMap<String, Double> commonCunts = new HashMap<String, Double>();
            double sumOfWeight = 0.0;

            for (String term : smallTerms) {
                if (hmBig.containsKey(term)) {
                    double min = hmSmall.get(term) / cp1;
                    double max = hmBig.get(term) / cp2;
                    if (min > max) {
                        double temp = min;
                        min = max;
                        max = temp;
                    }
                    double weight = (min / max) * (hmSmall.get(term) + hmBig.get(term));

                    sumOfWeight = sumOfWeight + weight;
                    commonCunts.put(term, weight);
                }
            }

            double meanWeight = sumOfWeight / commonCunts.size();
            double sumOfSquare = 0.0;
            for (String key : commonCunts.keySet()) {
                sumOfSquare = sumOfSquare + Math.pow(commonCunts.get(key) - meanWeight, 2.0);
            }

            double sd = Math.sqrt(sumOfSquare / commonCunts.size());
            double upperBound = (meanWeight + sd);
            double lowerBound = (meanWeight - sd);

            for (String key : commonCunts.keySet()) {
                if (commonCunts.get(key) >= lowerBound && commonCunts.get(key) <= upperBound) {
                    hm1.put(key, hmSmall.get(key));
                    hm2.put(key, hmBig.get(key));
                }
            }

            lhm.add(hm1);
            lhm.add(hm2);
        } catch (Exception e) {
            throw e;
        }
        return lhm;
    }

	private void FindPhraseContexts() {
		try{
			for(char ch='a'; ch<='z';ch++){
				String contextFile = phraseContextDir+String.valueOf(ch);
				
				String sCurrentLine;
				BufferedReader br = new BufferedReader(new FileReader(contextFile));
				
				while ((sCurrentLine = br.readLine()) != null) {
					
					if(sCurrentLine.isEmpty()) continue;
					
					String arr[] = sCurrentLine.split("\\s+");
					
					String stemPhrase = arr[0]+" "+arr[1];
					
					if(hmPhContexts.containsKey(stemPhrase)){
						int numberContexts = Integer.parseInt(arr[2]);
						
						HashMap<String, Double> hmcontexts = new HashMap<String, Double>();
						
						for(int i=1;i<=numberContexts;i++){
							String context = arr[i*3]+" "+arr[i*3+1];
							Double contextFreq = Double.parseDouble(arr[i*3+2]);
							hmcontexts.put(context, contextFreq);
						}
						
						hmPhContexts.put(stemPhrase, hmcontexts);
					}
				}
				
				br.close();
				
				
			}
			
			System.out.println("FindPhraseContexts end.");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		
	}

	private void FindPharseFreq() {
		try{
			
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(allPhraseFreqFile));
			
			while ((sCurrentLine = br.readLine()) != null) {
				
				if(sCurrentLine.isEmpty()) continue;
				
				String arr[] = sCurrentLine.split("\\s+");
				
				if(arr.length!=3) continue;
				
				String setmPh = arr[0]+" "+arr[1];
				
				if(phFreqs.containsKey(setmPh)){
					phFreqs.put(setmPh, Integer.parseInt(arr[2]));
				}
			}
			
			br.close();
			
			System.out.println("FindPharseFreq end.");
			
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
	}
	
}
