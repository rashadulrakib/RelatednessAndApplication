/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.phSim.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author administrator
 */
public class ContextFilterUtil {

    String stopWordFile = "/home/administrator/rakib/phrase-sim-data/stopWords.txt";
    ArrayList<String> listStopWords;

    public ContextFilterUtil(String stopWordFile) throws Exception {
        this.stopWordFile = stopWordFile;
        listStopWords = populateStopWords();
    }

    public HashMap<String, Double> filterContexts(HashMap<String, Double> contexts, FilterMode mode) throws Exception {
        switch (mode) {
            case SUFFIX:
                contexts = suffixKeyFilter(contexts);
                break;
            case INFIX:
                contexts = infixKeyFilter(contexts);
                break;
            case PREFIX:
                contexts = prefixKeyFilter(contexts);
                break;
        }
        return contexts;
    }

    public ArrayList<HashMap<String, Double>> filterNormalizedWeightBySTD(HashMap<String, Double> hmSmall,
            HashMap<String, Double> hmBig, double cp1, double cp2) throws Exception {
        ArrayList<HashMap<String, Double>> lhm = new ArrayList<HashMap<String, Double>>();
        try {

            Set<String> smallTerms = hmSmall.keySet();

            HashMap<String, Double> hm1 = new HashMap<String, Double>();
            HashMap<String, Double> hm2 = new HashMap<String, Double>();

            HashMap<String, Double> commonCunts = new HashMap<String, Double>();
            double sumOfWeight = 0.0;

            for (String term : smallTerms) {
                if (hmBig.containsKey(term)) {
                    double min = hmSmall.get(term) / cp1;
                    double max = hmBig.get(term) / cp2;
                    if (min > max) {
                        double temp = min;
                        min = max;
                        max = temp;
                    }
                    double weight = (min / max) * (hmSmall.get(term) + hmBig.get(term));

                    sumOfWeight = sumOfWeight + weight;
                    commonCunts.put(term, weight);
                }
            }

            double meanWeight = sumOfWeight / commonCunts.size();
            double sumOfSquare = 0.0;
            for (String key : commonCunts.keySet()) {
                sumOfSquare = sumOfSquare + Math.pow(commonCunts.get(key) - meanWeight, 2.0);
            }

            double sd = Math.sqrt(sumOfSquare / commonCunts.size());
            double upperBound = (meanWeight + sd);
            double lowerBound = (meanWeight - sd);

            for (String key : commonCunts.keySet()) {
                if (commonCunts.get(key) >= lowerBound && commonCunts.get(key) <= upperBound) {
                    hm1.put(key, hmSmall.get(key));
                    hm2.put(key, hmBig.get(key));
                }
            }

            lhm.add(hm1);
            lhm.add(hm2);
        } catch (Exception e) {
            throw e;
        }
        return lhm;
    }

    HashMap<String, Double[]> filterNormalizedWeightBySTD(HashMap<String, Double[]> hmCommonAll, double cp1, double cp2) throws Exception {
        HashMap<String, Double[]> hm = new HashMap<String, Double[]>();
        try {
            double sumOfWeight = 0.0;
            HashMap<String, Double> commonCunts = new HashMap<String, Double>();

            for (String term : hmCommonAll.keySet()) {
                double min = hmCommonAll.get(term)[0] / cp1;
                double max = hmCommonAll.get(term)[1] / cp2;
                if (min > max) {
                    double temp = min;
                    min = max;
                    max = temp;
                }
                double weight = (min / max) * (hmCommonAll.get(term)[0] + hmCommonAll.get(term)[1]);
                sumOfWeight = sumOfWeight + weight;
                commonCunts.put(term, weight);
            }

            double meanWeight = sumOfWeight / commonCunts.size();
            double sumOfSquare = 0.0;
            for (String key : commonCunts.keySet()) {
                sumOfSquare = sumOfSquare + Math.pow(commonCunts.get(key) - meanWeight, 2.0);
            }

            double sd = Math.sqrt(sumOfSquare / commonCunts.size());
            double upperBound = (meanWeight + sd);
            double lowerBound = (meanWeight - sd);

            for (String key : commonCunts.keySet()) {
                if (commonCunts.get(key) >= lowerBound && commonCunts.get(key) <= upperBound) {
                    hm.put(key, new Double[]{hmCommonAll.get(key)[0], hmCommonAll.get(key)[1]});
                }
            }

        } catch (Exception e) {
            throw e;
        }
        return hm;
    }

    private HashMap<String, Double> suffixKeyFilter(HashMap<String, Double> hmKey) throws Exception {
        HashMap<String, Double> hm = new HashMap<String, Double>();

        try {
            for (String key : hmKey.keySet()) {

                if (rightSWord(key)) {
                    continue;
                }

                hm.put(key, hmKey.get(key));
            }

        } catch (Exception e) {
            throw e;
        }

        return hm;
    }

    private HashMap<String, Double> prefixKeyFilter(HashMap<String, Double> hmKey) throws Exception {
        HashMap<String, Double> hm = new HashMap<String, Double>();

        try {
            for (String key : hmKey.keySet()) {

                if (leftSWord(key)) {
                    continue;
                }

                hm.put(key, hmKey.get(key));
            }

        } catch (Exception e) {
            throw e;
        }

        return hm;
    }

    private HashMap<String, Double> infixKeyFilter(HashMap<String, Double> hmKey) throws Exception {
        HashMap<String, Double> hm = new HashMap<String, Double>();

        try {
            for (String key : hmKey.keySet()) {

                if (allStopWords(key)) {
                    continue;
                }

                hm.put(key, hmKey.get(key));
            }

        } catch (Exception e) {
            throw e;
        }

        return hm;
    }

    private boolean rightSWord(String key) throws Exception {
        try {
            String[] arr = key.split("\\s+");
            if (arr != null && arr.length > 0 && listStopWords.contains(arr[arr.length - 1])) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    private boolean leftSWord(String key) throws Exception {
        try {
            String[] arr = key.split("\\s+");
            if (arr != null && arr.length > 0 && listStopWords.contains(arr[0])) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    private boolean anyStopWords(String str) throws Exception {
        try {
            String[] arr = str.split("\\s+");
            for (String s : arr) {
                if (listStopWords.contains(s)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
    }

    private boolean allStopWords(String key) throws Exception {
        try {
            String[] arr = key.split("\\s+");
            int count = 0;
            for (String s : arr) {
                if (listStopWords.contains(s)) {
                    count++;
                }
            }
            if (count == arr.length) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }

        return false;
    }

    private ArrayList<String> populateStopWords() throws Exception {
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
            throw e;
        }
        return stopWordsList;
    }
}
