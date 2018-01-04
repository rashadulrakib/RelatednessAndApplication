package dal.relatedness.phrase.compute.tokenized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;
import dal.relatedness.phrase.load.nontokenized.unigram.LoadNonTokenizeUnigram;

public class PhRelComputeUtil {

	public LoadNonTokenizeUnigram _loadNonTokenizeUnigram;
	public LoadNonTokenizeBigram _loadNonTokenizeBigram;
	
	public PhRelComputeUtil(LoadNonTokenizeUnigram loadNonTokenizeUnigram, LoadNonTokenizeBigram loadNonTokenizeBigram) {
		_loadNonTokenizeUnigram = loadNonTokenizeUnigram;
		_loadNonTokenizeBigram = loadNonTokenizeBigram;
	}
	
	public int GetPhIndexAl(int phLength, String ph) {
		
		try{
			if(phLength==1){
				return Collections.binarySearch(_loadNonTokenizeUnigram.getAlStemmedCrossCheckedUnis(), ph);
			}
			else if(phLength==2){
				return Collections.binarySearch(_loadNonTokenizeBigram.getAlStemmedCrossCheckedBgs(), ph);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int GetPhIndexAlHm(int phLength, String ph) {
		
		try{
			if(phLength==1){
				if(_loadNonTokenizeUnigram.getHmStemmedCrossCheckedUniIndex().containsKey(ph)){
					return _loadNonTokenizeUnigram.getHmStemmedCrossCheckedUniIndex().get(ph);
				}
			}
			else if(phLength==2){
				int subtract = ph.charAt(0) - '0' - 39;
				int alIndex = subtract < 0 ? ph.charAt(0) - '0' : subtract;
				//System.out.println("alIndex="+alIndex+",ph="+ph);
				//System.out.println(" _loadNonTokenizeBigram.getAlHmStemmedCrossCheckedBgIndex().get(alIndex)="
				//+ _loadNonTokenizeBigram.getAlHmStemmedCrossCheckedBgIndex().get(alIndex).containsKey(ph));
				if(_loadNonTokenizeBigram.getAlHmStemmedCrossCheckedBgIndex().get(alIndex).containsKey(ph)){
					return _loadNonTokenizeBigram.getAlHmStemmedCrossCheckedBgIndex().get(alIndex).get(ph);
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int GetPhFreqAl(int phLength, int phIndex) {
		
		try{
			if(phIndex>=0){
				if(phLength==1){
					
					return _loadNonTokenizeUnigram.getAlStemmedCrossCheckedUniFreqs().get(phIndex);
				}
				else if(phLength==2){
					return _loadNonTokenizeBigram.getAlStemmedCrossCheckedBgFreqs().get(phIndex);
				}
			}		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public double filterNormalizedWeightBySTDAndComputeSimilarityCtxIdFreqArrayFast(int[] ph1CtxIdFreqArray, int[] ph2CtxIdFreqArray, double cp1, double cp2) {
		
		double score = 0;
		
		try{
			HashMap<Integer, Double> hmSmall = ConvertCtxIdFreqToMap(ph1CtxIdFreqArray);
			HashMap<Integer, Double> hmBig = new HashMap<Integer, Double>();
			
			HashMap<Integer, Double> commonCunts = new HashMap<Integer, Double>();
            double sumOfWeight = 0.0;
            
            for(int i=0;i<ph2CtxIdFreqArray.length;i=i+2){
            	
            	Integer term = ph2CtxIdFreqArray[i];
            	
            	hmBig.put(term, (double)ph2CtxIdFreqArray[i+1]);
            	
            	if(hmSmall.containsKey(term)){
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
            for (Integer key : commonCunts.keySet()) {
                sumOfSquare = sumOfSquare + Math.pow(commonCunts.get(key) - meanWeight, 2.0);
            }

            double sd = Math.sqrt(sumOfSquare / commonCunts.size());
            double upperBound = (meanWeight + sd);
            double lowerBound = (meanWeight - sd);

            double nonCommonCount = 0;
            double filteredCommonCount = 0;
            double commonSumRatioWeight =0;
            
            for (Integer key : commonCunts.keySet()) {
                if (commonCunts.get(key) >= lowerBound && commonCunts.get(key) <= upperBound) {
                    
                    double min = hmSmall.get(key);
                    double max = hmBig.get(key);
                    if (min > max) {
                        double temp = min;
                        min = max;
                        max = temp;
                    }
                    commonSumRatioWeight = commonSumRatioWeight + (min / max) * (hmSmall.get(key) + hmBig.get(key));
                    
                    filteredCommonCount++;
                }
                else{
                	nonCommonCount++;
                }
            }
            
            int smallSize = hmSmall.size()-(int)nonCommonCount;
            int bigSize = hmBig.size()-(int)nonCommonCount;
            
            double cosSim = filteredCommonCount/(Math.sqrt(smallSize)*Math.sqrt(bigSize));
            score = normalizeSimilarity(cp1, cp2, cosSim * commonSumRatioWeight);
            
            if(cosSim>1){
            	System.out.println("Error: cosSim > 1 " +cp1+","+cp2);
            }

            commonCunts.clear();
            hmSmall.clear();
            hmBig.clear();
            
            hmBig = null;
            hmSmall = null;
            commonCunts = null;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return score;
	}
	
	public double filterNormalizedWeightBySTDAndComputeSimilarityCtxIdFreqArrayFastMultiThreaded(	int[] ph1CtxIdFreqArray, int[] ph2CtxIdFreqArray, int countPh1,
			int countPh2) {
		
		double score = 0;
		
		try{
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return score;
	}
	
	public double filterNormalizedWeightBySTDAndComputeSimilarityFast(
			HashMap<Integer, Double> hmSmall, HashMap<Integer, Double> hmBig,
			double cp1, double cp2) {
		double score = 0;
		
        try {

            Set<Integer> smallTerms = hmSmall.keySet();

            HashMap<Integer, Double> commonCunts = new HashMap<Integer, Double>();
            double sumOfWeight = 0.0;

            for (Integer term : smallTerms) {
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
            for (Integer key : commonCunts.keySet()) {
                sumOfSquare = sumOfSquare + Math.pow(commonCunts.get(key) - meanWeight, 2.0);
            }

            double sd = Math.sqrt(sumOfSquare / commonCunts.size());
            double upperBound = (meanWeight + sd);
            double lowerBound = (meanWeight - sd);

            double nonCommonCount = 0;
            double filteredCommonCount = 0;
            double commonSumRatioWeight =0;
            
            for (Integer key : commonCunts.keySet()) {
                if (commonCunts.get(key) >= lowerBound && commonCunts.get(key) <= upperBound) {
                    
                    double min = hmSmall.get(key);
                    double max = hmBig.get(key);
                    if (min > max) {
                        double temp = min;
                        min = max;
                        max = temp;
                    }
                    commonSumRatioWeight = commonSumRatioWeight + (min / max) * (hmSmall.get(key) + hmBig.get(key));
                    
                    filteredCommonCount++;
                }
                else{
                	nonCommonCount++;
                }
            }
            
            int smallSize = hmSmall.size()-(int)nonCommonCount;
            int bigSize = hmBig.size()-(int)nonCommonCount;
            
            double cosSim = filteredCommonCount/(Math.sqrt(smallSize)*Math.sqrt(bigSize));
            score = normalizeSimilarity(cp1, cp2, cosSim * commonSumRatioWeight);
            
            if(cosSim>1){
            	System.out.println("cosSim > 1 " +cp1+","+cp2);
            }

            commonCunts.clear();
            commonCunts = null;
            
            hmSmall.clear();
            hmBig.clear();
            
            hmBig = null;
            hmSmall = null;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
      
        return score;
	}
	
	public ArrayList<HashMap<Integer, Double>> filterNormalizedWeightBySTD(
			HashMap<Integer, Double> hmSmall, HashMap<Integer, Double> hmBig,
			double cp1, double cp2) {
		ArrayList<HashMap<Integer, Double>> lhm = new ArrayList<HashMap<Integer, Double>>();
        try {

            Set<Integer> smallTerms = hmSmall.keySet();

            HashMap<Integer, Double> hm1 = new HashMap<Integer, Double>();
            HashMap<Integer, Double> hm2 = new HashMap<Integer, Double>();

            HashMap<Integer, Double> commonCunts = new HashMap<Integer, Double>();
            double sumOfWeight = 0.0;

            for (Integer term : smallTerms) {
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
            for (Integer key : commonCunts.keySet()) {
                sumOfSquare = sumOfSquare + Math.pow(commonCunts.get(key) - meanWeight, 2.0);
            }

            double sd = Math.sqrt(sumOfSquare / commonCunts.size());
            double upperBound = (meanWeight + sd);
            double lowerBound = (meanWeight - sd);

            for (Integer key : commonCunts.keySet()) {
                if (commonCunts.get(key) >= lowerBound && commonCunts.get(key) <= upperBound) {
                    hm1.put(key, hmSmall.get(key));
                    hm2.put(key, hmBig.get(key));
                }
            }

            lhm.add(hm1);
            lhm.add(hm2);
        } catch (Exception e) {
            e.printStackTrace();
        }
      
        return lhm;
	}
	
	public ArrayList<HashMap<Integer, Double>> getCommonVectors(HashMap<Integer, Double> hmSmall, HashMap<Integer, Double> hmBig)  {
        ArrayList<HashMap<Integer, Double>> lhm = new ArrayList<HashMap<Integer, Double>>();
        try {

            HashMap<Integer, Double> hm1= new HashMap<Integer, Double>();
            HashMap<Integer, Double> hm2= new HashMap<Integer, Double>();
            
            Set<Integer> smallTerms = hmSmall.keySet();

            for (Integer term : smallTerms) {
                if (hmBig.containsKey(term)) {
                    hm1.put(term, hmSmall.get(term));
                    hm2.put(term, hmBig.get(term));
                }
            }
            
            lhm.add(hm1);
            lhm.add(hm2);

        } catch(Exception e){
			e.printStackTrace();
		}
        return lhm;
    }
	
	public ArrayList<Integer> getComMinusFilteredKeys(HashMap<Integer, Double> hmBig, HashMap<Integer, Double> hmSmall) {
        ArrayList<Integer> commonNotInFilteredList = new ArrayList<Integer>();
        try {
            for (Integer key : hmBig.keySet()) {
                if (!hmSmall.containsKey(key)) {
                    commonNotInFilteredList.add(key);
                }
            }
        } catch(Exception e){
			e.printStackTrace();
		}
        return commonNotInFilteredList;
    }
	
	public HashMap<Integer, Double> removeKeys(HashMap<Integer, Double> hmPhCos, ArrayList<Integer> keys)  {
        try {
            for (Integer key : keys) {
                hmPhCos.remove(key);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return hmPhCos;
    }
	
	public double getCosineSimilarity(HashMap<Integer, Double> hmSmall,
            HashMap<Integer, Double> hmBig, double cp1, double cp2) {
        try {

            Set<Integer> smallTerms = hmSmall.keySet();
            boolean anyCommonTerm = false;
            double sumSamllSquare = 0;
            double sum = 0;
            for (Integer term : smallTerms) {
                //double termWeightSmall = 1 - Math.log(hmSmall.get(term))/(1+Math.log(countmax)) ;
                //double termWeightSmall = Math.log(hmSmall.get(term))/Math.log(countmax);
                //double termWeightSmall = Math.log(hmSmall.get(term)/countmax);
                double termWeightSmall = 1;
                //double termWeightSmall = Math.log(hmSmall.get(term)) / Math.log(cp1);

                sumSamllSquare = sumSamllSquare + termWeightSmall * termWeightSmall;

                if (hmBig.containsKey(term)) {
                    anyCommonTerm = true;
                    //double termWeightBig=1- Math.log(hmBig.get(term))/(1+Math.log(countmax)) ;
                    //double termWeightBig = Math.log(hmBig.get(term)/countmax);
                    double termWeightBig = 1;
                    //double termWeightBig = Math.log(hmBig.get(term)) / Math.log(cp2);
                    sum = sum + termWeightSmall * termWeightBig;
                    //sum = sum + termWeightSmall * 1;
                }
            }

            if (anyCommonTerm) {
                double sumBigSquare = 0;
                Set<Integer> bigTerms = hmBig.keySet();
                for (Integer term : bigTerms) {
                    //double termWeightBig =1- Math.log(hmBig.get(term))/(1+Math.log(countmax)) ;
                    //double termWeightBig = Math.log(hmBig.get(term))/Math.log(countmax) ;
                    //double termWeightBig = Math.log(hmBig.get(term)/countmax);
                    double termWeightBig = 1;
                    //double termWeightBig = Math.log(hmBig.get(term)) / Math.log(cp2);
                    sumBigSquare = sumBigSquare + termWeightBig * termWeightBig;
                }
                return sum / (Math.sqrt(sumSamllSquare) * Math.sqrt(sumBigSquare));
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return 0;
    }
	
	public double getCommonCount(HashMap<Integer, Double> hmPh1Key, HashMap<Integer, Double> hmPh2Key) {
        double commonCount = 0.0;
        try {
            
            for (Integer key : hmPh1Key.keySet()) {
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
            e.printStackTrace();
        }
        return commonCount;
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
            throw e;
        }
        return score;
	 }

	 public HashMap<Integer, Double> ConvertCtxIdFreqToMap(int[] phCtxIdFreqArray) {
			
			HashMap<Integer, Double> hmPh = new HashMap<Integer, Double>();
			
			try{
				for(int i=0;i<phCtxIdFreqArray.length;i=i+2){
					hmPh.put(phCtxIdFreqArray[i], (double)phCtxIdFreqArray[i+1]);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return hmPh;
		}

//	public int[] ConvertHashMapToIdFreq(LinkedHashMap<Integer, Integer> lhmIdFreq) {
//		int[] phCtxIdFreqArray  = new int[lhmIdFreq.size()*2];
//		try{
//			int index = 0;
//			for(int id: lhmIdFreq.keySet()){
//				phCtxIdFreqArray[index++] = id;
//				phCtxIdFreqArray[index++] = lhmIdFreq.get(id);
//			}
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return phCtxIdFreqArray;
//	}

	
}
