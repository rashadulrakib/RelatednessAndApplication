package dal.relatedness.phrase.compute.tokenized;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import dal.relatedness.phrase.constants.PhraseFileNames;
import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;
import dal.relatedness.phrase.load.nontokenized.unigram.LoadNonTokenizeUnigram;
import dal.relatedness.phrase.load.tokenized.fourgram.LoadTokenizedFourgramVByteCoding;
import dal.relatedness.phrase.load.tokenized.trigram.LoadTokenizedTrigramVByteCoding;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.phrase.utils.encoding.VariableByteCode;

public class PhraseRelatednessTokenizedVByteCoding {

	public LoadNonTokenizeUnigram loadNonTokenizeUnigram;
	public LoadNonTokenizeBigram loadNonTokenizeBigram;
	public LoadTokenizedTrigramVByteCoding loadTokenizedTrigramVByteCoding;
	public LoadTokenizedFourgramVByteCoding loadTokenizedFourgramVByteCoding;
	public PhRelComputeUtil phRelComputeUtil;
	
	

	// public PhraseRelatednessTokenizedVByteCoding(){
	// loadNonTokenizeUnigram = new LoadNonTokenizeUnigram();
	// loadNonTokenizeBigram = new LoadNonTokenizeBigram();
	//
	// loadTokenizedTrigramVByteCoding = new
	// LoadTokenizedTrigramVByteCoding(loadNonTokenizeUnigram);
	// loadTokenizedFourgramVByteCoding = new
	// LoadTokenizedFourgramVByteCoding(loadNonTokenizeBigram);
	//
	// loadNonTokenizeUnigram.PopulateStemmedUniGramsList();
	// loadNonTokenizeBigram.PopulateStemmedBiGramsList();
	//
	// loadTokenizedFourgramVByteCoding.PopualteTokenizedFourgramVByteCoding();
	// loadTokenizedTrigramVByteCoding.PopualteTokenizedTrigramVByteCoding();
	//
	// phRelComputeUtil = new PhRelComputeUtil(loadNonTokenizeUnigram,
	// loadNonTokenizeBigram);
	//
	// }

	// public PhraseRelatednessTokenizedVByteCoding(){
	// loadNonTokenizeUnigram = new LoadNonTokenizeUnigram();
	// loadNonTokenizeBigram = new LoadNonTokenizeBigram();
	//
	// loadTokenizedTrigramVByteCoding = new
	// LoadTokenizedTrigramVByteCoding(loadNonTokenizeUnigram);
	// loadTokenizedFourgramVByteCoding = new
	// LoadTokenizedFourgramVByteCoding(loadNonTokenizeBigram);
	//
	// loadNonTokenizeUnigram.PopulateStemmedUniGramsHashMap();
	// loadNonTokenizeBigram.PopulateStemmedBiGramsHashMap();
	//
	// loadTokenizedFourgramVByteCoding.PopualteTokenizedFourgramVByteCoding();
	// loadTokenizedTrigramVByteCoding.PopualteTokenizedTrigramVByteCoding();
	//
	// phRelComputeUtil = new PhRelComputeUtil(loadNonTokenizeUnigram,
	// loadNonTokenizeBigram);
	//
	// }

	public PhraseRelatednessTokenizedVByteCoding() {
		loadNonTokenizeUnigram = new LoadNonTokenizeUnigram();
		loadNonTokenizeBigram = new LoadNonTokenizeBigram();

		loadTokenizedTrigramVByteCoding = new LoadTokenizedTrigramVByteCoding(
				loadNonTokenizeUnigram);
		loadTokenizedFourgramVByteCoding = new LoadTokenizedFourgramVByteCoding(
				loadNonTokenizeBigram);

		loadNonTokenizeUnigram.PopulateStemmedUniGramsHashMap();
		loadNonTokenizeBigram.PopulateStemmedBiGramsListHashMap();

		loadTokenizedTrigramVByteCoding.PopualteTokenizedTrigramVByteCoding();
		loadTokenizedFourgramVByteCoding.PopualteTokenizedFourgramVByteCoding();
		
		phRelComputeUtil = new PhRelComputeUtil(loadNonTokenizeUnigram,
				loadNonTokenizeBigram);

	}

