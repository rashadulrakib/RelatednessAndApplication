package dal.clustering.document.shared.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Random;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.DocClusterConstant;
import dal.clustering.document.shared.DocClusterUtil;
import dal.clustering.document.shared.DocClusterUtilGTM;
import dal.clustering.document.shared.DocClusterUtilTrWP;
import dal.clustering.document.shared.DocClusterUtilW2Vec;
import dal.clustering.document.shared.entities.ClusterResultConatainerText;
import dal.clustering.document.shared.entities.InstanceText;
import dal.clustering.document.shared.entities.InstanceW2Vec;

public class UnSupervisedClusteringText {
	public DocClusterUtilGTM docClusterUtilGtm;
	public DocClusterUtilTrWP docClusterUtilTrWP;
	
	DocClusterUtil docClusterUtil;
	DocClusterUtilW2Vec docClusterUtilW2Vec;
	
	public UnSupervisedClusteringText(DocClusterUtil docClusterUtil, DocClusterUtilW2Vec docClusterUtilW2Vec) throws IOException{
		docClusterUtilGtm = new DocClusterUtilGTM();
		docClusterUtilTrWP = new DocClusterUtilTrWP();
		this.docClusterUtil = docClusterUtil;
		this.docClusterUtilW2Vec = docClusterUtilW2Vec;
	}
	
	public UnSupervisedClusteringText(DocClusterUtil docClusterUtil) throws IOException{
		docClusterUtilGtm = new DocClusterUtilGTM();
		docClusterUtilTrWP = new DocClusterUtilTrWP();
		this.docClusterUtil = docClusterUtil;
	}
	
