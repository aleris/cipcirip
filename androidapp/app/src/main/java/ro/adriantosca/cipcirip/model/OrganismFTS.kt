package ro.adriantosca.cipcirip.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.FtsOptions.TOKENIZER_UNICODE61

@Entity
@Fts4(contentEntity = Organism::class, tokenizer = TOKENIZER_UNICODE61)
data class OrganismFTS(
    @ColumnInfo(collate = ColumnInfo.UNICODE)
    var nameLat: String,
    @ColumnInfo(collate = ColumnInfo.UNICODE)
    var nameRom: String,
    @ColumnInfo(collate = ColumnInfo.UNICODE)
    var nameEng: String
)

