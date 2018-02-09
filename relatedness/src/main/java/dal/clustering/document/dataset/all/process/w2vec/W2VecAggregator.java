package dal.clustering.document.dataset.all.process.w2vec;

import java.util.Set;

import dal.clustering.document.dataset.agnews.AgNewsUtil;
import dal.clustering.document.dataset.biomedical.BioMedicalUtil;
import dal.clustering.document.dataset.googlewebsnippets.GooglewebSnippetUtil;
import dal.clustering.document.dataset.stackoverflow.StackOverflowUtil;

public class W2VecAggregator {
	
	AgNewsUtil agNewsUtil;
	BioMedicalUtil bioMedicalUtil;
	GooglewebSnippetUtil googlewebSnippetUtil;
	StackOverflowUtil stackOverflowUtil;
	
	public W2VecAggregator(){
		agNewsUtil = new AgNewsUtil();
		bioMedicalUtil = new BioMedicalUtil();
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		stackOverflowUtil = new StackOverflowUtil();
	}
	
	public void AggregateW2VecsForDatasets(){
		try{
			Set<String> allUniqueWords = agNewsUtil.getUniqueWords();
			allUniqueWords.addAll(bioMedicalUtil.getUniqueWords());
			allUniqueWords.addAll(googlewebSnippetUtil.GetUniqueWords());
			allUniqueWords.addAll(stackOverflowUtil.getUniqueWords());
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
