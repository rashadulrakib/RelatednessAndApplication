package dal.dimensionality.reduction.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import dal.clustering.document.shared.DocClusterConstant;
import dal.dimensionality.reduction.tsne.DimReductionConstants;
import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.text.compute.trwp.TextRelatednessTrwpConstant;

public class DimReductionFourgramUtil {
	
	 private int vsm4gmSize =0;
	 private HashMap<Integer, int[]> hmPhIdContextIds ;
	 
	 public HashMap<Integer, int[]> getfourGmPhIdContextIds() {
			return hmPhIdContextIds;
	 }
	 
	 public int getVsm4gmSize(){
		 return vsm4gmSize;
	 }
	 
//	 public TreeSet<Integer> getfourGmVsmSingleDim() {
//			return vsm4gDim;
//	 }
	 
//		public void PopulateVsmSingleDimSizeAndPhIdCtxIdList(	ArrayList<Integer> alBgFreqs) {
//			
//			try{
//				
//				hmPhIdContextIds = new HashMap<Integer, int[]>();
//				vsm4gDim =new HashSet<Integer>();
//				
//				int totalPhCounts = 0;
//				long totalPhCtxSize = 0;
//				
//				for(char ch='a'; ch<='z';ch++){
//					System.out.println(new Date().toString());
//					System.out.println(Runtime.getRuntime().freeMemory());
//					
//					String fourGTokenizedFile = PhraseDirectories.tokenizedFourGramDir+String.valueOf(ch);
//					
//					System.out.println("load" + fourGTokenizedFile);
//					
//					long totalContexts = 0;	
//					int phCounts = 0;
//					int phCtxSum = 0;
//					int maxPhCtxSize = 0;
//					int minPhCtxSize=Integer.MAX_VALUE;
//					
//					HashSet<Integer> vsmDimPh = new HashSet<Integer>();
//					
//					DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fourGTokenizedFile)));
//					
//					while(in.available()>0){
//						int phid = in.readInt();
//						int contextSize = in.readInt();
//					
//						boolean flagIsPh = false;
//					
//						if(contextSize>=DimReductionConstants.MinContextSize){
//							if( alBgFreqs.get(phid)>=TextRelConstants.meanBgFreq+ TextRelConstants.std){
//								phCounts++;
//								phCtxSum=phCtxSum+contextSize;
//								if(maxPhCtxSize<contextSize){
//									maxPhCtxSize =contextSize; 
//								}
//								
//								if(minPhCtxSize>contextSize){
//									minPhCtxSize = contextSize;
//								}
//								totalPhCtxSize = totalPhCtxSize+ contextSize;
//								flagIsPh = true;
//							}
//						}
//							
//						totalContexts=totalContexts+contextSize;
//						
//						int maxCtxSize = contextSize>DimReductionConstants.MaxContextSize4gm?DimReductionConstants.MaxContextSize4gm:contextSize;
//						
//						int [] ctxIds = new  int[maxCtxSize];
//						
//						for(int i=0;i<contextSize;i++){
//							int ctxId = in.readInt();
//							int ctxFreq = in.readInt();
//							
//							if(i>=maxCtxSize){
//								continue;
//							}
//					
//							if(flagIsPh){
//								vsmDimPh.add(ctxId);
//								ctxIds[i]=ctxId;
//							}
//								
//						}
//						
//						if(flagIsPh){
//							hmPhIdContextIds.put(phid, ctxIds);
//						}
//					}
//					
//					in.close();
//					
//					totalPhCounts = totalPhCounts+phCounts;
//					
//					System.out.println("phCounts="+phCounts+", maxPhCtxSize="+maxPhCtxSize+", minPhCtxSize="+minPhCtxSize+", phCtxSum="+phCtxSum+", vsmDimPh="+vsmDimPh.size());
//					
//					vsm4gDim.addAll(vsmDimPh);
//					vsmDimPh.clear();
//					vsmDimPh = null;
//				}
//				
//				alBgFreqs.clear();
//				alBgFreqs = null;
//				
//				System.out.println("CreateVsmFourgramDimension finished., totalPhCounts="+totalPhCounts+",totalPhCtxSize="+totalPhCtxSize+", vsmDim="+vsm4gDim.size());
//				vsm4gmSize = vsm4gDim.size();
//				vsm4gDim.clear();
//				vsm4gDim =null;
//				
//			}
//			catch(Exception e){
//				e.printStackTrace();
//			}
//		}
		
	
	public void PopulateVsmSingleDimSizeAndPhIdCtxIdList(ArrayList<Integer> tempPhIds) {
		
		try{
			
			hmPhIdContextIds = new HashMap<Integer, int[]>();
			HashSet<Integer> vsm4gDimTemp  =new HashSet<Integer>();
			
			long totalPhCtxSize = 0;
			
			for(char ch='a'; ch<='z';ch++){
				System.out.println(new Date().toString());
				System.out.println("free memory="+Runtime.getRuntime().freeMemory());
				
				String fourGTokenizedFile = PhraseDirectories.tokenizedFourGramDir+String.valueOf(ch);
				
				System.out.println("load" + fourGTokenizedFile);
				
				long totalContexts = 0;	
			
				int phCtxSum = 0;
				int maxPhCtxSize = 0;
				int minPhCtxSize=Integer.MAX_VALUE;
				
				HashSet<Integer> vsmDimPh = new HashSet<Integer>();
				
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fourGTokenizedFile)));
				
