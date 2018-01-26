package dal.clustering.document.shared;

import dal.relatedness.text.compute.trwp.TextRelatednessTrwp;
import dal.relatedness.text.utils.TextRelatednessGoogleNgUtil;
import dal.utils.common.general.TextUtilShared;

public class DocClusterUtilTrWP {
	
	public TextRelatednessTrwp textRelatednessTrwp;
	DocClusterUtilGTM docClusterUtilGTM; //no need
	
	public DocClusterUtilTrWP(DocClusterUtilGTM docClusterUtilGTM, TextUtilShared textUtilShared,
			TextRelatednessGoogleNgUtil textRelatednessGoogleNgUtil){
		this.docClusterUtilGTM = docClusterUtilGTM;
		textRelatednessTrwp = new TextRelatednessTrwp(docClusterUtilGTM.textRelatednessGtm, textUtilShared, textRelatednessGoogleNgUtil);
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
}
