package ro.adriantosca.cipcirip.data

import androidx.room.*
import ro.adriantosca.cipcirip.model.*

@Dao
abstract class PlaceMediaDao {
    @Query("select exists (select 1 from PlaceMedia where placeId = :placeId and mediaId = :mediaId)")
    abstract fun exists(placeId: Long, mediaId: Long): Boolean

    @Insert
    abstract fun insert(placeMedia: PlaceMedia): Long

    @Update
    abstract fun update(placeMedia: PlaceMedia)
}
