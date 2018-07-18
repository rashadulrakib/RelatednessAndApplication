package dal.clustering.document.dataset.googlewebsnippets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import dal.clustering.document.shared.DocClusterUtil;
import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.relatedness.text.compute.w2vec.TextRelatednessW2VecConstant;

public class ProcessGoogleWebSnippet {
	
	DocClusterUtil docClusterUtil;
	TfIdfMatrixGenerator tfIdfMatrixGenerator;
	
	public ProcessGoogleWebSnippet(){
		docClusterUtil = new DocClusterUtil();
		tfIdfMatrixGenerator = new TfIdfMatrixGenerator();
	}
	
	 public void Process() {
			try{
				
//				LinkedHashMap<String, ArrayList<String>>  docsLabelBody  = loadAllDocsGoogleWebSnippet ();
//				LinkedHashMap<String, ArrayList<String>> docsLabelBodyStemmed  = StemDocs(docsLabelBody);
				
				//one block
				ArrayList<String[]> aldocsBodeyLabel = loadAllDocsGoogleWebSnippetList();
				WriteToArffFile(aldocsBodeyLabel);
				//end
				
				//two block
				loadAllDocsGoogleWebSnippetByW2VecListAndWriteToArff();
				//end
				
				//two block
				//loadAllDocsGoogleWebSnippetByW2VecListAndPruneByDFAndWriteToArff();
				//end
				
				
				//3rd block
				//new KMeansClustering(DocClusterConstant.GoogleWebSnippetDocsFile+".arff", 8).ClusterAndEvaluate();
				//end 3rd block
				 
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
	 }

	private void loadAllDocsGoogleWebSnippetByW2VecListAndPruneByDFAndWriteToArff() {
		try{
			ArrayList<String[]> aldocsBodeyLabel = new ArrayList<String[]>();
			
			BufferedReader br =  new BufferedReader(new FileReader(GoogleWebSnippetConstant.GoogleWebSnippetDocsFile));
			
			//HashSet<String> uniquewords = new HashSet<String>();
			
			//extract doc and label 
			String line="";
			while((line=br.readLine()) != null) {
		        line = line.trim().toLowerCase();
		        if(line.isEmpty()) continue;
		        
		        int lastSpaceIndex = line.lastIndexOf(" ");
		        String label = line.substring(lastSpaceIndex);
		        
		        String body = line.substring(0, lastSpaceIndex);
		        
		        
		        body = docClusterUtil.textUtilShared.PerformPreprocess(body);
		        ArrayList<String> processed = docClusterUtil.textUtilShared.RemoveStopWord(body);
		        body = docClusterUtil.textUtilShared.ConvertArrayListToString(processed);
		        
		        if(body.isEmpty()) continue;
		        //uniquewords.addAll(processed); //populate the unique words from the text corpus
		        
		        String arr[] = new String[2];
		        arr[0]= body;
		        arr[1] = label;
		        
		        aldocsBodeyLabel.add(arr);
			}
			
			br.close();
			
			//long seed = System.nanoTime();
			//Collections.shuffle(aldocsBodeyLabel, new Random(seed));
			
			//perform feature extraction based on document frequency
			List<List<String>> docs = new ArrayList<List<String>>();
			Set<String> uniqueterms = new HashSet<String>();
			for(String[] bodyLabel: aldocsBodeyLabel){
				List<String> words = Arrays.asList(bodyLabel[0].split("\\s+"));
				docs.add(words);
				uniqueterms.addAll(words);
			}
			HashMap<String, Integer> docFreqs = tfIdfMatrixGenerator.CalculateDocFrequency(uniqueterms, docs);
			double [] minMaxDocFreq = docClusterUtil.FindMinMaxDocFrequency(docFreqs, docs.size());
			
			//sub: take only the words in a sentence whose doc freqs are within maxMin Doc Freq.
			double minDocFreq = minMaxDocFreq[0];
			double maxDocFreq = minMaxDocFreq[1];
			
			ArrayList<String[]> aldocsBodeyLabelNew = new ArrayList<String[]>();
			
			HashSet<String> uniquewords = new HashSet<String>();
			
			for(String[] bodyLabel: aldocsBodeyLabel){
				String body = bodyLabel[0];
				String label = bodyLabel[1];
				
				String [] bodyWords = body.split("\\s+");
				
				StringBuilder newBodysb = new StringBuilder();
				
				for(String bodyWord: bodyWords){
					if(docFreqs.get(bodyWord)>=minDocFreq && docFreqs.get(bodyWord)<=maxDocFreq){
						newBodysb.append(bodyWord+" ");
						uniquewords.add(bodyWord);
					}
				}
				
				String newBody = newBodysb.toString().trim();
				String[] newBodyLabel = new String[2];
				newBodyLabel[0] = newBody;
				newBodyLabel[1] = label;
				
				aldocsBodeyLabelNew.add(newBodyLabel);
			}
			//end sub:
			//end perform feature extraction based on document frequency
			
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
            
            
          ///create vectors for each text
            ArrayList<double []> allavgVecs = new ArrayList<double[]>();
            ArrayList<String> labels = new ArrayList<String>();
            for(String [] bodeyLabel: aldocsBodeyLabelNew){
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
            
            //end create vectors for each text
            
            
          //create arff
            BufferedWriter bw = new BufferedWriter(new FileWriter(GoogleWebSnippetConstant.GoogleWebSnippetW2VecArffFileFilteredDF));
            
            bw.write("@relation GoogleWebSnippetDocsW2Vec\n\n");
            for(int i=0;i< TextRelatednessW2VecConstant.W2VecDimension;i++){
            	bw.write("@attribute ftr"+i+" NUMERIC\n");
            }
            bw.write("@attribute Category {business,computers, cultureartsentertainment, educationscience, engineering, health, politicssociety, sports}\n\n");
            bw.write("@data\n");
            
            int index =0;
            for(double[] wordVec : allavgVecs){
            	 for(double val: wordVec){
            		 bw.write(val +",");
            	 }
            	 bw.write(labels.get(index).trim()+"\n");
            	 
            	 index++;
            }
            
            bw.close();
            //end
			
            System.out.println("loadAllDocsGoogleWebSnippetByW2VecListAndPruneByDFAndWriteToArff end");

		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void loadAllDocsGoogleWebSnippetByW2VecListAndWriteToArff() {
		ArrayList<String[]> aldocsBodeyLabel = new ArrayList<String[]>();

		try{
			BufferedReader br =  new BufferedReader(new FileReader(GoogleWebSnippetConstant.GoogleWebSnippetDocsFile));
			
			HashSet<String> uniquewords = new HashSet<String>();
			
			//extract doc and label 
			String line="";
			while((line=br.readLine()) != null) {
				 line = line.trim().toLowerCase();
			        if(line.isEmpty()) continue;
			        
//			        int lastSpaceIndex = line.lastIndexOf(" ");
//			        String label = line.substring(lastSpaceIndex).trim();
//			        
//			        String body = line.substring(0, lastSpaceIndex).trim();
			        
			        String [] arrLabelBody = line.split("\\t");
					   
				   if(arrLabelBody.length!=2)
					   continue;
				   
				   String label = arrLabelBody[0].trim();
				   String body =  arrLabelBody[1].trim();
			        
			        body = docClusterUtil.textUtilShared.PerformPreprocess(body);
			        ArrayList<String> processed = docClusterUtil.textUtilShared.RemoveStopWord(body);
			        body = docClusterUtil.textUtilShared.ConvertArrayListToString(processed);
			        
			        if(body.isEmpty()) continue;
		        
		        uniquewords.addAll(processed); //populate the unique words from the text corpus
		        
		        String arr[] = new String[2];
		        arr[0]= body;
		        arr[1] = label;
		        
		        aldocsBodeyLabel.add(arr);
			}
			
			br.close();
			
			
			//long seed = System.nanoTime();
			//Collections.shuffle(aldocsBodeyLabel, new Random(seed));
			
			//end  extract doc and label //end  extract doc and label 
			
			//extract  vectors for each unique words of the text file
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
            
            //end extract  vectors for each unique words of the text file
			
			///create vectors for each text
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
            
            //end create vectors for each text
            
            
            //create arff
            BufferedWriter bw = new BufferedWriter(new FileWriter(GoogleWebSnippetConstant.GoogleWebSnippetW2VecArffFileWhole));
            
            bw.write("@relation GoogleWebSnippetDocsW2Vec\n\n");
            for(int i=0;i< TextRelatednessW2VecConstant.W2VecDimension;i++){
            	bw.write("@attribute ftr"+i+" NUMERIC\n");
            }
            bw.write("@attribute Category {business,computers, culture-arts-entertainment, education-science, engineering, health, politics-society, sports}\n\n");
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
			
            System.out.println("loadAllDocsGoogleWebSnippetByW2VecListAndWriteToArff end");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void WriteToArffFile(ArrayList<String[]> aldocsBodeyLabel) {
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(GoogleWebSnippetConstant.GoogleWebSnippetDocsFile+".arff"));
			
			bw.write("@relation GoogleWebSnippetDocs"+"\n\n");
			bw.write("@attribute Text string"+"\n");
			bw.write("@attribute Category {business,computers, cultureartsentertainment, educationscience, engineering, health, politicssociety, sports}"+"\n\n");
			
			bw.write("@data"+"\n");
			
			for(String[] bodyLabel: aldocsBodeyLabel ){
				bw.write("'"+bodyLabel[0]+"',"+bodyLabel[1]+"\n");
			}
			
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private ArrayList<String[]> loadAllDocsGoogleWebSnippetList() {
		ArrayList<String[]> aldocsBodeyLabel = new ArrayList<String[]>();
		try{
			
			BufferedReader br =  new BufferedReader(new FileReader(GoogleWebSnippetConstant.GoogleWebSnippetDocsFile));
			
			String line="";
			while((line=br.readLine()) != null) {
		        line = line.trim().toLowerCase();
		        if(line.isEmpty()) continue;
		        
//		        String arr[] = line.split("\\s+");
//		        String label = arr[arr.length-1];
		        
		        int lastSpaceIndex = line.lastIndexOf(" ");
		        String label = line.substring(lastSpaceIndex);
		        
		        String body = line.substring(0, lastSpaceIndex);
		        
		        String arr[] = new String[2];
		        arr[0]=body;
		        arr[1] = label;
		        
		        aldocsBodeyLabel.add(arr);
			}
			
			br.close();
			
			//long seed = System.nanoTime();
			//Collections.shuffle(aldocsBodeyLabel, new Random(seed));
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return aldocsBodeyLabel;
	}

	private LinkedHashMap<String, ArrayList<String>> StemDocs(	LinkedHashMap<String, ArrayList<String>> docsLabelBody) {
		LinkedHashMap<String, ArrayList<String>>  docsLabelBodyStem = new LinkedHashMap<String, ArrayList<String>>();
		try{
			for(String label: docsLabelBody.keySet()){
				ArrayList<String> docs = new ArrayList<String>();
				System.out.println(label+","+docsLabelBody.get(label).size());
				for(String doc: docsLabelBody.get(label)){
					String stemmed = docClusterUtil.textUtilShared.StemByEachWord(doc);
					docs.add(docClusterUtil.textUtilShared.StemByEachWord(doc));
					
					//System.out.println("orignal="+doc+", stem="+ stemmed);
				}
				docsLabelBodyStem.put(label, docs);
			}
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return docsLabelBodyStem;
	}
}
