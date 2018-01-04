package dal.clustering.document.tools.weka;

import java.util.ArrayList;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;

public class KMeansClusteringArff {
	private String arffFile;
	private int K;
	private int seed;
	private int seedInitializationMethod;
	private ArrayList<double []> CentroidsDoubleValue;
	private ArrayList<double []> InstancesDoubleValue;
	private double [] clusterAssignments; 
	
	public void setArffFile(String arffFile) {
		this.arffFile = arffFile;
	}

	public void setK(int k) {
		K = k;
	}

	public void setSeed(int seed) {
		this.seed = seed;
	}

	public void setSeedInitializationMethod(int seedInitializationMethod) {
		this.seedInitializationMethod = seedInitializationMethod;
	}

	public ArrayList<double []> getCentroids() {
		return CentroidsDoubleValue;
	}
	
	public ArrayList<double []> getInstances() {
		return InstancesDoubleValue;
	}
	
	public double [] getClusterAssignments(){
		return clusterAssignments;
	}
	
	public KMeansClusteringArff(){
	}
	
	public KMeansClusteringArff(String arffFile, int K, int seed, int seedInitializationMethod) throws Exception {
		this.arffFile = arffFile;
		this.K = K;
		this.seed = seed;
	}
	
	public void ClusterAndEvaluate(){
		try{
			Instances insts = new LoadArffFile(arffFile).load();
			insts.setClassIndex(insts.numAttributes()-1);
			
			weka.filters.unsupervised.attribute.Remove filter = new weka.filters.unsupervised.attribute.Remove();
			filter.setAttributeIndices("" + (insts.classIndex() + 1));
			filter.setInputFormat(insts);
			Instances dataClusterer = Filter.useFilter(insts, filter);
			
			SimpleKMeans kmeans = new SimpleKMeans();
			
			kmeans.setSeed(seed);
			kmeans.setPreserveInstancesOrder(true);
			kmeans.setNumClusters(K);
			kmeans.setInitializationMethod(new SelectedTag(seedInitializationMethod, SimpleKMeans.TAGS_SELECTION));
			
			kmeans.buildClusterer(dataClusterer);
			
			CentroidsDoubleValue = new ArrayList<double[]>();
			Instances centroids = kmeans.getClusterCentroids();
			for(Instance centroid: centroids){
				double[] cen = centroid.toDoubleArray();
				CentroidsDoubleValue.add(cen);
			}
			
			InstancesDoubleValue = new ArrayList<double[]>();
			for(Instance instData: dataClusterer){
				double[] indata = instData.toDoubleArray();
				InstancesDoubleValue.add(indata);
			}
			
			ClusterEvaluation eval = new ClusterEvaluation();
			eval.setClusterer(kmeans);
			eval.evaluateClusterer(insts);
			clusterAssignments = eval.getClusterAssignments();
			
			 
			System.out.println(eval.clusterResultsToString());	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
