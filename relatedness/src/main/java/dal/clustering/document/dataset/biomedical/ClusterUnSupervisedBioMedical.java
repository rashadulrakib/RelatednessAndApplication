package dal.clustering.document.dataset.biomedical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import dal.clustering.document.shared.ClusterEvaluation;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringText;
import dal.clustering.document.shared.cluster.UnSupervisedClusteringW2Vec;
import dal.clustering.document.shared.entities.ClusterResultConatainerText;
import dal.clustering.document.shared.entities.ClusterResultConatainerVector;
import dal.utils.common.general.UtilsShared;

public class ClusterUnSupervisedBioMedical {

	UnSupervisedClusteringText unSupervisedClusteringText;
	UnSupervisedClusteringW2Vec unSupervisedClusteringW2Vec;
	ClusterEvaluation clusterEvaluation;
	BioMedicalUtil bioMedicalUtil;
	
	public ClusterUnSupervisedBioMedical() throws IOException{
		bioMedicalUtil = new BioMedicalUtil();
		clusterEvaluation = new ClusterEvaluation(bioMedicalUtil.docClusterUtil);
		unSupervisedClusteringW2Vec = new UnSupervisedClusteringW2Vec(bioMedicalUtil.getUniqueWords(),
				bioMedicalUtil.getDocsBiomedicalFlat(), bioMedicalUtil.getDocsBiomedicalList(), 
				bioMedicalUtil.docClusterUtil);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(bioMedicalUtil.docClusterUtil, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
		//unSupervisedClusteringText = new UnSupervisedClusteringText(bioMedicalUtil.docClusterUtil);
	}

	public void ClusterDocsNGramBasedSimilarityGtm() {
		try{
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = bioMedicalUtil.getDocsBiomedicalList();
			ArrayList<String []> alDocLabelFlat = bioMedicalUtil.getDocsBiomedicalFlat();
			
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = bioMedicalUtil
					.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);
//			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
//			hmTrainDocsLabelBody.put("13", new ArrayList<String>(Arrays.asList("occurrence bombesin alytesin extracts skin three european discoglossid frogs pharmacological actions bombesin extravascular smooth muscle")));
//			hmTrainDocsLabelBody.put("17", new ArrayList<String>(Arrays.asList("studies fibrinolytic system ruptured intracranial aneurysm fibrinogen changes acute stage sah risk factor ischemic complications vasospasm author transl")));
//			hmTrainDocsLabelBody.put("14", new ArrayList<String>(Arrays.asList("year prospective study relationship habitual vocational physical activity risk incidence ischemic heart disease volunteer male federal employees")));
//			hmTrainDocsLabelBody.put("8", new ArrayList<String>(Arrays.asList("genetic control immune responses vitro development primary secondary plaque forming cell responses random terpolymer glutamic acid alanine tyrosine gat mouse spleen cells vitro")));
//			hmTrainDocsLabelBody.put("1", new ArrayList<String>(Arrays.asList("age changes oxygen consumption sdh activity liver homogenate male garden lizards malonate short term cold stress")));
//			hmTrainDocsLabelBody.put("2", new ArrayList<String>(Arrays.asList("olivocochlear vestibular efferent neurons feline brain stem location morphology number determined retrograde axonal transport acetylcholinesterase histochemistry")));
//			hmTrainDocsLabelBody.put("16", new ArrayList<String>(Arrays.asList("effects utero viral infection embryonic fetal neonatal survival comparison smedi porcine picorna viruses hog cholera vaccinal virus")));
//			hmTrainDocsLabelBody.put("4", new ArrayList<String>(Arrays.asList("oxygenation methyl desacetoxycephalosporin beta aminadipamido methylceph em carboxylic acid hydroxymethyl desacetylcephalosporin oxoglutarate linked dioxygenases acremonium chrysogenum streptomyces clavuligerus")));
//			hmTrainDocsLabelBody.put("18", new ArrayList<String>(Arrays.asList("cell mediated immune response gonococcal antigen uro arthritis reiter disease inhibition migration sensitised human lymphocytes lymphocyte transformation vitro measured thymidine uptake")));
//			hmTrainDocsLabelBody.put("19", new ArrayList<String>(Arrays.asList("massive hemorrhage caused acute solitary gastric erosion dieulafoy change prognosis preoperative endoscopy author transl")));
//			hmTrainDocsLabelBody.put("15", new ArrayList<String>(Arrays.asList("interstitial pneumonia children malignancies cytotoxic therapie clinical picture analysis promoting factors detailed discussion etiology author transl")));
//			hmTrainDocsLabelBody.put("10", new ArrayList<String>(Arrays.asList("third kasugamycin resistance locus ksgc ribosomal protein escherichia coli")));
//			hmTrainDocsLabelBody.put("12", new ArrayList<String>(Arrays.asList("investigation relationship cell surface calcium ion gating phosphatidylinositol turnover comparison effects elevated extracellular potassium ion concentration ileium smooth muscle pancreas")));
//			hmTrainDocsLabelBody.put("3", new ArrayList<String>(Arrays.asList("reconstitution oxidative phosphorylation adenosine triphosphate dependent transhydrogenase activity combination membrane fractions unca uncb mutant strains escherichia coli")));
//			hmTrainDocsLabelBody.put("5", new ArrayList<String>(Arrays.asList("nuclear magnetic resonance aromatic amino acid residues determine midpoint oxidation reduction potential iron sulfur cluster clostridium acidi urici clostridium pasteurianum ferredoxins")));
//			hmTrainDocsLabelBody.put("6", new ArrayList<String>(Arrays.asList("variation blood glucose serum growth hormone prolactin insulin subjects insulin dependent diabetes ogtt pretreatment br alpha ergocryptine")));
//			hmTrainDocsLabelBody.put("11", new ArrayList<String>(Arrays.asList("intrarenal renin angiotensin sodium interdependent mechanism controlling postclamp renal artery pressure renin release conscious dog chronic kidney goldblatt hypertension")));
//			hmTrainDocsLabelBody.put("7", new ArrayList<String>(Arrays.asList("action bacterial growth sarcoplasmic urea soluble proteins muscle effects clostridium perfringens salmonella enteritidis achromobacter liquefaciens streptococcus faecalis kurthia zopfi")));
//			hmTrainDocsLabelBody.put("20", new ArrayList<String>(Arrays.asList("electron spinal resonance analysis nitroxide spin label tetramethylpipidone oxyl tempone single crystals reduced tempone matrix")));
//			hmTrainDocsLabelBody.put("9", new ArrayList<String>(Arrays.asList("linkage disequilibrium subdivided populations")));
			
//			ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringText.PerformUnSeupervisedSeededClusteringByGtmWordSim
//					(hmTrainDocsLabelBody, alDocLabelFlat);
			ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringText
					.PerformUnSeupervisedSeededClusteringByGtmWordSimIterative(hmTrainDocsLabelBody, alDocLabelFlat);
			
			clusterEvaluation.ClusterEvaluationGeneratorText(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneText(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(clusterResultConatainerText.FinalCluster);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void ClusterDocsSimilarityByW2VecFollowingGtm() {
		try{
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = bioMedicalUtil.getDocsBiomedicalList();
			ArrayList<String []> alDocLabelFlat = bioMedicalUtil.getDocsBiomedicalFlat();
			
			LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = bioMedicalUtil
					.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);
			
			ClusterResultConatainerText clusterResultConatainerText = unSupervisedClusteringW2Vec.PerformUnSeuperVisedSeededClusteringByW2VecFollowingGtm
					(hmTrainDocsLabelBody, alDocLabelFlat);
			
			clusterEvaluation.EvalSemiSupervisedByAccOneToOneText(clusterResultConatainerText.FinalCluster);
			clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingText(clusterResultConatainerText.FinalCluster);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterDocsSimilarityByW2VecAverageVector() {
		try{
			
			LinkedHashMap<String, ArrayList<String>> docsLabelBody = bioMedicalUtil.getDocsBiomedicalList();
			ArrayList<String []> alDocLabelFlat = bioMedicalUtil.getDocsBiomedicalFlat();
			
			//for(int seed=0;seed<=0;seed++)
			{
//				System.out.println("seed="+0);
//				LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = bioMedicalUtil
//						.docClusterUtil.GetTrainSeedDocuments(docsLabelBody, 1, 0);
				
				LinkedHashMap<String, ArrayList<String>> hmTrainDocsLabelBody = new LinkedHashMap<String, ArrayList<String>>();
				hmTrainDocsLabelBody.put("13", new ArrayList<String>(Arrays.asList("investigation relationship cell surface calcium ion gating phosphatidylinositol turnover comparison effects elevated extracellular potassium ion concentration ileium smooth muscle pancreas")));
				hmTrainDocsLabelBody.put("17", new ArrayList<String>(Arrays.asList("time course changes blood pressure urinary excretion norepinephrine dopamine administration fusaric acid single dose elderly hypertensive patients")));
				hmTrainDocsLabelBody.put("14", new ArrayList<String>(Arrays.asList("studies fibrinolytic system ruptured intracranial aneurysm fibrinogen changes acute stage sah risk factor ischemic complications vasospasm author transl")));
				hmTrainDocsLabelBody.put("8", new ArrayList<String>(Arrays.asList("cell mediated immune response gonococcal antigen uro arthritis reiter disease inhibition migration sensitised human lymphocytes lymphocyte transformation vitro measured thymidine uptake")));
				hmTrainDocsLabelBody.put("1", new ArrayList<String>(Arrays.asList("relationship reversibility fibril formation subunit composition rat skin collagen")));
				hmTrainDocsLabelBody.put("2", new ArrayList<String>(Arrays.asList("olivocochlear vestibular efferent neurons feline brain stem location morphology number determined retrograde axonal transport acetylcholinesterase histochemistry")));
				hmTrainDocsLabelBody.put("16", new ArrayList<String>(Arrays.asList("induction lymphocyte responses small molecular weight antigen failure induce tolerance azobenzenearsonate aba specific cells guinea pigs aba conjugate copolymer glutamic acid lysine gl")));
				hmTrainDocsLabelBody.put("4", new ArrayList<String>(Arrays.asList("papyraceous ventricle adult apropos anatom clinical case observed man suffering spondylo epiphyseal dysplasia")));
				hmTrainDocsLabelBody.put("18", new ArrayList<String>(Arrays.asList("assessment functional activity human lymphocytes malignant disease local graft versus host reaction rats rosette forming cell test")));
				hmTrainDocsLabelBody.put("19", new ArrayList<String>(Arrays.asList("ray diffraction myelin membrane ii determination phase angles frog sciatic nerve heavy atom labeling calculation electron density distribution membrane")));
				hmTrainDocsLabelBody.put("15", new ArrayList<String>(Arrays.asList("year prospective study relationship habitual vocational physical activity risk incidence ischemic heart disease volunteer male federal employees")));
				hmTrainDocsLabelBody.put("10", new ArrayList<String>(Arrays.asList("reconstitution oxidase activity membranes derived aminolaevulinic acid requiring mutant escherichia coli")));
				hmTrainDocsLabelBody.put("12", new ArrayList<String>(Arrays.asList("maturation responsiveness cardioactive drugs differential effects acetylcholine norepinephrine theophylline tyramine glucagon dibutyryl cyclic amp atrial rate hearts fetal mice")));
				hmTrainDocsLabelBody.put("3", new ArrayList<String>(Arrays.asList("extracellular enzyme system utilized fungus sporotrichum pulverulentum chrysosporium lignorum breakdown cellulose activities endo beta glucanases carboxymethylcellulose")));
				hmTrainDocsLabelBody.put("5", new ArrayList<String>(Arrays.asList("glycogenesis glyconeogenesis human platelets incorporation glucose pyruvate citrate platelet glycogen glycogen synthetase fructose diphosphatase activity")));
				hmTrainDocsLabelBody.put("6", new ArrayList<String>(Arrays.asList("adult respiratory distress syndrome postoperative patients study pulmonary pathology shock lung prophylactic therapeutic implications")));
				hmTrainDocsLabelBody.put("11", new ArrayList<String>(Arrays.asList("dopa administration fine structures brain heart kidney rats special reference appearance microvascular thrombosis pathological significance")));
				hmTrainDocsLabelBody.put("7", new ArrayList<String>(Arrays.asList("system analysis multi oscillatory functional order circadian ultradian frequency range monitoring load effects exemplified varying light dark regimen intensive rearing sheep")));
				hmTrainDocsLabelBody.put("20", new ArrayList<String>(Arrays.asList("natriferic hydrosmotic effects neurohypophysial peptides analogues augmenting fluid uptake bufo melanostictus")));
				hmTrainDocsLabelBody.put("9", new ArrayList<String>(Arrays.asList("nuclear magnetic resonance aromatic amino acid residues determine midpoint oxidation reduction potential iron sulfur cluster clostridium acidi urici clostridium pasteurianum ferredoxins")));
				
				ClusterResultConatainerVector clusterResultConatainer = unSupervisedClusteringW2Vec.PerformUnSeuperVisedSeededClusteringByW2VecAverageVec
						(hmTrainDocsLabelBody, alDocLabelFlat);
				
				clusterEvaluation.ClusterEvaluationGeneratorVector(clusterResultConatainer.FinalCluster);
				clusterEvaluation.EvalSemiSupervisedByAccOneToOneVector(clusterResultConatainer.FinalCluster);
				clusterEvaluation.EvalSemiSupervisedByPurityMajorityVotingVector(clusterResultConatainer.FinalCluster);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterDocsBySimilarityMatrixGtm(){
		try{
			
			ArrayList<String []> alDocLabelFlat = bioMedicalUtil.getDocsBiomedicalFlat();
			
			//alDocLabelFlat = bioMedicalUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 150, 0);
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilGtm);

			double [][] saprsifyMatrix = bioMedicalUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, BioMedicalConstant.BiomedicalDocsFile, "\t");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void ClusterDocsBySimilarityMatrixCosineW2Vec(){
		try{
			ArrayList<String []> alDocLabelFlat = bioMedicalUtil.getDocsBiomedicalFlat();
			
			//alDocLabelFlat = bioMedicalUtil.docClusterUtil.SampledDocsPerCategory(alDocLabelFlat, 150, 0);
			
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			
			double [][] saprsifyMatrix = bioMedicalUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrix(docSimMatrix);
			
			UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\2018\\jan\\sparseMatrix", saprsifyMatrix, " ");
			
			//UtilsShared.ReWriteDocBodyLabelFile(alDocLabelFlat, BioMedicalConstant.BiomedicalDocsFile, "\t");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixCosineW2VecFixedSparsification(){
		try{
			ArrayList<String []> alDocLabelFlat = bioMedicalUtil.getDocsBiomedicalFlat();
			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeCosineMatrixW2Vec(alDocLabelFlat, unSupervisedClusteringW2Vec.docClusterUtilW2Vec);
			double [][] saprsifyMatrix = bioMedicalUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, BioMedicalConstant.NumberOfClusters);
			
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\data-web-snippets\\sparseMatrix-w2vec-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/sparseMatrix-w2vec-sd-0-Fixed", saprsifyMatrix, " ");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void GenerateDocsDisSimilarityMatrixGtmFixedSparsification(){
		try{
			ArrayList<String []> alDocLabelFlat =bioMedicalUtil.getDocsBiomedicalFlat();
			//ArrayList<String []> alDocLabelFlat = new ArrayList<String []>(googlewebSnippetUtil.GetDocsGoogleWebSnippetFlat().subList(0, 20));

			double [][] docSimMatrix= bioMedicalUtil.docClusterUtil.ComputeSimilarityMatrixGtm(alDocLabelFlat, unSupervisedClusteringText.docClusterUtilGtm);
			double [][] saprsifyMatrix = bioMedicalUtil.docClusterUtil.sparsificationUtil.SparsifyDocDisSimilarityMatrixAlgorithomic(docSimMatrix, BioMedicalConstant.NumberOfClusters);
			
			//UtilsShared.WriteMatrixToFile("D:\\PhD\\dr.norbert\\dataset\\shorttext\\stackoverflow\\sparseMatrix-gtm-sd-0-Fixed", saprsifyMatrix, " ");
			UtilsShared.WriteMatrixToFile("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/sparseMatrix-gtm-sd-0-Fixed", saprsifyMatrix, " ");
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
