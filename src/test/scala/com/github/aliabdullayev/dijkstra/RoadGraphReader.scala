package com.github.aliabdullayev.dijkstra

import com.github.aliabdullaev.dijkstra.common.GraphStorage
import com.github.aliabdullaev.dijkstra.geometry.{GraphEdge, GraphNode}
import io.getquill.{PostgresJdbcContext, SnakeCase}

import scala.collection.JavaConversions._

case class RoadEdge(osmSourceId: Long, osmTargetId: Long, roadType: Int, kmLen: Double)
case class RoadNode(osmId: Long, importance: Double, lat: Double, lon: Double) {
  def toGraphNode = new GraphNode(osmId, lat.toFloat, lon.toFloat)
}

object RoadGraphDBSchema {
  val ctx = new PostgresJdbcContext(SnakeCase, "ctx")

  import ctx._

  val nodes = quote {
    querySchema[RoadNode]("nodes")
  }

  val edges = quote {
    querySchema[RoadEdge]("edges")
  }
}


object RoadGraphReader extends GraphGenerator {

  import RoadGraphDBSchema._

  def readEdges: Array[RoadEdge] = {
    ctx.run(edges).toArray
  }

  def readNodes: Array[RoadNode] = {
    ctx.run(nodes).toArray
  }

  def toGraphStorage(nodes: Array[RoadNode], edges: Array[RoadEdge]): GraphStorage = {
    val nodePerOsmId: Map[Long, GraphNode] = nodes.map(i => i.osmId -> i.toGraphNode).toMap

    val outcomingEdges: Map[GraphNode, Array[GraphEdge]] = edges
      .map(i => new GraphEdge(nodePerOsmId(i.osmSourceId), nodePerOsmId(i.osmTargetId), (i.kmLen * 1000).toInt))
      .groupBy(_.getStartNode).withDefaultValue(Array.empty)

    val incomingEdges: Map[GraphNode, Array[GraphEdge]] = edges
      .map(i => new GraphEdge(nodePerOsmId(i.osmSourceId), nodePerOsmId(i.osmTargetId), (i.kmLen * 1000).toInt))
      .groupBy(_.getEndNode).withDefaultValue(Array.empty)

    nodePerOsmId.values.foreach(i => {
      i.setIncomingEdges(incomingEdges(i).toList)
      i.setOutcomingEdges(outcomingEdges(i).toList)
    })
    new GraphStorage(nodePerOsmId.values.toArray)
  }

  override def generateGraph(): GraphStorage = toGraphStorage(readNodes, readEdges)
}
