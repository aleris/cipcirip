package ro.adriantosca.cipcirip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = [PlaceMedia.Contract.placeId, PlaceMedia.Contract.mediaId],
    indices = [Index(PlaceMedia.Contract.mediaId)],
    foreignKeys = [
        ForeignKey(
            entity = Place::class,
            parentColumns = [Place.Contract.id],
            childColumns = [PlaceMedia.Contract.placeId]
        ),
        ForeignKey(
            entity = Media::class,
            parentColumns = [Media.Contract.id],
            childColumns = [PlaceMedia.Contract.mediaId]
        )
    ])
data class PlaceMedia(
    var placeId: Int,
    var mediaId: Int
) {
    object Contract {
        const val id = "id"
        const val placeId = "placeId"
        const val mediaId = "mediaId"
    }
}
