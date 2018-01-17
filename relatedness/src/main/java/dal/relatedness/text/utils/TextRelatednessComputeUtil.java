package dal.relatedness.text.utils;

import java.util.ArrayList;

import dal.clustering.document.shared.PairSim;

public class TextRelatednessComputeUtil {

	public static double ComputeSimilarityFromWeightedMatrixBySTD(
			ArrayList<ArrayList<PairSim>> t1t2simPairList, double commonSize, double t1Size,
			double t2Size, boolean isGTM) {
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
                    if (ocPS.value > (mean + sd)) {
                        filteredMeanSum = filteredMeanSum + ocPS.value;
                        count++;
                    }
                }
                if (count > 0.0) {
                    allMeanSum = allMeanSum + filteredMeanSum / count;
                }
            }
            
            if(isGTM){
            	score = (allMeanSum + commonSize) * (t1Size + t2Size) / t1Size / t2Size;
            }
            else{
            	score = (allMeanSum + commonSize) * (t1Size + t2Size) / 2.0 / t1Size / t2Size;
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return score;
	}

}
