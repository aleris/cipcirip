package ro.adriantosca.cipcirip.datagather.csv

data class Media(
    var id: Long = 0,
    var type: MediaType,
    var property: MediaProperty,
    var externalLink: String?,
    var attributionId: Long
)

enum class MediaType {
    Sound,
    Paint,
    Photo,
    Text
}

enum class MediaProperty {
    None,
    Male,
    Female
}
