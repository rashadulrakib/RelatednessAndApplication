/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.phSim.context;

import dal.simMethod.NormalizeSimilarity;
import dal.simMethod.SimMethod;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author administrator
 */
public class PhraseSimilarity {

    String ngDir = "/home/administrator/rakib/google-n-gram/n-gram-indexed/";
    String stopWordFile = "/home/administrator/rakib/phrase-sim-data/stopWords.txt";
    double N = 3107547215.0;
    HashMap<String,Double> hmPhCountCache = new  HashMap<String, Double>();

    public PhraseSimilarity(String ngDir, String stopWordFile) {
        this.ngDir = ngDir;
        this.stopWordFile = stopWordFile;
    }

    public PhraseSimilarity(String ngDir, String stopWordFile, double N) {
        this.ngDir = ngDir;
        this.stopWordFile = stopWordFile;
        this.N = N;
    }

    public double getSimilarity(String ph1, String ph2, SimMethod methodName) throws Exception {
        double sim = 0.0;
        try {
            sim = computeSimilarity(ph1, ph2, methodName);
        } catch (Exception ex) {
            throw ex;
        }

        return sim;
    }
    
    /*private double computeSimilarity(String ph1, String ph2, SimMethod methodName) throws Exception {
     //low result
        double score = 0.0;
        try {

            ContextFilterUtil objCfu = new ContextFilterUtil(stopWordFile);
            ContextUtil objCu = new ContextUtil(ngDir);
            NGramCountUtil objNGcu = new NGramCountUtil(ngDir);
            GeneralUtil objGu = new GeneralUtil();
            NormalizeSimilarity objNs = new NormalizeSimilarity();

            int ph1Size = ph1.split("\\s+").length;
            int ph2Size = ph2.split("\\s+").length;

            int[] ph1ContextIndexes = objCu.getPhContextIndexes(ph1Size);
            int[] ph2ContextIndexes = objCu.getPhContextIndexes(ph2Size);
            
            int[] ph1Indexes = objCu.getPhIndexes(ph1Size);
            int[] ph2Indexes = objCu.getPhIndexes(ph2Size);

            double avgPhSize = (ph1Size + ph2Size) / 2;
            HashMap<String, Double> hmPh1SuffixContexts = objCu.getContexts(ph1, "^" + ph1 + objCu.getContextRegx(avgPhSize, FilterMode.SUFFIX), objCu.getPhContextSFI(ph1Size, FilterMode.SUFFIX), ph1ContextIndexes, ph1Indexes);
            HashMap<String, Double> hmPh2SuffixContexts = objCu.getContexts(ph2, "^" + ph2 + objCu.getContextRegx(avgPhSize, FilterMode.SUFFIX), objCu.getPhContextSFI(ph2Size, FilterMode.SUFFIX), ph2ContextIndexes, ph2Indexes);
            hmPh1SuffixContexts = objCfu.filterContexts(hmPh1SuffixContexts, FilterMode.SUFFIX);
            hmPh2SuffixContexts = objCfu.filterContexts(hmPh2SuffixContexts, FilterMode.SUFFIX);

            HashMap<String, Double> hmPh1PrefixContexts = objCu.getContexts(ph1, "^" + ph1 + objCu.getContextRegx(avgPhSize, FilterMode.PREFIX), objCu.getPhContextSFI(ph1Size, FilterMode.PREFIX), ph1ContextIndexes, ph1Indexes);
            HashMap<String, Double> hmPh2PrefixContexts = objCu.getContexts(ph2, "^" + ph2 + objCu.getContextRegx(avgPhSize, FilterMode.PREFIX), objCu.getPhContextSFI(ph2Size, FilterMode.PREFIX), ph2ContextIndexes, ph2Indexes);
            hmPh1PrefixContexts = objCfu.filterContexts(hmPh1PrefixContexts, FilterMode.PREFIX);
            hmPh2PrefixContexts = objCfu.filterContexts(hmPh2PrefixContexts, FilterMode.PREFIX);

            HashMap<String, Double> hmPh1infixContexts = objCu.getContexts(ph1, "^" + ph1 + objCu.getContextRegx(avgPhSize, FilterMode.INFIX), objCu.getPhContextSFI(ph1Size, FilterMode.INFIX), ph1ContextIndexes, ph1Indexes);
            HashMap<String, Double> hmPh2infixContexts = objCu.getContexts(ph2, "^" + ph2 + objCu.getContextRegx(avgPhSize, FilterMode.INFIX), objCu.getPhContextSFI(ph2Size, FilterMode.INFIX), ph2ContextIndexes, ph2Indexes);
            hmPh1infixContexts = objCfu.filterContexts(hmPh1infixContexts, FilterMode.INFIX);
            hmPh2infixContexts = objCfu.filterContexts(hmPh2infixContexts, FilterMode.INFIX);

            double countPh1=0;
            double countPh2=0;
            if(hmPhCountCache.containsKey(ph1)){
                countPh1 = hmPhCountCache.get(ph1);
            }else{
                countPh1 = objNGcu.GetNGramCount(ph1, "^" + ph1 + objCu.getNGramRegx(avgPhSize), objCu.getNGramSuffix(ph1Size));
                hmPhCountCache.put(ph1, countPh1);
            }
            
            if(hmPhCountCache.containsKey(ph2)){
                countPh2 = hmPhCountCache.get(ph2);
            }else{
                countPh2 = objNGcu.GetNGramCount(ph2, "^" + ph2 + objCu.getNGramRegx(avgPhSize), objCu.getNGramSuffix(ph2Size));
                hmPhCountCache.put(ph2, countPh2);
            }
            
            //Find the overlapping bi-gram contexts
            HashMap<String, Double[]> hmCommonBiGramsSuffix = objGu.getCommonVectors(hmPh1SuffixContexts,hmPh2SuffixContexts);
            HashMap<String, Double[]> hmCommonBiGramsPrefix = objGu.getCommonVectors(hmPh1PrefixContexts,hmPh2PrefixContexts);
            HashMap<String, Double[]> hmCommonBiGramsInfix = objGu.getCommonVectors(hmPh1infixContexts,hmPh2infixContexts);
            
            HashMap<String, Double[]> hmCommonAll = new HashMap<String, Double[]>();
            hmCommonAll.putAll(hmCommonBiGramsSuffix);
            hmCommonAll.putAll(hmCommonBiGramsPrefix);
            hmCommonAll.putAll(hmCommonBiGramsInfix);
            //end
            
            //statistical pruning on commons
           HashMap<String, Double[]> hmCommonNonPrunedAll = objCfu.filterNormalizedWeightBySTD(hmCommonAll, countPh1, countPh2);
           ArrayList<String> comMinusFiltered = objGu.getComMinusFilteredKeys(hmCommonNonPrunedAll.keySet(),hmCommonAll.keySet()); 
           //end
           
           //create all bi-gram contexts
           HashMap<String, Double> hmPh1 = objGu.mergeContextKeys(new ArrayList(Arrays.asList(hmPh1SuffixContexts, hmPh1PrefixContexts, hmPh1infixContexts)));
           HashMap<String, Double> hmPh2 = objGu.mergeContextKeys(new ArrayList(Arrays.asList(hmPh2SuffixContexts, hmPh2PrefixContexts, hmPh2infixContexts)));
           hmPh1 = objGu.removeKeys(hmPh1, comMinusFiltered);
           hmPh2 = objGu.removeKeys(hmPh2, comMinusFiltered);
           //end
           
           double cosSim = objGu.getCosineSimilarity(hmPh1, hmPh2, countPh1, countPh2);
           double commonCount = objGu.getCommonCount(hmCommonAll);
          
            score = objNs.normalizeSimilarity(methodName, countPh1, countPh2, commonCount * cosSim);

        } catch (Exception ex) {
            throw ex;
        }
        return score;
    }*/

