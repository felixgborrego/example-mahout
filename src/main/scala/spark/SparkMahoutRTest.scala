package spark.drivers

import java.net.URI

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{ Path, FileSystem }
import org.apache.hadoop.io.{ Text, SequenceFile }
import org.apache.mahout.drivers.ItemSimilarityDriver._
import org.apache.mahout.math._

import scala.io.Source

// Import scala bindings operations
import scalabindings._
// Enable R-like dialect in scala bindings
import RLikeOps._
// Import distributed matrix apis
import drm._
// Import R-like distributed dialect
import RLikeDrmOps._
// Those are needed for Spark-specific
// operations such as context creation.
// 100% engine-agnostic code does not
// require these.
import org.apache.mahout.sparkbindings._
// A good idea when working with mixed
// scala/java iterators and collections
import collection._

/**
 * Example of how to use Mahout to perform a matrix operation using R syntax.
 *
 * You can find a complete implementation of a recommendation system in:
 * org.apache.mahout.drivers.ItemSimilarityDriver
 *
 * This example has been deployed and tested in a EC2 cluster using:
 * sbt sparkLaunchCluster
 */
object SparkMahoutRTest extends App {

  implicit val mahoutCtx = mahoutSparkContext(masterUrl = "spark://ec2-54-72-147-170.eu-west-1.compute.amazonaws.com:7077", appName = "MahoutLocalContext", addMahoutJars = false)

  val hdfsInputFile = createSequenceFile

  // Load distributed row matrices (DRM).
  val A = drmFromHDFS(path = hdfsInputFile)

  // Create a dummy ddr
  val B = drmParallelize(dense((1L, 2L), (1L, 2L), (1L, 1L)))

  // Apply example operation
  val X = A %*% B.t

  print(X)

  /**
   * Transform from txt to an HDFS SequenceFile.
   * In a real code we need to get this from S3.
   */
  def createSequenceFile() = {

    val hdfsSequenceFile = "seq_input.txt"
    val conf = new Configuration()
    val fs = FileSystem.get(URI.create(hdfsSequenceFile), conf);
    val path = new Path(hdfsSequenceFile)

    val writer = SequenceFile.createWriter(fs, conf, path, classOf[Text], classOf[VectorWritable])

    //val fileContent = Source.fromFile("input.txt").getLines()
    val fileContent = Source.fromString("""1 2
       2 3
       1 2
       2 3
       3 3""").getLines()

    fileContent.foreach { line =>
      val values = line.trim.split(" ")
      val keyReaded = values(0).toString.toInt
      val valueReaded = values(1).toString.toInt

      val vector = new NamedVector(new DenseVector(Array[Double](keyReaded, valueReaded)), "user item buy")

      val vec = new VectorWritable()
      vec.set(vector)
      //println(line)
      writer.append(new Text(vector.getName()), vec)
    }
    writer.close()

    hdfsSequenceFile
  }

  def print(drm: CheckpointedDrm[_]) {
    A.rdd.map {
      case (rowID, itemVector) =>
        (itemVector(1))
    }.toLocalIterator.foreach {
      println(_)
    }

  }

}
