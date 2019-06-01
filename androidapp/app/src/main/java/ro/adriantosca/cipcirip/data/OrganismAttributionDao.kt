package ro.adriantosca.cipcirip.data

import androidx.room.*
import ro.adriantosca.cipcirip.model.Attribution
import ro.adriantosca.cipcirip.model.Language
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.model.OrganismAttribution

@Dao
abstract class OrganismAttributionDao {
    @Query("select exists (select 1 from OrganismAttribution where organismId = :organismId and attributionId = :attributionId)")
    abstract fun exists(organismId: Long, attributionId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(organismAttribution: OrganismAttribution)

    @Update
    abstract fun update(organismAttribution: OrganismAttribution)
}