package dal.relatedness.phrase.startup;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import dal.clustering.document.dataset.agnews.AgNewsExternalEvaluation;
import dal.clustering.document.dataset.agnews.ClusterUnSupervisedAgNews;
import dal.clustering.document.dataset.agnews.ClusterUnSupervisedAgNewsRAD;
import dal.clustering.document.dataset.biomedical.BioMedicalExternalEvaluation;
import dal.clustering.document.dataset.biomedical.ClusterUnSupervisedBioMedical;
import dal.clustering.document.dataset.biomedical.ClusterUnSupervisedBioMedicalRAD;
import dal.clustering.document.dataset.googlewebsnippets.ClusterUnSupervisedGoogleWebSnippet;
import dal.clustering.document.dataset.googlewebsnippets.ClusterUnSupervisedGoogleWebSnippetRAD;
import dal.clustering.document.dataset.googlewebsnippets.WebSnippetExternalEvaluation;
import dal.clustering.document.dataset.stackoverflow.ClusterUnSupervisedStackOverflowRAD;
import dal.clustering.document.dataset.stackoverflow.StackOverflowExternalEvaluation;
import dal.clustering.document.dataset.trec.ClusterUnSupervisedTrec;
import dal.clustering.document.dataset.trec.ClusterUnSupervisedTrecRAD;
import dal.clustering.document.dataset.trec.TrecExternalEvaluation;
import dal.relatedness.phrase.bigram.LoadBigram;
import dal.relatedness.phrase.entities.bigram.BigramKey;
import dal.relatedness.phrase.unigram.LoadUnigram;



//* rakib@cgm6:~/java/RelatednessSolution/relatedness/src/main/java$ shopt -s globstar
//rakib@cgm6:~/java/RelatednessSolution/relatedness/src/main/java$ javac  -cp ".:./../../../lib/JavaFastPFOR-0.1.9.jar" **/*.java
//rakib@cgm6:~/java/RelatednessSolution/relatedness/src/main/java$ java -cp ".:./../../../lib/JavaFastPFOR-0.1.9.jar" dal.relatedness.phrase.startup.App

//rakib@cgm6:~/java/RelatednessSolution/relatedness/src/main/java$ shopt -s globstar
//rakib@cgm6:~/java/RelatednessSolution/relatedness/src/main/java$ javac  -cp ".:./../../../lib/JavaFastPFOR-0.1.9.jar:./../../../lib/weka.jar" **/*.java
//rakib@cgm6:~/java/RelatednessSolution/relatedness/src/main/java$ java -cp ".:./../../../lib/JavaFastPFOR-0.1.9.jar:./../../../lib/weka.jar" dal.relatedness.phrase.startup.App

//rakib@cgm6:~/java/RelatednessSolution/relatedness/src/main/java$ javac  -cp ".:./../../../lib/JavaFastPFOR-0.1.9.jar:./../../../lib/weka.jar:./../../../lib/commons-lang3-3.2.1.jar:./../../../lib/gtm.jar:./../../../lib/jdistlib-0.4.5.jar:./../../../lib/stanford-corenlp-3.3.1.jar:./../../../lib/stanford-parser-3.2.0.jar:./../../../lib/trove4j-3.0.3.jar" **/*.java
//rakib@cgm6:~/java/RelatednessSolution/relatedness/src/main/java$ java -cp ".:./../../../lib/JavaFastPFOR-0.1.9.jar:./../../../lib/weka.jar:./../../../lib/commons-lang3-3.2.1.jar:./../../../lib/gtm.jar:./../../../lib/jdistlib-0.4.5.jar:./../../../lib/stanford-corenlp-3.3.1.jar:./../../../lib/stanford-parser-3.2.0.jar:./../../../lib/trove4j-3.0.3.jar" dal.relatedness.phrase.startup.App

