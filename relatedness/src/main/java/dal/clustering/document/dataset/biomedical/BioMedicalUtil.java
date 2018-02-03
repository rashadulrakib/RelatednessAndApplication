package dal.clustering.document.dataset.biomedical;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.DocClusterUtil;

public class BioMedicalUtil {
	public DocClusterUtil docClusterUtil;
	HashSet<String> uniqueWords;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	
	public BioMedicalUtil(){
		docClusterUtil = new DocClusterUtil();
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		uniqueWords = new HashSet<String>();
		
		loadAllDocsBiomedical();
	}
	
	public HashSet<String> getUniqueWords(){
		return uniqueWords;
	}
	
	public ArrayList<String[]> getDocsBiomedicalFlat(){
		return aldocsBodeyLabelFlat;
	}
	
	public LinkedHashMap<String, ArrayList<String>> getDocsBiomedicalList(){
		return docsLabelBodyList;
	}
		
	private void loadAllDocsBiomedical() {
		try{
			
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
		   
		   for(String key: docsLabelBodyList.keySet()){
				System.out.println(key+","+docsLabelBodyList.get(key).size());
			}
		   
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
