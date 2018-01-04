/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.phSim.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author administrator
 */
public class NGramCountUtil {

    String ngDir = "/home/administrator/rakib/google-n-gram/n-gram-indexed/";

    public NGramCountUtil(String ngDir) {
        this.ngDir = ngDir;
    }

    public double GetNGramCount(String ph, String contextRegx, String fileSuffix) throws Exception {
        double count = 0.0;

        //StemmingUtil objSetm = new StemmingUtil();
        //ph = objSetm.stemPhrase(ph);
        
        ContextUtil objCu = new ContextUtil(ngDir);
        
        ArrayList<String> nGFiles = objCu.getGreedyContextFiles(ph, fileSuffix, 0);

        for (String filepath : nGFiles) {
            File f = new File(filepath);
            BufferedReader br = new BufferedReader(new FileReader(f));
            String s;

            while ((s = br.readLine()) != null) {
                s = s.trim().toLowerCase();
                if (Pattern.compile(contextRegx, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(s).find()) {
                    
                    /*String[] arr = s.split("\\s+");
                    String extractPhrase="";
                    for(int i=0;i<arr.length-1;i++){
                        extractPhrase=extractPhrase+" "+arr[i];
                    }
                    extractPhrase = objSetm.stemPhrase(extractPhrase.trim());
                    if(ph.equals(extractPhrase)){
                        count = count + Double.parseDouble(arr[arr.length - 1]);
                    }*/
                     String[] arr = s.split("\\s+");
                     count = count + Double.parseDouble(arr[arr.length - 1]);
                }
            }

            br.close();
        }

        return count;
    }
}
