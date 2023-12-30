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