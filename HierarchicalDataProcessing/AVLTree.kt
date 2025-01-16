import java.lang.Math.max
import kotlin.math.pow

// 이진 트리이기 때문에 노드 간의 비교가 가능해야한다, comparable 상속.
class AVLTree<T: Comparable<T>> {
    // 루트노드 선언, 기본값 null
    var root: AVLNode<T>? = null
    override fun toString() = root?.toString() ?: "empty tree"

    // Right-Right child 패턴에 의한 언밸런싱을 해결하기 위해 leftRotation 을 수행하는 함수
    private fun leftRotate(node: AVLNode<T>): AVLNode<T> { // 전달되는 노드가 타겟 노드이다.
        val pivot = node.rightChild!! // 피벗노드를 잡는다 right-right child 패턴일때만 호출할것이기 때문에 반드시 노드가 존재한다.
        // 피벗 노드의 왼쪽 자식(서브트리)를 타겟노드의 오른쪽 서브트리로 보낸다
        node.rightChild = pivot.leftChild
        pivot.leftChild = node // 타겟 노드를 피벗노드의 왼쪽으로 보낸다.
        // 타겟노드의 위치가 바뀌었으므로 높이정보를 업데이트 해준다..
        node.height = max(node.leftHeight, node.rightHeight) + 1  // 아랫쪽의 노드부터 높이 업데이트가 되는것이 중요하기 때문에 순서가 바뀌면 절대 안된다.
        // 피벗노드의 위치가 바뀌었으므로 높이정보를 업데이트 해준다.
        pivot.height = max(pivot.leftHeight, pivot.rightHeight) + 1
        return pivot
    }
    // Left-Left child 패턴에 의한 언밸런싱을 해결하기 위해 LeftRotation 을 하는 함수
    // Target node 를 인자로 전달한다
    private fun rightRotate(node: AVLNode<T>): AVLNode<T> {
        // 타겟노드의 왼쪽 자식노드를 pivot 노드로 잡는다
        // Left-Left Child 패턴에서만 호출할 것이기 때문에 !!을 붙여도 된다..
        val pivot = node.leftChild!!
        // 타겟 노드의 왼쪽 자식에 피벗 노드의 오른쪽 노드를 던져준다
        node.leftChild = pivot.rightChild
        // 피벗 노드의 오른쪽 자식을 타겟노드로 설정한다..
        pivot.rightChild = node
        // 타겟 노드와 피벗노드의 높이정보를 업데이트
        node.height = max(node.leftHeight, node.rightHeight) + 1
        pivot.height = max(pivot.leftHeight, pivot.rightHeight) + 1
        return pivot
    }

    // Right-Left Child 패턴에 의해 언밸런스 할 경우
    // Right rotation, Left rotation 을 차례로 수행하여 밸런싱하는 함수
    // 타겟 노드를 인자로 전달한다..
    private fun rightLeftRotate(node: AVLNode<T>): AVLNode<T> {
        // 만약 타겟노드의 오른쪽 자식노드가 널값이라면 node 를 반환한다.. 유효하지 않은 경우이기 때문이다.. 널값이 아니라면
        // 노드의 오른쪽 자식노드를 할당.
        val rightChild = node.rightChild ?: return node
        // 타겟 노드의 오른쪽 자식(피벗노드를 타겟 삼아서) 에 대하여 right rotate 를 수행한다..
        // 반환값으로 피벗 노드가 돌아오기 때문에..
        node.rightChild = rightRotate(rightChild)
        // 이제 타겟노드에 대해 leftRotate 를 수행한다
        return leftRotate(node)
    }
    // Left-Right Child 패턴에 의해 트리가 언밸런스 한 경우
    // 타겟의 피벗노드에 대하여(타겟의 왼쪽 자식) left rotation, 타겟 노드에 대하여 right rotation 을 수행
    private fun leftRightRotate(node: AVLNode<T>): AVLNode<T> {
        // 만약 타겟노드의
        val leftChild = node.leftChild ?: return node
        node.leftChild = leftRotate(leftChild)
        return rightRotate(node)
    }

