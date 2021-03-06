package dal.clustering.document.dataset.all.process.btm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import dal.clustering.document.dataset.agnews.AgNewsUtil;
import dal.clustering.document.dataset.biomedical.BioMedicalUtil;
import dal.clustering.document.dataset.googlewebsnippets.GoogleWebSnippetConstant;
import dal.clustering.document.dataset.googlewebsnippets.GooglewebSnippetUtil;
import dal.clustering.document.dataset.stackoverflow.StackOverflowUtil;
import dal.utils.common.general.UtilsShared;

public class BTMProcessor {
	AgNewsUtil agNewsUtil;
	BioMedicalUtil bioMedicalUtil;
	GooglewebSnippetUtil googlewebSnippetUtil;
	StackOverflowUtil stackOverflowUtil;
	
	public BTMProcessor(){
		//agNewsUtil = new AgNewsUtil();
//		bioMedicalUtil = new BioMedicalUtil();
		//googlewebSnippetUtil = new GooglewebSnippetUtil();
//		stackOverflowUtil = new StackOverflowUtil();
	}
	
	public void WriteBodies(){
		try{
			
			UtilsShared.WriteLinesToFile(GoogleWebSnippetConstant.WebSnippetBodies, googlewebSnippetUtil.GetBodies());
//			UtilsShared.WriteLinesToFile(AgNewsConstant.AgNewsBodies, agNewsUtil.GetBodies());
//			UtilsShared.WriteLinesToFile(StackOverflowConstant.StackOverflowBodies, stackOverflowUtil.GetBodies());
//			UtilsShared.WriteLinesToFile(BioMedicalConstant.BioMedicalBodies, bioMedicalUtil.GetBodies());
//			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void LabelInstancesByProbabilityOfTopicGivenDocument() {
		try{
			
			for(int i=1;i<=1;i++){
				String pTByDocFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\btm-vec-2280-72-alpha-0.6-beta-"+i;
				//String pTByDocFile = "/users/grad/rakib/dr.norbert/BTM-master/output/model/k8.pz_d_"+i;
				ArrayList<Integer> predLabels = new ArrayList<Integer>();
				System.out.println(pTByDocFile);
				BufferedReader br =  new BufferedReader(new FileReader(pTByDocFile));
				
				String line="";
				while((line=br.readLine()) != null) {
					line = line.trim();
			        if(line.isEmpty()) continue;
			        
			        String arr[] = line.split("\\s+");
			        
			        int maxPtIndex = Integer.MIN_VALUE;
			        double maxVal = Double.MIN_VALUE; 
			        
			        for(int j=0;j<arr.length;j++){
			        	double val = Double.parseDouble(arr[j]);
			        	
			        	if(maxVal<val){
			        		maxVal = val;
			        		maxPtIndex = j;
			        	}
			        }
			        
			        predLabels.add(maxPtIndex);
				}
				
				br.close();
				
				String outFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\btm-vec-2280-72-alpha-0.6-beta-label-"+i;
				//String outFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/data-web-snippets/BTM/labels/websnippet-BTM-labels-2280-"+i;
				BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
				
				for(int j=0;j<predLabels.size()-1;j++){
					bw.write(predLabels.get(j)+",");
				}
				bw.write(predLabels.get(predLabels.size()-1)+"\n");
				
				bw.close();
			}
			
			System.out.println("LabelInstancesByProbabilityOfTopicGivenDocument");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
