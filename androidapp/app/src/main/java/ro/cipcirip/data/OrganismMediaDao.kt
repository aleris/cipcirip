package ro.cipcirip.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.cipcirip.model.*

@Dao
abstract class OrganismMediaDao {
    @Query("select exists (select 1 from OrganismMedia where organismId = :organismId and mediaId = :mediaId)")
    abstract fun exists(organismId: Int, mediaId: Int): Boolean

    @Insert
    abstract fun insert(organismMedia: OrganismMedia)

    @Update
    abstract fun update(organismMedia: OrganismMedia)

    @Query("select Media.id, Media.type, Media.property, Media.isLocal, Media.externalLink, Media.attributionId, Attribution.description, Attribution.source from OrganismMedia inner join  Media on OrganismMedia.mediaId = Media.id inner join Attribution on Media.attributionId = Attribution.id where OrganismMedia.organismId = :organismId and Media.type = :mediaType")
    abstract fun findMediaWithAttributionWithMediaType(organismId: Int, mediaType: MediaType): LiveData<List<MediaWithAttribution>>

    @Query("select * from OrganismMedia")
    abstract fun all(): LiveData<List<OrganismMedia>>

}