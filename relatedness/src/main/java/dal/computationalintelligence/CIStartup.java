package dal.computationalintelligence;

import java.util.ArrayList;
import java.util.List;

public class CIStartup {
	
	public void CICallTextSim(){
		 //Step1(); //not useful
		 //Step2(); //not useful
		//Step3();
		//Step4();
		//Step5();
		//Step5_1();
		//Step6();
		//Step6_1(); //merge the required word and phrase pairs into a single file
		//Step7();
		Step7_1();
		//Step8(); //not related
	}

	private static void Step1(){
		TextProcessor textProcessor = new TextProcessor();
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRpar-12.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRpar-12-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRvid-12.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRvid-12-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\OnWN-12.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\OnWN-12-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTeuroparl-12.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTeuroparl-12-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTnews-12.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTnews-12-Token.txt");
		
		
		
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\FNWN-13.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\FNWN-13-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\headlines-13.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\headlines-13-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\OnWN-13.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\OnWN-13-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\SMT-13.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\SMT-13-Token.txt");
		
		
		
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-forums-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-forums-15-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-students-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-students-15-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-belief-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-belief-15-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-headlines-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-headlines-15-Token.txt");
		
		textProcessor.GenerateToken("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-images-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-images-15-Token.txt");
	}
	
	private static void Step2(){
		Sem2015SentenceExtractor sem2015SentenceExtractor = new Sem2015SentenceExtractor();
		
		sem2015SentenceExtractor.ExtractSentencePairs("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.input.answers-forums.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.gs.answers-forums.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-forums-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\gs-answers-forums-15.txt");
		
		sem2015SentenceExtractor.ExtractSentencePairs("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.input.answers-students.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.gs.answers-students.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-students-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\gs-answers-students-15.txt");
		
		sem2015SentenceExtractor.ExtractSentencePairs("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.input.belief.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.gs.belief.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-belief-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\gs-belief-15.txt");
		
		sem2015SentenceExtractor.ExtractSentencePairs("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.input.headlines.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.gs.headlines.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-headlines-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\gs-headlines-15.txt");
		
		sem2015SentenceExtractor.ExtractSentencePairs("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.input.images.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\STS.gs.images.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-images-15.txt", 
				"D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\gs-images-15.txt");
	}
	
	private static void Step3(){
		TextProcessor textProcessor = new TextProcessor();
		
		List<String> sentencePairFiles = new ArrayList<String>();
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRpar-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRvid-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\OnWN-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTeuroparl-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTnews-12.txt");
		
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\FNWN-13.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\headlines-13.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\OnWN-13.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\SMT-13.txt");
		
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-forums-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-students-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-belief-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-headlines-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-images-15.txt");
		
		textProcessor.ExtractWordsFromSentences(sentencePairFiles, "D:\\Dal\\ComputationalIntelligence\\AllWords\\AllWords.txt");
	}
	
	
	
	private static void Step4(){
		WordEmbeddingVectorProcessor wordEmbeddingVectorProcessor = new WordEmbeddingVectorProcessor();
		
		wordEmbeddingVectorProcessor.ExtractWordEmbeddingVector("D:\\Dal\\ComputationalIntelligence\\AllWords\\AllWords.txt", 
		"D:\\Dal\\glove.42B.300d\\glove.42B.300d.txt", 
		"D:\\Dal\\ComputationalIntelligence\\WordVecs\\AllWords-Vecs.txt");
	}
	
	private static void Step5(){
		TextProcessor textProcessor = new TextProcessor();
		
		List<String> sentencePairFiles = new ArrayList<String>();
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRpar-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRvid-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\OnWN-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTeuroparl-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTnews-12.txt");
		
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\FNWN-13.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\headlines-13.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\OnWN-13.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\SMT-13.txt");
		
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-forums-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-students-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-belief-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-headlines-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-images-15.txt");
		
		textProcessor.GenerateOnlyWordPairs(sentencePairFiles, "D:\\Dal\\ComputationalIntelligence\\WordPairs\\AllWordPairs.txt");
	}
	
	//Step5_1() generate Word_Phrase pairs
	private static void Step5_1(){
		TextProcessor textProcessor = new TextProcessor();
		
		List<String> sentencePairFiles = new ArrayList<String>();
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRpar-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRvid-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\OnWN-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTeuroparl-12.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTnews-12.txt");
		
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\FNWN-13.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\headlines-13.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\OnWN-13.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\SMT-13.txt");
		
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-forums-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-students-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-belief-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-headlines-15.txt");
		sentencePairFiles.add("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-images-15.txt");
				
		textProcessor.GenerateWordPhrasePairs(sentencePairFiles, 
				"D:\\Dal\\ComputationalIntelligence\\WordPhrasePairs\\AllWordPhrasePairs.txt");
	}
	
