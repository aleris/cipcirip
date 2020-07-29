package ro.cipcirip.data

import androidx.room.*
import androidx.lifecycle.LiveData
import ro.cipcirip.model.*


@Dao
abstract class OrganismDao {
    @Query("select exists (select 1 from Organism where id = :id)")
    abstract fun exists(id: Long): Boolean

    @Insert
    abstract fun insert(organism: Organism): Long

    @Update
    abstract fun update(organism: Organism)

    @Query("""
        select Media.*
        from Media 
            inner join OrganismMedia on Media.id = OrganismMedia.mediaId 
            inner join Organism on OrganismMedia.organismId = Organism.id
        where Organism.id = :organismId and Media.type = :mediaType
        """)
    abstract fun listMediaOfType(organismId: Long, mediaType: String): LiveData<List<Media>>

    @Query("""select Organism.* from Organism where Organism.id = :id""")
    abstract fun get(id: Long): LiveData<Organism>

//    @Query("select Organism.* from Organism inner join OrganismPlace on Organism.id = OrganismPlace.organismId where OrganismPlace.placeId = :placeId order by :order")
//    abstract fun listForPlace(placeId: Long, order: String = Organism.Contract.name(Language.Default)): LiveData<List<Organism>>

    @Query("""
        select Organism.id, Organism.code, Information.name
        from Organism
            inner join OrganismInformation on Organism.id = OrganismInformation.organismId
            inner join Information on OrganismInformation.informationId = Information.id
        where
            (Information.language = :language)
            and (
                Organism.id in (select rowid from OrganismFTS where (OrganismFTS match :searchText))
                or Information.id in (select rowid from InformationFTS where (InformationFTS match :searchText))
            ) 
        order by Information.name
        """)
    abstract fun find(searchText: String, language: Language): LiveData<List<OrganismCodeAndNameOnly>>

    @Query("select Count(id) from Organism")
    abstract fun count(): Long

    @Query("""
        select Organism.id, Organism.code, Information.name
        from Organism 
            inner join Information on Organism.id = Information.organismId
            inner join InformationFTS on Information.id = InformationFTS.rowid
        where Information.language = :language
        order by Information.name
        """)
    abstract fun all(language: Language): LiveData<List<OrganismCodeAndNameOnly>>
}
