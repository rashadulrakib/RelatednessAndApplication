package dal.relatedness.text.compute.tokenized;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.DocClusterUtil;
import dal.relatedness.phrase.compute.tokenized.PhraseRelatednessTokenized;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.text.processor.TextExtractorUtil;

public class TextRelatednessTokenizedOverlappingContext {

	PhraseRelatednessTokenized phraseRelatednessTokenized;

	public DocClusterUtil docClusterUtil;
	
	public TextRelatednessTokenizedOverlappingContext(){
		phraseRelatednessTokenized = new PhraseRelatednessTokenized();
		docClusterUtil = new DocClusterUtil();
	}
	
	public void ComputeTextRelatednessUsingUnigramOverlappingContext(){
		try{
			ArrayList<String[]> sentencePairs = TextExtractorUtil.extractSentencePairsFromSemEval2015();
			
			for (String[] pairs : sentencePairs) {
				
				String text1 = docClusterUtil.PerformPreprocess(pairs[0]);
				String text2 = docClusterUtil.PerformPreprocess(pairs[1]);
				
				 ArrayList<String> doc1  = docClusterUtil.StemByEachWord(  docClusterUtil.RemoveStopWord(text1));
				 ArrayList<String> doc2  = docClusterUtil.StemByEachWord(  docClusterUtil.RemoveStopWord(text2));
				 
				 HashSet<String> commonWords = docClusterUtil.GetCommonWords(doc1, doc2);
				 
				 doc1 = docClusterUtil.RemoveCommonWords(doc1, commonWords);
				 doc2 = docClusterUtil.RemoveCommonWords(doc2, commonWords);
				 
				 
				 
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
				 
				 double docSim = docClusterUtil.ComputeDocSImilarityByPhraseSimNotionFast(lhmIdFreqWord1, lhmIdFreqWord2, sumOfFreqWord1, sumOfFreqWord2);
				 
				 System.out.println(docSim);
				 
			}
		}
		 catch (Exception e) {
				e.printStackTrace();
			}
	}
}
