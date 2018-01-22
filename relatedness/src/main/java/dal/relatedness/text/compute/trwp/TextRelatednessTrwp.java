package dal.relatedness.text.compute.trwp;

import java.util.ArrayList;
import java.util.HashSet;

import dal.clustering.document.shared.PairSim;
import dal.relatedness.phrase.compute.tokenized.PhraseRelatednessTokenized;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.text.compute.gtm.TextRelatednessGtm;
import dal.relatedness.text.utils.TextRelatednessFunctionalUtil;
import dal.relatedness.text.utils.TextRelatednessGoogleNgUtil;
import dal.utils.common.general.TextUtilShared;

public class TextRelatednessTrwp {
	
	PhraseRelatednessTokenized phraseRelatednessTokenized;
	TextUtilShared textUtilShared;
	TextRelatednessGoogleNgUtil textRelatednessGoogleNgUtil;
	TextRelatednessFunctionalUtil textRelFunctionalUtil;
	TextRelatednessGtm textRelatednessGtm;
	
	public TextRelatednessTrwp(TextRelatednessGtm textRelatednessGtm) {
		this.textRelatednessGtm = textRelatednessGtm;
		phraseRelatednessTokenized = new PhraseRelatednessTokenized();
		textUtilShared = new TextUtilShared();
		textRelatednessGoogleNgUtil = new TextRelatednessGoogleNgUtil(textUtilShared);
		textRelFunctionalUtil = new TextRelatednessFunctionalUtil(phraseRelatednessTokenized, textRelatednessGtm, textUtilShared);
	}
	
