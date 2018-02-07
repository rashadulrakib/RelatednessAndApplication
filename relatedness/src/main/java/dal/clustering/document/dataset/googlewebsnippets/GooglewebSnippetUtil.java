package dal.clustering.document.dataset.googlewebsnippets;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import dal.clustering.document.shared.DocClusterUtil;

public class GooglewebSnippetUtil {
	
	public DocClusterUtil docClusterUtil;
	HashSet<String> uniqueWords;
	ArrayList<String[]> aldocsBodeyLabelFlat;
	ArrayList<String> aldocsBodeyLabelFlatWithStopWords;
	LinkedHashMap<String, ArrayList<String>> docsLabelBodyList;
	List<List<String>> documents;
	ArrayList<String> alBodies;
	
	public GooglewebSnippetUtil(){
		docClusterUtil = new DocClusterUtil();
		aldocsBodeyLabelFlat = new ArrayList<String[]>();
		aldocsBodeyLabelFlatWithStopWords = new ArrayList<String>();
		documents = new ArrayList<List<String>>();
		docsLabelBodyList = new LinkedHashMap<String, ArrayList<String>>();
		uniqueWords = new HashSet<String>();
		alBodies = new ArrayList<String>();
		
		loadAllDocsGoogleWebSnippet();

	}
	
	public ArrayList<String> GetBodies(){
		return alBodies;
	}
	
	public HashSet<String> GetUniqueWords(){
		return uniqueWords;
	}
	
	public ArrayList<String[]> GetDocsGoogleWebSnippetFlat(){
		return aldocsBodeyLabelFlat;
	}
	
	public List<List<String>> GetWebSnippetNewsDocuments() {
		return documents;
	}
	
	public ArrayList<String> GetDocsGoogleWebSnippetFlatWithStopWords(){
		return aldocsBodeyLabelFlatWithStopWords;
	}
	
	public LinkedHashMap<String, ArrayList<String>> GetDocsGoogleWebSnippetList(){
		return docsLabelBodyList;
	}
	
	private void loadAllDocsGoogleWebSnippet() {
		try{
			BufferedReader br =  new BufferedReader(new FileReader(GoogleWebSnippetConstant.GoogleWebSnippetDocsFile));
			
			String line="";
			while((line=br.readLine()) != null) {
		        line = line.trim().toLowerCase();
		        if(line.isEmpty()) continue;
		        
//		        int lastSpaceIndex = line.lastIndexOf(" ");
//		        String label = line.substring(lastSpaceIndex).trim();
//		        
//		        String body = line.substring(0, lastSpaceIndex).trim();
		        
		        String [] arrLabelBody = line.split("\\t");
				   
			   if(arrLabelBody.length!=2)
				   continue;
			   
			   String label = arrLabelBody[0].trim();
			   String body =  arrLabelBody[1].trim();
		        
		        body = docClusterUtil.textUtilShared.PerformPreprocess(body);
		        
		        if(body.isEmpty()){
		        	continue;
		        }
		        
		        aldocsBodeyLabelFlatWithStopWords.add(body);
		        
		        ArrayList<String> noStopWords = docClusterUtil.textUtilShared.RemoveStopWord(body);
		        body = docClusterUtil.textUtilShared.ConvertArrayListToString(noStopWords);
		        
		        if(body.isEmpty()){
		        	continue;
		        }
		        
		        alBodies.add(body);

		        documents.add(noStopWords);
		        
		        uniqueWords.addAll(noStopWords);
		        
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
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
