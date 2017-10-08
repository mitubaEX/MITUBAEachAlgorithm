package com.github.mituba

import java.net.URLEncoder
import dispatch._
import dispatch.Defaults._

/**
  * Created by mituba on 2017/08/25.
  */
class Searcher {
  def getMergeHexString(birthmark: String):String =
    birthmark.split(" ").map(_.toInt.toHexString).mkString("")

  def getHexBirthmark(birthmark: String): String =
    birthmark.split(",").map(getMergeHexString(_)).mkString(",")

  def getSplitHexBirthmark(birthmark:String, list: List[String] = List()): String = {
    if(birthmark.length <= 2)
      return list.mkString(" ")
    val splitList = birthmark.splitAt(2)
    println(splitList)
    getSplitHexBirthmark(splitList._2, splitList._1 :: list)
  }

  def getDecodeHexBirthmark(birthmark:String): String =
    birthmark.split(",").map(getSplitHexBirthmark(_)).mkString(",")

  def search(classInformation: ClassInformation, algorithm: String): List[SeachResult] = {
   // val birthmarkData = classInformation.data
    val birthmarkData = getHexBirthmark(classInformation.data)
    val postParam: String =
      s"""
           {
            "params":{
              "rows":"${500000}",
              "wt":"csv"
            }
           }
        """
    val postUrl: String = s"http://localhost:8983/solr/${classInformation.birthmark.replace("-","")}/query?q=encode_data:" + birthmarkData.replace(" ", "+").replace(".", "-")
    val encodedBirthmarkData: String = URLEncoder.encode(birthmarkData, "UTF-8")

    val requestHandler = url(postUrl).POST
      .addQueryParameter("fl", s"""filename,score,place,birthmark,data""")
      .addQueryParameter("sort", s"""score desc""")
      .setContentType("application/json", "UTF-8")  << postParam

    val start = System.currentTimeMillis

    val http = Http(requestHandler OK as.String)
    val result = http()
    val resultList = result.split("\n").map(_.split(",", 5))
      .map(n => new SeachResult(classInformation,
        new ClassInformation(filename=n(0), place=n(2), birthmark=n(3), data=n(4).replace("\\", "")),
        n(1))).filterNot(_.sim.contains("score"))
      .sortWith(_.sim.toDouble > _.sim.toDouble)
      .toList
    println("searchTime:" + (System.currentTimeMillis - start) + "msec")
    resultList
  }
}
