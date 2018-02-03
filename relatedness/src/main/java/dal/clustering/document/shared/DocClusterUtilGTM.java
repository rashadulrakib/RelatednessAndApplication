package dal.clustering.document.shared;

import java.io.IOException;

import org.textsim.textrt.proc.singlethread.TextInstance;

import dal.relatedness.text.compute.gtm.TextRelatednessGtm;


public class DocClusterUtilGTM {
	
	public TextRelatednessGtm textRelatednessGtm;
	
	public DocClusterUtilGTM() throws IOException{
		textRelatednessGtm = new TextRelatednessGtm();
	}
	
	public double ComputeWordSimGTM(String w1, String w2){
		double sim = 0;
		try{
			sim = textRelatednessGtm.ComputeWordSimGTM(w1, w2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sim;
	}

	public double ComputeTextSimGTM(String text1, String text2) {
		double sim = 0;
		try{
			sim = textRelatednessGtm.ComputeTextSimGTM(text1, text2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sim;
	}
	
	public double ComputeTextSimGTM(TextInstance text1, TextInstance text2) {
		double sim = 0;
		try{
			sim = textRelatednessGtm.ComputeTextSimGTM(text1, text2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sim;
	}
	
}
