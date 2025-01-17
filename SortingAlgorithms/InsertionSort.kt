// 삽입정렬을 ArrayList 의 확장함수로 구현
// 시간 복잡도
// 최선인 경우 : O(n) 내부 루프가 실행되지 않고 외부 루프만 실행
// 평균인 경우 : O(n^2)
// 최악인 경우 : O(n^2) 내부 루프에서 매번 전체 요소를 비교해야 함
fun <T : Comparable<T>> ArrayList<T>.insertionSort(showPasses: Boolean = false) {
    // 만약 크기가 2보다 작다면 정렬할 필요가 없다
    if (this.size < 2) return

    // 1부터 this.size - 1 까지의 current 에 대해
    // 인덱스 0은 이미 정렬된 것으로 간주하기 때문에 포함하지 않음.
    for (current in 1 until this.size) {
        // 1부터 current 를 뒤집은 인덱스에 대해
        for (shifting in (1 .. current).reversed()) {
            // 만약 현재 값이 이전 값보다 작다면..
            if(this[shifting] < this[shifting - 1]) {
                // 두 요소를 스왑하여 더 작은 값을 앞으로 이동한다.
                this.swapAt(shifting, shifting - 1)
                // 만약 이전 요소가 더 작다면..
                // 해당 pass 에서의 그룹은 정렬되었음이 자명하다.
                 } else{
                // 반복을 중단
                break
               }
            }
            if(showPasses) println(this)
        }
    }
