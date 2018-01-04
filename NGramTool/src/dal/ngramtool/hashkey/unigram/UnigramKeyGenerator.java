package dal.ngramtool.hashkey.unigram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;

import dal.ngramtool.constants.Directories;
import dal.ngramtool.constants.FileNames;

public class UnigramKeyGenerator {

	final int uniLines = 2456569;

	public void GenerateUnigramHashFunction() {
		try {
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(
					Directories.unigramCrossCheckDir
							+ FileNames.uniGramStemmedCrossChecekedFile));

			LinkedHashMap<String, Integer> hmUni = new LinkedHashMap<String, Integer>();

			int duplicate = 0;

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.isEmpty())
					continue;

				String arr[] = sCurrentLine.split("\\s+");

				if (arr.length != 2) {
					System.out.println("Wrong Uni " + sCurrentLine);
					continue;
				}

				//int hscode =  sfold(arr[0], uniLines);//arr[0].hashCode();

				if (!hmUni.containsKey(arr[0])) {
					hmUni.put(arr[0], 0);
				} else {
					System.out.println("old=" + hmUni.get(arr[0]) + ", new="
							+ arr[0] + ", " + arr[0]);
					duplicate++;
				}
			}

			br.close();

			System.out.println("duplicate=" + duplicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	int sfold(String s, int M) {
		int hash = 0;
		for (int i = 0; i < s.length(); i++) {
		  hash = (hash << 5) - hash + s.charAt(i);
		}
		return hash%M;
	}
}
