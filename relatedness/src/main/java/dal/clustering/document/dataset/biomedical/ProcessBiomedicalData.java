package dal.clustering.document.dataset.biomedical;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.clustering.document.tools.weka.processing.WekaProcessingUtil;
import dal.relatedness.text.compute.w2vec.TextRelatednessW2VecConstant;

public class ProcessBiomedicalData {

	BioMedicalUtil bioMedicalUtil;
	TfIdfMatrixGenerator tfIdfMatrixGenerator;
	WekaProcessingUtil wekaProcessingUtil;
	
	public ProcessBiomedicalData() {
		bioMedicalUtil = new BioMedicalUtil();
		tfIdfMatrixGenerator = new TfIdfMatrixGenerator();
		wekaProcessingUtil = new WekaProcessingUtil();
	}

	
	public void Process() {
		try{
			//loadAllDocsBiomedicalByW2VecListAndWriteToArff();
			loadAllDocsBiomedicalByW2VecWithLowDocFreAndWriteToArff();
			//CombineBioASQData();
			//ExtrcatMeshTagsWriteToFile();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void ExtrcatMeshTagsWriteToFile() {
		try{
			String file = "C:\\Users\\mona\\Desktop\\BioASQ\\MTI\\MTI-strict-filer-wsd\\text.out.txt";
			
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\mona\\Desktop\\BioASQ\\MTI\\MTI-strict-filer-wsd\\text.processed.txt"));
			
			BufferedReader br =  new BufferedReader(new FileReader(file));
			String line = "";
			int count = 0;

			int ii=0;
			
			ArrayList<String []> bodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
			
			while((line = br.readLine())!=null){
			   line = line.trim().toLowerCase();
			   if(line.isEmpty()) continue;
			   String arr[] = line.split("\\|");
			   
			   System.out.print(count/1000+1);
			   bw.write(Integer.toString(count/1000+1));
			   count++;
			   
			   StringBuilder sb = new StringBuilder();
			   for(int i=1;i<arr.length; i++){
				   String part = arr[i];
				   String mhArr [] = part.split(":");
				   if(mhArr[1].equals("mh")){   
					   sb.append(mhArr[0].toLowerCase()+" ");
				   }
			   }
			   
			   String preproc = bioMedicalUtil.docClusterUtil.textUtilShared.performPreprocess(sb.toString().trim());
			   ArrayList<String> txtAl = bioMedicalUtil.docClusterUtil.textUtilShared.RemoveStopWord(preproc);
			   String txt = bioMedicalUtil.docClusterUtil.textUtilShared.ConvertArrayListToString(txtAl);
			   
			   txt = txt.isEmpty()? bodyLabel.get(ii)[0]: txt.trim();
			   
			   System.out.println(" "+txt);
			   bw.write("\t"+txt+"\n");
			   
			   ii++;
			}
			br.close();
			
			bw.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	private void loadAllDocsBiomedicalByW2VecWithLowDocFreAndWriteToArff() {
		try{
			
			//int maxDocFreqTolerance =1;			
			//HashMap<String, Double> docFreqs = tfIdfMatrixGenerator.CalculateDocFrequency(bioMedicalUtil.getUniqueWords(), bioMedicalUtil.GetBiomedicalDocuments());
			
			ArrayList<String []> alBodyLabel = bioMedicalUtil.getDocsBiomedicalFlat();
			//ArrayList<String []> alBodyLabelPruned = bioMedicalUtil.docClusterUtil
			//		.textUtilShared.FixedPruneWordsByDocFreqs(docFreqs, alBodyLabel, maxDocFreqTolerance);
			HashMap<String, double[]> hmW2Vec = bioMedicalUtil.docClusterUtil.PopulateW2VecBioMedical(bioMedicalUtil.getUniqueWords());			
			ArrayList<InstanceW2Vec> instants = bioMedicalUtil.docClusterUtil.CreateW2VecForTestData(alBodyLabel, hmW2Vec);
			HashMap<String, ArrayList<InstanceW2Vec>> clusterGroupsOriginalLabel = bioMedicalUtil.docClusterUtil.GetClusterGroupsVectorByLabel(instants, true);
			
			//System.out.println("Original body label="+alBodyLabel.size()+", pruned="+alBodyLabelPruned.size());
			
			wekaProcessingUtil.WriteW2VecsToArffFile(BioMedicalConstant.BiomedicalW2VecArffFile,
					BioMedicalConstant.BioASQ2018W2VecDimension, clusterGroupsOriginalLabel.keySet(), instants);
			
		}catch(Exception e){
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
			   
			   String [] arrLabelBody = line.split("\\t+");
			   
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
