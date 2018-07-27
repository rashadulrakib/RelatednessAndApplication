package dal.clustering.document.dataset.agnews;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import dal.clustering.document.shared.DocClusterConstant;
import dal.clustering.document.shared.DocClusterUtil;

public class AgNewsUtil {
	
	HashSet<String> uniqueWords;
	HashSet<String> uniqueWordsTitle;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	ArrayList<String[]> aldocsBodyTitleLabelFlat;
	ArrayList<ArrayList<String[]>> aldocsBodeyLabelFlatList;
	List<List<String>> documents;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	public DocClusterUtil docClusterUtil;
	ArrayList<String> alBodies;
	
	public AgNewsUtil(){
		
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		uniqueWords = new HashSet<String>();
		documents = new ArrayList<List<String>>();
		docClusterUtil = new DocClusterUtil();
		alBodies = new ArrayList<String>();
		aldocsBodeyLabelFlatList = new ArrayList<ArrayList<String[]>>();
		aldocsBodyTitleLabelFlat = new ArrayList<String[]>();
		uniqueWordsTitle = new HashSet<String>();
		
		loadAllAgNews();
		
		PopulateNFoldWebSnippet();
	}
	
	public HashSet<String> getUniqueWordsTitle() {
		return uniqueWordsTitle;
	}

	public ArrayList<String[]> getAldocsBodyTitleLabelFlat() {
		return aldocsBodyTitleLabelFlat;
	}

	public ArrayList<String> GetBodies(){
		return alBodies;
	}
	
	public HashSet<String> getUniqueWords(){
		return uniqueWords;
	}
	
	public ArrayList<String[]> getAgNewsFlat(){
		return aldocsBodeyLabelFlat;
	}
	
	public ArrayList<ArrayList<String[]>> GetDocsAgNewsFlatList() {
		return aldocsBodeyLabelFlatList;
	}
	
	public List<List<String>> GetAgNewsDocuments(){
		return documents;
	}
	
	public LinkedHashMap<String, ArrayList<String>> getAgNewsList(){
		return docsLabelBodyList;
	}
	
	private void loadAllAgNews() {
		
		try{
			BufferedReader br =  new BufferedReader(new FileReader(AgNewsConstant.AgNewsDocsFile));
			
			String line="";
			while((line=br.readLine()) != null) {
				line = line.trim().toLowerCase();
		        if(line.isEmpty()) continue;
		       
		        String [] arrLabelTitleBody = line.split("\\t");
			   
		        if(arrLabelTitleBody.length!=3 && arrLabelTitleBody.length!=2)
				   continue;
			   
		        String label=null;
		        String body=null;
		        String bodyTitle = "";
		        
		        if(arrLabelTitleBody.length==3){
		        	label = arrLabelTitleBody[0].trim();
			       body =  (arrLabelTitleBody[1]+ " " +arrLabelTitleBody[2]).trim();
			       bodyTitle = arrLabelTitleBody[1].trim();
		        }else if(arrLabelTitleBody.length==2){
		        	label = arrLabelTitleBody[0].trim();
			        body =  arrLabelTitleBody[1].trim();
			        bodyTitle = arrLabelTitleBody[1].trim();
		        }
		        
		        body = docClusterUtil.textUtilShared.PerformPreprocess(body);
		        ArrayList<String> processed = docClusterUtil.textUtilShared.RemoveStopWord(body);
		        body = docClusterUtil.textUtilShared.ConvertArrayListToString(processed);
		        
		        bodyTitle = docClusterUtil.textUtilShared.PerformPreprocess(bodyTitle);
		        ArrayList<String> processedTitle = docClusterUtil.textUtilShared.RemoveStopWord(bodyTitle);
		        bodyTitle = docClusterUtil.textUtilShared.ConvertArrayListToString(processedTitle);
		        
		        if(body.isEmpty()) continue;
		        
		        alBodies.add(body);
		        
		        uniqueWords.addAll(processed);
		        uniqueWordsTitle.addAll(processedTitle);
		        
		        documents.add(processed);
		        
//		        String arr[] = new String[2];
//		        arr[0]= body;
//		        arr[1] = label;
			    
		        aldocsBodeyLabelFlat.add(new String[]{body, label});
		        aldocsBodyTitleLabelFlat.add(new String[] {bodyTitle, label});
		        
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
				System.out.println(key+","+docsLabelBodyList.get(key).size());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void PopulateNFoldWebSnippet() {
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
