// Graph 상속받음..
// 정점, 정점에 연결된 엣지리스트 들을 통해 그래프를 표현
class AdjacencyList<T> : Graph<T> {
    // 타입이 해쉬 맵..  키가 정점, 밸류는 엣지의 리스트
    // 인접 리스트가 마치 원하는 정점을 키로 엣지들을 얻어낼 수 있는 딕셔너리 형태로 생각해볼수도 있을 것.
    private val adjacencies: HashMap<Vertex<T>,
            ArrayList<Edge<T>>> = HashMap()
    // 그래프에 있는 모든 정점을 반환..
    override val allVertices: ArrayList<Vertex<T>>
        // adjacencies.keys 는 정점들의 집합을 반환,
        // 이를 연결 리스트로 변환하여 반환.
        get() = ArrayList(adjacencies.keys)
    //val vertices: Set<Vertex<T>>
    //    get() = adjacencies.keys
    // 주어진 모든 그래프의 정점만 현재 그래프의 adj 에 복사
    fun copyVertices(graph: AdjacencyList<T>) {
        // 그래프의 모든 정점에 대해..
        graph.allVertices.forEach {
            // 정점에 대한 엣지리스트를 빈값으로 초기화한다.
            adjacencies[it] = arrayListOf()
        }
    }

    // 새로운 정점을 생성하고 그래프에 추가..및 리턴
    override fun createVertex(data: T): Vertex<T> {
        // 해쉬맵의 아이템 개수, 처음에는 0이 리턴될것 만들때마다 인덱스가 1씩 늘어날것이다..
        // 현재 아이템 개수를 새로 만들 정점의 인덱스로 건네주면 딱이다
        val vertex = Vertex(adjacencies.count(), data)
        // 해쉬맵에서 방금 만든 정점을 키값으로 가지는 빈 리스트를 만들어준다.
        adjacencies[vertex] = ArrayList()
        // 방금 만든 정점을 리턴
        return vertex
    }

    // 방향 엣지를 그래프에 추가하는 함수
    override fun addDirectedEdge(
        // 소스, 목적지, 가중치를 인자로 받음
        source: Vertex<T>, destination:
        Vertex<T>, weight: Double?
    ) {
        // 소스와 데스트를 연결하는 엣지 클래스 객체를 하나 만들고 땡
        val edge = Edge(source, destination, weight)
        // 방금 만든 엣지가 이어져있는 소스노드를 키값으로 가지는 리스트에다가 엣지를 전해준다..
        adjacencies[source]?.add(edge)
    }

    // 인자로 주어진 정점에서 나가는 모든 엣지를 반환하는 함수
    override fun edges(source: Vertex<T>) =
        // 만약 정점에서 나가는 엣지가 없다면 빈 리스트를 반환(adj[source]의 반환값이 널이라면)
        adjacencies[source] ?: arrayListOf()

    // directed 엣지에 대한 가중치를 리턴해주는 함수
    override fun weight(source: Vertex<T>, destination: Vertex<T>):
            Double? {
        // 소스노드로부터 나가는 엣지들의 리스트에 대해
        // 조건을 만족하는 첫 번째 객체를 리턴..(첫 번째 요소만 검사한다는 의미가 아님)
        // 중괄호 안쪽을 만족하는 과 연결된 첫 번째 아이템이 리턴되거나 끝끝내 못찾았다면 NULL 이 리턴될 것..
        return edges(source).firstOrNull { it.destination ==
                destination }?.weight
    }


    override fun toString(): String {
        return buildString {
            adjacencies.forEach { (vertex, edges) ->
                val edgeString = edges.joinToString { it.destination.data.toString() }
                append("${vertex.data} ---> [ $edgeString ]\n")
            }
        }
    }
}
