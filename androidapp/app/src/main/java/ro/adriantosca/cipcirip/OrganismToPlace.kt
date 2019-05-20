package ro.adriantosca.cipcirip

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = [ "organismId", "placeId" ],
    indices = [Index("placeId")],
    foreignKeys = [
        ForeignKey(
            entity = Organism::class,
            parentColumns = ["id"],
            childColumns = ["organismId"]
        ),
        ForeignKey(
            entity = Place::class,
            parentColumns = ["id"],
            childColumns = ["placeId"]
        )
    ])
data class OrganismToPlace(
    var organismId: Long,
    var placeId: Long
)
