package dal.clustering.document.agnews;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import dal.clustering.document.shared.DocClusterConstant;
import dal.clustering.document.shared.PairSim;
import dal.clustering.document.shared.entities.ClusterDocumentShared;
import dal.relatedness.phrase.stemmer.porter.StemmingUtil;
import dal.relatedness.text.utils.TextRelatednessUtilCommon;

public class ProcessAgNewsDocument extends ClusterDocumentShared {
	ArrayList<String> phrasesOfAText;
	 
	 HashMap<String, Double> phSimChacheScores = new HashMap<String, Double>();
	 
	 HashMap<String, Integer> wphFreqsCache  = new HashMap<String, Integer>();
	 
	 public void Process() {
			try{

				//step1 ag_news
				
//				loadPhSimScores();
//				
//				LinkedHashMap<Integer, ArrayList<String>>  docsLabelTitleBody  = loadAllDocsAgNews();
//				System.out.println("docsLabelTitleBody.size="+docsLabelTitleBody.size());
//				ArrayList<String> docs = loadDocsFromDifferentCategory(docsLabelTitleBody);
//				
//				ArrayList<String> preprocesseddocs = docClusterUtil.PreProcessDocs(docs);
//				
//				System.out.println(new Date().toString());
//				
//				ArrayList<ArrayList<Double>> simMatrix  = constructSimMatrixTrwp(preprocesseddocs);
//				
//				writeSimMatrix(simMatrix);
//				
//				System.out.println("total phs="+wphFreqsCache.size());
//				System.out.println("total ph pairs="+phSimChacheScores.size());
//				
//				System.out.println(new Date().toString());
				
				//end step1 ag_news 
				
				
				
				//writeSimMatrixWholeFromFile(); //step2
				
				//w2vdc block
				loadAllDocAgNewsByW2VecListAndWriteToArff();
				//end w2vdc block
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

	private void loadAllDocAgNewsByW2VecListAndWriteToArff() {
		try{
		  List<String[]> aldocsBodeyLabel = new ArrayList<String[]>();
			int w2vecdimension = 300;
			
			BufferedReader br =  new BufferedReader(new FileReader(DocClusterConstant.AgNewsDocsFile));
			HashSet<String> uniquewords = new HashSet<String>();
			
			  String line = "";
			   while((line = br.readLine())!=null){
				   
				   line = line.trim();
				   if(line.isEmpty()) continue;
				   
				   String [] arrLabelTitleBody = line.split("\\t");
				   
				   if(arrLabelTitleBody.length!=3)
					   continue;
				   
				   String label = arrLabelTitleBody[0];
				   String body =  arrLabelTitleBody[1]+ " " +arrLabelTitleBody[2];
				   
				   body = docClusterUtil.PerformPreprocess(body);
			        ArrayList<String> processed = docClusterUtil.RemoveStopWord(body);
				   
			        uniquewords.addAll(processed); //populate the unique words from the text corpus
				   
			        String arr[] = new String[2];
			        arr[0]= docClusterUtil.ConvertArrayListToString(processed);
			        arr[1] = label;
				   
			        aldocsBodeyLabel.add(arr);
			   }
			
			br.close();
			
			long seed = System.nanoTime();
			Collections.shuffle(aldocsBodeyLabel, new Random(seed));
			
			//temp
			//aldocsBodeyLabel = aldocsBodeyLabel.subList(0, 50000);
			//end temp
			
			br = new BufferedReader(new FileReader(DocClusterConstant.InputGlobalWordEmbeddingFile));
	           
			String text="";
			HashMap<String, double[]> w2vec = new HashMap<String, double[]>();
			
            while ((text = br.readLine()) != null) {
            	text = text.trim().toLowerCase();
            	
            	String [] arr = text.split("\\s+");
            	String EmbeddingWord = arr[0];
            	
            	if(uniquewords.contains(EmbeddingWord)){
            		String [] vecs = text.replaceAll(EmbeddingWord, "").trim().split("\\s+");
            		double [] vecDoubles = new double[vecs.length];
            		for(int i=0; i< vecs.length;i++){
            			vecDoubles[i] = Double.parseDouble(vecs[i]);
            		}
            		w2vec.put(EmbeddingWord, vecDoubles);
            	}
            }
           
            br.close();
            
            
            ArrayList<double []> allavgVecs = new ArrayList<double[]>();
            ArrayList<String> labels = new ArrayList<String>();
            for(String [] bodeyLabel: aldocsBodeyLabel){
            	String body = bodeyLabel[0];
            	String label = bodeyLabel[1];
            	labels.add(label);
            	
            	String arr[] = body.split("\\s+");
            	double [] avgVec = new double[w2vecdimension];
            	
            	for(String word: arr){
            		if(w2vec.containsKey(word)){
            			double[] wordVec = w2vec.get(word); 
            			for(int i=0;i<avgVec.length;i++){
            				avgVec[i]=avgVec[i]+ wordVec[i];
            			}
            		}
            		
            	}
            	
            	//averaging avgvec
            	for(int i=0;i<avgVec.length;i++){
            		avgVec[i]=avgVec[i]/(double)arr.length;
            	}
            	//end averaging avgvec
            	
            	 allavgVecs.add(avgVec);
            }
            
            
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(DocClusterConstant.AgNewsW2VecArffFile));
            
            bw.write("@relation AgnewsW2Vec\n\n");
            for(int i=0;i< w2vecdimension;i++){
            	bw.write("@attribute ftr"+i+" NUMERIC\n");
            }
            bw.write("@attribute Category {1,2,3,4}\n\n");
            bw.write("@data\n");
            
            int index =0;
            for(double[] wordVec : allavgVecs)
            {
            	 for(double val: wordVec){
            		 bw.write(val +",");
            	 }
            	 bw.write(labels.get(index).trim()+"\n");
            	 
            	 index++;
            }
            
            bw.close();
            //end
			
            System.out.println("loadAllDocAgNewsByW2VecListAndWriteToArff end");
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	private void writeSimMatrixWholeFromFile() {
			try{
			 
			   BufferedReader br  = new BufferedReader(new FileReader("D:\\PhD\\dr.norbert\\dataset\\shorttext\\ag_news\\docSim-trwp-400"));
				
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
					   
					   squrematrix[i][i]=1.0;
					   //squrematrix[i][i]=0.0;
					   
					   for(int j=0;j<leftLength;j++){
						   if(j==i) continue;
						   squrematrix[i][j] = squrematrix[j][i];
					   }
					   
					   for(int j=0;j<row.size();j++){
						   squrematrix[i][j+ leftLength] = row.get(j);
					   }
			   }
			   
			   squrematrix[alMatrix.size()][alMatrix.size()] = 1.0;
			   //squrematrix[alMatrix.size()][alMatrix.size()] = 0.0;
			   
			   for(int col = 0;col<alMatrix.size();col++){
				   squrematrix[alMatrix.size()][col] =   squrematrix[col][alMatrix.size()];
			   }
			   
			   BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\PhD\\dr.norbert\\dataset\\shorttext\\ag_news\\docSim-trwp-400-matrix"));
			   
			   for(int i=0;i<squrematrix.length;i++ ){
				   for(int j=0;j< squrematrix.length;j++){
					   System.out.print(1-squrematrix[i][j]+" ");
					   bw.write(1-squrematrix[i][j]+" ");
					   
					  // System.out.print(squrematrix[i][j]+" ");
					   //bw.write(squrematrix[i][j]+" ");
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
				BufferedWriter bw = new BufferedWriter(new FileWriter(DocClusterConstant.AgNewsDocSimFile));
				
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
	 
	private void loadPhSimScores() {
			try{
				
				if(new File(DocClusterConstant.wordPhPairssimAgNewsFile).exists()){
					BufferedReader br =  new BufferedReader(new FileReader(DocClusterConstant.wordPhPairssimAgNewsFile));
					
					String line="";
					while((line=br.readLine()) != null) {
				        line = line.trim();
				        String [] arr = line.split(",");
				        String stem1 = StemmingUtil.stemPhrase(arr[0]);
				        String stem2 = StemmingUtil.stemPhrase(arr[1]);
				        phSimChacheScores.put(stem1+","+ stem2, Double.parseDouble(arr[2]));
				    }
					
					br.close();
				}
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
						 
						// System.out.println("Doc Index="+i);
						 
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
	                    String stemwph1=  StemmingUtil.stemPhrase(phWord1);
	                    String stemwph2 = StemmingUtil.stemPhrase(phWord2);
	                    
	                    String stemmedkey =stemwph1+","+stemwph2;
	                    double simOverlap = 0.0;
						
	                   if(!notUsefulWPhPairsStemmed.contains(stemmedkey)) 
	                   {
	                	 
	                	   
	                        if (phSimChacheScores.containsKey(stemmedkey)) {
	                            simOverlap =phSimChacheScores.get(stemmedkey) * weightForWords * weightForWords;
	                        } else {
	                        	stemmedkey = StemmingUtil.stemPhrase(phWord2)+","+StemmingUtil.stemPhrase(phWord1);
	                            if (phSimChacheScores.containsKey(stemmedkey)) {
	                                simOverlap =phSimChacheScores.get(stemmedkey) * weightForWords * weightForWords;
	                            }
	                            else{
	                            	  //if(weightForWords > 1.0)
	                            	  {
	                            		  simOverlap = phraseRelatednessTokenized.ComputeFastRelExternal(stemwph1, stemwph2)* weightForWords * weightForWords;
	                            	//simOverlap = phraseRelatednessTokenized.ComputeFastRelExternal(stemwph1, stemwph2);
	  	                            	//phSimChacheScores.put(stemwph1+","+stemwph2, simOverlap);
	       	                		   //System.out.println(stemwph1+","+stemwph2 +","+ simOverlap);
	       	                	   }
	                            	
	                            }
	                        }
	                    }
	                    
	                    simPairList.add(new PairSim(stemmedkey, simOverlap));
	                }
	                t1t2simPairList.add(simPairList);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
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
							int ph2Index = phraseRelatednessTokenized.phRelComputeUtil.GetPhIndexAlHm(wordsInPh2, stemwph2);

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
	        if(phrasesOfAText!=null){
	        	phrasesOfAText.clear();
	        	phrasesOfAText = null;
	        }
			
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
	           e.printStackTrace();
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
	            e.printStackTrace();
	        }
	        return str.trim();
	    }
	 
	 private ArrayList<String> loadDocsFromDifferentCategory(LinkedHashMap<Integer, ArrayList<String>> docsLabelTitleBody) {
		 ArrayList<String> docs = new ArrayList<String>();
		 
		 try{
			for(int key: docsLabelTitleBody.keySet()){
				
				
				docs.addAll(docsLabelTitleBody.get(key).subList(0, 100));
				System.out.println("key="+key+", size="+docsLabelTitleBody.get(key).size()+ "docs="+docs.size());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		 
		 return docs;
	}

	//loading only title or body or both
	 private LinkedHashMap<Integer, ArrayList<String>>  loadAllDocsAgNews() {
		 LinkedHashMap<Integer, ArrayList<String>>  docsLabelTitleBody = new LinkedHashMap<Integer, ArrayList<String>>();
		
		try{
			BufferedReader br =  new BufferedReader(new FileReader(DocClusterConstant.AgNewsDocsFile));
			
			String line="";
			while((line=br.readLine()) != null) {
		        line = line.trim();
		      
		        if(line.isEmpty()) continue;
		        
		        String arr[] = line.split("\t"); //0=lable, 1= title, 2=body
		        
		        int label= Integer.parseInt(arr[0]);
		        		        
		        if(!docsLabelTitleBody.containsKey(label)){
		        	 ArrayList<String> al = new ArrayList<String>();
		        	 al.add(arr[1]); 
		        	 
		        	docsLabelTitleBody.put(label, al);
		        }
		        else{
		        	ArrayList<String> al = docsLabelTitleBody.get(label);
		        	al.add(arr[1]);
		        	docsLabelTitleBody.put(label, al);
		        }
		    }
			
			br.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return docsLabelTitleBody;
	}
}
