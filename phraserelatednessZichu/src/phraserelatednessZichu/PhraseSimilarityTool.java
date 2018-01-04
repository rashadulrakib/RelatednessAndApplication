package phraserelatednessZichu;

import dal.phSim.context.PhraseSimilarity;
import dal.simMethod.SimMethod;

public class PhraseSimilarityTool {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println("test");
		PhraseSimilarity phSim = new PhraseSimilarity("D:\\Google-n-gram\\n-gram-indexed\\", 
				"D:\\Google-n-gram\\stopwords\\stopWords.txt");
		try {
			double simScore = phSim.getSimilarity("large number", "vast amount", SimMethod.NGD);
			System.out.println("large number, vast amount="+ simScore);
			
			simScore = phSim.getSimilarity("vast amount", "large quantity", SimMethod.NGD);
			System.out.println("vast amount, large quantity="+ simScore);
			
			simScore = phSim.getSimilarity("important part", "significant role", SimMethod.NGD);
			System.out.println("important part, significant role="+ simScore);
			
			simScore = phSim.getSimilarity("certain circumstance", "particular case", SimMethod.NGD);
			System.out.println("certain circumstance, particular case="+ simScore);
			
			simScore = phSim.getSimilarity("general principle", "basic rule", SimMethod.NGD);
			System.out.println("general principle, basic rule="+ simScore);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
