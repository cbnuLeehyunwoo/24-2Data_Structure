import java.lang.Math.pow
class RadixSort {
}

fun MutableList<Int>.radixSort() {
    // 진법
    val base = 10
    // 작업을 완료했음을 나타내는 플랙
    var done = false
    // 자릿수
    var digits = 1
    // 작업이 완료되지 않았을 때까지
    while(!done) {
        // 바로 작업완료로 만들어준다.

        done = true
        // 리스트 안에 리스트를 선언한다
        // 왜 이렇게 하는가
        // 버켓 역할을 뮤터블리스트가 수행하게 하고..
        // 그 버켓들을 한번에 담을 수 있는 큼직한 어레이 리스트를 만든다..
        val buckets = arrayListOf<MutableList<Int>>().apply{
            for(i in 0..9) {
                this.add(arrayListOf())
            }
        }
        // this 는 radix sort 를 호출한 객체 자체가 될것
        this.forEach {
                number ->
            val remainingPart = number / digits
            // 원하는 자리수의 수를 얻어내고
            val digit = remainingPart % base
            // 해당하는 수의 버켓에 넣어버린다.
            buckets[digit].add(number)
            if(remainingPart > 0) {
                done = false
            }
        }
        //
        digits *= base
        // 지우고
        this.clear()
        // 버켓에 있는걸 순서대로 뽑아서 다시 담는다(평탄화해서(일렬로 쭉 세운다))
        this.addAll(buckets.flatten())
    }
}
// Int 타입의 익스텐션
// 몇째 자리까지 존재하는지 리턴해준다..
fun Int.digits(): Int {
    var count = 0
    var num = this
    while(num != 0) {
        count += 1
        num /= 10
    }
    return count
}
// 해당 자리의 숫자값이 몇인지 리턴해준다.
// 마치 스트링의 인덱스 붙이듯이 자릿수를 왼쪽부터 관리하게 될것..

fun Int.digit(atPosition: Int): Int? {
    if(atPosition > digits()) return null
    var num = this
    val correctedPosition = (atPosition + 1).toDouble()
    while (num / (pow(10.0, correctedPosition).toInt()) != 0) {
        num /= 10
    }
    return num % 10
}
// 사전순으로 생각했을 때 app이 apple보다 앞에 위치하게 될것 즉 해당 자리에 아무것도 없으면 우선순위가 오히려 올라간다..
private fun msdRadixSorted(list: MutableList<Int>, position: Int): MutableList<Int> {
    // 정렬할 아이템중 최대자릿수를 이용
    if(position > list.maxDigits()) return list
    val buckets = arrayListOf<MutableList<Int>>().apply{
        for(i in 0..9) {
            this.add(arrayListOf())
        }
    }
    var priorityBucket = arrayListOf<Int>()
    list.forEach{ number->
        val digit = number.digit(position)
        if(digit == null) {
            priorityBucket.add(number)
            return@forEach
        }
        buckets[digit].add(number)
    }
    priorityBucket.addAll(
        buckets.reduce { result, bucket ->
            if (bucket.isEmpty()) return@reduce result
            result.addAll(msdRadixSorted(bucket, position + 1))
            result
        })
    return priorityBucket
}

fun MutableList<Int>.lexicograpicalSort() {
    val sorted = msdRadixSorted(this, 0)
    this.clear()
    this.addAll(sorted)

}
private fun MutableList<Int>.maxDigits(): Int {
    return this.maxOrNull()?.digits() ?: 0
}
