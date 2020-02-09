package ro.adriantosca.cipcirip.data

import androidx.room.*
import ro.adriantosca.cipcirip.model.OrganismMedia

@Dao
abstract class OrganismMediaDao {
    @Query("select exists (select 1 from OrganismMedia where organismId = :organismId and mediaId = :mediaId)")
    abstract fun exists(organismId: Int, mediaId: Int): Boolean

    @Insert
    abstract fun insert(organismMedia: OrganismMedia)

    @Update
    abstract fun update(organismMedia: OrganismMedia)
}