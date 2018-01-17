package dal.relatedness.text.compute.gtm;

import java.io.IOException;
import java.nio.file.Paths;

import org.textsim.textrt.preproc.SinglethreadTextrtPreprocessor;
import org.textsim.textrt.preproc.tokenizer.PennTreeBankTokenizer;
import org.textsim.textrt.proc.singlethread.SinglethreadTextRtProcessor;
import org.textsim.util.token.DefaultTokenFilter;
import org.textsim.wordrt.proc.DefaultWordRtProcessor;

public class TextRelatednessGtm {

	SinglethreadTextrtPreprocessor tpp = null;
	SinglethreadTextRtProcessor tp = null;
	
	DefaultWordRtProcessor wp = null;
	
	public TextRelatednessGtm() throws IOException{
		tpp = new SinglethreadTextrtPreprocessor(
				TextRelatednessGtmConstant.GTM_UNIGRAM_BIN_PATH,
		        null,
		        null,
		        new PennTreeBankTokenizer(),
		        new DefaultTokenFilter()
		        );
	 	tp = new SinglethreadTextRtProcessor(TextRelatednessGtmConstant.GTM_TRIGRAM_BIN_PATH);
	 	
	    wp = new DefaultWordRtProcessor(
	    		Paths.get(TextRelatednessGtmConstant.GTM_TRIGRAM_BIN_PATH)
        );
	}
	
	public double ComputeWordSimGTM(String w1, String w2){
		double sim = 0;
		try{
			sim = wp.sim(tpp.getWordID(w1), tpp.getWordID(w2));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sim;
	}

	public double ComputeTextSimGTM(String text1, String text2) {
		double sim = 0;
		try{
			
			text1 = text1.trim();
			text2 = text2.trim();
			
			if(text1.isEmpty() || text2.isEmpty()) return 0;
	    
			sim = tp.computeTextRT(tpp.createSingleTextInstance(text1), tpp.createSingleTextInstance(text2)); 
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sim;
	}
}
