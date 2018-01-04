package dal.relatedness.phrase.bigram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.entities.bigram.BigramKey;
import dal.relatedness.phrase.unigram.LoadUnigram;

public class BigramGeneratorFromFourGramContext {

	Map<String, Integer> unigramTokenWithIds;
	LoadBigram loadBgObj;
	TreeSet<String> processedBigrams;
	BufferedWriter bw = null;
	FileWriter fw = null;
	int notBigramExistCount = 0;
	int notUniExistCount =0;
	int notBigramExistPhraseCount=0;
	
	public void GenerateBigramFromFiltered4gmContexts(){
		try{
			loadBgObj = new LoadBigram();
			processedBigrams = new TreeSet<String>();
			unigramTokenWithIds = new LoadUnigram().loadTokenizedUniGramsWithId();
			loadBgObj.PopulateBgDoubleKeyFreq();
			
			File file = new File(PhraseDirectories.biGramIndexedProcessedContextDir+"w");
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			
			
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			
			RecursivelyPopulate4gmWithProcessedContexts(PhraseDirectories.fourGramIndexedProcessedContextDir+"w\\", 'w');
			System.out.println("RecursivelyPopulate4gmWithProcessedContexts finished."+new Date().toString());
//			processedBigrams.add("zeromancer ,");
//			processedBigrams.add("znz ycn");
//			processedBigrams.add("zombie 2pac");
//			processedBigrams.add("zombie aaliyah");
//			processedBigrams.add("zombie aaliyah");
//			processedBigrams.add("zombie bush");
			if (bw != null)
				bw.close();

			if (fw != null)
				fw.close();
			
			System.out.println(processedBigrams.size());
			
			//SaveProcessedBigrams('z');
			
			System.out.println("finish saving."+new Date().toString());
			System.out.println("notBigramExistCount="+notBigramExistCount);
			System.out.println("notUniExistCount="+notUniExistCount);
			System.out.println("notBigramExistPhraseCount="+notBigramExistPhraseCount);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
		
	}
	
//	private void SaveProcessedBigrams(char fileChar) throws FileNotFoundException {
//		try{
//			PrintWriter pr = new PrintWriter(Directories.biGramIndexedProcessedContextDir+String.valueOf(fileChar));
//			
//			for(String bg: processedBigrams){
//				String arr[] = bg.split("\\s+");
//				if(!unigramTokenWithIds.containsKey(arr[0])
//						|| !unigramTokenWithIds.containsKey(arr[1])){
//					
//					System.out.println(bg+" unigram- not exist.");
//					notUniExistCount++;
//					continue;
//				}
//				int uniId1 = unigramTokenWithIds.get(arr[0]);
//				int uniId2 = unigramTokenWithIds.get(arr[1]);
//				BigramKey bgKey = new BigramKey(uniId1, uniId2);
//				
//				if(!loadBgObj.getBgDoubleKeyFreqZ().containsKey(bgKey)){
//					System.out.println(bg+" bigram -not exist.");
//					notBigramExistCount++;
//					continue;
//				}
//				pr.println(bg+" "+loadBgObj.getBgDoubleKeyFreqZ().get(bgKey));
//			}
//			
//			pr.close();
//		}
//		catch(Exception e)
//		{
//			System.out.println(e.toString());
//		}
//	}
//	
	private int getBgFreq(String bg){
		
		String arr[] = bg.split("\\s+");
		if(!unigramTokenWithIds.containsKey(arr[0])
				|| !unigramTokenWithIds.containsKey(arr[1])){
			
			System.out.println(bg+" unigram- not exist.");
			notUniExistCount++;
			return -1;
		}
		int uniId1 = unigramTokenWithIds.get(arr[0]);
		int uniId2 = unigramTokenWithIds.get(arr[1]);
		BigramKey bgKey = new BigramKey(uniId1, uniId2);
		
		if(!loadBgObj.getBgDoubleKeyFreqW().containsKey(bgKey)){
			System.out.println(bg+" bigram -not exist.");
			notBigramExistCount++;
			return -1;
		}
		
		return loadBgObj.getBgDoubleKeyFreqW().get(bgKey);
	}

	private void RecursivelyPopulate4gmWithProcessedContexts( String path, char firstChar ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
            	RecursivelyPopulate4gmWithProcessedContexts( f.getAbsolutePath(), firstChar );
                //System.out.println( "Dir:" + f.getAbsoluteFile() );
            }
            else {
                //System.out.println( "File:" + f.getAbsoluteFile() );
                PopulateBgFromLoaded4gWithContexts(f, firstChar);
                
            }
        }
    }

	private void PopulateBgFromLoaded4gWithContexts(File file, char firstChar) {
		
		//TreeMap<String, TreeMap<String, Integer>> bgPhraseWithContexts = new TreeMap<String, TreeMap<String,Integer>>();
		
		try{
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((sCurrentLine = br.readLine()) != null) {
				
				String [] arr = sCurrentLine.split("\\s+");
				String bggram = arr[0]+" "+arr[1];
				
				if(bggram.charAt(0)==firstChar){
					if(!processedBigrams.contains(bggram)){
						processedBigrams.add(bggram);
						int bgFreq = getBgFreq(bggram);
						if(bgFreq>0){
							bw.write(bggram +" "+bgFreq+"\n");
						}
						else{
							notBigramExistPhraseCount++;
						}
					}
				}
				
				
//				int numberContexts = Integer.parseInt(arr[2]);
//				for(int i=1;i<=numberContexts;i++){
//					bggram = arr[i*3]+" "+arr[i*3+1];
//					if(bggram.charAt(0)==firstChar){
//						if(!processedBigrams.contains(bggram)){
//							processedBigrams.add(bggram);
//							int bgFreq = getBgFreq(bggram);
//							if(bgFreq>0){
//								bw.write(bggram +" "+bgFreq+"\n");
//							}
//						}
//					}
//				}
				
				if(processedBigrams.size()>0 && processedBigrams.size()%100000==0){
                	System.out.println("number of bigrams="+processedBigrams.size());
                }
			}
			br.close();
		}
		catch(Exception e){
			
		}
		
		//return bgPhraseWithContexts;
	}
}
