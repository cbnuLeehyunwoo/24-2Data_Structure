import java.lang.Math.max
import kotlin.math.pow

// 이진 트리이기 때문에 노드 간의 비교가 가능해야한다, comparable 상속.
class AVLTree<T: Comparable<T>> {
    // 루트노드 선언, 기본값 null
    var root: AVLNode<T>? = null
    override fun toString() = root?.toString() ?: "empty tree"

    private fun leftRotate(node: AVLNode<T>): AVLNode<T> { // 전달되는 노드가 타겟 노드이다.
        val pivot = node.rightChild!! // 피벗노드를 잡는다 right-right child 패턴일때만 호출할것이기 때문에 반드시 노드가 존재한다.
        node.rightChild = pivot.leftChild
        pivot.rightChild = node
        node.height = max(node.leftHeight, node.rightHeight) + 1  // 아랫쪽의 노드부터 높이 업데이트가 되는것이 중요하기 때문에 순서가 바뀌면 절대 안된다.
        pivot.height = max(pivot.leftHeight, pivot.rightHeight) + 1
        return pivot
    }

    private fun rightRotate(node: AVLNode<T>): AVLNode<T> {
        val pivot = node.leftChild!!
        node.leftChild = pivot.rightChild
        pivot.rightChild = node
        node.height = max(node.leftHeight, node.rightHeight) + 1
        pivot.height = max(pivot.leftHeight, pivot.rightHeight) + 1
        return pivot
    }

    private fun rightLeftRotate(node: AVLNode<T>): AVLNode<T> {
        val rightChild = node.rightChild ?: return node
        node.rightChild = rightRotate(rightChild)
        return leftRotate(node)
    }
    private fun leftRightRotate(node: AVLNode<T>): AVLNode<T> {
        val leftChild = node.leftChild ?: return node
        node.leftChild = leftRotate(leftChild)
        return rightRotate(node)
    }

    // 각 상황에 맞게 rotate 함수를 호출하는 함수
    // private 으로 선언되어있기 때문에 외부에서 직접 호출할 수 없다
    // 언제 쓰는가 ? : 트리의 내용이 바뀌면 balance 가 깨질 수 있기 때문에 그때 사용할 것
    private fun balanced(node: AVLNode<T>): AVLNode<T> {
        return when (node.balanceFactor) {
            2 -> {
                if (node.leftChild?.balanceFactor == -1)
                    leftRightRotate(node)
                else
                    rightRotate(node)
            }
            -2 -> {
                if (node.rightChild?.balanceFactor == 1)
                    rightLeftRotate(node)
                else
                    leftRotate(node)
            }
            else -> node
        }
    }
    // 시간 복잡도 O(log n)
    private fun insert(node: AVLNode<T>?, value: T): AVLNode<T>? {
        // 노드가 널이면 바로 리턴을 해버린다(주어진 노드부터 삽입을 해줘야 한다고 추측이 가능할것)
        node ?: return AVLNode(value)
        // 이진 트리의 정렬방식으로 노드의 자리를 찾아준다.
        if (value < node.value) {
            node.leftChild = insert(node.leftChild, value)
        } else {
            node.rightChild = insert(node.rightChild, value)
        }
        // 기존 인서트함수와 차이나는 부분.
        // 왜 노드를 기준으로 호출할까?
        // 왜 결과값을 노드에 다시 받을까? -> 정렬되면 노드값이 바뀔 것이다.
        val balancedNode = balanced(node)

        balancedNode?.height = Math.max(
            balancedNode?.leftHeight ?: 0,
            balancedNode?.rightHeight ?: 0
        ) + 1
        return balancedNode
    }
    fun insert(value: T) {
        root = insert(root, value)
    }

    // 제거하면 밸런스가 깨질것이다. 그래서 뒤에 추가내용 호출
    // 시간 복잡도 (log n)
    private fun remove(
        node: AVLNode<T>?,
        value: T
    ): AVLNode<T>? {
        node ?: return null
        when {
            value == node.value -> {
                if (node.leftChild == null && node.rightChild == null)
                    return null
                if (node.leftChild == null)
                    return node.rightChild
                if (node.rightChild == null)
                    return node.leftChild
                node.rightChild?.min?.value?.let {
                    node.value = it
                }
                node.rightChild = remove(node.rightChild, node.value)
            }
            value < node.value -> node.leftChild =
                remove(node.leftChild, value)
            else -> node.rightChild = remove(node.rightChild, value)
        }
        val balancedNode = balanced(node)
        balancedNode.height = max(
            balancedNode.leftHeight,
            balancedNode.rightHeight
        ) + 1
        return balancedNode
    }
    // 퍼블릭 리무브 함수
    fun remove(value: T) {
        root = remove(root, value)
    }

    fun leafNodesOfPerfect(height: Int): Int {
        return 2.0.pow(height).toInt()
    }

    fun nodesOfPerfect(height: Int): Int {
        return leafNodesOfPerfect(height + 1) - 1
    }
}
