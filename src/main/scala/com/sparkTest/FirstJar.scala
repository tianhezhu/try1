package com.sparkTest

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._

/**
 * Created by Doris on 2016/3/14.
 */
object FirstJar {

  def main(args: Array[String]) {
    if(args.length!=2){
      System.exit(2)
    }
    val conf = new SparkConf().setAppName("TestJar")
    val sc = new SparkContext(conf)
//    sc.textFile(args(0)).map(_.split("\t")).map((_,1)).reduceByKey(_+_).saveAsTextFile(args(1))
    sc.textFile(args(0)).flatMap(_.split("\t")).map((_,1)).reduceByKey(_+_).saveAsTextFile(args(1))
    sc.stop()
  }
}
