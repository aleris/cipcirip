package ro.adriantosca.cipcirip.data

import androidx.room.*
import ro.adriantosca.cipcirip.model.*

@Dao
abstract class PlaceAttributionDao {
    @Query("select exists (select 1 from PlaceAttribution where placeId = :placeId and attributionId = :attributionId)")
    abstract fun exists(placeId: Long, attributionId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(placeAttribution: PlaceAttribution): Long

    @Update
    abstract fun update(placeAttribution: PlaceAttribution)
}
