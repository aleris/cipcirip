package ro.cipcirip.model

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity
data class Place(
    @PrimaryKey
    var id: Long,
    var name: String,
    var region: String,
    var latitude: Double,
    var longitude: Double,
    var radius: Double // in meters
) {
    object Contract {
        const val id = "id"
    }
}