//background proces
//nohup java -cp ".:./../../../lib/JavaFastPFOR-0.1.9.jar:./../../../lib/weka.jar:./../../../lib/commons-lang3-3.2.1.jar:./../../../lib/gtm.jar:./../../../lib/jdistlib-0.4.5.jar:./../../../lib/stanford-corenlp-3.3.1.jar:./../../../lib/stanford-parser-3.2.0.jar:./../../../lib/trove4j-3.0.3.jar" dal.relatedness.phrase.startup.App > backlog.txt &
 
public class App 
{
    public static void main( String[] args ) throws Exception
    {
//    	long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
//    	Map<String, int[]> uniBitset = new LoadUnigramBitSet().loadUniGramsWithId();
//    	//size(uniBitset);
//    	long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
//    	long actualMemUsed=afterUsedMem-beforeUsedMem;
//    	System.out.println("Used memory="+actualMemUsed);
    	
//    	long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
//    	Map<String, Integer> uni = new LoadUnigram().loadUniGramsWithId();
//    	//size(uni);
//    	long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
//    	long actualMemUsed=afterUsedMem-beforeUsedMem;
//    	System.out.println("Used memory="+actualMemUsed);
    	
    	//new TokenizeBigram().tokenizeById();
    	
    	//new BigramFileIndexing().IndexFilesByTokenPrefix();
    	
    	//new FourgramFileIndexing().IndexFilesByTokenPrefix();
    	
    	//new TokenizeFourGram().tokenize();
    	
    	//new TokenizeFourGram().tokenizeByIdFreq();
    	
    	//new ContextProcessorFourgram().ProcessLeftRightMiddleContextForPhraseByRegx();
    	
    	//new TokenizeBigram().TokenizeBySumId();
    	
    	//new TokenizeBigram().TokenizeByDoubleKey();
    	
    	//new AppSupport().findIdFrequencyOfPhrases();
    	
    	//new Relatedness().ComputeByBase62Ids();
    	//new Relatedness().ComputeBySumId();
    	//new Relatedness().ComputeByDoubleKey();
    	
    	//new BigramGeneratorFromFourGramContext().GenerateBigramFromFiltered4gmContexts();
    	
    	//new BigramStemmer().StemPreocessedBigramsFrom4gram();
    	
    	
    	//new RelatednessProcessedContext().Compute();
    	//
    	
    	//new TrigramFileIndexing().IndexFilesByTokenPrefix();
    	
    	
    	//new UnigramStemmer().StemUnigram(); //1
    	//new BigramStemmer().StemRegularBigrams(); //2
    	//new TrigramStemmer().ProcessContextAndStem3grams(); //3
    	//new FourgramStemmer().StemPreocessed4grams(); //4
    	
    	//new UnigramTrigramTuning().TuneTrigramUnigram(); //5
    	//new BigramFourgramTuning().TuneFourgramBigram(); //6
    	//new ContextDictionaryGenerator().AccumulateContextDictionaryWithId(); //7
    	//new TokenizeTrigramFromCrossChecked().TokenizeTrigram(); //8
    	//new TokenizeFourgramFromCrossChecked().TokenizeFourGram(); //9
    	
    	//new TuneUnigramBigram().TuneUniBigram();
    	

    	//new TokenizeUnigram().TokenizeStemmedUnigram();
    	
    	
    	//No Encoding
    	//new PhraseRelatednessTokenized().Compute(); //10
    	//new PhraseRelatednessTokenized().ComputeFast(); //10
    	//End no encoding
    	
    	////FastPFOR
    	//new TokenizeFourgramFromTokenizedByFastPFOR().TokenizeFourgramFromTokenizedFileFastPFOR(); //10
    	//new TokenizeTrigramFromTokenizedByFastPFOR().TokenizeTriramFromTokenizedFileFastPFOR(); //11
    	//new PhraseRelatednessTokenizedFastPFOR().ComputeFastPFOR(); //12
    	//new PhraseRelatednessTokenizedFastPFOR().ComputeFastPFORAlHmFast(); //12
    	//end FastPFOR
    	
    	//Variable Length byte Encoding    	
    	//new TokenizeTrigramFromTokenizedByVByteCoding().TokenizeTrigramByteCoding(); //10
    	//new TokenizeFourgramFromTokenizedByVByteCoding().TokenizeFourgramByteCoding(); //11
    	//new PhraseRelatednessTokenizedVByteCoding().ComputeVByteCoding(); //12
    	//new PhraseRelatednessTokenizedVByteCoding().ComputeVByteCodingAlHmFast(); //12
    	//end Variable Length byte Encoding
    	
    	//EliasOmega
    	//new TokenizeTrigramFromTokenizedByEliasOmega().TokenizeTrigramEliasOmega(); //10
    	//new TokenizeFourgramFromTokenizedByEliasOmega().TokenizeFourgramEliasOmega();//11
    	//End EliasOmega
    	
    	//count nonzero pairs
    	//new PhraseRelCountNonZeroSimilarity().CountNonZeroSimilarityPairs();
    	
    	//end conut nonzero pairs
    	
    	//Tsne
    	//new  TsneInputMatrixConstructor().ConstructInputMatrix(); 
    	//end Tsne
    	
    	//FastMap
    	//new FastMapDimReducer().ReduceDimensionality();
    	//end FastMap
    	
    	//doc clustering
    	//new ClusterMiniNewsGroupDocument().ClusterDocsNGramBasedSimilarity();
    	
    	//new ClusterAgNewsDocument().ClusterDocsNGramBasedSimilarity();
    	//new ClusterSemiSupervisedAgNews().ClusterDocsW2VecBasedSimilarity();
    	//new ClusterSemiSupervisedAgNews().ClusterDocsNGramBasedSimilarity();
    	//new ClusterUnSupervisedAgNews().ClusterDocsNGramBasedSimilarityGtm();
    	//new ClusterUnSupervisedAgNews().ClusterDocsSimilarityByW2VecFollowingGtm();
    	//new ClusterUnSupervisedAgNews().ClusterDocsSimilarityByW2VecAverageVector();
    	//new ClusterUnSupervisedAgNews().ClusterDocsBySimilarityMatrixGtm();
    	//new ClusterUnSupervisedAgNews().ClusterDocsBySimilarityMatrixCosineW2Vec();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixGtmFixedSparsification();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixCosineW2VecCenterBasedSparsification();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixCosineW2VecWeightCenterBasedSparsification();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixCosineTfIdfCenterBasedSparsification();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixCosineCenterBasedSparsificationTfIdf();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixCosineWeightCenterBasedSparsificationTfIdf();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixFromFileSparsificationBFixedNbyKSimilarities();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixFromFileSparsification();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixCosineTfIdf();
    	//new ClusterUnSupervisedAgNews().GenerateDocsDisSimilarityMatrixFromFileSparsificationIterative();
    	//new ClusterUnSupervisedAgNews().GenerateDocSimMatrixW2Vec();
    	//new ClusterUnSupervisedAgNews().GenerateDocSimMatrixW2VecList();
    	//new AgNewsExternalEvaluation().ExternalEvaluate();
    	//new AgNewsExternalEvaluation().ExternalEvaluateRAD();
    	
    	//new ClusterUnSupervisedAgNewsRAD().FindCommonGenerateTrainTest();
    	//new ClusterUnSupervisedAgNewsRAD().GenerateTrainTest();
    	//new ClusterUnSupervisedAgNewsRAD().GenerateTrainTest2List();
    	//new ClusterUnSupervisedAgNewsRAD().GenerateDocTermMatrixW2Vec();
    	//new ClusterUnSupervisedAgNewsRAD().SaveDataToEmbedding();
    	//new ClusterUnSupervisedAgNewsRAD().GenerateSeed();
    	//new ClusterUnSupervisedAgNewsRAD().MergeAndWriteTrainTest();
    	//new ClusterUnSupervisedAgNewsRAD().GenerateTrainTest2List1();
    	
    	//new ProcessGoogleWebSnippet().Process();
    	//new ProcessWebSnippetSTC2().ProcessBySTC2();
    	//new ProcessWebSnippetSTC2().ProcessBySTC2List();
    	//new ClusterSemiSupervisedGoogleWebSnippet().ClusterDocsW2VecBasedSimilarity();
    	//new ClusterUnSupervisedGoogleWebSnippet().ClusterDocsNGramBasedSimilarityGtmAndW2Vec();
    	//new ClusterUnSupervisedGoogleWebSnippetWEKA().ClusterDocsNGramBasedSimilarityGtmAndW2VecByWEKA();
    	//new ClusterUnSupervisedGoogleWebSnippetWEKA().ConstructDocsSimilarityMatrixSparsificationByKMeansW2VecGtm();
    	//new ClusterUnSupervisedGoogleWebSnippet().ClusterDocsNGramBasedSimilarityGtm();
    	//new ClusterUnSupervisedGoogleWebSnippet().ClusterDocsSimilarityByW2VecFollowingGtm();
    	//new ClusterUnSupervisedGoogleWebSnippet().ClusterDocsSimilarityByW2VecAverageVector();
    	//new ClusterUnSupervisedGoogleWebSnippet().ClusterDocsBySimilarityMatrixGtm();
    	//new ClusterUnSupervisedGoogleWebSnippet().ClusterDocsBySimilarityMatrixTrWp();
    	//new ClusterUnSupervisedGoogleWebSnippet().ClusterDocsBySimilarityMatrixEuclidianW2Vec();
    	//new ClusterUnSupervisedGoogleWebSnippet().ClusterDocsBySimilarityMatrixCosineW2Vec();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixGtmFixedSparsification();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixTrwpFixedSparsification();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixCosineW2VecCenterBasedSparsification();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixCosineW2VecWeightCenterBasedSparsification();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixCosineCenterBasedSparsificationTfIdf();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixCosineWeightCenterBasedSparsificationTfIdf();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixFromFileSparsification();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixFromFileSparsificationBFixedNbyKSimilarities();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixFromFileSparsificationStandardDevNbyKSimilarities();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixCosineTfIdf();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixFromFileSparsificationIterative(); // real-doceng
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixFromFileSparsificationIterativeEMNLP(); // real-emnlp
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocsDisSimilarityMatrixFromFileSparsificationIterativeList();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocSimMatrixW2Vec();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocSimMatrixW2VecList();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocSimMatrixVector();
    	//new ClusterUnSupervisedGoogleWebSnippet().GenerateDocSimMatrixBTMKL();
    	new WebSnippetExternalEvaluation().ExternalEvaluate();
    	//new WebSnippetExternalEvaluation().ExternalEvaluateRAD();
    	//new WebSnippetExternalEvaluation().ExternalEvaluateList();
    	//new WebSnippetInternalEvaluation().InternalEvaluateAndSelectModel();
    	
    	//new ClusterUnSupervisedGoogleWebSnippetRAD().GenerateTrainTest();
    	//new ClusterUnSupervisedGoogleWebSnippetRAD().GenerateTrainTest2List();
    	//new ClusterUnSupervisedGoogleWebSnippetRAD().SaveDataToEmbedding();
    	//new ClusterUnSupervisedGoogleWebSnippetRAD().MergeAndWriteTrainTest();
    	//new ClusterUnSupervisedGoogleWebSnippetRAD().GenerateTrainTest2List1();
    	
    	//new ProcessStackOverflow().Process();
    	//new ProcessStackOverflowSTC2().ProcessBySTC2();
    	//new ClusterUnSupervisedStackOverflowWEKA().ClusterDocsNGramBasedSimilarityGtmAndW2VecByWEKA();
    	//new ClusterUnSupervisedStackOverflowWEKA().ConstructDocsSimilarityMatrixSparsificationByKMeansW2VecGtm();
    	//new ClusterUnSupervisedStackOverflow().ClusterDocsNGramBasedSimilarityGtm();
    	//new ClusterUnSupervisedStackOverflow().ClusterDocsSimilarityByW2VecFollowingGtm();
    	//new ClusterUnSupervisedStackOverflow().ClusterDocsSimilarityByW2VecAverageVector();
    	//new ClusterUnSupervisedStackOverflow().ConstructDocsSimilarityMatrixSparsificationByKMeansW2VecGtm();
    	//new ClusterUnSupervisedStackOverflow().ClusterDocsBySimilarityMatrixGtm();
    	//new ClusterUnSupervisedStackOverflow().ClusterDocsBySimilarityMatrixCosineW2Vec();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixGtmFixedSparsification();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixCosineW2VecCenterBasedSparsification();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixCosineW2VecWeightCenterBasedSparsification();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixCosineCenterBasedSparsificationTfIdf();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixCosineWeightCenterBasedSparsificationTfIdf();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixFromFileSparsification();
	    //new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixFromFileSparsificationBFixedNbyKSimilarities();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixCosineTfIdf();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocsDisSimilarityMatrixFromFileSparsificationIterative();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocSimMatrixW2Vec();
    	//new ClusterUnSupervisedStackOverflow().GenerateDocSimMatrixW2VecList();
    	//new StackOverflowExternalEvaluation().ExternalEvaluate();
    	
    	//new ClusterUnSupervisedStackOverflowRAD().GenerateTrainTest();
    	//new ClusterUnSupervisedStackOverflowRAD().GenerateTrainTest2List();
    	//new ClusterUnSupervisedStackOverflowRAD().SaveDataToEmbedding();
    	//new ClusterUnSupervisedStackOverflowRAD().MergeAndWriteTrainTest();
    	//new ClusterUnSupervisedStackOverflowRAD().GenerateTrainTest2List1();
    	
    	//***********biomedical************//
    	//new ProcessBiomedicalData().Process();
    	//new ProcessBiomedicalSTC2().ProcessBySTC2();
    	
    	//new ClusterUnSupervisedBioMedicalRAD().ClusterDocsByFrequentTerms();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateDocSimMatrixW2VecStemmed();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateDocSimMatrixDoc2Vec();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateDocSimMatrixW2VecPrunedFtrs();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateDocSimMatrixW2VecPrunedFtrsPreDefined();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateDocSimMatrixW2VecMaxMin();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateDocSimMatrixW2VecAvgMaxHarmonicMean();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateDocSimMatrixW2VecCenterWeighted();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateDocSimMatrixW2VecNoCommonWords();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification();
    	//new ClusterUnSupervisedBioMedicalRAD().AddhocClustering();
    	//new ClusterUnSupervisedBioMedicalRAD().AddhocClusteringTuned();    	
    	//new ClusterUnSupervisedBioMedicalRAD().ClusterByRemovingTopLengthVectors();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateEnsembleClusters();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateTrainTest();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateTrainTest2(750);
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateTrainTest2List();
    	//new ClusterUnSupervisedBioMedicalRAD().FilterOutliers();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateCommonCluster();
    	//new ClusterUnSupervisedBioMedicalRAD().SaveDataToEmbedding();
        //new ClusterUnSupervisedBioMedicalRAD().MergeAndWriteTrainTest();
    	//new ClusterUnSupervisedBioMedicalRAD().GenerateTrainTest2List1();
    	
    	//new ClusterUnSupervisedBioMedical().ClusterDocsNGramBasedSimilarityGtm();
    	//new ClusterUnSupervisedBioMedical().ClusterDocsSimilarityByW2VecFollowingGtm();
    	//new ClusterUnSupervisedBioMedical().ClusterDocsSimilarityByW2VecAverageVector();
    	//new ClusterUnSupervisedBioMedical().ClusterDocsBySimilarityMatrixGtm();
    	//new ClusterUnSupervisedBioMedical().ClusterDocsBySimilarityMatrixCosineW2Vec();
    	//new ClusterUnSupervisedBioMedical().GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification();
    	//new ClusterUnSupervisedBioMedical().GenerateDocsDisSimilarityMatrixFromFileSparsification();
    	//new ClusterUnSupervisedBioMedical().GenerateDocsDisSimilarityMatrixCosineTfIdf();    	
    	//new ClusterUnSupervisedBioMedical().GenerateDocSimMatrixW2Vec(); //RAD
    	//new ClusterUnSupervisedBioMedical().GenerateDocSimMatrixW2VecList();
    	//new ClusterUnSupervisedBioMedical().GenerateDocSimMatrixGtm();
    	//new ClusterUnSupervisedBioMedical().GenerateDocsDisSimilarityMatrixFromFileSparsificationIterative(); //RAD
	    //new ClusterUnSupervisedBioMedical().GenerateDocsDisSimilarityMatrixFromFileSparsificationBFixedNbyKSimilarities();
    	//new ClusterUnSupervisedBioMedical().GenerateDocsDisSimilarityMatrixCosineW2VecCenterBasedSparsification();
    	//new ClusterUnSupervisedBioMedical().GenerateDocsDisSimilarityMatrixCosineW2VecWeightCenterBasedSparsification();
    	//new ClusterUnSupervisedBioMedical().GenerateDocsDisSimilarityMatrixCosineCenterBasedSparsificationTfIdf();
    	//new ClusterUnSupervisedBioMedical().GenerateDocsDisSimilarityMatrixCosineWeightCenterBasedSparsificationTfIdf();
        //new BioMedicalExternalEvaluation().ExternalEvaluate();
    	//new BioMedicalExternalEvaluation().ExternalEvaluateRAD();
    	//***********end biomedical************//
    	
    	
    	//////**************trec/////////////
    	//new ClusterUnSupervisedTrec().GenerateDocSimMatrixW2Vec();
    	//new ClusterUnSupervisedTrec().GenerateDocsDisSimilarityMatrixFromFileSparsificationIterative();
    	//new TrecExternalEvaluation().ExternalEvaluate();
    	//new TrecExternalEvaluation().ExternalEvaluateRAD();
    	
    	
    	//new ClusterUnSupervisedTrecRAD().GenerateTrainTest();
    	//new ClusterUnSupervisedTrecRAD().GenerateTrainTest2(700);
    	//new ClusterUnSupervisedTrecRAD().MergeAndWriteTrainTest();
    	//new ClusterUnSupervisedTrecRAD().GenerateTrainTest2List1();
    	
    	//*end trec
    	
    	//new ClusterUnSupervisedCicling2002().ClusterDocsBySimilarityMatrixCosineW2Vec();
    	//new ClusterUnSupervisedCicling2002().ClusterDocsBySimilarityMatrixGtm();
    	//new Cicling2002ExternalEvaluation().ExternalEvaluate();
    	
    	
    	//new W2VecAggregator().AggregateW2VecsForDatasets();
    	
    	//new BTMProcessor().WriteBodies();
    	//new BTMProcessor().LabelInstancesByProbabilityOfTopicGivenDocument();
    	//new WebSnippetExternalEvaluation().ExternalEvaluateLoop();
    	//new StackOverflowExternalEvaluation().ExternalEvaluateLoop();
    	//new AgNewsExternalEvaluation().ExternalEvaluateLoop();
    	//new BioMedicalExternalEvaluation().ExternalEvaluateLoop();
    	
    	//end doc clustering
    	
    	//text rel
    	//new TextRelatednessTokenized().ComputeTextRelUsingTokenizedNGramNew();
    	//new TextRelatednessTokenizedOverlappingContext().ComputeTextRelatednessUsingUnigramOverlappingContext();
    	//end rext rel
    	
    	//computational intelligence
    	//new CIStartup().CICallTextSim();
    	//end computational intelligence
    	
    	//external evaluation
    	
    	//end external evaluation
    }
    