    // 각 상황에 맞게 rotate 함수를 호출하는 함수
    // private 으로 선언되어있기 때문에 외부에서 직접 호출할 수 없다
    // 언제 쓰는가 ? : 트리의 내용이 바뀌면 balance 가 깨질 수 있기 때문에 그때 사용할 것
    private fun balanced(node: AVLNode<T>): AVLNode<T> {
        // 기준 노드의 밸런스 팩터 값에 의해 어떤 로테이트 함수를 호출할지 결정
        return when (node.balanceFactor) {
            // 만약 밸런스 팩터값이 2라면..(left 서브트리의 높이가 더 높다..)
            2 -> {
                // 만약 기준 노드의 왼쪽 노드의 밸런스 팩터값이 -1이라면..
                // 왼쪽으로 치우친 상황 + 오른쪽으로 다시 치우침
                // left-right child 패턴
                if (node.leftChild?.balanceFactor == -1)
                    leftRightRotate(node)
                // 아니라면(left-left child 패턴이라면)
                else
                    // right Rotate 만으로 해결이 가능한 언밸런스이다
                    rightRotate(node)
            }
            // 오른쪽 서브트리의 높이가 더 높은 경우..(오른쪽으로 트리가 치우쳐있다)
            -2 -> {
                // 만약 타겟노드의 오른쪽 자식노드의 밸런스 팩터값이 1이라면..
                // 오른쪽 노드로 치우쳤다가 왼쪽으로 치우친 것..
                // Right-Left child 패턴임
                if (node.rightChild?.balanceFactor == 1)
                    rightLeftRotate(node)
                // right-right child 패턴일때
                else
                    // left rotation 만으로 해결이 가능한 언밸런스이다..
                    leftRotate(node)
            }
            // 위 경우에 해당하지 않는 경우에는 노드를 리턴한다..
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
        // 만약 현재 노드가 널이라면, 제거할 값이 없는것이다
        node ?: return null
        when {
            // 만약 찾는 값과 현재 노드의 값이 같다면(럭키)
            value == node.value -> {
                // 만약 왼오 자식노드가 다 없다면, 리프노드였다면..(럭키)
                if (node.leftChild == null && node.rightChild == null)
                    // 그냥 그대로 현재 노드를 지워버린다
                    return null
                // 만약 오른쪽 자식노드만 있다면
                if (node.leftChild == null)
                    // 부모노드와 오른쪽 자식노드를 이어준다..
                    return node.rightChild
                // 만약 왼쪽 자식노드만 있다면..
                if (node.rightChild == null)
                    // 부모노드와 왼쪽 자식노드를 이어준다..
                    return node.leftChild
                // 여기까지 왔다면 자식노드가 둘다있는 경우(비상)
                // 오른쪽 서브트리의 최솟값을 찾으러 떠난다
                // 찾은 값을 현재 노드의 값에 할당한다
                // 해당 로직으로 인해 오른쪽 서브트리의 값은 반드시 루트노드보다 크거나 같다는 조건이 유지된다.
                node.rightChild?.min?.value?.let {
                    node.value = it
                }
                // 가져온 값을 지우기 위해 출발.. 오른쪽 자식노드, 오른쪽서브트리에서 가장 작은값을 인자로 던져줌
                node.rightChild = remove(node.rightChild, node.value)
            }
            // 값을 아직 찾지 못한경우
            // 만약 찾는 값이 현재 노드의 값보다 작다면
            value < node.value -> node.leftChild =
                    // 왼쪽으로 다시 던진다
                remove(node.leftChild, value)
            // 크거나 같은 경우
            // 오른쪽으로 던진다.
            else -> node.rightChild = remove(node.rightChild, value)
        }
        // 타겟노드를 기준으로 밸런싱을 한번 하고,
        val balancedNode = balanced(node)
        // 높이를 새로 맞춰준다
        balancedNode.height = max(
            balancedNode.leftHeight,
            balancedNode.rightHeight
        ) + 1
        // 밸런스드 노드를 반환한다.(타겟노드의 자리를 대체할것)
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
