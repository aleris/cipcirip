package ro.adriantosca.cipcirip

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class OrganismDao {
    @Query("select Organism.* from Organism inner join OrganismToPlace on Organism.id = OrganismToPlace.organismId where OrganismToPlace.placeId = :placeId")
    abstract fun getOrganismsForPlace(placeId: Long): List<Organism>

    @Query("select * from Organism where id = :organismId")
    abstract fun getOrganism(organismId: Long): Organism
}