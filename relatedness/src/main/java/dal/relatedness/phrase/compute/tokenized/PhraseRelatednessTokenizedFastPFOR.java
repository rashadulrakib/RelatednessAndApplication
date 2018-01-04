package dal.relatedness.phrase.compute.tokenized;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.lemire.integercompression.differential.IntegratedIntCompressor;
import dal.relatedness.phrase.constants.PhraseFileNames;
import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;
import dal.relatedness.phrase.load.nontokenized.unigram.LoadNonTokenizeUnigram;
import dal.relatedness.phrase.load.tokenized.fourgram.LoadTokenizedFourgramFastPFOR;
import dal.relatedness.phrase.load.tokenized.trigram.LoadTokenizedTrigramFastPFOR;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;

public class PhraseRelatednessTokenizedFastPFOR {
	
	public LoadNonTokenizeUnigram loadNonTokenizeUnigram;
	public LoadNonTokenizeBigram loadNonTokenizeBigram;
	public LoadTokenizedTrigramFastPFOR loadTokenizedTrigramFastPFOR;
	public LoadTokenizedFourgramFastPFOR loadTokenizedFourgramFastPFOR;
	public PhRelComputeUtil phRelComputeUtil;
	IntegratedIntCompressor iic;
	
//	public PhraseRelatednessTokenizedFastPFOR(){
//		loadNonTokenizeUnigram = new LoadNonTokenizeUnigram();
//		loadNonTokenizeBigram = new LoadNonTokenizeBigram();
//		
//		loadTokenizedTrigramFastPFOR = new LoadTokenizedTrigramFastPFOR(loadNonTokenizeUnigram);
//		loadTokenizedFourgramFastPFOR = new LoadTokenizedFourgramFastPFOR(loadNonTokenizeBigram);
//		
//		loadNonTokenizeUnigram.PopulateStemmedUniGramsList();
//		loadNonTokenizeBigram.PopulateStemmedBiGramsList();
//		
//		loadTokenizedFourgramFastPFOR.PopualteTokenizedFourgramFastPFOR();
//		loadTokenizedTrigramFastPFOR.PopualteTokenizedTrigramFastPFOR();
//		
//		phRelComputeUtil = new PhRelComputeUtil(loadNonTokenizeUnigram, loadNonTokenizeBigram);
//		iic = new IntegratedIntCompressor();
//	}
	
	public PhraseRelatednessTokenizedFastPFOR(){
		loadNonTokenizeUnigram = new LoadNonTokenizeUnigram();
		loadNonTokenizeBigram = new LoadNonTokenizeBigram();
		
		loadTokenizedTrigramFastPFOR = new LoadTokenizedTrigramFastPFOR(loadNonTokenizeUnigram);
		loadTokenizedFourgramFastPFOR = new LoadTokenizedFourgramFastPFOR(loadNonTokenizeBigram);
		
		loadNonTokenizeUnigram.PopulateStemmedUniGramsHashMap();
		loadNonTokenizeBigram.PopulateStemmedBiGramsListHashMap();
		
		loadTokenizedTrigramFastPFOR.PopualteTokenizedTrigramFastPFOR();
		loadTokenizedFourgramFastPFOR.PopualteTokenizedFourgramFastPFOR();
		
		phRelComputeUtil = new PhRelComputeUtil(loadNonTokenizeUnigram, loadNonTokenizeBigram);
		iic = new IntegratedIntCompressor();
	}
	
	public void ComputeFastPFORAlHmFast(){
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(
					PhraseFileNames.PhrasePairsFile));

			ArrayList<String[]> pairs = new ArrayList<String[]>();

			while ((sCurrentLine = br.readLine()) != null) {

				if (sCurrentLine.isEmpty())
					continue;

				sCurrentLine = sCurrentLine.toLowerCase();

				String[] arr = sCurrentLine.split(",");
				String ph1 = StemmingUtil.stemPhrase(arr[0]);
				String ph2 = StemmingUtil.stemPhrase(arr[1]);

				pairs.add(new String[] { ph1, ph2 });

				// double score = ComputePhRelVByteCodingAlHm(ph1, ph2);
				// System.out.println(ph1+","+ph2+","+score);
			}

			String stTime = new Date().toString();

