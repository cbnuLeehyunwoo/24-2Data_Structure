// interface 를 쓰는 이유가 뭘까?
// 큰 프로잭트에서 이렇게 개발하는게 아주 좋다. 관리에 용이.
interface QueueInterface<T>  {
    fun enqueue(element: T): Boolean
    fun dequeue(): T?
    val count: Int
        get
    val isEmpty: Boolean
        get() = count == 0
    // 데이터를 보기만 하고싶다, 데이터가 없다면 널값 리턴하자
    fun peek(): T?

    // 제네릭 타입을 사용하는 확장함수(Queue interface 를 구현한 모든 객체에서 사용 가능
    // 큐의 원소를 역순으로 정렬하는 함수
    fun QueueInterface<T>.reverse() {
        // 역순으로 저장하기 위해 스택을 생성
        val aux = Stack<T> ()
        // 큐의 원소를 스택에 저장
        // 큐의 원소를 하나 꺼냄
        var next = this.dequeue()
        // 큐가 빌때까지
        while (next != null) {
            // 스택에 큐의 원소를 저장
            aux.push(next)
            // 다음 원소를 꺼낸다
            next = this.dequeue()
        }
        // 스택에서 원소를 하나 꺼냄
        var next2 = aux.pop()
        while (next2 != null) {
            // 꺼낸 원소를 다시 원본 큐에 추가
            this.enqueue(next2)
            // 다음 원소를 꺼낸다
            next2 = aux.pop()
        }
    }
}



class ArrayListQueue<T> : QueueInterface<T> {
    private val list = arrayListOf<T>()

    override val count: Int
        get() = list.size
    // 제일앞에 있는 요소를 리턴하는 함수, 요소가 없으면 널값 리턴,
    // 중괄호를 생략하고 =을 하여 바로 리턴값에 대해 쓸 수 있다.
    // 시간 복잡도 O(1)
    override fun peek(): T? = list.getOrNull(0)
    // 큐의 끝에 새로운 요소를 추가하는 함수
    // 시간 복잡도 O(1)
    // 만약 아이템이 꽉차면 k배 공간을 새로 할당하고 리스트를 복제하므로
    // 해당 경우에는 O(n)
    override fun enqueue(element: T): Boolean {
        // 리스트에 add 라는 함수가 이미 존재한다
        // 마지막에 추가된 요소 뒤에 주어진 요소 추가
        list.add(element)
        return true
    }
    // 리턴만 하는 함수이므로 중괄호 없이 =으로
    //
    override fun dequeue(): T? =
        // 비어있으면 널값을 리턴하고 아니라면 0번째 자리의 요소를 지우고 리턴
        if (isEmpty) null else list.removeAt(0)

    override fun toString(): String = list.toString()
}

class Queue {
}

// 연결 리스트를 이용한 큐
class LinkedListQueue<T> : QueueInterface<T> {
    // 큐의 원소를 저장하기 위한 연결리스트 객체 생성
    private val list = LinkedList<T>()
    // 큐에 현재 저장된 원소의 개수를 저장
    private var size = 0
    // 큐의 원소개수를 반환
    override val count : Int
        // 사용자 정의 게터 정의
        // 이 count 속성에 접근할 때마다 size 의 현재 값을 반환
        get() = size
    // 큐의 맨 앞 원소를 읽는 함수
    // 시간 복잡도 O(1)
    override fun peek(): T? = list.nodeAt(0)?.value
    // 큐에 원소를 추가하는 함수
    // 시간 복잡도 O(1)
    override fun enqueue(element: T): Boolean {
        // 링크드 리스트의 끝에 원소를 추가하는 함수
        list.append(element)
        size++
        return true
    }
    // 큐에서 원소를 제거하고 반환하는 함수
    // 시간 복잡도 O(n)
    override fun dequeue(): T? {
        // 0인덱스의 노드위치를 찾아서 반환 , 만약 ?앞이 널이라면(리스트가 비었다면)
        // 널값 반환
        val firstNode = list.nodeAt(0) ?: return null
        // 큐의 사이즈 감소
        size--
        // 리스트의 맨 앞의 노드를 지우고 반환
        return list.removeHead()
    }

    override fun toString(): String = list.toString()
}

class RingBufferQueue<T>(size: Int) : QueueInterface<T> {
    private val ringBuffer: RingBuffer<T> = RingBuffer(size)
    override val count: Int
        get() = ringBuffer.count
    override fun peek(): T? = ringBuffer.first()

    override fun enqueue(element: T): Boolean =
        ringBuffer.write(element)

    override fun dequeue(): T? =
        if (isEmpty) null else ringBuffer.read()

    override fun toString(): String = ringBuffer.toString()
}
