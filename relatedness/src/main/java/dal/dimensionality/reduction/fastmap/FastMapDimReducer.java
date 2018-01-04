package dal.dimensionality.reduction.fastmap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import dal.dimensionality.reduction.utils.DimReductionFourgramUtil;
import dal.dimensionality.reduction.utils.DimReductionUtil;
import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;
import dal.utils.common.general.UtilsShared;

public class FastMapDimReducer {
	
	LoadNonTokenizeBigram loadNonTokenizeBigram;
	DimReductionFourgramUtil dimReductionFourgramUtil;
	
	public FastMapDimReducer(){
		loadNonTokenizeBigram = new LoadNonTokenizeBigram();
		dimReductionFourgramUtil = new DimReductionFourgramUtil();
		
		
		loadNonTokenizeBigram.PopulateStemmedBiGramsListHashMap();
	}
	
	public void ReduceDimensionality(){
		try{

			TreeMap<String, Integer> tempPhIds = new TreeMap<String, Integer>();			// hard coded
			tempPhIds.put("action programm",332722);
			tempPhIds.put("american countri",1308340);
			tempPhIds.put("assist manag",2554461);
			tempPhIds.put("assist secretari",2556128);
			tempPhIds.put("basic rule",3445833);
			tempPhIds.put("bedroom window",3707280);
			tempPhIds.put("better job",3968117);
			tempPhIds.put("black hair",4261025);
			tempPhIds.put("board member",4474825);
			tempPhIds.put("bu compani",5175276);
			tempPhIds.put("busi unit",5384244);
			tempPhIds.put("capit market",6080476);
			tempPhIds.put("care plan",6163280);
			tempPhIds.put("central author",6581848);
			tempPhIds.put("certain circumst",6621254);
			tempPhIds.put("citi center",7255298);
			tempPhIds.put("cold air",7687771);
			tempPhIds.put("committe meet",7908216);
			tempPhIds.put("commun care",7926231);
			tempPhIds.put("compani director",7953560);
			tempPhIds.put("comput compani",8068269);
			tempPhIds.put("comput system",8074553);
			tempPhIds.put("counti council",8679817);
			tempPhIds.put("dark ey",9440138);
			tempPhIds.put("datum system",9504546);
			tempPhIds.put("defenc minist",9798368);
			tempPhIds.put("develop plan",10192730);
			tempPhIds.put("develop project",10193123);
			tempPhIds.put("differ kind",10364363);
			tempPhIds.put("differ part",10365797);
			tempPhIds.put("earli ag",11441745);
			tempPhIds.put("earli even",11443558);
			tempPhIds.put("earli stage",11446915);
			tempPhIds.put("earlier work",11450342);
			tempPhIds.put("econom condition",11566806);
			tempPhIds.put("econom develop",11567124);
			tempPhIds.put("econom problem",11569301);
			tempPhIds.put("educ author",11649689);
			tempPhIds.put("educ cours",11650813);
			tempPhIds.put("educ offic",11654386);
			tempPhIds.put("effect wai",11692190);
			tempPhIds.put("effici us",11698763);
			tempPhIds.put("elderli ladi",11807423);
			tempPhIds.put("elderli woman",11807965);
			tempPhIds.put("environ secretari",12282185);
			tempPhIds.put("european state",12608631);
			tempPhIds.put("famili allow",13129105);
			tempPhIds.put("feder assembli",13335457);
			tempPhIds.put("footbal club",14044049);
			tempPhIds.put("further evid",14879909);
			tempPhIds.put("futur develop",14894045);
			tempPhIds.put("gener level",15231211);
			tempPhIds.put("gener principl",15235241);
			tempPhIds.put("good effect",15724376);
			tempPhIds.put("good place",15728792);
			tempPhIds.put("govern interven",15800791);
			tempPhIds.put("govern leader",15801066);
			tempPhIds.put("great major",15947601);
			tempPhIds.put("health minist",16748359);
			tempPhIds.put("health servic",16750269);
			tempPhIds.put("high point",17065099);
			tempPhIds.put("high price",17065275);
			tempPhIds.put("hot weather",17490384);
			tempPhIds.put("hous benefit",17548782);
			tempPhIds.put("hous depart",17551155);
			tempPhIds.put("import part",18155092);
			tempPhIds.put("industri area",18511163);
			tempPhIds.put("intellig servic",18819417);
			tempPhIds.put("interest rate",18854676);
			tempPhIds.put("kitchen door",20450462);
			tempPhIds.put("labour cost",20759409);
			tempPhIds.put("larg number",20934277);
			tempPhIds.put("larg quantiti",20935549);
			tempPhIds.put("leagu match",21158623);
			tempPhIds.put("left arm",21231001);
			tempPhIds.put("littl room",21786028);
			tempPhIds.put("local offic",21889712);
			tempPhIds.put("long period",21992898);
			tempPhIds.put("low cost",22128418);
			tempPhIds.put("major issu",22535295);
			tempPhIds.put("manag skill",22650364);
			tempPhIds.put("manag structur",22651063);
			tempPhIds.put("market director",22880712);
			tempPhIds.put("market leader",22883153);
			tempPhIds.put("modern languag",24112808);
			tempPhIds.put("nation govern",24999355);
			tempPhIds.put("new agenc",25303085);
			tempPhIds.put("new bodi",25307497);
			tempPhIds.put("new inform",25324415);
			tempPhIds.put("new law",25327734);
			tempPhIds.put("new life",25328299);
			tempPhIds.put("new situat",25344385);
			tempPhIds.put("new technologi",25348253);
			tempPhIds.put("northern region",25747954);
			tempPhIds.put("offic worker",26411413);
			tempPhIds.put("oil industri",26470122);
			tempPhIds.put("old person",26512963);
			tempPhIds.put("older man",26523191);
			tempPhIds.put("opposi member",26807762);
			tempPhIds.put("parti leader",27683688);
			tempPhIds.put("parti offici",27684745);
			tempPhIds.put("particular case",27697707);
			tempPhIds.put("personnel manag",28180047);
			tempPhIds.put("phone call",28343489);
			tempPhIds.put("plan committe",28660236);
			tempPhIds.put("polit action",28937430);
			tempPhIds.put("practic difficulti",29254891);
			tempPhIds.put("present position",29386576);
			tempPhIds.put("previou dai",29450753);
			tempPhIds.put("public build",29947669);
			tempPhIds.put("railwai station",30390228);
			tempPhIds.put("research contract",31255224);
			tempPhIds.put("research work",31261573);
			tempPhIds.put("right hand",31601230);
			tempPhIds.put("rural commun",32165536);
			tempPhIds.put("secur polici",33008629);
			tempPhIds.put("servic depart",33297336);
			tempPhIds.put("short time",33701800);
			tempPhIds.put("signific role",33856391);
			tempPhIds.put("similar result",33910032);
			tempPhIds.put("small hous",34253396);
			tempPhIds.put("social activ",34401353);
			tempPhIds.put("social event",34402658);
			tempPhIds.put("special circumst",34792516);
			tempPhIds.put("state benefit",35236892);
			tempPhIds.put("state control",35238380);
			tempPhIds.put("studi group",35681447);
			tempPhIds.put("support group",36039237);
			tempPhIds.put("tax charg",36572074);
			tempPhIds.put("tax credit",36572334);
			tempPhIds.put("tax rate",36574457);
			tempPhIds.put("telephon number",36756075);
			tempPhIds.put("televi programm",36765224);
			tempPhIds.put("town council",38358031);
			tempPhIds.put("town hall",38359187);
			tempPhIds.put("train colleg",38452135);
			tempPhIds.put("train programm",38456575);
			tempPhIds.put("tv set",38906721);
			tempPhIds.put("variou form",39817760);
			tempPhIds.put("vast amount",39832694);
			tempPhIds.put("wage increas",40514776);
			tempPhIds.put("whole countri",41181529);
			tempPhIds.put("whole system",41185662);
			tempPhIds.put("world economi",41724954);
			
			ArrayList<Integer> phidList = new ArrayList<Integer>();
			Set<Integer> setphidliet = new LinkedHashSet<Integer>();
			for(Integer val: tempPhIds.values()){
				phidList.add(val);
				setphidliet.add(val);
			}
			
			System.out.println("phidList="+phidList.size()+", setphidliet="+setphidliet.size());
			
			dimReductionFourgramUtil.PopulateVsmSingleDimSizeAndPhIdCtxIdList(phidList);
			
			//merge the contexts and construct vsm and write it to file
			TreeSet<Integer> vsmSingleDim = CreateVsmByMargeAllthePhIds();
			ArrayList<byte[] > rowbycolMatrix = ConstructRowByColMatrix(new ArrayList<Integer>(vsmSingleDim), dimReductionFourgramUtil.getfourGmPhIdContextIds());
			
			//ArrayList<byte[] > colbyrowMatrix = ConstructColByRowMatrix(new ArrayList<Integer>(vsmSingleDim), dimReductionFourgramUtil.getfourGmPhIdContextIds());
			
			
			//int [] twoDistantPoints = FindTwoDistantPoints();*/
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private ArrayList<byte[]> ConstructColByRowMatrix(	ArrayList<Integer> vsmSingleDim, 	HashMap<Integer, int[]> getfourGmPhIdContextIds) {
		ArrayList<byte[] > nbymMatrix = new ArrayList<byte[] >();
		try{
			
			for(Integer phId: getfourGmPhIdContextIds.keySet()){
				int [] ctxids = getfourGmPhIdContextIds.get(phId);
				
				byte[] sparseVector = new byte[vsmSingleDim.size()]; 
				
				int onecount = 0;
				
				for(int ctxid: ctxids){
					//find index of ctxId in vsmSingleDim
					int ctxIdIndex = Collections.binarySearch(vsmSingleDim, ctxid);
					if(ctxIdIndex>=0 ){
						sparseVector[ctxIdIndex]=1;
						onecount++;
					}
				}
				
				nbymMatrix.add(sparseVector);
			
				System.out.println(sparseVector.length+", onecount="+onecount);				
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter ("/users/grad/rakib/java/RelatednessSolution/relatedness/src/main/java/dal/dimensionality/reduction/fastmap/144-phs-col-row-no-comma"));

			for(int i=0;i<vsmSingleDim.size();i++){
				for(int j=0;j<nbymMatrix.size()-1;j++){
					bw.write(nbymMatrix.get(j)[i]+" ");
				}
				bw.write(nbymMatrix.get(nbymMatrix.size()-1)[i]+"\n");
			}
			
			bw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return  new ArrayList<byte[] >();
	}

	private ArrayList<byte[] > ConstructRowByColMatrix(ArrayList<Integer> vsmSingleDim, 	HashMap<Integer, int[]> phIdContextIds) {
		ArrayList<byte[] > rowbycolmatrix = new ArrayList<byte[] >();
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter ("/users/grad/rakib/java/RelatednessSolution/relatedness/src/main/java/dal/dimensionality/reduction/fastmap/144-phs-row-col-no-comma"));

			int totalOneCount = 0;
			
			for(Integer phId: phIdContextIds.keySet()){
				int [] ctxids = phIdContextIds.get(phId);
				
				byte[] sparseVector = new byte[vsmSingleDim.size()]; 
				
				int onecount = 0;
				
				for(int ctxid: ctxids){
					//find index of ctxId in vsmSingleDim
					int ctxIdIndex = Collections.binarySearch(vsmSingleDim, ctxid);
					if(ctxIdIndex>=0 ){
						sparseVector[ctxIdIndex]=1;
						onecount++;
					}
				}
				
				for(int i=0; i< sparseVector.length-1;i++){
					//String str = String.format("", sparseVector[i]);
					bw.write(sparseVector[i]+" ");
				}
				
				bw.write(sparseVector[sparseVector.length-1]);
				
				bw.write("\n");
				System.out.println(sparseVector.length+", onecount="+onecount);
				totalOneCount= totalOneCount+ onecount;
				//nbymMatrix.add(sparseVector);
				
			}
			
			bw.close();
			
			System.out.println("totalOneCount="+totalOneCount);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return rowbycolmatrix;
	}

	private TreeSet<Integer> CreateVsmByMargeAllthePhIds(){
		 TreeSet<Integer> mergedPhIds = new TreeSet<Integer>();
		 
		 try{
			 for(Integer phId: dimReductionFourgramUtil.getfourGmPhIdContextIds().keySet()){
				for(int ctxId:  dimReductionFourgramUtil.getfourGmPhIdContextIds().get(phId)){
					mergedPhIds.add(ctxId);
				}
			 }
		 }
		 catch(Exception e){
				e.printStackTrace();
			}
		 
		 return mergedPhIds;
	}

	private int[] FindTwoDistantPoints() {
		
		int [] twoDistantPoints = new int[2];
		
		try{
		
			Integer[] ctxIds = (Integer []) dimReductionFourgramUtil.getfourGmPhIdContextIds().keySet().toArray();
			
			twoDistantPoints[0] = ctxIds[ DimReductionUtil.randInt(0, ctxIds.length)];
			twoDistantPoints[1] = FindFurthestPoint(twoDistantPoints[0]); 
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return twoDistantPoints;
	}

	private int FindFurthestPoint(int firstRandomPtPhIdIndex) {
		int furthestPtIndex = 0;
		try{
			
			double maxDist = Double.MIN_VALUE;
			
			int count =-1;
			
			for(int phIndex: dimReductionFourgramUtil.getfourGmPhIdContextIds().keySet()){
				
				count++;
				
				//should ignore previous furthest point pairs
				
				if(phIndex==firstRandomPtPhIdIndex) {
					continue;
				}
				
				double  dist = FindPointDistance(dimReductionFourgramUtil.getfourGmPhIdContextIds().get(firstRandomPtPhIdIndex), 
						dimReductionFourgramUtil.getfourGmPhIdContextIds().get(phIndex));
				
				if(dist> maxDist){
					maxDist = dist;
					furthestPtIndex = count;
				}
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return furthestPtIndex;
	}

	private double FindPointDistance(int[] ctxArray1, int[] ctxArray2) {
		double dist = Double.MIN_VALUE;
		try{
			
			if(AnyCommonContextIds(ctxArray1, ctxArray2)){
				
				HashSet<Integer> ctxIdSet1 = UtilsShared.ConvertIntArrayToHashSet(ctxArray1);
				int commonCount =0;
				for(int ctxId2: ctxArray2){
					if(ctxIdSet1.contains(ctxId2)){
						commonCount ++;
					}
				}
				
				 double cosSim = commonCount/(Math.sqrt(ctxArray1.length)*Math.sqrt(ctxArray2.length));
				 dist = Math.sqrt(2*(1-cosSim));
			}
			else{
				dist = Math.sqrt(2);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return dist;
	}
	
	private boolean AnyCommonContextIds(int[] ph1CtxIdFreqArray, int[] ph2CtxIdFreqArray) {

		try{
			int ph1CtxIdFirst = ph1CtxIdFreqArray[0];
			int ph1CtxIdLast = ph1CtxIdFreqArray[ph1CtxIdFreqArray.length-1];
			
			int ph2CtxIdFirst = ph2CtxIdFreqArray[0];
			int ph2CtxIdLast = ph2CtxIdFreqArray[ph2CtxIdFreqArray.length-1];
			
			return UtilsShared.AnyOverlapBetweenTwoRanges(ph1CtxIdFirst, ph1CtxIdLast, ph2CtxIdFirst, ph2CtxIdLast);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
