package dal.clustering.document.tools.weka;

import weka.core.tokenizers.NGramTokenizer;

public class NGTokenize {
	int ng;
	public NGTokenize(int ng){
		this.ng=ng;
	}
	
	public NGramTokenizer getNGTokenizer(){
		NGramTokenizer tokenizer = new NGramTokenizer();
		tokenizer.setNGramMinSize(ng);
		tokenizer.setNGramMaxSize(ng);
		tokenizer.setDelimiters("\\W");
		return tokenizer;
	}
}
