package ro.cipcirip.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ro.cipcirip.model.Media

@Dao
abstract class MediaDao {
    @Insert
    abstract fun insert(media: Media): Long

    @Update
    abstract fun update(media: Media)

    @Query("select exists (select 1 from Media where id = :id)")
    abstract fun exists(id: Int): Boolean

    @Query("select * from Media")
    abstract fun all(): LiveData<List<Media>>
}