package action.item04_클래스_객체_인터페이스

interface Expression2

data class Num2(val value: Int): Expression2
data class Sum2(val left: Expression2, val right: Expression2): Expression2

fun eval(expression2: Expression2): Int =
    when (expression2) {
        is Num2 -> expression2.value
        is Sum2 -> eval(expression2.left) + eval(expression2.right)
        else -> throw IllegalArgumentException("unknown expression")
    }
