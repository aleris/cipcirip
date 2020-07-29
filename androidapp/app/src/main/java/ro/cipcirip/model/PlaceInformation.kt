package ro.cipcirip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = [PlaceInformation.Contract.placeId, PlaceInformation.Contract.informationId],
    indices = [Index(PlaceInformation.Contract.informationId)],
    foreignKeys = [
        ForeignKey(
            entity = Place::class,
            parentColumns = [Place.Contract.id],
            childColumns = [PlaceInformation.Contract.placeId]
        ),
        ForeignKey(
            entity = Information::class,
            parentColumns = [Information.Contract.id],
            childColumns = [PlaceInformation.Contract.informationId]
        )
    ])
data class PlaceInformation(
    var placeId: Long,
    var informationId: Long
) {
    object Contract {
        const val placeId = "placeId"
        const val informationId = "informationId"
    }
}
