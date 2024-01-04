package action.item04_클래스_객체_인터페이스.동반객체

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

class Comic(val name: String) {
    companion object
}

fun Comic.Companion.fromJSON(jsonText: String): Comic {
    TODO("Not yet implemented")
}