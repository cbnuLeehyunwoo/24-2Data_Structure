// 모든 정점 쌍 간의 엣지 정보를 행렬로 표현
class AdjacencyMatrix<T> : Graph<T> {
    // 정점을 저장하는 리스트
    // 정점의 순서대로 배열에 저장되어 각 정점의 인덱스를 통해 엣지 정보를 찾을 수 있음..
    private val vertices = arrayListOf<Vertex<T>>()
    // 인접 행렬 데이터를 저장하는 2차원 리스트(매트릭스)
    // arrayListOf()는 함수, ArrayList 는 클래스..
    private val weights = arrayListOf<ArrayList<Double?>>()
    override val allVertices: ArrayList<Vertex<T>>
        get() = vertices

    // 새로운 정점을 그래프에 추가하는 함수..
    override fun createVertex(data: T): Vertex<T> {
        // 정점을 하나 만들고, 인덱스를 할당(현재 정점 개수)
        // 정점이 하나 더 들어오는 순간 매트릭스의 크기를 늘려야 할 것이다..
        val vertex = Vertex(vertices.count(), data)
        // 추가
        vertices.add(vertex)
        // 열 추가
        weights.forEach {
            it.add(null)
        }
        // 행 추가
        val row = ArrayList<Double?>(vertices.count())
        repeat(vertices.count()) {
            row.add(null)
        }
        weights.add(row)
        return vertex
    }
    // 방향 엣지를 추가하는 함수
    // 출발점과 도착점 사이에 가중치를 설정
    override fun addDirectedEdge(
        source: Vertex<T>,
        destination: Vertex<T>,
        weight: Double?

    ) {
        // 소스의 인덱스, 데스트의 인덱스 부분에 가중치 추가
        weights[source.index][destination.index] = weight
    }

    // 엣지들을 담는 리스트를 만들어야함..
    // 주어진 정점에서 출발하는 모든 간선들을 탐색하여 리스트로 반환하는 함수
    override fun edges(source: Vertex<T>): ArrayList<Edge<T>> {
        // 빈 리스트 생성
        val edges = arrayListOf<Edge<T>>()
        // 배열의 열 순회
        (0 until weights.size).forEach { column ->
            // 가중치 복사
            val weight = weights[source.index][column]
            // 만약 가중치가 널이 아니었다면(연결되어있었다면)
            if (weight != null) {
                // 새로운 엣지 객체를 만들고 아까 만든 리스트에 저장
                edges.add(Edge(source, vertices[column], weight))
            }
        }
        // 엣지 리스트를 반환
        return edges
    }

    // 엣지의 가중치를 반환하는 함수
    override fun weight(
        source: Vertex<T>,
        destination: Vertex<T>
    ): Double? {
        // 소스, 데스트의 인덱스 정보를 바탕으로 행렬에서 가중치를 찾는다.
        return weights[source.index][destination.index]
    }

    override fun toString(): String {
        val verticesDescription = vertices.joinToString(separator = "\n") {
            "${it.index}: ${it.data}" }
        val grid = weights.map { row ->
            buildString {
                (0 until weights.size).forEach { columnIndex ->
                    val value = row[columnIndex]
                    if (value != null) {
                        append("$value\t")
                    } else {
                        append("ø\t\t")
                    }
                }
            }
        }
        val edgesDescription = grid.joinToString("\n")
        return "$verticesDescription\n\n$edgesDescription"
    }

}

