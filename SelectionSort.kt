// 선택 정렬을 수행하는 함수를 ArrayList 의 확장 함수로 구현
// 시간 복잡도
// 최선의 경우 : O(n^2) 항상 모든 경우를 비교해야 함 
// 평균의 경우 : O(n^2) 항상 모든 경우를 비교해야 함
// 최악의 경우 : O(n^2) 항상 모든 경우를 비교해야 함
fun <T : Comparable<T>> ArrayList<T>.selectionSort(showPasses: Boolean = false) {
    // 만약 크기가 2보다 작으면 정렬할 필요가 없음
    if (this.size < 2) return

    // 리스트의 마지막 요소는 자동으로 정렬되므로 this.size - 2 까지만 반복
    for (current in 0 until (this.size - 1)) {
        // 현재 위치를 최솟값의 인덱스로 할당
        var lowest = current
        // current + 1부터 끝까지 탐색하여 최솟값을 찾는다
        for (other in (current + 1) until this.size) {
            // 현재 최솟값보다 작은 값이 발견되면
            if (this[lowest] > this[other]) {
                // 최솟값의 인덱스를 other 로 갱신한다.
                lowest = other
            }
        }
        // 만약 최솟값이 현재 값과 다르다면
        if (lowest != current) {
            // 현재 값과 최솟값을 바꿔준다
            this.swapAt(lowest, current)
        }
        if(showPasses) println(this)
    }
}
