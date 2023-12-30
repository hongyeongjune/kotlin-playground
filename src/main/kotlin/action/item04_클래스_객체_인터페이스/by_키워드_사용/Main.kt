package action.item04_클래스_객체_인터페이스.by_키워드_사용

interface Webtoon {
    fun getTitleNo(): Int
    fun getSubject(): String
}

// Delegate 패턴 X
class TrueBeauty : Webtoon {
    override fun getTitleNo(): Int {
        return 1
    }

    override fun getSubject(): String {
        return "여신강림"
    }
}

// Delegate 패턴 사용
class Kubera(
    private val webtoon: Webtoon,
) : Webtoon {
    override fun getTitleNo(): Int {
        return webtoon.getTitleNo()
    }

    override fun getSubject(): String {
        return webtoon.getSubject()
    }
}

// by 키워드 사용
class TowerOfGod(
    private val webtoon: Webtoon,
) : Webtoon by webtoon