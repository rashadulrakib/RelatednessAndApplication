package dal.clustering.document.shared.entities;

import dal.clustering.document.shared.DocClusterUtil;
import dal.relatedness.phrase.compute.tokenized.PhraseRelatednessTokenized;


public class ClusterDocumentShared {

	public DocClusterUtil docClusterUtil;
	public PhraseRelatednessTokenized phraseRelatednessTokenized ;
	
	public ClusterDocumentShared(){
		
		docClusterUtil = new DocClusterUtil();
		
	}
}
