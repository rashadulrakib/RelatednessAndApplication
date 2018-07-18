import re
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.feature_selection import SelectKBest, chi2, mutual_info_regression, mutual_info_classif, f_classif
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
from sklearn.feature_selection import SelectFromModel
from sklearn.ensemble import ExtraTreesClassifier, RandomForestClassifier
from sklearn.pipeline import Pipeline

file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/semisupervised/biomedicalraw_ensembele_train","r")
#file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
lines = file.readlines()
#np.random.shuffle(lines)
file.close()

train_data = []
train_labels = []
train_trueLabels = []

for line in lines:
 line=line.lower() 
 arr = re.split("\t", line)
 train_data.append(arr[2])
 train_labels.append(arr[0])
 train_trueLabels.append(arr[1])
 
file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/semisupervised/biomedicalraw_ensembele_test","r")
#file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
lines = file.readlines()
file.close()

test_data = []
test_labels = []

for line in lines:
 line=line.lower() 
 arr = re.split("\t", line)
 test_data.append(arr[2])
 test_labels.append(arr[1])

vectorizer = TfidfVectorizer( max_df=1.0, min_df=1, stop_words='english', use_idf=True, smooth_idf=True, norm='l2')
#vectorizer = TfidfVectorizer(max_df=0.15, min_df=1, stop_words=stopwords, use_idf=True, smooth_idf=True, norm='l2')
X_train = vectorizer.fit_transform(train_data)
X_test = vectorizer.transform(test_data)

#t0 = time()
#ch2 = SelectKBest(chi2, k='all')
#X_train = ch2.fit_transform(X_train, train_labels)
#X_test = ch2.transform(X_test)
#print("done in %fs" % (time() - t0))

#lsvc = LinearSVC(C=0.01, penalty="l1", dual=False).fit(X_train, train_labels)
#model = SelectFromModel(lsvc, prefit=True)
#X_train = model.transform(X_train)
#X_test = model.transform(X_test)

#model = ExtraTreesClassifier() 41
#model = model.fit(X_train, train_labels)
#model = SelectFromModel(model, prefit=True)
#X_train = model.transform(X_train)
#X_test = model.transform(X_test)

#model = Pipeline([
#  ('feature_selection', SelectFromModel(LinearSVC(penalty="l1"))),
#  ('classification', RandomForestClassifier())
#])
#X_train = model.fit_transform(X_train, train_labels)
#X_test = model.fit_transform(X_test)

#ch2 = SelectKBest(f_classif, k=10)
#X_train = ch2.fit_transform(X_train, train_labels)
#X_test = ch2.transform(X_test)

#ftrIndexes = ch2.get_support(indices=True)
#print(ftrIndexes)
#feature_names = vectorizer.get_feature_names()
#selected_feature_names = [feature_names[i] for i in ftrIndexes]
#print(selected_feature_names)

#clf=linear_model.LinearRegression()
#clf = LogisticRegression() #52
#clf = RidgeClassifier(tol=1e-1) #52
#clf = Perceptron(n_iter=100)
#clf=KNeighborsClassifier(n_neighbors=100)
#clf = LinearSVC(loss='l2', C=1000, dual=False, tol=1e-3)
clf = SGDClassifier(alpha=.0001, n_iter=100, penalty='l1') #41
#clf = SGDClassifier(alpha=.0001, n_iter=100, penalty='elasticnet')
#clf= MultinomialNB(alpha=.01)
#clf = BernoulliNB(alpha=.01)

t0 = time()
clf.fit(X_train, train_labels)
train_time = time() - t0
print ("train time: %0.3fs" % train_time)

t0 = time()
pred = clf.predict(X_test)
test_time = time() - t0
print ("test time:  %0.3fs" % test_time)

y_test = [int(i) for i in test_labels]
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

file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/semisupervised/biomedicalraw_ensembele_traintest","w")

for i in range(len(train_labels)):
 file.write(train_labels[i]+"\t"+train_trueLabels[i]+"\t"+train_data[i])

for i in range(len(test_labels)):
 file.write(pred[i]+"\t"+test_labels[i]+"\t"+test_data[i])

 
file.close()
