/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.phSim.Stemming;

/**
 *
 * @author administrator
 */
public class StemmingUtil {

    Porter objPorter;

    public StemmingUtil() {
        objPorter = new Porter();
    }

    public String stemPhrase(String s) {
        String ph = "";

        String[] arr = s.split("\\s+");
        for (String str : arr) {
            ph = ph + " " + objPorter.stripAffixes(str).trim();
        }

        return ph.trim();
    }
}
