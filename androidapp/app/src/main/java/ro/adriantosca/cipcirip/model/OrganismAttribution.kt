package ro.adriantosca.cipcirip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.model.Place

@Entity(
    primaryKeys = [OrganismAttribution.Contract.organismId, OrganismAttribution.Contract.attributionId],
    indices = [Index(OrganismAttribution.Contract.attributionId)],
    foreignKeys = [
        ForeignKey(
            entity = Organism::class,
            parentColumns = [Organism.Contract.id],
            childColumns = [OrganismAttribution.Contract.organismId]
        ),
        ForeignKey(
            entity = Place::class,
            parentColumns = [Place.Contract.id],
            childColumns = [OrganismAttribution.Contract.attributionId]
        )
    ])
data class OrganismAttribution(
    var organismId: Long,
    var mediaCode: String,
    var attributionId: Long
) {
    object Contract {
        const val id = "id"
        const val organismId = "organismId"
        const val attributionId = "attributionId"
    }
}
