package dal.relatedness.phrase.load.nontokenized.unigram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;

public class LoadNonTokenizeUnigram {
	
	private ArrayList<String> _alStemmedCrossCheckedUnis;
	private ArrayList<Integer> _alStemmedCrossCheckedUniFreqs;
	
	private HashMap<String, Integer> _hmStemmedCrossCheckedUniIndex;
	
	public ArrayList<String> getAlStemmedCrossCheckedUnis() {
		return _alStemmedCrossCheckedUnis;
	}
	
	public ArrayList<Integer> getAlStemmedCrossCheckedUniFreqs() {
		return _alStemmedCrossCheckedUniFreqs;
	}
	
	public HashMap<String, Integer> getHmStemmedCrossCheckedUniIndex(){
		return _hmStemmedCrossCheckedUniIndex;
	}
	
	public void ClearStemmedCrossChecked() {
		if(_alStemmedCrossCheckedUnis!=null) _alStemmedCrossCheckedUnis.clear();
		if(_alStemmedCrossCheckedUniFreqs!=null) _alStemmedCrossCheckedUniFreqs.clear();
		
		if(_hmStemmedCrossCheckedUniIndex!=null) _hmStemmedCrossCheckedUniIndex.clear();
		
		_alStemmedCrossCheckedUnis = null;
		_alStemmedCrossCheckedUniFreqs = null;
		_hmStemmedCrossCheckedUniIndex = null;
	}

	public void PopulateStemmedUniGramsList(){
		
		try{
		
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(PhraseDirectories.unigramCrossCheckDir+PhraseFileNames.uniGramStemmedCrossChecekedFile));		
		
			_alStemmedCrossCheckedUnis = new ArrayList<String>();
			_alStemmedCrossCheckedUniFreqs = new ArrayList<Integer>();
			
			System.out.println("start:"+new Date().toString());
			
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				String arr[] = sCurrentLine.split("\\s+");
				
				if(arr.length!=2){
					System.out.println("Wrong Uni "+sCurrentLine);
					continue;
				}

				_alStemmedCrossCheckedUnis.add(arr[0]);
				_alStemmedCrossCheckedUniFreqs.add(Integer.parseInt(arr[1]));
			}
			
			br.close();
			
			System.out.println("PopulateStemmedTokenizedUniGrams end."+_alStemmedCrossCheckedUnis.size()+", "+new Date().toString());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void PopulateStemmedUniGramsHashMap(){
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(PhraseDirectories.unigramCrossCheckDir+PhraseFileNames.uniGramStemmedCrossChecekedFile));		
		
			_hmStemmedCrossCheckedUniIndex = new HashMap<String, Integer>();
			_alStemmedCrossCheckedUniFreqs = new ArrayList<Integer>();
			
			int id=0;
			
			System.out.println("start:"+new Date().toString());
			
			while ((sCurrentLine = br.readLine()) != null) {
				if(sCurrentLine.isEmpty()) continue;
				
				String arr[] = sCurrentLine.split("\\s+");
				
				if(arr.length!=2){
					System.out.println("Wrong Uni "+sCurrentLine);
					continue;
				}
				
				_hmStemmedCrossCheckedUniIndex.put(arr[0], id++);
				_alStemmedCrossCheckedUniFreqs.add(Integer.parseInt(arr[1]));
			}
			
			br.close();
			
			System.out.println("PopulateStemmedUniGramsHashMap end."+_alStemmedCrossCheckedUniFreqs.size()+", "+new Date().toString());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
