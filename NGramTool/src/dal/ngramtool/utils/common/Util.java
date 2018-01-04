package dal.ngramtool.utils.common;

public class Util {

	public static String GetNgramFileName(String dir, String gram, String suffix, int fileIndex){
		return dir+suffix+"\\"+gram+"-"+suffix+"-"+ String.format("%04d", fileIndex);
	}
}
