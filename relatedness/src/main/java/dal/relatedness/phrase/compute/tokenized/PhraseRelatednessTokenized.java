package dal.relatedness.phrase.compute.tokenized;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import dal.relatedness.phrase.constants.PhraseFileNames;
import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;
import dal.relatedness.phrase.load.nontokenized.unigram.LoadNonTokenizeUnigram;
import dal.relatedness.phrase.load.tokenized.fourgram.LoadTokenizedFourgram;
import dal.relatedness.phrase.load.tokenized.trigram.LoadTokenizedTrigram;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.utils.common.general.UtilsShared;

public class PhraseRelatednessTokenized {
	
	public LoadNonTokenizeUnigram loadNonTokenizeUnigram;
	public LoadNonTokenizeBigram loadNonTokenizeBigram;
	public LoadTokenizedTrigram loadTokenizedTrigram;
	public LoadTokenizedFourgram loadTokenizedFourgram;
	public PhRelComputeUtil phRelComputeUtil;
	
	public PhraseRelatednessTokenized(){
		loadNonTokenizeUnigram = new LoadNonTokenizeUnigram();
		loadNonTokenizeBigram = new LoadNonTokenizeBigram();
		
		loadTokenizedTrigram = new LoadTokenizedTrigram(loadNonTokenizeUnigram);
		loadTokenizedFourgram = new LoadTokenizedFourgram(loadNonTokenizeBigram);
		
		loadNonTokenizeUnigram.PopulateStemmedUniGramsHashMap();
		loadNonTokenizeBigram.PopulateStemmedBiGramsListHashMap();
		
		loadTokenizedTrigram.PopualteTokenizedTrigram();
		loadTokenizedFourgram.PopualteTokenizedFourgram();
		
		phRelComputeUtil = new PhRelComputeUtil(loadNonTokenizeUnigram, loadNonTokenizeBigram);
		
	}
	
	public void Compute(){
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
				
				double score = ComputePhRel(ph1, ph2);
				System.out.println(ph1+","+ph2+","+score); 
			}
			
			System.out.println(stTime+","+new Date().toString());
			
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ComputeFast(){
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
			}

			String stTime = new Date().toString();

			BufferedWriter bw = new BufferedWriter(new FileWriter(PhraseFileNames.PhrasePairScoresFile)); 
			
			for (String[] arr : pairs) {
				String ph1 = arr[0];
				String ph2 = arr[1];

				double score = ComputePhRelHmFast(ph1, ph2);
				System.out.println(ph1 + "," + ph2 + "," + score);
				bw.write(ph1 + "," + ph2 + "," + score+"\n");
			}
			
			bw.close();

			System.out.println(stTime + "," + new Date().toString());

			br.close();

			System.out.println("ComputeFast finished.");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ComputeFastMultiThreaded(){
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
			}

			String stTime = new Date().toString();

			for (String[] arr : pairs) {
				String ph1 = arr[0];
				String ph2 = arr[1];

				double score = ComputePhRelHmFastMultiThreaded(ph1, ph2);
				System.out.println(ph1 + "," + ph2 + "," + score);
			}

			System.out.println(stTime + "," + new Date().toString());

			br.close();

			System.out.println("ComputeFastMultiThreaded finished.");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public double ComputeFastRelExternal(String ph1, String ph2){
		double sim =0;
		try{
			sim = ComputePhRelHmFast(ph1, ph2);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return sim;
	}
	
	private double ComputePhRelHmFastMultiThreaded(String ph1, String ph2) {
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

			int [] ph1CtxIdFreqArray = GetPhCtxArray(ph1Arr.length, ph1Index);
			int [] ph2CtxIdFreqArray = GetPhCtxArray(ph2Arr.length, ph2Index);
			
			if(ph1CtxIdFreqArray==null || ph2CtxIdFreqArray==null){
				return 0;
			}
			
			if(!AnyCommonContextIds(ph1CtxIdFreqArray, ph2CtxIdFreqArray)){
				return 0;
			}
			
			score = phRelComputeUtil.filterNormalizedWeightBySTDAndComputeSimilarityCtxIdFreqArrayFastMultiThreaded(ph1CtxIdFreqArray, ph2CtxIdFreqArray, countPh1, countPh2);
			
			ph1CtxIdFreqArray = null;
			ph2CtxIdFreqArray = null;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return score;
	}
	
	

	private double ComputePhRelHmFast(String ph1, String ph2) {
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

			int [] ph1CtxIdFreqArray = GetPhCtxArray(ph1Arr.length, ph1Index);
			int [] ph2CtxIdFreqArray = GetPhCtxArray(ph2Arr.length, ph2Index);
			
			if(ph1CtxIdFreqArray==null || ph2CtxIdFreqArray==null){
				return 0;
			}
			
			if(!AnyCommonContextIds(ph1CtxIdFreqArray, ph2CtxIdFreqArray)){
				return 0;
			}
			
			score = phRelComputeUtil.filterNormalizedWeightBySTDAndComputeSimilarityCtxIdFreqArrayFast(ph1CtxIdFreqArray, ph2CtxIdFreqArray, countPh1, countPh2);
			
			ph1CtxIdFreqArray = null;
			ph2CtxIdFreqArray = null;
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
			
		return score;
	}

	private boolean AnyCommonContextIds(int[] ph1CtxIdFreqArray, int[] ph2CtxIdFreqArray) {

		try{
			int ph1CtxIdFirst = ph1CtxIdFreqArray[0];
			int ph1CtxIdLast = ph1CtxIdFreqArray[ph1CtxIdFreqArray.length-2];
			
			int ph2CtxIdFirst = ph2CtxIdFreqArray[0];
			int ph2CtxIdLast = ph2CtxIdFreqArray[ph2CtxIdFreqArray.length-2];
			
			return UtilsShared.AnyOverlapBetweenTwoRanges(ph1CtxIdFirst, ph1CtxIdLast, ph2CtxIdFirst, ph2CtxIdLast);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public double ComputePhRel(String ph1, String ph2){
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
			
			int [] ph1CtxIdFreqArray = GetPhCtxArray(ph1Arr.length, ph1Index);
			int [] ph2CtxIdFreqArray = GetPhCtxArray(ph2Arr.length, ph2Index);
			
			if(ph1CtxIdFreqArray==null || ph2CtxIdFreqArray==null){
				return 0;
			}
			
			HashMap<Integer, Double> hmPh1 = phRelComputeUtil.ConvertCtxIdFreqToMap(ph1CtxIdFreqArray);
			HashMap<Integer, Double> hmPh2 = phRelComputeUtil.ConvertCtxIdFreqToMap(ph2CtxIdFreqArray);
			
			HashMap<Integer, Double> hmPhCos1 = new HashMap<Integer, Double>(hmPh1);
            HashMap<Integer, Double> hmPhCos2 = new HashMap<Integer, Double>(hmPh2);
            
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

	public int[] GetPhCtxArray(int length, int phIndex) {
		try{
			
			if(phIndex>=0){
				if(length==1){
					return loadTokenizedTrigram.getTriGmWordIdContextIdFreqs()[phIndex];
				}
				else if(length==2){
					return loadTokenizedFourgram.getfourGmPhIdContextIdFreqs()[phIndex];
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
