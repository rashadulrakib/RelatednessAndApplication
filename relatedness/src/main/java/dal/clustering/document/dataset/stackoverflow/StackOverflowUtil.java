package dal.clustering.document.dataset.stackoverflow;

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

public class StackOverflowUtil {

	public DocClusterUtil docClusterUtil;
	HashSet<String> uniqueWords;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	List<List<String>> documents;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	ArrayList<ArrayList<String[]>> aldocsBodeyLabelFlatList;
	ArrayList<String> alBodies;
	
	public StackOverflowUtil(){
		docClusterUtil = new DocClusterUtil();
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		uniqueWords = new HashSet<String>();
		documents = new ArrayList<List<String>>();
		alBodies = new ArrayList<String>();
		aldocsBodeyLabelFlatList = new ArrayList<ArrayList<String[]>>();
		
		loadAllDocsStackOverflow();
		
		PopulateNFoldData();
	}
	
	public ArrayList<String> GetBodies(){
		return alBodies;
	}
	
	public HashSet<String> getUniqueWords(){
		return uniqueWords;
	}
	
	public ArrayList<String[]> getDocsStackOverflowFlat(){
		return aldocsBodeyLabelFlat;
	}
	
	public ArrayList<ArrayList<String[]>> GetDocsStackOverflowFlatList() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<List<String>> GetStackOverflowDocuments() {
		return documents;
	}
	
	public LinkedHashMap<String, ArrayList<String>> getDocsStackOverflowList(){
		return docsLabelBodyList;
	}
	
	@SuppressWarnings("unchecked")
	private void loadAllDocsStackOverflow() {
		try{
			BufferedReader br =  new BufferedReader(new FileReader(StackOverflowConstant.StackOverflowDocsFile));
			
			String line = "";
			while((line = br.readLine())!=null){
			   
			   line = line.trim().toLowerCase();
			   if(line.isEmpty()) continue;
			   
			   String [] arrLabelBody = line.split("\\t");
			   
			   if(arrLabelBody.length!=2)
				   continue;
			   
			   String label = arrLabelBody[0].trim();
			   String body =  arrLabelBody[1].trim();
			   
			   body = docClusterUtil.textUtilShared.PerformPreprocess(body);
		        //@SuppressWarnings("unchecked")
				ArrayList<String> processed = docClusterUtil.textUtilShared.RemoveStopWord(body);
		        body = docClusterUtil.textUtilShared.ConvertArrayListToString(processed);
		       
		        if(body.isEmpty()){
		        	//body = arrLabelBody[1].trim();
		        	continue;
		        }
		        
		        uniqueWords.addAll(processed);
		        
		        documents.add(processed);
		        
		        alBodies.add(body);
		        
		        String arr[] = new String[2];
		        arr[0]= body;
		        arr[1] = label;
			    
		        aldocsBodeyLabelFlat.add(arr);
		        
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
