// 그래프의 인터페이스
interface Graph<T> {
    // 정점 하나를 만들고 반환받는 함수
    // 정점에 담길 데이터를 매개변수로
    abstract val allVertices: ArrayList<Vertex<T>>
    fun createVertex(data: T): Vertex<T>
    // 방향, 비방향이 왜 매개변수의 이름까지 같은가? 특히 왜 source dest 가 존재하는가
    // 이름을 그냥 그렇게만 불러지는거지 사실 차이는 없음(비방향함수)
    fun addDirectedEdge(source: Vertex<T>,
                        destination: Vertex<T>,
                        weight: Double?)
    fun addUndirectedEdge(source: Vertex<T>,
                          destination: Vertex<T>,
                          weight: Double?)
    {
        // 양방향으로 만들기 위해 스왑해서 한번 더 호출
        addDirectedEdge(source, destination, weight)
        addDirectedEdge(destination, source, weight)
    }
    // 엣지 하나를 추가하는 함수, 엣지 타입을 받는다 굳이 위에 함수 종류를 두개나 만들어놓고 굳이 또 타입을 받는가?
    // 위의 두 함수는 직접적으로 사용하지 않을 것이기 때문..
    fun add(edge: EdgeType,
            source: Vertex<T>,
            destination: Vertex<T>,
            weight: Double?)
    {
        // 엣지 타입에 따라서..
        when(edge) {
            // 만약 방향이 있는 엣지라면..
            EdgeType.DIRECTED -> addDirectedEdge(source, destination, weight)
            // 만약 방향이 없는 엣지라면..
            EdgeType.UNDIRECTED -> addUndirectedEdge(source, destination, weight)
        }
    }
    // 엣지들을 리턴할 것 같은데? 그럼 매개변수로 주어지는 정점과 이어지는 엣지들을 리턴하는건가?
    fun edges(source: Vertex<T>): ArrayList<Edge<T>>
    // 가중치를 반환하는 함수 특정
    // 특정 엣지를 어떻게 지목할까? 노드와 노드 사이의 엣지일 것
    fun weight(source: Vertex<T>,
               // 노드와 노드 사이에 엣지가 없을 수도 있기 때문에 nullable 리턴
               destination: Vertex<T>): Double?
    // 주어진 두개의 정점 사이의 path 개수를 리턴하는 함수
    fun numberOfPaths(
        source: Vertex<T>,
        destination: Vertex<T>
    ): Int {
        val visited: MutableSet<Vertex<T>> = mutableSetOf()
        return paths(source, destination, visited)
    }

    fun paths(
        source: Vertex<T>, destination: Vertex<T>,
        visited: MutableSet<Vertex<T>>, printPath: Boolean = true
    ): Int {
        var ct = 0
        visited.add(source)
        if (source == destination) {
            ct = 1
            if(printPath) {
                visited.forEach {
                    print("->${it.data}")
                }
                println()
            }
        } else{
            val neighbors = edges(source)
            neighbors.forEach { edge ->
                if (edge.destination !in visited)
                    ct += paths(edge.destination, destination, visited)
            }
        }
        visited.remove(source)
        return ct
    }
    // 소스부터 시작하여 넓이 우선 탐색 진행
    // 큰 제약이 없다면 같은 레벨 사이에서 어떤것이 먼저 탐색되어야하는지는 큰 관계 없다
    fun breadFirstSearch(source: Vertex<T>): ArrayList<Vertex<T>>
    {
        // 그냥 큐
        val queue = LinkedListQueue<Vertex<T>>()
        // 큐에 한번이라도 들어갔던 놈들(또 들어가면 안되므로)
        val enqueued = ArrayList<Vertex<T>>()
        // 방문된 놈들(큐에 들어갔다가 나온애들)
        val visited = ArrayList<Vertex<T>>()
        // 소스노드를 큐에 방금 넣고
        queue.enqueue(source)
        // 큐에 들어갔음 표시
        enqueued.add(source)

        while(true) {
            // 중지조건, 큐에서 디큐할수있는 놈이 없다면(디큐의 리턴이 널이라면) 반복 종료
            val vertex = queue.dequeue() ?: break
            // 큐에 들어갔다가 나온놈이므로
            visited.add(vertex)
            // 걔랑 이어져있는 엣지들을 구하고
            val neighborEdges = edges(vertex)
            neighborEdges.forEach {
                if(!enqueued.contains(it.destination)) {
                    // 한번도 큐에 들어간적 없는 놈들이라면
                    // 큐에 넣는다
                    queue.enqueue(it.destination)
                    enqueued.add(it.destination)
                }
            }
        }
        return visited
    }
    fun depthFirstSearch(source: Vertex<T>): ArrayList<Vertex<T>> {
        val stack = Stack<Vertex<T>>()
        val visited = arrayListOf<Vertex<T>>()
        val pushed = mutableSetOf<Vertex<T>>()
        stack.push(source)
        pushed.add(source)
        visited.add(source)

        outer@ while (true) {
            if(stack.isEmpty) break
            val vertex = stack.peek()!!
            val neighbors = edges(vertex)
            if(neighbors.isEmpty()) {
                stack.pop()
                continue
            }
            for (i in 0 until neighbors.size) {
                val destination = neighbors[i].destination
                // 스택에 들어간적 없는놈을 찾았다면
                if(destination !in pushed) {
                    stack.push(destination)
                    pushed.add(destination)
                    visited.add(destination)
                    // 바로 저 위로 돌아간다..
                    continue@outer
                }
            }
            // 이웃들은 있었지만 별 의미 없었다..
            stack.pop()
        }
        return visited
    }
}

// 실제 개발할때 유용하게 사용하면 좋다
// 이렇게 안하면 개발할때 0은 뭐뭐 1은 뭐뭐 주석을 달아야 할 것..
enum class EdgeType{
    DIRECTED,
    UNDIRECTED
}
