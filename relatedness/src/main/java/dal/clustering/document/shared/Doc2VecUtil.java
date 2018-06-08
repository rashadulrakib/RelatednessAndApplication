package dal.clustering.document.shared;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import dal.clustering.document.shared.entities.InstanceW2Vec;

public class Doc2VecUtil {

	public ArrayList<InstanceW2Vec> CreateDoc2VecInstances(String doc2vecFile,
			ArrayList<String[]> docsBiomedicalFlat, String seperator) {
		ArrayList<InstanceW2Vec> doc2vecs = new ArrayList<InstanceW2Vec>();
		try{
			
			BufferedReader br = new BufferedReader(new FileReader(doc2vecFile));
			
			String text="";

			int lineIndex = 0;
			
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	
            	if(text.isEmpty()) continue;
            	
            	String [] arr = text.split(seperator);

            	double [] ftrs = new double[arr.length];
            	
            	for(int i =0;i<arr.length;i++){
            		ftrs[i]= Double.parseDouble(arr[i]);
            	}
            	
            	InstanceW2Vec inst = new InstanceW2Vec();
            	inst.Features = ftrs;
            	inst.Text =  docsBiomedicalFlat.get(lineIndex)[0];
            	inst.OriginalLabel =  docsBiomedicalFlat.get(lineIndex)[1];
            	
            	doc2vecs.add(inst);
            	
            	lineIndex++;
            }
			
            br.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return doc2vecs;
	}
}
