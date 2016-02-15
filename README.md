###Examples

 * SimpleRecomendationTest It's a simple Mahout example just following https://mahout.apache.org/users/recommender/userbased-5-minutes.html
 * SparkMahoutRTest It's a simple Mahout operation over distributed matrix using R syntax ready to be executed in a EC2 Spark cluster!!

###Using Mahout from CLI

export SPARK_HOME=~/bigdata/spark-1.0.2-bin-hadoop1
export MAHOUT_HOME=~/bigdata/mahout-master/
/mahout spark-itemsimilarity --input /web/user-fborrego/example-mahout/input.txt --output test.out

./mahout spark-shell


### Deploy in EC2

* Configure spark.conf with your AWS credentials.
* execute sbt sparkLaunchCluster

### Links/docs

https://mahout.apache.org/users/recommender/recommender-first-timer-faq.html

https://mahout.apache.org/users/recommender/recommender-documentation.html

https://mahout.apache.org/users/sparkbindings/play-with-shell.html

http://mahout.apache.org/users/sparkbindings/ScalaSparkBindings.pdf