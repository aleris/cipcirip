package ro.cipcirip.model

import androidx.room.*

@Entity(indices = [Index(Organism.Contract.code)])
data class Organism (
    @PrimaryKey
    var id: Long,
    var code: String,
    var nameLat: String,
    var regnum: String,
    var phylum: String,
    var classis: String,
    var ordo: String,
    var familia: String,
    var genus: String,
    var species: String,
    var viewedTimestamp: Long
) {
    object Contract {
        const val id = "id"
        const val mediaPaintId = "mediaPaintId"
        const val code = "code"

        fun name(language: Language) = "name$language"
    }
}

@Entity
@Fts4(contentEntity = Organism::class, tokenizer = FtsOptions.TOKENIZER_UNICODE61)
data class OrganismFTS(
    @ColumnInfo(collate = ColumnInfo.UNICODE)
    var nameLat: String
)

data class OrganismCodeAndNameOnly(
    var id: Long,
    var code: String,
    var name: String
)

data class OrganismWithName(
    var id: Long,
    var code: String,
    var nameLat: String,
    var regnum: String,
    var phylum: String,
    var classis: String,
    var ordo: String,
    var familia: String,
    var genus: String,
    var species: String,
    var viewedTimestamp: Long,
    var name: String
)
