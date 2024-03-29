### 요약
* 여러가지 이유로 변수의 스코프는 좁게 만들어서 활용하는 것이 좋다. 
* 또한 var 보다는 val 를 사용하는 것이 좋다. 
* 람다에서 변수를 캡처하는 사실을 꼭 기억하자.
  * java 에서는 람다외부에 있는 변수를 참조할 때 항상 final 로 선언해야하지만, 코틀린은 아니다. 그래서 조심해야한다.

### 변수의 스코프를 최소화하라
* 프로퍼티보다 지역변수를 사용하는 것이 좋다.
* 최대한 좁은 스코프를 갖게 변수를 사용한다.

```kotlin
fun main() {
    val webtoons = listOf<String>()
    
    // 나쁜 예
    var webtoon: String
    for (index in webtoons.indices) {
        webtoon = webtoons[index]
    }
    
    // 조금 더 좋은 예
    for (index in webtoons.indices) {
        var webtoon = webtoons[index]
    }
    
    // 제일 좋은 예
    for ((index, webtoon) in webtoons.withIndex()) {
    }
}
```

### 장점
* 프로그램을 추적하고 관리하기 쉬워진다. -> 애플리케이션이 간단할수록 읽기도 쉽고 안전하다.
* mutable 프로퍼티는 좁은 스코프에 걸쳐 있을수록, 그 변경을 추적하는 것이 쉽다.
* 변수의 스코프가 너무 넓으면, 다른 개발자에 의해서 변수가 잘못 사용될 수 있다.
* 변수는 읽기 전용 또는 쓰기 전용 여부와 상관 없이, 변수를 정의할 때 초기화되는 것이 좋다.

### 참고
* 여러 프로퍼티를 한꺼번에 설정해야하는 경우에는 구조분해 선언을 활용하는 것이 좋다.

```kotlin
fun main() {
    val (color, description) = when {
        degrees < 5 -> "BLUE" to "cold"
        degrees < 23 -> "YELLOW" to "mild"
        else -> "RED" to "hot"
    }
}
```

### 캡처링
* 2부터 시작하는 숫자 리스트 생성
* 첫 번째 요소 선택 -> 이는 소수
* 남아 있는 숫자 중에서 2번에서 선택한 소수로 나눌 수 있는 모든 숫자 제거
* 시퀀스의 경우 모든 연산은 각 원소에 대해 순차 적으로 적용된다. 즉 첫 번째 원소가 (변환된 다음에 걸러지면서) 처리되고, 다시 두 번째 원소가 처리되며, 이런 처리가 모든 원소에 대해 적용된다. 따라서 원소에 연산을 차례대로 적용하다가 결과가 얻어지면 그 이후의 원소에 대해서는 변환이 이뤄지지 않을 수도 있다.

```kotlin
fun main() {
    val primes = sequence {
        var numbers = generateSequence(2) { it + 1 }

        while (true) {
            val prime = numbers.first()
            yield(prime)
            
            numbers = numbers.drop(1).filter { it % prime != 0 }
        }
    }

    println(primes.take(10).toList())
}
```

* numbers.first() = 2
* numbers.drop(1).filter { it % 2 != 0 }.first()
  * 2 drop 
  * 3 filter 통과 (3 % 2 != 0)
  * numbers.first() = 3
* numbers.drop(1).filter { it % 2 != 0 }.drop(1).filter { it % 3 != 0 }.first()
  * 2 drop
  * 3 filter 통과 (3 % 2 != 0)
  * 3 drop
  * 4 filter 통과안됨 (4 % 2 != 0) -> 5 filter 통과 (5 % 2 != 0) -> 5 filter 통과 (5 % 3 != 0)
  * numbers.first() = 5
* numbers.drop(1).filter { it % 2 != 0 }.drop(1).filter { it % 3 != 0 }.drop(1).filter { it % 5 != 0 }.first()
  * 2 drop
  * 3 filter 통과 (3 % 2 != 0)
  * 3 drop
  * 4 filter 통과안됨 (4 % 2 != 0) -> 5 filter 통과 (5 % 2 != 0) -> 5 filter 통과 (5 % 3 != 0)
  * 5 drop
  * 6 filter 통과안됨 (6 % 2 != 0) -> 7 filter 통과 (7 % 2 != 0) -> 7 filter 통과 (7 % 3 != 0) -> 7 filter 통과 (7 % 5 != 0)
  * numbers.first() = 7
* numbers.drop(1).filter { it % 2 != 0 }.drop(1).filter { it % 3 != 0 }.drop(1).filter { it % 5 != 0 }.drop(1).filter { it % 7 != 0 }.first()
  * 2 drop
  * 3 filter 통과 (3 % 2 != 0)
  * 3 drop
  * 4 filter 통과안됨 (4 % 2 != 0) -> 5 filter 통과 (5 % 2 != 0) -> 5 filter 통과 (5 % 3 != 0)
  * 5 drop
  * 6 filter 통과안됨 (6 % 2 != 0) -> 7 filter 통과 (7 % 2 != 0) -> 7 filter 통과 (7 % 3 != 0) -> 7 filter 통과 (7 % 5 != 0)
  * 7 drop
  * 8 filter 통과안됨 (8 % 2 != 0) -> 9 filter 통과 (9 % 2 != 0) -> 9 filter 통과안됨 (9 % 3 != 0) -> 10 filter 통과안됨 (10 % 2 != 0) -> 11 filter 통과 (11 % 2 != 0) -> 11 filter 통과 (11 % 3 != 0) -> 11 filter 통과 (11 % 5 != 0) -> 11 filter 통과 (11 % 7 != 0)
  * numbers.first() = 11

```kotlin
fun main() {
    val primes = sequence<Int> {
        var numbers = generateSequence(2) { it + 1 }

        var prime: Int
        while (true) {
            prime = numbers.first()
            yield(prime)

            numbers = numbers.drop(1).filter { it % prime != 0 }
        }
    }

    println(primes.take(10).toList())
}
```

* numbers.first() = 2
* numbers.drop(1).filter { it % 2 != 0 }.first()
    * 2 drop
    * 3 filter 통과 (3 % 2 != 0)
    * numbers.first() = 3
* numbers.drop(1).filter { it % 3 != 0 }.drop(1).filter { it % 3 != 0 }.first()
    * 2 drop
    * 3 filter 통과안됨 (3 % 3 != 0) -> 4 filter 통과 (4 % 3 != 0) 
    * 4 drop
    * 5 filter 통과 (5 % 3 != 0)
    * numbers.first() = 5
* numbers.drop(1).filter { it % 5 != 0 }.drop(1).filter { it % 5 != 0 }.drop(1).filter { it % 5 != 0 }.first()
    * 2 drop
    * 3 filter 통과 (3 % 5 != 0)
    * 3 drop
    * 4 filter 통과 (4 % 5 != 0)
    * 4 drop
    * 5 filter 통과안됨 (5 % 5 != 0) -> 6 filter 통과 (6 % 5 != 0)
    * numbers.first() = 6
* numbers.drop(1).filter { it % 6 != 0 }.drop(1).filter { it % 6 != 0 }.drop(1).filter { it % 6 != 0 }.drop(1).filter { it % 6 != 0 }.first()
    * 2 drop
    * 3 filter 통과 (3 % 6 != 0)
    * 3 drop
    * 4 filter 통과 (4 % 6 != 0)
    * 4 drop
    * 5 filter 통과 (5 % 6 != 0)
    * 5 drop
    * 6 filter 통과안됨 (6 % 6 != 0) -> 7 filter 통과 (7 % 6 != 0)
    * numbers.first() = 7