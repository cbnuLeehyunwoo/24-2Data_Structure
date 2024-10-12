// 제네릭 타입 노드를 정의한 클래스
// 각 노드는 값을 저장하고, 다음 노드를 가리키는 링크를 갖는다
data class Node<T>(var value: T, var next: Node<T>? = null) {

    // 노드들의 연결 상태를 문자열로 출력하는 메서드
    // 시간 복잡도: O(n)
    override fun toString(): String {
        // 다음 노드가 있으면 재귀 호출
        return if (next != null) {
            // 코틀린의 문자열 출력에서 $변수 뿐만 아니라 $함수의 반환값도 가능하다. 
            "$value -> ${next.toString()}"
            // 다음 노드가 없다면 재귀 호출X 
        } else {
            "$value"
        }
    }

    // 리스트를 역순으로 출력하는 메서드
    // 시간 복잡도: O(n)
    fun printInReverse() {
        // 재귀적으로 다음 노드를 출력한 후 현재 노드 출력
        // ?. 앞의 값이 널이라면 ?. 뒤에 값은 실행되지 않는다. 
        this.next?.printInReverse()
        if (this.next != null) {
            print(" -> ")
        }
        print(this.value.toString())
    }
}
