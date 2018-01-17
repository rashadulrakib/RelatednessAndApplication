package dal.clustering.document.shared;

import dal.relatedness.text.compute.trwp.TextRelatednessTrwp;
import dal.relatedness.text.utils.TextRelatednessGoogleNgUtil;
import dal.utils.common.general.TextUtilShared;

public class DocClusterUtilTrWP {
	
	TextRelatednessTrwp textRelatednessTrwp;
	
	public DocClusterUtilTrWP(DocClusterUtilGTM docClusterUtilGTM, TextUtilShared textUtilShared,
			TextRelatednessGoogleNgUtil textRelatednessGoogleNgUtil){
		textRelatednessTrwp = new TextRelatednessTrwp(docClusterUtilGTM.textRelatednessGtm, textUtilShared, textRelatednessGoogleNgUtil);
	}
	
	public double ComputeTextSimTrWp(String text1, String text2){ 
		double sim=0;
		try{
			sim = textRelatednessTrwp.ComputeTextRelatednessExternal(text1, text2);
					
		}catch(Exception e){
			e.printStackTrace();
		}
		return sim;
	}
}
