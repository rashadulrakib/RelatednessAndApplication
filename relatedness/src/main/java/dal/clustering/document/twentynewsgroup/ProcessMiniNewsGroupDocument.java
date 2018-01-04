package dal.clustering.document.twentynewsgroup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import dal.clustering.document.shared.DocClusterConstant;
import dal.clustering.document.shared.PairSim;
import dal.clustering.document.shared.TfIdfMatrixGenerator;
import dal.clustering.document.shared.entities.ClusterDocumentShared;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.text.utils.TextRelatednessUtilCommon;
import dal.utils.common.compute.ComputeUtil;

public class ProcessMiniNewsGroupDocument  extends ClusterDocumentShared {
	ArrayList<String> phrasesOfAText;
	 
	 HashMap<String, Double> phSimChacheScores = new HashMap<String, Double>();
	 
	 HashMap<String, Integer> wphFreqsCache  = new HashMap<String, Integer>();
	 
	 
	 
		public void ClusterDocsNGramBasedSimilarity() {
			try{
				
				//setp1 for mini-newsgroup dataset
				//loadPhSimScores();
				
				//ArrayList<String> docs = loadAllDocsMiniNewsGroup();
				//ArrayList<String> preprocessedDocs = preProcessMiniNewsGroupsDocs(docs);
				//ArrayList<String> preprocessedDocs = sampleMiniNewsgroupDocs(preprocessedDocs);
				
				//System.out.println(new Date().toString());
				
				//ArrayList<ArrayList<Double>> simMatrix  =constructSimMatrixTrwp(preprocessedDocs);
				//ArrayList<ArrayList<Double>> simMatrix  =constructSimMatrixCosine(preprocessedDocs);
				//ArrayList<ArrayList<Double>> simMatrix  =constructSimMatrixEuclidian(preprocessedDocs);
				
				//System.out.println(new Date().toString());
				
				//writeSimMatrix(simMatrix);
				
				//System.out.println("total phs="+wphFreqsCache.size());
				//System.out.println("total ph pairs="+phSimChacheScores.size());
				//end setp1
				
				//writeSimMatrixWholeFromFile(); //step2
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}


		private ArrayList<ArrayList<Double>> constructSimMatrixEuclidian(ArrayList<String> preprocessedDocs) {
			ArrayList<ArrayList<Double>> simMatrix = new ArrayList<ArrayList<Double>>();
			try{
		
				System.out.println("total docs="+ preprocessedDocs.size());
				
				List<List<String>> documents  = new ArrayList<List<String>>();
				
				for(int i=0;i< preprocessedDocs.size();i++){
				
					ArrayList<String> document =  docClusterUtil.ConvertAarryToArrayList( preprocessedDocs.get(i).split("\\s+"));
					document = docClusterUtil.RemoveStopWord(document);
					documents.add(document);
				}
				
				ArrayList<HashMap<String, Double>> tfIdfs = new TfIdfMatrixGenerator().ConstructTfIdfList(documents);
				
				for(int i=0;i<tfIdfs.size();i++){
					ArrayList<Double> row = new ArrayList<Double>();
					for(int j=i+1;j< tfIdfs.size();j++){
						double cosSim = ComputeUtil.ComputeEuclidianDistance(tfIdfs.get(i), tfIdfs.get(j));
						row.add(cosSim);
					}
					simMatrix.add(row);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return simMatrix;
		}


		private ArrayList<ArrayList<Double>> constructSimMatrixCosine(ArrayList<String> preprocessedDocs) {
			ArrayList<ArrayList<Double>> simMatrix = new ArrayList<ArrayList<Double>>();
			try{

				System.out.println("total docs="+ preprocessedDocs.size());
				
				List<List<String>> documents  = new ArrayList<List<String>>();
				
				for(int i=0;i< preprocessedDocs.size();i++){
				
					ArrayList<String> document =  docClusterUtil.ConvertAarryToArrayList( preprocessedDocs.get(i).split("\\s+"));
					document = docClusterUtil.RemoveStopWord(document);
					documents.add(document);
				}
				
				ArrayList<HashMap<String, Double>> tfIdfs = new TfIdfMatrixGenerator().ConstructTfIdfList(documents);
				
				for(int i=0;i<tfIdfs.size();i++){
					ArrayList<Double> row = new ArrayList<Double>();
					for(int j=i+1;j< tfIdfs.size();j++){
						double cosSim = docClusterUtil.ComputeCosineSImilarity(tfIdfs.get(i), tfIdfs.get(j));
						row.add(cosSim);
					}
					simMatrix.add(row);
				}
				
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return simMatrix;
		}


		private void writeSimMatrixWholeFromFile() {
			try{
			 
			   BufferedReader br  = new BufferedReader(new FileReader("D:\\PhD\\dr.norbert\\dataset\\mininews-group\\docSim-euclidian-100"));
				
			   ArrayList<ArrayList<Double>> alMatrix = new ArrayList<ArrayList<Double>>();

			   String line = "";
			   while((line = br.readLine())!=null){
				   
				   line = line.trim();
				   if(line.isEmpty()) continue;
				   
				   String [] arr = line.split("\\s+");
				   
				   ArrayList<Double> row = new ArrayList<Double>();
				   for(String str: arr){
					   row.add(Double.parseDouble(str));
				   }
				   
				   alMatrix.add(row);
				   
			   }
			   
			   br.close();
			   
			   double [][] squrematrix = new double[alMatrix.size()+1][alMatrix.size()+1]; 
				
			   for(int i=0;i<alMatrix.size();i++){
					   ArrayList<Double> row = alMatrix.get(i);
					   
					   int leftLength = squrematrix.length-row.size();
					   
					   //squrematrix[i][i]=1.0;
					   squrematrix[i][i]=0.0;
					   
					   for(int j=0;j<leftLength;j++){
						   if(j==i) continue;
						   squrematrix[i][j] = squrematrix[j][i];
					   }
					   
					   for(int j=0;j<row.size();j++){
						   squrematrix[i][j+ leftLength] = row.get(j);
					   }
			   }
			   
			   //squrematrix[alMatrix.size()][alMatrix.size()] = 1.0;
			   squrematrix[alMatrix.size()][alMatrix.size()] = 0.0;
			   
			   for(int col = 0;col<alMatrix.size();col++){
				   squrematrix[alMatrix.size()][col] =   squrematrix[col][alMatrix.size()];
			   }
			   
			   BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\mininews-group\\docSim-euclidian-100-matrix"));
			   
			   for(int i=0;i<squrematrix.length;i++ ){
				   for(int j=0;j< squrematrix.length;j++){
					   //System.out.print(1-squrematrix[i][j]+" ");
					   //bw.write(1-squrematrix[i][j]+" ");
					   
					   System.out.print(squrematrix[i][j]+" ");
					   bw.write(squrematrix[i][j]+" ");
				   }
				   System.out.println();
				   bw.write("\n");
			   }
			 
			   bw.close();
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		private void writeSimMatrix(ArrayList<ArrayList<Double>> simMatrix ) {
			try{
				BufferedWriter bw = new BufferedWriter(new FileWriter(DocClusterConstant.MiniNewsGroupDocSimFile));
				
				for(int i=0;i<simMatrix.size();i++){
					ArrayList<Double> rowsimScores = simMatrix.get(i); 
					for(int j=0; j< rowsimScores.size();j++){
						System.out.print(rowsimScores.get(j)+" ");
						bw.write(rowsimScores.get(j)+" ");
					}
					System.out.println();
					bw.write("\n");
				}
				
				bw.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		private ArrayList<ArrayList<Double>> constructSimMatrixTrwp(ArrayList<String> preprocessedDocs) {
			
			ArrayList<ArrayList<Double>> simMatrix = new ArrayList<ArrayList<Double>>();
			
			try{
			
				System.out.println("total docs="+ preprocessedDocs.size());
				
				for(int i=0;i< preprocessedDocs.size();i++){
					//for(int i=0;i<3;i++){
						
						ArrayList<Double> rowsimScores = new ArrayList<Double>();
						
						String text1 = preprocessedDocs.get(i);
						
						ArrayList<String> cands1 = docClusterUtil.splitByStopWord(text1);
						 ArrayList<String> phs1 = docClusterUtil.SplitPhrases(generatePhsByFreq(cands1));
						 
						  ArrayList<String> phWordList1 = docClusterUtil.CombineWordPhs(phs1, cands1);
						 
						// System.out.println("text1="+text1);
						 //System.out.println("phs1="+phs1+"\n........................................");
						 
						 System.out.println("Doc Index="+i);
						 
						for(int j=i+1;j<preprocessedDocs.size();j++){
						//for(int j=i+1;j<3;j++){
							
							
							String text2 = preprocessedDocs.get(j);
							//System.out.println("--text1="+text1);
							//System.out.println("--text2="+text2);
							
			                ArrayList<String> cands2 = docClusterUtil.splitByStopWord(text2);
			                
			                if(cands1.size()<=0 || cands2.size()<=0){
			                	rowsimScores.add(0.0);
			                	continue;
			                }
			                
			               
			                ArrayList<String> phs2 = docClusterUtil.SplitPhrases(generatePhsByFreq(cands2));
		                   ArrayList<String> phWordList2 = docClusterUtil.CombineWordPhs(phs2, cands2);
		                   
		                   ArrayList<String>  newPhWordList1 = docClusterUtil.SplitSuperPhrases(phWordList1, phWordList2);
		                   ArrayList<String>  newPhWordList2 = docClusterUtil.SplitSuperPhrases(phWordList2, phWordList1);
		                   
		                   if (newPhWordList1.size() > newPhWordList2.size()) {
		                       ArrayList<String> temp = newPhWordList1;
		                       newPhWordList1 = newPhWordList2;
		                       newPhWordList2 = temp;
		                   }
		                   
		                   ArrayList<String> commonPhWords = docClusterUtil.GetCommonPhWordsByStemming(newPhWordList1, newPhWordList2);
		                   ArrayList<String> getRestPhWords1 = docClusterUtil.GetRestPhWords(newPhWordList1, commonPhWords);
		                   ArrayList<String> getRestPhWords2 = docClusterUtil.GetRestPhWords(newPhWordList2, commonPhWords);
		                   
		                   //System.out.println("commonPhWords="+commonPhWords);
		                   //System.out.println("getRestPhWords1="+getRestPhWords1);
		                  //System.out.println("getRestPhWords2="+getRestPhWords2);
		                   
		                 //temp: works better than non-pruning word-ph sim pairs
		                   HashSet<String> notUsefulWPhPairsStemmed = getNotUsefulWPhPairsStemmed(phWordList1, phWordList2);
		                   //temp
		                   
		                   ArrayList<ArrayList<PairSim>> t1t2simPairList = null;
		                   if (getRestPhWords1.size() > 0 && getRestPhWords2.size() > 0) {
		                       t1t2simPairList = getWeightedSimilarityMatrix(getRestPhWords1, getRestPhWords2, notUsefulWPhPairsStemmed);
		                       
		                   }
		                   
		                   double textSImscore=0;
		                   
		                   if(t1t2simPairList!=null){
		                   textSImscore = TextRelatednessUtilCommon.ComputeSimilarityFromWeightedMatrixBySTD(t1t2simPairList, docClusterUtil.GetCommonWeight(commonPhWords), 
		                		   docClusterUtil.GetTextSize(newPhWordList1), docClusterUtil.GetTextSize(newPhWordList2), false);
		       			
			       			if (Double.isNaN(textSImscore)){
			       			textSImscore=0.0;
			       			}
			       				
			       			//System.out.println("textSImscore="+textSImscore);
		                   }
		                   
		                   rowsimScores.add(textSImscore);
		                   
						}
						
						//System.out.println("-------------------------");
						simMatrix.add(rowsimScores);
					}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return simMatrix;
		}
		
		private ArrayList<ArrayList<PairSim>> getWeightedSimilarityMatrix(ArrayList<String> restPhWords1, ArrayList<String> restPhWords2, HashSet<String> notUsefulWPhPairsStemmed) {
	        ArrayList<ArrayList<PairSim>> t1t2simPairList = new ArrayList<ArrayList<PairSim>>();
	        try {
	            for (String phWord1 : restPhWords1) {
	                ArrayList<PairSim> simPairList = new ArrayList<PairSim>();
	                for (String phWord2 : restPhWords2) {

	                    int wordsInPh1 = phWord1.split("\\s+").length;
	                    int wordsInPh2 = phWord2.split("\\s+").length;
	                    double totalWords = wordsInPh1 + wordsInPh2;
	                    double weightForWords = totalWords / 2;

	                    //String key = phWord1 + "," + phWord2;
	                    String stemmedkey = StemmingUtil.stemPhrase(phWord1)+","+StemmingUtil.stemPhrase(phWord2);
	                    double simOverlap = 0.0;
						
						/*Pattern p1 = Pattern.compile("[0-9]+");
						Matcher m1 = p1.matcher(phWord1);
						
						Pattern p2 = Pattern.compile("[0-9]+");
						Matcher m2 = p2.matcher(phWord2);
						
						if (m1.find() || m2.find()){
							simPairList.add(new cPairSim(key, simOverlap));
							continue;	
						}*/ 

//	                    if (weightForWords == 1.0) {
//	                        if (hmPairSimGTMWord.containsKey(key)) {
//	                            simOverlap = hmPairSimGTMWord.get(key);  //+pairSimOverlapPh.get(key);
//	                        } else {
//	                            key = phWord2 + "," + phWord1;
//	                            if (hmPairSimGTMWord.containsKey(key)) {
//	                                simOverlap = hmPairSimGTMWord.get(key); //+pairSimOverlapPh.get(key);
//	                            } else {
//	                                System.out.println("Not found Phrase gtm:" + key);
//	                            }
//	                        }
//	                    } 
//	                    else 
	                   if(!notUsefulWPhPairsStemmed.contains(stemmedkey)) 
	                   {
	                        if (phSimChacheScores.containsKey(stemmedkey)) {
	                            simOverlap =phSimChacheScores.get(stemmedkey) * weightForWords * weightForWords;
	                        } else {
	                            //key = phWord2 + "," + phWord1;
	                        	stemmedkey = StemmingUtil.stemPhrase(phWord2)+","+StemmingUtil.stemPhrase(phWord1);
	                            if (phSimChacheScores.containsKey(stemmedkey)) {
	                                simOverlap =phSimChacheScores.get(stemmedkey) * weightForWords * weightForWords;
	                            } 
	                        }
	                    }
	                    
	                    simPairList.add(new PairSim(stemmedkey, simOverlap));
	                }
	                t1t2simPairList.add(simPairList);
	            }
	        } catch (Exception e) {
	            System.out.println("error: getWeightedSimilarityMatrix->" + e.toString());
	        }
	        return t1t2simPairList;
	    }
		
		private HashSet<String> getNotUsefulWPhPairsStemmed(	ArrayList<String> restPhWords1, ArrayList<String> restPhWords2) {
			HashSet<String> notUsefulWPhPairs = new HashSet<String>();
			try {

				double sumSumRaio = 0;
				double totalPairs = 0;
				ArrayList<Double> alPairsVal = new ArrayList<Double>();
				ArrayList<String> alPairsPhrases = new ArrayList<String>();

				for (String phWord1 : restPhWords1) {

					for (String phWord2 : restPhWords2) {

						int wordsInPh1 = phWord1.split("\\s+").length;
						int wordsInPh2 = phWord2.split("\\s+").length;

						String stemwph1 = StemmingUtil.stemPhrase(phWord1);
						String stemwph2 = StemmingUtil.stemPhrase(phWord2);

						String key = stemwph1 + "," + stemwph2;

						if (stemwph1.equals(stemwph2)) {
							continue;
						} else {

							// phraseRelatednessTokenized.loadNonTokenizeUnigram.g

							int ph1Index =phraseRelatednessTokenized.phRelComputeUtil
									.GetPhIndexAlHm(wordsInPh1, stemwph1);
							int ph2Index = phraseRelatednessTokenized.phRelComputeUtil
									.GetPhIndexAlHm(wordsInPh2, stemwph2);

							if (ph1Index < 0 || ph2Index < 0) {
								continue;
							}

							int countPh1 = phraseRelatednessTokenized.phRelComputeUtil
									.GetPhFreqAl(wordsInPh1, ph1Index);
							int countPh2 = phraseRelatednessTokenized.phRelComputeUtil
									.GetPhFreqAl(wordsInPh2, ph2Index);

							if (countPh1 <= 0 || countPh2 <= 0) {
								continue;
							}
							
							//System.out.println("count1="+countPh1+","+phWord1+", "+"count2="+countPh2+","+phWord2);

							double sumRatio = (double) Math.min(countPh1, countPh2)
									/ (double) Math.max(countPh1, countPh2)
									* (double) (countPh1 + countPh2);
							sumSumRaio = sumSumRaio + sumRatio;
							totalPairs++;
							alPairsVal.add(sumSumRaio);
							alPairsPhrases.add(key);
						}

					}
				}

				double mean = sumSumRaio / totalPairs;
				double variaceSum = 0;

				for (double x : alPairsVal) {
					variaceSum = variaceSum + Math.pow(x - mean, 2);
				}

				double sd = Math.sqrt(variaceSum / alPairsVal.size());

				if (alPairsVal.size() == alPairsPhrases.size()) {
					for (int i = 0; i < alPairsVal.size(); i++) {
						double x = alPairsVal.get(i);

						if (x <= mean - 1.0 * sd || x >= mean + 1.0 * sd) {
							notUsefulWPhPairs.add(alPairsPhrases.get(i));
							//System.out.println("alPairsPhrases.get(i)="+alPairsPhrases.get(i));
						}

					}
				}

				//System.out.println("alPairsPhrases.size=" + alPairsPhrases.size()
				//		+ ",notUsefulWPhPairs.size=" + notUsefulWPhPairs.size());

			} catch (Exception e) {
				e.printStackTrace();
			}

			return notUsefulWPhPairs;
		}
		
		private ArrayList<String> generatePhsByFreq(ArrayList<String> candStrs) {
	        phrasesOfAText = new ArrayList<String>();
	        try {
	            for (String s : candStrs) {
	                s = s.trim();
	                if (s.isEmpty()) {
	                    continue;
	                }

	                if (s.split("\\s+").length < 2) {
	                    continue;
	                }

	                binarySplitCandidate(s);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return phrasesOfAText;
	    }
		
		private void binarySplitCandidate(String s) {
	        try {

	            if (s.trim().isEmpty()) {
	                return;
	            }

	            String[] ws = s.split("\\s+");

	                 
	                    if (ws.length >= 2) {
	                        double maxfreq = 0;
	                        String maxBg = "";
	                        int i1 = 0;
	                        int i2 = 0;
	                        for (int i = 0; i < ws.length - 1; i++) {
	                            String bg = (ws[i] + " " + ws[i + 1]).trim();
	                            if (bg.isEmpty()) {
	                                continue;
	                            }
	                            //if (hmBiGramFreq.containsKey(bg)) 
	                            
	                            double bgFreq = 0;
                            	
                            	if(wphFreqsCache.containsKey(bg)){
                            		bgFreq = wphFreqsCache.get(bg);
                            	}
                            	else{
                            		bgFreq = getBgFreq(StemmingUtil.stemPhrase(bg));
                            		wphFreqsCache.put(bg, (int)bgFreq);
                            	}
	                                if (bgFreq >= maxfreq && bgFreq >= (double) (DocClusterConstant.MeanBgFreq + DocClusterConstant.Std)) {
	                                    maxfreq = bgFreq;
	                                    maxBg = bg;
	                                    i1 = i;
	                                    i2 = i + 1;
	                                }
	                            
	                        }

	                        if (maxfreq > 0 && !maxBg.isEmpty() && i1 != i2) {
	                            phrasesOfAText.add(maxBg);
	                            String sLeft = getSubStr(ws, 0, i1 - 1);
	                            String sRight = getSubStr(ws, i2 + 2, ws.length);
	                            binarySplitCandidate(sLeft);
	                            binarySplitCandidate(sRight);
	                        }
	                    }
	                
	         

	        } catch (Exception e) {
	            System.out.println("error: binarySplitCandidate->" + e.toString());
	        }
	    }
		
		private double getBgFreq(String bg) {
			
			double bgFreq = 0;
			
			try{
			
				int bgIndex = phraseRelatednessTokenized.phRelComputeUtil.GetPhIndexAlHm(2, bg);
				
				if(bgIndex<0){
					return 0;
				}
				
				bgFreq = phraseRelatednessTokenized.phRelComputeUtil.GetPhFreqAl(2, bgIndex);				
			}
			catch (Exception e) {
	        	e.printStackTrace();
	        }
			
			return bgFreq;
		}
		
		private String getSubStr(String[] ws, int start, int end) {
	        String str = "";
	        try {
	            for (int i = start; i < end; i++) {
	                str = str + " " + ws[i];
	            }
	        } catch (Exception e) {
	            System.out.println("error: getSubStr->" + e.toString());
	        }
	        return str.trim();
	    }
		
		 private ArrayList<String> sampleMiniNewsgroupDocs(ArrayList<String> preprocessedDocs) {
				
			 ArrayList<String> al = new ArrayList<String>();
			 int offset = 100;
			 
			 try{
				for(int i=0;i<5;i++){
					al.add(preprocessedDocs.get(i*offset+4));
					al.add(preprocessedDocs.get(i*offset+5));
					al.add(preprocessedDocs.get(i*offset+6));
					al.add(preprocessedDocs.get(i*offset+7));
					al.add(preprocessedDocs.get(i*offset+8));
					al.add(preprocessedDocs.get(i*offset+9));
					al.add(preprocessedDocs.get(i*offset+10));
					al.add(preprocessedDocs.get(i*offset+11));
					al.add(preprocessedDocs.get(i*offset+12));
					al.add(preprocessedDocs.get(i*offset+13));
					al.add(preprocessedDocs.get(i*offset+14));
					al.add(preprocessedDocs.get(i*offset+15));
					al.add(preprocessedDocs.get(i*offset+16));
					al.add(preprocessedDocs.get(i*offset+17));
					al.add(preprocessedDocs.get(i*offset+18));
					al.add(preprocessedDocs.get(i*offset+19));
					al.add(preprocessedDocs.get(i*offset+20));
					al.add(preprocessedDocs.get(i*offset+21));
					al.add(preprocessedDocs.get(i*offset+22));
					al.add(preprocessedDocs.get(i*offset+23));


				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			 
			 return al;
		}
		
		
		
		private void loadPhSimScores() {
			try{
				
				if(new File(DocClusterConstant.wordPhPairssimMiniNewsGroupFile).exists()){
					BufferedReader br =  new BufferedReader(new FileReader(DocClusterConstant.wordPhPairssimMiniNewsGroupFile));
					
					String line="";
					while((line=br.readLine()) != null) {
				        line = line.trim();
				        String [] arr = line.split(",");
				        phSimChacheScores.put(arr[0]+","+ arr[1], Double.parseDouble(arr[2]));
				    }
					
					br.close();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		private ArrayList<String> loadAllDocsMiniNewsGroup() {
			ArrayList<String> docs = new ArrayList<String>();
			
			try{
				BufferedReader br =  new BufferedReader(new FileReader(DocClusterConstant.MiniNewsGroupDocsFile));
				
				String line="";
				while((line=br.readLine()) != null) {
			        line = line.trim();
			        docs.add(line);
			    }
				
				br.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return docs;
		}
}
