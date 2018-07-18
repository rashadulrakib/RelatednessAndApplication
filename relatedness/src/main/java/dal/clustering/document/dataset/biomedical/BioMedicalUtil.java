package dal.clustering.document.dataset.biomedical;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import dal.clustering.document.shared.Doc2VecUtil;
import dal.clustering.document.shared.DocClusterConstant;
import dal.clustering.document.shared.DocClusterUtil;

public class BioMedicalUtil {
	public Doc2VecUtil doc2VecUtil; 
	public DocClusterUtil docClusterUtil;
	HashSet<String> uniqueWords;
	HashSet<String> uniqueWordsStemmed;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	ArrayList<String[]> aldocsBodeyLabelFlatStemmed;
	ArrayList<ArrayList<String[]>> aldocsBodeyLabelFlatList;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	ArrayList<String> alBodies;
	List<List<String>> documents;
	List<List<String>> stemmedDocuments;
	List<Set<String>> stemmedDocumnetsUniqueTerms;
	HashSet<String> keptFtrs;
	
	public BioMedicalUtil(){
		doc2VecUtil = new Doc2VecUtil();
		docClusterUtil = new DocClusterUtil();
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		aldocsBodeyLabelFlatStemmed = new ArrayList<String[]>();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		uniqueWords = new HashSet<String>();
		uniqueWordsStemmed = new HashSet<String>();
		alBodies = new ArrayList<String>();
		documents = new ArrayList<List<String>>();
		stemmedDocuments = new ArrayList<List<String>>();
		stemmedDocumnetsUniqueTerms = new ArrayList<Set<String>>();
		aldocsBodeyLabelFlatList = new ArrayList<ArrayList<String[]>>();
		keptFtrs = new HashSet<String>();
		
		loadAllDocsBiomedical();
		
		//loadKeptFtrList();
		
		//PopulateNFoldData();
	}
	
	public List<List<String>> GetBiomedicalDocuments() {
		return documents;
	}
	
	public List<List<String>> GetBiomedicalStemmedDocuments() {
		return stemmedDocuments;
	}
	
	public List<Set<String>> GetBiomedicalStemmedDocumnetsUniqueTerms() {
		return stemmedDocumnetsUniqueTerms;
	}
	
	public ArrayList<String> GetBodies(){
		return alBodies;
	}
	
	public HashSet<String> getUniqueWords(){
		return uniqueWords;
	}
	
	public HashSet<String> GetUniqueWordsStemmed(){
		return uniqueWordsStemmed;
	}
	
	public ArrayList<String[]> getDocsBiomedicalFlat(){
		return aldocsBodeyLabelFlat;
	}
	
	public ArrayList<String[]> GetDocsBiomedicalFlatStemmed(){
		return aldocsBodeyLabelFlatStemmed;
	} 
	
	public ArrayList<ArrayList<String[]>> GetDocsBiomedicalFlatList() {
		return aldocsBodeyLabelFlatList;
	}
	
	public LinkedHashMap<String, ArrayList<String>> getDocsBiomedicalList(){
		return docsLabelBodyList;
	}
	
	public HashSet<String> GetKeptFtrList(){
		return keptFtrs;
	}
		
