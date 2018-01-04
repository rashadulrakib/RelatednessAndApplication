package dal.clustering.document.shared.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class PreprocessedContainer {
	public LinkedHashMap<String, ArrayList<String>> HmTrainDocsLabelBody; //ArrayList<String> may contain only one doc
	public ArrayList<String[]> AlTestDocsBodyLabel; //String[] two values, 0 = body, 1 = label
	public HashSet<String> UniqueWords;
}
