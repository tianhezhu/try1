package com.scalaTest

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
 * Created by Doris on 2015/10/22.
 */
object logicRegres {
  private var allData=ArrayBuffer[Array[Double]]()
  private var classLabels=ArrayBuffer[Array[Double]]()
  def getData(): Unit ={
    val z = Source.fromFile("F:\\workSpace\\python\\pythonTest\\TestCode\\testSet.txt")
    for (line <- z.getLines()){
      val z = line.split("\t")
      var oneData =Array(1.0,z(0).toDouble,z(1).toDouble)
      var oneLabel=Array(z(2).toDouble)
      classLabels+=oneLabel
      allData+=oneData
    }
    classLabels=matricTranspose(classLabels)
  }
  //    这个是经典的sigmoid函数
  def sigmoid(allData:ArrayBuffer[Array[Double]]): ArrayBuffer[Array[Double]] ={
    val format =new java.text.DecimalFormat("0000.000");
//    sigmoid函数的的循环计算
    for(data <- 0 until allData(0).length){
      allData(0)(data)=format.format(1.0/(1.0+Math.exp(-allData(0)(data)))).toDouble
    }
    allData
  }

  def main(args: Array[String]) {
//    调用下面这个类获取文件中的数据。
    getData
//    找到最佳系数
    gradAscent()
  }

  def gradAscent(): ArrayBuffer[Array[Double]] ={
    //    初始化一个参数矩阵
    var wei=Array(1.0,1.0,1.0)
    var weights=new ArrayBuffer[Array[Double]]
    //    将参数装入矩阵中以方便做矩阵乘法
    weights+=wei
    val alpha = 0.01
    for (i <- 0 until 500){
      //      矩阵相乘后放入sigmoid函数以得到分类
      val h=sigmoid(matricMultiplication(weights,matricTranspose(allData)))
      //      得到一个错误判断的列表，这个列表的数据为人工输入进来的分类classLabels循环减去这次判断出的分类
      var error=new Array[Double](classLabels(0).length)
      for(i<- 0 until classLabels(0).length){
        error(i) = classLabels(0)(i)-h(0)(i)
      }
//      这个变量就为了使error成为矩阵，好做矩阵乘法
      var endError=new ArrayBuffer[Array[Double]]
      endError+=error
      //      矩阵相乘
      var middleWei = matricMultiplication(matricTranspose(allData),matricTranspose(endError))
      var middleWei1 = new ArrayBuffer[Array[Double]]
      middleWei.foreach(x=>{
        var r = x.reduce(_+_)*alpha
        middleWei1+=Array(r)
      })
      var middleWei2 = new Array[Double](weights(0).length)
      for(i <- 0 until weights(0).length){
        middleWei2(i)=weights(0)(i)+middleWei1(i)(0)
      }
      weights(0)=middleWei2
    }
    weights
  }
//矩阵乘法函数，矩阵A左乘矩阵B
  def matricMultiplication(matricA:ArrayBuffer[Array[Double]],matricB:ArrayBuffer[Array[Double]]): ArrayBuffer[Array[Double]] ={
  //  先算出结果矩阵的行数和列数
    val row=matricA.length
    val column=matricB(0).length
  //  下面这个是要返回的结果矩阵
    var resultMatric=new ArrayBuffer[Array[Double]]
    for(r <- 0 until row){
      var line =new Array[Double](column)
      for(c<- 0 until column){
//      dataRC是要放入结果矩阵的第R行第C列的数据
        var dataRC=0.0
//      这个循环内部会将两个矩阵中相应位置的数据相乘后相加，最终得出dataRC
//        for(inside<-0 until matricB.length){
        for(inside<-0 until matricA(0).length){
          dataRC+=matricA(r)(inside)*matricB(inside)(c)
        }
//      下面这行代表将dataRC放到了结果矩阵的第r行的第c列上。这里加了个保留3位小数的四舍五入
        val df1 =new java.text.DecimalFormat("0000.000");
        dataRC = df1.format(dataRC).toDouble
        line(c)=dataRC
      }
//      下面这行代表结果矩阵增加了上面循环中创建出的第r行
      resultMatric+=line
    }
    resultMatric
  }


//矩阵转置函数
  def matricTranspose(matric:ArrayBuffer[Array[Double]]): ArrayBuffer[Array[Double]] ={
//  先算出结果矩阵的行数和列数
    val row=matric(0).length
    val column=matric.length
//  下面这个是要返回的结果矩阵
    var resultMatric=new ArrayBuffer[Array[Double]]
//  外层循环每循环一次创建一行数据，内存循环会将各个数放进去。因为使用的都是数组存数组的形式，所以用下表定位比map还要快
    for(r <- 0 until row){
      var line =new Array[Double](column)
      for (c<- 0 until column){
        line(c)=matric(c)(r)
      }
      resultMatric+=line
    }
    resultMatric
  }
}
