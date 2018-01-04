package dal.relatedness.phrase.utils.trigram;

import dal.relatedness.phrase.constants.ContextFilterMode;

public class TrigramUtil {
//	public static String GetTriGramOrderFromFilePath(String path){
//		String order="";
//		
//		if(path.contains("123")) order= "123";
//		else if(path.contains("213")) order= "213";
//		else if(path.contains("312")) order= "312";
//		
//		return order;
//	}

	public static ContextFilterMode GetTriGramContextFilterMode(String path) {
		if(path.contains("123")) return ContextFilterMode.SUFFIX;
		else if(path.contains("213")) return ContextFilterMode.INFIX;
		else if(path.contains("312")) return ContextFilterMode.PREFIX;
		return null;
	}

	public static String GetContextRegx(ContextFilterMode contxtFltMode) {
		String regx = "";
		
		if(contxtFltMode == ContextFilterMode.SUFFIX){
			regx = "^[a-zA-Z]+\\s[a-zA-Z]+$";
		}
		else if(contxtFltMode == ContextFilterMode.INFIX){
			regx = "^[a-zA-Z]+\\s[a-zA-Z]+$";
		}
		else if(contxtFltMode == ContextFilterMode.PREFIX){
			regx = "^[a-zA-Z]+\\s[a-zA-Z]+$";
		}
		
		return regx;
	}
}
