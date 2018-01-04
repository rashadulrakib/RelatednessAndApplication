/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.simMethod;

/**
 *
 * @author administrator
 */
public class NormalizeSimilarity {

    double N = 3107547215.0;

    public NormalizeSimilarity() {
    }

    public NormalizeSimilarity(double N) {
        this.N = N;
    }

    public double normalizeSimilarity(SimMethod method, double cp1, double cp2, double comCount) throws Exception {
        double score = 0.0;
        try {
            switch (method) {
                case NGD:
                    score = normalizeByNGD(cp1, cp2, comCount);
                    break;
            }
        } catch (Exception e) {
            throw e;
        }
        return score;
    }

    private double normalizeByNGD(double countPh1, double countPh2, double commonCount) throws Exception {
        double score = 0.0;
        try {
            if (countPh1 == 0 || countPh2 == 0 || commonCount == 0) {
                score = 0.0;
            } else {
                score = (Math.max(Math.log(countPh1) / Math.log(2), Math.log(countPh2) / Math.log(2)) - Math.log(commonCount) / Math.log(2))
                        / (Math.log(N) / Math.log(2) - Math.min(Math.log(countPh1) / Math.log(2), Math.log(countPh2) / Math.log(2)));
                score = Math.exp(-2 * score);
            }
        } catch (Exception e) {
            throw e;
        }
        return score;
    }
}
