package dal.relatedness.phrase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dal.relatedness.phrase.bigram.LoadBigram;
import dal.relatedness.phrase.entities.bigram.BigramKey;
import dal.relatedness.phrase.entities.fourgram.FourgramAsBigramContext;
import dal.relatedness.phrase.fourgram.LoadFourGram;
import dal.relatedness.phrase.unigram.LoadUnigram;
import dal.relatedness.phrase.utils.common.Base62;

public class Relatedness {
	
	Map<String, Integer> unigramTokenWithIds;
	Map<BigramKey, Integer> mergedBigramTokenWithIds = new HashMap<BigramKey, Integer>();
	HashMap<Integer, FourgramAsBigramContext> mergedFourGramTokenWithIds = new HashMap<Integer, FourgramAsBigramContext>();
	LoadBigram loadBgObj;

	String phPairsFile = "D:\\phrasePairs.txt";
	
	double N = 3107547215.0;
	
	public void Compute() throws Exception{
		unigramTokenWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();
		ArrayList<Map<BigramKey, Integer>> bigramTokenWithIds = new LoadBigram().populateBigramsWithIdsInParallel();
		ArrayList<HashMap<Integer, FourgramAsBigramContext>> fourGramTokenWithIds = new LoadFourGram().populateFourGramsWithIdAndContextsInParallel();
		
		for(Map<BigramKey, Integer> var: bigramTokenWithIds){
			mergedBigramTokenWithIds.putAll(var);
		}
		
		for(HashMap<Integer, FourgramAsBigramContext> var: fourGramTokenWithIds){
			mergedFourGramTokenWithIds.putAll(var);
		}
	
		System.out.println("Start="+new Date().toString());
		
		String sCurrentLine;
		BufferedReader br = new BufferedReader(new FileReader(phPairsFile));

		while ((sCurrentLine = br.readLine()) != null) {
			
			sCurrentLine = sCurrentLine.toLowerCase();
			String []arr = sCurrentLine.split(",");
			
			System.out.println(arr[0] +","+arr[1]+"=" +ComputeRelatedness(arr[0], arr[1]));
		}
		
		br.close();
	
		//System.out.println(ComputeRelatedness("general principle", "basic rule"));
		
		System.out.println("End="+new Date().toString());
	}
	
	public void ComputeByDoubleKey(){
		loadBgObj = new LoadBigram();
		unigramTokenWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();
		loadBgObj.PopulateBgDoubleKeyIdFreq();
		
		//int ph1Freq = GetPhFreqDoubleKey("large number");
		//System.out.println("large number->freq="+ph1Freq);
	}
	
//	private int GetPhFreqDoubleKey(String ph) {
//		int freq = 0;
//		
//		ph = ph.toLowerCase();
//		
//		String [] arr = ph.split("\\s+");
//		BigramKey bgDoubleKey = new BigramKey(unigramTokenWithIds.get(arr[0]), unigramTokenWithIds.get(arr[1])); 
//		
//		int id = loadBgObj.getBgDoubleKeyIds().get(bgDoubleKey);
//		freq = loadBgObj.getBgFreqs().get(id);
//		
//		return freq;	
//	}

	public void ComputeByBase62Ids(){
		try{
			loadBgObj = new LoadBigram();
			unigramTokenWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();
			loadBgObj.PopulateBgKey62IdFreq();
			
			int ph1Freq = GetPhFreqBase62("abc animal");
			System.out.println("abc animal->freq="+ph1Freq);
		}
		catch(Exception e){
			
		}
	}
	
	public void ComputeBySumId(){
		try{
			loadBgObj = new LoadBigram();
			//unigramTokenWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();
			loadBgObj.PopulateBgSumKeyIdFreq();
		}
		catch(Exception e){
			
		}
	}
	
	private int GetPhFreqBase62(String ph) {
		int freq = 0;
		
		ph = ph.toLowerCase();
		
		String [] arr = ph.split("\\s+");
		String bgKey62 = Base62.fromBase10(unigramTokenWithIds.get(arr[0]))+Base62.fromBase10(unigramTokenWithIds.get(arr[1]));
		
		int id = loadBgObj.getBgKeyBase62Ids().get(bgKey62);
		freq = loadBgObj.getBgFreqs().get(id);
		
		return freq;		
	}

	private double ComputeRelatedness(String ph1, String ph2) throws Exception{
		
		String [] ph1Arr = ph1.split("\\s+");
		String [] ph2Arr = ph2.split("\\s+");
		
		int ph1uni1 = unigramTokenWithIds.get(ph1Arr[0]);
		int ph1uni2 = unigramTokenWithIds.get(ph1Arr[1]);
		
		int ph2uni1 = unigramTokenWithIds.get(ph2Arr[0]);
		int ph2uni2 = unigramTokenWithIds.get(ph2Arr[1]);
		
		BigramKey bgKey1 = new BigramKey(ph1uni1, ph1uni2);
		BigramKey bgKey2 = new BigramKey(ph2uni1, ph2uni2);
		
		if(!mergedBigramTokenWithIds.containsKey(bgKey1)) return -1;
		if(!mergedBigramTokenWithIds.containsKey(bgKey2)) return -1;
		
		int ph1Id = mergedBigramTokenWithIds.get(bgKey1);
		int ph2Id = mergedBigramTokenWithIds.get(bgKey2);
		
		if(!mergedFourGramTokenWithIds.containsKey(ph1Id)) return -1;
		if(!mergedFourGramTokenWithIds.containsKey(ph2Id)) return -1;
		
		FourgramAsBigramContext ph1FreqContxetarr = mergedFourGramTokenWithIds.get(ph1Id);
		FourgramAsBigramContext ph2FreqContxetarr = mergedFourGramTokenWithIds.get(ph2Id);
		
		int ph1Freq = ph1FreqContxetarr.bgFrq;
		int ph2Freq = ph2FreqContxetarr.bgFrq;
		
		double sumRatios =0;
		
		if(ph1FreqContxetarr.contextIdFreq.size()<ph2FreqContxetarr.contextIdFreq.size()){
			for(Integer id: ph1FreqContxetarr.contextIdFreq.keySet()){
				if(ph2FreqContxetarr.contextIdFreq.containsKey(id)){
					int freq1 = ph1FreqContxetarr.contextIdFreq.get(id);
					int freq2 = ph2FreqContxetarr.contextIdFreq.get(id);
					
					sumRatios = sumRatios+ getSumRatio(freq1, freq2, ph1Freq, ph2Freq);
				}
			}
		}else{
			for(Integer id: ph2FreqContxetarr.contextIdFreq.keySet()){
				if(ph1FreqContxetarr.contextIdFreq.containsKey(id)){
					int freq1 = ph1FreqContxetarr.contextIdFreq.get(id);
					int freq2 = ph2FreqContxetarr.contextIdFreq.get(id);
		
					sumRatios = sumRatios+ getSumRatio(freq1, freq2, ph1Freq, ph2Freq);
				}
			}
		}
		
		return normalizeByNGD(ph1Freq, ph2Freq, sumRatios);
	}
	
	
	private double getSumRatio (int freq1, int freq2, int ph1Freq, int ph2Freq){
		double freq1Fraction = (double)freq1 / (double)ph1Freq;
		double freq2Fraction = (double)freq2 / (double)ph2Freq;
		
		double min = freq1Fraction, max = freq2Fraction;
		if(min>max) {
			min = freq2Fraction;
			max = freq1Fraction;
		}
		
		return (min/max)*(freq1 + freq2);
	}
	
	private double normalizeByNGD(double countPh1, double countPh2, double commonCount) throws Exception {
        double score = 0.0;
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
}
