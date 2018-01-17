package dal.relatedness.text.compute.tokenized;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;

import dal.clustering.document.shared.DocClusterConstant;
import dal.clustering.document.shared.DocClusterUtil;
import dal.clustering.document.shared.PairSim;
import dal.relatedness.phrase.compute.tokenized.PhraseRelatednessTokenized;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.text.constants.TextFileNames;
import dal.relatedness.text.processor.TextExtractorUtil;
import dal.relatedness.text.utils.TextRelatednessComputeUtil;

public class TextRelatednessTokenized {

	ArrayList<String> phrasesOfAText;

	PhraseRelatednessTokenized phraseRelatednessTokenized;

	public DocClusterUtil docClusterUtil;

	public TextRelatednessTokenized() {
		phraseRelatednessTokenized = new PhraseRelatednessTokenized();
		docClusterUtil = new DocClusterUtil();
	}

	public void ComputeTextRelUsingTokenizedNGram() {
		try {
			docClusterUtil.LoadGTMWordPairSimilarities();
			ComputeTextRel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ComputeTextRelUsingTokenizedNGramNew() {
		try {

			docClusterUtil.LoadGTMWordPairSimilarities();
			ArrayList<String[]> sentencePairs = TextExtractorUtil.extractSentencePairsFromSemEval2015();

			for (String[] pairs : sentencePairs) {
				String text1 = docClusterUtil.PerformPreprocess(pairs[0]);
				String text2 = docClusterUtil.PerformPreprocess(pairs[1]);

				//System.out.println(text1 + ", " + text2);
				
				double textSimscore = 0.0; 

				ArrayList<String> cands1 = docClusterUtil
						.splitByStopWord(text1);
				ArrayList<String> cands2 = docClusterUtil
						.splitByStopWord(text2);

				if (cands1.size() <= 0 || cands2.size() <= 0) {
					continue;
				}

				ArrayList<String> phs1 = docClusterUtil
						.SplitPhrases(generatePhsByFreq(cands1));
				ArrayList<String> phs2 = docClusterUtil
						.SplitPhrases(generatePhsByFreq(cands2));

				ArrayList<String> phWordList1 = docClusterUtil.CombineWordPhs(
						phs1, cands1);
				ArrayList<String> phWordList2 = docClusterUtil.CombineWordPhs(
						phs2, cands2);

				ArrayList<String> newPhWordList1 = docClusterUtil
						.SplitSuperPhrases(phWordList1, phWordList2);
				ArrayList<String> newPhWordList2 = docClusterUtil
						.SplitSuperPhrases(phWordList2, phWordList1);

				if (newPhWordList1.size() > newPhWordList2.size()) {
					ArrayList<String> temp = newPhWordList1;
					newPhWordList1 = newPhWordList2;
					newPhWordList2 = temp;
				}

				ArrayList<String> commonPhWords = docClusterUtil.GetCommonPhWordsByStemming(newPhWordList1, newPhWordList2);
				ArrayList<String> getRestPhWords1 = docClusterUtil
						.GetRestPhWords(newPhWordList1, commonPhWords);
				ArrayList<String> getRestPhWords2 = docClusterUtil
						.GetRestPhWords(newPhWordList2, commonPhWords);

				// System.out.println("commonPhWords="+commonPhWords);
				// System.out.println("getRestPhWords1="+getRestPhWords1);
				// System.out.println("getRestPhWords2="+getRestPhWords2);

				HashSet<String> notUsefulWPhPairsStemmed = getNotUsefulWPhPairsStemmed(
						getRestPhWords1, getRestPhWords2);

				// get weight sim matrix
				ArrayList<ArrayList<PairSim>> t1t2simPairList = null;
				if (getRestPhWords1.size() > 0 && getRestPhWords2.size() > 0) {
					t1t2simPairList = getWeightedSimilarityMatrix(getRestPhWords1, getRestPhWords2, notUsefulWPhPairsStemmed);

					textSimscore = TextRelatednessComputeUtil.ComputeSimilarityFromWeightedMatrixBySTD(
							t1t2simPairList,
							docClusterUtil.GetCommonWeight(commonPhWords),
							docClusterUtil.GetTextSize(phWordList1),
							docClusterUtil.GetTextSize(phWordList2), false);

					if (Double.isNaN(textSimscore)) {
						textSimscore = 0.0;
					}
				}

				System.out.println(textSimscore);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private HashSet<String> getNotUsefulWPhPairsStemmed(
			ArrayList<String> restPhWords1, ArrayList<String> restPhWords2) {
		HashSet<String> notUsefulWPhPairs = new HashSet<String>();
		try {

			double sumSumRaio = 0;
			double totalPairs = 0;
			ArrayList<Double> alPairsVal = new ArrayList<Double>();
			ArrayList<String> alPairsPhrases = new ArrayList<String>();

			for (String phWord1 : restPhWords1) {

				for (String phWord2 : restPhWords2) {

					int wordsInPh1 = phWord1.split("\\s+").length;
					int wordsInPh2 = phWord2.split("\\s+").length;

					String stemwph1 = StemmingUtil.stemPhrase(phWord1);
					String stemwph2 = StemmingUtil.stemPhrase(phWord2);

					String key = stemwph1 + "," + stemwph2;

					if (stemwph1.equals(stemwph2)) {
						continue;
					} else {

						// phraseRelatednessTokenized.loadNonTokenizeUnigram.g

						int ph1Index = phraseRelatednessTokenized.phRelComputeUtil
								.GetPhIndexAlHm(wordsInPh1, stemwph1);
						int ph2Index = phraseRelatednessTokenized.phRelComputeUtil
								.GetPhIndexAlHm(wordsInPh2, stemwph2);

						if (ph1Index < 0 || ph2Index < 0) {
							continue;
						}

						int countPh1 = phraseRelatednessTokenized.phRelComputeUtil
								.GetPhFreqAl(wordsInPh1, ph1Index);
						int countPh2 = phraseRelatednessTokenized.phRelComputeUtil
								.GetPhFreqAl(wordsInPh2, ph2Index);

						if (countPh1 <= 0 || countPh2 <= 0) {
							continue;
						}

						double sumRatio = (double) Math.min(countPh1, countPh2)
								/ (double) Math.max(countPh1, countPh2)
								* (double) (countPh1 + countPh2);
						sumSumRaio = sumSumRaio + sumRatio;
						totalPairs++;
						alPairsVal.add(sumSumRaio);
						alPairsPhrases.add(key);
					}

				}
			}

			double mean = sumSumRaio / totalPairs;
			double variaceSum = 0;

			for (double x : alPairsVal) {
				variaceSum = variaceSum + Math.pow(x - mean, 2);
			}

			double sd = Math.sqrt(variaceSum / alPairsVal.size());

			if (alPairsVal.size() == alPairsPhrases.size()) {
				for (int i = 0; i < alPairsVal.size(); i++) {
					double x = alPairsVal.get(i);

					if (x <= mean - 0.5 * sd || x >= mean + 0.5 * sd) {
						notUsefulWPhPairs.add(alPairsPhrases.get(i));
					}

				}
			}

//			System.out.println("alPairsPhrases.size=" + alPairsPhrases.size()
//					+ ",notUsefulWPhPairs.size=" + notUsefulWPhPairs.size());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return notUsefulWPhPairs;
	}

	private void ComputeTextRel() {
		try {

			phrasesOfAText = null;

			for (String inputSentencePairs : TextFileNames.SentencePairFileNames) {

				String outputTextSimScores = inputSentencePairs.replace(".txt",
						"-output.txt");

				System.out.println(inputSentencePairs + ","
						+ outputTextSimScores);

				BufferedReader br = new BufferedReader(new FileReader(
						inputSentencePairs));
				PrintWriter pr = new PrintWriter(outputTextSimScores);
				String text1, text2;

				while ((text1 = br.readLine()) != null) {

					text2 = br.readLine();
					if (text2 == null) {
						System.out.println("Inconsistent Pair");
						break;
					}

					text1 = docClusterUtil.PerformPreprocess(text1);
					text2 = docClusterUtil.PerformPreprocess(text2);

					ArrayList<String> cands1 = docClusterUtil
							.splitByStopWord(text1);
					ArrayList<String> cands2 = docClusterUtil
							.splitByStopWord(text2);

					// cands1 = removeSingleChars(cands1);
					// cands2 = removeSingleChars(cands2);

					ArrayList<String> phs1 = splitPhrases(generatePhsByFreq(cands1));
					ArrayList<String> phs2 = splitPhrases(generatePhsByFreq(cands2));

					ArrayList<String> phWordList1 = docClusterUtil
							.CombineWordPhs(phs1, cands1);
					ArrayList<String> phWordList2 = docClusterUtil
							.CombineWordPhs(phs2, cands2);

					phWordList1 = splitSuperPhrases(phWordList1, phWordList2);
					phWordList2 = splitSuperPhrases(phWordList2, phWordList1);

					if (phWordList1.size() > phWordList2.size()) {
						ArrayList<String> temp = phWordList1;
						phWordList1 = phWordList2;
						phWordList2 = temp;
					}

					ArrayList<String> commonPhWords = docClusterUtil
							.GetCommonPhWordsByStemming(phWordList1, phWordList2);
					ArrayList<String> getRestPhWords1 = docClusterUtil
							.GetRestPhWords(phWordList1, commonPhWords);
					ArrayList<String> getRestPhWords2 = docClusterUtil
							.GetRestPhWords(phWordList2, commonPhWords);

					ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
					if (getRestPhWords1.size() > 0
							&& getRestPhWords2.size() > 0) {
						t1t2simPairList = getWeightedSimilarityMatrix(
								getRestPhWords1, getRestPhWords2);
					}

					double score = TextRelatednessComputeUtil.ComputeSimilarityFromWeightedMatrixBySTD(
							t1t2simPairList,
							docClusterUtil.GetCommonWeight(commonPhWords),
							docClusterUtil.GetTextSize(phWordList1),
							docClusterUtil.GetTextSize(phWordList2),false);

					if (Double.isNaN(score)) {
						score = 0.0;
					}

					pr.println(score);
				}

				pr.close();
				br.close();
			}

			System.out
					.println("End ComputeTextSimilariryByWordPhraseSimilarity");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private double computeSimilarityFromWeightedMatrixBySTD(
//			ArrayList<ArrayList<PairSim>> t1t2simPairList, double common,
//			double t1Size, double t2Size) {
//		double score = 0.0;
//		try {
//			double allMeanSum = 0.0;
//			double cellValue = 0.0;
//
//			for (ArrayList<PairSim> simPairList : t1t2simPairList) {
//
//				double sum = 0.0;
//				for (PairSim ocPS : simPairList) {
//					double phWordsim = ocPS.value;
//					sum = sum + phWordsim;
//				}
//				double mean = sum / simPairList.size();
//
//				double sumVariance = 0.0;
//				for (PairSim ocPS : simPairList) {
//					sumVariance = sumVariance + Math.pow(mean - ocPS.value, 2);
//				}
//
//				double sd = Math.sqrt(sumVariance / simPairList.size());
//
//				double filteredMeanSum = 0.0;
//				double count = 0.0;
//				for (PairSim ocPS : simPairList) {
//					cellValue = cellValue + ocPS.value;
//					if (ocPS.value >= (mean + sd)) {
//						filteredMeanSum = filteredMeanSum + ocPS.value;
//						count++;
//					}
//				}
//				if (count > 0.0) {
//					allMeanSum = allMeanSum + filteredMeanSum / count;
//				}
//			}
//			score = (allMeanSum + common) * (t1Size + t2Size) / 2.0 / t1Size
//					/ t2Size;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return score;
//	}

	private ArrayList<ArrayList<PairSim>> getWeightedSimilarityMatrix(
			ArrayList<String> restPhWords1,	ArrayList<String> restPhWords2, HashSet<String> notUsefulWPhPairsStemmed) {
		ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
		try {

			for (String phWord1 : restPhWords1) {
				ArrayList<PairSim> simPairList = new ArrayList<PairSim>();
				for (String phWord2 : restPhWords2) {

					int wordsInPh1 = phWord1.split("\\s+").length;
					int wordsInPh2 = phWord2.split("\\s+").length;
					double totalWords = wordsInPh1 + wordsInPh2;
					double weightForWords = totalWords / 2;

					String stemwph1 = StemmingUtil.stemPhrase(phWord1);
					String stemwph2 = StemmingUtil.stemPhrase(phWord2);

					String stemkey = stemwph1 + "," + stemwph2;

					double simOverlap = 0.0;

					if (stemwph1.equals(stemwph1)) {
						simOverlap = 1.0 * weightForWords * weightForWords;
					} else {
						
//						if(notUsefulWPhPairsStemmed.contains(stemkey)){
//							continue;
//						}
//						else
						{
							if(weightForWords ==1.0){
								String wordKey = wordsInPh1+"," +wordsInPh2;
								if(docClusterUtil.hmGTMWordPairSim.containsKey(wordKey)){
									simOverlap = docClusterUtil.hmGTMWordPairSim.get(wordKey);
								}
								else if(docClusterUtil.hmGTMWordPairSim.containsKey(wordsInPh2+"," +wordsInPh1)){
									simOverlap = docClusterUtil.hmGTMWordPairSim.get(wordsInPh2+"," +wordsInPh1);
								}
							}
							else{
								simOverlap = phraseRelatednessTokenized.ComputeFastRelExternal(stemwph1, stemwph2)* weightForWords * weightForWords;
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

	private ArrayList<ArrayList<PairSim>> getWeightedSimilarityMatrix(
			ArrayList<String> restPhWords1, ArrayList<String> restPhWords2) {
		ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
		try {

			for (String phWord1 : restPhWords1) {
				ArrayList<PairSim> simPairList = new ArrayList<PairSim>();
				for (String phWord2 : restPhWords2) {

					int wordsInPh1 = phWord1.split("\\s+").length;
					int wordsInPh2 = phWord2.split("\\s+").length;
					double totalWords = wordsInPh1 + wordsInPh2;
					double weightForWords = totalWords / 2;

					String key = phWord1 + "," + phWord2;
					double simOverlap = 0.0;

					if (phWord1.equals(phWord2)) {
						simOverlap = 1.0 * weightForWords * weightForWords;
					} else {

						simOverlap = phraseRelatednessTokenized
								.ComputeFastRelExternal(phWord1, phWord2)
								* weightForWords * weightForWords;

					}

					simPairList.add(new PairSim(key, simOverlap));
				}
				t1t2simPairList.add(simPairList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t1t2simPairList;
	}

	private ArrayList<String> splitSuperPhrases(ArrayList<String> ls1,
			ArrayList<String> ls2) {
		ArrayList<String> phs = ls1;
		ArrayList<String> supers = new ArrayList<String>();

		try {

			for (String s1 : ls1) {
				for (String s2 : ls2) {
					// ArrayList<String> arr1 = new
					// ArrayList(Arrays.asList(s1.split("\\s+")));
					// ArrayList<String> arr2 = new
					// ArrayList(Arrays.asList(s2.split("\\s+")));

					ArrayList<String> arr1 = docClusterUtil
							.ConvertAarryToArrayList(s1.split("\\s+"));
					ArrayList<String> arr2 = docClusterUtil
							.ConvertAarryToArrayList(s2.split("\\s+"));

					ArrayList<String> commonPhWords = docClusterUtil.GetCommonPhWordsByStemming(arr1, arr2);
					if (commonPhWords.size() > 0) {
						// phs.remove(s1);
						// phs.addAll(arr1);
						supers.add(s1);
						break;
					}
				}
			}

			for (String s : supers) {
				phs.remove(s);
				// ArrayList<String> arr = new
				// ArrayList(Arrays.asList(s.split("\\s+")));
				ArrayList<String> arr = docClusterUtil
						.ConvertAarryToArrayList(s.split("\\s+"));
				phs.addAll(arr);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return phs;
	}

	private ArrayList<String> splitPhrases(ArrayList<String> phs) {
		ArrayList<String> bads = new ArrayList<String>();
		try {
			for (String s : phs) {
				s = s.trim();
				if (s.isEmpty()) {
					continue;
				}
				if (!docClusterUtil.PhHasLessThanEqualLimitedWords(s)
						|| !docClusterUtil.PhHasLessWorldLength(s)) {
					bads.add(s);
				}
			}

			for (String s : bads) {
				phs.remove(s);
				// phs.addAll(new ArrayList(Arrays.asList(s.split("\\s+"))));
				phs.addAll(docClusterUtil.ConvertAarryToArrayList(s
						.split("\\s+")));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phs;
	}

	private ArrayList<String> generatePhsByFreq(ArrayList<String> candStrs) {
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
							&& bgFreq >= (double) (DocClusterConstant.MeanBgFreq + DocClusterConstant.Std)) {
						maxfreq = bgFreq;
						maxBg = bg;
						i1 = i;
						i2 = i + 1;
					}

				}

				if (maxfreq > 0 && !maxBg.isEmpty() && i1 != i2) {
					phrasesOfAText.add(maxBg);
					String sLeft = docClusterUtil.GetSubStr(ws, 0, i1 - 1);
					String sRight = docClusterUtil.GetSubStr(ws, i2 + 2,
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
