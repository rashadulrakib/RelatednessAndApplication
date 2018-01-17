package dal.clustering.document.dataset.agnews;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.DocClusterUtil;

public class AgNewsUtil {
	
	public DocClusterUtil docClusterUtil;
	HashSet<String> uniqueWords;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	
	public AgNewsUtil(){
		docClusterUtil = new DocClusterUtil();
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		uniqueWords = new HashSet<String>();
		
		loadAllAgNews();
	}
	
	public HashSet<String> getUniqueWords(){
		return uniqueWords;
	}
	
	public ArrayList<String[]> getAgNewsFlat(){
		return aldocsBodeyLabelFlat;
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
		        
		        body = docClusterUtil.PerformPreprocess(body);
		        ArrayList<String> processed = docClusterUtil.RemoveStopWord(body);
		        body = docClusterUtil.ConvertArrayListToString(processed);
		        
		        if(body.isEmpty()) continue;
		        
		        uniqueWords.addAll(processed);
		        
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
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}