package ro.cipcirip.data

import androidx.room.*
import androidx.lifecycle.LiveData
import ro.cipcirip.model.*


@Dao
abstract class InformationDao {
    @Query("select exists (select 1 from Information where id = :id)")
    abstract fun exists(id: Long): Boolean

    @Insert
    abstract fun insert(organism: Information): Long

    @Update
    abstract fun update(organism: Information)

    @Query("select * from Information order by name asc")
    abstract fun all(): LiveData<List<Information>>

    @Query("""
        select Information.*,
            Attribution.description as attributionDescription,
            Attribution.source as attributionSource
        from Information
            inner join OrganismInformation on Information.id = OrganismInformation.informationId
            inner join Attribution on Information.attributionId = Attribution.id
        where Information.language = :language and OrganismInformation.organismId = :organismId
        """)
    abstract fun getWithAttribution(organismId: Long, language: Language): LiveData<InformationWithAttribution?>
}
