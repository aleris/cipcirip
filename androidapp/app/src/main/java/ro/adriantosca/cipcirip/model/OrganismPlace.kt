package ro.adriantosca.cipcirip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = [OrganismPlace.Contract.organismId, OrganismPlace.Contract.placeId],
    indices = [Index(OrganismPlace.Contract.placeId)],
    foreignKeys = [
        ForeignKey(
            entity = Organism::class,
            parentColumns = [Organism.Contract.id],
            childColumns = [OrganismPlace.Contract.organismId]
        ),
        ForeignKey(
            entity = Place::class,
            parentColumns = [Place.Contract.id],
            childColumns = [OrganismPlace.Contract.placeId]
        )
    ])
data class OrganismPlace(
    var organismId: Long,
    var placeId: Long
) {
    object Contract {
        const val id = "id"
        const val organismId = "organismId"
        const val placeId = "placeId"
    }
}
