// 제네릭 타입 노드를  정의한 클래스
// data class : 코틀린에서 제공하는 특별한 클래스, 주로 데이터를 저장, 전달
// equals(), hashCode(), toString(), copy(), componentN() 등을 기본 제공한다.
data class Node<T> (var value : T, var next : Node<T>? = null){  // 각 노드는 value 라는 값을 저장하고 다음 노드에 대한 링크를 갖는다
    // 마지막 노드는 다음 노드정보에 널값이 할당된다.
    override fun toString(): String {  // toString() 함수는 모든 코틀린 클래스에서 기본적으로 제공하는 메서드
        // 오버라이드 후 노드들의 연결상태를 읽기 쉽게 출력하는 함수로
        return if (next != null) {   // 다음 노드정보가 널이 아니라면 toString()함수를 다시 호출
            "$value -> ${next.toString()}"
        } else {   // 다음 노드정보가 널이라면 마지막 노드
            "$value"
        }
    }
    fun printInReverse() {  // 리스트를 역순으로 출력하는 함수
        this.next?.printInReverse()  //다음 노드가 존재하면 재귀적으로 호출하여 끝까지 간 다음 출력
        // next 가 널값이라면 해당 코드는 실행되지 않는다.
        if (this.next != null) { // 다음 노드가 널값이 아니라면.
            print(" -> ")  // 화살표 출력
        }

        print(this.value.toString()) // 현재 노드를 출력
    }
}
