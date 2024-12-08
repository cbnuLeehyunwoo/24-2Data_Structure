
// 엣지를 가짐으로써 추후에 백트래킹 할 때 사용할 수 있음
// 특정 정점을 방문했을 때 그 정점에 도달한 경로에 대한 정보 저장
class Visit<T>(val type: VisitType, val edge: Edge<T>? = null)
// 여기가 시작하는곳인지 아니면 간선인지 표기
enum class VisitType {
    START,
    EDGE
}

// 시간 복잡도 O(E log V)
// MinHeap 기반의 우선순위 큐 사용: O(log V) (새로운 아이템을 삽입하거나 삭제할 때)
// traverse 할 때 좋아보이는 자식에 대해서만 traverse  O(E)
// BFS 의 경우 모든 자식 노드에 대해 traverse 하므로 O(V + E)지만,
// 다익스트라는 1개의 자식노드(유망해보이는놈)만 traverse 하기 때문에 O(1 + E)
// Traverse 할 때 마다 우선순위 큐를 사용하므로(인큐 디큐) O(E) * O(log V) = O(E log V)
class Dijkstra<T>(private val graph: AdjacencyList<T>) {

    // paths 에는 현재의 스텝에서 각 노드, 아이템까지의 최단 경로 정보가 담겨있다..
    // 데스티네이션 부터 거슬러 올라오면서 다시 백체크
    private fun route(destination: Vertex<T>,
                      // 키가 버텍스(경로 추적을 시작할 목적지 정점)이고, 값이 Visit
                      // 다익스트라 알고리즘의 결과로 각 정점에 도달한 정보가 저장된 해쉬맵
                      paths: HashMap<Vertex<T>, Visit<T>>): ArrayList<Edge<T>> {
        // 현재 백트래킹 중인 정점
        var vertex = destination
        // 백트래킹 과정에서 발견한 엣지들을 저장할 리스트
        val path = arrayListOf<Edge<T>>()
        loop@ while (true) {
            // 현재 정점에 대한 visit 정보를 가져옴..
            // 만약 정점이 없다면 루프 종료..
            val visit = paths[vertex] ?: break
            // 엣지타입인지 스타트 타입인지
            when (visit.type) {
                // 엣지 타입인경우
                // 아직 중간을 찾고있군 계속 진행한다
                VisitType.EDGE -> visit.edge?.let {
                    // path 에다가 넣고
                    path.add(it)
                    // 그놈의 소스를 넣는다.
                    vertex = it.source
                }
                // 현재 정점이 출발점이므로 끝
                VisitType.START -> break@loop

            }
        }
        // 출발점에서 목적지까지의 엣지들을 담은 리스트..
        return path
    }

    // destination 정점까지의 경로를 구하고 경로에 포함된 가중치를 모두 더하여 반환하는 함수
    private fun distance(destination: Vertex<T>,
                         // 다익스트라 알고리즘의 결과로 생성된 경로 정보가 담긴 해쉬맵
                         paths: HashMap<Vertex<T>, Visit<T>>): Double{
        // route 함수를 호춣하여 destination 경로에 포함된 엣지 리스트를 받는다.
        val path = route(destination, paths)
        // 엣지들의 모든 가중치를 모아서 더한다(없으면 0.0으로 던진다)
        return path.sumOf {it. weight ?: 0.0}
    }
    // destination 정점까지의 최단 경로를 엣지들의 리스트 형태로 반환
    fun shortestPath(destination: Vertex<T>,
                     paths: HashMap<Vertex<T>,Visit<T>>): ArrayList<Edge<T>> {
        return route(destination, paths)
    }

    // 위의 함수와 전혀 다른 함수
    // 다익스트라 알고리즘
    // 정점 하나를 던져주면 해쉬맵을 리턴한다
    fun shortestPath(start: Vertex<T>): HashMap<Vertex<T>, Visit<T>>
    {
        // 각 정점에 도달하기 위한 경로정보를 저장하는 해쉬맵
        val paths: HashMap<Vertex<T>, Visit<T>> = HashMap()
        // 백트래킹 할 때 여기가 끝임을 알리기 위해..
        paths[start] = Visit(VisitType.START)
        // 우선순위 기준을 제공하는 함수
        // 두개를 입력받아서 distance 함수를 호출
        // 현재까지의 최단 경로 길이를 비교
        // second - first 를 함으로써
        // 빼기 연산 결과가 양수면 first 우선순위가 높고(first 길이가 더 짧고)
        // 음수면 second 가 우선순위가 높다(second 길이가 더 짧다)
        // 결론적으로 MinHeap 기반의 우선순위 큐가 될 것
        val distanceComparator = Comparator<Vertex<T>>{ first, second->
               (distance(second, paths) - distance(first, paths)).toInt() }
        // Min heap 기반의 우선순위 큐가 된다..
        // 정점간의 경로가 짧을수록 우선순위가 높아질 것
        val priorityQueue =
            PriorityQueue(distanceComparator)
        // 큐에 우선 시작 정점 인큐
        priorityQueue.enqueue(start)

        // 반복적으로 정점 처리..
        while (true) {
            // 우선순위 큐에서 정점 하나 꺼냄(가장 짧은 경로를 가진 정점이 나올 것)
            // 없으면 break
            // 디큐하면 내부적으로 힙 정렬이 일어날 것, O(log V)
            val vertex = priorityQueue.dequeue() ?: break
            // 꺼낸 정점에서 나가는 모든 엣지들을 가져옴
            val edges = graph.edges(vertex)
            // 그 엣지들에 대해
            edges.forEach {
                // 가중치를 확인, 가중치 없으면 continue
                val weight = it.weight ?: return@forEach
                // 만약 목적지의 값이 nil 값이거나(아직 해당 정점까지의 경로가 계산된 적 없음)
                if (paths[it.destination] == null
                    // 방금 뺀 아주 유망한 놈(vertex)부터의 경로가 기존의 값(paths 안의 정보) 보다 작다면..
                    || distance(vertex, paths) + weight < distance(it.destination, paths)) {
                    // 그럼 해당 목적지까지의 경로를 유망한 놈부터의 경로로 업데이트
                    paths[it.destination] = Visit(VisitType.EDGE, it)
                    // 유망한 놈의 목적지 정점을 우선순위 큐에 인큐
                    // 내부적으로 힙정렬, O(log V)
                    // 언젠가 반복에서 큐에서 꺼내어질 것
                    priorityQueue.enqueue(it.destination)
                }
            }
        }
        return paths
    }
    }
