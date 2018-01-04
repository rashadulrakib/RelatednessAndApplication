package dal.ngramtool.utils.fourgram;

public class FourgramUtil {

	public static boolean IsValidSplit4Gram(String fourgram) {
		String arr [] = fourgram.split("\\s+");
		
		if(arr.length!=5) return false;
		
		if(!IsValidSplitByFirst2Char(arr[0]) || !IsValidSplitByFirst2Char(arr[1])) return false;
		
		return true;
	}

	private static boolean IsValidSplitByFirst2Char(String gram) {
		
		if(gram.length()<2) return false;
		
		if((gram.charAt(0)>='a' && gram.charAt(0)<='z')
				&& (gram.charAt(1)>='a' && gram.charAt(1)<='z')){
			return true;
		}
		
		return false;
	}

	public static String GetIndexedDir(String fourgramfileindexingdir,
			String indexFileName) {
		return fourgramfileindexingdir+indexFileName;
	}

	public static String GetIndexedSubFileName(String[] fourArr) {
		return String.valueOf(fourArr[0].charAt(0))+
 			   String.valueOf(fourArr[0].charAt(1))+
			   String.valueOf(fourArr[1].charAt(0))+
			   String.valueOf(fourArr[1].charAt(1));
	}

}
