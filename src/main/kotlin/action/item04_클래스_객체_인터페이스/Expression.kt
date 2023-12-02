package action.item04_클래스_객체_인터페이스

sealed class Expression

data class Num(val value: Int): Expression()
data class Sum(val left: Expression, val right: Expression): Expression()

fun eval(expression: Expression): Int =
    when (expression) {
        is Num -> expression.value
        is Sum -> eval(expression.left) + eval(expression.right)
    }

fun main() {
    val num = Num(1)
    println(eval(num))
}