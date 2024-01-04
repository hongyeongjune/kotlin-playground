### 요약
* 코틀린의 인터페이스는 디폴트 구현을 포함할 수 있고, 프로퍼티도 포함(자바 불가능)할 수 있다.
* 모든 코틀린 선언은 기본적으로 final 이며 public 이다.
* 선언이 final 이 되지 않게 만들려면 앞에 open 을 붙여야 한다.
* internal 선언은 같은 모듈 안에서만 볼 수 있다.
* 중첩 클래스는 기본적으로 내부 클래스가 아니다. 바깥쪽 클래스에 대한 참조를 중첩 클래스 안에 포함시키려면 inner 키워드를 중첩 클래스 선언 앞에 붙여서 내부 클래스로 만들어야 한다.
* sealed 클래스를 상속하는 클래스를 정의하려면 반드시 부모 클래스 정의 안에 중첩(또는 내부) 클래스로 정의해야 한다. (코틀린 1.1 부터는 같은 파일 안에만 있으면 된다.)
* 초기화 블록과 부 생성자를 활용해 클래스 인스턴스를 더 유연하게 초기화할 수 있다.
* field 식별자를 통해 프로퍼티 접근자(getter setter)안에서 프로퍼티의 데이터를 저장하는데 쓰이는 뒷받침하는 필드를 참조할 수 있다.
* 데이터 클래스를 사용하면 컴파일러가 equals, hashCode, toString, copy 등의 메서드를 자동으로 생성해준다.
* 클래스를 위임하면 위임 패턴(by)을 구현할 때 필요한 수많은 코드 준비를 줄일 수 있다.
* 객체 선언을 사용하면 코틀린답게 싱글턴 클래스를 정의할 수 있다.
* 동반 객체는 자바의 정적 메서드와 필드 정의를 대신한다.
* 동반 객체도 다른 객체와 마찬가지로 인터페이스를 구현할 수 있다. 외부에서 동반 객체에 대한 확장 함수와 프로퍼티를 정의할 수 있다.
* 코틀린의 객체 식은 자바의 익명 클래스를 대신한다. 하지만 코틀린 객체식은 여러 인스턴스를 구현하거나 객체가 포함된 영역에 있는 변수 값을 변경할 수 있는 등 자바 무명 내부 클래스보다 더 많은 기능을 제공한다.


### 코틀린 인터페이스
* 코틀린 인터페이스 안에는 추상 메서드뿐 아니라 구현이 있는 메서드도 정의할 수 있다.
* 다만, 인터페이스에는 아무런 상태(필드)도 들어갈 수 없다.
* 코틀린에서는 override 변경자를 꼭 사용해야 한다. (자바는 아니다.) 
  * 즉, 상위 클래스에 있는 메서드와 시그니처가 같은 메서드를 우연히 하위 클래스에서 선언하는 경우 컴파일이 안 되기 때문에 override 를 붙이거나 메서드 이름을 바꿔야만 한다.
* 디폴트 메서드 앞에 default 키워드로 꾸밀 필요없이 그냥 메서드 본문을 메서드 시그니처 뒤에 추가하면 된다.

#### 동일한 이름을 가진 디폴트 메서드를 구현하는 다른 인터페이스 정의
```kotlin
interface Clickable {
  fun showOff() = println("I'm clickable")
}

interface Focusable {
  fun showOff() = println("I'm focusable")
}

class Button : Clickable, Focusable {
  // example 1 : 새로운 구현
  override fun showOff() {
    println("I'm button")
  }

  // example 2 : 상속한 구현 중 하나만 호출
  override fun showOff() = super<Clickable>.showOff()

  // example 3 : 상속한 구현 중 하나만 호출
  override fun showOff() = super<Focusable>.showOff()

  // example 4 : super 를 이용하여 둘 다 호출
  override showOff() {
    super<Clickable>.showOff()
    super<Focusable>.showOff()
  }
}
```

