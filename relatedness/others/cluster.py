import re
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans, MiniBatchKMeans
from sklearn import metrics
import numpy as np
from time import time
from sklearn.decomposition import PCA
from sklearn.decomposition import TruncatedSVD
from sklearn.feature_extraction.text import HashingVectorizer
from sklearn.feature_extraction.text import TfidfTransformer
from sklearn.pipeline import make_pipeline
from sklearn.preprocessing import Normalizer

def preprocess(str):
 str=re.sub(r'\b[a-z]\b|\b\d+\b', '', str)
 str=re.sub(r'\s+',' ',str).strip()
 return str

file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
lines = file.readlines()
file.close()
data = []
labels = []
#np.random.shuffle(lines)
#random.shuffle(lines)

for line in lines:
 arr = line.strip().split("\t")
 str = preprocess(arr[1])
 data.append(str)
 labels.append(arr[0])
 
vectorizer = TfidfVectorizer(max_df=0.25, min_df=1, stop_words='english', use_idf=True, smooth_idf=True, norm='l2')
X = vectorizer.fit_transform(data)
rdata = PCA(n_components=2).fit_transform(X.toarray())
#svd = TruncatedSVD(100)
#normalizer = Normalizer(copy=False)
#lsa = make_pipeline(svd, normalizer)
#rdata = lsa.fit_transform(X)

#km = MiniBatchKMeans(n_clusters=20, init='k-means++', n_init=1, init_size=1000, batch_size=1000, verbose=opts.verbose)
km = KMeans(n_clusters=20, init='k-means++', max_iter=100, n_init=1, verbose=True)
t0 = time()
km.fit(rdata)
#km.fit(rdata)
print("done in %0.3fs" % (time() - t0))

#y_test = [int(i) for i in labels]
#pred_test = [int(i) for i in km.labels_]

print("Homogeneity: %0.3f" % metrics.homogeneity_score(labels, km.labels_))
print("Completeness: %0.3f" % metrics.completeness_score(labels, km.labels_))
print("V-measure: %0.3f" % metrics.v_measure_score(labels, km.labels_))
