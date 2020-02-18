package ro.cipcirip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = [OrganismMedia.Contract.organismId, OrganismMedia.Contract.mediaId],
    indices = [Index(OrganismMedia.Contract.mediaId)],
    foreignKeys = [
        ForeignKey(
            entity = Organism::class,
            parentColumns = [Organism.Contract.id],
            childColumns = [OrganismMedia.Contract.organismId]
        ),
        ForeignKey(
            entity = Media::class,
            parentColumns = [Media.Contract.id],
            childColumns = [OrganismMedia.Contract.mediaId]
        )
    ])
data class OrganismMedia(
    var organismId: Int,
    var mediaId: Int
) {
    object Contract {
        const val organismId = "organismId"
        const val mediaId = "mediaId"
    }
}
