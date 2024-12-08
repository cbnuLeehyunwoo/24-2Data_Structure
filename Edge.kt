
data class Edge<T> (
    // 소스, 타입이 정점
    val source: Vertex<T>,
    // 어디로 가니?
    val destination: Vertex<T>,
    // 가중치도 추가 가능
    val weight: Double? = null
)