* 한 클래스에서 위 두 인터페이스를 구현하면 어느쪽도 선택되지 않고 컴파일러 오류가 발생한다.
* 이름이과 시그니처가 같은 멤버 메서드에 대해 둘 이상의 디폴트 구현이 있는 경우에는 인터페이스를 구현하는 하위 클래스에서 명시적으로 새로운 구현을 제공해야 한다.

### open, final, abstract 변경자: 기본적으로 final
* 자바의 클래스와 메서드는 기본적으로 상속에 열려있지만 코틀린의 클래스와 메서드는 기본적으로 final 이다.
* 어떤 클래스에 상속을 허용하려면 클래스 앞에 open 변경자를 붙여야 한다.
* 오버라이드를 허용하고 싶은 메서드나 프로퍼티의 앞에도 open 변경자를 붙여야 한다.
* 코틀린에서 abstract 로 선언한 추상 클래스는 인스턴스화할 수 없다. 
  * 즉, 추상 클래스에는 구현이 없는 추상 멤버가 있기 때문에 하위 클래스에서 그 추상 멤버를 오버라이드해야만 하는게 보통이다.
  * 따라서, 추상 멤버 앞에 open 변경자를 사용할 필요가 없다. 
* 인터페이스를 오버라이드를 하는 경우에는 기본적으로 열려 있다.
* 오버라이드로 구현한 메서드는 기본적으로 열려있다. final 키워드를 통해 오버라이드를 금지할 수 있다.

변경자|기능|설명
---|---|---
final|오버라이드할 수 없음|클래스 멤버의 기본 변경자
open|오버라이드할 수 있음|반드시 open을 명시해야 오버라이드할 수 있다.
abstract|반드시 오버라이드해야 함|추상 클래스의 멤버에만 이 변경자를 붙일 수 있다. 추상 멤버에는 구현이 있으면 안 된다.
override|상위 클래스나 상위 인스턴스의 멤버를 오버라이드하는 중|오버라이드하는 멤버는 기본적으로 열려있다. 하위 클래스의 오버라이드를 금지하려면 final을 명시해야 한다.

### 가시성 변경자: 기본적으로 공개
* 자바와 달리 코틀린의 기본 가시성은 public 이다.
* 패키지 전용 가시성에 대한 대안으로 코틀린에서는 internal 이라는 새로운 가시성 변경자를 도입했다.
* 자바에서는 패키지가 같은 클래스를 선언하기만 하면 어떤 프로젝트의 외부에 있는 코드라도 패키지 내부에 있는 패키지 전용 선언에 접근할 수 있다.
* 하지만, 코틀린의 internal 은 한 번에 한꺼번에 컴파일되는 코틀린 파일(모듈)들을 의미하기 때문에 진정한 캡슐화를 제공한다는 장점이 있다. (모듈 내부 가시성)
* 코틀린에서는 클래스, 함수,, 프로퍼티 등 최상위 선언에 대해 private 가시성을 허용한다.

변경자|클래스 선언|최상위 선언
---|---|---
public (기본 가시성)|모든 곳에서 볼 수 있다.|모든 곳에서 볼 수 있다.
internal|같은 모듈안에서만 볼 수 있다.|같은 모듈 안에서만 볼 수 있다.
protected|하위 클래스 안에서만 볼 수 있다.|최상위 선언에 적용할 수 없다.
private|같은 클래스 안에서만 볼 수 있다.|같은 파일 안에서만 볼 수 있다.

### 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스
* 자바와 다르게 코틀린의 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다.
  * 즉, 기본적으로 자바의 내부 클래스를 static 으로 선언한 것과 같다.

```kotlin
import java.io.Serializableinterface

interface State: Serializable

interface View {
  fun getCurrentState(): State
}
```

```java
public class Button implements View { 
  @Override
  public State getCurrentState() {
      return new ButtonState();
  }
  
  public class ButtonState implements State {
      /* ... */
  }
}
```

