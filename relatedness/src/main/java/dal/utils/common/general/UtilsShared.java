package dal.utils.common.general;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;

public class UtilsShared {
	public static boolean AnyOverlapBetweenTwoRanges(int range1First, int range1Last, int range2First, int range2Last){
		
		if(range1First<=range2First){
			if(range1Last>=range2First){
				return true;
			}
		}
		else{
			if(range2Last>=range1First){
				return true;
			}
		}
		
		return false;	
 }

	public static HashSet<Integer> ConvertIntArrayToHashSet(int[] array) {
		 HashSet<Integer> hs = new HashSet<Integer>();
		
		try{
			for(int id: array){
				hs.add(id);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return hs;
	}
	
	public static  ArrayList<Double> ConvertDoubleArrayToArrayList(double[] array) {
		ArrayList<Double> hs = new ArrayList<Double>();
		
		try{
			for(double id: array){
				hs.add(id);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return hs;
	}
	
	public static double[][] TransposeMatrix(double [][] m){
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }
	
	public static double [][] InitializeMatrix(int rows, int cols){
		double [][] matrix = new double [rows][];
		try{
			for(int i=0;i<rows;i++){
				double [] arr = new double[cols];
				matrix[i] = arr;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return matrix;
	}
	
	public static void WriteMatrixToFile(String outFile, double [][] m, String seperator){
		try{		
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			
			 for (int i = 0; i < m.length; i++){
	            for (int j = 0; j < m[i].length; j++){
	            	bw.write(Double.toString(m[i][j]));
	            	if(j<m[i].length-1){
	            		bw.write(seperator);
	            	}
	            }
	            
	            bw.write("\n");
			 }
			 
			 bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
