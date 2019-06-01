package ro.adriantosca.cipcirip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Organism(
    @PrimaryKey(autoGenerate = true)
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
    var hasSound: Boolean,
    var hasPaint: Boolean,
    var hasPhotoMale: Boolean, // by convention, the first photo is male (even for plants)
    var hasPhotoFemale: Boolean,
    var viewedTimestamp: Long
) {
    object Contract {
        const val id = "id"

        fun name(language: Language) = "name$language"
    }

    object MediaCode {
        const val sound = "sound"
        const val paint = "paint"
        const val photoMale = "photoMale"
        const val photoFemale = "photoFemale"

        fun photoOther(order: Int) = "photoOther_$order"
    }
}
