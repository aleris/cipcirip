package ro.adriantosca.cipcirip.data

import androidx.room.*
import ro.adriantosca.cipcirip.model.Attribution
import ro.adriantosca.cipcirip.model.Language
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.model.OrganismAttribution

@Dao
abstract class OrganismDao {
    @Query("select exists (select 1 from Organism where id = :id)")
    abstract fun exists(id: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(organism: Organism): Long

    @Update
    abstract fun update(organism: Organism)

    @Query("select Attribution.* from Attribution inner join OrganismAttribution on Attribution.id = OrganismAttribution.attributionId inner join Organism on OrganismAttribution.organismId = Organism.id where Organism.id = :organismId and OrganismAttribution.mediaCode = :mediaCode")
    abstract fun getAttribution(organismId: Long, mediaCode: String): Attribution

    @Query("select * from Organism where id = :id")
    abstract fun get(id: Long): Organism

    @Query("select Organism.* from Organism inner join OrganismPlace on Organism.id = OrganismPlace.organismId where OrganismPlace.placeId = :placeId order by :order")
    abstract fun listForPlace(placeId: Long, order: String = Organism.Contract.name(Language.Default)): List<Organism>

    @Query("select Organism.* from Organism inner join OrganismFTS4 on Organism.id = OrganismFTS4.docid where OrganismFTS4 match :searchText order by :order")
    abstract fun find(searchText: String, order: String): List<Organism>

    fun findWildcard(searchText: String, order: String = Organism.Contract.name(Language.Default)): List<Organism> = find("*$searchText*", order)
}
