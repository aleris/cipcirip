package ro.adriantosca.cipcirip.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ro.adriantosca.cipcirip.model.Attribution
import ro.adriantosca.cipcirip.model.Language
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.model.OrganismAttribution

@Dao
abstract class AttributionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(attribution: Attribution): Long

    @Query("select exists (select 1 from Attribution where id = :id)")
    abstract fun exists(id: Long): Boolean
}