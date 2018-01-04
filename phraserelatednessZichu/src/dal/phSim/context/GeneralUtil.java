/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.phSim.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author administrator
 */
public class GeneralUtil {

    public GeneralUtil() {
    }

    public HashMap<String, Double> mergeContextKeys(ArrayList<HashMap> arrayList) throws Exception {
        HashMap<String, Double> mergedHm = new HashMap<String, Double>();
        try {
            for (int i = 0; i < arrayList.size(); i++) {
                HashMap<String, Double> hm = arrayList.get(i);
                for (String key : hm.keySet()) {
                    if (mergedHm.containsKey(key)) {
                        mergedHm.put(key, mergedHm.get(key) + hm.get(key));
                    } else {
                        mergedHm.put(key, hm.get(key));
                    }
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return mergedHm;
    }
    
    /*public HashMap<String, Double[]> getCommonVectors(HashMap<String, Double> hmSmall, HashMap<String, Double> hmBig) throws Exception {
        HashMap<String, Double[]> lhm = new HashMap<String, Double[]>();
        try {

            Set<String> smallTerms = hmSmall.keySet();

            for (String term : smallTerms) {
                if (hmBig.containsKey(term)) {
                    lhm.put(term, new Double[]{hmSmall.get(term),hmBig.get(term)});
                }
            }

        } catch (Exception e) {
            throw e;
        }
        return lhm;
    }*/

    public ArrayList<HashMap<String, Double>> getCommonVectors(HashMap<String, Double> hmSmall, HashMap<String, Double> hmBig) throws Exception {
        ArrayList<HashMap<String, Double>> lhm = new ArrayList<HashMap<String, Double>>();
        try {

            HashMap<String, Double> hm1= new HashMap<String, Double>();
            HashMap<String, Double> hm2= new HashMap<String, Double>();
            
            Set<String> smallTerms = hmSmall.keySet();

            for (String term : smallTerms) {
                if (hmBig.containsKey(term)) {
                    hm1.put(term, hmSmall.get(term));
                    hm2.put(term, hmBig.get(term));
                }
            }
            
            lhm.add(hm1);
            lhm.add(hm2);

        } catch (Exception e) {
            throw e;
        }
        return lhm;
    }

    public ArrayList<String> getComMinusFilteredKeys(HashMap<String, Double> hmBig, HashMap<String, Double> hmSmall) throws Exception {
        ArrayList<String> commonNotInFilteredList = new ArrayList<String>();
        try {
            for (String key : hmBig.keySet()) {
                if (!hmSmall.containsKey(key)) {
                    commonNotInFilteredList.add(key);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return commonNotInFilteredList;
    }
    
    public ArrayList<String> getComMinusFilteredKeys(Set<String> small, Set<String> big) throws Exception {
        ArrayList<String> commonNotInFilteredList = new ArrayList<String>();
        try {
            for (String key : big) {
                if (!small.contains(key)) {
                    commonNotInFilteredList.add(key);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return commonNotInFilteredList;
    }

    public HashMap<String, Double> removeKeys(HashMap<String, Double> hmPhCos, ArrayList<String> keys) throws Exception {
        try {
            for (String key : keys) {
                hmPhCos.remove(key);
            }
        } catch (Exception e) {
            throw e;
        }
        return hmPhCos;
    }

    public double getCosineSimilarity(HashMap<String, Double> hmSmall,
            HashMap<String, Double> hmBig, double cp1, double cp2) throws Exception {
        try {

            Set<String> smallTerms = hmSmall.keySet();
            boolean anyCommonTerm = false;
            double sumSamllSquare = 0;
            double sum = 0;
            for (String term : smallTerms) {
                //double termWeightSmall = 1 - Math.log(hmSmall.get(term))/(1+Math.log(countmax)) ;
                //double termWeightSmall = Math.log(hmSmall.get(term))/Math.log(countmax);
                //double termWeightSmall = Math.log(hmSmall.get(term)/countmax);
                //double termWeightSmall = 1;
                double termWeightSmall = Math.log(hmSmall.get(term)) / Math.log(cp1);

                sumSamllSquare = sumSamllSquare + termWeightSmall * termWeightSmall;

                if (hmBig.containsKey(term)) {
                    anyCommonTerm = true;
                    //double termWeightBig=1- Math.log(hmBig.get(term))/(1+Math.log(countmax)) ;
                    //double termWeightBig = Math.log(hmBig.get(term)/countmax);
                    //double termWeightBig = 1;
                    double termWeightBig = Math.log(hmBig.get(term)) / Math.log(cp2);
                    sum = sum + termWeightSmall * termWeightBig;
                    //sum = sum + termWeightSmall * 1;
                }
            }

            if (anyCommonTerm) {
                double sumBigSquare = 0;
                Set<String> bigTerms = hmBig.keySet();
                for (String term : bigTerms) {
                    //double termWeightBig =1- Math.log(hmBig.get(term))/(1+Math.log(countmax)) ;
                    //double termWeightBig = Math.log(hmBig.get(term))/Math.log(countmax) ;
                    //double termWeightBig = Math.log(hmBig.get(term)/countmax);
                    //double termWeightBig = 1;
                    double termWeightBig = Math.log(hmBig.get(term)) / Math.log(cp2);
                    sumBigSquare = sumBigSquare + termWeightBig * termWeightBig;
                }
                return sum / (Math.sqrt(sumSamllSquare) * Math.sqrt(sumBigSquare));
            }
        } catch (Exception e) {
            throw e;
        }

        return 0;
    }
    
    public double getCommonCount(HashMap<String, Double> hmPh1Key, HashMap<String, Double> hmPh2Key) throws Exception {
        double commonCount = 0.0;
        try {
            
            for (String key : hmPh1Key.keySet()) {
                if (hmPh2Key.containsKey(key)) {
                    double min = hmPh1Key.get(key);
                    double max = hmPh2Key.get(key);
                    if (min > max) {
                        double temp = min;
                        min = max;
                        max = temp;
                    }
                    commonCount = commonCount + (min / max) * (min + max);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return commonCount;
    }
    
    public double getCommonCount(HashMap<String, Double[]> hmCommonAll) throws Exception {
        double commonCount = 0.0;
        try {
            for(String term:hmCommonAll.keySet()){
                double min = hmCommonAll.get(term)[0];
                double max = hmCommonAll.get(term)[1];
                if (min > max) {
                    double temp = min;
                    min = max;
                    max = temp;
                }
                commonCount = commonCount + (min / max) * (min + max);
            }
        }
        catch (Exception e) {
            throw e;
        }
        
        return commonCount;
    }
    
}
