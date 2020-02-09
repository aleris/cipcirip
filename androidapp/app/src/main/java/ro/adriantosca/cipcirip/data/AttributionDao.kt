package ro.adriantosca.cipcirip.data

import androidx.room.*
import ro.adriantosca.cipcirip.model.Attribution

@Dao
abstract class AttributionDao {
    @Insert
    abstract fun insert(attribution: Attribution): Long

    @Update
    abstract fun update(attribution: Attribution)

    @Query("select exists (select 1 from Attribution where id = :id)")
    abstract fun exists(id: Int): Boolean
}