### 요약
* var 보다는 val 을 사용하는 것이 좋다.
* mutable 프로퍼티보다는 immutable 프로퍼티를 사용하는 것이 좋다.
* mutable 객체와 클래스보다는 immutable 객체와 클래스를 사용하는 것이 좋다.
* 변경이 필요한 대상을 만들어야 한다면, immutable 데이터 클래스로 만들고 copy 를 활용하는 것이 좋다.
* 컬렉션에 상태를 저장해야 한다면, mutable 컬렉션보다는 읽기 전용 컬렉션을 사용하는 것이 좋다.
* 변이 지점을 적절하게 설계하고, 불필요한 변이 지점은 만들지 않는 것이 좋다.
* mutable 객체를 외부에 노출하지 않는 것이 좋다.

### 가변성을 제한하라
* 읽기 전용 프로퍼티(val)
* 가변 컬렉션과 읽기 전용 컬렉션 구분하기
* 데이터 클래스와 copy

#### 읽기 전용 프로퍼티(val)
* 코틀린은 ```val``` 키워드를 사용하여 읽기 전용 프로퍼티로 만들 수 있다.
* 다만, 읽기 전용 프로퍼티가 ```mutable``` 객체를 담고 있다면, 내부적으로 변할 수 있다.
  * ex. ```val list = mutableListOf(1,2,3)```
* 읽기 전용 프로퍼티는 다른 프로퍼티를 활용하는 사용자 정의 게터로도 정의할 수 있다. 이렇게 var 프로퍼티를 사용하는 val 프로퍼티는 var 프로퍼티가 변할 때 같이 변한다.
  ```kotlin
  var name: String = "webtoon"
  val myName
    get() = "$name"
  
  fun main() {
    println(myName) // webtoon
    name = "naver"
    println(myName) // naver
  }
  ```
* var 은 게터와 세터를 모두 제공하지만, val 은 변경이 불가능하므로 게터만 제공한다. 그래서 val 을 var 로 오버라이드할 수 있다.
  ```kotlin
  interface Element {
    val active: Boolean
  }
  
  class ActiveElement: Element {
    override var active: Boolean = false
  }
  ```
* val 은 정의 옆에 상태가 바로 적히므로, 코드의 실행을 예측하는 것이 훨씬 간단하고, 스마트 캐스트 등의 추가적인 기능을 활용할 수 있다.
  * 아래 예시에서 name 은 게터로 정의했으므로 스마트 캐스트할 수 없다. (게터를 활용하므로 값을 사용하는 시점에 따라 name 이 다를 수 있기 때문)
  ```kotlin
  val name: String? = "webtoon"
  val myName
    get() = name?.let { "$name" }
  
  fun main() {
    if (myName != null) {
      println(myName.length) // 오류
    }
  }
  ```
  
#### 가변 컬렉션과 읽기 전용 컬렉션 구분하기
* 읽기 전용 컬렉션 : Iterable, Collection, Set, List 등...
* 읽고 쓸 수 있는 컬렉션 : MutableIterable, MutableCollection, MutableSet, MutableList, java.util.ArrayList, java.util.HashSet 등...
* 읽기 전용 컬렉션이 내부의 값을 변경할 수 없는 것은 아니다.
  * 코틀린은 이 방식을 통해 immutable 하지 않은 컬렉션을 외부적으로 immutable 하게 보여지게하여 안정성을 얻는다. (불변성, 스레드 안정성 등..) 
  * 예를 들어, ```Iterable<T>.map``` 혹은 ```Iterable<T>.filter``` 함수를 사용하면 변경할 수 있다.
  * 실제 kotlin map 내부 코드를 간단하게 만든 예시
  ```kotlin
  public inline fun <T, R> Iterable<T>.map(transform: (T) -> R): List<R> {
    val destination = ArrayList<R>()
    for (item in this) {
      destination.add(transform(item))
    }
    return destination
  }
  ```
* 리스트를 읽기 전용으로 리턴했을 때 이를 읽기 전용으로만 사용해야한다. 컬렉션 다운캐스팅은 추상화를 무시하는 행위이다.
  * 예를 들어, JVM 에서 listOf 는 자바의 ArrayList 인스턴스를 리턴하고, 이것은 add 와 set 메서드를 제공한다. 따라서 Kotlin 의 MutableList 로 변경할 수 있다.
  * 읽기 전용에서 mutable 로 변경이 필요하면, 복제(copy)를 통해서 새로운 mutable 컬렉션을 만드는 것이 좋다.
  * ex. ```listOf(1,2,3).toMutableList()```

