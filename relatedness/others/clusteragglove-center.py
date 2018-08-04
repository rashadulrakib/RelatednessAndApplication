import re
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans, MiniBatchKMeans
from sklearn import metrics
import numpy as np
from time import time

##load center
centers = np.loadtxt('/home/owner/PhD/dr.norbert/dataset/shorttext/agnews/semisupervised/centers-127600', dtype='float', delimiter=' ')
#end

file='/home/owner/PhD/dr.norbert/dataset/shorttext/agnews/agnews-w2vec-glove-vector-body-title-127600'
data = np.loadtxt(file, dtype='float', delimiter=' ')

data1= np.delete(data, [0], axis=1)
labels= data[:,0]

km = KMeans(n_clusters=4, init=centers, max_iter=100, n_init=5, verbose=1, random_state=0)
t0 = time()
km.fit(data1)
print("done in %0.3fs" % (time() - t0))

np.savetxt('/home/owner/PhD/dr.norbert/dataset/shorttext/agnews/agnews-127600-glove-labels', km.labels_ + 1, newline='\n', fmt='%i')

print("Homogeneity: %0.3f" % metrics.homogeneity_score(labels, km.labels_))
print("Completeness: %0.3f" % metrics.completeness_score(labels, km.labels_))
print("V-measure: %0.3f" % metrics.v_measure_score(labels, km.labels_))
