import java.lang.Math.max

// AVLNode 의 노드를 정의하는 클래스, 노드 간의 크기 비교가 가능해야 하기 때문에
// comparable 상속
class AVLNode<T: Comparable<T>> (var value: T) {
    // 높이값 저장
    var height = 0
    // 왼쪽 자식노드
    var leftChild: AVLNode<T>? = null
    // 오른쪽 자식노드
    var rightChild: AVLNode<T>? = null
    // 최솟값을 갖는 노드를 저장
    val min: AVLNode<T>? // 노드 자체를 가리키기 위해
        // 사용자정의 getter()
        // 왼쪽 자식의 min 값을 재귀 호출
        // 왼쪽 자식이 없을 경우 자신이 최솟값이 된다.
        get() = leftChild?.min ?: this

    // 왼쪽 자식노드의 높이
    val leftHeight: Int
        // 없으면 -1
        get() = leftChild?.height ?: -1
    // 오른쪽 자식노드의 높이
    val rightHeight: Int
        // 없으면 -1
        get() = rightChild?.height ?: -1
    // 밸런스팩터 값
    val balanceFactor: Int
        // 왼쪽 높이 - 오른쪽 높이
        get() = leftHeight - rightHeight

    // AVL tree 의 높이를 계산하는 함수
    // 기본값으로 현재 노드를 할당
    fun height(node: AVLNode<T>? = this): Int{
        // node 가 null 이 아닐 때만 실행
        return node?.let {
            // 왼쪽 오른쪽 자식노드의 높이의 최댓값보다 1커야 한다.
            1 + Math.max(height(node.leftChild), height(node.rightChild))
            // 노드가 널이라면 높이가 -1이 된다. 
        } ?: -1
    }

    private fun diagram(node: AVLNode<T>?,
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
    override fun toString() = diagram(this)
}
