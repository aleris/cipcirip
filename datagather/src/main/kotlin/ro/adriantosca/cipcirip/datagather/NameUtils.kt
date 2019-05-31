package ro.adriantosca.cipcirip.datagather

object NameUtils {
    fun queryName(text: String) = text.replace(" ", "+")

    fun firstNameLat(birdInfo: BirdInfo): String {
        val nameLat = birdInfo.nameLat
        if (nameLat.contains('(')) {
            return nameLat.substringBefore('(')
        }
        return nameLat
    }

    fun secondNameLat(birdInfo: BirdInfo): String? {
        val nameLat = birdInfo.nameLat
        if (nameLat.contains('(')) {
            return nameLat.substringAfter('(').substringBefore(')')
        }
        return null
    }
}
