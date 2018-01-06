package dal.ngramtool.startup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class NgramApp {

	public static void main(String[] args) {
		
		try {
//			Split4Gram split4Gram = new Split4Gram();
//			split4Gram.SplitByFirst2CharFromFirst2Grams("1234", 33, 130);
//			System.out.println("1234 finished...");
//			split4Gram.SplitByFirst2CharFromFirst2Grams("2314", 26, 130);
//			System.out.println("2314 finished...");
//			split4Gram.SplitByFirst2CharFromFirst2Grams("3412", 27, 130);
//			System.out.println("3412 finished...");
			
			//new UnigramKeyGenerator().GenerateUnigramHashFunction();
			
			//new BigramKeyGenerator().GenerateBigramHashFunction();
			
//			System.out.println("aa");
//			
			Point2D [] A = new Point2D[5];
			
			A[0].x = -1 ;  A[0].y = -2 ;
					  A[1].x =  1;   A[1].y =  2 ;
					  A[2].x =  2 ;  A[2].y =  4 ;
					  A[3].x = -3 ;  A[3].y =  2 ;
					  A[4].x =  2 ;  A[4].y = -2 ;
			
			int out1 = new Solution1().solution(A);
			System.out.println(out1);
			
			
//			int [] A = new int [2];
//			
//			A[0] =  -6;
//					  A[1] = -5;
////					  A[2] =  6;
////					  A[3] =  9;
////					  A[4] =  7;
////					  A[5] =  8;
//			
//			int ou2 = new Solution2().solution(A);
//			System.out.println(ou2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}

class Solution2 {
    public int solution(int[] A) {
        int seqlen = A.length;
        ArrayList<Integer> sorted = new ArrayList<Integer>();
    	try{
        	if(A.length==0) return 0;
        	if(A.length==1) return 1;
    		
    		for(int i=0;i<A.length;i++){
        		sorted.add(A[i]);
        	}
    		
    		Collections.sort(sorted);
        	
        	int min = sorted.get(0);
        	
        	int lenCount = 0;
        	//int index =0;
        	for(int elem: sorted){
        		//if(index==0) continue;
        		int diff = elem-min;
        		if(diff>1){
        			break;
        		}else{
        			lenCount++;
        		}
        		
        		//index ++;		
        	}
        	
        	seqlen = lenCount;
        }catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return seqlen;
    }
}

class Point2D {
	  public int x;
	  public int y;
}

class Solution1 {
    public int solution(Point2D[] A) {
        int rays = 0;
    	try{
        	HashSet<Double> hmSlopeCounts1 = new HashSet<Double>();
        	HashSet<Double> hmSlopeCounts2 = new HashSet<Double>();
        	HashSet<Double> hmSlopeCounts3 = new HashSet<Double>();
        	HashSet<Double> hmSlopeCounts4 = new HashSet<Double>();
        	
        	HashSet<Double> hmSlopeCounts_X0Y1pos = new HashSet<Double>();
        	HashSet<Double> hmSlopeCounts_X0Y1neg = new HashSet<Double>();
        
        	
        	
        	if(A==null) return 0;
        	if(A.length==0) return 0;
        	if(A.length==1) return 1;
        	
        	for(int i=0;i< A.length;i++){
        		
        		double slope = A[i].y/A[i].x;
        		
        		
        		if(A[i].y>0 && A[i].x>0){
        			hmSlopeCounts1.add(slope);
        		}
        		
        		if(A[i].y>0 && A[i].x<0){
        			hmSlopeCounts2.add(slope);
        		}
        		
        		if(A[i].y<0 && A[i].x<0){
        			hmSlopeCounts3.add(slope);
        		}
        		
        		if(A[i].y<0 && A[i].x>0){
        			hmSlopeCounts4.add(slope);
        		}
        		
        		if(A[i].y==0 && (A[i].x>0 || A[i].x<0)){
        			hmSlopeCounts_X0Y1pos.add(slope);
        		}
        		
        		if(A[i].x==0 && (A[i].y>0 || A[i].y<0)){
        			hmSlopeCounts_X0Y1neg.add(slope);
        		}
        	}
        	
        	rays = hmSlopeCounts1.size()+hmSlopeCounts2.size()+hmSlopeCounts3.size()+hmSlopeCounts4.size()
        			+hmSlopeCounts_X0Y1pos.size()+hmSlopeCounts_X0Y1neg.size();
        	
        }catch (Exception e) {
			e.printStackTrace();
		}
    	return rays;
    }
}



