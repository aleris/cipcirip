package ro.adriantosca.cipcirip.datagather.csv

data class Media(
    var id: Int = 0,
    var type: MediaType,
    var property: MediaProperty,
    var externalLink: String?,
    var attributionId: Int
)

enum class MediaType {
    Sound,
    Paint,
    Photo,
    Information
}

enum class MediaProperty {
    None,
    Male,
    Female
}