	public TextRelatednessTrwp(TextRelatednessGtm textRelatednessGtm, TextUtilShared textUtilShared, 
			TextRelatednessGoogleNgUtil textRelatednessGoogleNgUtil) {
		this.textRelatednessGtm = textRelatednessGtm;
		this.textUtilShared = textUtilShared;
		this.textRelatednessGoogleNgUtil = textRelatednessGoogleNgUtil;
		//phraseRelatednessTokenized = new PhraseRelatednessTokenized();
		textRelFunctionalUtil = new TextRelatednessFunctionalUtil(phraseRelatednessTokenized, textRelatednessGtm, textUtilShared);
	}

//	public void ComputeTextRelUsingTokenizedNGram() {
//		try {
//			textUtilShared.LoadGTMWordPairSimilarities();
//			ComputeTextRel();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public void ComputeTextRelUsingTokenizedNGramNew() {
//		try {
//
//			textUtilShared.LoadGTMWordPairSimilarities();
//			ArrayList<String[]> sentencePairs = TextExtractorUtil.extractSentencePairsFromSemEval2015();
//
//			for (String[] pairs : sentencePairs) {
//				String text1 = textUtilShared.PerformPreprocess(pairs[0]);
//				String text2 = textUtilShared.PerformPreprocess(pairs[1]);
//
//				//System.out.println(text1 + ", " + text2);
//				
//				double textSimscore = ComputeTextRelatednessExternal(text1, text2);
//				
//				System.out.println(textSimscore);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public double ComputeTextRelatednessExternalTrwp(String text1, String text2){
		double sim =0;
		try{
			
			text1 = text1.trim();
			text2 = text2.trim();
			
			if(text1.isEmpty() || text2.isEmpty()) return 0;

			
			ArrayList<String> cands1 = textUtilShared.convertAarryToArrayList(text1.split("\\s+"));
			ArrayList<String> cands2 =  textUtilShared.convertAarryToArrayList(text2.split("\\s+"));

			if (cands1.size() <= 0 || cands2.size() <= 0) {
				return 0;
			}

			if (cands1.size() > cands2.size()) {
				ArrayList<String> temp = cands1;
				cands1 = cands2;
				cands2 = temp;
			}

			ArrayList<String> commonPhWords = textUtilShared.GetCommonPhWordsByStemming(cands1, cands2);
			ArrayList<String> getRestPhWords1 = textUtilShared.GetRestPhWords(cands1, commonPhWords);
			ArrayList<String> getRestPhWords2 = textUtilShared.GetRestPhWords(cands2, commonPhWords);
			
//			if(commonPhWords.size()>0){
//				 System.out.println("commonPhWords="+commonPhWords);
//				 System.out.println("getRestPhWords1="+getRestPhWords1);
//				 System.out.println("getRestPhWords2="+getRestPhWords2);
//			}

//			HashSet<String> notUsefulWPhPairsStemmed = getNotUsefulWPhPairsStemmed(
//					getRestPhWords1, getRestPhWords2);

			// get weight sim matrix
			ArrayList<ArrayList<PairSim>> t1t2simPairList = null;
			if (getRestPhWords1.size() > 0 && getRestPhWords2.size() > 0) {
				t1t2simPairList = textRelFunctionalUtil.GetWeightedSimilarityMatrix(getRestPhWords1, getRestPhWords2, null);

				sim = textRelatednessGoogleNgUtil.ComputeSimilarityFromWeightedMatrixBySTD(
						t1t2simPairList,
						commonPhWords.size()*2,
						cands1.size()*2,
						cands2.size()*2, false);

				if (Double.isNaN(sim)) {
					sim = 0.0;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return sim;
	}
	
	public double ComputeTextRelatednessExternal(String text1, String text2){
		double sim =0;
		try{
			
			text1 = text1.trim();
			text2 = text2.trim();
			
			if(text1.isEmpty() || text2.isEmpty()) return 0;
			
			ArrayList<String> cands1 = textRelatednessGoogleNgUtil.splitByStopWord(text1);
			ArrayList<String> cands2 = textRelatednessGoogleNgUtil.splitByStopWord(text2);

			if (cands1.size() <= 0 || cands2.size() <= 0) {
				return 0;
			}

			ArrayList<String> phs1 = textRelatednessGoogleNgUtil.SplitPhrases(textRelFunctionalUtil.GeneratePhsByFreq(cands1));
			ArrayList<String> phs2 = textRelatednessGoogleNgUtil.SplitPhrases(textRelFunctionalUtil.GeneratePhsByFreq(cands2));

			ArrayList<String> phWordList1 = textUtilShared.CombineWordPhs(phs1, cands1);
			ArrayList<String> phWordList2 = textUtilShared.CombineWordPhs(phs2, cands2);

			ArrayList<String> newPhWordList1 = textRelatednessGoogleNgUtil.SplitSuperPhrases(phWordList1, phWordList2);
			ArrayList<String> newPhWordList2 = textRelatednessGoogleNgUtil.SplitSuperPhrases(phWordList2, phWordList1);

			if (newPhWordList1.size() > newPhWordList2.size()) {
				ArrayList<String> temp = newPhWordList1;
				newPhWordList1 = newPhWordList2;
				newPhWordList2 = temp;
			}

			ArrayList<String> commonPhWords = textUtilShared.GetCommonPhWordsByStemming(newPhWordList1, newPhWordList2);
			ArrayList<String> getRestPhWords1 = textUtilShared.GetRestPhWords(newPhWordList1, commonPhWords);
			ArrayList<String> getRestPhWords2 = textUtilShared.GetRestPhWords(newPhWordList2, commonPhWords);

			// System.out.println("commonPhWords="+commonPhWords);
			// System.out.println("getRestPhWords1="+getRestPhWords1);
			// System.out.println("getRestPhWords2="+getRestPhWords2);

			HashSet<String> notUsefulWPhPairsStemmed = getNotUsefulWPhPairsStemmed(
					getRestPhWords1, getRestPhWords2);

			// get weight sim matrix
			ArrayList<ArrayList<PairSim>> t1t2simPairList = null;
			if (getRestPhWords1.size() > 0 && getRestPhWords2.size() > 0) {
				t1t2simPairList = textRelFunctionalUtil.GetWeightedSimilarityMatrix(getRestPhWords1, getRestPhWords2, notUsefulWPhPairsStemmed);

				sim = textRelatednessGoogleNgUtil.ComputeSimilarityFromWeightedMatrixBySTD(
						t1t2simPairList,
						textRelatednessGoogleNgUtil.GetCommonWeight(commonPhWords),
						textRelatednessGoogleNgUtil.GetTextSize(phWordList1),
						textRelatednessGoogleNgUtil.GetTextSize(phWordList2), false);

				if (Double.isNaN(sim)) {
					sim = 0.0;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return sim;
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

	public ArrayList<ArrayList<String>> GetPhWordLists(ArrayList<String> bodies) {
		
		ArrayList<ArrayList<String>> alBodies = new ArrayList<ArrayList<String>>();
		
		try{
			for(String body: bodies){
				ArrayList<String> wordPhs = GetPhWordSingleDoc(body);
				System.out.println(wordPhs);
				alBodies.add(wordPhs);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return alBodies;
	}

	private ArrayList<String> GetPhWordSingleDoc(String body) {
		ArrayList<String> wordPhs = null;
		
		try{
			ArrayList<String> cands = textRelatednessGoogleNgUtil.splitByStopWord(body);
			ArrayList<String> phs = textRelatednessGoogleNgUtil.SplitPhrases(textRelFunctionalUtil.GeneratePhsByFreq(cands));
			wordPhs = textUtilShared.CombineWordPhs(phs, cands);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return wordPhs;
	}
	
	public void GeneratePhWordPairs(ArrayList<ArrayList<String>> docsByPhWords) {
		try{
			for(int i=0;i<docsByPhWords.size();i++){
				for(int j=i+1;j<docsByPhWords.size();j++){
					
					ArrayList<String> phWordList1 = docsByPhWords.get(i);
					ArrayList<String> phWordList2 = docsByPhWords.get(j);
					
					ArrayList<String> newPhWordList1 = textRelatednessGoogleNgUtil.SplitSuperPhrases(phWordList1, phWordList2);
					ArrayList<String> newPhWordList2 = textRelatednessGoogleNgUtil.SplitSuperPhrases(phWordList2, phWordList1);

					if (newPhWordList1.size() > newPhWordList2.size()) {
						ArrayList<String> temp = newPhWordList1;
						newPhWordList1 = newPhWordList2;
						newPhWordList2 = temp;
					}

					ArrayList<String> commonPhWords = textUtilShared.GetCommonPhWordsByStemming(newPhWordList1, newPhWordList2);
					ArrayList<String> getRestPhWords1 = textUtilShared.GetRestPhWords(newPhWordList1, commonPhWords);
					ArrayList<String> getRestPhWords2 = textUtilShared.GetRestPhWords(newPhWordList2, commonPhWords);
					
					System.out.println(commonPhWords);
					System.out.println(getRestPhWords1);
					System.out.println(getRestPhWords2);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private void ComputeTextRel() {
//		try {
//
//			for (String inputSentencePairs : TextFileNames.SentencePairFileNames) {
//
//				String outputTextSimScores = inputSentencePairs.replace(".txt",
//						"-output.txt");
//
//				System.out.println(inputSentencePairs + ","
//						+ outputTextSimScores);
//
//				BufferedReader br = new BufferedReader(new FileReader(
//						inputSentencePairs));
//				PrintWriter pr = new PrintWriter(outputTextSimScores);
//				String text1, text2;
//
//				while ((text1 = br.readLine()) != null) {
//
//					text2 = br.readLine();
//					if (text2 == null) {
//						System.out.println("Inconsistent Pair");
//						break;
//					}
//
//					text1 = textUtilShared.PerformPreprocess(text1);
//					text2 = textUtilShared.PerformPreprocess(text2);
//
//					ArrayList<String> cands1 = textRelatednessGoogleNgUtil.splitByStopWord(text1);
//					ArrayList<String> cands2 = textRelatednessGoogleNgUtil.splitByStopWord(text2);
//
//					// cands1 = removeSingleChars(cands1);
//					// cands2 = removeSingleChars(cands2);
//
//					ArrayList<String> phs1 = splitPhrases(phraseGeneratorUtil.GeneratePhsByFreq(cands1));
//					ArrayList<String> phs2 = splitPhrases(phraseGeneratorUtil.GeneratePhsByFreq(cands2));
//
//					ArrayList<String> phWordList1 = textUtilShared.CombineWordPhs(phs1, cands1);
//					ArrayList<String> phWordList2 = textUtilShared.CombineWordPhs(phs2, cands2);
//
//					phWordList1 = splitSuperPhrases(phWordList1, phWordList2);
//					phWordList2 = splitSuperPhrases(phWordList2, phWordList1);
//
//					if (phWordList1.size() > phWordList2.size()) {
//						ArrayList<String> temp = phWordList1;
//						phWordList1 = phWordList2;
//						phWordList2 = temp;
//					}
//
//					ArrayList<String> commonPhWords = textUtilShared.GetCommonPhWordsByStemming(phWordList1, phWordList2);
//					ArrayList<String> getRestPhWords1 = textUtilShared.GetRestPhWords(phWordList1, commonPhWords);
//					ArrayList<String> getRestPhWords2 = textUtilShared.GetRestPhWords(phWordList2, commonPhWords);
//
//					ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
//					if (getRestPhWords1.size() > 0
//							&& getRestPhWords2.size() > 0) {
//						t1t2simPairList = getWeightedSimilarityMatrix(
//								getRestPhWords1, getRestPhWords2);
//					}
//
//					double score = textRelatednessGoogleNgUtil.ComputeSimilarityFromWeightedMatrixBySTD(
//							t1t2simPairList,
//							textRelatednessGoogleNgUtil.GetCommonWeight(commonPhWords),
//							textRelatednessGoogleNgUtil.GetTextSize(phWordList1),
//							textRelatednessGoogleNgUtil.GetTextSize(phWordList2),false);
//
//					if (Double.isNaN(score)) {
//						score = 0.0;
//					}
//
//					pr.println(score);
//				}
//
//				pr.close();
//				br.close();
//			}
//
//			System.out
//					.println("End ComputeTextSimilariryByWordPhraseSimilarity");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

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

	

//	private ArrayList<ArrayList<PairSim>> getWeightedSimilarityMatrix(
//			ArrayList<String> restPhWords1, ArrayList<String> restPhWords2) {
//		ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
//		try {
//
//			for (String phWord1 : restPhWords1) {
//				ArrayList<PairSim> simPairList = new ArrayList<PairSim>();
//				for (String phWord2 : restPhWords2) {
//
//					int wordsInPh1 = phWord1.split("\\s+").length;
//					int wordsInPh2 = phWord2.split("\\s+").length;
//					double totalWords = wordsInPh1 + wordsInPh2;
//					double weightForWords = totalWords / 2;
//
//					String key = phWord1 + "," + phWord2;
//					double simOverlap = 0.0;
//
//					if (phWord1.equals(phWord2)) {
//						simOverlap = 1.0 * weightForWords * weightForWords;
//					} else {
//
//						simOverlap = phraseRelatednessTokenized
//								.ComputeFastRelExternal(phWord1, phWord2)
//								* weightForWords * weightForWords;
//
//					}
//
//					simPairList.add(new PairSim(key, simOverlap));
//				}
//				t1t2simPairList.add(simPairList);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return t1t2simPairList;
//	}

//	private ArrayList<String> splitSuperPhrases(ArrayList<String> ls1,
//			ArrayList<String> ls2) {
//		ArrayList<String> phs = ls1;
//		ArrayList<String> supers = new ArrayList<String>();
//
//		try {
//
//			for (String s1 : ls1) {
//				for (String s2 : ls2) {
//					// ArrayList<String> arr1 = new
//					// ArrayList(Arrays.asList(s1.split("\\s+")));
//					// ArrayList<String> arr2 = new
//					// ArrayList(Arrays.asList(s2.split("\\s+")));
//
//					ArrayList<String> arr1 = textUtilShared.ConvertAarryToArrayList(s1.split("\\s+"));
//					ArrayList<String> arr2 = textUtilShared.ConvertAarryToArrayList(s2.split("\\s+"));
//
//					ArrayList<String> commonPhWords = textUtilShared.GetCommonPhWordsByStemming(arr1, arr2);
//					if (commonPhWords.size() > 0) {
//						// phs.remove(s1);
//						// phs.addAll(arr1);
//						supers.add(s1);
//						break;
//					}
//				}
//			}
//
//			for (String s : supers) {
//				phs.remove(s);
//				// ArrayList<String> arr = new
//				// ArrayList(Arrays.asList(s.split("\\s+")));
//				ArrayList<String> arr = textUtilShared.ConvertAarryToArrayList(s.split("\\s+"));
//				phs.addAll(arr);
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return phs;
//	}

//	private ArrayList<String> splitPhrases(ArrayList<String> phs) {
//		ArrayList<String> bads = new ArrayList<String>();
//		try {
//			for (String s : phs) {
//				s = s.trim();
//				if (s.isEmpty()) {
//					continue;
//				}
//				if (!textRelatednessGoogleNgUtil.PhHasLessThanEqualLimitedWords(s)
//						|| !textRelatednessGoogleNgUtil.PhHasLessWorldLength(s)) {
//					bads.add(s);
//				}
//			}
//
//			for (String s : bads) {
//				phs.remove(s);
//				// phs.addAll(new ArrayList(Arrays.asList(s.split("\\s+"))));
//				phs.addAll(textUtilShared.ConvertAarryToArrayList(s.split("\\s+")));
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return phs;
//	}

}
