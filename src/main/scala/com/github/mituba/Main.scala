/*
 * This Scala source file was generated by the Gradle 'init' task.
 */

package com.github.mituba

import java.io.File

import scala.io.Source


object Main {
  def createClassInformation(filename: String, kindOfBirthmark: String): List[ClassInformation] ={
    val fileSource = Source.fromFile(filename)
    fileSource.getLines().map(_.split(",", 4))
      .filter(_.length >= 4) .map(n => new ClassInformation(filename = n(0), place = n(1), birthmark = kindOfBirthmark, data = n(3))).toList
  }

  def printCompareResults(algorithm: String, compareResults: List[CompareResult]): Unit ={
    compareResults.foreach(n =>
      printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", algorithm, n.class1.filename, n.class1.place, n.class1.birthmark, n.class1.data,
      n.class2.filename, n.class2.place, n.class2.birthmark, n.class2.data, n.sim))
  }

  def printSearchResults(algorithm: String, compareResults: List[SeachResult]): Unit ={
    compareResults.foreach(n =>
      printf("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", algorithm, n.class1.filename, n.class1.place, n.class1.birthmark, n.class1.data,
        n.class2.filename, n.class2.place, n.class2.birthmark, n.class2.data, n.sim))
  }

  def main(args: Array[String]): Unit = {

    val list: List[String] = List("jw")
    val classInformations: List[ClassInformation] = createClassInformation(args(0), args(1))
    classInformations.foreach(m => {
      printSearchResults("jw", new Searcher().search(m, "jw"))
      println("compareResult")
      printCompareResults("jw", new SeachResults("jw", new Searcher().search(m, "jw")).runCompare().compareResults)
    }
    )
    System.exit(1)
  }
}