	public void ComputeVByteCodingAl() {
		try {

			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(
					PhraseFileNames.PhrasePairsFile));

			String stTime = new Date().toString();

			while ((sCurrentLine = br.readLine()) != null) {

				if (sCurrentLine.isEmpty())
					continue;

				sCurrentLine = sCurrentLine.toLowerCase();

				String[] arr = sCurrentLine.split(",");
				String ph1 = StemmingUtil.stemPhrase(arr[0]);
				String ph2 = StemmingUtil.stemPhrase(arr[1]);

				double score = ComputePhRelVByteCodingAl(ph1, ph2);
				System.out.println(ph1 + "," + ph2 + "," + score);
			}

			System.out.println(stTime + "," + new Date().toString());

			br.close();

			System.out.println("ComputeVByteCoding finished.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ComputeVByteCodingAlHmFast() {
		try {

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

				double score = ComputePhRelVByteCodingAlHmFast(ph1, ph2);
				System.out.println(ph1 + "," + ph2 + "," + score);
			}

			System.out.println(stTime + "," + new Date().toString());

			br.close();

			System.out.println("ComputeVByteCodingAlHm finished.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private double ComputePhRelVByteCodingAlHmFast(String ph1, String ph2){
		
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

			List<Integer> ph1CtxIdArray = GetPhCtxIdArray(ph1Arr.length, ph1Index);
			List<Integer> ph2CtxIdArray = GetPhCtxIdArray(ph2Arr.length, ph2Index);

			List<Integer> ph1CtxFreqArray = GetPhCtxFreqArray(ph1Arr.length, ph1Index);
			List<Integer> ph2CtxFreqArray = GetPhCtxFreqArray(ph2Arr.length, ph2Index);

			if (ph1CtxIdArray == null || ph2CtxIdArray == null || ph1CtxFreqArray == null || ph2CtxFreqArray == null) {
				return 0;
			}

			if (ph1CtxIdArray.size() != ph1CtxFreqArray.size() || ph2CtxIdArray.size() != ph2CtxFreqArray.size()) {
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
	
	private double ComputePhRelVByteCodingAlHm(String ph1, String ph2) {
		try {
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

			int countPh1 = phRelComputeUtil
					.GetPhFreqAl(ph1Arr.length, ph1Index);
			int countPh2 = phRelComputeUtil
					.GetPhFreqAl(ph2Arr.length, ph2Index);

			if (countPh1 <= 0 || countPh2 <= 0) {
				return 0;
			}

			List<Integer> ph1CtxIdArray = GetPhCtxIdArray(ph1Arr.length,
					ph1Index);
			List<Integer> ph2CtxIdArray = GetPhCtxIdArray(ph2Arr.length,
					ph2Index);

			List<Integer> ph1CtxFreqArray = GetPhCtxFreqArray(ph1Arr.length,
					ph1Index);
			List<Integer> ph2CtxFreqArray = GetPhCtxFreqArray(ph2Arr.length,
					ph2Index);

			if (ph1CtxIdArray == null || ph2CtxIdArray == null
					|| ph1CtxFreqArray == null || ph2CtxFreqArray == null) {
				return 0;
			}

			if (ph1CtxIdArray.size() != ph1CtxFreqArray.size()
					|| ph2CtxIdArray.size() != ph2CtxFreqArray.size()) {
				return 0;
			}

			HashMap<Integer, Double> hmPh1 = ConvertCtxIdFreqToMap(
					ph1CtxIdArray, ph1CtxFreqArray);
			HashMap<Integer, Double> hmPh2 = ConvertCtxIdFreqToMap(
					ph2CtxIdArray, ph2CtxFreqArray);

			HashMap<Integer, Double> hmPhCos1 = new HashMap<Integer, Double>(
					hmPh1);
			HashMap<Integer, Double> hmPhCos2 = new HashMap<Integer, Double>(
					hmPh2);

			ArrayList<HashMap<Integer, Double>> lhm = phRelComputeUtil
					.filterNormalizedWeightBySTD(hmPh1, hmPh2, countPh1,
							countPh2);
			hmPh1 = lhm.get(0);
			hmPh2 = lhm.get(1);

			ArrayList<HashMap<Integer, Double>> lhmCos = phRelComputeUtil
					.getCommonVectors(hmPhCos1, hmPhCos2);
			HashMap<Integer, Double> hmPh1Common = lhmCos.get(0);

			ArrayList<Integer> comMinusFiltered = phRelComputeUtil
					.getComMinusFilteredKeys(hmPh1Common, hmPh1);
			hmPhCos1 = phRelComputeUtil.removeKeys(hmPhCos1, comMinusFiltered);
			hmPhCos2 = phRelComputeUtil.removeKeys(hmPhCos2, comMinusFiltered);

			double cosSim = phRelComputeUtil.getCosineSimilarity(hmPhCos1,
					hmPhCos2, countPh1, countPh2);
			// score=cosSim;
			double commonCount = phRelComputeUtil.getCommonCount(hmPh1, hmPh2);
			commonCount = commonCount * cosSim;

			double score = phRelComputeUtil.normalizeSimilarity(countPh1,
					countPh2, commonCount);

			return score;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}
	
//	private double ComputePhRelVByteCodingAlHm(String ph1, String ph2) {
//		try {
//			if (ph1.equals(ph2)) {
//				return 1;
//			}
//
//			String[] ph1Arr = ph1.split("\\s+");
//			String[] ph2Arr = ph2.split("\\s+");
//
//			int ph1Index = phRelComputeUtil.GetPhIndexAlHm(ph1Arr.length, ph1);
//			int ph2Index = phRelComputeUtil.GetPhIndexAlHm(ph2Arr.length, ph2);
//
//			if (ph1Index < 0 || ph2Index < 0) {
//				return 0;
//			}
//
//			int countPh1 = phRelComputeUtil
//					.GetPhFreqAl(ph1Arr.length, ph1Index);
//			int countPh2 = phRelComputeUtil
//					.GetPhFreqAl(ph2Arr.length, ph2Index);
//
//			if (countPh1 <= 0 || countPh2 <= 0) {
//				return 0;
//			}
//
//			List<Integer> ph1CtxIdArray = GetPhCtxIdArray(ph1Arr.length,
//					ph1Index);
//			List<Integer> ph2CtxIdArray = GetPhCtxIdArray(ph2Arr.length,
//					ph2Index);
//
//			List<Integer> ph1CtxFreqArray = GetPhCtxFreqArray(ph1Arr.length,
//					ph1Index);
//			List<Integer> ph2CtxFreqArray = GetPhCtxFreqArray(ph2Arr.length,
//					ph2Index);
//
//			if (ph1CtxIdArray == null || ph2CtxIdArray == null
//					|| ph1CtxFreqArray == null || ph2CtxFreqArray == null) {
//				return 0;
//			}
//
//			if (ph1CtxIdArray.size() != ph1CtxFreqArray.size()
//					|| ph2CtxIdArray.size() != ph2CtxFreqArray.size()) {
//				return 0;
//			}
//
//			HashMap<Integer, Double> hmPh1 = ConvertCtxIdFreqToMap(
//					ph1CtxIdArray, ph1CtxFreqArray);
//			HashMap<Integer, Double> hmPh2 = ConvertCtxIdFreqToMap(
//					ph2CtxIdArray, ph2CtxFreqArray);
//
//			HashMap<Integer, Double> hmPhCos1 = new HashMap<Integer, Double>(
//					hmPh1);
//			HashMap<Integer, Double> hmPhCos2 = new HashMap<Integer, Double>(
//					hmPh2);
//
//			ArrayList<HashMap<Integer, Double>> lhm = phRelComputeUtil
//					.filterNormalizedWeightBySTD(hmPh1, hmPh2, countPh1,
//							countPh2);
//			hmPh1 = lhm.get(0);
//			hmPh2 = lhm.get(1);
//
//			ArrayList<HashMap<Integer, Double>> lhmCos = phRelComputeUtil
//					.getCommonVectors(hmPhCos1, hmPhCos2);
//			HashMap<Integer, Double> hmPh1Common = lhmCos.get(0);
//			HashMap<Integer, Double> hmPh2Common = lhmCos.get(1);
//
//			ArrayList<Integer> comMinusFiltered1 = phRelComputeUtil
//					.getComMinusFilteredKeys(hmPh1Common, hmPh1);
//			ArrayList<Integer> comMinusFiltered2 = phRelComputeUtil
//					.getComMinusFilteredKeys(hmPh2Common, hmPh2);
//			
//			hmPhCos1 = phRelComputeUtil.removeKeys(hmPhCos1, comMinusFiltered1);
//			hmPhCos2 = phRelComputeUtil.removeKeys(hmPhCos2, comMinusFiltered2);
//
//			double cosSim = phRelComputeUtil.getCosineSimilarity(hmPhCos1,
//					hmPhCos2, countPh1, countPh2);
//			// score=cosSim;
//			double commonCount = phRelComputeUtil.getCommonCount(hmPh1, hmPh2);
//			commonCount = commonCount * cosSim;
//
//			double score = phRelComputeUtil.normalizeSimilarity(countPh1,
//					countPh2, commonCount);
//
//			return score;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return 0;
//	}

	private double ComputePhRelVByteCodingAl(String ph1, String ph2) {
		try {
			if (ph1.equals(ph2)) {
				return 1;
			}

			String[] ph1Arr = ph1.split("\\s+");
			String[] ph2Arr = ph2.split("\\s+");

			int ph1Index = phRelComputeUtil.GetPhIndexAl(ph1Arr.length, ph1);
			int ph2Index = phRelComputeUtil.GetPhIndexAl(ph2Arr.length, ph2);

			if (ph1Index < 0 || ph2Index < 0) {
				return 0;
			}

			int countPh1 = phRelComputeUtil
					.GetPhFreqAl(ph1Arr.length, ph1Index);
			int countPh2 = phRelComputeUtil
					.GetPhFreqAl(ph2Arr.length, ph2Index);

			if (countPh1 <= 0 || countPh2 <= 0) {
				return 0;
			}

			List<Integer> ph1CtxIdArray = GetPhCtxIdArray(ph1Arr.length,
					ph1Index);
			List<Integer> ph2CtxIdArray = GetPhCtxIdArray(ph2Arr.length,
					ph2Index);

			List<Integer> ph1CtxFreqArray = GetPhCtxFreqArray(ph1Arr.length,
					ph1Index);
			List<Integer> ph2CtxFreqArray = GetPhCtxFreqArray(ph2Arr.length,
					ph2Index);

			if (ph1CtxIdArray == null || ph2CtxIdArray == null
					|| ph1CtxFreqArray == null || ph2CtxFreqArray == null) {
				return 0;
			}

			if (ph1CtxIdArray.size() != ph1CtxFreqArray.size()
					|| ph2CtxIdArray.size() != ph2CtxFreqArray.size()) {
				return 0;
			}

			HashMap<Integer, Double> hmPh1 = ConvertCtxIdFreqToMap(
					ph1CtxIdArray, ph1CtxFreqArray);
			HashMap<Integer, Double> hmPh2 = ConvertCtxIdFreqToMap(
					ph2CtxIdArray, ph2CtxFreqArray);

			HashMap<Integer, Double> hmPhCos1 = hmPh1;
			HashMap<Integer, Double> hmPhCos2 = hmPh2;

			ArrayList<HashMap<Integer, Double>> lhm = phRelComputeUtil
					.filterNormalizedWeightBySTD(hmPh1, hmPh2, countPh1,
							countPh2);
			hmPh1 = lhm.get(0);
			hmPh2 = lhm.get(1);

			ArrayList<HashMap<Integer, Double>> lhmCos = phRelComputeUtil
					.getCommonVectors(hmPhCos1, hmPhCos2);
			HashMap<Integer, Double> hmPh1Common = lhmCos.get(0);

			ArrayList<Integer> comMinusFiltered = phRelComputeUtil
					.getComMinusFilteredKeys(hmPh1Common, hmPh1);
			hmPhCos1 = phRelComputeUtil.removeKeys(hmPhCos1, comMinusFiltered);
			hmPhCos2 = phRelComputeUtil.removeKeys(hmPhCos2, comMinusFiltered);

			double cosSim = phRelComputeUtil.getCosineSimilarity(hmPhCos1,
					hmPhCos2, countPh1, countPh2);
			
			double commonCount = phRelComputeUtil.getCommonCount(hmPh1, hmPh2);
			commonCount = commonCount * cosSim;

			double score = phRelComputeUtil.normalizeSimilarity(countPh1,
					countPh2, commonCount);

			return score;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	private HashMap<Integer, Double> ConvertCtxIdFreqToMap(
			List<Integer> phCtxIdArray, List<Integer> phCtxFreqArray) {
		HashMap<Integer, Double> hm = new HashMap<Integer, Double>();
		try {
			if (phCtxIdArray.size() != phCtxIdArray.size()) {
				return hm;
			}

			for (int i = 0; i < phCtxIdArray.size(); i++) {
				if (i < phCtxIdArray.size() - 1
						&& phCtxIdArray.get(i) > phCtxIdArray.get(i + 1)) {
					System.out.println("Problem in decompressing CtxIds");
					return new HashMap<Integer, Double>();
				}
				hm.put(phCtxIdArray.get(i), (double) phCtxFreqArray.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hm;
	}

	private List<Integer> GetPhCtxIdArray(int length, int phIndex) {
		try {
			if (length == 1) {
				byte[] ctxIdArray = loadTokenizedTrigramVByteCoding
						.getTriGmWordIdContextIdsByteCoding()[phIndex];
				if (ctxIdArray != null) {
					return VariableByteCode.decodeInterpolate(ctxIdArray); // equals
																			// to
																			// data
				}
			} else if (length == 2) {
				byte[] ctxIdArray = loadTokenizedFourgramVByteCoding
						.getfourGmPhIdContextIdsByteCoding()[phIndex];
				if (ctxIdArray != null) {
					return VariableByteCode.decodeInterpolate(ctxIdArray); // equals
																			// to
																			// data
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;
	}

	private List<Integer> GetPhCtxFreqArray(int length, int phIndex) {
		try {
			if (length == 1) {
				byte[] ctxFreqArray = loadTokenizedTrigramVByteCoding
						.getTriGmWordIdContextFreqsByteCoding()[phIndex];
				if (ctxFreqArray != null) {
					return VariableByteCode.decode(ctxFreqArray);
				}
			} else if (length == 2) {
				byte[] ctxFreqArray = loadTokenizedFourgramVByteCoding
						.getfourGmPhIdContextFreqsByteCoding()[phIndex];
				if (ctxFreqArray != null) {
					return VariableByteCode.decode(ctxFreqArray);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
