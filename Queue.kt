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
