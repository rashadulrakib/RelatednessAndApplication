from collections import Counter
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
from sklearn.covariance import EllipticEnvelope
from sklearn.ensemble import IsolationForest
#from sklearn.neighbors import LocalOutlierFactor

for j in range(20):
 fileId = j+1
 file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/semisupervised/4118/"+str(fileId),"r")
 lines = file.readlines()
 file.close()

 train_data = []
 train_labels = []
 train_trueLabels = []

 for line in lines:
  line=line.lower().strip() 
  arr = re.split("\t", line)
  train_data.append(arr[2])
  train_labels.append(arr[0])
  train_trueLabels.append(arr[1])

 vectorizer = TfidfVectorizer( max_df=0.5, min_df=1, stop_words='english', use_idf=True, smooth_idf=True, norm='l2')
 x_train = vectorizer.fit_transform(train_data)

 contratio = len(train_data)/20000*2
  
 if len(train_data)>1100:
  contratio = len(train_data)/20000*7;
 
 isf = IsolationForest(n_estimators=100, max_samples='auto', contamination=contratio, max_features=1.0, bootstrap=True, verbose=0, random_state=0)
 outlierPreds = isf.fit(x_train).predict(x_train)

 print(len(outlierPreds))
 print(Counter(outlierPreds))

 file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/semisupervised/4118/"+str(fileId)+"_outlierpred","w")
 for pred in outlierPreds:
  file.write(str(pred)+"\n") 
 
 file.close()
