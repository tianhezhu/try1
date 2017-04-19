package com.sparkTest

import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{Put, HTable}
import org.apache.hadoop.hbase.util.Bytes

/**
 * Created by Doris on 2016/3/14.
 */
object HbaseTest {
  def main(args: Array[String]) {
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", "Hadoop-DN-01,Hadoop-DN-02")
    val table = new HTable(conf,"DDDDD")
    var put = new Put(args(0).getBytes())
//    var put = new Put(Bytes.toBytes("asdasdas"))
    put.add(Bytes.toBytes("columns"), Bytes.toBytes("column"), "aaa".getBytes())
    table.put(put)
  }
}
