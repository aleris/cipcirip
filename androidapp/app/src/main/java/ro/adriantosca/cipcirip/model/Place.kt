package ro.adriantosca.cipcirip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey
    var id: Int,
    var nameRom: String,
    var nameEng: String,
    var regionRom: String,
    var regionEng: String,
    var descriptionRom: String,
    var descriptionEng: String,
    var latitude: Double,
    var longitude: Double,
    var radius: Double // in meters
) {
    object Contract {
        const val id = "id"

        fun name(language: Language) = "name$language"
    }
}
