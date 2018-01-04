package dal.relatedness.text.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class TextExtractorUtil {
	public  static ArrayList<String[]> extractSentencePairsFromSemEval2015() {

		ArrayList<String[]> sentencePairs = new ArrayList<String[]>();
		// String dataSetFile =
		// "D:\\Dal\\ComputationalIntelligence\\Dataset\\Sem-15\\input-answers-forums-15.txt";
		String dataSetFile = "/users/grad/rakib/ComputationalIntelligence/dataset/Sem-15/input-answers-forums-15.txt";

		try {
			BufferedReader br = new BufferedReader(new FileReader(dataSetFile));

			String line1;
			while ((line1 = br.readLine()) != null) {
				line1 = line1.trim();
				String line2 = br.readLine().trim();
				sentencePairs.add(new String[] { line1, line2 });
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sentencePairs;
	}
}
