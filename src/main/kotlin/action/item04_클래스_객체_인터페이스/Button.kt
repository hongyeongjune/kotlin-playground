package action.item04_클래스_객체_인터페이스

interface Clickable {
    fun showOff() = println("I'm clickable")
}

interface Focusable {
    fun showOff() = println("I'm focusable")
}

open class Button : Clickable, Focusable {
    override fun showOff() {
        println("test")
    }

    open fun onClick() = println("I'm click")
}

open class InputBox : Button() {
    override fun showOff() {
        println("I'm inputBox")
    }

    override fun onClick() = println("I'm click by inputBox")
}

class CommonModal : InputBox() {
    override fun onClick() = println("I'm click by commonModal")
}