* 위 코드는 State 인터페이스를 구현한 ButtonState 클래스를 정의해서 Button 에 대한 구체적인 정보를 저장한다.
* 다음의 코드를 실제로 수행하면 "NotSerializableException"이 발생한다.
* 왜냐하면, 자바에서는 다른 클래스 안에 정의한 클래스는 자동으로 내부 클래스가 되므로, 바깥쪽 Button 클래스에 대한 참조를 묵시적으로 포함한다.
* 그 참조로 인해 ButtonState 를 직렬화할 수 없다.
* 이 문제를 해결하려면 ButtonState 를 static class 로 변경하여 묵시적인 참조를 사라지게 해야 한다.
* 코틀린에서는 중첩 클래스에 아무런 변경자가 붙지 않으면 자바 static 중첩 클래스와 같다.
* 이를 내부 클래스로 변경하고 싶다면, inner 변경자를 붙여서 사용하면 된다.
* 코틀린에서 내부 클래스 Inner 안에서 바깥쪽 클래스 Outer 의 참조에 접근하려면 ```this@Outer``` 라고 써야 한다.

클래스 B 안에 정의된 클래스 A|자바|코틀린
---|---|---
중첩 클래스(바깥쪽 클래스에 대한 참조를 지정하지 않음)|static class A|class A
내부 클래스(바깥쪽 클래스에 대한 참조를 저장함)|class A|inner class A

### 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한
```kotlin
interface Expression2
data class Num2(val value: Int): Expression2
data class Sum2(val left: Expression2, val right: Expression2): Expression2

fun eval(expression2: Expression2): Int =
    when (expression2) {
        is Num2 -> expression2.value
        is Sum2 -> eval(expression2.left) + eval(expression2.right)
        else -> throw IllegalArgumentException("unknown expression")
    }
```

```kotlin
sealed class Expression
data class Num(val value: Int): Expression()
data class Sum(val left: Expression, val right: Expression): Expression()

fun eval(expression: Expression): Int =
    when (expression) {
        is Num -> expression.value
        is Sum -> eval(expression.left) + eval(expression.right)
    }
```

* 위 처럼 상위 클래스에 sealed 를 사용하면 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다.
* sealed 클래스의 하위 클래스를 정의할 때는 반드시 상위 클래스 안에 중첩시켜야 한다. (같은 파일안에 존재해야한다.)
* sealed 클래스는 자동으로 open 클래스이다.
* 내부적으로 sealed 클래스는 private 생성자를 가진다.
  * 왜냐하면, 봉인된 인터페이스를 만들 수 있다면 그 인터페이스를 자바 쪽에서 구현하지 못하게 막을 수 있는 수단이 코틀린 컴파일러에는 없기 때문이다.
* sealed 클래스는 자기 자신이 추상 클래스이고, 자신이 상속받는 여러 클래스를 가질 수 있기 때문에 "enum" 클래스와 달리 상속을 활용한 풍부한 동작을 구현할 수 있다.

### 클래스 초기화: 주 생성자와 초기화 블록
* 아래 세개의 코드는 모두 다 같은 정의이다.
```kotlin
class User constructor(_name: String) {
    val name: String
    init {
        name = _name
    }
}

class User(_name: String) {
    val name = _name
}

class User(val name: String)
```

* 함수 파라미터와 마찬가지로 생성자 파라미터에도 디폴트 값을 정의할 수 있다.

```kotlin
class User(
  val name: String, 
  val isSucsribed: Boolean = false,
)
```

* 클래스에 기반 클래스가 있다면 주 생성자에서 기반 클래스의 생성자를 호출해야 할 필요가 있다. 기반 클래스를 초기화하려면 기반 클래스 이름 뒤에 괄호를 치고 생성자를 넘긴다.

```kotlin
open class User(val name: String)

class MainUser(name: String) : User(name)
```

* 별도로 생성자를 정의하지 않으면 컴파일러가 자동으로 디폴트 생성자를 만들어준다.
  * 따라서, 생성자를 정의하지 않은 클래스를 상속한 하위 클래스는 반드시 클래스의 생성자를 호출해야한다. 
  * 아래 코드는 예시다.
