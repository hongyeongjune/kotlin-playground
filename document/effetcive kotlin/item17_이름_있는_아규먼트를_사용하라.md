### 이름 있는 아규먼트를 사용하라
> https://kotlinlang.org/docs/coding-conventions.html#named-arguments  

> Use the named argument syntax when a method takes multiple parameters of the same primitive type, or for parameters of Boolean type, unless the meaning of all parameters is absolutely clear from context.

> 메서드가 동일한 primitive type 의 파라미터를 여러 개 받는 경우 또는 모든 파라미터의 의미가 문맥에서 명확하지 않은 경우, boolean type 의 매개변수에 대해서는 Named Argument 문법을 사용합니다.


* 파라미터가 명확하지 않은 경우에는 이를 직접 지정해서 명확하게 만들어 줄 수 있다. -> Named Argument 사용
* Named Argument 를 사용하면, 이름을 기반으로 값이 무엇을 나타내는지 알 수 있고, 파라미터 입력 순서와 상관없으므로 안전하다.

```kotlin
val text = (1..10).joinToString(
    separator = "|"
)
```

### 디폴트 아규먼트의 경우
* 프로퍼티가 디폴트 아규먼트를 가질 경우, 항상 Named Argument 를 붙여서 사용하는 것이 좋다.
* 일반적으로 함수 이름은 필수 파라미터들과 관련이 있기 때문에 디폴트 값을 갖는 옵션 파라미터의 설명이 명확하지 않으므로 사용하는 것이 좋다.

### 같은 타입의 파라미터가 많은 경우
* 파라미터가 모두 같은 타입이라면, 위치를 잘못 입력하면 오류가 발생할 수 있으므로 사용하는 것이 좋다.

### 함수 타입 파라미터
* 모든 함수 타입 아규먼트는 Named Argument 를 사용하는 것이 좋다.