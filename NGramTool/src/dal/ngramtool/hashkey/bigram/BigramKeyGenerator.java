package dal.ngramtool.hashkey.bigram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import dal.ngramtool.constants.Directories;
import dal.ngramtool.constants.FileNames;


public class BigramKeyGenerator {
	public void GenerateBigramHashFunction() {
		try {
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(Directories.bigramCrossCheckDir+FileNames.bgStemmedCrossedCheckedFile));		
		
			//HashMap<String, Integer> hm = new HashMap<String, Integer> ();
			ArrayList<HashMap<String, Integer>> al = new ArrayList<HashMap<String,Integer>>();
			
			for(int i=0;i<36;i++){
				al.add(null);
			}
			
			int id=0;
			
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				String arr[] = sCurrentLine.split("\\s+");
				
				if(arr.length!=3){
					System.out.println("Wrong bg "+sCurrentLine);
					continue;
				}

				String key = arr[0]+" "+arr[1];
				
				int subtract =  key.charAt(0)-'0'-39; 
				
				int alIndex = subtract<0? key.charAt(0)-'0':subtract; 
				
				if(al.get(alIndex)==null){
					HashMap<String, Integer> hm = new HashMap<String, Integer>();
					hm.put(key, id++);
					al.add(alIndex, hm);
				}
				else{
					HashMap<String, Integer> hm = al.get(alIndex);
					if (!hm.containsKey(key)) {
						hm.put(key, id++);
					} else {
						System.out.println(key + " duplicate.");
					}
					al.add(alIndex, hm);
				}
				

			}
			
			br.close();
			
			System.out.println(al.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
