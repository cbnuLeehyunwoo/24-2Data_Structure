class LinkedList<T> : Iterable<T>, Collection<T> {
    // nullable 타입으로 선언, 초기값에 null 할당
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    // Collection 인터페이스의 프로퍼티 구현
    override var size = 0
        //  size 값을 read only 로 만들기 (setter 커스터마이징)
        private set
    // 이터레이터 구현, 연결 리스트가 '반복 가능'하도록 Iterator 반환
    override fun iterator(): Iterator<T> {
        return LinkedListIterator(this)
    }
    // 리스트가 비어 있는지를 확인하는 함수 비면 True, 아니면 False
    override fun isEmpty(): Boolean {
        return size == 0
    }
    // 특정 요소가 리스트에 포함되어있는지 확인하는 함수
    // 시간 복잡도 O(n)
    override fun contains(element: T): Boolean {
        // 연결 리스트의 모든 요소를 반복
        for (item in this)
        // 만약 현재 요소가 찾고자 하는 요소라면 true 반환
            if (item == element) return true
        // 다 돌았어도 못찾으면 false
        return false
    }
    // 주어진 컬렉션의 모든 요소가 리스트에 포함되어있는지 확인하는 함수
    // 시간 복잡도 O(m * n) m: elements 의 크기, n: 연결리스트의 크기
    override fun containsAll(elements: Collection<T>): Boolean {
        // 검색할 요소를 반복
        for (searched in elements)
        // 하나라도 요소가 리스트에 포함되어있지 않다면 false 반환
            if (!contains(searched)) return false
        // 만약 모든 요소가 포함되어있으면 true 반환
        return true
    }
    // 리스트의 문자열 표현을 반환하는 함수
    // 시간 복잡도 O(1)
    override fun toString(): String {
        // 만약 리스트가 비어있다면
        if (isEmpty()) {
            return "Empty list"
        }
        // 아니라면, 리스트의 첫 번째 노드에 대한 문자열 표현 반환
        return head.toString()
    }
    // 제네릭 타입 T의 value 값을 받아 리스트에 추가, LinkedList 타입의 객체를 반환하는 메서드
    // 시간 복잡도 O(1)
    fun push(value: T): LinkedList<T> {
        // 푸쉬된 새로운 노드가 head 가 되기 때문에 푸쉬된노드 : 헤드,
        // 헤드의 다음 노드 : 현재 헤드
        head = Node(value = value, next = head)
        // 만약 tail 이 널이라면, 연결리스트에 노드가 하나이므로
        if (tail == null) {
            // head 노드이면서 tail 노드로 만든다.
            tail = head
        }
        // 연결리스트의 사이즈 증가
        size++
        // 현재 리스트 객체를 반환, 이를 통해 메서드 체이닝 가능(push(1).push(2).push(3) 등등)
        return this
    }    // 이 리스트를 반환 해줄테니 네가 원하는 작업을 계속해

    // 리스트의 맨 뒤에 노드를 추가하는 함수
    // push 는 리스트의 맨 앞에 노드를 추가하지만 append 는 리스트의 맨 뒤에 노드를 추가.
    // 시간 복잡도 O(1)
    fun append(value: T): LinkedList<T> {
        // 1 만약 리스트가 비어있다면
        if (isEmpty()) {
            // push 를 하나 append 를 하나 차이가 없다.
            push(value)
            // 현재 리스트 객체를 반환
            return this
        }
        // 2 현재 테일의 다음 노드를 입력받은 값으로 업데이트
        tail?.next = Node(value = value)
        // 3 현재 테일의 다음 노드를 테일로 설정한다.
        tail = tail?.next
        // 리스트의 크기 증가
        size++
        // 현재 리스트 객체를 반환
        return this
    }
    // 인덱스를 받아 해당 위치의 노드를 반환하는 함수
    // 시간 복잡도 O(n)
    fun nodeAt(index: Int): Node<T>? {
        // 1 head 부터 시작
        var currentNode = head
        // index 0부터 시작
        var currentIndex = 0
        // 2 노드가 null 이 아니고, 입력받은 인덱스보다 작을때까지
        while (currentNode != null && currentIndex < index) {
            // 다음노드로
            currentNode = currentNode.next
            // 인덱스 증가
            currentIndex++
        }
        // 인덱스만큼 순회한 노드 반환, 인덱스가 유효하지 않을 경우 널값 반환
        return currentNode
    }

    // 주어진 노드 뒤에 새로운 노드를 삽입하는 함수
    // 시간 복잡도 O(1) , 노드를 찾는것은 NodeAt 함수가 수행
    fun insert(value: T, afterNode: Node<T>): Node<T> {
        // 1 만약 주어진 노드가 마지막 노드라면
        if (tail == afterNode) {
            // append 함수를 사용하는것과 다름없다
            append(value)
            // 삽입한 노드를 반환
            return tail!!
        }
        // 2 삽입할 노드를 보관
        val newNode = Node(value = value, next = afterNode.next)
        // 3 주어진 노드 다음 노드정보에 삽입할 노드를 할당
        afterNode.next = newNode
        // 리스트의 사이즈 증가
        size++
        // 삽입한 노드를 반환
        return newNode
    }
    // 리스트의 첫 번째 요소를 제거하고 반환하는 함수
    // 시간 복잡도 O(1)
    fun pop(): T? {
        // 만약 리스트가 비어있지 않다면 사이즈 감소
        if (!isEmpty()) size--
        // head 노드의 값을 결과에 할당, 리스트가 비어있다면 널값 할당(head 가 null 이므로)
        val result = head?.value
        // 헤드노드는 없어지기에 다음 노드를 헤드노드로 할당
        head = head?.next
        // 만약 리스트가 비어있다면 or 첫 번째 요소를 제거했는데 리스트가 빌 경우
        if (isEmpty()) {
            // tail 노드를 null 로 설졍
            tail = null
        }
        // 결과값을 반환
        return result
    }

    // 첫 번째 노드를 제거하고 반환하는 함수
    // 시간 복잡도 O(1)
    fun removeHead(): T? {
        // 사이즈 감소
        size--
        // 만약 head 가 null 이면 null 반환
        val head = head ?: return null
        // 헤드노드는 없어지기에 다음 노드를 헤드노드로 할당
        this.head = head.next
        // 만약 리스트가 비어있으면 or 첫 번째 요소를 제거했는데 리스트가 빌 경우
        if (isEmpty())
        // tail 노드를 null 로 설정
            this.tail = null
        // 제거된 노드의 값을 반환
        return head.value
    }
    // 마지막 노드를 제거하고 반환하는 함수
    // 시간 복잡도 O(n)
    fun removeLast(): T? {
        // 1 // 리스트가 비어있으면 null 반환
        val head = head ?: return null
        // 2 만약 노드가 하나뿐이라면 pop() 함수를 쓰는것과 다를게 없다.
        if (head.next == null) return pop()
        // 3 사이즈 감소
        size--
        // 4
        // prev (이전) 노드 선언, 기본값 헤드노드
        var prev = head
        // current (현재) 노드 선언, 기본값 헤드노드
        var current = head
        // next (다음) 노드 선언, 기본값 헤드노드 다음노드
        var next = current.next
        // 다음노드가 널값이 될 때까지
        while (next != null) {
            // 이전노드에 현재노드 할당
            prev = current
            // 현재노드에 다음노드 할당
            current = next
            // 다음노드에 다음다음노드 할당
            next = current.next
        }
        // 5 이전노드의 다음노드정보에 널값 할당(연결 끊기)
        prev.next = null
        // 테일노드에 이전노드 할당
        tail = prev
        // 노드자체는 메모리에 남아있기에 접근 가능 단 참조가 끝나면 가비지 컬렉터에 의해 수거될 수 있다.
        return current.value
    }

    // 주어진 노드의 다음 노드를 제거하는 함수
    // 시간 복잡도 O(1)
    fun removeAfter(node: Node<T>): T? {
        // 제거할 노드의 값을 가져옴
        val result = node.next?.value
        // 노드의 다음값이 테일노드라면.
        if (node.next == tail) {
            // 주어진 노드가 테일이 된다.
            tail = node
        }
        // 만약 주어진 노드의 다음 값이 널이 아니라면(제거할 노드가 있다면)
        if (node.next != null) {
            // 사이즈 1 감소
            size--
        }
        // 다음 노드를 제거. 즉 주어진 노드의 다음 노드정보에 다음다음노드의 주소 할당
        // 다음 다음 노드의 주소가 널이라면(없다면) 널값 할당
        node.next = node.next?.next
        // 제거한 노드의 값을 반환
        return result
    }
    // 리스트의 아이템들을 역순으로 출력하는 함수
    fun printInReverse() {
        // 첫 번째 노드에서부터 재귀적으로 역순 출력
        this.nodeAt(0)?.printInReverse()
    }

    // 리스트의 중간 노드를 반환하는 함수
    // 시간 복잡도 O(n)
    fun getMiddle(): Node<T>? {
        var slow = this.nodeAt(0)
        var fast = this.nodeAt(0)
        while (fast != null) {
            // fast 한칸 이동
            fast = fast.next
            // fast 인덱스가 널값이 아니라면 (끝이 아니라면)
            if (fast != null) {
                // fast 한칸 더 이동 즉 fast 는 slow 의 두배의 속도로 이동한다
                fast = fast.next
                // slow 한칸 이동
                slow = slow?.next
            }
        }
        // fast 가 리스트의 끝에 도달한다면 slow 는 리스트의 중간에 도달할 것
        return slow
    }
    // 주어진 노드를 역순으로 다른 리스트에 추가하는 함수
    // 시간 복잡도 O(n)
    private fun addInReverse(list: LinkedList<T>, node: Node<T>)
    {
        // 현재 노드의 다음 노드를 저장
        val next = node.next
        // 만약 다음 노드가 널값이 아니라면, (탐색할 노드가 남아있다면)
        if (next != null) {
            // 재귀적으로 addInReverse(list, next) 를 호출한다.
            addInReverse(list, next)    
        }// 재귀 호출이 끝나면 함수의 호출 스택이 돌아오면서 역순으로 값을 추가
        list.append(node.value)
    }

    // 리스트를 역순으로 반환하는 함수
    // 시간 복잡도 O(n)
    fun reversed(): LinkedList<T> {
        // 빈 연결리스트 객체 생성
        val result = LinkedList<T>()
        // 현재 리스트의 첫 번째 노드를 가져옴
        val head = this.nodeAt(0)
        // 가져온 노드가 널이 아닐 때 (리스트가 비어있지 않을 때)
        if (head != null) {
            // addInReverse 함수 호출
            // head 노드부터 연결리스트의 모든 노드를 순회하며 result 리스트에 노드를 역순으로 추가
            addInReverse(result, head)
        }
        // 연결리스트 객체 반환
        return result
    }
    // 클래스 내부에서만 호출할 수 있는 append
    // result 리스트에 현재 노드의 값을 추가하고 현재 노드의 다음 노드를 반환하는 함수
    private fun append(
        result: LinkedList<T>,
        node: Node<T>
    ): Node<T>? {
        result.append(node.value)
        return node.next
    }
    // 정렬된 두 리스트를 병합하는 함수
    // 시간 복잡도 O(m + n)
    fun mergeSorted(
        otherList: LinkedList<T>
    ): LinkedList<T> {
        // 만약 현재 리스트가 비어있다면 다른 리스트를 반환
        if (this.isEmpty()) return otherList
        // 다른 리스트가 비어있다면 현재 리스트를 반환
        if (otherList.isEmpty()) return this
        // 둘다 비어있지 않은 경우, 병합할 결과를 저장할 빈 리스트 생성
        val result = LinkedList<T>()
        // 현재 리스트의 노드 (기본값 첫번째)
        var left = nodeAt(0)
        // 다른 리스트의 노드 (기본값 첫번째)
        var right = otherList.nodeAt(0)
        // 현재 리스트와 다른 리스트의 노드가 널값이 아닐 때까지
        while (left != null && right != null) {
            // 왼쪽 노드의 값이 더 작으면
            if ((left.value as Int) < (right.value as Int)) {
                // 결과 리스트에 현재 리스트 노드 추가
                left = append(result, left)
                // 오른쪽 노드의 값이 더 작으면
            } else {
                // 결과 리스트에 다른 리스트 노드 추가
                right = append(result, right)
            }
        }
        // 남은 노드 추가(현재 리스트)
        while (left != null) {
            // 남아있는 노드 전부 추가
            left = append(result, left)
        }
        // 남은 노드 추가(다른 리스트)
        while (right != null) {
            // 남아있는 노드 전부 추가
            right = append(result, right)
        }
        // 병합된 결과 리스트 반환
        return result
    }
}

