package com.github.aliabdullayev.dijkstra

import java.lang.Math.abs
import java.util.Random

import com.github.aliabdullaev.dijkstra.common.GraphStorage
import com.github.aliabdullaev.dijkstra.geometry.{GraphEdge, GraphNode}
import com.github.aliabdullaev.dijkstra.util.performance.MathUtils
import com.github.aliabdullaev.dijkstra.util.performance.PerformanceUtils.round
import org.apache.spark.internal.Logging

case class RandomGraphGenerator(nodeCnt: Int, medianEdgeCnt: Int, medianEdgeLen: Int) extends GraphGenerator with Logging {
  val rand = new Random()

  override def generateGraph(): GraphStorage = {
    val nodes = (0 to nodeCnt).map(new GraphNode(_, rand.nextLong(), rand.nextFloat(), rand.nextFloat())).toArray
    nodes.foreach { node =>
      val edgeCnt = abs(round(rand.nextGaussian() * medianEdgeCnt)) + 1
      (0 to edgeCnt).foreach { i =>
        val targetNode: GraphNode = nodes(getTargetNodeId(nodes, node))
        val edgeLen = getEdgeLen(medianEdgeLen, 20)
        val edge = new GraphEdge(node, targetNode, edgeLen)
        val revertedEdge = new GraphEdge(targetNode, node, edgeLen)

        targetNode.getIncomingEdges.put(node.getOrigId, edge)
        node.getOutcomingEdges.put(targetNode.getOrigId, edge)
        targetNode.getOutcomingEdges.put(node.getOrigId, revertedEdge)
        node.getIncomingEdges.put(targetNode.getOrigId, revertedEdge)
      }
    }
    log.info("Median edge len: {}", MathUtils.mean(nodes.flatMap(_.getOutcomingEdges.values()).map(_.getLen)))
    val edgeLenHist = edgeLenHist(nodes)
    log.info("Hist of edge len: {}", edgeLenHist)
    new GraphStorage(nodes)
  }

  private def getTargetNodeId(nodes: Array[GraphNode], startNode: GraphNode): Long = {
    val startNodeId = startNode.getOrigId
    var targetNodeId = startNodeId
    while (targetNodeId == startNodeId || isExistRelationWithTarget(startNode, targetNodeId)) {
      targetNodeId = nodes(rand.nextInt(nodes.length)).getOrigId
    }
    targetNodeId
  }

  private def isExistRelationWithTarget(graphNode: GraphNode, targetNodeId: Long): Boolean = {
    graphNode.getOutcomingEdges.contains(targetNodeId)
  }

  private def getEdgeLen(minLen: Int, medianEdgeLen: Int): Int = {
    abs(round(rand.nextGaussian() * medianEdgeLen)) + minLen
  }

  private def edgeLenHist(nodes: Array[GraphNode]) = {
    nodes.flatMap(_.getOutcomingEdges.values())
      .map(i => round(i.getLen()))
      .groupBy(i => i).map { case (len, edges) => len -> edges.length }
  }
}




