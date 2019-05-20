package ro.adriantosca.cipcirip

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String,
    var region: String,
    var latitude: Double,
    var longitude: Double
)
