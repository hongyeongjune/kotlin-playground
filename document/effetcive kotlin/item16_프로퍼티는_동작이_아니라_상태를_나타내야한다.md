### 프로퍼티는 동작이 아니라 상태를 나타내야한다.
* 코틀린의 프로퍼티는 사용자 정의 게터와 세터를 가질 수 있다.
* 코틀린에서는 ```field```라는 backing field 가 있다.
* backing field 는 프로퍼티의 값을 저장하기 위한 필드다. 
* 코틀린에서는 필드를 바로 선언할 수 없고 프로퍼티로 선언하면 아래의 경우에 자동으로 backing field 가 생긴다. 
  * 적어도 하나의 접근자가 기본으로 구현되는 접근자를 사용하는 경우
  * 커스텀 접근자가 field 키워드를 통해 backing field 를 참조하는 경우
* 아래 예시 코드처럼 kotlin 에서 backing field 를 사용하지 않고 setter 를 사용하면 무한재귀에 빠질 수 있다.
* val 을 사용해서 읽기 전용 프로퍼티를 만들 때는 field 가 만들어지지 않는다.

```kotlin
class Main {
    var firstName: String = ""
        set(value) {
            if (value.isNotBlank()) {
                field = value
            }
        }
    
    var secondName: String = ""
        set(value) {
            if (value.isNotBlank()) {
                this.secondName = value
            }
        }
}
```

```java
public final class Main {
   @NotNull
   private String firstName = "";
   @NotNull
   private String secondName = "";

   @NotNull
   public final String getFirstName() {
      return this.firstName;
   }

   public final void setFirstName(@NotNull String value) {
      Intrinsics.checkNotNullParameter(value, "value");
      CharSequence var2 = (CharSequence)value;
      if (!StringsKt.isBlank(var2)) {
         this.firstName = value;
      }

   }

   @NotNull
   public final String getSecondName() {
      return this.secondName;
   }

   public final void setSecondName(@NotNull String value) {
      Intrinsics.checkNotNullParameter(value, "value");
      CharSequence var2 = (CharSequence)value;
      if (!StringsKt.isBlank(var2)) {
          // 무한 재귀
         this.setSecondName(value);
      }

   }
}
```

* var 를 이용한 코틀린 프로퍼티는 게터와 세터를 정의할 수 있으며, 이를 파생 프로퍼티(derived property)라고 부른다.
* 이처럼 코틀린의 모든 프로퍼티는 디폴트로 캡슐화되어 있다.
* 프로퍼티를 통한 getter, setter 접근은 자바의 필드와 다를 수 있습다. 
* 예를 들어 자바 표준 라이브러리 Date 를 활용해 객체에 날짜를 저장하는 상황에 직렬화 문제 등으로 객체를 더 이상 이러한 타입으로 사용할 수 없게 되었다면, 코틑린은 데이터를 millis 라는 별도의 프로퍼티로 옮기고, 이를 활용해서 date 프로퍼티에 저장하지 않고 랩/언랩하도록 코드를 변경하기만 하면 된다.

```kotlin
class Main {
    var millis: Long = 0L
    
    var date: Date
        // 매번 Date 객체를 생성
        get() = Date(millis)
        set(value) {
          // 객체는 millis 필드만 가진다.
            millis = value.time
        }
}
```

```java
public final class Main {
   private long millis;

   public final long getMillis() {
      return this.millis;
   }

   public final void setMillis(long var1) {
      this.millis = var1;
   }

   @NotNull
   public final Date getDate() {
      return new Date(this.millis);
   }

   public final void setDate(@NotNull Date value) {
      Intrinsics.checkNotNullParameter(value, "value");
      this.millis = value.getTime();
   }
}
```

* 코틀린의 프로퍼티는 개념적으로 접근자(val 의 경우 getter, var 의 경우 getter 와 setter)를 나타낸다.
* 따라서 코틀린은 인터페이스에도 프로퍼티를 정의할 수 있다. (오버라이드도 가능하다.) + 프로퍼티 위임도 할 수 있다. (item21 에서 자세한 설명)
* 프로퍼티는 본질적으로 함수이므로, 확장 프로퍼티도 만들 수 있다.

```kotlin
// 확장 프로퍼티 예시
val String.lastChar: Char
    get() = get(this.length - 1)
```

* 코틀린 프로퍼티는 자바의 필드처럼 필드가 아니라 접근자를 나타낸다.
* 프로퍼티를 함수 대신 사용할 수 있지만, 그렇다고 완전 대체하는 것은 좋지 않다.
* 왜냐하면 프로퍼티를 함수 대신 사용한다면, 여러 가지 오해를 불러일으킬 수 있다.
* 원칙적으로 프로퍼티는 상태를 나타내거나 설정하기 위한 목적으로만 사용하는 것이 좋고, 다른 로직 등을 포함하지 않아야 한다.
* '이 프로퍼티를 함수로 정의할 경우, 접두사로 get 또는 set 을 붙일 것인가?' 만약 아니라면, 이를 프로퍼티로 만드는 것은 좋지 않다.

* 참고로 상태를 추출/설정할 때는 프로퍼티를 사용해야한다. 함수를 사용하지 않는 것이 좋다.

```kotlin
class User {
    private var name: String = ""
    // 사용 금지
    fun getName() = name
    // 사용 금지
    fun setName(name: String) {
        this.name = name
    }
}
```

### 프로퍼티 대신 함수로 사용하는 것이 좋은 경우
* 연산 비용이 높거나, 복잡도가 O(1) 보다 큰 경우
* 비즈니스 로직을 포함하는 경우
* 결정적이지 않은 경우 : 같은 동작을 연속적으로 두 번 했는데 다른 값이 나오면 함수를 사용하는 것이 좋다.
* 변환의 경우 : 변환은 관습적으로 Int.toDouble() 과 같은 변환 함수를 이루어진다.
* 게터에서 프로퍼티의 상태 변경이 일어나야 하는 경우