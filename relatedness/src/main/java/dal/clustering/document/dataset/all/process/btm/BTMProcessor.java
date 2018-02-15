package dal.clustering.document.dataset.all.process.btm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import dal.clustering.document.dataset.agnews.AgNewsConstant;
import dal.clustering.document.dataset.agnews.AgNewsUtil;
import dal.clustering.document.dataset.biomedical.BioMedicalConstant;
import dal.clustering.document.dataset.biomedical.BioMedicalUtil;
import dal.clustering.document.dataset.googlewebsnippets.GoogleWebSnippetConstant;
import dal.clustering.document.dataset.googlewebsnippets.GooglewebSnippetUtil;
import dal.clustering.document.dataset.stackoverflow.StackOverflowConstant;
import dal.clustering.document.dataset.stackoverflow.StackOverflowUtil;
import dal.utils.common.general.UtilsShared;

public class BTMProcessor {
	AgNewsUtil agNewsUtil;
	BioMedicalUtil bioMedicalUtil;
	GooglewebSnippetUtil googlewebSnippetUtil;
	StackOverflowUtil stackOverflowUtil;
	
	public BTMProcessor(){
		agNewsUtil = new AgNewsUtil();
		bioMedicalUtil = new BioMedicalUtil();
		googlewebSnippetUtil = new GooglewebSnippetUtil();
		stackOverflowUtil = new StackOverflowUtil();
	}
	
	public void WriteBodies(){
		try{
			
			UtilsShared.WriteLinesToFile(GoogleWebSnippetConstant.WebSnippetBodies, googlewebSnippetUtil.GetBodies());
			UtilsShared.WriteLinesToFile(AgNewsConstant.AgNewsBodies, agNewsUtil.GetBodies());
			UtilsShared.WriteLinesToFile(StackOverflowConstant.StackOverflowBodies, stackOverflowUtil.GetBodies());
			UtilsShared.WriteLinesToFile(BioMedicalConstant.BioMedicalBodies, bioMedicalUtil.GetBodies());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void LabelInstancesByProbabilityOfTopicGivenDocument() {
		try{
			String pTByDocFile = "D:\\PhD\\dr.norbert\\BTM-master\\output\\model\\k8.pz_d";
			ArrayList<Integer> predLabels = new ArrayList<Integer>();
			
			BufferedReader br =  new BufferedReader(new FileReader(pTByDocFile));
			
			String line="";
			while((line=br.readLine()) != null) {
				line = line.trim();
		        if(line.isEmpty()) continue;
		        
		        String arr[] = line.split("\\s+");
		        
		        int maxPtIndex = Integer.MIN_VALUE;
		        double maxVal = Double.MIN_VALUE; 
		        
		        for(int i=0;i<arr.length;i++){
		        	double val = Double.parseDouble(arr[i]);
		        	
		        	if(maxVal<val){
		        		maxVal = val;
		        		maxPtIndex = i;
		        	}
		        }
		        
		        predLabels.add(maxPtIndex);
			}
			
			br.close();
			
			String outFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\websnippet-BTM-labels-2208";
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			
			for(int i=0;i<predLabels.size()-1;i++){
				bw.write(predLabels.get(i)+",");
			}
			bw.write(predLabels.get(predLabels.size()-1)+"\n");
			
			bw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
