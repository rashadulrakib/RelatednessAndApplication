package dal.dimensionality.reduction.tsne;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import dal.dimensionality.reduction.utils.DimReductionUtil;
import dal.relatedness.phrase.load.nontokenized.bigram.LoadNonTokenizeBigram;
import dal.relatedness.phrase.load.nontokenized.unigram.LoadNonTokenizeUnigram;


public class TsneInputMatrixConstructor {

//	public LoadNonTokenizeUnigram loadNonTokenizeUnigram;
	public LoadNonTokenizeBigram loadNonTokenizeBigram;
	
	VsmConstructorTrigram vsmConstructorTrigram;
	VsmConstructorFourgram vsmConstructorFourgram;
	
	DimReductionUtil dimReductionUtil; 

	public TsneInputMatrixConstructor(){
		//loadNonTokenizeUnigram = new LoadNonTokenizeUnigram();
		loadNonTokenizeBigram = new LoadNonTokenizeBigram();
		dimReductionUtil = new DimReductionUtil();
		
		vsmConstructorTrigram = new VsmConstructorTrigram();
		vsmConstructorFourgram = new VsmConstructorFourgram();
				
		//loadNonTokenizeUnigram.PopulateStemmedUniGramsHashMap();
		loadNonTokenizeBigram.PopulateStemmedBiGramsListHashMap();
		
	}
	
	public void ConstructInputMatrix(){
		try{
			
			//TreeSet<Integer> vsmSingleDim = dimReductionUtil.CreateVsmSingleDimension(loadNonTokenizeBigram.getAlStemmedCrossCheckedBgFreqs());
			
//			//List<TreeSet<Integer>> lstVsm3g = vsmConstructorTrigram.ConstructVsmTriGram(vsmSingleDim);
//			List<byte []> lstVsm4g = vsmConstructorFourgram.ConstructVsmFourGram(new ArrayList<Integer>(vsmSingleDim));
//			
//			List<byte []> lstVsmAll = new ArrayList<byte[]>();
//			//lstVsmall.addAll(lstVsm3g);
//			lstVsmAll.addAll(lstVsm4g);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	
	
}
