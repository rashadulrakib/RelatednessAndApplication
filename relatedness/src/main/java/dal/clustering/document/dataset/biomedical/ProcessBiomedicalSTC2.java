package dal.clustering.document.dataset.biomedical;

import java.util.ArrayList;
import java.util.HashMap;

import dal.clustering.document.method.stc2.STC2ProcessUtil;
import dal.utils.common.general.UtilsShared;

public class ProcessBiomedicalSTC2 {
	BioMedicalUtil bioMedicalUtil;
	STC2ProcessUtil sTC2ProcessUtil;
	
	public ProcessBiomedicalSTC2(){
		bioMedicalUtil = new BioMedicalUtil();
		sTC2ProcessUtil = new STC2ProcessUtil(BioMedicalConstant.BioMedicalSTC2VocabIndexIn);
	}
	
	public void ProcessBySTC2(){
		try{
			
			ArrayList<String[]> aldocsBodeyLabelFlat = bioMedicalUtil.getDocsBiomedicalFlat();
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
			
			UtilsShared.WriteLinesToFile(BioMedicalConstant.BioMedicalSTC2TextIndexOut, allLinesBodyWordIndex);
			UtilsShared.WriteLinesToFile(BioMedicalConstant.BioMedicalSTC2RawOut, alBodies);
			UtilsShared.WriteLinesToFile(BioMedicalConstant.BioMedicalSTC2GndOut, alLabels);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
