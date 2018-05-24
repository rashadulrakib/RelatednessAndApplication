package dal.clustering.document.shared.entities;

import dal.clustering.document.shared.DocClusterUtil;
import dal.relatedness.phrase.compute.tokenized.PhraseRelatednessTokenized;


public class ClusterDocumentProcessingShared {

	public DocClusterUtil docClusterUtil;
	public PhraseRelatednessTokenized phraseRelatednessTokenized ;
	
	public ClusterDocumentProcessingShared(){
		
		docClusterUtil = new DocClusterUtil();
		phraseRelatednessTokenized = new PhraseRelatednessTokenized();
		
	}
}