#### 데이터 클래스의 copy
* Immutable 객체의 장점
  * 한 번 정의된 상태가 유지되므로, 코드를 이해하기 쉽다.
  * immutable 객체는 공유했을 때도 충돌이 따로 이루어지지 않으므로, 병렬 처리를 안전하게 할 수 있다.
  * immutable 객체에 대한 참조는 변경되지 않으므로, 쉽게 캐시할 수 있다.
  * immutable 객체는 방어적 복사본을 만들 필요가 없고, 객체를 복사할 때 깊은 복사를 따로 하지 않아도 된다.
  * immutable 객체는 ```set``` 또는 ```map``` 의 키로 사용할 수 있다. (mutable 객체는 키로 사용할 수 없다.)
    * ```set``` 과 ```map``` 은 내부적으로 해시 테이블을 사용하고, 해시 테이블은 처음 요소를 넣을 때 요소의 값을 기반으로 버킷을 결정하기 때문에 요소에 수정이 일어나면 해시 테이블 내부에서 요소를 찾을 수 없다.
* Immutable 객체는 변경할 수 없다는 단점이 있다. 따라서 immutable 객체는 자신의 일부를 수정할 수 있는 새로운 객체를 만들어내는 메서드를 가져야한다.
  ```kotlin
  class User(
    val name: String,
  ) {
    fun modify(name: String) = User(name)
  }
  
  fun main() {
    val user1 = User("naver")
    val user2 = user1.modify("webtoon")
    println(user2)
  }
  ```
* 다만, 위처럼 함수를 계속 만드는 것은 번거로우므로 data 한정자의 copy 메서드를 활용하면, 새로운 객체를 만들 수 있다.
  ```kotlin
  data class User(
    val name: String,
    val surname: String,
  )

  fun main() {
    val user1 = User("naver", "webtoon")
    val user2 = user1.copy(surname = "cloud")
    println(user2)
  }
  ```
  
### 다른 종류의 변경 가능 지점
#### 변경할 수 있는 리스트가 필요할 때 
* mutable 컬렉션 사용 (val + Mutable Collection)
* var 로 읽고 쓸 수 있는 프로퍼티 사용 (var + Immutable Collection)
```kotlin
fun main() {
  val list1 = mutableListOf<String>()
  var list2 = listOf<String>()
  list1.add("webtoon") // ["webtoon"]
  list2 = list2 + "webtoon" // ["webtoon"]
}
```

#### mutable 컬렉션 사용 (val + Mutable Collection)
* 구체적인 리스트 구현 내부에 변경 가능 지점이 있다.
* 멀티스레드 처리가 이루어질 경우, 내부적으로 적절한 동기화가 되어 있는지 확실하게 알 수 없다.

#### var 로 읽고 쓸 수 있는 프로퍼티 사용 (var + Immutable Collection)
* 프로퍼티 자체가 변경 가능 지점이다.
* 멀티스레드 처리의 안정성이 조금 더 좋지만 이 또한 완벽하지는 않다.

#### 정리
* 값을 변경할 수 있는 Collection 을 사용해야한다면 두 가지 방법 중 하나를 선택해서 사용하는 것이 좋다.
* 다만, 프로퍼티와 컬렉션을 모두 변경 가능한 지점으로 만드는 방법은 피하는 것이 좋다. (var + Mutable Collection)

### 변경 가능 지점 노출하지 말기
* mutable 객체를 외부에 노출하는 것은 굉장히 위험하다. (아래 코드와 같이 수정을 할 수 있기 때문)

```kotlin
data class User(
  val name: String,
  val surname: String,
)

class UserRepository {
  private val storedUsers = mutableMapOf<Int, User>()

  fun findAll(): MutableMap<Int, User> {
    return storedUsers
  }
}

fun main() {
  val userRepository = UserRepository()
  val users = userRepository.findAll()
  users[1] = User("naver", "webtoon") // {1=User(name=naver, surname=webtoon)}
}
```

#### 업캐스트하여 가변성을 제한
```kotlin
data class User(
  val name: String,
  val surname: String,
)

class UserRepository {
  private val storedUsers = mutableMapOf<Int, User>()

  fun findAll(): Map<Int, User> {
    return storedUsers
  }
}

fun main() {
  val userRepository = UserRepository()
  val users = userRepository.findAll()
  users[1] = User("naver", "webtoon") // error
}
```