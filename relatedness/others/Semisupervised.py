import re
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_selection import SelectKBest, chi2
import numpy as np
import random
import sys
from time import time
from sklearn import metrics
from sklearn.linear_model import RidgeClassifier
from sklearn.svm import LinearSVC
from sklearn.linear_model import SGDClassifier
from sklearn.linear_model import Perceptron
from sklearn.naive_bayes import BernoulliNB, MultinomialNB
from sklearn.neighbors import KNeighborsClassifier
from sklearn.linear_model import LogisticRegression
from sklearn import linear_model

def preprocess(str):
 str=re.sub(r'\b[a-z]\b|\b\d+\b', '', str)
 str=re.sub(r'\s+',' ',str).strip()
 return str


#file=open('D:/Google-n-gram/stopwords/stopWords.txt',"r")
#stopwords=file.readlines()
#file.close() 
 
#file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
lines = file.readlines()
data = []
labels = []
np.random.shuffle(lines)
#random.shuffle(lines)
file.close()

for line in lines:
 line=line.lower() 
 arr = line.split("\t", 1)
 str = preprocess(arr[1])
 #if len(str) >0:
 # print('data=',len(str)) 
 data.append(str)
 labels.append(arr[0])

data_train,data_test,labels_train,labels_test=train_test_split(data,labels, test_size=0.80, random_state=0)

file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/80-800-body-label","w")

for i in range(len(data_test)):
 labelbody = labels_test[i]+"\t"+data_test[i]
 file.write(labelbody+"\n")

file.close()


vectorizer = TfidfVectorizer(max_df=0.15, min_df=1, stop_words='english', use_idf=True, smooth_idf=True, norm='l2')
X_train = vectorizer.fit_transform(data_train)
X_test = vectorizer.transform(data_test)

#km = KMeans(n_clusters=20, init='k-means++', max_iter=100, n_init=5, verbose=True, random_state=10)
#t0 = time()
#km.fit(X_test)
#print("done in %0.3fs" % (time() - t0))

#print("Homogeneity: %0.3f" % metrics.homogeneity_score(labels_test, km.labels_))
#print("Completeness: %0.3f" % metrics.completeness_score(labels_test, km.labels_))
#print("V-measure: %0.3f" % metrics.v_measure_score(labels_test, km.labels_))

#agcluster = AgglomerativeClustering(n_clusters=20, affinity='euclidean', compute_full_tree='auto', linkage='ward')
#agLabels = agcluster.fit_predict(X_test.toarray())

#print("Homogeneity: %0.3f" % metrics.homogeneity_score(labels_test, agLabels))
#print("Completeness: %0.3f" % metrics.completeness_score(labels_test, agLabels))
#print("V-measure: %0.3f" % metrics.v_measure_score(labels_test, agLabels))


t0 = time()
ch2 = SelectKBest(chi2, k=800)
X_train = ch2.fit_transform(X_train, labels_train)
X_test = ch2.transform(X_test)
print("done in %fs" % (time() - t0))

np.savetxt("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/t80-800", X_test.toarray(), delimiter=' ', fmt='%.6f', newline='\n')

fastcluster.linkage(X_test, method='ward', metric='euclidean')

km = KMeans(n_clusters=20, init='k-means++', max_iter=100, n_init=5, verbose=True, random_state=10)
t0 = time()
km.fit(X_test)
print("done in %0.3fs" % (time() - t0))

#y_test = [int(i) for i in labels]
#pred_test = [int(i) for i in km.labels_]

print("Homogeneity: %0.3f" % metrics.homogeneity_score(labels_test, km.labels_))
print("Completeness: %0.3f" % metrics.completeness_score(labels_test, km.labels_))
print("V-measure: %0.3f" % metrics.v_measure_score(labels_test, km.labels_))

#spec = SpectralClustering(n_clusters=20, eigen_solver='arpack', random_state=0, n_init=10, gamma=1.0, affinity='rbf', n_neighbors=10, eigen_tol=0.0, assign_labels='kmeans', degree=3, coef0=1, n_jobs=1)
#spec_labels = spec.fit_predict(X_test)

#print("Homogeneity: %0.3f" % metrics.homogeneity_score(labels_test, spec_labels))
#print("Completeness: %0.3f" % metrics.completeness_score(labels_test, spec_labels))
#print("V-measure: %0.3f" % metrics.v_measure_score(labels_test, spec_labels))
