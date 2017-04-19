package com.scalaTest

import com.myTest.Point
import scala.collection.mutable.ArrayBuffer


/**
 * Created by Doris on 2015/9/9.
 */
object TryMain {
//  这个数代表有多少个质心
  val centroidsNum:Int=2;
//  设置两次kmeans之间的差的最大值
  val boundary:Double=0.3;
  def main(args: Array[String]) {
    val arrbuffer1=ArrayBuffer[String]()
    arrbuffer1+=("3.2,2.4","2.1,8.9","1.3,4.1","5.2,6.4","2.8,7.0")
    var list=ArrayBuffer[Point]()
    arrbuffer1.foreach(line=>{
      val a = line.split(",")
      val p=new Point(a(0).toDouble,a(1).toDouble)
      list+=p
    })
//    val centoids = Array.fill(centroidsNum){Point.random()}
    val centoids = Array(list(1),list(3))
    kmeans(list,centoids,boundary)
//    for(p<- 0 until list.length){
//      val a = (list(p)->(list(p),1))
//      System.out.println(a)
//    }
  }

  def kmeans(points:ArrayBuffer[Point],centroids:Array[Point],boundary:Double): Unit ={
//    计算其中一个点的归属簇
    def closestCentroid(point: Point):Point = {
      centroids.reduceLeft((a, b) => if ((point distance a) < (point distance b)) a else b)
    }
//    循环每个点计算归属簇，把簇心作为键，这个点作为值存到一个键值对中，再用za这个Map存放这个键值对集合，在spark中会存在rdd中
    val za = new scala.collection.mutable.HashMap[Point,ArrayBuffer[Point]]
//    把所有质心加入到这个MR中，原因是有的质心可能不会有点在它的簇中
    for(i<-0 until centroids.length){
      za(centroids(i))=new ArrayBuffer[Point]
    }
    points.foreach(
      point=>{
        za(closestCentroid(point))+=point
      }
    )
    println(za.toString())
    val newCentroids=new ArrayBuffer[Point](centroidsNum)
//  循环将新的质心算出来，如果跟以前的质心不同，就看两个质心的差的绝对值是否大于设定的值，如果有一个质心差的绝对值大于这个值，就再循环kmeans
    var targ:Boolean=false
    for((k,v)<-za){
      if(v.length==0){
        newCentroids+=k
      }else{
        val allPoint = v.reduceLeft(_+_)
        val newPoint = allPoint/v.length
        if(newPoint.distance(k)>boundary){
          targ=true
        }
        newCentroids+=allPoint/v.length
      }
    }
    System.out.println(centroids.toString)
    System.out.println(newCentroids.toString)
    if(targ){
      kmeans(points,newCentroids.toArray,boundary)
    }
  }

}
