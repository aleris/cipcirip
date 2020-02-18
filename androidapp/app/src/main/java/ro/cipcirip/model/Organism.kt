package ro.cipcirip.model

import androidx.room.*

@Entity(indices = [Index(Organism.Contract.code)])
data class Organism(
    @PrimaryKey
    var id: Int,
    var code: String,
    var nameLat: String,
    @ColumnInfo(collate = ColumnInfo.UNICODE)
    var nameRom: String,
    var nameEng: String,
    var regnum: String,
    var phylum: String,
    var classis: String,
    var ordo: String,
    var familia: String,
    var genus: String,
    var species: String,
    @ColumnInfo(collate = ColumnInfo.UNICODE)
    var descriptionRom: String,
    var descriptionEng: String,
    var viewedTimestamp: Long
) {
    object Contract {
        const val id = "id"
        const val mediaPaintId = "mediaPaintId"
        const val code = "code"

        fun name(language: Language) = "name$language"
    }
}
