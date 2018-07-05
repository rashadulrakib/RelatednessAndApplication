package dal.relatedness.text.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import dal.clustering.document.shared.PairSim;
import dal.relatedness.phrase.compute.tokenized.PhraseRelatednessTokenized;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.text.compute.gtm.TextRelatednessGtm;
import dal.relatedness.text.compute.trwp.TextRelatednessTrwpConstant;
import dal.utils.common.general.TextUtilShared;

public class TextRelatednessFunctionalUtil {
	
	ArrayList<String> phrasesOfAText;
	
	PhraseRelatednessTokenized phraseRelatednessTokenized;
	TextUtilShared textUtilShared;
	static HashMap<String, Double> hmWordPhPairSim = new HashMap<String, Double>();
	TextRelatednessGtm textRelatednessGtm;
	
	public TextRelatednessFunctionalUtil(PhraseRelatednessTokenized phraseRelatednessTokenized, TextRelatednessGtm textRelatednessGtm, 
			TextUtilShared textUtilShared){
		this.phraseRelatednessTokenized = phraseRelatednessTokenized;
		this.textUtilShared = textUtilShared;
		this.textRelatednessGtm = textRelatednessGtm;
		//hmWordPhPairSim = new HashMap<String, Double>();
	}
	
//	public TextRelatednessFunctionalUtil(PhraseRelatednessTokenized phraseRelatednessTokenized, TextUtilShared textUtilShared){
//		this.phraseRelatednessTokenized = phraseRelatednessTokenized;
//		this.textUtilShared = textUtilShared;
//	}
	
