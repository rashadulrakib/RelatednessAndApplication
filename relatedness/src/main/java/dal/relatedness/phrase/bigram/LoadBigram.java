package dal.relatedness.phrase.bigram;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.entities.bigram.BigramKey;
import dal.relatedness.phrase.utils.common.Util;

public class LoadBigram {
	
	private LinkedHashMap<String, Integer> _hmBgKeyBase62Ids;
	private LinkedHashMap<Integer, Integer> _hmBgKeySumIds;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyIds;

	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqA;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqB;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqC;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqD;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqE;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqF;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqG;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqH;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqI;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqJ;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqK;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqL;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqM;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqN;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqO;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqP;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqQ;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqR;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqS;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqT;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqU;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqV;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqW;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqX;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqY;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreqZ;
	private LinkedHashMap<BigramKey, Integer> _hmBgDoubleKeyFreq_;

	
	private ArrayList<Integer> _bgFreqs;

//	private ArrayList<Integer> _bgFreqsA;
//	private ArrayList<Integer> _bgFreqsB;
//	private ArrayList<Integer> _bgFreqsC;
//	private ArrayList<Integer> _bgFreqsD;
//	private ArrayList<Integer> _bgFreqsE;
//	private ArrayList<Integer> _bgFreqsF;
//	private ArrayList<Integer> _bgFreqsG;
//	private ArrayList<Integer> _bgFreqsH;
//	private ArrayList<Integer> _bgFreqsI;
//	private ArrayList<Integer> _bgFreqsJ;
//	private ArrayList<Integer> _bgFreqsK;
//	private ArrayList<Integer> _bgFreqsL;
//	private ArrayList<Integer> _bgFreqsM;
//	private ArrayList<Integer> _bgFreqsN;
//	private ArrayList<Integer> _bgFreqsO;
//	private ArrayList<Integer> _bgFreqsP;
//	private ArrayList<Integer> _bgFreqsQ;
//	private ArrayList<Integer> _bgFreqsR;
//	private ArrayList<Integer> _bgFreqsS;
//	private ArrayList<Integer> _bgFreqsT;
//	private ArrayList<Integer> _bgFreqsU;
//	private ArrayList<Integer> _bgFreqsV;
//	private ArrayList<Integer> _bgFreqsW;
//	private ArrayList<Integer> _bgFreqsX;
//	private ArrayList<Integer> _bgFreqsY;
//	private ArrayList<Integer> _bgFreqsZ;
//	private ArrayList<Integer> _bgFreqs_;


	public ArrayList<Integer> getBgFreqs() {
		return _bgFreqs;
	}

	public LinkedHashMap<String, Integer> getBgKeyBase62Ids() {
		return _hmBgKeyBase62Ids;
	}
	
