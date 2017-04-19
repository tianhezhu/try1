package com.scalaTest

import scala.collection.JavaConversions.propertiesAsScalaMap
/**
 * Created by Doris on 2015/9/9.
 */
object study {
  def main(args: Array[String]) {
//    val props: scala.collection.Map[String,String]=System.getProperties();
//    for((k,v)<-props){
//      System.out.println("key:"+k+",value:"+v)
//    }
    val rddFall=Array("json1","json2")
    var a:String="{"
    rddFall.foreach(x=>{
      a=a+x
      a=a+","
    })
    a=a+"}"
    System.out.println(a)
  }
}
