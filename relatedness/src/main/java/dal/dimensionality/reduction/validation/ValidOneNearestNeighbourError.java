package dal.dimensionality.reduction.validation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ValidOneNearestNeighbourError {
	public static void main(String[] args) {
		String file = "D:\\PhD\\dr.norbert\\nov-23-17\\20newsgroup-2d-output.csv";
		String label = "D:\\PhD\\dr.norbert\\nov-23-17\\20newsgroup-label";
		
		try{

			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			
			List<double[]> pts  =new ArrayList<double[]>();
			while ((str = in.readLine()) != null) {
                str = str.trim();
		        
                String [] arr = str.split(",");
             
                double [] ptXs = new double [arr.length];
                
                for(int i=0; i< arr.length;i++){
                	ptXs[i] = Double.parseDouble(arr[i]);
                }
                
                pts.add(ptXs);
                         
			}
			
			in.close();
			
			 in = new BufferedReader(new FileReader(label));
			 
			 List<Integer> labels  = new ArrayList<Integer>();
			 
			 while ((str = in.readLine()) != null) {
	                str = str.trim();
	                //labels.add(Integer.parseInt(str));
	                labels.add((int)Double.parseDouble(str));
			 }
			 
			 in.close();
			
			int err=0;
			
			for(int i=0;i< pts.size();i++){
				
				double minDIst = Double.MAX_VALUE;
				int jIndex = 0;
				int iIndex = i;
				for(int j=0;j<pts.size();j++){
					
					if(i==j) continue;
					
					double dist = GetDistance(pts.get(i), pts.get(j));
					if(minDIst>=dist){
						minDIst = dist;
						jIndex = j;
					}
				}
				
				System.out.println(iIndex+","+jIndex);
				if(labels.get(iIndex)!=labels.get(jIndex)){
					err++;
				}
				
			}
			
			System.out.println("err="+(double)err/labels.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}

	private static double GetDistance(double [] xs1, double [] xs2) {
		
		if(xs1.length!=xs2.length){
			return 0;
		}
		
		double sqSum = 0;
		
		for(int i=0;i< xs1.length; i++){
			sqSum = sqSum + (xs1[i]-xs2[i])*(xs1[i]-xs2[i]);
		}
		
		return Math.sqrt(sqSum);
	}

}