    private double computeSimilarity(String ph1, String ph2, SimMethod methodName) throws Exception {
        double score = 0.0;
        try {

            ContextFilterUtil objCfu = new ContextFilterUtil(stopWordFile);
            ContextUtil objCu = new ContextUtil(ngDir);
            NGramCountUtil objNGcu = new NGramCountUtil(ngDir);
            GeneralUtil objGu = new GeneralUtil();
            NormalizeSimilarity objNs = new NormalizeSimilarity();

            int ph1Size = ph1.split("\\s+").length;
            int ph2Size = ph2.split("\\s+").length;

            int[] ph1ContextIndexes = objCu.getPhContextIndexes(ph1Size);
            int[] ph2ContextIndexes = objCu.getPhContextIndexes(ph2Size);
            
            int[] ph1Indexes = objCu.getPhIndexes(ph1Size);
            int[] ph2Indexes = objCu.getPhIndexes(ph2Size);

            double avgPhSize = (ph1Size + ph2Size) / 2;
            HashMap<String, Double> hmPh1SuffixContexts = objCu.getContexts(ph1, "^" + ph1 + objCu.getContextRegx(avgPhSize, FilterMode.SUFFIX), objCu.getPhContextSFI(ph1Size, FilterMode.SUFFIX), ph1ContextIndexes, ph1Indexes);
            HashMap<String, Double> hmPh2SuffixContexts = objCu.getContexts(ph2, "^" + ph2 + objCu.getContextRegx(avgPhSize, FilterMode.SUFFIX), objCu.getPhContextSFI(ph2Size, FilterMode.SUFFIX), ph2ContextIndexes, ph2Indexes);
            hmPh1SuffixContexts = objCfu.filterContexts(hmPh1SuffixContexts, FilterMode.SUFFIX);
            hmPh2SuffixContexts = objCfu.filterContexts(hmPh2SuffixContexts, FilterMode.SUFFIX);

            HashMap<String, Double> hmPh1PrefixContexts = objCu.getContexts(ph1, "^" + ph1 + objCu.getContextRegx(avgPhSize, FilterMode.PREFIX), objCu.getPhContextSFI(ph1Size, FilterMode.PREFIX), ph1ContextIndexes, ph1Indexes);
            HashMap<String, Double> hmPh2PrefixContexts = objCu.getContexts(ph2, "^" + ph2 + objCu.getContextRegx(avgPhSize, FilterMode.PREFIX), objCu.getPhContextSFI(ph2Size, FilterMode.PREFIX), ph2ContextIndexes, ph2Indexes);
            hmPh1PrefixContexts = objCfu.filterContexts(hmPh1PrefixContexts, FilterMode.PREFIX);
            hmPh2PrefixContexts = objCfu.filterContexts(hmPh2PrefixContexts, FilterMode.PREFIX);

            HashMap<String, Double> hmPh1infixContexts = objCu.getContexts(ph1, "^" + ph1 + objCu.getContextRegx(avgPhSize, FilterMode.INFIX), objCu.getPhContextSFI(ph1Size, FilterMode.INFIX), ph1ContextIndexes, ph1Indexes);
            HashMap<String, Double> hmPh2infixContexts = objCu.getContexts(ph2, "^" + ph2 + objCu.getContextRegx(avgPhSize, FilterMode.INFIX), objCu.getPhContextSFI(ph2Size, FilterMode.INFIX), ph2ContextIndexes, ph2Indexes);
            hmPh1infixContexts = objCfu.filterContexts(hmPh1infixContexts, FilterMode.INFIX);
            hmPh2infixContexts = objCfu.filterContexts(hmPh2infixContexts, FilterMode.INFIX);

            HashMap<String, Double> hmPh1 = objGu.mergeContextKeys(new ArrayList(Arrays.asList(hmPh1SuffixContexts, hmPh1PrefixContexts, hmPh1infixContexts)));
            HashMap<String, Double> hmPh2 = objGu.mergeContextKeys(new ArrayList(Arrays.asList(hmPh2SuffixContexts, hmPh2PrefixContexts, hmPh2infixContexts)));

            //System.out.println("\nContexts for "+ph1+","+ph2);
            //System.out.println(hmPh1.toString());
            //System.out.println(hmPh2.toString());
            
            PrintWriter pr = new PrintWriter("D:\\Google-n-gram\\Zichu\\"+ph1+".txt");
            
            for(String context: hmPh1.keySet())
            {
            	pr.println(context+"="+hmPh1.get(context));
            }
            
            pr.close();
            
            pr = new PrintWriter("D:\\Google-n-gram\\Zichu\\"+ph2+".txt");
            
            for(String context: hmPh2.keySet())
            {
            	pr.println(context+"="+hmPh2.get(context));
            }
            
            pr.close();
            
            double countPh1=0;
            double countPh2=0;
            if(hmPhCountCache.containsKey(ph1)){
                countPh1 = hmPhCountCache.get(ph1);
            }else{
                countPh1 = objNGcu.GetNGramCount(ph1, "^" + ph1 + objCu.getNGramRegx(avgPhSize), objCu.getNGramSuffix(ph1Size));
                hmPhCountCache.put(ph1, countPh1);
            }
            
            if(hmPhCountCache.containsKey(ph2)){
                countPh2 = hmPhCountCache.get(ph2);
            }else{
                countPh2 = objNGcu.GetNGramCount(ph2, "^" + ph2 + objCu.getNGramRegx(avgPhSize), objCu.getNGramSuffix(ph2Size));
                hmPhCountCache.put(ph2, countPh2);
            }
            
            
            HashMap<String, Double> hmPhCos1 = hmPh1;
            HashMap<String, Double> hmPhCos2 = hmPh2;

            ArrayList<HashMap<String, Double>> lhm = objCfu.filterNormalizedWeightBySTD(hmPh1, hmPh2, countPh1, countPh2);
            hmPh1 = lhm.get(0);
            hmPh2 = lhm.get(1);

            ArrayList<HashMap<String, Double>> lhmCos = objGu.getCommonVectors(hmPhCos1, hmPhCos2);
            HashMap<String, Double> hmPh1Common = lhmCos.get(0);

            ArrayList<String> comMinusFiltered = objGu.getComMinusFilteredKeys(hmPh1Common, hmPh1);
            hmPhCos1 = objGu.removeKeys(hmPhCos1, comMinusFiltered);
            hmPhCos2 = objGu.removeKeys(hmPhCos2, comMinusFiltered);

            double cosSim = objGu.getCosineSimilarity(hmPhCos1, hmPhCos2, countPh1, countPh2);
            //score=cosSim;
            double commonCount = objGu.getCommonCount(hmPh1, hmPh2);
            commonCount = commonCount * cosSim;

            score = objNs.normalizeSimilarity(methodName, countPh1, countPh2, commonCount);
            

        } catch (Exception ex) {
            throw ex;
        }
        return score;
    }
}
