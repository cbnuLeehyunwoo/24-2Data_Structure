

// 시간복잡도 O(n log n)
// 각 병합 단계에서 새 리스트를 생성하므로..
// 공간 복잡도는 이론상 O(n log n)이지만 좀더 신경 써준다면 O(n)까지 낮출 수 있다.
fun <T: Comparable<T>> List<T>.mergeSort(): List<T> {
    // 자신의 사이즈가 2미만이라면 정렬할 필요가 없으니까 그대로 리턴
    if(this.size < 2) return this
    // 절반으로 쪼개야하기에 절반위치를 찾아놓는다
    val middle = this.size / 2

    // sublist 는 새로운 리스트를 반환한다.
    // 머지소트 함수는 리턴 타입이 리스트이다.
    // 머지소트 함수에서 리턴된 리스트는 정렬되었음을 믿고 코딩하고 있다...
    // 즉 절반씩만 정렬되어서 튀어나올 것이다
    val left = this.subList(0, middle).mergeSort()
    val right = this.subList(middle, this.size).mergeSort()
    // 마지막 두 묶음을 합치고 정렬해준다.
    return merge(left, right)
}
private fun <T: Comparable<T>> merge(left: List<T>, right: List<T>): List<T> {
    // 왼쪽 리스트와 오른쪽 리스트에서 비교할 요소를 가리킴
    var leftIndex = 0
    var rightIndex = 0
    // 결과를 저장할 리스트 생성
    var result = mutableListOf<T>()
    // 두 리스트의 끝에 도달하기 전까지 반복한다.
    while (leftIndex < left.size && rightIndex < right.size) {
        // 왼쪽에서 값 추출
        // 왼쪽 선수 입장
        val leftElement = left[leftIndex]
        // 오른쪽에서 값 추출
        // 오른쪽 선수 입장
        val rightElement = right[rightIndex]
        // 만약 오른쪽이 더 크다면..
        // 왼쪽이 졌다.. 왼쪽 다음 선수 나와라
        if(leftElement < rightElement) {
            // 왼쪽 요소를 결과리스트에 집어넣고
            result.add(leftElement)
            // 왼쪽 인덱스를 증가시킴
            leftIndex += 1
        }
        // 만약 왼쪽이 더 크다면..
        // 오른쪽이 졌다.. 오른쪽 다음 선수 나와라
        else if (leftElement > rightElement) {
            // 결과리스트에 오른쪽 값을 집어넣고
            result.add(rightElement)
            // 오른쪽 인덱스를 증가시킴
            rightIndex += 1
        }
        // 비긴경우(둘이 크기가 똑같을 경우)
        else {
            // 그냥 둘다 결과리스트에 넣어버린다
            result.add(leftElement)
            leftIndex += 1
            result.add(rightElement)
            rightIndex += 1
        }
    }
    // 왼쪽의 최종승리일경우
    // 왼쪽의 선수들이 아직 남아있다..
    if (leftIndex < left.size)
        // 순서대로 결과에 넣어준다(선수들끼리는 이미 정렬되어있으므로)
        result.addAll(left.subList(leftIndex, left.size))
    // 오른쪽의 최종승리일결루
    // 오른쪽의 선수들이 아직 남아있다..
    if (rightIndex < right.size)
        // 순서대로 결과에 넣어준다(선수들끼리는 이미 정렬되어있으므로)
        result.addAll(right.subList(rightIndex, right.size))
    return result
}
// 이터러블 타입의 객체에 대하여 동작하도록 하는 함수
// 비교 개능해야하기에 컴패어러블 상속
fun <T: Comparable<T>> merge(
    first: Iterable<T>,
    second: Iterable<T>
    ): Iterable<T> {
    val result = mutableListOf<T>()
    val firstIterator = first.iterator()
    val secondIterator = second.iterator()
    // 결과물을 담은 리스트를 생성

    if(!firstIterator.hasNext()) return second
    if(!secondIterator.hasNext()) return first

    var firstEl = firstIterator.nextOrNull()
    var secondEl = secondIterator.nextOrNull()
    // 배틀하는 내용
    // 즉 남아있는 선수가 있어야 배틀을 할 수 있다..
    while (firstEl != null && secondEl != null) {
        when{
            // 오른쪽이 이겼을 때..
            firstEl < secondEl -> {
                // 왼쪽 선수를 결과리스트에 넣어준다
                result.add(firstEl)
                // 다음 호출
                firstEl = firstIterator.nextOrNull()
            }
            // 왼쪽이 이겼을 때..
            secondEl < firstEl -> {
                // 오른쪽 선수를 결과 리스트에 넣어준다
                result.add(secondEl)
                // 이터레이터 증가
                secondEl = secondIterator.nextOrNull()
            }
            // 비겼을 때..
            else -> {
                // 왼쪽 오른쪽 둘다 넣어주고 이터레이터를 증가시킨다..
                result.add(firstEl)
                result.add(secondEl)
                firstEl = firstIterator.nextOrNull()
                secondEl = secondIterator.nextOrNull()
            }
        }
    }
    // 이긴 팀은 배열에 아직 다 안들어갔을 것..
    // 왼쪽팀이 이겼을 경우
    // 이터레이터가 널이 아닐 것이다..
    // 널일때까지(끝까지) 반복
    while (firstEl != null) {
        result.add(firstEl)
        firstEl  = firstIterator.nextOrNull()
    }
    // 오른쪽 팀이 이겼을 경우
    // 이터레이터가 널이 아닐 것이다..
    // 널일때까지(끝까지) 반복
    while (secondEl != null){
        result.add(secondEl)
        secondEl = secondIterator.nextOrNull()
    }
    return result
}
// 이터레이터는 마지막 next() 호출 시 에러가 발생하므로 hasNext()를 먼저
// 호출해주는 도움 함수
private fun<T> Iterator<T>.nextOrNull(): T? {
    // 만약 다음 값이 있으면 다음 값을 리턴하고
    // 없다면 널을 리턴
    return if (this.hasNext()) this.next() else null
}

