package dal.clustering.document.dataset.stackoverflow;

import java.util.ArrayList;
import java.util.HashMap;

import dal.clustering.document.method.stc2.STC2ProcessUtil;
import dal.utils.common.general.UtilsShared;

public class ProcessStackOverflowSTC2 {
	StackOverflowUtil stackOverflowUtil;
	STC2ProcessUtil sTC2ProcessUtil;
	
	public ProcessStackOverflowSTC2(){
		stackOverflowUtil = new StackOverflowUtil();
		sTC2ProcessUtil = new STC2ProcessUtil(StackOverflowConstant.StackOverflowSTC2VocabIndexIn);
	}
	
	public void ProcessBySTC2(){
		try{
			
			ArrayList<String[]> aldocsBodeyLabelFlat = stackOverflowUtil.getDocsStackOverflowFlat();
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
					alLabels.add(label);
				}
			}
			
			System.out.println("bodys="+ alBodies.size());
			
			UtilsShared.WriteLinesToFile(StackOverflowConstant.StackOverflowSTC2TextIndexOut, allLinesBodyWordIndex);
			UtilsShared.WriteLinesToFile(StackOverflowConstant.StackOverflowSTC2RawOut, alBodies);
			UtilsShared.WriteLinesToFile(StackOverflowConstant.StackOverflowSTC2GndOut, alLabels);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
