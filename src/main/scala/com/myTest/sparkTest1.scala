package com.myTest

import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by Doris on 2015/9/7.
 */
object sparkTest1 {

  def main(args: Array[String]) {
    if(args.length != 2){
      System.err.println("there have "+args.length+" args")
      System.exit(2);
    }else{
      for(a <- 0 until args.length){
        System.out.println(a);
      }
    }

    val conf = new SparkConf().setAppName("helloSpark")
    val sc = new SparkContext(conf)
    sc.textFile(args(0)).flatMap(_.split("\t")).map((_,1)).reduceByKey(_+_).saveAsTextFile(args(1))
    val rdd1 = sc.textFile(args(0)).flatMap(_.split("\t")).map((_,1)).reduce(reduceImpl)
    sc.stop()
  }

  def reduceImpl(a:(String,Int),b:(String,Int)): (String,Int) ={
    var z=0
    z = a._2+b._2
    return null
  }
}