			for (String[] arr : pairs) {
				String ph1 = arr[0];
				String ph2 = arr[1];

				double score = ComputePhRelPFORAlHmFast(ph1, ph2);
				System.out.println(ph1 + "," + ph2 + "," + score);
			}

			System.out.println(stTime + "," + new Date().toString());

			br.close();

			System.out.println("ComputeFastPFORAlHmFast finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private double ComputePhRelPFORAlHmFast(String ph1, String ph2) {
		double score =0;
		
		try{
			
			if (ph1.equals(ph2)) {
				return 1;
			}

			String[] ph1Arr = ph1.split("\\s+");
			String[] ph2Arr = ph2.split("\\s+");

			int ph1Index = phRelComputeUtil.GetPhIndexAlHm(ph1Arr.length, ph1);
			int ph2Index = phRelComputeUtil.GetPhIndexAlHm(ph2Arr.length, ph2);

			if (ph1Index < 0 || ph2Index < 0) {
				return 0;
			}

			int countPh1 = phRelComputeUtil.GetPhFreqAl(ph1Arr.length, ph1Index);
			int countPh2 = phRelComputeUtil.GetPhFreqAl(ph2Arr.length, ph2Index);

			if (countPh1 <= 0 || countPh2 <= 0) {
				return 0;
			}

			int[] ph1CtxIdArray = GetPhCtxIdArray(ph1Arr.length, ph1Index);
			int[] ph2CtxIdArray = GetPhCtxIdArray(ph2Arr.length, ph2Index);

			int[] ph1CtxFreqArray = GetPhCtxFreqArray(ph1Arr.length, ph1Index);
			int[] ph2CtxFreqArray = GetPhCtxFreqArray(ph2Arr.length, ph2Index);

			if (ph1CtxIdArray == null || ph2CtxIdArray == null || ph1CtxFreqArray == null || ph2CtxFreqArray == null) {
				return 0;
			}

			if (ph1CtxIdArray.length != ph1CtxFreqArray.length || ph2CtxIdArray.length != ph2CtxFreqArray.length) {
				return 0;
			}

			HashMap<Integer, Double> hmPh1 = ConvertCtxIdFreqToMap(ph1CtxIdArray, ph1CtxFreqArray);
			HashMap<Integer, Double> hmPh2 = ConvertCtxIdFreqToMap(ph2CtxIdArray, ph2CtxFreqArray);
			
			score = phRelComputeUtil.filterNormalizedWeightBySTDAndComputeSimilarityFast(hmPh1, hmPh2, countPh1, countPh2);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
		return score;
	}

	public void ComputeFastPFOR(){
		try{
			
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(PhraseFileNames.PhrasePairsFile));
			
			String stTime = new Date().toString();
			
			while ((sCurrentLine = br.readLine()) != null) {
				
				if(sCurrentLine.isEmpty()) continue;
				
				sCurrentLine = sCurrentLine.toLowerCase();
				
				String [] arr = sCurrentLine.split(",");
				String ph1 = StemmingUtil.stemPhrase(arr[0]);
				String ph2 = StemmingUtil.stemPhrase(arr[1]);
				
				double score = ComputePhRelFastPFOR(ph1, ph2);
				System.out.println(ph1+","+ph2+","+score); 
			}
			
			System.out.println(stTime+","+new Date().toString());
			
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private double ComputePhRelFastPFOR(String ph1, String ph2) {
		try{
			if(ph1.equals(ph2)){
				return 1;
			}
			
			String [] ph1Arr = ph1.split("\\s+");
			String [] ph2Arr = ph2.split("\\s+");
			
			int ph1Index = phRelComputeUtil.GetPhIndexAl(ph1Arr.length, ph1);
			int ph2Index = phRelComputeUtil.GetPhIndexAl(ph2Arr.length, ph2);
			
			if(ph1Index<0 || ph2Index<0){
				return 0;
			}
			
			int countPh1 = phRelComputeUtil.GetPhFreqAl(ph1Arr.length, ph1Index);
			int countPh2 = phRelComputeUtil.GetPhFreqAl(ph2Arr.length, ph2Index);
			
			if(countPh1<=0 || countPh2<=0){
				return 0;
			}
			
			int [] ph1CtxIdArray = GetPhCtxIdArray(ph1Arr.length, ph1Index);
			int [] ph2CtxIdArray = GetPhCtxIdArray(ph2Arr.length, ph2Index);
			
			int [] ph1CtxFreqArray = GetPhCtxFreqArray(ph1Arr.length, ph1Index);
			int [] ph2CtxFreqArray = GetPhCtxFreqArray(ph2Arr.length, ph2Index);
			
			if(ph1CtxIdArray==null || ph2CtxIdArray==null || ph1CtxFreqArray==null || ph2CtxFreqArray==null){
				return 0;
			}
			
			if(ph1CtxIdArray.length!=ph1CtxFreqArray.length || ph2CtxIdArray.length!=ph2CtxFreqArray.length){
				return 0;
			}
			
			HashMap<Integer, Double> hmPh1 = ConvertCtxIdFreqToMap(ph1CtxIdArray, ph1CtxFreqArray);
			HashMap<Integer, Double> hmPh2 = ConvertCtxIdFreqToMap(ph2CtxIdArray, ph2CtxFreqArray);
			
			HashMap<Integer, Double> hmPhCos1 = hmPh1;
            HashMap<Integer, Double> hmPhCos2 = hmPh2;
            
            ArrayList<HashMap<Integer, Double>> lhm = phRelComputeUtil.filterNormalizedWeightBySTD(hmPh1, hmPh2, countPh1, countPh2);
            hmPh1 =  lhm.get(0);
            hmPh2 =  lhm.get(1);

            ArrayList<HashMap<Integer, Double>> lhmCos = phRelComputeUtil.getCommonVectors(hmPhCos1, hmPhCos2);
            HashMap<Integer, Double> hmPh1Common = lhmCos.get(0);

            ArrayList<Integer> comMinusFiltered = phRelComputeUtil.getComMinusFilteredKeys(hmPh1Common, hmPh1);
            hmPhCos1 = phRelComputeUtil.removeKeys(hmPhCos1, comMinusFiltered);
            hmPhCos2 = phRelComputeUtil.removeKeys(hmPhCos2, comMinusFiltered);

            double cosSim = phRelComputeUtil.getCosineSimilarity(hmPhCos1, hmPhCos2, countPh1, countPh2);
            //score=cosSim;
            double commonCount = phRelComputeUtil.getCommonCount(hmPh1, hmPh2);
            commonCount = commonCount * cosSim;

            double score = phRelComputeUtil.normalizeSimilarity( countPh1, countPh2, commonCount);
            
			return score;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return 0;
	}

	private HashMap<Integer, Double> ConvertCtxIdFreqToMap(int[] phCtxIdArray, int[] phCtxFreqArray) {
		HashMap<Integer, Double> hm = new HashMap<Integer, Double>();
		try{
			if(phCtxIdArray.length!=phCtxIdArray.length){
				return hm;
			}
			
			for(int i=0;i<phCtxIdArray.length;i++){
				if(i<phCtxIdArray.length -1 && phCtxIdArray[i]>phCtxIdArray[i+1]){
					System.out.println("Problem in decompressing CtxIds");
					return new HashMap<Integer, Double>();
				}
				hm.put(phCtxIdArray[i], (double)phCtxFreqArray[i]);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return hm;
	}

	private int[] GetPhCtxFreqArray(int length, int phIndex) {
		try{
			if(length==1){
				return loadTokenizedTrigramFastPFOR.getTriGmWordIdContextFreqsFastPFOR()[phIndex]; 
			}
			else if(length==2){
				return loadTokenizedFourgramFastPFOR.getfourGmPhIdContextFreqsFastPFOR()[phIndex]; 
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private int[] GetPhCtxIdArray(int length, int phIndex) {
		try{
			if(length==1){
				int[] ctxIdArray = loadTokenizedTrigramFastPFOR.getTriGmWordIdContextIdsFastPFOR()[phIndex];
				if(ctxIdArray!=null){
					return iic.uncompress(ctxIdArray); // equals to data
				}
			}
			else if(length==2){
				int[] ctxIdArray = loadTokenizedFourgramFastPFOR.getfourGmPhIdContextIdsFastPFOR()[phIndex]; 
				if(ctxIdArray!=null){
					return iic.uncompress(ctxIdArray); // equals to data
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
}
