package action.item04_클래스_객체_인터페이스

open class User(val name: String)

class MainUser(name: String) : User(name) {

}

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

fun main() {
    val button = Button()
    button.showOff()
    MainUser("hyj")
}