	public ClusterResultConatainerText PerformUnSeupervisedSeededClusteringByGtmWordSim(LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody,
			ArrayList<String[]> alTestDocsBodyLabel) {
		ClusterResultConatainerText clusterResultConatainerText = new ClusterResultConatainerText();
		
		try{
			
			ClusterEvaluation tempclusterEvaluation = new ClusterEvaluation(docClusterUtil);
			
			boolean converge = false;
			int iter = 0;
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
			
			double minimumSSE = Double.MAX_VALUE;
			
			HashSet<String> usedMedoidTexts = new HashSet<String>();
			
			while(!converge && iter++< DocClusterConstant.KMedoidMaxIteration){
				LinkedHashMap<String, ArrayList<InstanceText>> newClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
			
				double newSSE = 0;
				
				for(String key: hmTrainDocsLabelBody.keySet()){
					usedMedoidTexts.addAll(hmTrainDocsLabelBody.get(key));
				}
				
				for(String[] bodyLabel: alTestDocsBodyLabel){
					String body = bodyLabel[0];
					String label = bodyLabel[1];
					
					//last item is the center-medoid
					String closestLabel = "";
					double mostSimilarity = Double.MIN_VALUE;
					
					for(String centerLabel: hmTrainDocsLabelBody.keySet()){
						ArrayList<String> allCentes = hmTrainDocsLabelBody.get(centerLabel);
						String centerText = allCentes.get(allCentes.size()-1);
						
						//double dist = 1.0-docClusterUtilText.ComputeTextSimGTM(centerText, body);
						double similarity = docClusterUtilGtm.ComputeTextSimGTM(centerText, body);
						
						if(mostSimilarity<similarity){
							mostSimilarity = similarity;
							closestLabel = centerLabel;
						}
					}
					
					if(closestLabel.trim().length()<=0){
						continue;
					}
					
					newSSE = newSSE + mostSimilarity;
					
					InstanceText newInst = new InstanceText(); 
					newInst.OriginalLabel = label;
					newInst.Text = body;
					newInst.ClusteredLabel = closestLabel;
					
					if(!newClusters.containsKey(closestLabel)){
						ArrayList<InstanceText> newAl = new ArrayList<InstanceText>();
						newAl.add(newInst);
						newClusters.put(closestLabel, newAl);
					}
					else{
						ArrayList<InstanceText> newAl = newClusters.get(closestLabel);
						newAl.add(newInst);
						newClusters.put(closestLabel, newAl);
					}
				}
				
				//temp
				
				tempclusterEvaluation.EvalSemiSupervisedByAccOneToOneText(newClusters);
				tempclusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(newClusters);
				//temp
				
				if(minimumSSE>newSSE){
					System.out.println("newSSE="+newSSE+",minimumSSE="+minimumSSE);
					
					minimumSSE = newSSE;
					lastClusters = newClusters;
					
				}
				
//				for(String newLabel: newClusters.keySet()){
//					ArrayList<InstanceText> newinsts = newClusters.get(newLabel);
//					int randIndex = new Random().nextInt(newinsts.size());
//					InstanceText newinst = newinsts.get(randIndex);
//					ArrayList<String> al = new ArrayList<String>();
//					al.add(newinst.Text);
//					hmTrainDocsLabelBody.put(newLabel, al);
//				}
				
				converge = true;
			}
			
			clusterResultConatainerText.FinalCluster = lastClusters;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return clusterResultConatainerText;
	}
	
	public ClusterResultConatainerText PerformUnSeupervisedSeededClusteringByGtmWordSimIterative(LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody,  ArrayList<InstanceW2Vec> alTestDocsBodyLabel, int x) {
		
		ClusterResultConatainerText clusterResultConatainerText = new ClusterResultConatainerText();
		
		try{
			
			ClusterEvaluation tempclusterEvaluation = new ClusterEvaluation(docClusterUtil);
			
			boolean converge = false;
			int iter = 0;
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
			LinkedHashMap<String, double[]> lastCenetrsW2Vec = new LinkedHashMap<String, double[]>();
			double minimumSSE = Double.MAX_VALUE;
			
			while(!converge && iter++< DocClusterConstant.KMedoidMaxIteration){
				LinkedHashMap<String, ArrayList<InstanceText>> newClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
				LinkedHashMap<String, ArrayList<InstanceW2Vec>> newClustersW2Vec = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
				
				double newSSE = 0;
				
				for(InstanceW2Vec instW2Vec: alTestDocsBodyLabel){
					String body = instW2Vec.Text;
					String label = instW2Vec.OriginalLabel;
					
					//last item is the center-medoid
					String closestLabel = "";
					double mostSimilarity = Double.MIN_VALUE;
					
					for(String centerLabel: hmTrainDocsLabelBody.keySet()){
						ArrayList<String> allCentes = hmTrainDocsLabelBody.get(centerLabel);
						String centerText = allCentes.get(allCentes.size()-1);
						
						//double dist = 1.0-docClusterUtilText.ComputeTextSimGTM(centerText, body);
						double similarity = docClusterUtilGtm.ComputeTextSimGTM(centerText, body);
						
						if(mostSimilarity<similarity){
							mostSimilarity = similarity;
							closestLabel = centerLabel;
						}
					}
					
					if(closestLabel.trim().length()<=0){
						continue;
					}
					
					newSSE = newSSE + mostSimilarity;
					
					InstanceText newInst = new InstanceText(); 
					newInst.OriginalLabel = label;
					newInst.Text = body;
					newInst.ClusteredLabel = closestLabel;
					
//					InstanceW2Vec instanceW2Vec = new InstanceW2Vec();
//					instanceW2Vec.OriginalLabel = label;
//					instanceW2Vec.Features = docClusterUtilW2Vec.PopulateW2VecForSingleDoc(body);
//					instanceW2Vec.Text = body;
//					instanceW2Vec.ClusteredLabel = closestLabel;
					
					if(!newClusters.containsKey(closestLabel)){
						ArrayList<InstanceText> newAl = new ArrayList<InstanceText>();
						newAl.add(newInst);
						newClusters.put(closestLabel, newAl);
					}
					else{
						ArrayList<InstanceText> newAl = newClusters.get(closestLabel);
						newAl.add(newInst);
						newClusters.put(closestLabel, newAl);
					}
					
					
					////
					if(!newClustersW2Vec.containsKey(closestLabel)){
						ArrayList<InstanceW2Vec> newAl = new ArrayList<InstanceW2Vec>();
						newAl.add(instW2Vec);
						newClustersW2Vec.put(closestLabel, newAl);
					}
					else{
						ArrayList<InstanceW2Vec> newAl = newClustersW2Vec.get(closestLabel);
						newAl.add(instW2Vec);
						newClustersW2Vec.put(closestLabel, newAl);
					}
				}
				
				//temp
				
				tempclusterEvaluation.EvalSemiSupervisedByAccOneToOneText(newClusters);
				tempclusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(newClusters);
				//temp
				
				//if(minimumSSE>newSSE)
				{
					System.out.println("newSSE="+newSSE+",minimumSSE="+minimumSSE+",iter="+iter);
					//minimumSSE = newSSE;
				}
				
				LinkedHashMap<String, double[]> newCenetrsW2Vec = docClusterUtil.ReComputeCenters(newClustersW2Vec);
				
				if(iter>1)
				{
					converge = docClusterUtil.IsConverge(newCenetrsW2Vec, lastCenetrsW2Vec);
				}
				
				//hmTrainDocsLabelBody = docClusterUtil.GetInstanceClosestToCentersText(newClustersW2Vec, newCenetrsW2Vec);
				hmTrainDocsLabelBody = docClusterUtil.GetInstanceClosestToCentersText(alTestDocsBodyLabel, newCenetrsW2Vec, 0);
				
				lastClusters = newClusters;
				lastCenetrsW2Vec = newCenetrsW2Vec;
				
				clusterResultConatainerText.FinalCluster = lastClusters;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return clusterResultConatainerText;
	}
	
	public ClusterResultConatainerText PerformUnSeupervisedSeededClusteringByGtmWordSimIterative(
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody, ArrayList<String[]> alTestDocsBodyLabel) {
		
		ClusterResultConatainerText clusterResultConatainerText = new ClusterResultConatainerText();
		
		try{
			
			ClusterEvaluation tempclusterEvaluation = new ClusterEvaluation(docClusterUtil);
			
			boolean converge = false;
			int iter = 0;
			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
			LinkedHashMap<String, double[]> lastCenetrsW2Vec = new LinkedHashMap<String, double[]>();
			double minimumSSE = Double.MAX_VALUE;
			
			while(!converge && iter++< DocClusterConstant.KMedoidMaxIteration){
				LinkedHashMap<String, ArrayList<InstanceText>> newClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
				LinkedHashMap<String, ArrayList<InstanceW2Vec>> newClustersW2Vec = new LinkedHashMap<String, ArrayList<InstanceW2Vec>>();
				
				double newSSE = 0;
				
				for(String[] bodyLabel: alTestDocsBodyLabel){
					String body = bodyLabel[0];
					String label = bodyLabel[1];
					
					//last item is the center-medoid
					String closestLabel = "";
					double mostSimilarity = Double.MIN_VALUE;
					
					for(String centerLabel: hmTrainDocsLabelBody.keySet()){
						ArrayList<String> allCentes = hmTrainDocsLabelBody.get(centerLabel);
						String centerText = allCentes.get(allCentes.size()-1);
						
						//double dist = 1.0-docClusterUtilText.ComputeTextSimGTM(centerText, body);
						double similarity = docClusterUtilGtm.ComputeTextSimGTM(centerText, body);
						
						if(mostSimilarity<similarity){
							mostSimilarity = similarity;
							closestLabel = centerLabel;
						}
					}
					
					if(closestLabel.trim().length()<=0){
						continue;
					}
					
					newSSE = newSSE + mostSimilarity;
					
					InstanceText newInst = new InstanceText(); 
					newInst.OriginalLabel = label;
					newInst.Text = body;
					newInst.ClusteredLabel = closestLabel;
					
					InstanceW2Vec instanceW2Vec = new InstanceW2Vec();
					instanceW2Vec.OriginalLabel = label;
					instanceW2Vec.Features = docClusterUtilW2Vec.PopulateW2VecForSingleDoc(body);
					instanceW2Vec.Text = body;
					instanceW2Vec.ClusteredLabel = closestLabel;
					
					if(!newClusters.containsKey(closestLabel)){
						ArrayList<InstanceText> newAl = new ArrayList<InstanceText>();
						newAl.add(newInst);
						newClusters.put(closestLabel, newAl);
					}
					else{
						ArrayList<InstanceText> newAl = newClusters.get(closestLabel);
						newAl.add(newInst);
						newClusters.put(closestLabel, newAl);
					}
					
					
					////
					if(!newClustersW2Vec.containsKey(closestLabel)){
						ArrayList<InstanceW2Vec> newAl = new ArrayList<InstanceW2Vec>();
						newAl.add(instanceW2Vec);
						newClustersW2Vec.put(closestLabel, newAl);
					}
					else{
						ArrayList<InstanceW2Vec> newAl = newClustersW2Vec.get(closestLabel);
						newAl.add(instanceW2Vec);
						newClustersW2Vec.put(closestLabel, newAl);
					}
				}
				
				//temp
				
				tempclusterEvaluation.EvalSemiSupervisedByAccOneToOneText(newClusters);
				tempclusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(newClusters);
				//temp
				
				//if(minimumSSE>newSSE)
				{
					System.out.println("newSSE="+newSSE+",minimumSSE="+minimumSSE+",iter="+iter);
					//minimumSSE = newSSE;
				}
				
				LinkedHashMap<String, double[]> newCenetrsW2Vec = docClusterUtil.ReComputeCenters(newClustersW2Vec);
				
				if(iter>1)
				{
					converge = docClusterUtil.IsConverge(newCenetrsW2Vec, lastCenetrsW2Vec);
				}
				
				hmTrainDocsLabelBody = docClusterUtil.GetInstanceClosestToCentersText(newClustersW2Vec, newCenetrsW2Vec);
				//hmTrainDocsLabelBody = docClusterUtil.GetInstanceClosestToCentersText(alTestDocsBodyLabel, newCenetrsW2Vec);
				
				lastClusters = newClusters;
				lastCenetrsW2Vec = newCenetrsW2Vec;
				
				clusterResultConatainerText.FinalCluster = lastClusters;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return clusterResultConatainerText;
	}

		//this is the k-medoid but wrong	
//	public ClusterResultConatainerText PerformUnSeuperVisedSeededClustering(LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody,
//			ArrayList<String[]> alTestDocsBodyLabel) {
//		ClusterResultConatainerText clusterResultConatainerText = new ClusterResultConatainerText();
//		
//		try{
//			
//			ClusterEvaluation tempclusterEvaluation = new ClusterEvaluation();
//			
//			boolean converge = false;
//			int iter = 0;
//			LinkedHashMap<String, ArrayList<InstanceText>> lastClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
//			
//			//double minimumSSE = Double.MAX_VALUE;
//			
//			HashSet<String> usedMedoidTexts = new HashSet<String>();
//			
//			while(!converge && iter++< DocClusterConstant.KMedoidMaxIteration){
//				LinkedHashMap<String, ArrayList<InstanceText>> newClusters = new LinkedHashMap<String, ArrayList<InstanceText>>();
//			
//				double newSSE = 0;
//				
//				for(String key: hmTrainDocsLabelBody.keySet()){
//					usedMedoidTexts.addAll(hmTrainDocsLabelBody.get(key));
//				}
//				
//				for(String[] bodyLabel: alTestDocsBodyLabel){
//					String body = bodyLabel[0];
//					String label = bodyLabel[1];
//					
//					//last item is the center-medoid
//					String closestLabel = "";
//					double closestDist = Double.MAX_VALUE;
//					
//					for(String centerLabel: hmTrainDocsLabelBody.keySet()){
//						ArrayList<String> allCentes = hmTrainDocsLabelBody.get(centerLabel);
//						String centerText = allCentes.get(allCentes.size()-1);
//						
//						double dist = 1.0-docClusterUtilText.ComputeTextSimGTM(centerText, body);
//						
//						if(closestDist>dist){
//							closestDist = dist;
//							closestLabel = centerLabel;
//						}
//					}
//					
//					newSSE = newSSE + closestDist;
//					
//					InstanceText newInst = new InstanceText(); 
//					newInst.OriginalLabel = label;
//					newInst.Text = body;
//					newInst.ClusteredLabel = closestLabel;
//					
//					if(!newClusters.containsKey(closestLabel)){
//						ArrayList<InstanceText> newAl = new ArrayList<InstanceText>();
//						newAl.add(newInst);
//						newClusters.put(closestLabel, newAl);
//					}
//					else{
//						ArrayList<InstanceText> newAl = newClusters.get(closestLabel);
//						newAl.add(newInst);
//						newClusters.put(closestLabel, newAl);
//					}
//				}
//				
////				if(minimumSSE> newSSE){
////					minimumSSE = newSSE;
////					lastClusters = newClusters;
////					
////					
////				}
////				//temp
////				System.out.println("newSSE="+newSSE+",minimumSSE="+minimumSSE);
//				tempclusterEvaluation.EvalSemiSupervisedByAccOneToOneText(newClusters);
//				tempclusterEvaluation.EvalSemiSupervisedByAccMajorityVotingText(newClusters);
////				//temp
////				
////				hmTrainDocsLabelBody = replaceOldMedoidByNew(hmTrainDocsLabelBody, alTestDocsBodyLabel);
//				
//				String nonMediodBody = getNonMedoidBody(usedMedoidTexts, alTestDocsBodyLabel);
//				LinkedHashMap<String, ArrayList<String>> hmTEMPTrainDocsLabelBody = (LinkedHashMap<String, ArrayList<String>>) hmTrainDocsLabelBody.clone();
//				LinkedHashMap<String, ArrayList<String>> hmLOWESTSseTEMPTrainDocsLabelBody = (LinkedHashMap<String, ArrayList<String>>) hmTrainDocsLabelBody.clone();
//	
//				for(String centerLabel: hmTrainDocsLabelBody.keySet()){
//					ArrayList<String> allCentes = hmTrainDocsLabelBody.get(centerLabel);
//					ArrayList<String> allCentes1 = new ArrayList<String>();
//					ArrayList<String> allCentes2 = new ArrayList<String>();
//					
//					for(String center: allCentes){
//						allCentes1.add(center);
//						allCentes2.add(center);
//					}
//					hmTEMPTrainDocsLabelBody.put(centerLabel, allCentes1);
//					hmLOWESTSseTEMPTrainDocsLabelBody.put(centerLabel, allCentes2);
//					System.out.println("before="+centerLabel+","+allCentes);
//				}
//				
//				
//				for(String centerLabel: hmTEMPTrainDocsLabelBody.keySet()){
//					ArrayList<String> allCentes = hmTEMPTrainDocsLabelBody.get(centerLabel);
//					String centerText = allCentes.get(allCentes.size()-1);
//					
//					//String orgCenterText = centerText;
//					
//					allCentes.remove(centerText);
//					allCentes.add(nonMediodBody);
//					hmTEMPTrainDocsLabelBody.put(centerLabel, allCentes);
//					
//					double sseForNonCenter = computesseForNonCenter(alTestDocsBodyLabel, hmTEMPTrainDocsLabelBody);
//					
//					if(sseForNonCenter<newSSE){
//						//hmLOWESTSseTEMPTrainDocsLabelBody = hmTEMPTrainDocsLabelBody;
//						//hmLOWESTSseTEMPTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
//						for(String key1: hmTEMPTrainDocsLabelBody.keySet()){
//							ArrayList<String> allCentes3 = new ArrayList<String>();
//							for(String cent3: hmTEMPTrainDocsLabelBody.get(key1)){
//								allCentes3.add(cent3);
//							}
//							hmLOWESTSseTEMPTrainDocsLabelBody.put(key1, allCentes3);
//						}
//						System.out.println("centerLabel="+centerLabel+",sseForNonCenter="+sseForNonCenter+",newSSE="+newSSE);
//						newSSE = sseForNonCenter;
//						break;
//					}
//					else{
//						allCentes.remove(nonMediodBody);
//						allCentes.add(centerText);
//						hmTEMPTrainDocsLabelBody.put(centerLabel, allCentes);
//					}
//				}
//				
//				
//				for(String centerLabel: hmLOWESTSseTEMPTrainDocsLabelBody.keySet()){
//					ArrayList<String> allCentes = hmLOWESTSseTEMPTrainDocsLabelBody.get(centerLabel);
//					hmTrainDocsLabelBody.put(centerLabel, allCentes);
//					System.out.println("after="+centerLabel+","+allCentes);
//				}
//				
//				converge = true;
//				lastClusters = newClusters;
//			}
//			
//			clusterResultConatainerText.FinalCluster = lastClusters;
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return clusterResultConatainerText;
//	}

	private double computesseForNonCenter(ArrayList<String[]> alTestDocsBodyLabel,
			LinkedHashMap<String, ArrayList<String>> hmTEMPTrainDocsLabelBody) {
		double sse = 0;
		try{
			for(String[] bodyLabel: alTestDocsBodyLabel){
				String body = bodyLabel[0];
				String label = bodyLabel[1];
				
				//last item is the center-medoid
				String closestLabel = "";
				double closestDist = Double.MAX_VALUE;
				
				for(String centerLabel: hmTEMPTrainDocsLabelBody.keySet()){
					ArrayList<String> allCentes = hmTEMPTrainDocsLabelBody.get(centerLabel);
					String centerText = allCentes.get(allCentes.size()-1);
					
					double dist = 1.0-docClusterUtilGtm.ComputeTextSimGTM(centerText, body);
					
					if(closestDist>dist){
						closestDist = dist;
						closestLabel = centerLabel;
					}
				}
				
				sse = sse + closestDist;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return sse;
	}

	private String getNonMedoidBody(HashSet<String> usedMedoidTexts, ArrayList<String[]> alTestDocsBodyLabel) {
		
		String nonMediodBody = "";
		try{
			
			Random rd = new Random();
			
			for(int i=0;i<alTestDocsBodyLabel.size();i++){
				int randomObjectIndexNotusedInPreviousCentroid = rd.nextInt(alTestDocsBodyLabel.size());
				String [] bodyLabelNotUsedBefore =  alTestDocsBodyLabel.get(randomObjectIndexNotusedInPreviousCentroid);
				String bodyNotUsedBefore = bodyLabelNotUsedBefore[0];
				if(!usedMedoidTexts.contains(bodyNotUsedBefore)){
					nonMediodBody = bodyNotUsedBefore;
					break;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return nonMediodBody;
	}

//	private LinkedHashMap<String, ArrayList<String>> replaceOldMedoidByNew(LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody,
//			ArrayList<String[]> alTestDocsBodyLabel) {
//		
//		try{
//			
//			int medoids = hmTrainDocsLabelBody.size();
//			Random rd = new Random();
//			int randomMedoidIndex = rd.nextInt(medoids);
//			
//			int i=0;
//			String randomMedoidLabel = "";
//			HashSet<String> prevMedoids = new HashSet<String>();
//			
//			for(String medLabel: hmTrainDocsLabelBody.keySet()){
//				if(i==randomMedoidIndex){
//					randomMedoidLabel = medLabel;
//				}
//				i++;
//				prevMedoids.addAll(hmTrainDocsLabelBody.get(medLabel));
//			}
//			
//			int objects = alTestDocsBodyLabel.size();
//			
//			String randomMedoidBody = "";
//			for(i=0;i<alTestDocsBodyLabel.size();i++){
//				int randomObjectIndexNotusedInPreviousCentroid = rd.nextInt(objects);
//				String [] bodyLabelNotUsedBefore =  alTestDocsBodyLabel.get(randomObjectIndexNotusedInPreviousCentroid);
//				String bodyNotUsedBefore = bodyLabelNotUsedBefore[0];
//				if(!prevMedoids.contains(bodyNotUsedBefore)){
//					randomMedoidBody = bodyNotUsedBefore;
//					break;
//				}
//			}
//			
//			if(hmTrainDocsLabelBody.containsKey(randomMedoidLabel) && randomMedoidBody.length()>1){
//				ArrayList<String> prevMedoidsTemp = hmTrainDocsLabelBody.get(randomMedoidLabel);
//				prevMedoidsTemp.add(randomMedoidBody);
//				hmTrainDocsLabelBody.put(randomMedoidLabel, prevMedoidsTemp);
//			}
//			
//			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		
//		return hmTrainDocsLabelBody;
//	}
}
