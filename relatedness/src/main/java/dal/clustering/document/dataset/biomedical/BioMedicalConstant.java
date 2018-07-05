package dal.clustering.document.dataset.biomedical;

public class BioMedicalConstant {
	
	public static final String  BiomedicalTextArffFile ="D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedicalrawText.arff";
	//public static final String BiomedicalDocsFile =  "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedicalraw";
	//public static final String BiomedicalW2VecArffFile =  "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedicalrawW2Vec.arff";
	public static final String BiomedicalW2VecArffFile =  "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedicalMeshW2Vec.arff";

	//public static final String BiomedicalDocsFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedicalraw";
	//public static final String BiomedicalDocsFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/80-800-body-label";
    //public static final String BiomedicalDocsFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\biomedicalraw";
	public static final String BiomedicalDocsFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\80-800-body-label";
	//public static final String BiomedicalDocsFile = "C:\\Users\\mona\\Desktop\\BioASQ\\MTI\\MTI-strict-filer-wsd\\text.processed.txt";
	
	public static final String BioMedicalSTC2VocabIndexIn="D:\\PhD\\dr.norbert\\method\\STC2-master\\STC2-master\\dataset\\Biomedical_vocab2idx.dic";
	public static final String BioMedicalSTC2TextIndexOut="D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\STC2\\Biomedical_index.txt";
	public static final String BioMedicalSTC2RawOut="D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\STC2\\Biomedical.txt";
	public static final String BioMedicalSTC2GndOut="D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\STC2\\Biomedical_gnd.txt";

	public static final String BioMedicalBodies = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\BTM\\BiomedicalBodies.txt";
	
	public static final String BioMedicalBioASQVector = "/users/grad/rakib/w2vecs/biomedical/BioASQ/word2vecTools/vectors.txt";
	public static final String BioMedicalBioASQWord = "/users/grad/rakib/w2vecs/biomedical/BioASQ/word2vecTools/types.txt";
	public static final String BioMedicalBioASQCombined = "/users/grad/rakib/w2vecs/biomedical/BioASQ/bioasqvectors.txt";
    //public static final String BioMedicalBioASQ2018 = "/users/grad/rakib/w2vecs/biomedical/BioASQ/2018/bioasqvectors2018.txt";
	public static final String BioMedicalBioASQ2018 = "D:\\PhD\\w2vecs\\biomedical\\BioASQ\\2018\\pubmed2018_w2v_200D.txt";
	public static final int BioASQ2018W2VecDimension = 200;
	
	public static final String BioMedicalBioNlpPMC = "/users/grad/rakib/w2vecs/biomedical/BioNlp/PMC-w2v.txt";
	public static final String BioMedicalBioNlpPubMed = "/users/grad/rakib/w2vecs/biomedical/BioNlp/PubMed-w2v.txt";

	public static final int NumberOfClusters = 20;
	//public static final String KeptFtrsFile = "D:\\PhD\\dr.norbert\\dataset\\shorttext\\biomedical\\semisupervised\\features-7000.txt";
	public static final String KeptFtrsFile = "/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/features-7000.txt";
}
