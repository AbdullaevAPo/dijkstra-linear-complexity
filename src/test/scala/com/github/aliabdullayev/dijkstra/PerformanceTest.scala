package com.github.aliabdullayev.dijkstra

import java.util.Random

import com.github.aliabdullaev.dijkstra.binaryheap.BinaryHeapDijkstraAlgo
import com.github.aliabdullaev.dijkstra.common.{DijkstraAlgoArray, GraphStorage}
import com.github.aliabdullaev.dijkstra.fibonacciheap.FibonacciHeapDijkstraAlgo
import com.github.aliabdullaev.dijkstra.linear.LinearDijkstraAlgo
import com.github.aliabdullaev.dijkstra.util.BitArray
import org.apache.spark.internal.Logging
import org.scalatest.FunSuite

import scala.collection.JavaConverters._

class PerformanceTest extends FunSuite with Logging {
  val randomSeed = 0
  val rand = new Random(randomSeed)

  test("performance comparision with random generated graphs") {
    var nodeCnt = 10000
    while (nodeCnt < 1E6) {
      performanceComparision(RandomGraphGenerator(nodeCnt, 5, 70))
      nodeCnt *= 1.5
    }
  }

  test("performance comparision with road graphs") {
    performanceComparision(RoadGraphReader, iterCnt = 1000)
  }

  private def performanceComparision(graphGenerator: GraphGenerator, iterCnt: Int = 100): Unit = {
    val graph = graphGenerator.generateGraph()
    var linearTime = 0.0
    var fibonacciTime = 0.0
    var heapTime = 0.0
    (0 to iterCnt).foreach { _ =>
      val startNode = graph.nodes(rand.nextInt(graph.nodes.length))
      val targetNodes = new BitArray(graph.nodes.map(i => new Integer(i.getId)).distinct.toList.asJava)

      val (dArr1, t1) = time { () => new LinearDijkstraAlgo(graph, true).runDijkstra(startNode, targetNodes) }
      linearTime += t1

      val (dArr2, t2) = new FibonacciHeapDijkstraAlgo(graph, true).runDijkstra(startNode, targetNodes)
      fibonacciTime += t2
      qualityCheck(dArr1, dArr2)

      val (dArr3, t3) = new BinaryHeapDijkstraAlgo(graph, true).runDijkstra(startNode, targetNodes)
      heapTime += t3
      qualityCheck(dArr2, dArr3)
    }
    log.info("nodeCnt: {}, edgeCnt: {}, linearTime: {}, fibonacciTime: {}, binaryHeapTime: {}", graph.nodes.length, graph.getTotalEdgeCnt,
      String.format("%3f", linearTime), String.format("%3f", fibonacciTime), String.format("%3f", heapTime))
  }

  private def qualityCheck(dArr1: DijkstraAlgoArray, dArr2: DijkstraAlgoArray): Unit = {
    0 to dArr1.size() foreach (i => assert(dArr1.getLength(i) == dArr2.getLength(i)))
  }

  def time[A](f: () => A): (A, Double) = {
    val startTime = System.nanoTime()
    val res = f()
    (res, (System.nanoTime() - startTime) / 1.0E9)
  }
}

trait GraphGenerator {
  def generateGraph(): GraphStorage
}