	private void loadAllDocsBiomedical() {
		try{
			
			HashMap<String, Integer> uniqueTexts = new LinkedHashMap<String, Integer>();
			
			BufferedReader br =  new BufferedReader(new FileReader(BioMedicalConstant.BiomedicalDocsFile));
			
			String line = "";
			while((line = br.readLine())!=null){
			   
			   line = line.trim();
			   if(line.isEmpty()) continue;
			   
			   String [] arrLabelBody = line.split("\\t");
			   
			   if(arrLabelBody.length!=2)
				   continue;
			   
			   String label = arrLabelBody[0].trim();
			   String body =  arrLabelBody[1].trim();
			   
			    body = docClusterUtil.textUtilShared.PerformPreprocess(body);
		        ArrayList<String> processed = docClusterUtil.textUtilShared.RemoveStopWord(body);
		        body = docClusterUtil.textUtilShared.ConvertArrayListToString(processed);
		        ArrayList<String> processedStemmed = docClusterUtil.textUtilShared.StemByEachWord(processed);
		        String bodyStemmed = docClusterUtil.textUtilShared.ConvertArrayListToString(processedStemmed);
		       
		        if(body.isEmpty() || bodyStemmed.isEmpty()) continue;
		       
		        //ArrayList<String> processed = new ArrayList<String>(Arrays.asList(body.split("\\s+")));
		        
		        if(!uniqueTexts.containsKey(body)){
		        	uniqueTexts.put(body, 1);
		        }else{
		        	uniqueTexts.put(body, uniqueTexts.get(body)+1);
		        }
		        
		        alBodies.add(body);
		        
		        uniqueWords.addAll(processed);
		        uniqueWordsStemmed.addAll(processedStemmed);
		        
		        documents.add(processed);
		        stemmedDocuments.add(processedStemmed);
		        HashSet<String> uniqueDocTems = docClusterUtil.textUtilShared.ConvertStringListToHashSetStemming(processed);
		        stemmedDocumnetsUniqueTerms.add(uniqueDocTems);
		        
		        String arr[] = new String[2];
		        arr[0]= body;
		        arr[1] = label;
		        
		        String arrStemmed[] = new String[2];
		        arrStemmed[0] = bodyStemmed;
		        arrStemmed[1] = label;
			    
		        aldocsBodeyLabelFlat.add(arr);
		        aldocsBodeyLabelFlatStemmed.add(arrStemmed);
		        
		        if(!docsLabelBodyList.containsKey(label)){
		        	ArrayList<String> bodies = new ArrayList<String>();
		        	bodies.add(body);
		        	docsLabelBodyList.put(label,bodies );
		        }
		        else{
		        	ArrayList<String> bodies = docsLabelBodyList.get(label);
		        	bodies.add(body);
		        	docsLabelBodyList.put(label,bodies );
		        }
		   }
			   
		   br.close();
		   
		   for(String key: docsLabelBodyList.keySet()){
				System.out.println(key+","+docsLabelBodyList.get(key).size()+", uniquesize="+uniqueTexts.size());
		   }
		   
		   for(String key: uniqueTexts.keySet()){
			   if(uniqueTexts.get(key)>1){
				   System.out.println(key+","+uniqueTexts.get(key));
			   }
		   }
		   
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void loadKeptFtrList() {
		try{		
			BufferedReader br =  new BufferedReader(new FileReader(BioMedicalConstant.KeptFtrsFile));
			
			String line = "";
			while((line = br.readLine())!=null){
			   
			   line = line.trim().toLowerCase();
			   if(line.isEmpty()) continue;
			   
			   keptFtrs.add(line);
			}
			
			br.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void PopulateNFoldData() {
		try{
			ArrayList<String[]> alNew = new ArrayList<String[]>(aldocsBodeyLabelFlat);
			Collections.shuffle(alNew, new Random(DocClusterConstant.DataRandConstant));
			
			int itemsPerFold = alNew.size()/DocClusterConstant.DataFold;
			
			for(int i=0;i<DocClusterConstant.DataFold;i++){
				int foldStartIndex = i*itemsPerFold;
				int foldEndIndex = foldStartIndex + itemsPerFold;
				
				ArrayList<String[]> alFold = new ArrayList<String[]>();
				
				//1/10 th items
				for(int j=foldStartIndex; j<foldEndIndex;j++){
					alFold.add(alNew.get(j));
				}
				//9/10 th items
//				for(int j=0; j<foldStartIndex;j++){
//					alFold.add(alNew.get(j));
//				}
//				
//				for(int j=foldEndIndex; j<alNew.size();j++){
//					alFold.add(alNew.get(j));
//				}
				
				aldocsBodeyLabelFlatList.add(alFold);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