* 반면 인터페이스는 생성자가 없기 때문에 어떤 클래스가 인터페이스를 구현하는 경우 그 클래스의 상위 클래스 목록에 있는 인터페이스 이름 뒤에는 괄호가 없다.

```kotlin
open class Button
class RadioButton : Button()
```

* 어떤 클래스를 클래스 외부에서 인스턴스화하지 못하게 막고 싶다면 모든 생성자를 private 으로 만들면 된다.

```kotlin
class User private constructor()
```

### 부 생성자: 상위 클래스를 다른 방식으로 초기화
* 일반적으로 코틀린에서는 생성자가 여럿 있는 경우가 자바보다 훨씬 적다.
* 자바에서 오버로드한 생성자가 필요한 상황 중 상당수는 코틀린의 디폴트 파라미터 값과 이름 붙은 인자 문법을 사용해 해결할 수 있다.

```kotlin
open class Title(val titleNo: Int) {
    lateinit var subject: String
    lateinit var description: String

    constructor(titleNo: Int, subject: String) : this(titleNo) {
        this.subject = subject
    }

    constructor(titleNo: Int, subject: String, description: String) : this(titleNo, subject) {
        this.description = description
    }
}

class Episode : Title {
  constructor(titleNo: Int) : super(titleNo)
  constructor(titleNo: Int, subject: String) : super(titleNo, subject)
  constructor(titleNo: Int, subject: String, description: String) : super(titleNo, subject, description)
}
```

### 데이터 클래스와 불변성: copy() 메서드
* 데이터 클래스의 모든 프로퍼티를 읽기 전용으로 만들어서 데이터 클래스를 불변 클래스로 만드는 것이 좋다.
* HashMap 등의 컨테이너에 데이터 클래스 객체를 담는 경우엔 불변성이 필수적이다.
* 데이터 클래스 객체를 키로 하는 값을 컨테이너에 담은 다음에 키로 쓰인 데이터 객체의 프로퍼티를 변경하면 컨테이너 상태가 잘못될 수 있다.
* 불변 객체를 주로 사용하는 프로그램에서는 스레드가 사용 중인 데이터를 다른 스레드가 변경할 수 없으므로 스레드를 동기화해야 할 필요가 줄어든다.
* 데이터 클래스 인스턴스를 불변 객체로 더 쉽게 활용할 수 있게 코틀린 컴파일러는 한 가지 편의 메서드를 제공한다. : copy 메서드
* 객체를 메모리상에서 직접 바꾸는 대신 복사본을 만드는 편이 더 낫다. 
* 복사본은 원본과 다른 생명주기를 가지며, 복사를 하면서 일부 프로퍼티 값을 바꾸거나 복사본을 제거해도 프로그램에서 원본을 참조하는 다른 부분에 전혀 영향을 끼치지 않는다.

### by 키워드 사용
* delegate 패턴은 어떤 기능을 자신이 처리하지 않고, 다른 객체 위임 시켜 그 객체가 일을 처리하도록 만드는 것
* by 키워드를 사용하면 따로 코드를 작성하지 않아도 바로 위임시킬 수 있다.

```kotlin
// Delegate 패턴 X
class TrueBeauty : Webtoon {
    override fun getTitleNo(): Int {
        return 1
    }

    override fun getSubject(): String {
        return "여신강림"
    }
}

// Delegate 패턴 사용
class Kubera(
    private val webtoon: Webtoon,
) : Webtoon {
    override fun getTitleNo(): Int {
        return webtoon.getTitleNo()
    }

    override fun getSubject(): String {
        return webtoon.getSubject()
    }
}

// by 키워드 사용
class TowerOfGod(
    private val webtoon: Webtoon,
) : Webtoon by webtoon
```

