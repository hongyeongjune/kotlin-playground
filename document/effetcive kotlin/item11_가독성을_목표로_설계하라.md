### 요약

### 인식 부하 감소
```kotlin
fun main() {
    // 구현 A
    if (person != null && person.isAdult) {
        view.showPerson(person)
    } else {
        view.showError()
    }
    
    // 구현 B
    person?.takeIf { it.isAdult }
        ?.let(view::showPerson)
        ?: view.showError()
}
```

* B 는 일단 읽고 이해하기 어렵다.
* 가독성이란 코드를 읽고 얼마나 빠르게 이해할 수 있는지를 의미한다.
* 코틀린 초보자는 A 가 더 읽고 이해하기 쉽다.
* 구현 A 는 그냥 일반적으로 사용되는 관용구 (if/else, &&, 메서드호출)을 사용하고 있다.
* 구현 B 는 코틀린에서는 꽤 일반적으로 사용되는 관용구 (안전호출, takeIf, let, Elvis 연산자, 제한된 함수 레퍼런스)를 사용하고 있다.
* 신입 개발자를 채용했는데, let, takeIf 가 무엇인지 모른다면 이해시키기 위해 하루가 걸릴 수도 있다.
* 숙련된 코틀린 개발자도 B 코드는 익숙하지 않을 수 있다. (숙련된 개발자라고 내내 코틀린만 붙잡고 있는건 아니기 때문)
* 사용 빈도가 적은 관용구는 코드를 복잡하게 만든다. 그리고 그런 관용구들을 한 문장 내부에 조합해서 사용하면 복잡성은 훨씬 더 빠르게 증가한다.
* 구현 A 는 수정이 쉽다. if 블록에 작업을 추가한다고 생각하면 쉽게 추가할 수 있을 것 이다.
* 하지만, 구현 B 는 더 이상 함수 참조를 사용할 수 없으므로, 코드를 수정해야한다. -> Elvis 연산자의 오른쪽 부분이 하나 이상의 표현식을 갖게하려면 함수를 추가로 사용해야한다.
* 구현 A 는 디버깅도 간단한다.

```kotlin
fun main() {
    // 구현 B
    // Elvis 연산자의 오른쪽 부분이 하나 이상의 표현식을 갖게하려면 함수를 추가로 사용해야한다.
    person?.takeIf { it.isAdult }
        ?.let(view::showPerson)
        ?: run {
            view.showError()
            view.hideProgress()
        }
}
```

* 정리하면, 기본적으로 **'인지 부하'**를 줄이는 방향으로 코드를 작성해야한다.

### 극단적이 되지 않기
* 앞 내용을 통해서 'let 을 절대로 쓰면 안된다'로 이해하면 안된다.
* let 은 좋은 코드를 만들기 위해서 다양하게 활용되는 인기 있는 관용구이다.
* 예를 들어, nullable 가변 프로퍼티가 있고, null 이 아닐 때만 어떤 작업을 수행해야 하는 경우가 있다고 가정하자.

* 가변 프로퍼티는 쓰레드와 관련된 문제를 발생시킬 수 있으므로, 스마트 캐스팅이 불가능하다. 이 때 대표적으로 let 을 사용하여 해결할 수 있다.

```kotlin
class Person(val name: String)
var person: Person? = null

fun main() {
    person?.let { it.name }
}
```

* 연산을 아규먼트 처리 후로 이동 시킬 때 
  * ```print(students.filter{}.joinToString{})``` -> ```students.filter{}.joinToString{}.let(::print)```
* 데코레이터를 사용해서 객체를 Wrap 할 때
  * ```var obj = FileInputStream("/file.gz").let(::BufferedInputStream).let(::ZipInputStream).let(::ObjectInputStream).readObject() as SomeObject```

