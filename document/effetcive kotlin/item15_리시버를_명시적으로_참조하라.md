### 요약
* 짧게 적을 수 있다는 이유만으로 리시버를 제거하지 말자
* 여러 개의 리시버가 있는 상황 등에는 리시버를 명시적으로 적어 주는 것이 좋다.
* 리시버를 명시적으로 지정하면, 가독성이 향상된다.

### 리시버를 명시적으로 참조하라
* 아래 두 함수의 결과는 같다.

```kotlin
// 리시버 사용 X
fun <T: Comparable<T>> List<T>.quickSort(): List<T> {
    if (size < 2) return this
    val pivot = first()
    val (smaller, bigger) = drop(1).partition { it < pivot }
    return smaller.quickSort() + pivot + bigger.quickSort()
}
```

```kotlin
// 리시버 사용 O
fun <T: Comparable<T>> List<T>.quickSort(): List<T> {
    if (this.size < 2) return this
    val pivot = this.first()
    val (smaller, bigger) = this.drop(1).partition { it < pivot }
    return smaller.quickSort() + pivot + bigger.quickSort()
}
```

### 여러 개의 리시버
* 스코프 내부에 둘 이상의 리시버가 있는 경우, 리시버를 명시적으로 나타내면 좋다.

```kotlin
class Node(val name: String) {
    private fun create(name: String): Node? = Node(name)

    fun makeChild(childName: String): Node? {
        return create("$name.$childName").apply { print("Created $name") }
    }
}

fun main() {
    // Created parent 출력
    Node("parent").makeChild("child")
}
```

* 위 코드에서 apply 함수 내부에서 this 의 타입이 Node? 라서, 이를 직접 사용할 수 없어 ```Created parent``` 가 출력된다. 
* 실제로, apply 안의 print 문에서 리시버를 그대로 사용하면 컴파일 에러가 발생한다. 

```kotlin
class Node(val name: String) {
    private fun create(name: String): Node? = Node(name)

    fun makeChild(childName: String): Node? {
        // 컴파일 에러
        return create("$name.$childName").apply { print("Created ${this.name}") }
    }
}
```

* 이를 사용하려면 언팩을 해야하고, 이렇게 하면 다음과 같은 결과가 나온다.

```kotlin
class Node(val name: String) {
    private fun create(name: String): Node? = Node(name)

    fun makeChild(childName: String): Node? {
        return create("$name.$childName").apply { print("Created ${this?.name}") }
    }
}

fun main() {
    // Created parent.child 출력
    Node("parent").makeChild("child")
}
```

* 이처럼 리시버가 명확하지 않다면, 명시적으로 리시버를 적어서 이를 명확하게 해주는 것이 좋다.
* 추가로, 외부에 있는 리시버를 사용하려면 레이블을 사용하면된다. 

```kotlin
class Node(val name: String) {
    private fun create(name: String): Node? = Node(name)

    fun makeChild(childName: String): Node? {
        return create("$name.$childName").apply { print("Created ${this?.name} in ${this@Node.name}") }
    }
}

fun main() {
    // Created parent.child in parent 출력
    Node("parent").makeChild("child")
}
```

* 리시버를 명확하게 작성하면 코드를 안전하게 사용할 수 있고, 가독성도 향상된다.