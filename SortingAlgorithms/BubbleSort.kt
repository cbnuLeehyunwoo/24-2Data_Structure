// 버블 소트를 구현한 ArrayList 확장 함수
// 제네릭 타입 T가 Comparable 인터페이스를 구현해야 한다는 조건(요소들 간의 비교가 가능해야 한다)
// 시간 복잡도
// 최선인 경우 : O(n) 리스트가 이미 정렬된 경우 한 번의 패스로 정렬 여부를 확인
// 평균인 경우 : O(n^2) 
// 최악인 경우 : O(n^2) 리스트가 역순일 경우, 모든 요소를 반복적으로 비교하며 스왑
fun <T: Comparable<T>> ArrayList<T>.bubbleSort(showPasses: Boolean = false) {
    // 리스트의 크기가 2보다 작다면 정렬할 필요가 없으므로 즉시 반환
    if(this.size < 2) return

    // 정렬 범위를 리스트 크기에서 1씩 줄이면서 역순으로 진행
    // 리스트 크기가 5라면 end 값은 4, 3, 2, 1로 진행할 것
    for (end in (1 until this.size).reversed()) {
        // 스왑이 발생했음을 기록하는 플랙
        var swapped = false
        // 현재 정렬 범위 내에서 두개의 인접한 요소를 비교
        for (current in  0 until end) {
            // 만약 현재 값이 뒤의 값보다 크다면
            if(this[current] > this[current + 1]) {
                // 현재 값과 뒤의 값을 교환
                this.swapAt(current, current + 1)
                // 교환여부 true 로..
                swapped = true
            }
        }
        // 만약 showPasses 플랙이 트루였다면 현재 반복중인 객체를 출력
        // 각 pass 마다 출력되게 될것
        if(showPasses) println(this)
        // 만약 이번 pass 에서 한바퀴를 다 돌았는데도 swap 이 발생하지 않았다면
        // 정렬이 완료된것. 
        if(!swapped) return
    }
}
