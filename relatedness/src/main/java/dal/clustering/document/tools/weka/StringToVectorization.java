package dal.clustering.document.tools.weka;

import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.Rainbow;
import weka.core.tokenizers.NGramTokenizer;
import weka.core.tokenizers.WordTokenizer;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class StringToVectorization {
	NGramTokenizer tokenizer;
	Instances instances;
	
	public StringToVectorization(NGramTokenizer tokenizer,Instances instances){
		this.tokenizer= tokenizer;
		this.instances = instances;
	}
	
	public StringToWordVector convertStringToVector() throws Exception{
		StringToWordVector filter = new StringToWordVector();
		
		WordTokenizer word = new WordTokenizer();
		
		filter.setTokenizer(word);
		filter.setInputFormat(instances);
		filter.setWordsToKeep(1000000000);
		filter.setDoNotOperateOnPerClassBasis(true);
		filter.setLowerCaseTokens(true);
		filter.setIDFTransform(true);
		
//		SelectedTag tag = new SelectedTag(arg0, arg1);
//		
//		filter.setNormalizeDocLength(newType);
		filter.setTFTransform(true);
		filter.setMinTermFreq(10);
	    filter.setPeriodicPruning(-1.0);
		
//		SnowballStemmer snowStem = new SnowballStemmer();
//		filter.setStemmer(snowStem);
	
		Rainbow rainbowStop = new Rainbow();
		filter.setStopwordsHandler(rainbowStop);
		
		//filter.setUseStoplist(true);
		
		return filter;
	}
}
