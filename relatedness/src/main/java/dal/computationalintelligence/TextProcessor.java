package dal.computationalintelligence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import dal.relatedness.phrase.compute.tokenized.PhraseRelatednessTokenized;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.text.utils.TextRelatednessUtilCommon;

public class TextProcessor {

	ArrayList<String> phrasesOfAText;
	HashMap<String, Double> hmBiGramFreq;
	HashMap<TokenPairKey, Double> hmWordPhPairSimScores;
	
	ArrayList<String> listStopWords;
	String stopWordFile = "D:\\Dal\\ComputationalIntelligence\\Stopwords\\stopWords.txt";
	
	int minLettersInWord = 2;
    int maxWordsInPhrase = 2;
    
    long meanBgFreq = 2140;
    long std = 314830;
    
    public PrintWriter prNotFoundPairs;
    
    PhraseRelatednessTokenized phraseRelatednessTokenized;
    
    public TextProcessor() {
    	try{
    		prNotFoundPairs = new PrintWriter("D:\\Dal\\ComputationalIntelligence\\Notfound\\NotFoundPairs.txt");
    		
    		phraseRelatednessTokenized = new PhraseRelatednessTokenized();
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
    
	public void GenerateToken(String sentencePairFile, String tokenFile){
		try{
			
			listStopWords = populateStopWords();
			hmBiGramFreq = loadBgFreq("D:\\Dal\\ComputationalIntelligence\\PhraseCount\\AllPhraseCount.txt");
			phrasesOfAText = null;
			
			BufferedReader br = new BufferedReader(new FileReader(sentencePairFile));
			PrintWriter pr = new PrintWriter(tokenFile);
            String text1, text2;
            String orgText1, orgText2;
            while ((text1 = br.readLine()) != null) {
            	text1 = text1.trim().toLowerCase();
            	orgText1 = text1;
            	
            	text2 = br.readLine();
                if(text2==null){
                	System.out.println("Inconsistent Pair");
                	break;
                }
                text2 = text2.trim().toLowerCase();
                orgText2 = text2;
                
                text1 = text1.replaceAll("&amp;", " ").trim();
                text2 = text2.replaceAll("&amp;", " ").trim();
                                
                text1 = text1.replaceAll("[^a-zA-Z ]", " ").trim();
                text2 = text2.replaceAll("[^a-zA-Z ]", " ").trim();
                
                ArrayList<String> cands1 = splitByStopWord(text1);
                ArrayList<String> cands2 = splitByStopWord(text2);
                
                //if(cands1.size()<=0 || cands2.size()<=0)
                {
					//cands1= convertAarryToArrayList(text1.split("\\s+"));
					//cands2= convertAarryToArrayList(text2.split("\\s+"));
                	
                	//HashSet<String> hcands1 = SplitCandidatesBySpace(cands1);
	                //HashSet<String> hcands2 = SplitCandidatesBySpace(cands2);
                	
					cands1 = removeSingleChars(cands1);
					cands2 = removeSingleChars(cands2);
					//outScoreFile.println(0+"\t"+gtScore);
					//continue;
				}
                
                ArrayList<String> phs1 = splitPhrases(generatePhsByFreq(cands1));
                ArrayList<String> phs2 = splitPhrases(generatePhsByFreq(cands2));
                
                ArrayList<String> phWordList1 = combineWordPhs(phs1, cands1);
                ArrayList<String> phWordList2 = combineWordPhs(phs2, cands2);

                phWordList1 = splitSuperPhrases(phWordList1, phWordList2);
                phWordList2 = splitSuperPhrases(phWordList2, phWordList1);

                if (phWordList1.size() > phWordList2.size()) {
                    ArrayList<String> temp = phWordList1;
                    phWordList1 = phWordList2;
                    phWordList2 = temp;
                }

                ArrayList<String> commonPhWords = getCommonPhWords(phWordList1, phWordList2);
                ArrayList<String> getRestPhWords1 = getRestPhWords(phWordList1, commonPhWords);
                ArrayList<String> getRestPhWords2 = getRestPhWords(phWordList2, commonPhWords);
                
                pr.println(orgText1+"\n"+orgText2+"\n"+getRestPhWords1.toString().replace("[", "").replace("]", "").trim()+"\n"
                +getRestPhWords2.toString().replace("[", "").replace("]", "").trim()+"\n"
                +commonPhWords.toString().replace("[", "").replace("]", "").trim()+"\n");
            }
            
            pr.close();
            br.close();
            
            System.out.println("End GenerateToken");
		}
		catch(Exception e){
			
		}
	}
	
	public void ExtractWordsFromSentences(List<String> inputSentenceFiles, String outputAllWordsFile){
		try{
			
			listStopWords = populateStopWords();			
			HashSet<String> uniqueWords = new HashSet<String>(); 
					
			for(String sentenceFile : inputSentenceFiles){
				
				BufferedReader br = new BufferedReader(new FileReader(sentenceFile));
				
				String text1, text2;
	            while ((text1 = br.readLine()) != null) {
	            	text1 = text1.trim().toLowerCase();
	            	
	            	text2 = br.readLine();
	                if(text2==null){
	                	System.out.println("Inconsistent Pair");
	                	break;
	                }
	                text2 = text2.trim().toLowerCase();
	                
	                text1 = text1.replaceAll("&amp;", " ").trim();
	                text2 = text2.replaceAll("&amp;", " ").trim();
	                                
	                text1 = text1.replaceAll("[^a-zA-Z ]", " ").trim();
	                text2 = text2.replaceAll("[^a-zA-Z ]", " ").trim();
	                
	                ArrayList<String> cands1 = splitByStopWord(text1);
	                ArrayList<String> cands2 = splitByStopWord(text2);
	                
	                //cands1= convertAarryToArrayList(text1.split("\\s+"));
					//cands2= convertAarryToArrayList(text2.split("\\s+"));
					cands1 = removeSingleChars(cands1);
					cands2 = removeSingleChars(cands2);
	                
	                HashSet<String> hcands1 = SplitCandidatesBySpace(cands1);
	                HashSet<String> hcands2 = SplitCandidatesBySpace(cands2);
	                
	                uniqueWords.addAll(hcands1);
	                uniqueWords.addAll(hcands2);
	            }
				
				br.close();
			}
			
			PrintWriter pr = new PrintWriter(outputAllWordsFile);
			
			for(String word: uniqueWords){
				pr.println(word);
			}
			
			pr.close();
			
			System.out.println("End Extract all  words");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public void GenerateOnlyWordPairs(List<String> inputSentenceFiles, String outputAllWordpairsFile){
		try{
			listStopWords = populateStopWords();
			HashMap<TokenPairKey, Integer> wordPairs = new HashMap<TokenPairKey, Integer>();
			
			for(String sentenceFile : inputSentenceFiles){
				
				BufferedReader br = new BufferedReader(new FileReader(sentenceFile));
				
				String text1, text2;
	            while ((text1 = br.readLine()) != null) {
	            	text1 = text1.trim().toLowerCase();
	            	
	            	text2 = br.readLine();
	                if(text2==null){
	                	System.out.println("Inconsistent Pair");
	                	break;
	                }
	                text2 = text2.trim().toLowerCase();
	                
	                text1 = text1.replaceAll("&amp;", " ").trim();
	                text2 = text2.replaceAll("&amp;", " ").trim();
	                                
	                text1 = text1.replaceAll("[^a-zA-Z ]", " ").trim();
	                text2 = text2.replaceAll("[^a-zA-Z ]", " ").trim();
	                
	                ArrayList<String> cands1 = splitByStopWord(text1);
	                ArrayList<String> cands2 = splitByStopWord(text2);
	                
					cands1 = removeSingleChars(cands1);
					cands2 = removeSingleChars(cands2);
	                
	                HashSet<String> hcands1 = SplitCandidatesBySpace(cands1);
	                HashSet<String> hcands2 = SplitCandidatesBySpace(cands2);
	                
	                ArrayList<String> stringsList1 = new ArrayList<String>(hcands1);
	                ArrayList<String> stringsList2 = new ArrayList<String>(hcands2);
	                
	                for(int i=0;i<stringsList1.size();i++){
	                	for(int j=0;j<stringsList2.size();j++){
	                		TokenPairKey tokenPairKey = new TokenPairKey(stringsList1.get(i), stringsList2.get(j));
	                		wordPairs.put(tokenPairKey, 0);
	                	}
	                }
	            }
				
				br.close();
			}
			
			PrintWriter pr = new PrintWriter(outputAllWordpairsFile);
			
			for(TokenPairKey tokenPairKey: wordPairs.keySet()){
				pr.println(tokenPairKey.token1+","+tokenPairKey.token2);
			}
			
			pr.close();
			
			System.out.println("End GenerateOnlyWordPairs" + ", total word pairs="+wordPairs.size());
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	
	public void GenerateWordPhrasePairs(List<String> inputSentenceFiles, String outputWordPhrasePairsFile){
		try{
			listStopWords = populateStopWords();
			hmBiGramFreq = loadBgFreq("D:\\Dal\\ComputationalIntelligence\\PhraseCount\\AllPhraseCount.txt");
			phrasesOfAText = null;
			HashMap<TokenPairKey, Integer> wordPhrasePairs = new HashMap<TokenPairKey, Integer>();
			
			for(String sentenceFile : inputSentenceFiles){
				
				BufferedReader br = new BufferedReader(new FileReader(sentenceFile));
				
				String text1, text2;
	            while ((text1 = br.readLine()) != null) {
	            	text1 = text1.trim().toLowerCase();
	            	
	            	text2 = br.readLine();
	                if(text2==null){
	                	System.out.println("Inconsistent Pair");
	                	break;
	                }
	                text2 = text2.trim().toLowerCase();
	                
	                text1 = text1.replaceAll("&amp;", " ").trim();
	                text2 = text2.replaceAll("&amp;", " ").trim();
	                                
	                text1 = text1.replaceAll("[^a-zA-Z ]", " ").trim();
	                text2 = text2.replaceAll("[^a-zA-Z ]", " ").trim();
	                
	                ArrayList<String> cands1 = splitByStopWord(text1);
	                ArrayList<String> cands2 = splitByStopWord(text2);
	                
					cands1 = removeSingleChars(cands1);
					cands2 = removeSingleChars(cands2);
	                
					ArrayList<String> phs1 = splitPhrases(generatePhsByFreq(cands1));
	                ArrayList<String> phs2 = splitPhrases(generatePhsByFreq(cands2));

	                ArrayList<String> phWordList1 = combineWordPhs(phs1, cands1);
	                ArrayList<String> phWordList2 = combineWordPhs(phs2, cands2);

	                phWordList1 = splitSuperPhrases(phWordList1, phWordList2);
	                phWordList2 = splitSuperPhrases(phWordList2, phWordList1);

	                if (phWordList1.size() > phWordList2.size()) {
	                    ArrayList<String> temp = phWordList1;
	                    phWordList1 = phWordList2;
	                    phWordList2 = temp;
	                }

	                ArrayList<String> commonPhWords = getCommonPhWords(phWordList1, phWordList2);
	                ArrayList<String> getRestPhWords1 = getRestPhWords(phWordList1, commonPhWords);
	                ArrayList<String> getRestPhWords2 = getRestPhWords(phWordList2, commonPhWords);
 
	                HashSet<String> hRestPhWords1 = new HashSet<String>(); 
	                HashSet<String> hRestPhWords2 = new HashSet<String>();
	               
	                hRestPhWords1.addAll(getRestPhWords1);
	                hRestPhWords2.addAll(getRestPhWords2);
	               
	                ArrayList<String> stringsList1 = new ArrayList<String>(hRestPhWords1);
	               	ArrayList<String> stringsList2 = new ArrayList<String>(hRestPhWords2);
					
	                for(int i=0;i<stringsList1.size();i++){
	                	for(int j=0;j<stringsList2.size();j++){
	                		TokenPairKey tokenPairKey = new TokenPairKey(stringsList1.get(i), stringsList2.get(j));
	                		wordPhrasePairs.put(tokenPairKey, 0);
	                	}
	                }
	            }
				
				br.close();
			}
			
			PrintWriter pr = new PrintWriter(outputWordPhrasePairsFile);
			
			for(TokenPairKey tokenPairKey: wordPhrasePairs.keySet()){
				pr.println(tokenPairKey.token1+","+tokenPairKey.token2);
			}
			
			pr.close();
			
			System.out.println("End GenerateWordPhrasePairs");
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	
	public void ComputeTextSimilariryByWordSimilarity(String inputSentencePairs, String inputWordSimScores,
			String outputTextSimScores) {
		try{
			listStopWords = populateStopWords();
			hmWordPhPairSimScores = getPairSimScores(inputWordSimScores);
			
			BufferedReader br = new BufferedReader(new FileReader(inputSentencePairs));
			//PrintWriter pr = new PrintWriter(outputTextSimScores);
            String text1, text2;
            
            while ((text1 = br.readLine()) != null) {
            	text1 = text1.trim().toLowerCase();
            	
            	text2 = br.readLine();
                if(text2==null){
                	System.out.println("Inconsistent Pair");
                	break;
                }
                text2 = text2.trim().toLowerCase();
                
                text1 = text1.replaceAll("&amp;", " ").trim();
                text2 = text2.replaceAll("&amp;", " ").trim();
                                
                text1 = text1.replaceAll("[^a-zA-Z ]", " ").trim();
                text2 = text2.replaceAll("[^a-zA-Z ]", " ").trim();
                
                ArrayList<String> cands1 = splitByStopWord(text1);
                ArrayList<String> cands2 = splitByStopWord(text2);
                
//				cands1 = removeSingleChars(cands1);
//				cands2 = removeSingleChars(cands2);
				
				HashSet<String> hcands1 = SplitCandidatesBySpace(cands1);
                HashSet<String> hcands2 = SplitCandidatesBySpace(cands2);
                
                ArrayList<String> phWordList1 = new ArrayList<String>();
                phWordList1.addAll(hcands1);
                
                ArrayList<String> phWordList2 = new ArrayList<String>();
                phWordList2.addAll(hcands2);
                
                if (phWordList1.size() > phWordList2.size()) {
                    ArrayList<String> temp = phWordList1;
                    phWordList1 = phWordList2;
                    phWordList2 = temp;
                }

                ArrayList<String> commonPhWords = getCommonPhWords(phWordList1, phWordList2);
                ArrayList<String> getRestPhWords1 = getRestPhWords(phWordList1, commonPhWords);
                ArrayList<String> getRestPhWords2 = getRestPhWords(phWordList2, commonPhWords);
                
                ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
                if (getRestPhWords1.size() > 0 && getRestPhWords2.size() > 0) {
                   // t1t2simPairList = getWeightedSimilarityMatrix(getRestPhWords1, getRestPhWords2);
                }
				
				//double score = computeSimilarityFromWeightedMatrixBySTD(t1t2simPairList, getCommonWeight(commonPhWords), getTextSize(phWordList1), getTextSize(phWordList2));
                double score = computeSimilarityFromWeightedMatrixBySTD(t1t2simPairList, commonPhWords.size(), phWordList1.size(), phWordList2.size());
				
				if (Double.isNaN(score)){
					score=0.0;
				}
				
				//pr.println(score);
				System.out.println(score);
            }
            
           // pr.close();
            br.close();
			
			System.out.println("End ComputeTextSimilariryByWordSimilarity");
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
	}
	
	public void ComputeTextSimilariryByWordPhraseSimilarity(String inputSentencePairs, String inputWordPhraseSimScores,
			String outputTextSimScores){
		try{
			listStopWords = populateStopWords();
			hmBiGramFreq = loadBgFreq("D:\\Dal\\ComputationalIntelligence\\PhraseCount\\AllPhraseCount.txt");
			hmWordPhPairSimScores = getPairSimScores(inputWordPhraseSimScores);
			phrasesOfAText = null;
			
			BufferedReader br = new BufferedReader(new FileReader(inputSentencePairs));
			PrintWriter pr = new PrintWriter(outputTextSimScores);
            String text1, text2;
            
            while ((text1 = br.readLine()) != null) {
            	text1 = text1.trim().toLowerCase();
            	
            	text2 = br.readLine();
                if(text2==null){
                	System.out.println("Inconsistent Pair");
                	break;
                }
                text2 = text2.trim().toLowerCase();
                
                text1 = text1.replaceAll("&amp;", " ").trim();
                text2 = text2.replaceAll("&amp;", " ").trim();
                                
                text1 = text1.replaceAll("[^a-zA-Z ]", " ").trim();
                text2 = text2.replaceAll("[^a-zA-Z ]", " ").trim();
                
                ArrayList<String> cands1 = splitByStopWord(text1);
                ArrayList<String> cands2 = splitByStopWord(text2);
                
				cands1 = removeSingleChars(cands1);
				cands2 = removeSingleChars(cands2);
				
				ArrayList<String> phs1 = splitPhrases(generatePhsByFreq(cands1));
                ArrayList<String> phs2 = splitPhrases(generatePhsByFreq(cands2));
                
                ArrayList<String> phWordList1 = combineWordPhs(phs1, cands1);
                ArrayList<String> phWordList2 = combineWordPhs(phs2, cands2);

                phWordList1 = splitSuperPhrases(phWordList1, phWordList2);
                phWordList2 = splitSuperPhrases(phWordList2, phWordList1);

                if (phWordList1.size() > phWordList2.size()) {
                    ArrayList<String> temp = phWordList1;
                    phWordList1 = phWordList2;
                    phWordList2 = temp;
                }

                ArrayList<String> commonPhWords = getCommonPhWords(phWordList1, phWordList2);
                ArrayList<String> getRestPhWords1 = getRestPhWords(phWordList1, commonPhWords);
                ArrayList<String> getRestPhWords2 = getRestPhWords(phWordList2, commonPhWords);
                
                //temp: works better than non-pruning word-ph sim pairs
                HashSet<String> notUsefulWPhPairsStemmed = getNotUsefulWPhPairsStemmed(phWordList1, phWordList2);
                //temp
                
                
                
                ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
                if (getRestPhWords1.size() > 0 && getRestPhWords2.size() > 0) {
                    t1t2simPairList = getWeightedSimilarityMatrix(getRestPhWords1, getRestPhWords2, notUsefulWPhPairsStemmed);
                }
				
				double score =  computeSimilarityFromWeightedMatrixBySTD(t1t2simPairList, getCommonWeight(commonPhWords), getTextSize(phWordList1), getTextSize(phWordList2));
				
				if (Double.isNaN(score)){
					score=0.0;
				}
				
				//pr.println(score);
				System.out.println(score);
            }
            
            pr.close();
            br.close();
			
			
			System.out.println("End ComputeTextSimilariryByWordPhraseSimilarity");
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	private HashSet<String> getNotUsefulWPhPairsStemmed(	ArrayList<String> restPhWords1, ArrayList<String> restPhWords2) {
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
						
						//System.out.println("count1="+countPh1+","+phWord1+", "+"count2="+countPh2+","+phWord2);

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

					if (x <= mean - 1.0 * sd || x >= mean + 1.0 * sd) {
						notUsefulWPhPairs.add(alPairsPhrases.get(i));
						//System.out.println("alPairsPhrases.get(i)="+alPairsPhrases.get(i));
					}

				}
			}

			//System.out.println("alPairsPhrases.size=" + alPairsPhrases.size()
			//		+ ",notUsefulWPhPairs.size=" + notUsefulWPhPairs.size());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return notUsefulWPhPairs;
	}
	
	
	private double computeSimilarityFromWeightedMatrixBySTD(ArrayList<ArrayList<PairSim>> t1t2simPairList, double common,
            double t1Size, double t2Size) {
        double score = 0.0;
        try {
            double allMeanSum = 0.0;
            double cellValue = 0.0;

            for (ArrayList<PairSim> simPairList : t1t2simPairList) {

                double sum = 0.0;
                for (PairSim ocPS : simPairList) {
                    double phWordsim = ocPS.value;
                    sum = sum + phWordsim;
                }
                double mean = sum / simPairList.size();

                double sumVariance = 0.0;
                for (PairSim ocPS : simPairList) {
                    sumVariance = sumVariance + Math.pow(mean - ocPS.value, 2);
                }

                double sd = Math.sqrt(sumVariance / simPairList.size());

                double filteredMeanSum = 0.0;
                double count = 0.0;
                for (PairSim ocPS : simPairList) {
                    cellValue = cellValue + ocPS.value;
                    if (ocPS.value >= (mean + sd)) {
                        filteredMeanSum = filteredMeanSum + ocPS.value;
                        count++;
                    }
                }
                if (count > 0.0) {
                    allMeanSum = allMeanSum + filteredMeanSum / count;
                }
            }
            score = (allMeanSum + common) * (t1Size + t2Size) / 2.0 / t1Size / t2Size;
        } catch (Exception e) {
            System.out.println("error: computeSimilarityFromWeightedMatrixBySTD->" + e.toString());
        }

        return score;
    }

	private HashMap<TokenPairKey, Double> getPairSimScores(String pairSimFile) {
        HashMap<TokenPairKey, Double> pairSim = new HashMap<TokenPairKey, Double>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(pairSimFile)));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                String arr[] = line.split(",");
                if (line.isEmpty() || arr.length < 3) {
                    continue;
                }

                TokenPairKey key = new TokenPairKey( arr[0].trim() , arr[1].trim());
                pairSim.put(key, Double.parseDouble(arr[2].trim()));
            }
            br.close();
        } catch (Exception e) {
            System.out.println("error: getPairSimScores->" + e.toString());
        }
        return pairSim;
    }
	
	private ArrayList<ArrayList<PairSim>> getWeightedSimilarityMatrix(ArrayList<String> restPhWords1, ArrayList<String> restPhWords2, HashSet<String> notUsefulWPhPairsStemmed) {
        ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
        try {
            for (String phWord1 : restPhWords1) {
                ArrayList<PairSim> simPairList = new ArrayList<PairSim>();
                for (String phWord2 : restPhWords2) {

                    int wordsInPh1 = phWord1.split("\\s+").length;
                    int wordsInPh2 = phWord2.split("\\s+").length;
                    double totalWords = wordsInPh1 + wordsInPh2;
                    double weightForWords = totalWords / 2;

                    //String key = phWord1 + "," + phWord2;
                    TokenPairKey key = new TokenPairKey(phWord1, phWord2);
                    double simOverlap = 0.0;
					
					/*Pattern p1 = Pattern.compile("[0-9]+");
					Matcher m1 = p1.matcher(phWord1);
					
					Pattern p2 = Pattern.compile("[0-9]+");
					Matcher m2 = p2.matcher(phWord2);
					
					if (m1.find() || m2.find()){
						simPairList.add(new cPairSim(key, simOverlap));
						continue;	
					}*/ 

//                    if (weightForWords == 1.0) {
//                        if (hmPairSimGTMWord.containsKey(key)) {
//                            simOverlap = hmPairSimGTMWord.get(key);  //+pairSimOverlapPh.get(key);
//                        } else {
//                            key = phWord2 + "," + phWord1;
//                            if (hmPairSimGTMWord.containsKey(key)) {
//                                simOverlap = hmPairSimGTMWord.get(key); //+pairSimOverlapPh.get(key);
//                            } else {
//                                System.out.println("Not found Phrase gtm:" + key);
//                            }
//                        }
//                    } 
//                    else 
                   if(!notUsefulWPhPairsStemmed.contains(StemmingUtil.stemPhrase(phWord1)+","+StemmingUtil.stemPhrase(phWord2))) 
                   {
                        if (hmWordPhPairSimScores.containsKey(key)) {
                            simOverlap = hmWordPhPairSimScores.get(key) * weightForWords * weightForWords;
                        } else {
                            //key = phWord2 + "," + phWord1;
                        	key = new TokenPairKey(phWord2, phWord1);
                            if (hmWordPhPairSimScores.containsKey(key)) {
                                simOverlap = hmWordPhPairSimScores.get(key) * weightForWords * weightForWords;
                            } else {
                                System.out.println("Not found :" + key.token1+","+key.token2);
                                prNotFoundPairs.println("Not found :" + key.token1+","+key.token2);
                            }
                        }
                    }
                    
                    simPairList.add(new PairSim(key, simOverlap));
                }
                t1t2simPairList.add(simPairList);
            }
        } catch (Exception e) {
            System.out.println("error: getWeightedSimilarityMatrix->" + e.toString());
        }
        return t1t2simPairList;
    }
	
	private double getTextSize(ArrayList<String> phWords) {
        double size = 0;
        try {
            for (String s : phWords) {
                size = size + s.split("\\s+").length * 2;
            }
        } catch (Exception e) {
            System.out.println("error: getTextSize->" + e.toString());
        }
        return size;
    }
	
	private double getCommonWeight(ArrayList<String> commonPhWords) {
        double com = 0;
        try {
            for (String s : commonPhWords) {
                com = com + s.split("\\s+").length * 2;
            }
        } catch (Exception e) {
            System.out.println("error: getCommonWeight->" + e.toString());
        }
        return com;
    }
	
	private ArrayList<String> removeSingleChars(ArrayList<String> alStr){
		ArrayList<String> al = new ArrayList<String>();
		try{
			for(String s: alStr){
				s=s.trim();
				if(s.length()>=2){
					al.add(s);
				}
			}
		}catch (Exception e) {
            System.out.println("error: removeSingleChars->" + e.toString());
        }
		return al;
	}
	
	private HashSet<String> SplitCandidatesBySpace(ArrayList<String> cands1) {
		HashSet<String> cads = new HashSet<String>();
		
		try{
			for(String str:cands1){
				String [] arr = str.split("\\s+");
				for(String splitStr: arr){
					cads.add(splitStr);
				}
			}
		}
		catch(Exception e){
		}
		
		return cads;
	}

	private HashMap<String, Double> loadBgFreq(String file){
		HashMap<String, Double> al = new HashMap<String, Double>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File(file)));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                String arr[] = line.split(",");
                if (line.isEmpty() || arr.length < 2) {
                    continue;
                }
				String bg = arr[0].trim();
				if(bg.length()<=0){
					continue;
				}
				
				double freq = Double.parseDouble(arr[1].trim());
				if(freq<=0){
					continue;
				}
				
				al.put(bg, freq);
			}
			br.close();
		}catch (Exception e) {
            System.out.println("loadBgFreq="+e.toString());
        }
		return al;
	}
	
	private ArrayList<String> getRestPhWords(ArrayList<String> phWordList, ArrayList<String> commonPhWords) {
        ArrayList<String> restPhWords = new ArrayList<String>();
        try {
            for (String phWord : phWordList) {
                if (!commonPhWords.contains(phWord)) {
                    restPhWords.add(phWord);
                }
            }
        } catch (Exception e) {
            System.out.println("error: getRestPhWords->" + e.toString());
        }
        return restPhWords;
    }
	
	private ArrayList<String> getCommonPhWords(ArrayList<String> phWordList1, ArrayList<String> phWordList2) {
        ArrayList<String> commonPhWords = new ArrayList<String>();
        try {
            for (int i = 0; i < phWordList1.size(); i++) {
                for (int j = 0; j < phWordList2.size(); j++) {
                    if (phWordList1.get(i).equals(phWordList2.get(j))) {
                        commonPhWords.add(phWordList1.get(i));
                        break;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("error: getCommonPhWords->" + e.toString());
        }
        return commonPhWords;
    }
	
	private ArrayList<String> combineWordPhs(ArrayList<String> phs, ArrayList<String> cands) {
        ArrayList<String> wPhs = new ArrayList<String>();
        try {
            for (String s : cands) {
                s = s.trim();
                if (s.isEmpty()) {
                    continue;
                }
                for (String p : phs) {
                    s = s.replaceAll("\\b" + p + "\\b", "").trim();
                }
                if (!s.isEmpty()) {
                    //wPhs.addAll(Arrays.asList(s.split("\\s+")));
					wPhs.addAll(convertAarryToArrayList(s.split("\\s+")));
                }
            }

            wPhs.addAll(phs);
        } catch (Exception e) {
            System.out.println("error: combineWordPhs->" + e.toString());
        }

        return wPhs;
    }
	
	private ArrayList<String> convertAarryToArrayList(String arr[]){
		ArrayList<String> al = new ArrayList<String>();
		try{
			for (String s: arr){
				if(!s.isEmpty()){
					al.add(s);
				}
			}
		}catch (Exception e) {
            System.out.println("error: convertAarryToArrayList->" + e.toString());
        }
		return al;
	}
	
	private ArrayList<String> splitPhrases(ArrayList<String> phs) {
        ArrayList<String> bads = new ArrayList<String>();
        try {
            for (String s : phs) {
                s = s.trim();
                if (s.isEmpty()) {
                    continue;
                }
                if (!phHasLessThanEqualLimitedWords(s) || phHasLessWorldLength(s)) {
                    bads.add(s);
                }
            }

            for (String s : bads) {
                phs.remove(s);
                //phs.addAll(new ArrayList(Arrays.asList(s.split("\\s+"))));
				phs.addAll(convertAarryToArrayList(s.split("\\s+")));
				
            }
        } catch (Exception e) {
            System.out.println("error: splitPhrases->" + e.toString());
        }
        return phs;
    }
	
	private boolean phHasLessWorldLength(String phWord) {
        try {
            String[] arrP = phWord.split("\\s");
            for (String w : arrP) {
                w = w.trim();
                if (w.length() < minLettersInWord) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("error: phHasLessWorldLength->" + e.toString());
        }
        return false;
    }
	
	private boolean phHasLessThanEqualLimitedWords(String phWord) {
        try {
            String[] arrP = phWord.split("\\s");
            if (arrP.length > maxWordsInPhrase) {
                return false;
            }
        } catch (Exception e) {
            System.out.println("error: phHasMaxWords->" + e.toString());
        }
        return true;
    }
	
	private ArrayList<String> splitSuperPhrases(ArrayList<String> ls1, ArrayList<String> ls2) {
        ArrayList<String> phs = ls1;
        ArrayList<String> supers = new ArrayList<String>();

        try {

            for (String s1 : ls1) {
                for (String s2 : ls2) {
                    //ArrayList<String> arr1 = new ArrayList(Arrays.asList(s1.split("\\s+")));
                    //ArrayList<String> arr2 = new ArrayList(Arrays.asList(s2.split("\\s+")));
					
					ArrayList<String> arr1 = convertAarryToArrayList(s1.split("\\s+"));
                    ArrayList<String> arr2 = convertAarryToArrayList(s2.split("\\s+"));

                    ArrayList<String> commonPhWords = getCommonStemmedPhWords(arr1, arr2);
                    if (commonPhWords.size() > 0) {
                        //phs.remove(s1);
                        //phs.addAll(arr1);
                        supers.add(s1);
                        break;
                    }
                }
            }

            for (String s : supers) {
                phs.remove(s);
                //ArrayList<String> arr = new ArrayList(Arrays.asList(s.split("\\s+")));
				ArrayList<String> arr = convertAarryToArrayList(s.split("\\s+"));
                phs.addAll(arr);
            }

        } catch (Exception e) {
            System.out.println("error: splitSuperPhrases->" + e.toString());
        }
        return phs;
    }
	
	private ArrayList<String> getCommonStemmedPhWords(ArrayList<String> phWordList1, ArrayList<String> phWordList2) {
        ArrayList<String> commonPhWords = new ArrayList<String>();
        try {
            for (int i = 0; i < phWordList1.size(); i++) {
                for (int j = 0; j < phWordList2.size(); j++) {
                    if (equalStemmedPhrases(phWordList1.get(i), phWordList2.get(j))) {
                        commonPhWords.add(phWordList1.get(i));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error: getCommonStemmedPhWords->" + e.toString());
        }
        return commonPhWords;
    }
	
	private boolean equalStemmedPhrases(String ph1, String ph2) {
        boolean isEqual = true;
        try {
            ph1 = getStemmedPhrase(ph1);
            ph2 = getStemmedPhrase(ph2);

            isEqual = ph1.equals(ph2);

        } catch (Exception e) {
            System.out.println("error: equalStemmedPhrases->" + e.toString());
        }
        return isEqual;
    }

    private String getStemmedPhrase(String ph) {
        String str = "";
        try {
            ph = ph.trim().toLowerCase();
            Porter obj = new Porter();
            String[] arr = ph.split("\\s+");
            for (String s : arr) {
                str = str + " " + obj.stripAffixes(s).trim();
            }
        } catch (Exception e) {
            System.out.println("error: getStemmedPhrase->" + e.toString());
        }
        return str.trim();
    }
	
	private ArrayList<String> generatePhsByFreq(ArrayList<String> candStrs) {
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
            System.out.println("error: generatePhsByFreq->" + e.toString());
        }
        return phrasesOfAText;
    }
	
	private void binarySplitCandidate(String s) {
        try {

            if (s.trim().isEmpty()) {
                return;
            }

            String[] ws = s.split("\\s+");

            /*if (ws.length <= 1) {
             return;
             } else {
                if (ws.length == 2) {
                 if (!s.trim().isEmpty() && hmBiGramFreq.containsKey(s) && hmBiGramFreq.get(s) >= (double) (meanBgFreq + std)) {
                 phrasesOfAText.add(s);
                 return;
                 }
                 } else*/ 
                 {
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
                            if (hmBiGramFreq.containsKey(bg)) {
                                double bgFreq = hmBiGramFreq.get(bg);
								//System.out.println(bg+","+bgFreq);
                                if (bgFreq >= maxfreq && bgFreq >= (double) (meanBgFreq + std)) {
                                    maxfreq = bgFreq;
                                    maxBg = bg;
                                    i1 = i;
                                    i2 = i + 1;
                                }
                            }
                        }

                        if (maxfreq > 0 && !maxBg.isEmpty() && i1 != i2) {
                            phrasesOfAText.add(maxBg);
                            String sLeft = getSubStr(ws, 0, i1 - 1);
                            String sRight = getSubStr(ws, i2 + 2, ws.length);
                            binarySplitCandidate(sLeft);
                            binarySplitCandidate(sRight);
                        }
                    }
                }
            //}

        } catch (Exception e) {
            System.out.println("error: binarySplitCandidate->" + e.toString());
        }
    }
	
	private String getSubStr(String[] ws, int start, int end) {
        String str = "";
        try {
            for (int i = start; i < end; i++) {
                str = str + " " + ws[i];
            }
        } catch (Exception e) {
            System.out.println("error: getSubStr->" + e.toString());
        }
        return str.trim();
    }
	
	private ArrayList<String> splitByStopWord(String s) {
        ArrayList<String> ps = new ArrayList<String>();
        try {
            String[] arr = s.split("\\s+");

            String phrase = "";
            for (String w : arr) {
                w = w.trim();
                if (w.isEmpty()) {
                    continue;
                }
                if (!listStopWords.contains(w)) {
                    phrase = phrase + " " + w;
                } else {
                    phrase = phrase.trim();
                    if (!phrase.isEmpty()) {
                        ps.add(phrase);
                    }
                    phrase = "";
                }
            }

            phrase = phrase.trim();
            if (!phrase.isEmpty()) {
                ps.add(phrase);
            }
        } catch (Exception e) {
            System.out.println("error: splitByStopWord->" + e.toString());
        }
        return ps;
    }
	
	private ArrayList<String> populateStopWords() {
        ArrayList<String> stopWordsList = new ArrayList<String>();
        try {
            BufferedReader brsw = new BufferedReader(new FileReader(new File(stopWordFile)));
            String line;
            while ((line = brsw.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.isEmpty()) {
                    continue;
                }
                stopWordsList.add(line);
            }
            brsw.close();
        } catch (Exception e) {
            System.out.println("error: populateStopWords->" + e.toString());
        }
        return stopWordsList;
    }	

}
