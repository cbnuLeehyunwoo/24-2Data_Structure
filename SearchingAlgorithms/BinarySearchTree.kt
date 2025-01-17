// comparable 을 상속받는 이유: 대소비교라는 연산이 가능해야한다. 코틀린안에서는 이걸 안하면 대소비교가 안된다.
// T 타입 객체 간에 대소비교가 가능하도록 강제
// 노드 차원이 아닌 트리차원에서의 클래스
class BinarySearchTree<T: Comparable<T>> {
    // 루트노드 하나만 들고있다.
    var root: BinaryNode<T>? = null
    // 루트에 대한 toString 을 리턴하거나 루트가 널값이라면 empty tree 반환
    override fun toString() = root?.toString() ?: "empty tree"

    // 해당 함수는 특정 노드부터 값을 추가하는 함수이지만,
    // 거의 대다수의 경우에는 루트노드에서부터 값을 추가하길 바랄 것이다
    // 즉 밸류만 던져줘도 자동으로 루트노드부터 값을 추가하게 동작을 설계하면 좋을 것이다.
    // 재귀적으로 동작하는 함수이다..
    // 시간 복잡도는 O(log n) ~ O(n)
    // 불균형 트리인 경우는 노드 개수 n과 높이가 가까워진다 즉 O(n)
    // 균형 트리인 경우 트리의 높이가 log n과 가까우므로 O(log n) (이진 탐색이 가능해진다)
    private fun insert(
        node: BinaryNode<T>?,
        value: T
    ): BinaryNode<T> {
        // 만약 현재의 노드가 널이었다면
        // 주어진 값을 저장한 노드 만들어서 리턴 널값아니면 그냥 스킵
        node ?: return BinaryNode(value)
        // 만약 값이 현재 노드값보다 작다면
        if(value < node.value)
            // 왼쪽노드로 가야할 것이다 즉 (왼쪽 자식노드, 값)을 인자로 전달하며 재귀호출
            node.leftChild = insert(node.leftChild, value)
        // 값이 현재 노드값과 같거나 크다면
        else
            // 오른쪽 노드로 가야할 것
            node.rightChild = insert(node.rightChild, value)
        return node
    }
    // 입력된 값이 트리에 존재하는지 확인하는 함수
    // 시간 복잡도는 O(log n) ~ O(n)
    // 불균형 트리인 경우는 노드 개수 n과 높이가 가까워진다 즉 O(n)
    // 균형 트리인 경우 트리의 높이가 log n과 가까우므로 O(log n) (이진 탐색이 가능해진다)
    fun contains(value: T): Boolean {
        // 탐색 노드 정보, 기본값: 루트노드
        var current = root
        // 탐색 노드가 널이 아닐 때까지..
        while (current != null) {
            // 만약 탐색 노드값이 찾고자 하는 값이라면
            if (current.value == value)
                // 값이 트리에 존재한다.
                return true
            // 탐색 노드값이 찾는값이 아니라면
            // 만약 탐색 노드값보다 찾고자 하는 값이 작다면
            current = if (value < current.value) {
                // 탐색 노드를 왼쪽 자식노드로 업데이트
                current.leftChild
                // 크거나 같은경우에는
            } else{
                // 탐색 노드를 오른쪽 자식노드로 업데이트
                current.rightChild
            }
        }
        // while 문을 다돌았는데도 찾는값을 만나지 못한 경우
        return false
    }
    // private remove 함수를 실질적으로 사용하는 퍼블릭 remove e함수
    // private remove 함수는 탐색 시작 노드를 던져주지만
    // 하지만 사람들은 루트값을 넣지 않고 밸류값만 주면 그값을 지워주는 기능을 더 좋아할 것이다.
    fun remove(value: T) {
        // 루트, 던져준 값으로 private remove 호출
        root = remove(root, value)
    }
    // 값이 존재하는지 여부를 리턴하는 함수
    // 시간 복잡도 O(n)
    fun contains(subtree: BinarySearchTree<T>): Boolean {
        // 트리의 모든 값을 저장할 MutableSet<T> set 을 선언
        val set = mutableSetOf<T>()
        // 중위 순회를 하면서 각 노드의 값을 set 에 추가한다.
        // visit 의 동작을 set.add(it)으로 전달
        root?.traverseInOrder {
            set.add(it)
        }
        // 서브트리의 값들이 주어진 트리에 모두 존재하는지 확인하기 위한 플래그 변수선언
        // 기본값 true
        var isEqual = true
        // 서브트리의 루트에 대하여 다시 중위순회를 하면서
        subtree.root?.traverseInOrder {
            // 각 노드 visit 의 동작을 정의
            // 현재 isEqual 값과 set 의 요소에 it이 있는지 체크 &&
            // 즉 하나라도 contains 를 만족하지 않으면 false 가 된다.
            // contains(): set 의 빌트인 함수 존재하면 트루, 아니면 폴스
            isEqual = isEqual&& set.contains(it)
        }
        return isEqual
    }

    //  주어진 노드부터 시작해서 주어진 밸류를 찾아내서 어떻게든 지우는 함수(주어진 값이 어떤 노드에 있는지 모르기 때문에)
    // 시간 복잡도 O(n)
    private fun remove(
        // 지워야 할 노드를 전달하는 매개변수가 아님, 탐색을 시작할 노드정보
        node:BinaryNode<T>?,
        // 지워야 할 밸류
        value: T
    ): BinaryNode<T>? {
        // 현재 탐색중인 노드가 널이라면
        // 널값을 리턴한다
        node ?: return null
        when {
            // 현재 노드의 값이 내가 지우고자 하는 값인경우
            value == node.value -> {
                // 자식노드가 존재하지 않는 경우
                if (node.leftChild == null && node.rightChild == null)
                // null 을 반환하여 부모 노드에서 이 노드를 제거한다.
                // remove 함수에 널값이 재귀호출 된다.
                    return null
                // 오른쪽 자식노드만 존재하는 경우
                if (node.leftChild == null)
                // 부모노드의 부모노드와 연결
                // 부모노드가 이 노드를 제거하고 오른쪽 자식을 현재 노드의 위치로 대신하게 한다
                    return node.rightChild
                // 왼쪽 자식노드만 존재하는 경우
                if (node.rightChild == null)
                    // 부모노드가 이 노드를 제거하고 왼쪽 자식을 현재 노드의 위치로 대신하게 한다
                    return node.leftChild
                // 자식노드가 둘다 있는 경우
                // 오른쪽 서브트리에서 가장 작은 값을 찾는다
                node.rightChild?.min?.value?.let {
                    // 그 값을 현재 노드의 값으로 대체한다.
                    node.value = it
                }
                // 오른쪽 서브트리에서 불러온 값을 삭제하기 위해 remove 함수를 다시 호출한다.
                node.rightChild = remove(node.rightChild, node.value)
            }// 삭제할 값이 현재 노드의 값보다 큰 경우
            // 왼쪽 서브트리로 이동하여 remove 함수를 재귀 호출
            value < node.value -> node.leftChild =
                remove(node.rightChild, value)
            // 삭제할 값이 현재 노드의 값보다 작은 경우
            // 오른쪽 서브트리로 이동하여 remove 함수를 재귀 호출
            else -> node.rightChild = remove(node.rightChild, value)
        }
        // 최종적으로 수정된 노드를 반환한다.
        return node
    }

    fun insert(value: T) {   // 엄청 잘 구현해놀고(제너럴하게) 사용자들에게는 각각 목적에 맞게 일부 기능만 제공하겠다는 의도
        root = insert(root, value)
    }
}
