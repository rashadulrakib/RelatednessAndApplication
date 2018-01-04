/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.phSim.context;

import dal.phSim.Stemming.StemmingUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author administrator
 */
public class ContextUtil {

    String ngDir = "/home/administrator/rakib/google-n-gram/n-gram-indexed/";
    int maxFolderDepth = 4;
    ArrayList<String> allCharIndex;

    public ContextUtil(String ngDir) {
        this.ngDir = ngDir;
    }

    public HashMap<String, Double> getContexts(String ph, String contextRegx, String fileSuffix, int[] contextIndexes, int[] phIndexes) throws Exception {
        HashMap<String, Double> hmContextCounts = new HashMap<String, Double>();
        try {
            //ArrayList<String> contextFiles = getContextFiles(ph, fileSuffix, 2);
            //ArrayList<String> contextFiles = getExactContextFiles(ph, fileSuffix, 2);
            
            StemmingUtil objStem = new StemmingUtil();
            
            ArrayList<String> contextFiles = getGreedyContextFiles(ph, fileSuffix, 2);
            ArrayList<String> al = extractByPattern(contextRegx, contextFiles);

            ph = objStem.stemPhrase(ph);
            
            for (String s : al) {
                String[] arr = s.split("\\s");
                
                String extractPh = "";
                for (int i = 0; i < phIndexes.length; i++) {
                    extractPh = extractPh + " " + arr[phIndexes[i]].trim();
                }
                extractPh =  objStem.stemPhrase(extractPh.trim());
                
                
                if(!extractPh.equals(ph)){
                    continue;
                }
                
                String keyContext = "";
                for (int i = 0; i < contextIndexes.length; i++) {
                    keyContext = keyContext + " " + arr[contextIndexes[i]].trim();
                }
                keyContext = keyContext.trim();

                if (!hmContextCounts.containsKey(keyContext)) {
                    hmContextCounts.put(keyContext, Double.parseDouble(arr[arr.length - 1].trim()));
                } else {
                    hmContextCounts.put(keyContext, hmContextCounts.get(keyContext) + Double.parseDouble(arr[arr.length - 1].trim()));
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return hmContextCounts;
    }

    public ArrayList<String> getGreedyContextFiles(String ph, String fileSuffix, int contextSize) throws Exception {
        ArrayList<String> files = new ArrayList<String>();

        try {
            ph = ph.trim().toLowerCase();
            String[] words = ph.split("\\s+");
            int nGramsUsedInFileName = words.length;
            String ngFolder = getFolder(nGramsUsedInFileName + contextSize);
            int maxCharInIndex = maxFolderDepth/nGramsUsedInFileName;
           
            String subFolder = "";
            String fileName = "";
            for (int i = 0; i < nGramsUsedInFileName; i++) {
                String ng = words[i].trim();
                for (int j = 0; j < ng.length() && j < maxCharInIndex; j++) {
                    subFolder = subFolder + ng.charAt(j) + "\\";
                    fileName = fileName + ng.charAt(j);
                }
            }

            if (!fileName.isEmpty() && !subFolder.isEmpty()) {
                String filePath = ngDir + ngFolder + subFolder + fileName + fileSuffix;
                if (new File(filePath).exists()) {
                    files.add(filePath);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }

        return files;
    }

    /*public ArrayList<String> getExactContextFiles(String ph, String fileSuffix, int contextSize) throws Exception {
     ArrayList<String> files = new ArrayList<String>();
     try {
     ph = ph.trim().toLowerCase();
     String[] words = ph.split("\\s+");
     int totalWords = words.length;
     String ngFolder = getFolder(totalWords + contextSize);
     String fileName = "";

     String subFolder = "";

     if (totalWords == 1) {
     int wLegnth = words[0].trim().length();

     if (contextSize == 0) {
     subFolder = words[0].charAt(0) + "/" + words[0].charAt(1) + "/" + words[0].charAt(2) + "/";
     fileName = subFolder.replaceAll("/", "");
     } else if (contextSize == 2) {
     if (wLegnth == 3) {
     subFolder = words[0].charAt(0) + "/" + words[0].charAt(1) + "/" + words[0].charAt(2) + "/";
     fileName = subFolder.replaceAll("/", "");
     } else if (wLegnth == 4) {
     subFolder = words[0].charAt(0) + "/" + words[0].charAt(1) + "/"
     + words[0].charAt(2) + "/" + words[0].charAt(3) + "/";
     fileName = subFolder.replaceAll("/", "");
     } else if (wLegnth >= 5) {
     subFolder = words[0].charAt(0) + "/" + words[0].charAt(1) + "/"
     + words[0].charAt(2) + "/" + words[0].charAt(3) + "/"
     + words[0].charAt(4) + "/";
     fileName = subFolder.replaceAll("/", "");
     }
     }
     } else if (totalWords == 2) {
     subFolder = words[0].charAt(0) + "/" + words[0].charAt(1) + "/"
     + words[1].charAt(0) + "/" + words[1].charAt(1) + "/";
     fileName = subFolder.replaceAll("/", "");
     }

     if (!fileName.isEmpty() && !subFolder.isEmpty()) {
     String filePath = ngDir + ngFolder + subFolder + fileName + fileSuffix;
     if (new File(filePath).exists()) {
     files.add(filePath);
     }
     }

     } catch (Exception ex) {
     throw ex;
     }
     return files;
     }*/
    /*public ArrayList<String> getContextFiles(String ph, String fileSuffix, int contextSize) throws Exception {
     ArrayList<String> files = new ArrayList<String>();
     try {
     ph = ph.trim().toLowerCase();
     String[] words = ph.split("\\s+");
     int totalWords = words.length;
     String folder = getFolder(totalWords + contextSize);
     int maxCharInIndex = maxFolderDepth / totalWords;
     allCharIndex = new ArrayList<String>();
     genNumOfCharPerm(maxCharInIndex, 1, totalWords, "");
     for (String indexComb : allCharIndex) {
     char[] indArr = indexComb.toCharArray();
     String fileName = "";
     String subDir = "";
     if (totalWords == indArr.length) {

     //if (!equalCharsInIndex(indArr)) {
     //    continue;
     //}

     for (int i = 0; i < indArr.length; i++) {
     int numChars = Integer.parseInt(String.valueOf(indArr[i]));
     String word = words[i].trim();
     if (numChars > word.length()) {
     fileName = "";
     subDir = "";
     break;
     }
     for (int j = 0; j < numChars; j++) {
     fileName = fileName + word.charAt(j);
     subDir = subDir + word.charAt(j) + "/";
     }
     }
     }
     if (!fileName.isEmpty() && !subDir.isEmpty()) {
     String filePath = ngDir + folder + subDir + fileName + fileSuffix;
     if (new File(filePath).exists()) {
     files.add(filePath);
     }
     }
     }
     } catch (Exception ex) {
     throw ex;
     }
     return files;
     }*/
    public ArrayList<String> extractByPattern(String pattern, ArrayList<String> contextFiles) throws Exception {
        ArrayList<String> al = new ArrayList<String>();
        try {
            for (String filePath : contextFiles) {
                BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim().toLowerCase();
                    Matcher mtr = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(line);
                    if (mtr.find()) {
                        al.add(line);
                    }
                }

                br.close();
            }

        } catch (Exception e) {
            throw e;
        }
        return al;
    }

    public int[] getPhContextIndexes(int phSize) throws Exception {
        int[] indexes = null;
        try {
            if (phSize == 1) {
                indexes = new int[]{1, 2};
            } else if (phSize == 2) {
                indexes = new int[]{2, 3};
            }
        } catch (Exception ex) {
            throw ex;
        }
        return indexes;
    }
    
    public int[] getPhIndexes(int phSize) throws Exception {
        int[] indexes = null;
        try {
            if (phSize == 1) {
                indexes = new int[]{0};
            } else if (phSize == 2) {
                indexes = new int[]{0,1};
            }
        } catch (Exception ex) {
            throw ex;
        }
        return indexes;
    }

    public String getContextRegx(double avgPhSize, FilterMode mode) throws Exception {
        String regx = "";
        try {
            switch (mode) {
                case SUFFIX:
                    if (avgPhSize < 2) {
                        regx = "[a-zA-Z]*\\s[a-zA-Z]+\\s[a-zA-Z]+\\s[0-9]+$";
                        //regx = "\\s[a-zA-Z]+\\s[a-zA-Z]+\\s[0-9]+$";
                    } else {
                        regx = ".*\\s.+\\s[a-zA-Z0-9]+\\s[0-9]+$";
                        //regx = "\\s.+\\s[a-zA-Z0-9]+\\s[0-9]+$";
                    }
                    break;
                case PREFIX:
                    if (avgPhSize < 2) {
                        regx = "[a-zA-Z]*\\s[a-zA-Z]+\\s[a-zA-Z]+\\s[0-9]+$";
                        //regx = "\\s[a-zA-Z]+\\s[a-zA-Z]+\\s[0-9]+$";
                    } else {
                        regx = ".*\\s[a-zA-Z0-9]+\\s.+\\s[0-9]+$";
                        //regx = "\\s[a-zA-Z0-9]+\\s.+\\s[0-9]+$";
                    }
                    break;
                case INFIX:
                    if (avgPhSize < 2) {
                        regx = "[a-zA-Z]*\\s[a-zA-Z]+\\s[a-zA-Z]+\\s[0-9]+$";
                        //regx = "\\s[a-zA-Z]+\\s[a-zA-Z]+\\s[0-9]+$";
                    } else {
                        regx = ".*\\s[a-zA-Z0-9]+\\s[a-zA-Z0-9]+\\s[0-9]+$";
                        //regx = "\\s[a-zA-Z0-9]+\\s[a-zA-Z0-9]+\\s[0-9]+$";
                    }
                    break;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return regx;
    }

    public String getNGramRegx(double avgPhSize) throws Exception {
        String regx = "";
        try {
            if (avgPhSize < 2) {
                regx = "[a-zA-Z]*\\s[0-9]+$";
                //regx = "\\s[0-9]+$";
            } else {
                regx = ".*\\s[0-9]+$";
                //regx = "\\s[0-9]+$";
            }

        } catch (Exception ex) {
            throw ex;
        }
        return regx;
    }

    public String getPhContextSFI(int phSize, FilterMode mode) throws Exception {
        String conxSFI = "";
        switch (mode) {
            case SUFFIX:
                conxSFI = getPhContextSuffix(phSize);
                break;
            case PREFIX:
                conxSFI = getPhContextPrefix(phSize);
                break;
            case INFIX:
                conxSFI = getPhContextInfix(phSize);
                break;
        }
        return conxSFI;
    }

    public String getNGramSuffix(int phSize) throws Exception {
        String suffix = "";
        try {
            if (phSize == 1) {
                suffix = "-1";
            } else if (phSize == 2) {
                suffix = "-12";
            }
        } catch (Exception ex) {
            throw ex;
        }
        return suffix;
    }

    private String getFolder(int phSize) throws Exception {
        String folder = "";
        try {
            if (phSize == 1) {
                folder = "1gm-0\\";
            } else if (phSize == 2) {
                folder = "2gm-0-1\\";
            } else if (phSize == 3) {
                folder = "3gm-0\\";
            } else if (phSize == 4) {
                folder = "4gm-0-1\\";
            }
        } catch (Exception ex) {
            throw ex;
        }
        return folder;
    }

    private void genNumOfCharPerm(int maxCharLength, int minCharLength, int wordsInPhrase, String permStr) throws Exception {
        try {
            for (int j = maxCharLength; j >= minCharLength; j--) {
                permStr = permStr + j;
                if (permStr.length() == wordsInPhrase) {
                    allCharIndex.add(permStr);
                    permStr = permStr.substring(0, permStr.length() - 1);
                } else {
                    genNumOfCharPerm(maxCharLength, minCharLength, wordsInPhrase, permStr);
                    permStr = permStr.substring(0, permStr.length() - 1);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private boolean equalCharsInIndex(char[] indArr) throws Exception {
        try {
            char ch = indArr[0];
            for (int i = 1; i < indArr.length; i++) {
                if (ch != indArr[i]) {
                    return false;
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return true;
    }

    private String getPhContextSuffix(int phSize) throws Exception {
        String suffix = "";
        try {
            if (phSize == 1) {
                suffix = "-123";
            } else if (phSize == 2) {
                suffix = "-1234";
            }

        } catch (Exception ex) {
            throw ex;
        }
        return suffix;
    }

    private String getPhContextPrefix(int phSize) throws Exception {
        String prefix = "";
        try {
            if (phSize == 1) {
                prefix = "-312";
            } else if (phSize == 2) {
                prefix = "-3412";
            }
        } catch (Exception ex) {
            throw ex;
        }
        return prefix;
    }

    private String getPhContextInfix(int phSize) throws Exception {
        String infix = "";
        try {
            if (phSize == 1) {
                infix = "-213";
            } else if (phSize == 2) {
                infix = "-2314";
            }
        } catch (Exception ex) {
            throw ex;
        }
        return infix;
    }
}
