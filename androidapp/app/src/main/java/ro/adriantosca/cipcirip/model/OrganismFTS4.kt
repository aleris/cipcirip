package ro.adriantosca.cipcirip.model

import androidx.room.Entity
import androidx.room.Fts4

@Entity
@Fts4(contentEntity = Organism::class)
data class OrganismFTS4(
    var nameLat: String,
    var nameRom: String,
    var nameEng: String,
    var descriptionRom: String,
    var descriptionEng: String
)