	private static void Step6(){
		WordEmbeddingVectorProcessor wordEmbeddingVectorProcessor = new WordEmbeddingVectorProcessor();
		wordEmbeddingVectorProcessor.GenerateWordSimScores("D:\\Dal\\ComputationalIntelligence\\WordPairs\\AllWordPairs.txt", 
				"D:\\Dal\\ComputationalIntelligence\\WordVecs\\AllWords-Vecs.txt", 
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt");
	}
	
	//Step6_1 generate Word && Phrase sim scores
	private static void Step6_1(){
		//preprocess All.txt and extract only phrae pairs and combine with w2vec word pairs and print all them in a file.
		WordPhrasePairProcessor wordPhrasePairProcessor = new WordPhrasePairProcessor();
		wordPhrasePairProcessor.ExtractAndMergeWordPhrasePairWithSimScores("D:\\Dal\\ComputationalIntelligence\\AllTextSimDatasetWordPhraseSimScores\\AllTextSimSemEvalDatasetWordPhraseSimScores.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt", 
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt");
	}
	
	private static void Step7(){
		TextProcessor textProcessor = new TextProcessor();
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRpar-12.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
			"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\MSRpar-12-tex-sim-gloveword-scores");

		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRvid-12.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\MSRvid-12-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\OnWN-12.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\OnWN-12-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTeuroparl-12.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\SMTeuroparl-12-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTnews-12.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\SMTnews-12-tex-sim-gloveword-scores");
		
		
		
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\FNWN-13.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\FNWN-13-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\headlines-13.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\headlines-13-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\OnWN-13.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\OnWN-13-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\SMT-13.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\SMT-13-tex-sim-gloveword-scores");
		
		
		
		
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-forums-15.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\answers-forums-15-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-students-15.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\answers-students-15-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-belief-15.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\belief-15-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-headlines-15.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\headlines-15-tex-sim-gloveword-scores");
		
		textProcessor.ComputeTextSimilariryByWordSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-images-15.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordSimilarity\\EmbeddingSimScore.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\images-15-tex-sim-gloveword-scores");
	}
	
	private static void Step7_1(){
		TextProcessor textProcessor = new TextProcessor();
		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRpar-12.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\MSRpar-12-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\MSRvid-12.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\MSRvid-12-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\OnWN-12.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\OnWN-12-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTeuroparl-12.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\SMTeuroparl-12-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-12\\SMTnews-12.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\SMTnews-12-tex-sim-gloveword-phrase-scores");
		
		
		
		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\FNWN-13.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\FNWN-13-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\headlines-13.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\headlines-13-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\OnWN-13.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\OnWN-13-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-13\\SMT-13.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\SMT-13-tex-sim-gloveword-phrase-scores");
		
		
		
		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-forums-15.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\trwp-answers-forums-15-tex-sim-word-phrase-scores");
		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-students-15.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\answers-students-15-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-belief-15.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\belief-15-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-headlines-15.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\headlines-15-tex-sim-gloveword-phrase-scores");
//		
//		textProcessor.ComputeTextSimilariryByWordPhraseSimilarity("D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-images-15.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\WordPhraseSimilarity\\WordPhraseSimScores.txt",
//				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\images-15-tex-sim-gloveword-phrase-scores");
		
		if(textProcessor.prNotFoundPairs!=null){
			textProcessor.prNotFoundPairs.close();
		}
	}
	
	private static void Step8() {
		Sem2015SentenceExtractor sem2015SentenceExtractor = new Sem2015SentenceExtractor();
		sem2015SentenceExtractor.ExtractOutputScoresFromTrwpSem2015
		("C:\\Users\\mona\\Dropbox\\semEval2015\\dataset\\test_evaluation_task2a\\test_evaluation_task2a\\STS.gs.answers-forums.txt", 
				"C:\\Users\\mona\\Dropbox\\semEval2015\\txtSimScores\\STS.answers-forums-scores.output.txt", 
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\answers-forums-15-Trwp-scores");
		
		sem2015SentenceExtractor.ExtractOutputScoresFromTrwpSem2015
		("C:\\Users\\mona\\Dropbox\\semEval2015\\dataset\\test_evaluation_task2a\\test_evaluation_task2a\\STS.gs.answers-students.txt", 
				"C:\\Users\\mona\\Dropbox\\semEval2015\\txtSimScores\\STS.answers-students-scores.output.txt", 
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\answers-students-15-Trwp-scores");
		
		
		sem2015SentenceExtractor.ExtractOutputScoresFromTrwpSem2015
		("C:\\Users\\mona\\Dropbox\\semEval2015\\dataset\\test_evaluation_task2a\\test_evaluation_task2a\\STS.gs.belief.txt", 
				"C:\\Users\\mona\\Dropbox\\semEval2015\\txtSimScores\\STS.belief-scores.output.txt", 
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\belief-15-Trwp-scores");
		
		
		sem2015SentenceExtractor.ExtractOutputScoresFromTrwpSem2015
		("C:\\Users\\mona\\Dropbox\\semEval2015\\dataset\\test_evaluation_task2a\\test_evaluation_task2a\\STS.gs.headlines.txt", 
				"C:\\Users\\mona\\Dropbox\\semEval2015\\txtSimScores\\STS.headlines-scores.output.txt", 
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\headlines-15-Trwp-scores");
		
		
		sem2015SentenceExtractor.ExtractOutputScoresFromTrwpSem2015
		("C:\\Users\\mona\\Dropbox\\semEval2015\\dataset\\test_evaluation_task2a\\test_evaluation_task2a\\STS.gs.images.txt", 
				"C:\\Users\\mona\\Dropbox\\semEval2015\\txtSimScores\\STS.images-scores.output.txt", 
				"D:\\Dal\\ComputationalIntelligence\\SimScores\\TextSimilarity\\images-15-Trwp-scores");
	}

}

