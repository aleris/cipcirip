package ro.adriantosca.cipcirip.datagather.csv

data class Information(
    var id: Int = 0,
    var organismId: Int,
    var language: Language,
    var name: String,
    var description: String,
    var externalLink: String,
    var attributionId: Int
)
