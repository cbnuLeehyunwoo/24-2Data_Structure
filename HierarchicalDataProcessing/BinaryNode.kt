import java.lang.Math.max
// 제네릭 타입을 사용하여 어떤 타입이든 이진 트리의 값으로
// Comparable 인터페이스를 구현하여 값들 간의 비교가 가능하도록 강제
class BinaryNode<T: Comparable<T>>(var value: T) {
    // 왼쪽 자식노드
    var leftChild: BinaryNode<T>? = null
    // 오른쪽 자식노드
    var rightChild: BinaryNode<T>? = null
    // 현재 노드를 기준으로 최소값을 가진 노드를 반환
    val min: BinaryNode<T>?
        // 사용자 정의 getter()
        // 왼쪽 자식노드가 존재할 경우 왼쪽 자식노드의 min 재귀적으로 호출
        get() = leftChild?.min ?: this

    //현재 노드가 이진 탐색트리의 규칙을 만족하는지 검사
    val isBinarySearchTree: Boolean
        get() = isBST(this, min = null, max = null)

    //In order Traversal 을 수행하는 함수
    // 시간 복잡도 O(n)
    // 이진 탐색 트리의 경우 값들이 오름차순으로 정렬된 순서로 방문한다
    fun traverseInOrder(visit: BinaryVisitor<T>) {
        leftChild?.traverseInOrder(visit)
        visit(value)
        rightChild?.traverseInOrder(visit)
    }

    // Pre order Traversal 을 수행하는 함수
    // 시간 복잡도 O(n)
    fun traversePreOrder(visit: BinaryVisitor<T>) {
        visit(value)
        leftChild?.traverseInOrder(visit)
        rightChild?.traverseInOrder(visit)
    }

    // Post order Traversal 을 수행하는 함수
    // 시간 복잡도 O(n)
    fun traversePostOrder(visit: BinaryVisitor<T>) {
        leftChild?.traverseInOrder(visit)
        rightChild?.traverseInOrder(visit)
        visit(value)
    }

    // 이진 트리의 높이를 계산하는 함수
    // 주어진 노드를 루트로하는 트리의 높이를 계산
    // 시간 복잡도 O(n) n: 트리의 노드 개수(모든 노드를 방문하여 높이를 구하고 비교하므로)
    fun height(node: BinaryNode<T>? = this): Int {
        // 재귀적으로 왼쪽 오른쪽 자식의 높이를 구하고그중 더 큰값을 선택
        return node?.let {
            // 1을 더하는 이유: 현재 노드까지 포함해야하기 때문에
            1 + max(height(node.leftChild), height(node.rightChild))
            // 만약 node 가 null 인 경우 트리의 높이를 -1로 반환
        } ?: -1
    }

    // 주어진 이진트리가 이진탐색트리의 속성을 만족하는지 확인
    // 루트부터 내려가면서 탐색하자 O(n) n: 노드 개수
    // min: 현재 서브트리에서 허용할 수 있는 최소값
    // max: 현재 서브트리에서 허용할 수 있는 최대값
    // 즉 각 노드가 속할 수 있는 범위를 나타내는 지표
    private fun isBST(tree: BinaryNode<T>?, min: T?, max: T?): Boolean {
        // 노드가 널이면 트루를 리턴한다, 비어있는 것이면 BST 이다.
        tree ?: return true
        // 노드의 값이 min 보다 작거나 같다면,(왼쪽에 최솟값이 존재해야하는데.)
        if (min != null && tree.value <= min)
            return false
        // 노드의 값이 max 보다 크다면, (오른쪽에 최댓값이 존재해야하는데.)
        else if (max != null && tree.value > max)
            return false
        // 재귀적으로 왼쪽과 오른쪽 노드를 탐색, 하나라도 불만족한다면 false 가 되게 된다.
        return isBST(tree.leftChild, min, tree.value) &&
                isBST(tree.rightChild, tree.value, max)
    }

    private fun diagram(node: BinaryNode<T>?,
                        top: String = "",
                        root: String = "",
                        bottom: String = ""): String {
        return node?.let {
            if (node.leftChild == null && node.rightChild == null) {
                "$root${node.value}\n"
            } else {
                diagram(node.rightChild, "$top ", "$top┌──", "$top│ ") +
                        root + "${node.value}\n" +
                        diagram(node.leftChild, "$bottom│ ", "$bottom└──", "$bottom ")
            }
        } ?: "${root}null\n"
    }
    // 특정 객체가 다른 객체와 같은지 판단하기 위해 equal 함수를 재정의한 함수
    // other : 현재 Binary Node 객체와 비교될 객체
    // 시간 복잡도 O(N)
    override fun equals(other: Any?): Boolean {
        // 널값이 아닌지,  바이너리 노드인지 체크
        // 하나라도 불만족하면 바로 false 반환
        // <*>는 제네릭 타입을 무시하고 타입만 확인하겠다는 의미
        // 즉 타입만 일치하면 내부 제네릭 타입은 중요하지 않다는 의도
        return if (other != null && other is BinaryNode<*>) {
            // 두 노드의값이 같니
            this.value == other.value &&
                    // 왼쪽 자식노드가 같니 , 연산자 오버로딩. == 를 사용하면 equals 가 호출된다
                    // 즉 재귀적으로 자식노드끼리도 모두 같은지 확인하게 된다
                    this.leftChild == other.leftChild &&
                    // 오른쪽 자식노드가 같니
                    this.rightChild == other.rightChild
        } else {
            // other 가 null 이거나, BinaryNode 가 아니면 false 반환
            false
        }
    }
    override fun toString() = diagram(this)
}
