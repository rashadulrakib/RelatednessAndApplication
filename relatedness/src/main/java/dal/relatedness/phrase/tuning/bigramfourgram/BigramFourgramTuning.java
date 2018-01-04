package dal.relatedness.phrase.tuning.bigramfourgram;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.TreeMap;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;

public class BigramFourgramTuning {

	TreeMap<String, Integer> crossedBgs;

	public void TuneFourgramBigram() {
		try {
			TuneFourBigram();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void TuneFourBigram() {
		try {

			HashMap<String, Integer> bgs = new HashMap<String, Integer>();

			for (char ch = 'a'; ch <= 'z'; ch++) {
				String inputFile = PhraseDirectories.biGramIndexedProcessedContextStemDir+ String.valueOf(ch);
				BufferedReader br = new BufferedReader(new FileReader(inputFile));

				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {

					if (sCurrentLine.isEmpty())
						continue;

					String arr[] = sCurrentLine.split("\\s+");

					if (arr.length != 3) {
						System.out.println("bg pb=" + sCurrentLine);
						continue;
					}

					bgs.put(arr[0] + " " + arr[1], Integer.parseInt(arr[2]));

				}
				
				br.close();
			}

			// load fourgrams and crosscheck
			crossedBgs = new TreeMap<String, Integer>();

			// for(File fourgmFile: fourgmFiles)
			for (char ch = 'a'; ch <= 'z'; ch++) {
				String fourgmFile = PhraseDirectories.fourGramIndexedProcessedContextStemDir
						+ String.valueOf(ch);

				String outFilepath = PhraseDirectories.fourgramCrossCheckDir
						+ String.valueOf(ch);

				System.out.println("Input=" + fourgmFile + ", output="
						+ outFilepath);

				
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outFilepath));

				BufferedReader br1 = new BufferedReader(new FileReader(
						fourgmFile));

				String sCurrentLine1;
				int lineCountOriginal4gm = 0;
				int lineCountCrosschecked = 0;
				while ((sCurrentLine1 = br1.readLine()) != null) {

					if (sCurrentLine1.isEmpty())
						continue;

					lineCountOriginal4gm++;

					String arr[] = sCurrentLine1.split("\\s+");

					String bgPhraseIn4g = arr[0] + " " + arr[1];

					if (bgs.containsKey(bgPhraseIn4g)) {
						crossedBgs.put(bgPhraseIn4g, bgs.get(bgPhraseIn4g));

						bufferedWriter.write(sCurrentLine1 + "\n");

						lineCountCrosschecked++;
					}
				}

				br1.close();

				System.out.println("lineCountOriginal4gm="
						+ lineCountOriginal4gm + ", lineCountCrosschecked="
						+ lineCountCrosschecked);

				bufferedWriter.close();
				
			}

			System.out.println("bgs original=" + bgs.size()
					+ ", crossed checked=" + crossedBgs.size());

			SaveTunedBgs();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void SaveTunedBgs() {
		try {
			String outFile = PhraseDirectories.bigramCrossCheckDir
					+ PhraseFileNames.bgStemmedCrossedCheckedFile;

			FileWriter writer = new FileWriter(outFile);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			for (String bgKey : crossedBgs.keySet()) {
				bufferedWriter
						.write(bgKey + " " + crossedBgs.get(bgKey) + "\n");
			}

			bufferedWriter.close();
			writer.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