				while(in.available()>0){
					int phid = in.readInt();
					int contextSize = in.readInt();
						
					totalContexts=totalContexts+contextSize;
					
					int maxCtxSize = contextSize ;//>DimReductionConstants.MaxContextSize4gm?DimReductionConstants.MaxContextSize4gm:contextSize;
					
					int [] ctxIds = new  int[maxCtxSize];
					
					for(int i=0;i<contextSize;i++){
						int ctxId = in.readInt();
						int ctxFreq = in.readInt();
						
						if(i>=maxCtxSize){
							continue;
						}
				
						if(tempPhIds.contains(phid)){
							vsmDimPh.add(ctxId);
							ctxIds[i]=ctxId;
						}
							
					}
					
					if(tempPhIds.contains(phid)){
						hmPhIdContextIds.put(phid, ctxIds);
					}
				}
				
				in.close();
				
				System.out.println(" maxPhCtxSize="+maxPhCtxSize+", minPhCtxSize="+minPhCtxSize+", phCtxSum="+phCtxSum+", vsmDimPh="+vsmDimPh.size());
				
				vsm4gDimTemp.addAll(vsmDimPh);
				vsmDimPh.clear();
				vsmDimPh = null;
			}
			
			System.out.println("CreateVsmFourgramDimension finished. ,totalPhCtxSize="+totalPhCtxSize+", vsmDim="+vsm4gDimTemp.size()+", total phs count="+hmPhIdContextIds.size());
			vsm4gmSize = vsm4gDimTemp.size();
			vsm4gDimTemp.clear();
			vsm4gDimTemp =null;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public TreeSet<Integer> CreateVsmFourgramDimension(ArrayList< Integer> alBgFreqs){
		TreeSet<Integer> vsmDim = new TreeSet<Integer>();
		try{
			
			int totalPhCounts = 0;
			long totalPhCtxSize = 0;
			
			
			for(char ch='a'; ch<='z';ch++){
				System.out.println(new Date().toString());
				System.out.println("free memory="+Runtime.getRuntime().freeMemory());
				
				String fourGTokenizedFile = PhraseDirectories.tokenizedFourGramDir+String.valueOf(ch);
				
				System.out.println("load" + fourGTokenizedFile);
				
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fourGTokenizedFile)));
				
				long totalContexts = 0;	
