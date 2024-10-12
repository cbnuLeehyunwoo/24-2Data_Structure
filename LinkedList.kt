class LinkedList<T> : Iterable<T>, Collection<T> {
    private var head: Node<T>? = null   // nullable 타입으로 선언, 초기값에 null 할당
    private var tail: Node<T>? = null
    override var size = 0               // Collection 인터페이스의 프로퍼티 구현
        private set //  size 값을 read only 로 만들기 (setter 커스터마이징)

    override fun iterator(): Iterator<T> {   // 이터레이터 구현, 연결 리스트가 '반복 가능'하도록 Iterator 반환
        return LinkedListIterator(this)
    }

    override fun isEmpty(): Boolean {  // 리스트가 비어 있는지를 확인하는 함수 비면 True, 아니면 False
        return size == 0
    }

    override fun contains(element: T): Boolean {  // 특정 요소가 리스트에 포함되어있는지 확인하는 함수
        for (item in this)       // 연결 리스트의 모든 요소를 반복
            if (item == element) return true // 만약 현재 요소가 찾고자 하는 요소라면 true 반환
        return false // 다 돌았어도 못찾으면 false
    }

    override fun containsAll(elements: Collection<T>): Boolean {  // 주어진 컬렉션의 모든 요소가 리스트에 포함되어있는지 확인하는 함수
        for (searched in elements)     // 검색할 요소를 반복
            if (!contains(searched)) return false  // 하나라도 요소가 리스트에 포함되어있지 않다면 false 반환
        return true // 만약 모든 요소가 포함되어있으면 true 반환
    }

    override fun toString(): String {  // 리스트의 문자열 표현을 반환
        if (isEmpty()) {  // 만약 리스트가 비어있다면
            return "Empty list"  // "Empty list" 반환
        }
        return head.toString() // 아니라면, 리스트의 첫 번째 노드에 대한 문자열 표현 반환
    }

    fun push(value: T): LinkedList<T> {         // 제네릭 타입 T의 value 값을 받아 리스트에 추가, LinkedList 타입의 객체 반환
        head = Node(value = value, next = head) // 푸쉬된 새로운 노드가 head 가 되기 때문에 푸쉬된노드 : 헤드, 헤드의 다음 노드 : 현재 헤드
        if (tail == null) {  // 만약 tail 이 널이라면, 연결리스트에 노드가 아무것도 없는 것이므로
            tail = head  // head 노드이면서 tail 노드로 만든다.
        }
        size++  // 연결리스트의 사이즈 증가
        return this  // 현재 리스트 객체를 반환, 이를 통해 메서드 체이닝 가능(push(1).push(2).push(3) 등등)
    }    // 이 리스트를 반환 해줄테니 네가 원하는 작업을 계속해

    fun append(value: T): LinkedList<T> {  // push 는 리스트의 맨 앞에 노드를 추가하지만 append 는 리스트의 맨 뒤에 노드를 추가.
        // 1
        if (isEmpty()) {   // 만약 리스트가 비어있다면
            push(value)    // push 를 하나 append 를 하나 차이가 없다.
            return this    // 현재 리스트 객체를 반환
        }
        // 2
        tail?.next = Node(value = value)  // 현재 테일의 다음 노드를 입력받은 값으로 업데이트 하고 (해당 문장은 tail 이 널이면 아무것도 하지 않는다.
        // 3
        tail = tail?.next   // 현재 테일의 다음 노드를 테일로 설정한다.
        size++  // 리스트의 크기 증가
        return this  // 현재 리스트 객체를 반환
    }

    fun nodeAt(index: Int): Node<T>? {  // 인덱스를 받아 해당 위치의 노드를 반환하는 함수
        // 1
        var currentNode = head   // head 부터 시작
        var currentIndex = 0     // index 0부터 시작
        // 2
        while (currentNode != null && currentIndex < index) {  // 노드가 null 이 아니고, 입력받은 인덱스보다 작을때까지 반복
            currentNode = currentNode.next   // 다음노드로
            currentIndex++   // 인덱스 증가
        }
        return currentNode  // 인덱스만큼 순회한 노드 반환, 인덱스가 유효하지 않을 경우 널값 반환
    }

    fun insert(value: T, afterNode: Node<T>): Node<T> {  // 주어진 노드 뒤에 새로운 노드를 삽입하는 함수
        // 1
        if (tail == afterNode) {  // 만약 주어진 노드가 마지막 노드라면
            append(value)         // append 함수를 사용하는것과 다름없다
            return tail!!         // 삽입한 노드를 반환
        }
        // 2
        val newNode = Node(value = value, next = afterNode.next) // 삽입할 노드를 보관
        // 3
        afterNode.next = newNode // 주어진 노드 다음 노드정보에 삽입할 노드를 할당
        size++  // 리스트의 사이즈 증가
        return newNode  // 삽입한 노드를 반환
    }

    fun pop(): T? {   // 리스트의 첫 번째 요소를 제거하고 반환하는 함수
        if (!isEmpty()) size--  // 만약 리스트가 비어있지 않다면 사이즈 감소
        val result = head?.value  // head 노드의 값을 결과에 할당, 리스트가 비어있다면 널값 할당(head 가 null 이므로)
        head = head?.next  // 헤드노드는 없어지기에 다음 노드를 헤드노드로 할당
        if (isEmpty()) {  // 만약 리스트가 비어있다면 or 첫 번째 요소를 제거했는데 리스트가 빌 경우
            tail = null  // tail 노드를 null 로 설졍
        }
        return result    // 결과값을 반환
    }

    fun removeHead(): T? {         // 첫 번째 노드를 제거하고 반환하는 함수
        val head = head ?: return null   // 만약 head 가 null 이면 null 반환
        size--    // 사이즈 감소
        this.head = head.next  // 헤드노드는 없어지기에 다음 노드를 헤드노드로 할당
        if (isEmpty())  // 만약 리스트가 비어있으면 or 첫 번째 요소를 제거했는데 리스트가 빌 경우
            this.tail = null  // tail 노드를 null 로 설정
        return head.value  // 제거된 노드의 값을 반환
    }

    fun removeLast(): T? {  // 마지막 노드를 제거하고 반환
        // 1
        val head = head ?: return null   // 리스트가 비어있으면 null 반환
        // 2
        if (head.next == null) return pop()  // 만약 노드가 하나뿐이라면 pop() 함수를 쓰는것과 다를게 없다.
        // 3
        size--  // 사이즈 감소
        // 4
        var prev = head          // 이전노드, 기본값 헤드노드
        var current = head       // 현재노드, 기본값 헤드노드
        var next = current.next  // 다음노드, 기본값 헤드노드 다음노드
        while (next != null) {   // 다음노드가 널값이 될 때까지 반복
            prev = current       // 이전노드에 현재노드 할당
            current = next       // 현재노드에 다음노드 할당
            next = current.next  // 다음노드에 다음다음노드 할당
        }
        // 5
        prev.next = null         // 이전노드의 다음노드정보에 널값 할당(연결 끊기)
        tail = prev              // 테일노드에 이전노드 할당
        return current.value     // 노드자체는 메모리에 남아있기에 접근 가능 단 참조가 끝나면 가비지 컬렉터에의해 수거될 수 있다.
    }

    fun removeAfter(node: Node<T>): T? {  // 주어진 노드의 다음 노드를 제거
        val result = node.next?.value  // 제거할 노드의 값을 가져옴
        if (node.next == tail) {  // 노드의 다음값이 테일노드라면.
            tail = node  // 주어진 노드가 테일이 된다.
        }
        if (node.next != null) {  // 만약 주어진 노드의 다음 값이 널이 아니라면(제거할 노드가 있다면)
            size--  // 사이즈 1 감소
        }
        node.next = node.next?.next  // 다음 노드를 제거. 즉 주어진 노드의 다음 노드정보에 다음다음노드의 주소 할당
        return result
    }

    fun printInReverse() {   // 리스트를 역순으로 출력하는 함수
        this.nodeAt(0)?.printInReverse()  // 첫 번째 노드에서부터 재귀적으로 역순 출력
    }

    fun getMiddle(): Node<T>? {   // 리스트의 중간 노드를 반환하는 함수
        var slow = this.nodeAt(0)
        var fast = this.nodeAt(0)
        while (fast != null) {
            fast = fast.next      // fast 한칸 이동
            if (fast != null) {
                fast = fast.next   // slow 한칸 이동
                slow = slow?.next  // fast 한칸 더 이동 즉 slow 두배의 속도로 이동
            }
        }
        return slow  // fast 가 리스트의 끝에 도달한다면 slow 는 리스트의 중간에 도달
    }

    private fun addInReverse(list: LinkedList<T>, node: Node<T>) // 주어진 노드를 역순으로 다른 리스트에 추가
    {
        val next = node.next  // 현재 노드의 다음 노드를 저장
        if (next != null) { // 만약 다음 노드가 널값이 아니라면, (탐색할 노드가 남아있다면)
            addInReverse(list, next) // 재귀적으로 addInReverse(list, next) 를 호출한다.
        }
        list.append(node.value) // 재귀 호출이 끝나면 함수의 호출 스택이 돌아오면서 역순으로 값을 추가
    }

    fun reversed(): LinkedList<T> {  // 리스트를 역순으로 반환하는 함수
        val result = LinkedList<T>()  // 빈 연결리스트 객체 생성
        val head = this.nodeAt(0)  // 현재 리스트의 첫 번째 노드를 가져옴
        if (head != null) {  // 가져온 노드가 널이 아닐 때 (리스트가 비어있지 않을 때)
            addInReverse(result, head)  // addInReverse 함수 호출
        }    // head 노드부터 연결리스트의 모든 노드를 순회하며 result 리스트에 노드를 역순으로 추가
        return result
    }

    private fun append(   // 클래스 내부에서만 호출할 수 있는 append  result 리스트에 현재 노드의 값을 추가하고 현재 노드의 다음 노드를 반환하는 함수
        result: LinkedList<T>,
        node: Node<T>
    ): Node<T>? {
        result.append(node.value)
        return node.next
    }

    fun mergeSorted(   // 정렬된 두 리스트를 병합하는 함수
        otherList: LinkedList<T>
    ): LinkedList<T> {
        if (this.isEmpty()) return otherList   // 만약 현재 리스트가 비어있다면 다른 리스트를 반환
        if (otherList.isEmpty()) return this   // 다른 리스트가 비어있다면 현재 리스트를 반환
        val result = LinkedList<T>()           // 병합된 결과를 저장할 빈 리스트 생성
        var left = nodeAt(0)             // 현재 리스트의 노드 (기본값 첫번째)
        var right = otherList.nodeAt(0)  // 다른 리스트의 노드 (기본값 첫번째)
        while (left != null && right != null) {  // 현재 리스트와 다른 리스트의 노드가 널값이 아닐 때까지
            if ((left.value as Int) < (right.value as Int)) {  // 왼쪽 노드의 값이 더 작으면
                left = append(result, left)  // 결과 리스트에 현재 리스트 노드 추가
            } else {                         // 오른쪽 노드의 값이 더 작으면
                right = append(result, right)  // 결과 리스트에 다른 리스트 노드 추가
            }
        }
        while (left != null) {  // 남은 노드 추가(현재 리스트)
            left = append(result, left) // 남아있는 노드 전부 추가
        }
        while (right != null) { // 남은 노드 추가(다른 리스트)
            right = append(result, right) // 남아있는 노드 전부 추가
        }
        return result  // 병합된 결과 리스트 반환
    }
}

class LinkedListIterator<K> (
    private val list: LinkedList<K>
) : Iterator<K> {
    private var index = 0
    private var lastNode: Node<K>? = null

    override fun next(): K {
        if (index >= list.size) throw IndexOutOfBoundsException()  // 인덱스가 리스트의 크기를 초과하면 오류발생
        lastNode = if (index == 0) {  // 값을 반환하는 if문, lastNode 에 할당할 값 결정
            list.nodeAt(0) // 인덱스가 0이라면 첫 번째 노드를 lastNode 에 할당
        } else
            lastNode?.next   // 0이 아니라면 lastNode 의 다음 노드를 lastNode 에 할당
        index++  // 인덱스 증가
        return lastNode!!.value
    }
    override fun hasNext(): Boolean {  // 메서드 재정의, 다음 요소가 존재하는지 판단하는 함수
        return index < list.size // 인덱스가 리스트의 크기보다 작은지 확인, true : 다음 요소가 존재, false : 다음 요소 없음
    }
}
