package dal.clustering.document.stackoverflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.DocClusterUtil;

public class StackOverflowUtil {

	public DocClusterUtil docClusterUtil;
	HashSet<String> uniqueWords;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	
	public StackOverflowUtil(){
		docClusterUtil = new DocClusterUtil();
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		uniqueWords = new HashSet<String>();
		
		loadAllDocsStackOverflow();
	}
	
	public HashSet<String> getUniqueWords(){
		return uniqueWords;
	}
	
	public ArrayList<String[]> getDocsStackOverflowFlat(){
		return aldocsBodeyLabelFlat;
	}
	
	public LinkedHashMap<String, ArrayList<String>> getDocsStackOverflowList(){
		return docsLabelBodyList;
	}
	
	private void loadAllDocsStackOverflow() {
		try{
			BufferedReader br =  new BufferedReader(new FileReader(StackOverflowConstant.StackOverflowDocsFile));
			
			String line = "";
			while((line = br.readLine())!=null){
			   
			   line = line.trim();
			   if(line.isEmpty()) continue;
			   
			   String [] arrLabelBody = line.split("\\t");
			   
			   if(arrLabelBody.length!=2)
				   continue;
			   
			   String label = arrLabelBody[0].trim();
			   String body =  arrLabelBody[1].trim();
			   
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