	public ArrayList<ArrayList<String>> GenerateWordsPhrasesEachText(
			ArrayList<String> alDocLabelFlatWithStopWords, TextRelatednessGoogleNgUtil textRelatednessGoogleNgUtil) {
		ArrayList<ArrayList<String>> wordsPhrasesEachText = new ArrayList<ArrayList<String>>();
		
		try{
			for(String text: alDocLabelFlatWithStopWords){
//				ArrayList<String > candStrs = textRelatednessGoogleNgUtil.splitByStopWord(text);
//				
//				ArrayList<String> phs = textRelatednessGoogleNgUtil.SplitPhrases(GeneratePhsByFreq(candStrs));
//				
//				ArrayList<String> phWordList = textUtilShared.CombineWordPhs(phs, candStrs);
				
				ArrayList<String> phWordList = textUtilShared.convertAarryToArrayList(text.split("\\s+"));
				
				System.out.println(phWordList);
				
				wordsPhrasesEachText.add(phWordList);
			}
			
			System.out.println("wordsPhrasesEachText="+wordsPhrasesEachText.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return wordsPhrasesEachText;
	}
	
	public ArrayList<String> GeneratePhsByFreq(ArrayList<String> candStrs) {
		if (phrasesOfAText != null) {
			phrasesOfAText.clear();
			phrasesOfAText = null;
		}
		phrasesOfAText = new ArrayList<String>();
		try {
			for (String s : candStrs) {
				s = s.trim();
				if (s.isEmpty()) {
					continue;
				}

				if (s.split("\\s+").length < 2) {
					continue;
				}

				binarySplitCandidate(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phrasesOfAText;
	}

	public ArrayList<ArrayList<PairSim>> GetWeightedSimilarityMatrix(
			ArrayList<String> restPhWords1,	ArrayList<String> restPhWords2, HashSet<String> notUsefulWPhPairsStemmed) {
		ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
		
//		if(hmWordPhPairSim!=null){
//			hmWordPhPairSim.clear();
//			hmWordPhPairSim = null;
//		}
//		
		//hmWordPhPairSim = new HashMap<String, Double>();
		
		try {

			for (String phWord1 : restPhWords1) {
				ArrayList<PairSim> simPairList = new ArrayList<PairSim>();
				for (String phWord2 : restPhWords2) {

					String phWord1arr1[] = phWord1.split("\\s+");
					String phWord1arr2[] = phWord2.split("\\s+");
					
					int wordsInPh1 = phWord1arr1.length;
					int wordsInPh2 = phWord1arr2.length;
					double totalWords = wordsInPh1 + wordsInPh2;
					double weightForWords = totalWords / 2;

					String stemwph1 = StemmingUtil.stemPhrase(phWord1);
					String stemwph2 = StemmingUtil.stemPhrase(phWord2);

					String stemkey = stemwph1 + "," + stemwph2;

					double simOverlap = 0.0;

					if (stemwph1.equals(stemwph2)) {
						simOverlap = 1.0 * weightForWords * weightForWords;
					} else {
						
//						if(notUsefulWPhPairsStemmed.contains(stemkey)){
//							continue;
//						}
//						else
						{
							if(weightForWords ==1.0){
								//String wordKey = wordsInPh1+"," +wordsInPh2;
//								if(hmWordPhPairSim.containsKey(stemkey)){
//									simOverlap = hmWordPhPairSim.get(stemkey);
//								}
//								else if(hmWordPhPairSim.containsKey(stemwph2 + "," + stemwph1)){
//									simOverlap = hmWordPhPairSim.get(stemwph2 + "," + stemwph1);
//								}else
								{
									simOverlap = textRelatednessGtm.ComputeWordSimGTM(phWord1, phWord2);
								}
								
								//hmWordPhPairSim.put(stemkey, simOverlap);
							}
							else if(weightForWords==1.5){
								double maxSim = 0;
								
								for(String w1: phWord1arr1){
									for(String w2: phWord1arr2){
										double interSim = textRelatednessGtm.ComputeWordSimGTM(w1, w2);
										if(maxSim < interSim){
											maxSim = interSim;
										}
									}
								}
								
								simOverlap = maxSim * weightForWords * weightForWords;
								
							}else if(weightForWords==2.0){
//								if(hmWordPhPairSim.containsKey(stemkey)){
//									simOverlap = hmWordPhPairSim.get(stemkey);
//								}else if(hmWordPhPairSim.containsKey( stemwph2 + "," + stemwph1)){
//									simOverlap = hmWordPhPairSim.get(stemwph2 + "," + stemwph1);
//								}else
								{
									simOverlap = phraseRelatednessTokenized.ComputeFastRelExternal(stemwph1, stemwph2)* weightForWords * weightForWords;
								}
								System.out.println(stemkey+", ");
								//hmWordPhPairSim.put(stemkey, simOverlap);
							}else{
								System.out.println(stemkey+", bad weight factror="+ weightForWords);
							}
						}
					}

					simPairList.add(new PairSim(stemkey, simOverlap));
				}
				t1t2simPairList.add(simPairList);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t1t2simPairList;
	}
	
	private void binarySplitCandidate(String s) {
		try {

			if (s.trim().isEmpty()) {
				return;
			}

			String[] ws = s.split("\\s+");

			if (ws.length >= 2) {
				double maxfreq = 0;
				String maxBg = "";
				int i1 = 0;
				int i2 = 0;
				for (int i = 0; i < ws.length - 1; i++) {
					String bg = (ws[i] + " " + ws[i + 1]).trim();
			
					if (bg.isEmpty()) {
						continue;
					}

					double bgFreq = getBgFreq(StemmingUtil.stemPhrase(bg));

					if (bgFreq >= maxfreq
							&& bgFreq >= (double) (TextRelatednessTrwpConstant.MeanBgFreq + TextRelatednessTrwpConstant.Std)) {
						maxfreq = bgFreq;
						maxBg = bg;
						i1 = i;
						i2 = i + 1;
					}

				}

				if (maxfreq > 0 && !maxBg.isEmpty() && i1 != i2) {
					phrasesOfAText.add(maxBg);
					String sLeft = textUtilShared.GetSubStr(ws, 0, i1 - 1);
					String sRight = textUtilShared.GetSubStr(ws, i2 + 2,
							ws.length);
					binarySplitCandidate(sLeft);
					binarySplitCandidate(sRight);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private double getBgFreq(String bg) {

		double bgFreq = 0;

		try {

			int bgIndex = phraseRelatednessTokenized.phRelComputeUtil
					.GetPhIndexAlHm(2, bg);

			if (bgIndex < 0) {
				return 0;
			}

			bgFreq = phraseRelatednessTokenized.phRelComputeUtil.GetPhFreqAl(2,
					bgIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bgFreq;
	}
	
}