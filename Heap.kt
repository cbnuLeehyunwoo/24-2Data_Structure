import java.lang.annotation.ElementType
import java.util.Collections

// 단순히 아이템을 추가/삭제하는 인터페이스
// 제네릭 타입 Element 를 사용하여 다양한 타입의 데이터를 처리가능하게..
interface MyCollection<Element> {
    // 컬렉션에 포함된 요소의 개수를 나타냄
    val count: Int
        get
    val isEmpty: Boolean
        get() = count == 0
    fun insert(element: Element)
    // 인덱스를 주지 않아도 지울 수 있는 힙의 대표값이 있을 것이다..
    fun remove(): Element?
    // 인덱스를 주고 해당 위치의 값을 지우는 함수
    fun remove(index: Int): Element?
}
// 힙 인터페이스
// MyCollection 을 상속받으며, peek()함수를 추가로 가짐
interface  HeapInterface<Element> : MyCollection<Element> {
    // 컬렉션내의 값을 보기만 한다..
    // 힙의 종류에 따라 해당 함수는 최대값 혹은 최소값을 리턴하게 될 것이다.
    fun peek(): Element?

}
// ArrayList 를 사용하여 구현하는 힙
abstract class AbstractHeap<Element>() : HeapInterface<Element> {
    // 힙을 ArrayList 를 이용하여 저장한다.
    var elements: ArrayList<Element> = ArrayList<Element>()
    override val count: Int
        get() = elements.size
    // 루트노드는 ArrayList 의 가장 앞에 위치할 것이므로
    // peek 함수는 배열리스트의 첫번쩨 노드(루트노드) 리턴
    override fun peek(): Element? = elements.first()
    // 주어진 인덱스를 바탕으로 구한다..
    private fun leftChildIndex(index: Int) = (2 * index) + 1
    private fun rightChildIndex(index: Int) = (2 * index) + 2
    private fun parentIndex(index: Int) = (index-1) /2

    // priority 에 따라 양수 , 0, 음수를 반환할 것
    abstract fun compare(a: Element, b: Element): Int

    // siftUp 의 작업은 O(log n), 아이템 자체를 추가하는 것은 O(1)
    // 즉 insert() 라는 작업은 O(log n)의 시간복잡도를 갖는다..
    override fun insert(element: Element) {
        // 새로운 노드 추가는 list 의 add 함수로 해결이 가능하다!
        elements.add(element)
        // 방금 추가된 놈이 Heap priority 를 만족하는지 보장할 수 없다
        // 방금 추가된 놈의 인덱스를 던져주면서 siftUp 을 수행한다.
        siftUp(count -1)
    }

    // insert 를 수행했을 때 힙 프로퍼티가 보장되지 않는 상황을 해결해주는 함수
    // 시간 복잡도 O(log n) 최악의 경우 트리의 레벨 - 1 만큼 진행한다.
    private fun siftUp(index: Int) {
        // 주어진 인덱스를 기준으로
        var child = index
        // 부모 노드의 인덱스를 구해낸다
        var parent = parentIndex(child)
        // 인덱스가 0이라면 위에 부모노드가 없을 것
        // 자식노드의 인덱스가 0보다 크고(루트노드가 아니고), 자식노드의 우선순위가 부모노드보다 높을때... swap 을 수행한다.
        while(child > 0 && compare(elements[child], elements[parent])>0) {
            // element 안에서,, child 와 parent 의 자리를 바꾼다..
            Collections.swap(elements, child, parent)
            // 인덱스도 바꿔준다
            child = parent
            parent = parentIndex(child)
        }
    }
    // remove 를 수행했을 때 힙 프로퍼티가 보장되지 않는 상황을 해결해주는 함수
    // 시간복잡도 O(log n) 최악의 경우 트리의 레벨 - 1 만큼 진행할 것이다.
    private fun siftDown(index: Int) {
        // 처음엔 0이 들어갈 것(루트노드부터 진행한다면)
        var parent = index
        while (true) {
            // 왼쪽 자식노드 인덱스
            val left = leftChildIndex(parent)
            // 오른쪽 자식노드 인덱스
            val right = rightChildIndex(parent)
            // 일단 부모노드를 후보로
            var candidate = parent
            // 제일 큰 놈을 찾는 로직
            // 우선 왼쪽과 부모노드를 비교
            // 인덱스가 유효한지 검증하는 로직도 포함되어있음.
            if (left < count &&
                compare(elements[left], elements[candidate]) > 0) {
                // 만약 프로퍼티가 왼쪽 자식노드가 더 높았다면
                // 새로운 후보로 왼쪽 자식노드
                candidate = left
            }
            // 왼쪽과 후보노드(왼쪽일수도 부모노드일수도) 비교
            // 인덱스가 유효한지 검증하는 로직도 포함되어있음.
            if (right < count &&
                compare(elements[right], elements[candidate]) > 0) {
                // 만약 프로퍼티가 오른쪽 자식노드가 더 높았다면
                // 새로운 후보로 오른쪽 자식노드
                candidate = right
            }
            // 만약 제일 큰놈이 parent 인덱스랑 같다면(왼오 자식노드와 비교했음에도 후보교체가 없었다면)
            // 맥스힙 프로퍼티를 만족하는 것이다
            if (candidate == parent)
                // 그대로 리턴
                return
            // 아닌 경우에는 스왑을 진행하고 반복
            // 실제 값을 바꿔주고
            Collections.swap(elements, parent, candidate)
            // 인덱스도 바꿔준다
            parent = candidate
        }
    }
    // 인덱스를 주지 않았을 때 동작하는 remove 함수
    // 일반적으로 루트노드를 리무브하게 된다..
    // 즉 리무브를 하고나면 Heap 의 형태가 망가질 것.
    // 따라서 루트노드를 가장 마지막 노드와 스왑하고 삭제한다
    // 스왑한 노드가 priority 를 만족하기 위해 SiftDown 을 구현해야한다.
    // swap 의 시간 복잡도: O(n), SiftDown 의 시간 복잡도: O(log n)
    override fun remove(): Element? {
        // 비어있으면 널값 리턴(지울게없다)
        if (isEmpty) return null
        // 루트노드와 마지막 노드를 스왑(인덱스 0은 루트노드, 크기 - 1은 마지막 노드)
        Collections.swap(elements, 0, count - 1)
        // 루트노드였던 놈을 지우고 값을 저장
        val item = elements.removeAt(count - 1)
        // 루트노드부터 SHIFT DOWN 실행
        siftDown(0)
        // 지운 값을 반환(루트노드)
        return item
    }

