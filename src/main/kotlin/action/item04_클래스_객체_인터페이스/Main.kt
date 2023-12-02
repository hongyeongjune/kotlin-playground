package action.item04_클래스_객체_인터페이스

import java.io.Serializable

fun main() {
    val button = Button()
    button.showOff()
}

interface State: Serializable