package com.scalaTest

import scala.math._

import scala.collection.mutable
/**
 * Created by Doris on 2015/9/29.
 */
object bayes {

  def createData(): Array[Array[String]] ={
    val array = Array(
      Array("my","dog","has","flea","problems","help","please"),
      Array("maybe","not","take","him","to","dog","park","stupid"),
      Array("my","dalmation","is","so","cute","I","love","him"),
      Array("stop","posting","stupid","worthless","garbage"),
      Array("mr","licks","ate","my","steak","how","to","stop","him"),
      Array("quit","buying","worthless","dog","food","stupid")
    )
    array
  }

  def createVocabList(dataSet:Array[Array[String]]):Array[String]={
    var resultSet = new mutable.LinkedHashSet[String]
    dataSet.foreach(_.foreach(x=>{resultSet.+=(x)}))
    resultSet.toArray
  }
//这个方法找出每个实例包含所有特征中的哪些特征，形成新的数组
  def bagOfWords2VecMN(vocabList:Array[String],inputSet:Array[String]): Array[Int] ={
    val intArray:Array[Int] = new Array(vocabList.length)
    inputSet.foreach(x=>{
      intArray(vocabList.indexOf(x))+=1
    })
    intArray
  }
//求出该分类中所有特征出现次数占所有分类中所有特征出现次数的比
  def trainNBO(trainMatrix:Array[Array[Int]],trainCategory:Array[Int],classy:Int): Array[Double] ={
    var classSum=0
    var wordCount=0.0
    trainCategory.foreach(x=>{if(x==classy){classSum+=1}})
    var newVector=new Array[Int](trainMatrix(0).length)
    for(i<-0 until newVector.length){
      newVector(i)=1
    }
    for(i<-0 until trainMatrix.length){
      if(trainCategory(i)==classy){
        trainMatrix(i).foreach(x=>{
          if(x==1){
            wordCount+=1
            newVector(i)+=1
          }
        })
      }
    }
    var result:Array[Double]=new Array[Double](trainMatrix(0).length)
    for(i<-0 until newVector.length){
      result(i)=log(newVector(i)/wordCount)
    }
    result
  }

  def classifyNB(wordList:Array[Int],class0:Array[Double],class1:Array[Double],ratio0:Double,ratio1:Double):Int={
    var sum0:Double=0.0
    var sum1:Double=0.0
    for(i<-0 until class0.length){
      sum0+=class0(i)*wordList(i)
    }
    for(i<-0 until class0.length){
      sum1+=class1(i)*wordList(i)
    }
    sum0=sum0+log(ratio0)
    sum1=sum1+log(ratio1)
    if(sum0>sum1){
      return 0
    }else{
      return 1
    }
  }

  def main(args: Array[String]) {
    val dataSet = createData
    val vocabList = createVocabList(dataSet)

    var vectorForInt:Array[Array[Int]]=new Array(dataSet.length)
    for (i<- 0 until dataSet.length){
      //这个方法找出每个实例包含所有特征中的哪些特征
      vectorForInt(i)=bagOfWords2VecMN(vocabList,dataSet(i))
    }
    val classVec = Array(0,1,0,1,0,1)
    //求出该分类中所有特征出现次数占所有分类中所有特征出现次数的比
    val class1 = trainNBO(vectorForInt,classVec,1)
    val class0 = trainNBO(vectorForInt,classVec,0)
    val ratio=0.5
    var testWords=Array("love","my","dalmation")
//    算某个实例属于每个分类的比例最大值
    var c =classifyNB(bagOfWords2VecMN(vocabList,testWords),class0,class1,ratio,ratio)
    System.out.println("classified as:"+c)
    testWords=Array("stupid","garbage")
    c =classifyNB(bagOfWords2VecMN(vocabList,testWords),class0,class1,ratio,ratio)
    System.out.println("classified as:"+c)
  }

}
