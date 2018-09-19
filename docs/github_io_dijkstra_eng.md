This article is about a modification of dijkstra algorithm with linear complexity.
I have developed the application for trip planning and one of the target was find a shortest path and wasted time of them.

### Dijkstra algorithm description
Symbols:
* V - set of graph nodes
* E - set of graph edges
* w[i, j] weight of edge from i node to j node
* a - start search node
* q - priority queue of nodes 
* d[i] - distance to i node

1. d[a] = 0, d[i] = inf
2. while q is not empty:
  2.1. find v, where: v contains in q && v equals min node in q.
       Usually, order of node i in queue q calculated by value of d[i], but, particulary, in A* algorithm euristic distance used for this.
  2.2. for all nodes u that have relation (edge) from node v:
    2.2.1.1. if d[u] > w[v, u] + d[v]
      3.1.1. remove u from q
      3.1.2. d[u] = w[v, u] + d[v]
      3.1.3. u >> q (add node u to queue q).
  2.3. Remove v from q 

Complexity of algorithm: O(min(v) * n + update(u) * m)), where: 
- min(v) - complexity of find min value in priority queue.
- update(u) - complexity of update priority of u in queue q. Note: priority for node u is monotonically decreasing function, so it can be decreased only.  
- n - nodes count
- m - edges count

### Existed modifications 
Today, exist a lot of modifications of dijkstra algorithm that improve its performance.
Usually they based on usage different data structures for node priority queue. Let's describe some of them:
| structure type | find and delete min | update node priority |
|----------------|---------------------|-----------------------|
| Red-Black tree | O(log(n))           | O(log(n))             | 
| binary heap    | O(log(n))           | O(log(n))(sift-up)    |
| fibonacci heap | O(log(n))           | O(log(1)              |

### Sorting hash table
For make dijkstra algorithm with linear complexity  
### Probabilistic approach
