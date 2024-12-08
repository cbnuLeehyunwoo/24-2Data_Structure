import kotlin.math.roundToInt

// 시간 복잡도 O(E log V)
// 새로운 아이템을 삽입 하거나 삭제할 때 O(log V) MinHeap 기반이기 때문,
// 인접노드와 연결된 엣지만을 대상으로 삽입 삭제가 일어나기 때문
// 각 노드의 차수만큼 유망한 후보를 고려하기 때문에 O(E)
// 우선순위 큐에 대한 인큐작업 수행 O(E log V)
// 유망한 엣지들 중에서 가장 유망한 놈을 찾는 작업은 정점 개수만큼 반복 O(V),
// 디큐 작업도 수행하기 때문에 O(V log V)
// 결과적으로 O((V + E) log V)
// 일반적으로 엣지의 수가 정점보다 크므로 O(E log V)
// 만약 엣지들의 가중치가 모두 유일할 경우 그래프의 최소신장트리는 유일할 것이다..
// 유일하지 않다면 여러경우의 최소신장트리가 존재할 것.
object Prim {
    // 현재 방문한 노드에서 연결된 간선 중 아직 신장 트리에 추가되지 않은 간선들을 우선순위 큐에 추가함
    private fun <T> addAvailableEdges(
        // 현재 처리중인 노드
        vertex: Vertex<T>,
        // 그래프
        graph: Graph<T>,
        // 신장트리에 포함된 노드 집합
        visited: Set<Vertex<T>>,
        // 우선순위 큐
        priorityQueue: AbstractPriorityQueue<Edge<T>>
    ) {
        // 현재 노드와 연결된 모든 간선을 가져온다
        graph.edges(vertex).forEach { edge ->
            // 만약 간선이 방문되지 않았다면 우선순위 큐에 추가
            if (edge.destination !in visited) {
                priorityQueue.enqueue(edge)
            }
        }
    }
    // 프림 알고리즘을 수행하는 함수
    // 그래프를 매개변수로 받고(인접 리스트), 비용과 최소 신장 트리의 쌍을 리턴
    fun <T> produceMinimumSpanningTree(graph: AdjacencyList<T>): Pair<Double, AdjacencyList<T>> {
        // 가중치(누적계산)
        var cost = 0.0
        // 신장트리를 저장할 새로운 인접 리스트 객체
        val mst = AdjacencyList<T>()
        // 방문한 노드를 저장할 집합
        val visited = mutableSetOf<Vertex<T>>()
        // 간선의 가중치가 작을수록 높은 우선순위를 부여하게 되는 Comparator
        val comparator = Comparator<Edge<T>> {
                first, second ->
            val firstWeight: Double = first.weight ?: 0.0
            val secondWeight: Double = second.weight ?: 0.0
            (secondWeight - firstWeight).roundToInt()
        }
        // 우선순위 큐 객체
        val priorityQueue = PriorityQueue(comparator)
        // 매개변수로 들어온 그래프의 정정만 복사
        mst.copyVertices(graph)
        // 그래프의 첫 번째 노드를 시작노드로 설정
        // 그래프가 비어있다면 즉시 종료
        val start = graph.allVertices.firstOrNull() ?: return Pair(cost, mst)
        // 시작 노드를 visited 에 추가
        visited.add(start)
        // 시작노드와 연결된 엣지들을 우선순위 큐에 추가
        addAvailableEdges(start, graph, visited, priorityQueue)

        // 우선순위 큐에서 가중치가 가장 낮은 엣지를 계속해서 선택
        while (true) {
            // 우선순위 큐에서 가장 유망한 엣지를 꺼냄, 없으면 루프 종료
            val smallestEdge = priorityQueue.dequeue() ?: break
            // 유망한 엣지의 목적지 정점
            val vertex = smallestEdge.destination
            // 만약 유망한 엣지의 목적지 정점이 visited 에 들어가있다면(이미 방문되었다면) continue
            if (visited.contains(vertex)) continue
            // 방문된적 없다면 방문했음 표시
            visited.add(vertex)
            // 누적 가중치에 유망한 엣지의 가중치를 더한다. 가중치가 없으면 0더함
            cost += smallestEdge.weight ?: 0.0
            // 신장트리에 유망한 엣지 추가, 매개변수로 비방향, 소스, 목적지, 가중치 던짐
            mst.add(EdgeType.UNDIRECTED, smallestEdge.source,
                smallestEdge.destination, smallestEdge.weight)
            // 새로 방문된 노드와 연결된 모든 간선을 우선순위 큐에 추가
            addAvailableEdges(vertex, graph, visited, priorityQueue)
        }
        return Pair(cost, mst)
    }
}
