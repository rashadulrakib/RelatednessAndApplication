package dal.clustering.document.dataset.biomedical;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import dal.relatedness.text.compute.w2vec.TextRelatednessW2VecConstant;

public class ProcessBiomedicalData {

	//DocClusterUtil docClusterUtil;
	BioMedicalUtil bioMedicalUtil;
	
	public ProcessBiomedicalData() {
		//docClusterUtil = new DocClusterUtil();
		bioMedicalUtil = new BioMedicalUtil();
	}

	
	public void Process() {
		try{
			//loadAllDocsBiomedicalByW2VecListAndWriteToArff();
			//CombineBioASQData();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void CombineBioASQData() {
		try{
			
			ArrayList<String> words = new ArrayList<String>();
			
			BufferedReader br =  new BufferedReader(new FileReader(BioMedicalConstant.BioMedicalBioASQWord));
			String line = "";
			while((line = br.readLine())!=null){
			   line = line.trim();
			   if(line.isEmpty()) continue;
			   words.add(line);
			}
			br.close();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(BioMedicalConstant.BioMedicalBioASQCombined));
			
			br =  new BufferedReader(new FileReader(BioMedicalConstant.BioMedicalBioASQVector));
			int i=0;
			while((line = br.readLine())!=null){
			   line = line.trim();
			   if(line.isEmpty()) continue;
			   
			   String word = words.get(i);
			   bw.write(word+" "+line+"\n");
			   
			   i++;
			}
			br.close();
			bw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	private void loadAllDocsBiomedicalByW2VecListAndWriteToArff() {
		try{
			List<String[]> aldocsBodeyLabel = new ArrayList<String[]>();
			
			BufferedReader br =  new BufferedReader(new FileReader(BioMedicalConstant.BiomedicalDocsFile));
			HashSet<String> uniquewords = new HashSet<String>();
			
			String line = "";
			while((line = br.readLine())!=null){
			   
			   line = line.trim();
			   if(line.isEmpty()) continue;
			   
			   String [] arrLabelBody = line.split("\\t");
			   
			   if(arrLabelBody.length!=2)
				   continue;
			   
			   String label = arrLabelBody[0].trim();
			   String body =  arrLabelBody[1].trim();
			   
			   body = bioMedicalUtil.docClusterUtil.textUtilShared.PerformPreprocess(body);
		        ArrayList<String> processed = bioMedicalUtil.docClusterUtil.textUtilShared.RemoveStopWord(body);
		        body = bioMedicalUtil.docClusterUtil.textUtilShared.ConvertArrayListToString(processed);
		        
		        if(body.isEmpty()) continue;
			   
		        uniquewords.addAll(processed); //populate the unique words from the text corpus
			   
		        String arr[] = new String[2];
		        arr[0]= body;
		        arr[1] = label;
			   
		        aldocsBodeyLabel.add(arr);
			   
		   }
			   
		   br.close();
		  
			br = new BufferedReader(new FileReader(TextRelatednessW2VecConstant.InputGlobalWordEmbeddingFile));
	           
			String text="";
			HashMap<String, double[]> w2vec = new HashMap<String, double[]>();
			
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	
            	String [] arr = text.split("\\s+");
            	String EmbeddingWord = arr[0];
            	
            	if(uniquewords.contains(EmbeddingWord)){
            		String [] vecs = text.replaceAll(EmbeddingWord, "").trim().split("\\s+");
            		double [] vecDoubles = new double[vecs.length];
            		for(int i=0; i< vecs.length;i++){
            			vecDoubles[i] = Double.parseDouble(vecs[i]);
            		}
            		w2vec.put(EmbeddingWord, vecDoubles);
            	}
            }
           
            br.close();
            
            ArrayList<double []> allavgVecs = new ArrayList<double[]>();
            ArrayList<String> labels = new ArrayList<String>();
            for(String [] bodeyLabel: aldocsBodeyLabel){
            	String body = bodeyLabel[0];
            	String label = bodeyLabel[1];
            	labels.add(label);
            	
            	String arr[] = body.split("\\s+");
            	double [] avgVec = new double[TextRelatednessW2VecConstant.W2VecDimension];
            	
            	for(String word: arr){
            		if(w2vec.containsKey(word)){
            			double[] wordVec = w2vec.get(word); 
            			for(int i=0;i<avgVec.length;i++){
            				avgVec[i]=avgVec[i]+ wordVec[i];
            			}
            		}
            		
            	}
            	
            	//averaging avgvec
            	for(int i=0;i<avgVec.length;i++){
            		avgVec[i]=avgVec[i]/(double)arr.length;
            	}
            	//end averaging avgvec
            	
            	 allavgVecs.add(avgVec);
            }
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(BioMedicalConstant.BiomedicalW2VecArffFile));
            
            bw.write("@relation BiomedicalDocsW2Vec\n\n");
            for(int i=0;i< TextRelatednessW2VecConstant.W2VecDimension;i++){
            	bw.write("@attribute ftr"+i+" NUMERIC\n");
            }
            bw.write("@attribute Category {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20}\n\n");
            bw.write("@data\n");
            
            int index =0;
            for(double[] wordVec : allavgVecs)
            {
            	 for(double val: wordVec){
            		 bw.write(val +",");
            	 }
            	 bw.write(labels.get(index).trim()+"\n");
            	 
            	 index++;
            }
            
            bw.close();
            //end
			
            System.out.println("loadAllDocsBiomedicalByW2VecListAndWriteToArff end");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
