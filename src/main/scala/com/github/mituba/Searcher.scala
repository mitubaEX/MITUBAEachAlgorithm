package com.github.mituba

import java.net.URLEncoder

import dispatch._
import dispatch.Defaults._

/**
  * Created by mituba on 2017/08/25.
  */
class Searcher {
  def search(classInformation: ClassInformation, algorithm: String): List[SeachResult] = {
    val birthmarkData = classInformation.data
    val postParam: String =
      s"""
           {
            "params":{
              "q":"${birthmarkData.replace(" ", "+").replace(".", "-")}",
              "rows":"${2001}",
              "wt":"csv"
            }
           }
        """

//    val postParam: String =
//      s"""
//           {
//             query:"${birthmarkData.replace(" ", "+").replace(".", "-")}",
//             fields:["filename","lev:strdist(data,"${birthmarkData}",${algorithm})","place","birthmark","data"],
//             sort:"strdist(data,"${birthmarkData}",${algorithm}) desc"
//             limit:"${2001}"
//           }
//        """
    val postUrl: String = s"http://localhost:8982/solr/birth_${classInformation.birthmark.replace("-","")}1/query"
    val encodedBirthmarkData: String = URLEncoder.encode(birthmarkData, "UTF-8")

    val requestHandler = url(postUrl).POST
      .addQueryParameter("fl", s"""filename,lev:strdist(data,"${birthmarkData}",${algorithm}),place,birthmark,data""")
//      .addQueryParameter("sort", s"""strdist(data,"${birthmarkData}",${algorithm}) desc""")
      .setContentType("application/json", "UTF-8")  << postParam

    val http = Http(requestHandler OK as.String)
    http().split("\n").map(_.split(",", 5))
      .map(n => new SeachResult(classInformation, new ClassInformation(filename=n(0), place=n(2), birthmark=n(3), data=n(4)), n(1))).filterNot(_.sim.contains("lev"))
      .sortWith(_.sim.toDouble > _.sim.toDouble)
      .toList
  }
}
