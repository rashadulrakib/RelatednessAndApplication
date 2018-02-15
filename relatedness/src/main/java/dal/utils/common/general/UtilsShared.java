package dal.utils.common.general;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	public static double [][] InitializeMatrix(int rows, int cols, double initialVal){
		double [][] matrix = new double [rows][];
		try{
			for(int i=0;i<rows;i++){
				double [] arr = new double[cols];
				Arrays.fill(arr, initialVal);
				matrix[i] = arr;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return matrix;
	}
	
	public static void WriteMatrixToFile(String outFile, double [][] m, String seperator){
		try{		
			System.out.println("start writing:"+m.length);
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
			 
			 System.out.println("End WriteMatrixToFile");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void ReWriteDocBodyLabelFile(ArrayList<String []> alDocLabelFlat, String outFileBodyLabel,
			String seperator) {
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFileBodyLabel));
			
			for(String [] bodyLabel: alDocLabelFlat){
				bw.write(bodyLabel[1]+seperator+bodyLabel[0]+"\n");
			}
			
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void WriteLinesToFile(String outFile, ArrayList<String> lines){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			
			for(String line: lines){
				bw.write(line+"\n");
			}
			
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static double[][] LoadMatrixFromFile(String file){
		double[][] distMatrix = null; 
		
		try{
			
			System.out.println("Start LoadMatrixFromFile");
			
			String line;
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			int size = line.split("\\s+").length;
			br.close();
			
			distMatrix = new double[size][];
			
			br = new BufferedReader(new FileReader(file));
	        int i=0;
	        while ((line = br.readLine()) != null) {
	           line = line.trim();
	           if(line.isEmpty()) continue;
	           
	           double row [] = ConvertStringArrayToDoubleArray(line.split("\\s+"));
	           distMatrix[i++]= row;
	           
	           System.out.println("Start LoadMatrixFromFile="+i);
	        }
	        br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("End LoadMatrixFromFile");
		
		return distMatrix;
	}
	

	public static double[][] LoadSimilarityMatrixFromDistanceFile(String distFile) {
		double[][] simMatrix = null; 
		
		try{
			
			System.out.println("Start LoadSimilarityMatrixFromDistanceFile");
			
			String line;
			
			BufferedReader br = new BufferedReader(new FileReader(distFile));
			line = br.readLine();
			int size = line.split("\\s+").length;
			br.close();
			
			simMatrix = new double[size][];
			
			br = new BufferedReader(new FileReader(distFile));
	        int i=0;
	        while ((line = br.readLine()) != null) {
	           line = line.trim();
	           if(line.isEmpty()) continue;
	           
	           String[] arr = line.split("\\s+");
	           
	           double[] vals = new double[arr.length];
	           
	           for(int j=0;j<arr.length;j++){
	        	   vals[j] = 1 - Double.parseDouble(arr[j]);
	           }
	           
	           simMatrix[i++]= vals;
	           
	           System.out.println("Start LoadSimilarityMatrixFromDistanceFile="+i);
	        }
	        br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("End LoadSimilarityMatrixFromDistanceFile");
		
		return simMatrix;
	}

	public static double[] ConvertStringArrayToDoubleArray(String[] arr) {
		double[] vals = new double[arr.length];
		
		try{
			for(int i=0; i< arr.length;i++){
				vals[i] = Double.parseDouble(arr[i]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return vals;
	}

	public static double[][] CopyMatrix(double[][] docSimMatrix, boolean byIsDistance) {
		double [][] matrix = new double [docSimMatrix.length][];
		try{
			if(byIsDistance){
				for(int i=0;i<docSimMatrix.length;i++){
					matrix[i] = new double[docSimMatrix.length];
					
					for(int j=0;j<docSimMatrix.length;j++){
						matrix[i][j] = 1-docSimMatrix[i][j];
					}
				}
			}else{
				for(int i=0;i<docSimMatrix.length;i++){
					matrix[i] = new double[docSimMatrix.length];
					
					for(int j=0;j<docSimMatrix.length;j++){
						matrix[i][j] = docSimMatrix[i][j];
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return matrix;
	}
	
	public static ArrayList<double[]> LoadVectorFromFile(String file){
		ArrayList<double[]> vecs = new ArrayList<double[]>(); 
		
		try{
			
			System.out.println("Start LoadVectorFromFile");
			
			String line;
			
			BufferedReader br = new BufferedReader(new FileReader(file));
	        int i=0;
	        while ((line = br.readLine()) != null) {
	           line = line.trim();
	           if(line.isEmpty()) continue;
	           
	           double row [] = ConvertStringArrayToDoubleArray(line.split("\\s+"));
	           vecs.add(row);
	           
	           System.out.println("Start LoadVectorFromFile="+i++);
	        }
	        br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("End LoadVectorFromFile");
		
		return vecs;
	}
	
}
