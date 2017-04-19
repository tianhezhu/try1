package com.scalaTest

import math._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Doris on 2015/9/22.
 */
object DecisionTree {

  def main(args: Array[String]) {
    var myDat = chooseBestFeatureToSplit(getData)
    print(myDat)
  }



  def calcInfoEntropy(dataSet:Array[Array[String]]): Double ={
    val len = dataSet.length
    val map = new scala.collection.mutable.HashMap[String,Double]
    for(i <- 0 until len){
      val arr = dataSet(i)
      if(map.contains(arr(arr.length-1))){
        map(arr(arr.length-1))+=1
      }else{
        map(arr(arr.length-1))=1
      }
    }
    var infoEntropy=0.0
    for((k,v)<-map){
      val prob=v/len
      infoEntropy-=prob*myLog(prob,2)
    }
    infoEntropy
  }

  def getData(): Array[Array[String]] ={
    val data=Array(Array("1","1","yes"),
      Array("1","1","yes"),
//      Array("0","1","yes"),//33
      Array("1","0","no"),
      Array("0","1","no"),
      Array("0","1","no")
      )
    data
  }
  def chooseBestFeatureToSplit(dataSet:Array[Array[String]]): Int ={
    val columnNum = dataSet(0).length-1
    val baseEntropy = calcInfoEntropy(dataSet)
    var bestInfoGain=0.0
    var bestFeature = -1
    for (i<- 0 until columnNum){
      var  uniqueVals = new mutable.LinkedHashSet[Int]
      dataSet.foreach(x=>{
        uniqueVals.+=(x(i).toInt)
      })
      var newEntropy=0.0
      for (value <- uniqueVals){
        val newSet=splitDataSet(dataSet,i,value)
        val prob=newSet.size.toDouble/dataSet.size
        newEntropy+=prob*calcInfoEntropy(newSet)
      }
      val infoGain=baseEntropy-newEntropy
      System.out.println(infoGain)
      if(infoGain>bestInfoGain){
        bestInfoGain=infoGain
        bestFeature=i
      }
    }
    return bestFeature
  }

  def  splitDataSet(dataSet:Array[Array[String]],axis:Int,value:Int): Array[Array[String]] ={
    var count=ArrayBuffer[Array[String]]()
     for(data<-dataSet){
       if(data(axis).toInt==value){
         count+=data
       }
     }
    count.toArray
  }

  def myLog(value:Double,base:Double): Double ={
    var result=log(value)/log(base)
    result
  }
}
