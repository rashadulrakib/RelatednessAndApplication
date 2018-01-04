package dal.relatedness.phrase.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;

public class ContextDictionaryLoader {
	
	private ArrayList<String> _alStemmedCrossCheckedContexts;
	
	public ArrayList<String> getStemmedCrossCheckedContexts() {
		return _alStemmedCrossCheckedContexts;
	}
	
	public void ClearStemmedCrossChecked() {
		if(_alStemmedCrossCheckedContexts!=null) _alStemmedCrossCheckedContexts.clear();
		
		_alStemmedCrossCheckedContexts = null;
	}
	
	public void PopulateStemmedContextDictionary(){
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(PhraseDirectories.nGramIndexedProcessedContextStemDictionaryDir+PhraseFileNames.nGmAllContextFile));		
		
			_alStemmedCrossCheckedContexts = new ArrayList<String>();
			
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				String arr[] = sCurrentLine.split("\\s+");
				if(arr.length!=2){
					System.out.println("wrong context "+ sCurrentLine);
					continue;
				}
				
				_alStemmedCrossCheckedContexts.add(sCurrentLine);
				
				if(_alStemmedCrossCheckedContexts.size()%1000000==0){
					System.out.println("_alStemmedCrossCheckedContexts="+_alStemmedCrossCheckedContexts.size());
				}
			}
			
			br.close();
			
			System.out.println("PopulateStemmedContextDictionary end."+_alStemmedCrossCheckedContexts.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
