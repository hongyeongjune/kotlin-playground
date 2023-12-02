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
* selaed 클래스는 자기 자신이 추상 클래스이고, 자신이 상속받는 여러 클래스를 가질 수 있기 때문에 "enum" 클래스와 달리 상속을 활용한 풍부한 동작을 구현할 수 있다.


