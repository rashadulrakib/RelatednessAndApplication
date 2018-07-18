import re
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import FeatureAgglomeration
import numpy as np
from time import time

def preprocess(str):
 str=re.sub(r'\b[a-z]\b|\b\d+\b', '', str)
 str=re.sub(r'\s+',' ',str).strip()
 return str

file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
lines = file.readlines()
data = []
labels = []
file.close()

for line in lines:
 line=line.lower() 
 arr = line.split("\t", 1)
 str = preprocess(arr[1])
 data.append(str)
 labels.append(arr[0])

vectorizer = TfidfVectorizer(max_df=0.15, min_df=1, stop_words='english', use_idf=True, smooth_idf=True, norm='l2')
X = vectorizer.fit_transform(data) 

ftrCluster = FeatureAgglomeration(n_clusters=20, affinity='euclidean',compute_full_tree='auto', linkage='ward')
fittans = ftrCluster.fit_transform(X.toarray())

np.savetxt('/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/hacftrtfidf_20',fittans, delimiter=' ', fmt='%.10f')

############

from sklearn.cluster import KMeans
import numpy as np
import sys
import math
from sklearn import svm
import collections
 
data = np.loadtxt('/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/hacftrtfidf', dtype=float, delimiter=' ')
km = KMeans(n_clusters=20, init='k-means++', max_iter=100, n_init=5, verbose=False, random_state=0)
km.fit(data)
preds = km.labels_
np.savetxt('/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/hacftrtfidf_20_labels',preds, delimiter='\n', fmt='%.0d')
#print(km.cluster_centers_)
#print(km.inertia_)


#def closestDistWithCenterIndex(x, centers):
# minJ=0
# minDist=sys.float_info.max 
# for j in len(range(centers)):
#  center = centers[j]  
  #print(center)
#  dist = euclidean_distances(center, x)
#  print(dist)   
  #if minDist > dist:
  #  minJ = j
  #  minDist = dist
	
 #return minJ, minDist

def euclidean_dist(data_x, data_y):
 if len(data_x) != len(data_y):
  raise Exception('Data sets must be the same dimension')
 dimensions = len(data_x)
 sum_dims = 0
 for dim in range(0, dimensions):
  sum_dims = sum_dims + (data_x[dim] - data_y[dim])*(data_x[dim] - data_y[dim])
  
 return math.sqrt(sum_dims) 
 

centers = km.cluster_centers_ 
distSumCenetrs=[0] * len(km.cluster_centers_)
itemCountersInCluster=[0] * len(km.cluster_centers_)


dic = {}

for i in range(len(data)):
 x = data[i]
 label = km.labels_[i] 
 dic.setdefault(label, []).append((x, i/2000))
 minJ=0
 minDist=sys.float_info.max 
 for j in range(len(centers)):
  center = centers[j]  
  dist = euclidean_dist(center, x)
  if minDist > dist:
   minDist = dist
   minJ = j
   
 distSumCenetrs[minJ] = distSumCenetrs[minJ] + minDist 
 itemCountersInCluster[minJ] = itemCountersInCluster[minJ]+1

def tuple_cmp(a,b):
 if a[0] > b[0]:
  return 1 
 else:
  return -1
  
tuples = [] 
for j in range(len(distSumCenetrs)):
 t=(itemCountersInCluster[j]/distSumCenetrs[j], itemCountersInCluster[j], distSumCenetrs[j], j)
 tuples.append(t)  

tuples.sort(tuple_cmp)
for tup in tuples:
 print(tup)

train_tup=tuples[2]
train_data_index=train_tup[3]

tup_arr = dic[train_data_index] 
train_data_list = []
for t_tup in tup_arr:
 train_data_list.append(t_tup[0])

clf = svm.OneClassSVM(nu=0.1, kernel="rbf", gamma=0.1) 
clf.fit(train_data_list)
y_pred_train = clf.predict(train_data_list)
  
