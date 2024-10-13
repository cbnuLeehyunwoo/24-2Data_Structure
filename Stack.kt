interface  StackInterface<Element> {
    val count: Int
        get

    fun peek(): Element?
    val isEmpty: Boolean
        get() = count == 0
    fun push(element: Element)
    fun pop(): Element?
}


class Stack<Element>(): StackInterface<Element> {
    // 기본 제공, array 기반의 List,
    // List 로 봐도 무방하지만 실제로 구조는 array 기반임
    private val storage = arrayListOf<Element>()
    // List 처럼 다룰 수는 있지만
    // 실제는 Array 처럼 임의의 위치에 접근하는게 쉽다)
    override fun toString() = buildString {
        appendLine("----top----")
        storage.asReversed().forEach{
            appendLine("$it")
        }
        appendLine("-----------")
    }

    // element 를 받아서 storage 에 추가하는 함수
    // 시간 복잡도 O(1)
    override fun push(element: Element) {
        // arrayListOf를 씀으로서 쓸수 있는 add 함수
        storage.add(element)
    }

    //  맨 위의 요소를 제거하고 반환하는 함수
    // 시간 복잡도 O(1)
    override fun pop() : Element? {
        // 빼낼게 없는 경우에
        if(isEmpty) {
            return null
        }
        // 맨 뒤에 있는 놈을 지우자. 실제 아이템 개수와 인덱스는 1차이가 있으므로 -1
        return storage.removeAt(count - 1)
    }  // storage 의 가장 오른쪽 위치가 입구이자 출구이다

    override fun peek(): Element? {
        // 이미 구현되어있는 함수, 제일 뒤에있는놈 혹은 아이템이 한개도 없다면 널 리턴
        // 스택의 입구이자 출구는 배열의 부분
        return storage.lastOrNull()
    }
    override val count: Int
        get() = storage.size
}
