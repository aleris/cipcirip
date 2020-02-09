package ro.adriantosca.cipcirip.data

import androidx.room.*
import ro.adriantosca.cipcirip.model.Media

@Dao
abstract class MediaDao {
    @Insert
    abstract fun insert(media: Media): Long

    @Update
    abstract fun update(media: Media)

    @Query("select exists (select 1 from Media where id = :id)")
    abstract fun exists(id: Int): Boolean
}