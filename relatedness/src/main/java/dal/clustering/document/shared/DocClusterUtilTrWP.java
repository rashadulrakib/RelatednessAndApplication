package dal.clustering.document.shared;

import java.util.ArrayList;

import org.textsim.textrt.proc.singlethread.TextInstance;

import dal.relatedness.text.compute.trwp.TextRelatednessTrwp;
import dal.relatedness.text.utils.TextRelatednessGoogleNgUtil;
import dal.utils.common.general.TextUtilShared;

public class DocClusterUtilTrWP {
	
	public TextRelatednessTrwp textRelatednessTrwp;
	public DocClusterUtilGTM docClusterUtilGTM; //no need
	
	public DocClusterUtilTrWP(DocClusterUtilGTM docClusterUtilGTM, TextUtilShared textUtilShared,
			TextRelatednessGoogleNgUtil textRelatednessGoogleNgUtil){
		this.docClusterUtilGTM = docClusterUtilGTM;
		//textRelatednessTrwp = new TextRelatednessTrwp(docClusterUtilGTM.textRelatednessGtm, textUtilShared, textRelatednessGoogleNgUtil);
	}
	
	public double ComputeTextSimTrWp(String text1, String text2){ 
		double sim=0;
		try{
			//sim = textRelatednessTrwp.ComputeTextRelatednessExternalTrwp(text1, text2);
			sim = docClusterUtilGTM.ComputeTextSimGTM(text1, text2);
					
		}catch(Exception e){
			e.printStackTrace();
		}
		return sim;
	}
	
	public double ComputeTextSimTrWp(TextInstance text1, TextInstance text2){ 
		double sim=0;
		try{
			//sim = textRelatednessTrwp.ComputeTextRelatednessExternalTrwp(text1, text2);
			sim = docClusterUtilGTM.ComputeTextSimGTM(text1, text2);
					
		}catch(Exception e){
			e.printStackTrace();
		}
		return sim;
	}

	public double ComputeTextSimTrWp(ArrayList<String> wordsPhs1, ArrayList<String> wordsPhs2) {
		double sim=0;
		
		try{
			sim = textRelatednessTrwp.ComputeTextRelatednessExternal(wordsPhs1, wordsPhs2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sim;
	}
}
