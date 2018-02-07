package dal.clustering.document.dataset.agnews;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import dal.clustering.document.shared.DocClusterUtil;

public class AgNewsUtil {
	
	HashSet<String> uniqueWords;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	List<List<String>> documents;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	public DocClusterUtil docClusterUtil;
	
	public AgNewsUtil(){
		
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		uniqueWords = new HashSet<String>();
		documents = new ArrayList<List<String>>();
		docClusterUtil = new DocClusterUtil();
		
		loadAllAgNews();
	}
	
	public HashSet<String> getUniqueWords(){
		return uniqueWords;
	}
	
	public ArrayList<String[]> getAgNewsFlat(){
		return aldocsBodeyLabelFlat;
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
		        
		        if(arrLabelTitleBody.length==3){
		        	label = arrLabelTitleBody[0].trim();
			       body =  (arrLabelTitleBody[1]+ " " +arrLabelTitleBody[2]).trim();
		        }else if(arrLabelTitleBody.length==2){
		        	label = arrLabelTitleBody[0].trim();
			        body =  arrLabelTitleBody[1].trim();
		        }
		        
		        body = docClusterUtil.textUtilShared.PerformPreprocess(body);
		        ArrayList<String> processed = docClusterUtil.textUtilShared.RemoveStopWord(body);
		        body = docClusterUtil.textUtilShared.ConvertArrayListToString(processed);
		        
		        if(body.isEmpty()) continue;
		        
		        uniqueWords.addAll(processed);
		        
		        documents.add(processed);
		        
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
