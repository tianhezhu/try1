package com.myTest

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD


object SparkKMeans {
  def main(args: Array[String]) {
    if (args.length < 3) {
      System.err.println("Please input <master> <dataFile> <knumbers>")
      System.exit(1)
    }
    val master = args(0)
    val dataFile = args(1)
    val knumbers = args(2).toInt

    val sc = new SparkContext(master, "SparkKMeans", System.getenv("SRARK_HOME"), Seq(System.getenv("SPARK_CLASSPATH")))

    val lines = sc.textFile(dataFile)
    val points = lines.map(line => {
      val parts = line.split("\t").map(_.toDouble)
      new Point(parts(0), parts(1))
    }).cache


    //      val centroids = Array.fill(knumbers){points(new Random().nextInt(points.length))}
    val centroids = Array.fill(knumbers) {Point.random}
    println("test points: ")
    points.foreach(println(_))
    println("initialize centroids:\n" + centroids.mkString("\n") + "\n")

    val startTime = System.currentTimeMillis()

    val resultCentroids = kmeans(points, centroids, 0.01, sc)

    val endTime = System.currentTimeMillis()
    val runTime = endTime - startTime
    println("run Time: " + runTime + "\nFinal centroids: \n" + resultCentroids.mkString("\n"))
  }

  def kmeans(points: RDD[Point], centroids: Seq[Point], epsilon: Double, sc: SparkContext): Seq[Point] = {
    def closestCentroid(point: Point) = {
      centroids.reduceLeft((a, b) => if ((point distance a) < (point distance b)) a else b)
    }

    /**
    val clusters = (
                points.map(point => closestCentroid(point) -> (point, 1)).reduceByKeyToDriver({
                    case ((ptA, numA), (ptB, numB)) => (ptA + ptB, numA + numB)
                }).map({
                    case (centroid, (ptSum, numPts)) => centroid -> ptSum / numPts
                }))
                println("clusters:")
                clusters.foreach(println(_))
      */

    var cluster = points.map(
      point => (
        closestCentroid(point) -> (point, 1)
        )
    )
    cluster.foreach(println(_))

    var clusterSum = cluster.reduceByKey {case ((ptA, numA), (ptB, numB)) => (ptA + ptB, numA + numB)}
    var average = clusterSum.map {pair => (pair._1, pair._2._1 / pair._2._2)}.collectAsMap()

    val newCentroids = centroids.map(oldCentroid => {
      average.get(oldCentroid) match {
        case Some(newCentroid) => newCentroid
        case None              => oldCentroid
      }
    })

    val movement = (centroids, newCentroids).zipped.map(_ distance _)
    println("Centroids changed by\n" + movement.map(d => "%3f".format(d)).mkString("(", ", ", ")")
      + "\nto\n" + newCentroids.mkString(", ") + "\n")


    if (movement.exists(_ > epsilon))
      kmeans(points, newCentroids, epsilon, sc)
    else
      newCentroids
  }
}