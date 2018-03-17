package dal.utils.common.compute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dal.utils.common.general.UtilsShared;

public class ComputeUtil {
	
	public static double NormalizeSimilarity( double cp1, double cp2, double comCount)  {
        double score = 0.0;
        try {
            score = normalizeByNGD(cp1, cp2, comCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
	 }
	 
	 private static double normalizeByNGD(double countPh1, double countPh2, double commonCount) throws Exception {
        double score = 0.0;
        double N = 3107547215.0;
        try {
            if (countPh1 == 0 || countPh2 == 0 || commonCount == 0) {
                score = 0.0;
            } else {
                score = (Math.max(Math.log(countPh1) / Math.log(2), Math.log(countPh2) / Math.log(2)) - Math.log(commonCount) / Math.log(2))
                        / (Math.log(N) / Math.log(2) - Math.min(Math.log(countPh1) / Math.log(2), Math.log(countPh2) / Math.log(2)));
                score = Math.exp(-2 * score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return score;
	 }
	 
	 public static double ComputeCosineSimilarity(HashMap<String, Double> smallVec,
				HashMap<String, Double> bigVec, double bigSqrt) {
		 double sim = 0;
		 
		try{
			double smallSqSum = 0;
			double v1v2Sum = 0.0;
			
			for(String smallKey: smallVec.keySet()){
				smallSqSum = smallSqSum+ smallVec.get(smallKey)*smallVec.get(smallKey);
				if(bigVec.containsKey(smallKey)){
					v1v2Sum = v1v2Sum+ smallVec.get(smallKey)* bigVec.get(smallKey);
				}
			}
			
			return v1v2Sum/Math.sqrt(smallSqSum)/bigSqrt;
			
		}catch (Exception e) {
            e.printStackTrace();
        }
		return sim;
	 }
	 
	public static double ComputeCosineSimilarity(HashMap<String, Double> v1, HashMap<String, Double> v2) {
		double sim = 0;
		try{
			
			double v1v2Sum = 0.0;
			
			double v1Sq = 0.0;
			for(String v1Key: v1.keySet()){
				if(v2.containsKey(v1Key)){
					v1v2Sum = v1v2Sum+ v1.get(v1Key)* v2.get(v1Key);
				}
				
				v1Sq = v1Sq+Math.pow( v1.get(v1Key), 2);
			}
			
			double v2Sq = 0.0;
			for(String v2Key: v2.keySet()){
				v2Sq = v2Sq+Math.pow( v2.get(v2Key), 2);
			}
			
			return v1v2Sum/Math.sqrt(v1Sq)/Math.sqrt(v2Sq);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sim;
	}
	
	public static double ComputeCosineSimilarity(double [] ftr1, double[] ftr2){
		try{
			List<Double> vec1 = UtilsShared.ConvertDoubleArrayToArrayList(ftr1);
			List<Double> vec2 = UtilsShared.ConvertDoubleArrayToArrayList(ftr2);
			
			return ComputeCosineSimilarity(vec1, vec2);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public static double ComputeCosineSimilarity(List<Double> vec1, List<Double> vec2) {
		try{
			
			if(vec1.size()!=vec2.size()) return 0;
		
			double vec1SqureSum = 0;
			for(Double val: vec1){
				vec1SqureSum = vec1SqureSum + val*val;
			}
			
			double vec2SqureSum = 0;
			for(Double val: vec2){
				vec2SqureSum = vec2SqureSum + val*val;
			}
			
			if(vec1SqureSum==0 || vec2SqureSum==0){
				return 0;
			}
			
			double vec1vec2ProductSum = 0;
			for(int i=0;i<vec1.size();i++){
				vec1vec2ProductSum = vec1vec2ProductSum +  vec1.get(i)*vec2.get(i);
			}
			
			double simScore = vec1vec2ProductSum/(Math.sqrt(vec1SqureSum)*Math.sqrt(vec2SqureSum)); 
			
			if(simScore<0) return 0;
			
			return simScore;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public static double ComputeEuclidianDistance(double[] center, double[] instanceFtrs) {
		double dist = 0;
		try{
			double sumSq =0;
			for(int i=0;i<center.length;i++){
				sumSq = sumSq + (center[i]-instanceFtrs[i])*(center[i]-instanceFtrs[i]);
			}
			
			dist = Math.sqrt(sumSq);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return dist;
	}
	
	public static double ComputeEuclidianDistance(HashMap<String, Double> v1, HashMap<String, Double> v2) {
		double sim = 0;
		try{
			
			Set<String> commonV  = new HashSet<String>();
			
			double commonVSq =0.0;
			double v1Sq = 0;
			double v2Sq= 0;
			
			for(String v1Key: v1.keySet()){
				if(v2.containsKey(v1Key)){
					commonV.add(v1Key);
					commonVSq = commonVSq + Math.pow(v1.get(v1Key)-v2.get(v1Key), 2);
					continue;
				}
				v1Sq = v1Sq + v1.get(v1Key)*v1.get(v1Key);
			}
			
			for(String v2Key: v2.keySet()){
				if(v1.containsKey(v2Key)){
					continue;
				}
				v2Sq = v2Sq + v2.get(v2Key)*v2.get(v2Key);
			}
			
			sim = Math.sqrt(commonVSq+ v1Sq+ v2Sq);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return sim;
	}

	public static double ComputeKLDivergenceSimilarity(double[] ftr1, double[] ftr2) {
		double sim=0;
		try{
			if(ftr1.length!=ftr2.length) return 0;
			
			double [] middleVec = new double[ftr1.length]; 
			for(int i=0;i<ftr1.length;i++){
				middleVec[i] = (ftr1[i]+ ftr2[i])/2; 
			}
			
			double distKL1 = KLDistance(ftr1, middleVec);
			double distKL2 = KLDistance(ftr2, middleVec);
			
			double dist = (distKL1+distKL2)/2;
			
			if(dist>1 || dist<0){
				System.out.println("dist="+dist);
				sim = 0;
				//sim = 1;
			}else{
				sim = 1 - dist;
				//sim = dist;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return sim;
	}

	private static double KLDistance(double[] ftr, double[] middleVec) {
		double dist = 0;
		try{
			for(int i=0;i<ftr.length;i++){
				dist = dist + ftr[i]*Math.log(ftr[i]/middleVec[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dist;
	}
	
	public static double ComputeVecSqrt(HashMap<String, Double> vec){
		double sqrt  =1;
		try{
			double sum = 0;
			for(String key: vec.keySet()){
				sum = sum + vec.get(key)*vec.get(key);
			}
			sqrt = Math.sqrt(sum);
		}catch(Exception e){
			e.printStackTrace();
		}
		return sqrt;
	}
	
}
