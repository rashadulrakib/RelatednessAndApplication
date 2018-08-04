import re
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans, MiniBatchKMeans
from sklearn import metrics
import numpy as np
from time import time


file='/users/grad/rakib/dr.norbert/dataset/shorttext/agnews/agnews-w2vec-glove-vector-127600'
data = np.loadtxt(file, dtype='float', delimiter=' ')

data1= np.delete(data, [0], axis=1)
labels= data[:,0]

km = KMeans(n_clusters=4, init='random', max_iter=100, n_init=5, verbose=0, random_state=50)
t0 = time()
km.fit(data1)
print("done in %0.3fs" % (time() - t0))

np.savetxt('kmLabels', km.labels_ + 1, newline='\n', fmt='%i')

print("Homogeneity: %0.3f" % metrics.homogeneity_score(labels, km.labels_))
print("Completeness: %0.3f" % metrics.completeness_score(labels, km.labels_))
print("V-measure: %0.3f" % metrics.v_measure_score(labels, km.labels_))


##########cluster by tfidf############
import re
from sklearn.feature_extraction.text import TfidfVectorizer
from time import time
from sklearn.cluster import KMeans, MiniBatchKMeans
from sklearn import metrics
import numpy as np

def preprocess(str):
 str=re.sub(r'[^a-zA-Z0-9 ]', ' ', str)
 str=re.sub(r'\b[a-z]\b|\b\d+\b', '', str)
 str=re.sub(r'lt', ' ', str)
 str=re.sub(r'gt', ' ', str)
 str=re.sub(r'\s+',' ',str).strip()
 return str

filepath = "/home/owner/PhD/dr.norbert/dataset/shorttext/agnews/agnewsdataraw-whole"
file1=open(filepath,"r")
lines = file1.readlines()
file1.close()

data = []
labels = []

for line in lines:
 arr = line.strip().split("\t")
 str = preprocess(arr[1])
 data.append(str)
 labels.append(arr[0])

vectorizer = TfidfVectorizer(max_df=0.25, min_df=1, stop_words='english', use_idf=True, smooth_idf=True, norm='l2')
X = vectorizer.fit_transform(data)

km = KMeans(n_clusters=4, init='k-means++', max_iter=100, n_init=5, verbose=0)
t0 = time()
km.fit(X)
print("done in %0.3fs" % (time() - t0))

np.savetxt('/home/owner/PhD/dr.norbert/dataset/shorttext/agnews/agnews-127600-tfidf-labels', km.labels_ + 1, newline='\n', fmt='%i')

print("Homogeneity: %0.3f" % metrics.homogeneity_score(labels, km.labels_))
print("Completeness: %0.3f" % metrics.completeness_score(labels, km.labels_))
print("V-measure: %0.3f" % metrics.v_measure_score(labels, km.labels_))



