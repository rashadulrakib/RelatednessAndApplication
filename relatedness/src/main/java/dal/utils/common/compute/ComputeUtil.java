package dal.utils.common.compute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class ComputeUtil {
	
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
	
	public static LinkedHashMap<String, double[]> ComputeInitialCenters(LinkedHashMap<String, ArrayList<double[]>> trainW2Vecs) {
		LinkedHashMap<String, double[]> InitialCenters = new LinkedHashMap<String, double[]>();

		try{
			for(String label: trainW2Vecs.keySet()){
				int centerSize = trainW2Vecs.get(label).get(0).length;
				double totalDocVecs = trainW2Vecs.get(label).size();
				double [] center = new double[centerSize];
				
				//sum all the vecs
				for(double[] docVec: trainW2Vecs.get(label)){
					for(int i=0;i<docVec.length;i++){
						center [i] = center[i]+docVec[i];
					}
				}
				
				//average centers;
				for(int i=0;i<center.length;i++){
					center[i]=center[i]/totalDocVecs;
				}
				
				InitialCenters.put(label, center);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return InitialCenters;
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
}
