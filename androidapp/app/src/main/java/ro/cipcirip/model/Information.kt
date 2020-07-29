package ro.cipcirip.model

import androidx.room.*

@Entity(
    indices = [Index(Information.Contract.attributionId)],
    foreignKeys = [
        ForeignKey(
            entity = Attribution::class,
            parentColumns = [Attribution.Contract.id],
            childColumns = [Information.Contract.attributionId]
        )
    ])
data class Information(
    @PrimaryKey
    var id: Long,
    var organismId: Long,
    var language: Language,
    @ColumnInfo(collate = ColumnInfo.UNICODE)
    var name: String,
    @ColumnInfo(collate = ColumnInfo.UNICODE)
    var description: String,
    var externalLink: String?,
    var attributionId: Long
) {
    object Contract {
        const val id = "id"
        const val attributionId = "attributionId"
    }
}

@Entity
@Fts4(contentEntity = Information::class, tokenizer = FtsOptions.TOKENIZER_UNICODE61)
data class InformationFTS(
    @ColumnInfo(collate = ColumnInfo.UNICODE)
    var name: String
)

data class InformationWithAttribution(
    var id: Long,
    var organismId: Long,
    var language: Language,
    var name: String,
    var description: String,
    var externalLink: String?,
    var attributionId: Long,
    var attributionDescription: String,
    var attributionSource: String
)
