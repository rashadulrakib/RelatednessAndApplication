package dal.clustering.document.shared;

public class DocClusterUtilTrWP {
	
	public DocClusterUtilTrWP(){
		
	}
	
	public double ComputeTextSimTrwp(String text1, String text2){ 
		double sim=0;
		try{
			text1 = text1.trim();
			text2 = text2.trim();
			
			if(text1.isEmpty() || text2.isEmpty()) return 0;
			
			
					
		}catch(Exception e){
			e.printStackTrace();
		}
		return sim;
	}
}
