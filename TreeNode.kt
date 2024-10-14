import com.sun.source.tree.Tree
// 타입 앨리어스
// 트리노드 객체 하나를 매개변수로 받고 아무것도 반환하지 않는(Unit) 함수에 대한 별칭
typealias Visitor<T> = (TreeNode<T>) -> Unit
typealias BinaryVisitor<T> = (T) -> Unit


// 주어진 값(value)와 자식노드(children)을 가지는 클래스
// 제네릭 타입으로 선언, 값은 val 로 할당되며 한번 할당되면 변경할 수 없다.
class TreeNode<T>(val value: T) {
    //값이 변할 수 있다.(Mutable)
    // 현재 노드의 자식 노드들을 저장하는 리스트
    // TreeNode<T> 타입의 자식 노드들만 저장할 수 있다.
    private val children: MutableList<TreeNode<T>> = mutableListOf()

    // 트리노드의 자식노드 리스트에 새로운 자식 노드를 추가하는 함수
    // 뒤의 add 함수는 코틀린의 표준함수, 리스트의 마지막에 child 를 추가한다.
    fun add(child: TreeNode<T>) = children.add(child)

    // 깊이우선탐색하는 함수
    // 시간 복잡도 O(n)
    // 매개변수로 visit 의 역할을 수행할 함수를 입력받음
    fun forEachDepthFirst(visit: Visitor<T>) {
        //현재 노드 탐색
        visit(this)
        //현재 노드의 모든 직접적인 하위노드에 대해
        children.forEach {
            // 재귀호출 실행
            it.forEachDepthFirst(visit)
        }
    }

    // 너비우선탐색하는 함수
    // 시간 복잡도 O(n)
    fun forEachLevelOrder(visit: Visitor<T>) {
        // 현재 노드 탐색
        visit(this)
        // 자식노드들을 저장할 큐 생성
        val queue = LinkedListQueue<TreeNode<T>>()
        // 현재 노드의 모든 자식노드에 대하여
        // 큐에 넣는다(가장 먼저 들어온 노드가 가장 먼저 나간다)
        children.forEach { queue.enqueue(it) }
        // 큐에 있는 노드를 하나 꺼낸다
        var node = queue.dequeue()
        // 꺼낸 노드가 널일때까지
        while (node != null) {
            // 현재 노드 탐색
            visit(node)
            // 현재 노드의 자식노드를 큐에 추가
            node.children.forEach { queue.enqueue(it)}
            // 큐에서 다음 노드 꺼냄
            node = queue.dequeue()
        }
    }
    // 트리에서 특정 값을 가지는 노드를 찾는 함수
    // 시간복잡도 O(n) (모든 노드를 탐색하게 된다)
    fun search(value: T): TreeNode<T>? {
        // 특정 값을 저장할 결과 노드 생성
        var result: TreeNode<T>? = null
        // 너비 우선탐색 호출
        forEachLevelOrder {
            // 각 노드를 검사
            // 중괄호 안의 코드 자체를 visitor 매개변수로 전달
            if (it.value == value) {
                // 값을 찾으면 result 에 저장
                result = it
            }
        }
        // 결과 반환
        return result
    }
    //이진 트리를 문자열로 표현하는 방식을 정의하는 함수
    // 너비 우선 탐색 방식으로 순회하여 각 노드의 값을 포함한 문자열 생성
    // 시간 복잡도 O(n) (모든 문자열을 순회하며 출력에 추가하므로)
    override fun toString(): String {
        // 노드를 저장하기 위한 큐 선언
        val queue = ArrayListQueue<TreeNode<T>>()
        // 현재 레벨에 남아있는 노드의 수를 저장하는 변수
        var nodesLeftInCurrentLevel = 0
        // 최종적으로 반환될 문자열을 저장하는 변수
        var ret = ""
        // 현재 노드를 큐에 추가
        queue.enqueue(this)
        // 큐가 비어있지 않을 때까지
        while (queue.isEmpty.not()) {
            // 현재 레벨의 노드 수를 저장.
            nodesLeftInCurrentLevel = queue.count
            // 현재 레벨의 노드를 다 탐색할 때까지
            while (nodesLeftInCurrentLevel > 0) {
                // 큐에서 노드 꺼내기
                val node = queue.dequeue()
                node?.let {
                    // ret 문자열에 현재 노드의 값을 추가한다
                    ret += "${node.value} "
                    // 현재 노드의 자식 노드들을 큐에 추가한다
                    node.children.forEach { queue.enqueue(it) }
                    // 노드 수 감소
                    nodesLeftInCurrentLevel--
                } ?: break
            }
            ret += "\n"
        }
        return ret
    }
}

