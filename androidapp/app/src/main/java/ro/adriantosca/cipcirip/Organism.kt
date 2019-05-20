package ro.adriantosca.cipcirip

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Organism(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var code: String,
    var nameRom: String,
    var nameEng: String,
    var nameLat: String,
    var regnum: String,
    var phylum: String,
    var classis: String,
    var ordo: String,
    var familia: String,
    var genus: String,
    var species: String,
    var description: String,
    var hasSound: Boolean,
    var soundAttribution: String,
    var hasImagePaint: Boolean,
    var imagePaintAttribution: String,
    var hasImageMale: Boolean,
    var imageMaleAttribution: String,
    var hasImageFemale: Boolean,
    var imageFemaleAttribution: String,
    var viewedTimestamp: Long
)
