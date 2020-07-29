package ro.cipcirip.data

import androidx.room.*
import ro.cipcirip.model.*

@Dao
abstract class OrganismInformationDao {
    @Query("select exists (select 1 from OrganismInformation where organismId = :organismId and informationId = :informationId)")
    abstract fun exists(organismId: Long, informationId: Long): Boolean

    @Insert
    abstract fun insert(organismInformation: OrganismInformation)

    @Update
    abstract fun update(organismInformation: OrganismInformation)
}
