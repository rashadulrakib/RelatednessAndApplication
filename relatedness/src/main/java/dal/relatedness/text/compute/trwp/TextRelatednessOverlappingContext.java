package dal.relatedness.text.compute.trwp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.DocClusterUtil;
import dal.relatedness.phrase.compute.tokenized.PhraseRelatednessTokenized;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.text.processor.TextExtractorUtil;
import dal.utils.common.compute.ComputeUtil;

public class TextRelatednessOverlappingContext {

	PhraseRelatednessTokenized phraseRelatednessTokenized;

	public DocClusterUtil docClusterUtil;
	
	public TextRelatednessOverlappingContext(){
		phraseRelatednessTokenized = new PhraseRelatednessTokenized();
		docClusterUtil = new DocClusterUtil();
	}
	
	public void ComputeTextRelatednessUsingUnigramOverlappingContext(){
		try{
			ArrayList<String[]> sentencePairs = TextExtractorUtil.extractSentencePairsFromSemEval2015();
			
			for (String[] pairs : sentencePairs) {
				
				String text1 = docClusterUtil.textUtilShared.PerformPreprocess(pairs[0]);
				String text2 = docClusterUtil.textUtilShared.PerformPreprocess(pairs[1]);
				
				 ArrayList<String> doc1  = docClusterUtil.textUtilShared.StemByEachWord(  docClusterUtil.textUtilShared.RemoveStopWord(text1));
				 ArrayList<String> doc2  = docClusterUtil.textUtilShared.StemByEachWord(  docClusterUtil.textUtilShared.RemoveStopWord(text2));
				 
				 HashSet<String> commonWords = docClusterUtil.textUtilShared.GetCommonWords(doc1, doc2);
				 
				 doc1 = docClusterUtil.textUtilShared.RemoveCommonWords(doc1, commonWords);
				 doc2 = docClusterUtil.textUtilShared.RemoveCommonWords(doc2, commonWords);
				 
				 
				 
				 ///text1
				 double sumOfFreqWord1 = 0;
				 LinkedHashMap<Integer, Integer> lhmIdFreqWord1 = new LinkedHashMap<Integer, Integer>();
				 
				 for(String stemmedWord: doc1){
					 
					 int wordId = phraseRelatednessTokenized.phRelComputeUtil.GetPhIndexAlHm(1, stemmedWord);
					 int wordFreq = phraseRelatednessTokenized.phRelComputeUtil.GetPhFreqAl(1, wordId);
					 sumOfFreqWord1 = sumOfFreqWord1+ wordFreq;
					 
					 int [] ctxIdFreqArr = phraseRelatednessTokenized.GetPhCtxArray(1, wordId);
					 
					 if(ctxIdFreqArr!=null){
						 for(int i=0;i<ctxIdFreqArr.length;i=i+2){
							 if(!lhmIdFreqWord1.containsKey(ctxIdFreqArr[i])){
								 lhmIdFreqWord1.put(ctxIdFreqArr[i], ctxIdFreqArr[i+1]); 
							 }
							 else{
								 lhmIdFreqWord1.put(ctxIdFreqArr[i], lhmIdFreqWord1.get(ctxIdFreqArr[i]) + ctxIdFreqArr[i+1]); 
							 }
						}
					 }
				 }
				 
				// int [] ctxIdFreqArrmerged1 = phraseRelatednessTokenized.phRelComputeUtil.ConvertHashMapToIdFreq(lhmIdFreqWord1);
				 
				 
				 ///text2
				 double sumOfFreqWord2 = 0;
				 LinkedHashMap<Integer, Integer> lhmIdFreqWord2 = new LinkedHashMap<Integer, Integer>();
				 
				 for(String stemmedWord: doc2){
					 
					 int wordId = phraseRelatednessTokenized.phRelComputeUtil.GetPhIndexAlHm(1, StemmingUtil.stemPhrase(stemmedWord));
					 int wordFreq = phraseRelatednessTokenized.phRelComputeUtil.GetPhFreqAl(1, wordId);
					 sumOfFreqWord2 = sumOfFreqWord2+ wordFreq;
					 
					 int [] ctxIdFreqArr = phraseRelatednessTokenized.GetPhCtxArray(1, wordId);
					 
					 if(ctxIdFreqArr!=null){
						 for(int i=0;i<ctxIdFreqArr.length;i=i+2){
							 if(!lhmIdFreqWord2.containsKey(ctxIdFreqArr[i])){
								 lhmIdFreqWord2.put(ctxIdFreqArr[i], ctxIdFreqArr[i+1]); 
							 }
							 else{
								 lhmIdFreqWord2.put(ctxIdFreqArr[i], lhmIdFreqWord2.get(ctxIdFreqArr[i]) + ctxIdFreqArr[i+1]); 
							 }
						}
					 }
					 
				 }
				 
				 //int [] ctxIdFreqArrmerged2 = phraseRelatednessTokenized.phRelComputeUtil.ConvertHashMapToIdFreq(lhmIdFreqWord2);
				 
				 double docSim = computeDocSimilarityByPhraseSimNotionFast(lhmIdFreqWord1, lhmIdFreqWord2, sumOfFreqWord1, sumOfFreqWord2);
				 
				 System.out.println(docSim);
				 
			}
		}
		 catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public double computeDocSimilarityByPhraseSimNotionFast(LinkedHashMap<Integer, Integer> lhmIdFreqWord1, 	LinkedHashMap<Integer, Integer> lhmIdFreqWord2, double sumOfFreqWord1, double sumOfFreqWord2) {
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
            score = ComputeUtil.NormalizeSimilarity(sumOfFreqWord1, sumOfFreqWord2, cosSim * commonSumRatioWeight);
            
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
}
