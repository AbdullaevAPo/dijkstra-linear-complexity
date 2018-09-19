package com.github.aliabdullayev.dijkstra

import java.util.{PriorityQueue, Random}

import com.github.aliabdullaev.dijkstra.binaryheap.IntBinaryHeap
import org.scalatest.FunSuite

class HeapTest extends FunSuite {
  test("checkHeapWork") {
    val heap = new IntBinaryHeap(Integer.compare(_, _))
    heap.insert(10)
    heap.insert(20)
    heap.insert(5)
    heap.insert(15)
    heap.printHeapList()
    assert(heap.remove() == 5)
    heap.printHeapList()
    assert(heap.remove() == 10)
    heap.printHeapList()
    assert(heap.remove() == 15)
    assert(heap.remove() == 20)
  }

  test("checkHeapWorkForHugeCount") {
    val heap = new IntBinaryHeap(Integer.compare(_, _))
    val queue = new PriorityQueue[Int]()
    val rand = new Random()
    val randomElements = (0 to 10000).map(i => rand.nextInt(100)).toArray
    randomElements.take(1000).foreach { i =>
      heap.insert(i)
      queue.add(i)
    }
    randomElements.drop(1000).foreach { i =>
      heap.insert(i)
      queue.add(i)
      val minId1 = queue.remove()
      val minId2 = heap.remove()
      assert(minId1 == minId2)
    }
  }
}
