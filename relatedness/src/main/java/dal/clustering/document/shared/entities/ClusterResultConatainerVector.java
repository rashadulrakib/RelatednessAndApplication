package dal.clustering.document.shared.entities;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ClusterResultConatainerVector {
	public ArrayList<InstanceW2Vec> ClusteredInstancesTest;
	public LinkedHashMap<String, ArrayList<InstanceW2Vec>> FinalCluster;
	public LinkedHashMap<String, InstanceW2Vec> InstanceClosestToCenter;
}
