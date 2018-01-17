package dal.clustering.document.shared;

public class DocClusterConstant {
	
	public static final String InputGlobalWordEmbeddingFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\glove.42B.300d\\glove.42B.300d.txt";
	//public static final String InputGlobalWordEmbeddingFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/glove.42B.300d/glove.42B.300d.txt";
	
	//public static final String MiniNewsGroupDocsFile = "D:\\PhD\\dr.norbert\\dataset\\mininews-group\\all-data";

  public static final String StopWordFile = "D:\\Google-n-gram\\stopwords\\stopWords.txt";
  //public final static String StopWordFile = "/users/grad/rakib/google-n-gram/stopwords/stopWords.txt";
//	public static final String PhPairsFile = "C:\\Users\\mona\\Desktop\\phpairs";
 //public static final String MiniNewsGroupDocSimFile = "D:\\PhD\\dr.norbert\\dataset\\mininews-group\\docSim-euclidian";
  //public static final String WPhFreqMiniNewsGroupFile = "C:\\Users\\mona\\Desktop\\wpfreqfile";
   
	//public static final String wordPhPairssimAgNewsFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\ag_news\\agnewsGTMwordPhraseSimScores";
	//public static final String wPhFreqAgNewsFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\ag_news\\phrasefreqs";
	
	public static final long MeanBgFreq = 2140;
	public static final	long Std = 314830;
	
	public static final int MinLettersInWord = 2;
	public static final int MaxWordsInPhrase = 2;
	
	public static final int W2VecDimension = 300;
	
	public static final int KMeansMaxIteration = 500;
	public static final int KMedoidMaxIteration = 500;
	
	public static final String GTM_UNIGRAM_BIN_PATH = "gtmdata/corpus.uni";
	public static final String GTM_TRIGRAM_BIN_PATH = "gtmdata/corpus.tri";
	
	public static final String MiniNewsGroupDocsFile = "/users/grad/rakib/dr.norbert/docclustering/mininewsgroup/all-data";
	public static final String MiniNewsGroupDocSimFile = "/users/grad/rakib/dr.norbert/docclustering/mininewsgroup/docSim";
	//public static final String StopWordFile = "/users/grad/rakib/google-n-gram/stopwords/stopWords.txt";

	
	public static final String wordPhPairssimMiniNewsGroupFile = "/users/grad/rakib/dr.norbert/docclustering/wordPhPairsSimFile";
	public static final String WPhFreqMiniNewsGroupFile = "/users/grad/rakib/dr.norbert/docclustering/wpfreqfile";
	
	public static final String OnlyPhPairsFile = "/users/grad/rakib/dr.norbert/docclustering/onlyphpairs";
	public static final String OnlyWordPairsFile = "/users/grad/rakib/dr.norbert/docclustering/onlywdpairs";
	
	public static final String GTMScores = "/users/grad/rakib/TextSimilarity/SimScores/GTMSimScore.txt";
	public static final String wordPhPairssimAgNewsFile = "/users/grad/rakib/dr.norbert/docclustering/agnews/agnewsGTMwordPhraseSimScores";
	public static final String AgNewsDocSimFile = "/users/grad/rakib/dr.norbert/docclustering/agnews/agNewsdocSim";
}
