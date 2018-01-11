package dal.clustering.document.dataset.googlewebsnippets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import dal.clustering.document.method.stc2.STC2ProcessUtil;
import dal.utils.common.general.UtilsShared;

public class ProcessWebSnippetSTC2 {
	
	GooglewebSnippetUtil googlewebSnippetUtil;
	STC2ProcessUtil sTC2ProcessUtil;
	
	HashMap<String, String> hmLabelIds;
	
	public ProcessWebSnippetSTC2(){
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		sTC2ProcessUtil = new STC2ProcessUtil(GoogleWebSnippetConstant.WebSnippetSTC2VocabIndexIn);
		populateLabelIds();
	}
	
	private void populateLabelIds() {
		hmLabelIds = new LinkedHashMap<String, String>();
		hmLabelIds.put("business", "1");
		hmLabelIds.put("computers", "2");
		hmLabelIds.put("culture-arts-entertainment", "3");
		hmLabelIds.put("education-science", "4");
		hmLabelIds.put("engineering", "5");
		hmLabelIds.put("health", "6");
		hmLabelIds.put("politics-society", "7");
		hmLabelIds.put("sports", "8");
	}

	public void ProcessBySTC2(){
		try{
			
			ArrayList<String[]> aldocsBodeyLabelFlat = googlewebSnippetUtil.getDocsGoogleWebSnippetFlat();
			HashMap<String, Integer> hmVocabIndex = sTC2ProcessUtil.getAlVocabIndex();
			
			ArrayList<String> allLinesBodyWordIndex = new ArrayList<String>();
			ArrayList<String> alLabels = new ArrayList<String>();
			ArrayList<String> alBodies = new ArrayList<String>();
			
			for(String [] bodyLabel: aldocsBodeyLabelFlat){
				String body = bodyLabel[0].trim();
				String label = bodyLabel[1].trim();
				
				if(body.isEmpty() || label.isEmpty()) continue;
				
				String arr [] = body.split("\\s+");
				
				StringBuilder lineBodyWordIndx = new StringBuilder(); 

				for(String word: arr){
					if(hmVocabIndex.containsKey(word)){
						lineBodyWordIndx.append(hmVocabIndex.get(word)+" ");
					}
				}
				
				String bodyIndexLine = lineBodyWordIndx.toString().trim();
				
				if(!bodyIndexLine.isEmpty() && bodyIndexLine.length()>0){
					allLinesBodyWordIndex.add(bodyIndexLine);
					alBodies.add(body);
					alLabels.add(hmLabelIds.get(label));
				}
			}
			
			System.out.println("bodys="+ alBodies.size());
			
			UtilsShared.WriteLinesToFile(GoogleWebSnippetConstant.WebSnippetSTC2TextIndexOut, allLinesBodyWordIndex);
			UtilsShared.WriteLinesToFile(GoogleWebSnippetConstant.WebSnippetSTC2RawOut, alBodies);
			UtilsShared.WriteLinesToFile(GoogleWebSnippetConstant.WebSnippetSTC2GndOut, alLabels);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