    static void size(Map map) {
        try{
            System.out.println("Index Size: " + map.size());
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(baos);
            oos.writeObject(map);
            oos.close();
            System.out.println("Data Size: " + baos.size());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    static int[] mergeArrays(int[] a, int[] b) {
    	
    	int[] merged = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;

        while (i < a.length && j < b.length)
        {
            if (a[i] < b[j])       
            	merged[k++] = a[i++];

            else        
            	merged[k++] = b[j++];               
        }

        while (i < a.length)  
        	merged[k++] = a[i++];


        while (j < b.length)    
        	merged[k++] = b[j++];

        return merged;
    }
    
    private static void mergeTwoArray() {
    	int m1 = Integer.parseInt(System.console().readLine());
    	int a1[] = new int[m1];
    	for(int i=0;i<m1;i++){
    		a1[i] = Integer.parseInt(System.console().readLine());
    	}
    	
    	int m2 = Integer.parseInt(System.console().readLine());
    	int a2[] = new int[m2];
    	for(int i=0;i<m2;i++){
    		a2[i] = Integer.parseInt(System.console().readLine());
    	}
    	
    	mergeArrays(a1,a2);
	}

	static int maxDiff(int a[]){
    	int maxDiff =-1;
    	for(int i=0;i<a.length-1;i++){
    		for(int j=i+1;j<a.length;j++){
    			if(a[i]<a[j]){
    				int diff =  a[j]-a[i];
    				if(diff>maxDiff){
    					maxDiff = diff;
    				}
    			}
    		}
    	}
    	
    	return maxDiff;
    }
    
    public static void add(){
    	try{
    		int c = 4/0;
    	}catch(Exception e){
    		System.out.println("bbbb");
    	}finally{
    		System.out.println("aa");
    	}
    }
}

class AppSupport{
	
	public void findIdFrequencyOfPhrases() throws IOException, InterruptedException, ExecutionException{
		Map<String, Integer> uniWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();
		
		String fileOfBgPhrases = "D:\\githubprojects\\NLP-Projects\\RelatednessSolution\\phraserelatedness\\src\\main\\java\\dal\\relatedness\\startup\\phrases";
		
		Map<Character, ArrayList<String>> phraseGroups = new LinkedHashMap<Character, ArrayList<String>>();
		
		String sCurrentLine;
		BufferedReader br = new BufferedReader(new FileReader(fileOfBgPhrases));

		while ((sCurrentLine = br.readLine()) != null) {
			sCurrentLine = sCurrentLine.trim().toLowerCase();
			
			Character key = sCurrentLine.charAt(0);
			
			if(!phraseGroups.containsKey(key)){
				ArrayList<String> al = new ArrayList<String>();
				al.add(sCurrentLine);
				phraseGroups.put(key, al);
			}else{
				ArrayList<String> al = phraseGroups.get(key);
				if(!al.contains(sCurrentLine)) al.add(sCurrentLine);
				phraseGroups.put(key, al);
			}
			 
		}
		
		br.close();
		
		LoadBigram loadbg = new LoadBigram();
		
		for(Character key: phraseGroups.keySet()){
			
			//Map<BigramKey, int[]> bgTokenWithIds = loadbg.populateBigramsWithIdFreqs(Directories.tokenizedBiGramDir+key);
			ArrayList<Map<BigramKey, Integer>> bgTokenWithIds = loadbg.populateBigramsWithIdsInParallel();
			
			ArrayList<String> phrases = phraseGroups.get(key);
			
			for(String phrase: phrases){
				String arr[] = phrase.toLowerCase().split("\\s+");
				int uni1 = uniWithIds.get(arr[0]);
				int uni2 = uniWithIds.get(arr[1]);
						
				BigramKey bgKey = new BigramKey(uni1, uni2);
				
				int bgId = bgTokenWithIds.get(0).containsKey(bgKey) ? bgTokenWithIds.get(0).get(bgKey): -1;
				//int freq = bgTokenWithIds.containsKey(bgKey) ? bgTokenWithIds.get(bgKey)[1]: -1;
				
				//System.out.println(phrase+","+bgId+","+freq);
				System.out.println(phrase+","+bgId);
			}
		}
	}
		
	
}
