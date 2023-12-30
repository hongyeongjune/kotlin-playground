package action.item04_클래스_객체_인터페이스.object_키워드_사용

fun main() {
    Webtoon.trueBeauty()
    val novel = Novel.of(
        subject = "광마회귀"
    )
    novel.copy()
}

class Webtoon {
    companion object {
        fun trueBeauty() {
            println("I am true beauty")
        }
    }
}

data class Novel private constructor(
    val novelNo: Int?,
    val subject: String,
    val description: String?,
) {
    companion object {
        fun of(
            novelNo: Int? = null,
            subject: String,
            description: String? = null,
        ): Novel {
            return Novel(
                novelNo = novelNo,
                subject = subject,
                description = description,
            )
        }
    }
}