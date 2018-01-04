package dal.ngramtool.startup;

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
			
			System.out.println("aa");
			
			new Solution().solution(new int []{2,3,4,5,4,2,3,3,9,10});
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	

}

class Solution {
    public int solution(int[] A) {
       HashSet<Integer> ints = new HashSet<Integer>();
       int min = Integer.MAX_VALUE;
       int max = Integer.MIN_VALUE;
       for(int i=0; i<A.length;i++){
    	   ints.add(A[i]);
    	   if(min> A[i]){
    		   min = A[i];
    	   }
    	   
    	   if(max<A[i]){
    		   max = A[i];
    	   }
       }
       
       int arrSize = max-min+1;
       int newArr [] = new int [arrSize];
       
       for(int i=0;i<newArr.length;i++){
    	   newArr[i]= Integer.MAX_VALUE;
       }
       
       for(int i=0; i<A.length;i++){
    	   int offset = A[i]-min;
    	   newArr[offset] = A[i];
       }
       
       boolean found = false;
       int smallNumber = 1;
       for(int i=0;i<newArr.length;i++){
    	   if(newArr[i]==Integer.MAX_VALUE){
    		   found = true;
    		   smallNumber = i+min;
    		   break;
    	   }
       }
       
       System.out.println(smallNumber);
       return 0;
    }
}
