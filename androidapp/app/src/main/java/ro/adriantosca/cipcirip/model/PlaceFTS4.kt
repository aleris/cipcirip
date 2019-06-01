package ro.adriantosca.cipcirip.model

import androidx.room.Entity
import androidx.room.Fts4

@Entity
@Fts4(contentEntity = Place::class)
data class PlaceFTS4(
    var nameRom: String,
    var nameEng: String,
    var regionRom: String,
    var regionEng: String,
    var descriptionRom: String,
    var descriptionEng: String
)
