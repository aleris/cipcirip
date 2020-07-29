package ro.cipcirip.data

import androidx.room.*
import ro.cipcirip.model.*

@Dao
abstract class PlaceMediaDao {
    @Query("select exists (select 1 from PlaceMedia where placeId = :placeId and mediaId = :mediaId)")
    abstract fun exists(placeId: Long, mediaId: Long): Boolean

    @Insert
    abstract fun insert(placeMedia: PlaceMedia)

    @Update
    abstract fun update(placeMedia: PlaceMedia)
}