### 객체 선언: 싱글턴 쉽게 만들기 
* 코틀린은 객체 선언 기능을 통해 싱글턴을 언어에서 기본 지원한다.
* 객체 선언은 ```object``` 키워드로 시작한다.
* 하지만, 생성자는 객체 선언에 쓸 수 없다.
* 일반 클래스 인스턴스와 달리 싱글턴 객체는 객체 선언문이 있는 위치에서 생성자 호출 없이 즉시 만들어지기 때문에 필요 없다.

```kotlin
object NaverWebtoon { 
  val allEmployees = listOf<Emplolyee>()
  
  fun calculateSalary() {
    TODO("Not yet implemented")
  }
}
```

### 동반 객체: 팩토리 메서드와 정적 멤버가 들어갈 장소
* 코틀린 클래스 안에는 정적 멤버가 없다.
* 코틀린 언어는 자바 static 키워드를 지원하지 않는다.
* 대신 코틀린에서는 패키지 수준의 ```최상위 함수```와 ```객체 선언```을 활용한다.
* 클래스 안에 정의된 객체 중 하나에 companion object 라는 키워드를 사용하면 클래스의 동반 객체를 만들 수 있다.

```kotlin
fun main() {
    Webtoon.trueBeauty()
}

class Webtoon {
    companion object {
        fun trueBeauty() {
            println("I am true beauty")
        }
    }
}
```
```shell
> I am true beauty
```

* 동반 객체는 자신을 둘러싼 클래스의 모든 private 멤버에 접근할 수 있다.
* 따라서 동반 객체는 팩토리 패턴을 구현하기 가장 적합한 위치다.

```kotlin
class Webtoon private constructor(
    id: Int,
    subject: String,
) {
    companion object {
        fun of(
            id: Int,
            subject: String,
        ): Webtoon {
            return Webtoon(
                id = id,
                subject = subject,
            )
        }
    }
}
```

* 위 처럼 정적 팩터리 메서드를 사용하기에 적합하다.
* 정적 팩터리 메서드의 장점은 effective kotlin 33장에서도 설명하고있다.


#### 동반 객체에서 인터페이스를 구현할 수 있다.
* 아래 코드를 보면 동반 객체에서 인터페이스를 구현했다.
* 추상 팩터리가 있다면 Novel 객체를 팩터리에게 넘길 수 있다.
* 즉, 동반 객체가 구현한 인터페이스를 넘길 때 Novel 클래스 이름을 사용했다는 점에 유의해야한다.

```kotlin
interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

class Novel private constructor(
    val subject: String
) {
   companion object : JSONFactory<Novel> {
       override fun fromJSON(jsonText: String): Novel {
           TODO("Not yet implemented")
       }
   }
}

fun <T> loadFromJSON(factory: JSONFactory<T>): T {
  TODO("Not yet implemented")
}

fun main() {
  loadFromJSON(Novel)
}
```

#### 동반 객체 확장
* 확장 함수를 사용하면 코드 기반의 다른 곳에서 정의된 클래스의 인스턴스에 대해 새로운 메서드를 정의할 수 있다.
* 동반 객체에 확장함수를 사용하려면 다음과 같다.
* 주의점은 동반 객체에 대한 확장함수를 사용하려면 원래 클레스에 동반 객체를 꼭 선언해야 한다는 점에 주의하면 된다.

```kotlin
class Comic(val name: String) {
    companion object
}

fun Comic.Companion.fromJSON(jsonText: String): Comic {
    TODO("Not yet implemented")
}
```

### 객체 식: 무명 내부 클래스를 다른 방식으로 작성
* object 키워드를 싱글턴과 같은 객체를 정의하고 그 객체에 이름을 붙일 때만 사용하지 않는다.
* 익명클래스를 정의할 때도 object 키워드를 쓴다.
* 예를 들어, 이벤트 리스너를 구현하면 다음과 같다.

```kotlinv
import java.awt.Window
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

fun countClicks(window: Window) {
    var clickCount = 0

    window.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent?) {
            clickCount++
        }
    })
}
```