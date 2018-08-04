package dal.clustering.document.dataset.trec;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.DocClusterUtil;

public class TrecUtil {
	HashSet<String> uniqueWords;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	public DocClusterUtil docClusterUtil;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	
	public TrecUtil(){
		
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		uniqueWords = new HashSet<String>();
		docClusterUtil = new DocClusterUtil();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		loadAllTrec();
	}
	
	
	public HashSet<String> getUniqueWords(){
		return uniqueWords;
	}
	
	public ArrayList<String[]> getTrecFlat(){
		return aldocsBodeyLabelFlat;
	}
	
	public LinkedHashMap<String, ArrayList<String>> getTrecList(){
		return docsLabelBodyList;
	}
	
	private void loadAllTrec() {
		
		try{
			BufferedReader br =  new BufferedReader(new FileReader(TrecConstant.TrecDocsFile));
			
			String line="";
			
			while((line=br.readLine()) != null) {
				line = line.trim().toLowerCase();
		        if(line.isEmpty()) continue;
		       
		        String [] arrLabelBody = line.split("\\t");
			   
		        if(arrLabelBody.length!=2)
				   continue;
			   
		        String label=null;
		        String body=null;
		   
	        	label = arrLabelBody[0].trim();
		        body =  arrLabelBody[1].trim();
		        
		        body = docClusterUtil.textUtilShared.PerformPreprocess(body);
		        ArrayList<String> processed = docClusterUtil.textUtilShared.RemoveStopWord(body);
		        body = docClusterUtil.textUtilShared.ConvertArrayListToString(processed);
		        
		        if(body.isEmpty()) continue;
		      
		        uniqueWords.addAll(processed);
		        
		        aldocsBodeyLabelFlat.add(new String[]{body, label});
		        
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
			
			System.out.println("total="+aldocsBodeyLabelFlat.size());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