test_tup=tuples[19] 
test_data_index=test_tup[3]
test_tup_arr = dic[test_data_index] 
test_data_list = []
for t_tup in test_tup_arr:
 test_data_list.append(t_tup[0])

#clf.fit(test_data_list)
y_pred_test = clf.predict(test_data_list)

collections.Counter(y_pred_test)



#############################extract text and labels (train & test)##################
from sklearn.cluster import KMeans
import numpy as np
import sys
import math
from sklearn import svm
import collections
import re
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
 
#file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
lines = file.readlines()
textdata = []
textlabels = []
file.close()

for line in lines:
 line=line.lower() 
 arr = line.split("\t", 1)
 str = preprocess(arr[1])
 textdata.append(str)
 textlabels.append(arr[0])

 
data = np.loadtxt('/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/hacftrtfidf', dtype=float, delimiter=' ')
km = KMeans(n_clusters=20, init='k-means++', max_iter=100, n_init=5, verbose=False, random_state=0)
km.fit(data)
preds = km.labels_
np.savetxt('/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/hacftrtfidf_20_labels',preds, delimiter='\n', fmt='%.0d')

dic = {}

data_train= []
data_test= []
labels_train = []
labels_test = []

for i in range(len(textdata)):
 text_x = textdata[i]
 label = km.labels_[i] 
 dic.setdefault(label, []).append((text_x, i/2000))
 if label == 0:
  data_test.append(text_x) 
  labels_test.append(i/2000) 
 
 #if label == 16 or label==5 or label==17 or label==15 or label==2 or label==1 or label==7:
 if label==5 or label==2 or label==1 or label==7:
  data_train.append(text_x)
  labels_train.append(label)   

  
vectorizer = TfidfVectorizer(max_df=0.15, min_df=1, stop_words='english', use_idf=True, smooth_idf=True, norm='l2')
#vectorizer = TfidfVectorizer(max_df=0.15, min_df=1, stop_words=stopwords, use_idf=True, smooth_idf=True, norm='l2')
X_train = vectorizer.fit_transform(data_train)
X_test = vectorizer.transform(data_test)

t0 = time()
ch2 = SelectKBest(chi2, k=3000)
X_train = ch2.fit_transform(X_train, labels_train)
X_test = ch2.transform(X_test)
print("done in %fs" % (time() - t0))

clf = LogisticRegression() #52
#clf = RidgeClassifier(tol=1e-1) #52
#clf = Perceptron(n_iter=100)
#clf=KNeighborsClassifier(n_neighbors=10)
#clf = LinearSVC(loss='l2', C=1000, dual=False, tol=1e-3)
#clf = SGDClassifier(alpha=.0001, n_iter=50, penalty='l1')
#clf = SGDClassifier(alpha=.0001, n_iter=100, penalty='elasticnet')
#clf= MultinomialNB(alpha=.01)
#clf = BernoulliNB(alpha=.01)

t0 = time()
clf.fit(X_train, labels_train)
train_time = time() - t0
print ("train time: %0.3fs" % train_time)

t0 = time()
pred = clf.predict(X_test)
test_time = time() - t0
print ("test time:  %0.3fs" % test_time)

y_test = [int(i) for i in labels_test]
pred_test = [int(i) for i in pred]
score = metrics.homogeneity_score(y_test, pred_test)
print ("homogeneity_score:   %0.3f" % score)
score = metrics.completeness_score(y_test, pred_test)
print ("completeness_score:   %0.3f" % score)
score = metrics.v_measure_score(y_test, pred_test)
print ("v_measure_score:   %0.3f" % score)
score = metrics.accuracy_score(y_test, pred_test)
print ("acc_score:   %0.3f" % score)
score = metrics.normalized_mutual_info_score(y_test, pred_test)  
print ("nmi_score:   %0.3f" % score)
  
#####################real term clustering##############
 
