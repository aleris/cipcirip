package ro.adriantosca.cipcirip.data

import androidx.room.*
import ro.adriantosca.cipcirip.model.Language
import ro.adriantosca.cipcirip.model.Media
import ro.adriantosca.cipcirip.model.Organism
import androidx.lifecycle.LiveData



@Dao
abstract class OrganismDao {
    @Query("select exists (select 1 from Organism where id = :id)")
    abstract fun exists(id: Int): Boolean

    @Insert
    abstract fun insert(organism: Organism): Long

    @Update
    abstract fun update(organism: Organism)

    @Query("select Media.* from Media inner join OrganismMedia on Media.id = OrganismMedia.mediaId inner join Organism on OrganismMedia.organismId = Organism.id where Organism.id = :organismId and Media.type = :mediaType")
    abstract fun listMediaOfType(organismId: Long, mediaType: String): LiveData<List<Media>>

    @Query("select * from Organism where id = :id")
    abstract fun get(id: Long): LiveData<Organism>

    @Query("select Organism.* from Organism inner join OrganismPlace on Organism.id = OrganismPlace.organismId where OrganismPlace.placeId = :placeId order by :order")
    abstract fun listForPlace(placeId: Long, order: String = Organism.Contract.name(Language.Default)): LiveData<List<Organism>>

    @Query("select Organism.* from Organism inner join OrganismFTS on Organism.id = OrganismFTS.rowid where OrganismFTS.nameRom match :searchText order by :order")
    abstract fun find(searchText: String, order: String = Organism.Contract.name(Language.Default)): LiveData<List<Organism>>

    @Query("select Count(id) from Organism")
    abstract fun count(): Int

    @Query("select * from Organism order by nameRom")
    abstract fun all(): LiveData<List<Organism>>
}
