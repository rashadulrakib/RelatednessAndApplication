package dal.ngramtool.fourgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import dal.ngramtool.constants.Directories;
import dal.ngramtool.utils.common.Util;
import dal.ngramtool.utils.fourgram.FourgramUtil;

public class Split4Gram {

	@SuppressWarnings("resource")
	public void SplitByFirst2CharFromFirst2Grams(String fileSuffix, int fileStartIndex, int fileEndIndex) throws Exception{
		try{
		
			String fourgramDir = Directories.fourgramDir;
			String lastIndexedFullFilePath = "";
			FileWriter fwIndexedFullFilePath = null;
			BufferedWriter bwIndexedFullFilePath = null;
			
			for(int i=fileStartIndex ; i<=fileEndIndex;i++){
				String fileName = Util.GetNgramFileName(fourgramDir, "4gm", fileSuffix, i);
				File file = new File(fileName);
				
				if(!file.exists()){
					continue;
				}
				
				System.out.println(fileName);
				
				BufferedReader br = new BufferedReader(new FileReader(file));
		        String line;
		        
		        int lineCount = 0;
		        
		        while ((line = br.readLine()) != null) {
		           line = line.trim().toLowerCase();
		           if(line.isEmpty()) continue;
		           
		           if(FourgramUtil.IsValidSplit4Gram(line)){
		        	   
		        	   String fourArr [] = line.split("\\s+");
		        	   
		        	   String indexFileSubDir = fourArr[0].charAt(0)+"\\"+fourArr[0].charAt(1)+"\\"+fourArr[1].charAt(0)+"\\"+fourArr[1].charAt(1)+"\\";
		        	   String indexedDir = FourgramUtil.GetIndexedDir(Directories.fourGramIndexedDir,indexFileSubDir);
		        	   
		        	   File fourgramIndexDir = new File(indexedDir);
		        	   if(!fourgramIndexDir.exists()){
		        		   if(!fourgramIndexDir.mkdirs()){
		        			   throw new Exception(indexedDir+" creation failed.");
		        		   }
		        	   }
		        	  
		        	   String indexedSubFileName =  FourgramUtil.GetIndexedSubFileName(fourArr); 
		        	   String indexedFullFilePath = indexedDir+indexedSubFileName+"-"+fileSuffix;
		        	   //System.out.println(indexedFullFilePath);
		        	   
		        	   if(!lastIndexedFullFilePath.equals(indexedFullFilePath)){
		        		   
		        		   if(bwIndexedFullFilePath!=null){
		        			   bwIndexedFullFilePath.close();
		        		   }
		        		   
		        		   if(fwIndexedFullFilePath!=null){
		        			   fwIndexedFullFilePath.close();
		        		   }
		        		   
		        		   File fileIndexedFullFilePath = new File(indexedFullFilePath);
		        		   
		        		   if (!fileIndexedFullFilePath.exists()) {
			        		   fileIndexedFullFilePath.createNewFile();
			        	   }
		        		   
		        		   fwIndexedFullFilePath = new FileWriter(fileIndexedFullFilePath.getAbsoluteFile(), true);
		        		   bwIndexedFullFilePath = new BufferedWriter(fwIndexedFullFilePath);
		        		   
		        		   bwIndexedFullFilePath.write(line+"\n");
		        	   }
		        	   else{
		        		   bwIndexedFullFilePath.write(line+"\n");
		        	   }
		        	   
//		        	   File fileIndexedFullFilePath = new File(indexedFullFilePath);
//		        	   
//		        	   if (!fileIndexedFullFilePath.exists()) {
//		        		   fileIndexedFullFilePath.createNewFile();
//		        	   }
//		        	   
//		        	   FileWriter fwIndexedFullFilePath = new FileWriter(fileIndexedFullFilePath.getAbsoluteFile(), true);
//		        	   BufferedWriter bwIndexedFullFilePath = new BufferedWriter(fwIndexedFullFilePath);
//
//		        	   bwIndexedFullFilePath.write(line+"\n");
//
//		        	   bwIndexedFullFilePath.close();
//		        	   fwIndexedFullFilePath.close();
		        	   
		        	   lastIndexedFullFilePath = indexedFullFilePath;
		        	   
		        	   if(++lineCount % 2000000==0){
		        		   System.out.println(lineCount);
		        	   }

		           }
		        }
		        
		        System.out.println("Total lines="+lineCount);
		        
		        br.close();
			}
			
			if(bwIndexedFullFilePath!=null){
 			   bwIndexedFullFilePath.close();
 		   }
 		   
 		   if(fwIndexedFullFilePath!=null){
 			   fwIndexedFullFilePath.close();
 		   }
		}
		catch(Exception e){
			throw e;
		}
	}
}
