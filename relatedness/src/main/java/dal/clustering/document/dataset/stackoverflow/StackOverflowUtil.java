package dal.clustering.document.dataset.stackoverflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import dal.clustering.document.shared.DocClusterUtil;

public class StackOverflowUtil {

	public DocClusterUtil docClusterUtil;
	HashSet<String> uniqueWords;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	List<List<String>> documents;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	ArrayList<String> alBodies;
	
	public StackOverflowUtil(){
		docClusterUtil = new DocClusterUtil();
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		uniqueWords = new HashSet<String>();
		documents = new ArrayList<List<String>>();
		alBodies = new ArrayList<String>();
		
		loadAllDocsStackOverflow();
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
	
	public List<List<String>> GetStackOverflowDocuments() {
		return documents;
	}
	
	public LinkedHashMap<String, ArrayList<String>> getDocsStackOverflowList(){
		return docsLabelBodyList;
	}
	
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
		        ArrayList<String> processed = docClusterUtil.textUtilShared.RemoveStopWord(body);
		        body = docClusterUtil.textUtilShared.ConvertArrayListToString(processed);
		       
		        if(body.isEmpty()) continue;
		        
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

	
}