//				long lineCount = 0;
//				long maxCtxSizeGlobal = 0;
//				
//				long skippedContxets = 0;
				
				int phCounts = 0;
				int phCtxSum = 0;
				int maxPhCtxSize = 0;
				int minPhCtxSize=Integer.MAX_VALUE;
				
				TreeSet<Integer> vsmDimPh = new TreeSet<Integer>();
				
				while(in.available()>0){
					int phid = in.readInt();
					int contextSize = in.readInt();
					
					boolean flagIsPh = false;
					
					if(contextSize>=DimReductionConstants.MinContextSize)
					if( alBgFreqs.get(phid)>=  TextRelatednessTrwpConstant.MeanBgFreq+ TextRelatednessTrwpConstant.Std){
						phCounts++;
						phCtxSum=phCtxSum+contextSize;
						if(maxPhCtxSize<contextSize){
							maxPhCtxSize =contextSize; 
						}
						
						if(minPhCtxSize>contextSize){
							minPhCtxSize = contextSize;
						}
						totalPhCtxSize = totalPhCtxSize+ contextSize;
						flagIsPh = true;
						//System.out.println("phid="+phid+", contextSize="+contextSize+", alBgFreqs.get(phid)="+alBgFreqs.get(phid));
					}
					
//					if(contextSize>=TsneConstants.MaxContextSize4gm){
//						skippedContxets++;
//					}
//					else 	if(alBgFreqs.get(phid)<0 || ( alBgFreqs.get(phid)<TextRelConstants.meanBgFreq+ TextRelConstants.std)){
//						skippedContxets++;
//					}
//					else{
//						lineCount++;
//					}
					
//					if(maxCtxSizeGlobal<contextSize){
//						maxCtxSizeGlobal = contextSize;
//					}
					
					totalContexts=totalContexts+contextSize;
							
					for(int i=0;i<contextSize;i++){
						int ctxId = in.readInt();
						int ctxFreq = in.readInt();
						
//						if(alBgFreqs.get(phid)<0 || ( alBgFreqs.get(phid)<TextRelConstants.meanBgFreq+ TextRelConstants.std)){
//							continue;
//						}
//						
						if(i>=DimReductionConstants.MaxContextSize4gm){
							continue;
						}
//						
//						if(contextSize<TsneConstants.MinContextSize){
//							continue;
//						}
//						
						if(flagIsPh)
							vsmDimPh.add(ctxId);
					}
					
					
					
//					if(lineCount%1000000==0){
//						System.out.println("lineCount="+lineCount+", fourgramVSMCtxSize="+vsmDim.size());
//					}
						
					
				}
				
//				System.out.println("totalContexts="+totalContexts+", avg context length="
//					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
//					+", PhWithLargeContxets="+skippedContxets+", lineCount="+lineCount+", fourgramVSMCtxSize="+vsmDim.size());
				
				in.close();
				
				totalPhCounts = totalPhCounts+phCounts;
				
				System.out.println("phCounts="+phCounts+", maxPhCtxSize="+maxPhCtxSize+", minPhCtxSize="+minPhCtxSize+", phCtxSum="+phCtxSum+", vsmDimPh="+vsmDimPh.size());
				
				vsmDim.addAll(vsmDimPh);
				vsmDimPh.clear();
				vsmDimPh = null;
			}
			
			System.out.println("CreateVsmFourgramDimension finished., totalPhCounts="+totalPhCounts+",totalPhCtxSize="+totalPhCtxSize+", vsmDim="+vsmDim.size());
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return vsmDim;
	}
	
	public TreeSet<Integer> CreateVsmFourgramDimension(){
		TreeSet<Integer> vsmDim = new TreeSet<Integer>();
		try{
			
			for(char ch='a'; ch<='z';ch++){
				System.out.println(new Date().toString());
				System.out.println(Runtime.getRuntime().freeMemory());
				
				String fourGTokenizedFile = PhraseDirectories.tokenizedFourGramDir+String.valueOf(ch);
				
				System.out.println("load" + fourGTokenizedFile);
				
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(fourGTokenizedFile)));
				
				long totalContexts = 0;	
				long lineCount = 0;
				long maxCtxSizeGlobal = 0;
				
				long skippedContxets = 0;
				
				while(in.available()>0){
					int phid = in.readInt();
					int contextSize = in.readInt();
					
					lineCount++;
					
					if(contextSize>=DimReductionConstants.MaxContextSize4gm){
						skippedContxets++;
					}
					
					if(maxCtxSizeGlobal<contextSize){
						maxCtxSizeGlobal = contextSize;
					}
					
					totalContexts=totalContexts+contextSize;
							
					for(int i=0;i<contextSize;i++){
						int ctxId = in.readInt();
						int ctxFreq = in.readInt();
						
						if(i>=DimReductionConstants.MaxContextSize4gm){
							continue;
						}
						
						if(contextSize<DimReductionConstants.MinContextSize){
							continue;
						}
						
						vsmDim.add(ctxId);
					}
					
					if(lineCount%1000000==0){
						System.out.println("lineCount="+lineCount+", fourgramVSMCtxSize="+vsmDim.size());
					}
						
					
				}
				
				System.out.println("totalContexts="+totalContexts+", avg context length="
					+(double)totalContexts/(double)lineCount+", maxCtxSizeGlobal "+maxCtxSizeGlobal
					+", PhWithLargeContxets="+skippedContxets+", lineCount="+lineCount+", fourgramVSMCtxSize="+vsmDim.size());
				
				in.close();
				
			}
			
			System.out.println("CreateVsmFourgramDimension finished.");
			
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return vsmDim;
	}

}
