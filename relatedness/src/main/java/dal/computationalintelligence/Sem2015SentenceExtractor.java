package dal.computationalintelligence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Sem2015SentenceExtractor {

	public void ExtractSentencePairs(String inputFile, String groudTruthFile, String outputFileNewSentPairs,
			String groudTruthFileNew){
		try{
			BufferedReader br = new BufferedReader(new FileReader(inputFile));
			String str = "";
			ArrayList<String[]> sentPairs = new  ArrayList<String[]>();
			
			while ((str = br.readLine()) != null) {
            	str = str.trim().toLowerCase();
            	String [] arr = str.split("\t");
            	String sent1 = arr[0].trim();
            	String sent2 = arr[1].trim();
            	sentPairs.add(new String[]{sent1, sent2});
			}
			br.close();
			
			ArrayList<String> gsScores = new ArrayList<String>();
			br = new BufferedReader(new FileReader(groudTruthFile));
			while ((str = br.readLine()) != null) {
				gsScores.add(str.trim());
			}
			br.close();
			
			if(gsScores.size()==sentPairs.size()){
				
				PrintWriter pr1 = new PrintWriter(outputFileNewSentPairs);
				PrintWriter pr2 = new PrintWriter(groudTruthFileNew);
				
				for(int i=0;i<gsScores.size();i++){
					String gsScore =gsScores.get(i);
					if(gsScore.equals("")){
						continue;
					}
					
					pr1.println(sentPairs.get(i)[0]);
					pr1.println(sentPairs.get(i)[1]);
					
					pr2.println(gsScores.get(i));
				}
				
				pr1.close();
				pr2.close();
				
				System.out.println("End Sentence Extraction");
			}
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public void ExtractOutputScoresFromTrwpSem2015(String inputGroudTruthFile, String input_ProgramOutputScoreFile,
			String outputSemEval2015FinalScore){
		try{
			String str;
			ArrayList<String> gsScores = new ArrayList<String>();
			
			BufferedReader br = new BufferedReader(new FileReader(inputGroudTruthFile));
			while ((str = br.readLine()) != null) {
				gsScores.add(str.trim());
			}
			br.close();
			
			ArrayList<String> programTrWPScores = new ArrayList<String>();
			br = new BufferedReader(new FileReader(input_ProgramOutputScoreFile));
			while ((str = br.readLine()) != null) {
				programTrWPScores.add(str.trim());
			}
			br.close();
			
			
			if(gsScores.size() == programTrWPScores.size()){
				
				PrintWriter pr = new PrintWriter(outputSemEval2015FinalScore);
				
				for(int i=0;i<gsScores.size();i++){
					String gsScore =gsScores.get(i);
					if(gsScore.equals("")){
						continue;
					}
					pr.println(programTrWPScores.get(i).split("\\s+")[0]);
				}
				
				pr.close();
			}
			else{
				System.out.println("Inconsistent lines ExtractOutputScoresFromTrwpSem2015");
			}
			
			System.out.println("End ExtractOutputScoresFromTrwpSem2015");
		}catch(Exception e){
			System.out.println(e.toString());
		}
	}
}
