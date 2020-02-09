package ro.adriantosca.cipcirip.datagather.csv

data class Organism(
    var id: Long = 0,
    var code: String,
    var nameLat: String,
    var nameRom: String,
    var nameEng: String,
    var regnum: String,
    var phylum: String,
    var classis: String,
    var ordo: String,
    var familia: String,
    var genus: String,
    var species: String,
    var descriptionRom: String,
    var descriptionEng: String,
    var viewedTimestamp: Long
)
