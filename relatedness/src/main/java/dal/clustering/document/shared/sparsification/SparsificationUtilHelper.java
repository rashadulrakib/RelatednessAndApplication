package dal.clustering.document.shared.sparsification;

import java.util.HashSet;

public class SparsificationUtilHelper {

	public static boolean IsEnd(double diff, HashSet<Double> uniqueDiffs) {
		try{
			if((diff>=0 && diff<=5) || uniqueDiffs.contains(diff)){
				return true;
			}
			
			for(double val: uniqueDiffs){
				if(diff>=val){
					return true;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean IsEndNbyK(double diff, HashSet<Double> uniqueDiffs,
			double alphaFactor) {
		
		if(alphaFactor<0) return true;
		
		if(uniqueDiffs.contains(diff)){
			return true;
		}
		
		for(double val: uniqueDiffs){
			if(diff>=val){
				return true;
			}
		}
		
		return false;
	}

	public static boolean IsEnd(double diff, HashSet<Double> uniqueDiffs, double alphaFactor) {
		
		if(alphaFactor<=0) return false;
		
		return IsEnd(diff, uniqueDiffs);
	}
	
}
