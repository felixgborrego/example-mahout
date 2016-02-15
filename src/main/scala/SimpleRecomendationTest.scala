import java.io.File

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import collection.JavaConversions._

/**
 * Simple recomendation system to Test Mahout over Spark on EC2.
 *
 *
 * https://mahout.apache.org/users/recommender/userbased-5-minutes.html
 */
object SimpleRecomendationTest extends App {
  val csv = this.getClass.getResource("/dataset.csv")
  val model = new FileDataModel(new File(csv.getPath))

  val similarity = new PearsonCorrelationSimilarity(model)

  val neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model)

  val recommender = new GenericUserBasedRecommender(model, neighborhood, similarity)

  val recommendations = recommender.recommend(2, 3).toList;
  recommendations.foreach { recomendation =>
    println(recomendation)
  }

}
