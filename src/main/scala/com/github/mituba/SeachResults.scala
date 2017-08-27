package com.github.mituba

import java.io.{BufferedReader, File, FileReader, FileWriter}

import com.github.pochi.runner.scripts.ScriptRunnerBuilder

import scala.io.Source

/**
  * Created by mituba on 2017/08/26.
  */
class SeachResults(val algorithm: String, val seachResults: List[SeachResult]){
  def runCompare(): CompareResults ={
    new CompareResults(algorithm = algorithm,compareResults = seachResults.map(n => compare(n.class1, n.class2)))
  }

  def compare(class1: ClassInformation, class2: ClassInformation): CompareResult ={
    val file1: File = createFile(class1, "class1.csv")
    val file2: File = createFile(class2, "class2.csv")
    val builder = new ScriptRunnerBuilder
    val runner = builder.build
    val arg = Array("./compare_input_csv_test.js", class1.birthmark, file1.getPath, file2.getPath)
    runner.runsScript(arg)
    val compareResult = new BufferedReader(new FileReader(new File("compareResult.csv"))).readLine().split(",").toList
    new CompareResult(class1, class2, compareResult(2))
  }

  def createFile(cl: ClassInformation, createFileName: String): File ={
    val file = new File(createFileName)
    val fileWriter = new FileWriter(file)
    fileWriter.write(s"""${cl.filename},${cl.place},${cl.birthmark},${cl.data}""")
    fileWriter.close()
    file
  }
}

