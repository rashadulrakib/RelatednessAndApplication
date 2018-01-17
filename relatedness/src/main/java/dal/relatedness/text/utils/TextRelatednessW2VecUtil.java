package dal.relatedness.text.utils;

import java.util.ArrayList;
import java.util.HashMap;

import dal.clustering.document.shared.PairSim;
import dal.utils.common.compute.ComputeUtil;
import dal.utils.common.general.UtilsShared;

public class TextRelatednessW2VecUtil {
	
	HashMap<String, double[]> w2vec;
	HashMap<String, Double> hmWordPairSim;
	
	public TextRelatednessW2VecUtil(HashMap<String, double[]> w2vec){
		this.w2vec = w2vec;
		hmWordPairSim = new HashMap<String, Double>();
	}

	public ArrayList<ArrayList<PairSim>> GetWeightedSimilarityMatrix(
			ArrayList<String> restDoc1, ArrayList<String> restDoc2) {
		
		ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
		
		try{
			for (String word1 : restDoc1) {
				ArrayList<PairSim> simPairList = new ArrayList<PairSim>();
				for (String word2 : restDoc2) {

					String wordpairkey = word1 + "," + word2;

					double wordSim = 0.0;

					if (word1.equals(word2)) {
						wordSim = 1.0;
					} else {
						if(hmWordPairSim.containsKey(wordpairkey)){
							wordSim = hmWordPairSim.get(wordpairkey);
						}
						else if(hmWordPairSim.containsKey(word2+"," +word1)){
							wordSim = hmWordPairSim.get(word2+"," +word1);
						}else{
							if(w2vec.containsKey(word1) && w2vec.containsKey(word2)){
								wordSim = ComputeUtil.ComputeCosineSimilarity(
										UtilsShared.ConvertDoubleArrayToArrayList(w2vec.get(word1)),
										UtilsShared.ConvertDoubleArrayToArrayList(w2vec.get(word2))
										);
								hmWordPairSim.put(wordpairkey, wordSim);
							}
						}
					}

					simPairList.add(new PairSim(wordpairkey, wordSim));
				}
				t1t2simPairList.add(simPairList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return t1t2simPairList;
	}
	
}