from sklearn.cluster import KMeans
import numpy as np
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
#import sys
#import math
#from sklearn import svm
#import collections
import re
#from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_selection import SelectKBest, chi2
#import numpy as np
#import random
#from time import time
from sklearn import metrics
from sklearn.linear_model import RidgeClassifier
#from sklearn.svm import LinearSVC
#from sklearn.linear_model import SGDClassifier
#from sklearn.linear_model import Perceptron
#from sklearn.naive_bayes import BernoulliNB, MultinomialNB
#from sklearn.neighbors import KNeighborsClassifier
from sklearn.linear_model import LogisticRegression
#from sklearn import linear_model


def preprocess(str):
 str=re.sub(r'\b[a-z]\b|\b\d+\b', '', str)
 str=re.sub(r'\s+',' ',str).strip()
 return str
 
#file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
lines = file.readlines()
file.close()

textdata = []
textlabels = []

terms = []
stopWs = set(stopwords.words('english'))

for line in lines:
 line=line.lower() 
 arr = line.split("\t", 1)
 str = preprocess(arr[1])
 word_tokens = word_tokenize(str)
 filtered_sentence = [w for w in word_tokens if not w in stopWs]
 terms.extend(filtered_sentence)
 textdata.append(filtered_sentence)
 textlabels.append(arr[0])

terms = set(terms) 
bioMedicalBioASQ2018File = "/users/grad/rakib/w2vecs/biomedical/BioASQ/2018/pubmed2018_w2v_400D.txt"

termsVectors = []

file=open(bioMedicalBioASQ2018File,"r")
vectorlines = file.readlines()
file.close()

lineProgCount = 0
for vecline in vectorlines:
 vecarr = vecline.split()
 lineProgCount=lineProgCount+1
 if lineProgCount % 100000 ==0:
  print(lineProgCount)
 
 if len(vecarr) < 20:
  continue
 
 w2vecword = vecarr[0]
 if w2vecword in terms:
  termsVectors.append(vecline)

del vectorlines

vectors = []
termsVectorsDic = {}

for vecline in termsVectors:
 veclinearr = vecline.split()
 vecword = veclinearr[0]
 vecnumbers = map(float, veclinearr[1:]) 
 vectors.append(vecnumbers)
 termsVectorsDic[vecword]=vecnumbers

km = KMeans(n_clusters=20, random_state=0).fit(vectors) 

train_data = []
train_labels=[]

for i in range(len(vectors)):
 train_labels.append(km.labels_[i])
 train_data.append(vectors[i])

test_data=[]
test_labels=[]
 
for i in range(len(textdata)):
 
 words = textdata[i]
 sum_vecs = [0] * 400
 print(len(sum_vecs), i)
 for word in words:
  if word in termsVectorsDic:
   for j in range(len(sum_vecs)):
    sum_vecs[j]=sum_vecs[j]+termsVectorsDic[word][j]
   
 test_data.append(sum_vecs)
 test_labels.append(i/2000)
 

#ch2 = SelectKBest(chi2, k='all')
#X_train = ch2.fit_transform(train_data, train_labels)
#X_test = ch2.transform(test_data) 

clf = LogisticRegression() #52
clf = RidgeClassifier(tol=1e-1) #52

clf.fit(train_data, train_labels)
preds = clf.predict(test_data)

score = metrics.accuracy_score(test_labels, preds)
print ("acc_score:   %0.3f" % score)



#####################real term clustering by majority voting ##############
 
from sklearn.cluster import KMeans
import numpy as np
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
#import sys
#import math
#from sklearn import svm
import collections
import re
#from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_selection import SelectKBest, chi2
#import numpy as np
#import random
#from time import time
from sklearn import metrics
from sklearn.linear_model import RidgeClassifier
#from sklearn.svm import LinearSVC
#from sklearn.linear_model import SGDClassifier
#from sklearn.linear_model import Perceptron
#from sklearn.naive_bayes import BernoulliNB, MultinomialNB
#from sklearn.neighbors import KNeighborsClassifier
from sklearn.linear_model import LogisticRegression
#from sklearn import linear_model


