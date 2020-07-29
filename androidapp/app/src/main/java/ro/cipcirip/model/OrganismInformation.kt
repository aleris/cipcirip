package ro.cipcirip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = [OrganismInformation.Contract.organismId, OrganismInformation.Contract.informationId],
    indices = [Index(OrganismInformation.Contract.informationId)],
    foreignKeys = [
        ForeignKey(
            entity = Organism::class,
            parentColumns = [Organism.Contract.id],
            childColumns = [OrganismInformation.Contract.organismId]
        ),
        ForeignKey(
            entity = Information::class,
            parentColumns = [Information.Contract.id],
            childColumns = [OrganismInformation.Contract.informationId]
        )
    ])
data class OrganismInformation(
    var organismId: Long,
    var informationId: Long
) {
    object Contract {
        const val organismId = "organismId"
        const val informationId = "informationId"
    }
}