// 연결리스트를 위한 이터레이터 인터페이스 구현
class LinkedListIterator<K> (
    // 이터레이터가 순회할 연결 리스트를 나타냄, 해당 리스트의 노드에 접근하고 값을 가져오는데 사용
    private val list: LinkedList<K>
) : Iterator<K> {
    // 인덱스
    private var index = 0
    // 메서드에서 반환된 마지막 노드를 저장
    private var lastNode: Node<K>? = null
    // 반복 중 다음 요소를 가져오는 함수
    // 시간복잡도 O(n)
    override fun next(): K {
        // 인덱스가 리스트의 크기를 초과하면 오류발생
        if (index >= list.size) throw IndexOutOfBoundsException()
        // 코틀린에서 if 문을 결과값으로 할당할 수 있다, lastNode 에 할당할 값 결정
        lastNode = if (index == 0) {
            // 인덱스가 0이라면 첫 번째 노드를 lastNode 에 할당
            list.nodeAt(0)
            // 0이 아니라면 lastNode 의 다음 노드를 lastNode 에 할당
        } else
            lastNode?.next
        index++  // 인덱스 증가
        return lastNode!!.value
    }   // 메서드 재정의, 다음 요소가 존재하는지 판단하는 함수
        // 시간 복잡도 O(n)
    override fun hasNext(): Boolean {
        // 인덱스가 리스트의 크기보다 작은지 확인, true : 다음 요소가 존재, false : 다음 요소 없음
        return index < list.size
    }
}