def preprocess(str1):
 str1=re.sub(r'\b[a-z]\b|\b\d+\b', '', str1)
 str1=re.sub(r'\s+',' ',str1).strip()
 return str1
 
#file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
lines = file.readlines()
file.close()

textdata = []
textlabels = []

terms = []
stopWs = set(stopwords.words('english'))

for line in lines:
 line=line.lower() 
 arr = line.split("\t", 1)
 str1 = preprocess(arr[1])
 word_tokens = word_tokenize(str1)
 filtered_sentence = [w for w in word_tokens if not w in stopWs]
 terms.extend(filtered_sentence)
 textdata.append(filtered_sentence)
 textlabels.append(arr[0])

terms = set(terms) 
bioMedicalBioASQ2018File = "/users/grad/rakib/w2vecs/biomedical/BioASQ/2018/pubmed2018_w2v_400D.txt"

termsVectors = []

file=open(bioMedicalBioASQ2018File,"r")
vectorlines = file.readlines()
file.close()

lineProgCount = 0
for vecline in vectorlines:
 vecarr = vecline.split()
 lineProgCount=lineProgCount+1
 if lineProgCount % 100000 ==0:
  print(lineProgCount)
 
 if len(vecarr) < 20:
  continue
 
 w2vecword = vecarr[0]
 if w2vecword in terms:
  termsVectors.append(vecline)

del vectorlines

vectors = []
termsVectorsDic = {}

for vecline in termsVectors:
 veclinearr = vecline.split()
 vecword = veclinearr[0]
 vecnumbers = map(float, veclinearr[1:]) 
 vectors.append(vecnumbers)
 termsVectorsDic[vecword]=vecnumbers

km = KMeans(n_clusters=20, random_state=10).fit(vectors) 

train_data = []
train_labels=[]

for i in range(len(vectors)):
 train_labels.append(km.labels_[i])
 train_data.append(vectors[i])

print(collections.Counter(train_labels)) 
 
clf = LogisticRegression() #52
#clf = RidgeClassifier(tol=1e-1) #52

clf.fit(train_data, train_labels) 

file = open('/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/majorityclass', 'w')

test_labels=[] 
pred_labels=[]
 
for i in range(len(textdata)):
 test_data=[]
 
 test_labels.append(i/2000+1)
 
 words = textdata[i]
 #sum_vecs = [0] * 400
 #print(len(sum_vecs), i)
 for word in words:
  if word in termsVectorsDic:
   test_data.append(termsVectorsDic[word])    
 
 if len(test_data) > 0:
  preds = clf.predict(test_data) 
  predCounts = collections.Counter(preds)
  #print(predCounts) 
  file.write(str(predCounts)+"\n")
  pred_labels.append(predCounts.items()[0][0]+1)
 else:
  #print("no label") 
  file.write("no label\n")

file.close() 

#ch2 = SelectKBest(chi2, k='all')
#X_train = ch2.fit_transform(train_data, train_labels)
#X_test = ch2.transform(test_data) 

#clf = LogisticRegression() #52
#clf = RidgeClassifier(tol=1e-1) #52

#clf.fit(train_data, train_labels)

#preds = clf.predict(test_data)

score = metrics.accuracy_score(test_labels, pred_labels)
print ("acc_score:   %0.3f" % score)

############calculate variance###########
import numpy as np

file = open('D:/PhD/SupervisedFeatureSelection/majorityclass', 'r')
lines = file.readlines()
file.close()

file = open('D:/PhD/SupervisedFeatureSelection/majorityclass_variance', 'w')

lineCount=0
for line in lines:
 line = line.strip()
 arr = line.split(",")
 counts = []
 lineCount=lineCount+1
 for dic in arr:
  dic = dic.strip()  
  dicarr = dic.split(":")
  count = dicarr[1].strip()
  #print(count)
  counts.append(int(count))
  
 variance = np.var(counts)
 print(variance)   
 file.write(str(lineCount)+","+str(variance)+"\n")

file.close() 