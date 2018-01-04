package dal.clustering.document.shared;

import java.io.IOException;

import org.textsim.textrt.preproc.SinglethreadTextrtPreprocessor;
import org.textsim.textrt.preproc.tokenizer.PennTreeBankTokenizer;
import org.textsim.textrt.proc.singlethread.SinglethreadTextRtProcessor;
import org.textsim.util.token.DefaultTokenFilter;

public class DocClusterUtilText {
	
	SinglethreadTextrtPreprocessor tpp = null;
	SinglethreadTextRtProcessor tp = null;
	
	public DocClusterUtilText() throws IOException{
		 	tpp = new SinglethreadTextrtPreprocessor(
			        DocClusterConstant.GTM_UNIGRAM_BIN_PATH,
			        null,
			        null,
			        new PennTreeBankTokenizer(),
			        new DefaultTokenFilter()
			        );
		 	tp = new SinglethreadTextRtProcessor(DocClusterConstant.GTM_TRIGRAM_BIN_PATH);
	}

	public double ComputeTextSimGTM(String centerText, String body) {
		double sim = 0;
		try{
			
//	    DefaultWordRtProcessor wp = new DefaultWordRtProcessor(
//	        Paths.get(DocClusterConstant.GTM_TRIGRAM_BIN_PATH)
//	        );


//	    String w1 = "apple";
//	    String w2 = "orange";
//	    System.out.println(String.format("word similarity between \"%s\" and \"%s\": %f",
//	        w1, w2, wp.sim(tpp.getWordID(w1), tpp.getWordID(w2))
//	        ));
	    
//	    String t1 = "manufacture manufacturer directory directory china taiwan products manufacturers directory- taiwan china products manufacturer direcory exporter directory supplier directory suppliers";
//	    String t2 = "empmag electronics manufacturing procurement homepage electronics manufacturing procurement magazine procrement power products production essentials data management";
//	    System.out.println(String.format("text similarity between \"%s\" and \"%s\": %f",
//	        t1, t2, tp.computeTextRT(tpp.createSingleTextInstance(t1), tpp.createSingleTextInstance(t2))
//	        ));
	    
	    sim = tp.computeTextRT(tpp.createSingleTextInstance(centerText), tpp.createSingleTextInstance(body)); 
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sim;
	}
	
}
