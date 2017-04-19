package com.myTest

import java.io._


import scala.io.Source

/**
 * Created by Doris on 2015/10/29.
 */
object createData {

  def main(args: Array[String]) {
//    val z = Source.fromFile("F:\\workSpace\\python\\pythonTest\\TestCode\\testSet.txt")
//    val writer = new FileWriter("F:\\workSpace\\python\\pythonTest\\TestCode\\testSetSMO.txt", true);
//    for (line <- z.getLines()) {
//      var skip = line.split("\t");
//      if ((skip(0).toDouble < (-0.2) && skip(1).toDouble > 8) || (skip(0).toDouble > 0.8 && skip(1).toDouble < 7)) {
//        val line1=line+"\n"
//        writer.write(line1)
//      }
//    }
//    writer.close()
    val a = getClass().getResourceAsStream("/aaaa.txt")
    println(a.read)
  }
}