    // 인덱스가 주어졌을 때 해당 인덱스의 값을 지우는 함수
    // 시간 복잡도 O(log n)
    override fun remove(index: Int): Element? {
        // 주어진 인덱스가 유효하지 않다면..(마지막 인덱스는 count - 1일 것이다.)
        if (index >= count) return null
        // if 문을 만족하면 if문 안의 코드가 리턴하는 값을 리턴할 것
        return if (index == count - 1) {
            // 만약 지우고자 하는 값이 마지막 값이라면
            // 지워도 힙 우선순위를 해치지 않는다.
            // removeAt 함수를 호출
            elements.removeAt(count - 1)
            // 지웠을 때 힙 우선순위를 해치는 자리에 있다면
        } else {
            // 일단 지우려는 값이랑 마지막 요소랑 바꾸고
            Collections.swap(elements, index, count - 1)
            // 마지막 요소(지우려는 값)을 지워버린다..
            val item = elements.removeAt(count - 1)
            // 뭘 해야할지 모르기 때문에 siftDown, siftUp 을 둘다 써준다 이러면 확실히 힙 우선순위를 만족하게 될 것
            siftDown(index)
            siftUp(index)
            // 변수 이름만 달랑 써있음, 문법에 안맞지만
            // 코틀린에서는 마지막에 평가된 표현식이 해당 블록의 리턴값으로 사용되게 된다.
            // 코드 맥락상 else 의 경우에는 item 을 리턴하겠음..으로 컴파일러가 해석할 것임
            item
        }
    }
    // 임의의 값을 가진 아이템이 힙의 몇 번째 인덱스에 존재하는지 찾는 함수
    // i : 몇번째 아이템부터 찾아나갈것인지 받는 매개변수
    private fun index(element: Element, i: Int): Int? {
        // i 값이 유효하지 않은 경우
        if(i >= count)
            return null
        // element 와 힙의 i 번째 요소랑 비교했을 때 더 크다면(element 의 프로퍼티가 더 크다면)
        // 뒤를 아무리 찾아도 찾을 수 없음(왜냐면 뒤에는 절대 지금요소보다 큰 프로퍼티를 가진 값이있을수 없기에..)
        if (compare(element, elements[i]) > 0)
            return null
        // 잭팟, 찾았음..
        // 해당 인덱스를 리턴
        if(element == elements[i])
            return i
        // 위의 경우중 아무것도 아닌 경우
        // 왼쪽 찾아보기, (찾을 값, 왼쪽 자식노드인덱스를) 재귀호출로 던짐
        val leftChildIndex = index(element, leftChildIndex(i))
        if(leftChildIndex != null) return leftChildIndex
        // 오른쪽 찾아보기
        val rightChildIndex = index(element, rightChildIndex(i))
        if(rightChildIndex != null) return rightChildIndex
        // 못찾았음..
        return null
    }

    // 임의의 ArrayList 객체를 Heap 객체로 바꿔주는 함수
    // 시간 복잡도 O(n log n), 진짜 딥하게 가면 O(n)
    fun heapify(values: ArrayList<Element>) {
        elements = values
        // 비어있지 않은 경우에만 수행
        if(!elements.isEmpty()) {
            // 아이템 개수를 절반으로 줄인 인덱스부터 루트노드까지..
            // 이런 순서로 진행하는 이유는 위부터 하면 자식노드들밖에 고려안하기에 제대로 소팅이 안된다..
            // 앞에서 위에서부터 SHIFT DOWN 을 할 수 있던 이유는 아래 자식노드들이 전부 프로퍼티를 만족한다는 것을 확신하고 내려가기에...
            (count / 2 downTo 0).forEach {
                // SIFT DOWN 을 수행..
                siftDown(it)
            }
        }
    }
}
// compare()함수를 사용하기 위해 제네릭타입에 Comparable 을 상속받는다
class MaxHeap<Element: Comparable<Element>>() : AbstractHeap<Element>() {
    override fun compare(a: Element, b: Element): kotlin.Int {
        // 매개변수들이 알아서 잘 하겠지 믿고 쓰는 느낌..
        // 앞의거를 주어를 잡고 더 우수할 때는 1 아니면 -1 같으면 0을 리턴하도록 암묵적으로 되어있기에
        // 해당 사실을 믿고 구현한다.
        // a를 기준으로 비교한다.,
        return a.compareTo(b)
    }
}

class MinHeap<Element: Comparable<Element>>(): AbstractHeap<Element>() {
    override fun compare(a: Element, b: Element): kotlin.Int {
        // b를 기준으로 비교하기 때문에
        //  a < b
        //  b < a
        // 즉 이렇게 바뀌기 때문에
        return b.compareTo(a)
    }
}
