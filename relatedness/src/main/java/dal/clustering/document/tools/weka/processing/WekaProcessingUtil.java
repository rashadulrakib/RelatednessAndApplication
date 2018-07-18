package dal.clustering.document.tools.weka.processing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Set;

import dal.clustering.document.shared.entities.InstanceW2Vec;
import dal.utils.common.general.UtilsShared;

public class WekaProcessingUtil {

	public void WriteTextToArffFile(String arffFile, ArrayList<String[]> aldocsBodeyLabel, String relation) {
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(arffFile));
			
			bw.write("@relation "+relation+"\n\n");
			bw.write("@attribute Text string"+"\n");
			bw.write("@attribute Category {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20}"+"\n\n");
			
			bw.write("@data"+"\n");
			
			for(String[] bodyLabel: aldocsBodeyLabel ){
				bw.write("'"+bodyLabel[0]+"',"+bodyLabel[1]+"\n");
			}
			
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void WriteW2VecsToArffFile(String arffFile, int dimension, Set<String> labels, ArrayList<InstanceW2Vec> instants){
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(arffFile));
            
            bw.write("@relation BiomedicalDocsW2Vec\n\n");
            for(int i=0;i< dimension;i++){
            	bw.write("@attribute ftr"+i+" NUMERIC\n");
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("@attribute Category {");
            
            ArrayList<String> alLables = UtilsShared.ConvertCollectionToArrayList(labels);            
            for(int i=0;i<alLables.size()-1;i++){
            	sb.append(alLables.get(i)+",");
            }
            sb.append(alLables.get(alLables.size()-1)+"}\n\n");            
            bw.write(sb.toString().trim());
            
            bw.write("@data\n");            
            for(InstanceW2Vec inst: instants){
            	for(double ftr: inst.Features){
            		bw.write(ftr +",");
            	}
            	bw.write(inst.OriginalLabel+"\n");
            }
            
            bw.close();
            //end
			
            System.out.println("loadAllDocsBiomedicalByW2VecListAndWriteToArff end");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
