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


file=open('D:/Google-n-gram/stopwords/stopWords.txt',"r")
stopwords=file.readlines()
file.close() 
 
file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
#file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/biomedicalraw","r")
lines = file.readlines()
data = []
labels = []
np.random(0).shuffle(lines)
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
 
#file=open("/users/grad/rakib/dr.norbert/dataset/shorttext/biomedical/semisupervised/80-800-body-label","w")

#for i in range(len(data_test)):
# labelbody = labels_test[i]+"\t"+data_test[i]
# file.write(labelbody)

#file.close()

vectorizer = TfidfVectorizer(max_df=0.15, min_df=1, stop_words='english', use_idf=True, smooth_idf=True, norm='l2')
#vectorizer = TfidfVectorizer(max_df=0.15, min_df=1, stop_words=stopwords, use_idf=True, smooth_idf=True, norm='l2')
X_train = vectorizer.fit_transform(data_train)
X_test = vectorizer.transform(data_test)

t0 = time()
ch2 = SelectKBest(chi2, k=7000)
X_train = ch2.fit_transform(X_train, labels_train)
X_test = ch2.transform(X_test)
print("done in %fs" % (time() - t0))
ftrIndexes = ch2.get_support(indices=True)
print(ftrIndexes)
feature_names = vectorizer.get_feature_names()
selected_feature_names = [feature_names[i] for i in ftrIndexes]
print(selected_feature_names)

