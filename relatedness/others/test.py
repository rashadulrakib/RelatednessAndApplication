import re
from sklearn.feature_extraction.text import TfidfVectorizer
#from sklearn.feature_selection import SelectKBest, chi2
import numpy as np
import random
import sys
from time import time
from sklearn import metrics
from sklearn.linear_model import RidgeClassifier, LogisticRegression
#from sklearn.svm import LinearSVC
from sklearn.linear_model import SGDClassifier
#from sklearn.linear_model import Perceptron
#from sklearn.naive_bayes import BernoulliNB, MultinomialNB
#from sklearn.neighbors import KNeighborsClassifier
#from sklearn import linear_model
#from sklearn.preprocessing import MinMaxScaler, StandardScaler
import numpy as np
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from sent_vecgenerator import generate_sent_vecs
from word_vec_extractor import extract_word_vecs

list_toktextdatas = []

#file=open("D:/PhD/dr.norbert/dataset/shorttext/stackoverflow/semisupervised/stackoverflowraw_ensembele_train","r")
#file=open("D:/PhD/dr.norbert/dataset/shorttext/data-web-snippets/semisupervised/data-web-snippetsraw_ensembele_train","r")
#file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/semisupervised/biomedicalraw_ensembele_train","r")
file=open("/home/owner/PhD/dr.norbert/dataset/shorttext/agnews/semisupervised/agnewsraw_ensembele_train","r")
lines = file.readlines()
file.close()


train_data = []
train_labels = []
train_trueLabels = []

train_textdata = []

for line in lines:
 line=line.lower().strip() 
 arr = re.split("\t", line)
 train_data.append(arr[2])
 word_tokens = word_tokenize(arr[2])
 train_textdata.append(word_tokens)
 train_labels.append(arr[0])
 train_trueLabels.append(arr[1])

list_toktextdatas.append(train_textdata)

 
#file=open("D:/PhD/dr.norbert/dataset/shorttext/stackoverflow/semisupervised/stackoverflowraw_ensembele_test","r")  
file=open("/home/owner/PhD/dr.norbert/dataset/shorttext/agnews/semisupervised/agnewsraw_ensembele_test","r") 
#file=open("D:/PhD/dr.norbert/dataset/shorttext/data-web-snippets/semisupervised/data-web-snippetsraw_ensembele_test","r")
#file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/semisupervised/biomedicalraw_ensembele_test","r")  

lines = file.readlines()
file.close()

test_data = []
test_labels = []

test_textdata = []

for line in lines:
 line=line.lower().strip() 
 arr = re.split("\t", line)
 word_tokens = word_tokenize(arr[2])
 test_textdata.append(word_tokens)
 test_data.append(arr[2])
 test_labels.append(arr[1])

list_toktextdatas.append(test_data)

gloveFile = "/home/owner/PhD/dr.norbert/dataset/shorttext/glove.42B.300d/glove.42B.300d.txt"

termsVectorsDic = extract_word_vecs(list_toktextdatas, gloveFile, 300)

list_toktextdatavecs = generate_sent_vecs(list_toktextdatas, termsVectorsDic, 300)



