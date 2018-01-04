package dal.relatedness.phrase.load.nontokenized.bigram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import dal.relatedness.phrase.constants.PhraseDirectories;
import dal.relatedness.phrase.constants.PhraseFileNames;

public class LoadNonTokenizeBigram {

	private ArrayList<String> _alStemmedCrossCheckedBgs;
	private ArrayList<Integer> _alStemmedCrossCheckedBgFreqs;
	private HashMap<String, Integer> _hmStemmedCrossCheckedBgIndex;
	private ArrayList<HashMap<String, Integer>> _alhmStemmedCrossCheckedBgIndex;

	public ArrayList<String> getAlStemmedCrossCheckedBgs() {
		return _alStemmedCrossCheckedBgs;
	}

	public ArrayList<Integer> getAlStemmedCrossCheckedBgFreqs() {
		return _alStemmedCrossCheckedBgFreqs;
	}

	public HashMap<String, Integer> getHmStemmedCrossCheckedBgIndex() {
		return _hmStemmedCrossCheckedBgIndex;
	}

	public ArrayList<HashMap<String, Integer>> getAlHmStemmedCrossCheckedBgIndex() {
		return _alhmStemmedCrossCheckedBgIndex;
	}

	public void ClearStemmedCrossChecked() {
		if (_alStemmedCrossCheckedBgs != null)
			_alStemmedCrossCheckedBgs.clear();
		if (_alStemmedCrossCheckedBgFreqs != null)
			_alStemmedCrossCheckedBgFreqs.clear();
		if (_hmStemmedCrossCheckedBgIndex != null)
			_hmStemmedCrossCheckedBgIndex.clear();
		if (_alhmStemmedCrossCheckedBgIndex != null)
			_alhmStemmedCrossCheckedBgIndex.clear();

		_alStemmedCrossCheckedBgs = null;
		_alStemmedCrossCheckedBgFreqs = null;
		_hmStemmedCrossCheckedBgIndex = null;
		_alhmStemmedCrossCheckedBgIndex = null;
	}

	public void PopulateStemmedBiGramsList() {

		try {
			
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(
					PhraseDirectories.bigramCrossCheckDir
							+ PhraseFileNames.bgStemmedCrossedCheckedFile));

			_alStemmedCrossCheckedBgs = new ArrayList<String>();
			_alStemmedCrossCheckedBgFreqs = new ArrayList<Integer>();

			System.out.println("start:"+new Date().toString());
			
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.isEmpty())
					continue;

				String arr[] = sCurrentLine.split("\\s+");

				if (arr.length != 3) {
					System.out.println("Wrong bg " + sCurrentLine);
					continue;
				}

				_alStemmedCrossCheckedBgs.add(arr[0] + " " + arr[1]);
				_alStemmedCrossCheckedBgFreqs.add(Integer.parseInt(arr[2]));
			}

			br.close();

			System.out.println("PopulateStemmedTokenizedBiGrams="
					+ _alStemmedCrossCheckedBgs.size()+", "+new Date().toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void PopulateStemmedBiGramsHashMap() {
		try {
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(
					PhraseDirectories.bigramCrossCheckDir
							+ PhraseFileNames.bgStemmedCrossedCheckedFile));

			_hmStemmedCrossCheckedBgIndex = new HashMap<String, Integer>();
			_alStemmedCrossCheckedBgFreqs = new ArrayList<Integer>();

			int id = 0;

			System.out.println("start:"+new Date().toString());
			
			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.isEmpty())
					continue;

				String arr[] = sCurrentLine.split("\\s+");

				if (arr.length != 3) {
					System.out.println("Wrong bg " + sCurrentLine);
					continue;
				}

				_hmStemmedCrossCheckedBgIndex.put(arr[0] + " " + arr[1], id++);
				_alStemmedCrossCheckedBgFreqs.add(Integer.parseInt(arr[2]));
			}

			br.close();

			System.out.println("PopulateStemmedBiGramsHashMap="
					+ _alStemmedCrossCheckedBgFreqs.size()+", "+new Date().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void PopulateStemmedBiGramsListHashMap() {
		try {
			String sCurrentLine;
			BufferedReader br = new BufferedReader(new FileReader(
					PhraseDirectories.bigramCrossCheckDir
							+ PhraseFileNames.bgStemmedCrossedCheckedFile));

			_alhmStemmedCrossCheckedBgIndex = new ArrayList<HashMap<String, Integer>>();
			_alStemmedCrossCheckedBgFreqs = new ArrayList<Integer>();

			for (int i = 0; i < 36; i++) {
				_alhmStemmedCrossCheckedBgIndex.add(null);
			}

			int id = 0;
			
			System.out.println("start:"+new Date().toString());

			while ((sCurrentLine = br.readLine()) != null) {
				if (sCurrentLine.isEmpty())
					continue;

				String arr[] = sCurrentLine.split("\\s+");

				if (arr.length != 3) {
					System.out.println("Wrong bg " + sCurrentLine);
					continue;
				}

				String key = arr[0] + " " + arr[1];

				int subtract = key.charAt(0) - '0' - 39;

				int alIndex = subtract < 0 ? key.charAt(0) - '0' : subtract;

				if (_alhmStemmedCrossCheckedBgIndex.get(alIndex) == null) {
					HashMap<String, Integer> hm = new HashMap<String, Integer>();
					hm.put(key, id++);
					_alhmStemmedCrossCheckedBgIndex.set(alIndex, hm);
				} else {
					HashMap<String, Integer> hm = _alhmStemmedCrossCheckedBgIndex
							.get(alIndex);
					if (!hm.containsKey(key)) {
						hm.put(key, id++);
					} else {
						System.out.println(key + " duplicate.");
						continue;
					}
					_alhmStemmedCrossCheckedBgIndex.set(alIndex, hm);
				}

				_alStemmedCrossCheckedBgFreqs.add(Integer.parseInt(arr[2]));
			}

			br.close();
			
			System.out.println("PopulateStemmedBiGramsListHashMap="
					+ _alStemmedCrossCheckedBgFreqs.size()+", "+new Date().toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
