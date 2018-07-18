import re
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans
from sklearn import metrics

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
X_test = vectorizer.fit_transform(test_data)

km = KMeans(n_clusters=20, init='k-means++', max_iter=100, n_init=5)
km.fit(X_test)
print(len(km.labels_), len(test_data))


score = metrics.homogeneity_score(test_labels, km.labels_)
print ("homogeneity_score:   %0.3f" % score)
score = metrics.completeness_score(test_labels, km.labels_)
print ("completeness_score:   %0.3f" % score)
score = metrics.v_measure_score(test_labels, km.labels_)
print ("v_measure_score:   %0.3f" % score)
score = metrics.accuracy_score(test_labels, km.labels_)
print ("acc_score:   %0.3f" % score)
score = metrics.normalized_mutual_info_score(test_labels, km.labels_)  
print ("nmi_score:   %0.3f" % score)

file=open("D:/PhD/dr.norbert/dataset/shorttext/biomedical/semisupervised/biomedicalraw_ensembele_traintest","w")

for i in range(len(train_labels)):
 file.write(train_labels[i]+"\t"+train_trueLabels[i]+"\t"+train_data[i])

for i in range(len(km.labels_)):
 print(i,km.labels_[i]+1, test_labels[i], test_data[i])
 file.write(str(km.labels_[i]+1)+"\t"+test_labels[i]+"\t"+test_data[i])

 
file.close()