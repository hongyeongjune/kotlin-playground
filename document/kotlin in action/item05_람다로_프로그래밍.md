### 람다 식의 문법
* run 은 인자로 받은 람다를 실행해주는 라이브러리 함수이다.
* 코틀린에는 함수 호출 시 맨 뒤에 있는 람다 식이라면 그 람다를 괄호 밖으로 빼낼 수 있다.
* 또한, 람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썻다면 호출 시 빈 괄호를 없애도 된다.

```kotlin
fun main() {
    val webtoons = listOf(
        Webtoon(
            subject = "kubera",
            period = 10,
        ),
        Webtoon(
            subject = "true beauty",
            period = 15,
        ),
    )
    
    webtoons.maxBy({ webtoon: Webtoon -> webtoon.period })
    
    // 코틀린에는 함수 호출 시 맨 뒤에 있는 람다 식이라면 그 람다를 괄호 밖으로 빼낼 수 있다.
    webtoons.maxBy() { webtoon: Webtoon -> webtoon.period }
    
    // 또한, 람다가 어떤 함수의 유일한 인자이고 괄호 뒤에 람다를 썻다면 호출 시 빈 괄호를 없애도 된다.
    webtoons.maxBy { webtoon: Webtoon -> webtoon.period }
}
```

### 지연 계산(lazy) 컬렉션 연산
* 코틀린 표준 라이브러리 참조 문서에는 filter() 와 map() 이 리스트를 반환한다고 써 있다.
* 이는 이 연쇄 호출이 리스트 2개를 만든다는 뜻이다.
* 이럴 경우 만약 원소가 수백만개가 되면 호율이 떨어진다.
* 이를 더 효율적으로 만들기 위해서는 각 연산이 컬렉션을 직접 사용하는 대신 시퀀스를 사용하게 만들어야 한다.
* 시퀀스는 중간 결과를 저장하는 컬렉션이 생기지 않기 때문에 원소가 많을 경우 눈에 띄게 성능이 좋아진다.

```kotlin
fun main() {
    webtoons.map { it.subject }.filter { it.startsWith("A") }
    
    webtoons.asSequence()
        .map { it.subject }
        .filter { it.startsWith("A") }
        .toList()
}
```

### 시퀀스 연산 실행: 중간 연산과 최종 연산
* 시퀀스에 대한 연산은 중가 연산과 최종 연산으로 나뉜다.
* 중간 연산은 다른 시퀀스를 반환한다.
* 최종 연산은 결과를 반환한다.

```kotlin
fun main() {
    // 아무런 내용도 출력되지 않는다.
    listOf(1,2,3,4).asSequence()
        .map { 
            print("map($it")
            it * it
        }
        .filter { 
            print("filter($it)")
            it % 2 == 0
        }

    // 최종 연산을 추가하면 출력이 된다.
    listOf(1,2,3,4).asSequence()
        .map {
            print("map($it")
            it * it
        }
        .filter {
            print("filter($it)")
            it % 2 == 0
        }
        // 최종 연산 추가
        .toList()
    
    // map(1) filter(1) map(2) filter(4) map(3) filter(9) map(4) filter(16)
}
```

* 직접 연산을 구현하면 map 이 모두 실행된 후에 filter 가 실행된다.
* 하지만, 시퀀스의 경우에는 map -> filter / map -> filter 순으로 실행된다.
* 따라서 원소에 연산을 차례대로 적용하다가 결과가 얻어지면 그 이후의 원소에 대해서는 변환이 이루어지지 않을 수 있다.

```kotlin
fun main() {
    // 즉시 계산 
    // map -> 1, 4, 9, 16 계산
    // find -> 4
    println(
        listOf(1,2,3,4)
            .map { it * it }
            .find { it > 3 }
    )
    
    // 지연 계산
    // map -> 1 , find -> 1
    // map -> 4 , find -> 4 
    // 종료
    println(
        listOf(1,2,3,4).asSequence()
            .map { it * it }
            .find { it > 3 }
    )
}
```

* 자바 8 스트림은 코틀린 시퀀스와 개념이 같다.
* 코틀린에서 같은 개념을 따로 구현해 사용하는 이유는 예전 버전 자바를 사용하는 경우 자바 8 에 있는 스트림이 없기 때문이다.

