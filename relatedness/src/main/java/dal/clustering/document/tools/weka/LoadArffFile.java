package dal.clustering.document.tools.weka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

public class LoadArffFile {
String fileName;
	
	public LoadArffFile(String fileName){
		this.fileName = fileName;
	}
	
	public Instances load () throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		ArffReader arff = new ArffReader(reader); 
		reader.close();
		Instances insts = arff.getData();
		//insts.setClassIndex(insts.numAttributes()-1);
		return insts;
	}
}
