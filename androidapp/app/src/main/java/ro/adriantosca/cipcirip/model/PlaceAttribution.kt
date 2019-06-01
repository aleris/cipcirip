package ro.adriantosca.cipcirip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.model.Place

@Entity(
    primaryKeys = [PlaceAttribution.Contract.placeId, PlaceAttribution.Contract.attributionId],
    indices = [Index(PlaceAttribution.Contract.attributionId)],
    foreignKeys = [
        ForeignKey(
            entity = Organism::class,
            parentColumns = [Organism.Contract.id],
            childColumns = [PlaceAttribution.Contract.placeId]
        ),
        ForeignKey(
            entity = Place::class,
            parentColumns = [Place.Contract.id],
            childColumns = [PlaceAttribution.Contract.attributionId]
        )
    ])
data class PlaceAttribution(
    var placeId: Long,
    var mediaCode: String,
    var attributionId: Long
) {
    object Contract {
        const val id = "id"
        const val placeId = "placeId"
        const val attributionId = "attributionId"
    }
}