	public LinkedHashMap<Integer, Integer> getBgKeySumIds() {
		return _hmBgKeySumIds;
	}
	
	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyIds() {
		return _hmBgDoubleKeyIds;
	}

	
	
	
	
	
	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqA() {
		return _hmBgDoubleKeyFreqA;
	}
	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqB() {
		return _hmBgDoubleKeyFreqB;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqC() {
		return _hmBgDoubleKeyFreqC;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqD() {
		return _hmBgDoubleKeyFreqD;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqE() {
		return _hmBgDoubleKeyFreqE;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqF() {
		return _hmBgDoubleKeyFreqF;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqG() {
		return _hmBgDoubleKeyFreqG;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqH() {
		return _hmBgDoubleKeyFreqH;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqI() {
		return _hmBgDoubleKeyFreqI;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqJ() {
		return _hmBgDoubleKeyFreqJ;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqK() {
		return _hmBgDoubleKeyFreqK;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqL() {
		return _hmBgDoubleKeyFreqL;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqM() {
		return _hmBgDoubleKeyFreqM;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqN() {
		return _hmBgDoubleKeyFreqN;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqO() {
		return _hmBgDoubleKeyFreqO;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqP() {
		return _hmBgDoubleKeyFreqP;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqQ() {
		return _hmBgDoubleKeyFreqQ;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqR() {
		return _hmBgDoubleKeyFreqR;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqS() {
		return _hmBgDoubleKeyFreqS;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqT() {
		return _hmBgDoubleKeyFreqT;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqU() {
		return _hmBgDoubleKeyFreqU;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqV() {
		return _hmBgDoubleKeyFreqV;
	}
	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqW() {
		return _hmBgDoubleKeyFreqW;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqX() {
		return _hmBgDoubleKeyFreqX;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqY() {
		return _hmBgDoubleKeyFreqY;
	}

	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreqZ() {
		return _hmBgDoubleKeyFreqZ;
	}
	
	public LinkedHashMap<BigramKey, Integer> getBgDoubleKeyFreq_() {
		return _hmBgDoubleKeyFreq_;
	}

//	public ArrayList<Integer> getBgFreqsA() {
//		return _bgFreqsA;
//	}
//	public ArrayList<Integer> getBgFreqsB() {
//		return _bgFreqsB;
//	}	
//	public ArrayList<Integer> getBgFreqsC() {
//		return _bgFreqsC;
//	}	
//	public ArrayList<Integer> getBgFreqsD() {
//		return _bgFreqsD;
//	}	
//	public ArrayList<Integer> getBgFreqsE() {
//		return _bgFreqsE;
//	}	
//	public ArrayList<Integer> getBgFreqsF() {
//		return _bgFreqsF;
//	}
//	public ArrayList<Integer> getBgFreqsG() {
//		return _bgFreqsG;
//	}
//	public ArrayList<Integer> getBgFreqsH() {
//		return _bgFreqsH;
//	}	
//	public ArrayList<Integer> getBgFreqsI() {
//		return _bgFreqsI;
//	}	
//	public ArrayList<Integer> getBgFreqsJ() {
//		return _bgFreqsJ;
//	}	
//	public ArrayList<Integer> getBgFreqsK() {
//		return _bgFreqsK;
//	}	
//	public ArrayList<Integer> getBgFreqsL() {
//		return _bgFreqsL;
//	}
//	public ArrayList<Integer> getBgFreqsM() {
//		return _bgFreqsM;
//	}
//	public ArrayList<Integer> getBgFreqsN() {
//		return _bgFreqsN;
//	}	
//	public ArrayList<Integer> getBgFreqsO() {
//		return _bgFreqsO;
//	}	
//	public ArrayList<Integer> getBgFreqsP() {
//		return _bgFreqsP;
//	}	
//	public ArrayList<Integer> getBgFreqsQ() {
//		return _bgFreqsQ;
//	}	
//	public ArrayList<Integer> getBgFreqsR() {
//		return _bgFreqsR;
//	}
//	public ArrayList<Integer> getBgFreqsS() {
//		return _bgFreqsS;
//	}
//	public ArrayList<Integer> getBgFreqsT() {
//		return _bgFreqsT;
//	}	
//	public ArrayList<Integer> getBgFreqsU() {
//		return _bgFreqsU;
//	}	
//	public ArrayList<Integer> getBgFreqsV() {
//		return _bgFreqsV;
//	}	
//	public ArrayList<Integer> getBgFreqsW() {
//		return _bgFreqsW;
//	}	
//	public ArrayList<Integer> getBgFreqsX() {
//		return _bgFreqsX;
//	}
//	public ArrayList<Integer> getBgFreqsY() {
//		return _bgFreqsY;
//	}	
//	public ArrayList<Integer> getBgFreqsZ() {
//		return _bgFreqsZ;
//	}
//	public ArrayList<Integer> getBgFreqs_() {
//		return _bgFreqs_;
//	}
		

	public void PopulateBgDoubleKeyIdFreq(){
		try{
			/*char ch;
			String firstIndexChar;
			
			_hmBgDoubleKeyIds = new LinkedHashMap<BigramKey, Integer>();
			_bgFreqs = new ArrayList<Integer>();
			
			for( ch = 'a' ; ch <= 'a' ; ch++ ){
	     			firstIndexChar = Character.toString(ch);
				System.out.println(firstIndexChar);	
				
				String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
				
				
				FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
				DataInputStream in = new DataInputStream(fileIn);
				
				int numberOfRecords = in.readInt();
				System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
				for(int i=0;i<numberOfRecords;i++){
					int ugkey1 = in.readInt();
					int ugkey2 = in.readInt();
					int tokenIndex = in.readInt();
					int frequency = in.readInt();
					
					_bgFreqs.add(frequency);
			
					_hmBgDoubleKeyIds.put(new BigramKey(ugkey1, ugkey2) , tokenIndex);
				}
				
				in.close();
				fileIn.close();
				
				System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
				System.out.println(_hmBgDoubleKeyIds.size()+","+"array size"+_bgFreqs.size()+new Date().toString());
			}*/
			
			//PopulateBgDoubleKeyIdFreqA();
			//PopulateBgDoubleKeyIdFreqB();
//			PopulateBgDoubleKeyIdFreqC();
//			PopulateBgDoubleKeyIdFreqD();
//			PopulateBgDoubleKeyIdFreqE();
//			PopulateBgDoubleKeyIdFreqG();
//			PopulateBgDoubleKeyIdFreqH();
//			PopulateBgDoubleKeyIdFreqI();
//			PopulateBgDoubleKeyIdFreqJ();
//			PopulateBgDoubleKeyIdFreqK();
//			PopulateBgDoubleKeyIdFreqL();
//			PopulateBgDoubleKeyIdFreqM();
//			PopulateBgDoubleKeyIdFreqN();
//			PopulateBgDoubleKeyIdFreqO();
//			PopulateBgDoubleKeyIdFreqP();
//			PopulateBgDoubleKeyIdFreqQ();
//			PopulateBgDoubleKeyIdFreqR();
//			PopulateBgDoubleKeyIdFreqS();
//			PopulateBgDoubleKeyIdFreqT();
//			PopulateBgDoubleKeyIdFreqU();
//			PopulateBgDoubleKeyIdFreqV();
//			PopulateBgDoubleKeyIdFreqW();
//			PopulateBgDoubleKeyIdFreqX();
//			PopulateBgDoubleKeyIdFreqY();
			
//			PopulateBgDoubleKeyIdFreq_();
		}
		catch(Exception e){
			
		}
	}
	
	public void PopulateBgDoubleKeyFreq(){
		try{
			PopulateBgDoubleKeyFreqW();
		}
		catch(Exception e){
			
		}
	}

//	private void PopulateBgDoubleKeyFreqA(){
//		try{
//			String firstIndexChar = Character.toString('a');
//			
//			_hmBgDoubleKeyFreqA = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//
//				_hmBgDoubleKeyFreqA.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqA.size()+","+new Date().toString());
//			
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqB(){
//		try{
//			String firstIndexChar = Character.toString('b');
//			
//			_hmBgDoubleKeyFreqB = new LinkedHashMap<BigramKey, Integer>();
//		
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//			
//				_hmBgDoubleKeyFreqB.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqB.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqC(){
//		try{
//			String firstIndexChar = Character.toString('c');
//			
//			_hmBgDoubleKeyFreqC = new LinkedHashMap<BigramKey, Integer>();
//		
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//				_hmBgDoubleKeyFreqC.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqC.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqD(){
//		try{
//			String firstIndexChar = Character.toString('d');
//			
//			_hmBgDoubleKeyFreqD = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//				_hmBgDoubleKeyFreqD.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqD.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqE(){
//		try{
//			String firstIndexChar = Character.toString('e');
//			
//			_hmBgDoubleKeyFreqE = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//				
//				_hmBgDoubleKeyFreqE.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqE.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqF(){
//		try{
//			String firstIndexChar = Character.toString('f');
//			
//			_hmBgDoubleKeyFreqF = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//				_hmBgDoubleKeyFreqF.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqF.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqG(){
//		try{
//			String firstIndexChar = Character.toString('g');
//			
//			_hmBgDoubleKeyFreqG = new LinkedHashMap<BigramKey, Integer>();
//		
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqG.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqG.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqH(){
//		try{
//			String firstIndexChar = Character.toString('h');
//			
//			_hmBgDoubleKeyFreqH = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqH.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqH.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqI(){
//		try{
//			String firstIndexChar = Character.toString('i');
//			
//			_hmBgDoubleKeyFreqI = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//		
//				_hmBgDoubleKeyFreqI.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqI.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqJ(){
//		try{
//			String firstIndexChar = Character.toString('j');
//			
//			_hmBgDoubleKeyFreqJ = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//			
//				_hmBgDoubleKeyFreqJ.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqJ.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqK(){
//		try{
//			String firstIndexChar = Character.toString('k');
//			
//			_hmBgDoubleKeyFreqK = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqK.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqK.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqL(){
//		try{
//			String firstIndexChar = Character.toString('l');
//			
//			_hmBgDoubleKeyFreqL = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqL.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqL.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqM(){
//		try{
//			String firstIndexChar = Character.toString('m');
//			
//			_hmBgDoubleKeyFreqM = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//		
//				_hmBgDoubleKeyFreqM.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqM.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqN(){
//		try{
//			String firstIndexChar = Character.toString('n');
//			
//			_hmBgDoubleKeyFreqN = new LinkedHashMap<BigramKey, Integer>();
//		
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqN.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqN.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqO(){
//		try{
//			String firstIndexChar = Character.toString('o');
//			
//			_hmBgDoubleKeyFreqO = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqO.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqO.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqP(){
//		try{
//			String firstIndexChar = Character.toString('p');
//			
//			_hmBgDoubleKeyFreqP = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqP.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqP.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqQ(){
//		try{
//			String firstIndexChar = Character.toString('q');
//			
//			_hmBgDoubleKeyFreqQ = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqQ.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqQ.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqR(){
//		try{
//			String firstIndexChar = Character.toString('r');
//			
//			_hmBgDoubleKeyFreqR = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqR.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqR.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqS(){
//		try{
//			String firstIndexChar = Character.toString('s');
//			
//			_hmBgDoubleKeyFreqS = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqS.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqS.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqT(){
//		try{
//			String firstIndexChar = Character.toString('t');
//			
//			_hmBgDoubleKeyFreqT = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqT.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqT.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqU(){
//		try{
//			String firstIndexChar = Character.toString('u');
//			
//			_hmBgDoubleKeyFreqU = new LinkedHashMap<BigramKey, Integer>();
//		
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//				_hmBgDoubleKeyFreqU.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqU.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqV(){
//		try{
//			String firstIndexChar = Character.toString('v');
//			
//			_hmBgDoubleKeyFreqV = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//			
//				_hmBgDoubleKeyFreqV.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqV.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
	private void PopulateBgDoubleKeyFreqW(){
		try{
			String firstIndexChar = Character.toString('w');
			
			_hmBgDoubleKeyFreqW = new LinkedHashMap<BigramKey, Integer>();
			
			System.out.println(firstIndexChar);	
			String tokenizedBiGramFile= PhraseDirectories.tokenizedBiGramDir+ firstIndexChar;
			
			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
			DataInputStream in = new DataInputStream(fileIn);
			
			int numberOfRecords = in.readInt();
			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
			for(int i=0;i<numberOfRecords;i++){
				int ugkey1 = in.readInt();
				int ugkey2 = in.readInt();
				int tokenIndex = in.readInt();
				int frequency = in.readInt();
				
				
				_hmBgDoubleKeyFreqW.put(new BigramKey(ugkey1, ugkey2) , frequency);
			}
			
			in.close();
			fileIn.close();
			
			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
			System.out.println(_hmBgDoubleKeyFreqW.size()+","+new Date().toString());
		}
		catch(Exception e){
			
		}

	}
//	
//	private void PopulateBgDoubleKeyFreqX(){
//		try{
//			String firstIndexChar = Character.toString('x');
//			
//			_hmBgDoubleKeyFreqX = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//			
//				_hmBgDoubleKeyFreqX.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqX.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqY(){
//		try{
//			String firstIndexChar = Character.toString('y');
//			
//			_hmBgDoubleKeyFreqY = new LinkedHashMap<BigramKey, Integer>();
//		
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//				
//				_hmBgDoubleKeyFreqY.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqY.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreqZ(){
//		try{
//			String firstIndexChar = Character.toString('z');
//			
//			_hmBgDoubleKeyFreqZ = new LinkedHashMap<BigramKey, Integer>();
//		
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//				_hmBgDoubleKeyFreqZ.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreqZ.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
//	
//	private void PopulateBgDoubleKeyFreq_(){
//		try{
//			String firstIndexChar = Character.toString('_');
//			
//			_hmBgDoubleKeyFreq_ = new LinkedHashMap<BigramKey, Integer>();
//			
//			System.out.println(firstIndexChar);	
//			String tokenizedBiGramFile= Directories.tokenizedBiGramDir+ firstIndexChar;
//			
//			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
//			DataInputStream in = new DataInputStream(fileIn);
//			
//			int numberOfRecords = in.readInt();
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			for(int i=0;i<numberOfRecords;i++){
//				int ugkey1 = in.readInt();
//				int ugkey2 = in.readInt();
//				int tokenIndex = in.readInt();
//				int frequency = in.readInt();
//				
//				_hmBgDoubleKeyFreq_.put(new BigramKey(ugkey1, ugkey2) , frequency);
//			}
//			
//			in.close();
//			fileIn.close();
//			
//			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
//			System.out.println(_hmBgDoubleKeyFreq_.size()+","+new Date().toString());
//		}
//		catch(Exception e){
//			
//		}
//
//	}
	
	public void PopulateBgSumKeyIdFreq() throws IOException{
		
		char ch;
		String firstIndexChar;
		
		_hmBgKeySumIds = new LinkedHashMap<Integer, Integer>();
		_bgFreqs = new ArrayList<Integer>();
		
		for( ch = 'a' ; ch <= 'a' ; ch++ ){
     		firstIndexChar = Character.toString(ch);
			System.out.println(firstIndexChar);	
			
			String tokenizedBiGramFile= PhraseDirectories.tokenizedBiGramDir+ firstIndexChar;
			
			
			FileInputStream fileIn = new FileInputStream(tokenizedBiGramFile);
			DataInputStream in = new DataInputStream(fileIn);
			
			int numberOfRecords = in.readInt();
			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
			for(int i=0;i<numberOfRecords;i++){
				int bgsumKey = in.readInt();
				int tokenIndex = in.readInt();
				int frequency = in.readInt();
		
				_hmBgKeySumIds.put(bgsumKey, tokenIndex);
			}
			
			in.close();
			fileIn.close();
			
			System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
			System.out.println(_hmBgKeySumIds.size()+","+new Date().toString());
		}
		
	}
	
	public void PopulateBgKey62IdFreq() throws Exception{ 
		try{
			char ch;
			String firstIndexChar;

			_hmBgKeyBase62Ids = new LinkedHashMap<String, Integer>();
			_bgFreqs = new ArrayList<Integer>();
			
			for( ch = 'a' ; ch <= 'a' ; ch++ ){
         		firstIndexChar = Character.toString(ch);
				System.out.println(firstIndexChar);	
				
				String tokenizedBiGramFile= PhraseDirectories.tokenizedBiGramDir+ firstIndexChar;
				
				
				String sCurrentLine;
				BufferedReader br = new BufferedReader(new FileReader(tokenizedBiGramFile));

				int numberOfRecords = Integer.parseInt(br.readLine());
				
				for(int i=0;i<numberOfRecords;i++){
					sCurrentLine = br.readLine();
					String []arr = sCurrentLine.split("\\s+");
					
					String bgKey62 = arr[0];
					int bgId = Integer.parseInt(arr[1]);
					int frequency = Integer.parseInt(arr[2]);
					
					_hmBgKeyBase62Ids.put(bgKey62, bgId);
					_bgFreqs.add(frequency);
				}
				
				br.close();
				
				System.out.println("total tokens="+numberOfRecords+","+tokenizedBiGramFile+","+new Date().toString());
				System.out.println(_hmBgKeyBase62Ids.size()+","+new Date().toString());
			}
			
		}
		catch(Exception e){
			throw e;
		}
	}

	public Map<BigramKey, int[]> populateBigramsWithIdFreqs(String fileName) throws IOException{
		Map<BigramKey, int[]> bgTokenWithIds = new LinkedHashMap<BigramKey, int[]>();
		
		File file = new File(fileName);
		
		FileInputStream fileIn = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fileIn);
		
		int numberOfRecords = in.readInt();
		//System.out.println("total tokens="+numberOfRecords+","+file.getAbsolutePath()+","+new Date().toString());
		for(int i=0;i<numberOfRecords;i++){
			int keyUni1 = in.readInt();
			int keyUni2 = in.readInt();
			int tokenIndex = in.readInt();
			int frequency = in.readInt();
	
			bgTokenWithIds.put(new BigramKey(keyUni1, keyUni2), new int[] {tokenIndex, frequency});
		}
		
		in.close();
		fileIn.close();
		
		//System.out.println(bgTokenWithIds.size()+","+new Date().toString());
		return bgTokenWithIds;
	}
	
	public Map<BigramKey, Integer> populateBigramsWithIds() throws IOException{
		File folder = new File(PhraseDirectories.tokenizedBiGramDir);
		File [] files = folder.listFiles();
		
		Map<BigramKey, Integer> bgTokenWithIds = new LinkedHashMap<BigramKey, Integer>();
		
		for(File file: files){
			FileInputStream fileIn = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fileIn);
			
			int numberOfRecords = in.readInt();
			System.out.println("total tokens="+numberOfRecords+","+file.getAbsolutePath()+","+new Date().toString());
			for(int i=0;i<numberOfRecords;i++){
				int keyUni1 = in.readInt();
				int keyUni2 = in.readInt();
				int tokenIndex = in.readInt();
				int frequency = in.readInt();
				
				bgTokenWithIds.put(new BigramKey(keyUni1, keyUni2), tokenIndex);
			}
			
			in.close();
			fileIn.close();
		}
		
		System.out.println(bgTokenWithIds.size()+","+new Date().toString());
		return bgTokenWithIds;
	}

	public ArrayList<Map<BigramKey, Integer>> populateBigramsWithIdsInParallel() throws IOException, InterruptedException, ExecutionException{
		File files [] = new File(PhraseDirectories.tokenizedBiGramDir).listFiles();
		int threads = files.length;
		
		ArrayList<Map<BigramKey, Integer>> allList = new ArrayList<Map<BigramKey,Integer>>(threads);
		
		
		
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		List<Future<Map<BigramKey, Integer>>> list = new ArrayList<Future<Map<BigramKey,Integer>>>(threads);
	
//		for(int i=0;i<threads;i++){
//			allList.add(null);
//			list.add(null);
//		}
		
		
		for(File bgFile: files){
			
			Callable<Map<BigramKey, Integer>> callable = new LoadBigramParallel(bgFile);
			Future<Map<BigramKey, Integer>> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
			int getFileIndex = Util.getIndexFromSingleCharFile(bgFile.getName())%threads;
            //list.add(getFileIndex, future);
			list.add(future);
		}
		
		for(Future<Map<BigramKey, Integer>> fut : list){
            try {
                //print the return value of Future, notice the output delay in console
                // because Future.get() waits for task to get completed
                //System.out.println(fut.get().size());
				allList.add(fut.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		/*for(int i=0;i<list.size();i++){
			Future<Map<BigramKey, Integer>> fut = list.get(i);
			allList.add(i, fut.get());
		}*/
		
		System.out.println(allList.size());
		
		executor.shutdownNow();
		// Wait until all threads are finish
		//executor.awaitTermination();		
		return allList;
	}
	
	public ArrayList<Map<BigramKey, Integer[]>> populateBigramsWithIdFreqInParallel() throws IOException{
		ArrayList<Map<BigramKey, Integer[]>> allList = new ArrayList<Map<BigramKey,Integer[]>>();
		
		File files [] = new File(PhraseDirectories.tokenizedBiGramDir).listFiles();
		int threads = files.length;
		
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		List<Future<Map<BigramKey, Integer[]>>> list = new ArrayList<Future<Map<BigramKey,Integer[]>>>();
		
		for(File bgFile: files){
			
			Callable<Map<BigramKey, Integer[]>> callable = new LoadBigramParallelIdFreq(bgFile);
			Future<Map<BigramKey, Integer[]>> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
            list.add(future);
			
		}
		
		for(Future<Map<BigramKey, Integer[]>> fut : list){
            try {
                //print the return value of Future, notice the output delay in console
                // because Future.get() waits for task to get completed
                //System.out.println(fut.get().size());
				allList.add(fut.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		System.out.println(allList.size());
		
		executor.shutdownNow();
		// Wait until all threads are finish
		//executor.awaitTermination();		
		return allList;
	}
	
	public ArrayList<Map<Integer, Integer>> populateBigramsWithOnlyIdFreqInParallel() throws IOException, InterruptedException, ExecutionException{
		ArrayList<Map<Integer, Integer>> allList = new ArrayList<Map<Integer,Integer>>();
		
		File files [] = new File(PhraseDirectories.tokenizedBiGramDir).listFiles();
		int threads = files.length;
		
		ExecutorService executor = Executors.newFixedThreadPool(threads);
		List<Future<Map<Integer, Integer>>> list = new ArrayList<Future<Map<Integer,Integer>>>(threads);
		
		for(File bgFile: files){
			
			Callable<Map<Integer, Integer>> callable = new LoadBigramParallelOnlyIdFreq(bgFile);
			Future<Map<Integer, Integer>> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
			int getFileIndex = Util.getIndexFromSingleCharFile(bgFile.getName())%threads;
            //list.add(getFileIndex, future);
			list.add( future);
			
		}
		
		for(int i=0; i<list.size();i++ ){
			Future<Map<Integer, Integer>> fut = list.get(i);
			allList.add(i,fut.get());
			System.out.println("Loading Total bg Tokens="+allList.get(i).size());
		}
		
		System.out.println(allList.size());
		
		executor.shutdownNow();
		// Wait until all threads are finish
		//executor.awaitTermination();		
		return allList;
	}

	
}


class LoadBigramParallel implements Callable<Map<BigramKey, Integer>> {
	
	File bgFile;
	
	public LoadBigramParallel(File bgFile){
		this.bgFile = bgFile;
	}
	
	public Map<BigramKey, Integer> call() throws Exception {
        //return the thread name executing this callable task
        Map<BigramKey, Integer> bgTokenWithIds = new LinkedHashMap<BigramKey, Integer>();
        
        FileInputStream fileIn = new FileInputStream(bgFile);
		DataInputStream in = new DataInputStream(fileIn);
		
		int numberOfRecords = in.readInt();
		System.out.println("total tokens="+numberOfRecords+","+bgFile.getAbsolutePath()+","+new Date().toString());
		for(int i=0;i<numberOfRecords;i++){
			int keyUni1 = in.readInt();
			int keyUni2 = in.readInt();
			int tokenIndex = in.readInt();
			
			bgTokenWithIds.put(new BigramKey(keyUni1, keyUni2), tokenIndex);
		}
		
		in.close();
		fileIn.close();
      
        return bgTokenWithIds;
    }
}


class LoadBigramParallelIdFreq implements Callable<Map<BigramKey, Integer[]>> {
	
	File bgFile;
	
	public LoadBigramParallelIdFreq(File bgFile){
		this.bgFile = bgFile;
	}
	
	public Map<BigramKey, Integer[]> call() throws Exception {
        //return the thread name executing this callable task
        Map<BigramKey, Integer[]> bgTokenWithIds = new LinkedHashMap<BigramKey, Integer[]>();
        
        FileInputStream fileIn = new FileInputStream(bgFile);
		DataInputStream in = new DataInputStream(fileIn);
		
		int numberOfRecords = in.readInt();
		System.out.println("total tokens="+numberOfRecords+","+bgFile.getAbsolutePath()+","+new Date().toString());
		for(int i=0;i<numberOfRecords;i++){
			int keyUni1 = in.readInt();
			int keyUni2 = in.readInt();
			int tokenIndex = in.readInt();
			int frequency = in.readInt();
			
			bgTokenWithIds.put(new BigramKey(keyUni1, keyUni2), new Integer[]{tokenIndex, frequency});
		}
		
		in.close();
		fileIn.close();
      
        return bgTokenWithIds;
    }
}

class LoadBigramParallelOnlyIdFreq implements Callable<Map<Integer, Integer>> {
	File bgFile;
	public LoadBigramParallelOnlyIdFreq(File bgFile) {
		this.bgFile = bgFile;
	}

	public Map<Integer, Integer> call() throws Exception {
		Map<Integer, Integer> bgTokenWithIds = new LinkedHashMap<Integer, Integer>();
        
        FileInputStream fileIn = new FileInputStream(bgFile);
		DataInputStream in = new DataInputStream(fileIn);
		
		int numberOfRecords = in.readInt();
		System.out.println("total tokens="+numberOfRecords+","+bgFile.getAbsolutePath()+","+new Date().toString());
		for(int i=0;i<numberOfRecords;i++){
			int keyUni1 = in.readInt();
			int keyUni2 = in.readInt();
			int tokenIndex = in.readInt();
			int frequency = in.readInt();
			
			bgTokenWithIds.put(tokenIndex, frequency);
		}
		
		in.close();
		fileIn.close();
      
        return bgTokenWithIds;
	}

}