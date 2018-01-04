package dal.clustering.document.tools.weka;

import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.tokenizers.NGramTokenizer;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class KMeansClusteringText {
	String arffFile;
	int K;
	
	public KMeansClusteringText(String arffFile, int K) throws Exception {
		this.arffFile = arffFile;
		this.K = K;
	}
	
	public boolean ClusterAndEvaluate(){
		try{
						
			Instances insts = new LoadArffFile(arffFile).load();
			NGramTokenizer ngtokenizer = new NGTokenize(1).getNGTokenizer();
			StringToWordVector filterngVectors = new StringToVectorization(ngtokenizer,insts).convertStringToVector();
			Instances filteredData = Filter.useFilter(insts, filterngVectors);
			
//			HashMap<String, Integer> classIndex = new HashMap<String, Integer>();
//			classIndex.put("business", 0);
//			classIndex.put("computers", 1);
//			classIndex.put("cultureartsentertainment", 2);
//			classIndex.put("educationscience", 3);
//			classIndex.put("engineering", 4);
//			classIndex.put("health", 5);
//			classIndex.put("politicssociety", 6);
//			classIndex.put("sports", 7);
			int [] orglabel = new int[filteredData.size()];
			int cout =0;
			for(Instance inst: filteredData){
				double val = inst.value(0);
				System.out.print( (int)val+",");
				orglabel[cout++] = (int)val;
			}
		
			Remove  remove = new Remove();
			remove.setAttributeIndices("1");
			remove.setInputFormat(filteredData);
			Instances instNew = Filter.useFilter(filteredData, remove);
			
//			PrincipalComponents pca = new PrincipalComponents();
//			pca.setVarianceCovered(0.95);
//			pca.setInputFormat(instNew);
//			pca.setMaximumAttributeNames(5);
			
			//instNew=  Filter.useFilter(instNew, pca);
//			pca.buildEvaluator(instNew);
			
						
			SimpleKMeans clusterKMeans = new SimpleKMeans();
			clusterKMeans.setNumClusters(K);
			clusterKMeans.setPreserveInstancesOrder(true);
			clusterKMeans.setMaxIterations(10000);
			clusterKMeans.setSeed(10);
			
			
			EuclideanDistance euclidDist = new EuclideanDistance();
			clusterKMeans.setDistanceFunction(euclidDist);
			
			
			clusterKMeans.buildClusterer(instNew);
			int assingArray [] = clusterKMeans.getAssignments();
			
//			ClusterEvaluation eval = new ClusterEvaluation();
//			eval.setClusterer(clusterKMeans);
//		    eval.evaluateClusterer(new Instances(instNew));
//		    
//		    System.out.println("Result: " + eval.clusterResultsToString());
			System.out.println();
			int missmatch =0;
			for(int i=0;i<assingArray.length;i++){
				System.out.print( assingArray[i]+",");
				if( assingArray[i]!=orglabel[i]){
					missmatch++;
				}
			}
		    System.out.println("length="+assingArray.length);
		    System.out.println("missmatch="+missmatch+",acc="+(1-missmatch/(double)assingArray.length));
		    System.out.println("Clustering done.");
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
