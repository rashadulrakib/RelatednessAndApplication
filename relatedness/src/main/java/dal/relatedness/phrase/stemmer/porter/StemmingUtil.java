package dal.relatedness.phrase.stemmer.porter;

public class StemmingUtil {

//    Porter objPorter;
//
//    public StemmingUtil() {
//        objPorter = new Porter();
//    }

    public static String stemPhrase(String s) {
        String ph = "";

        String[] arr = s.split("\\s+");
        for (String str : arr) {
            ph = ph + " " + Porter.stripAffixes(str).trim();
        }

        return ph.trim();
    }
}