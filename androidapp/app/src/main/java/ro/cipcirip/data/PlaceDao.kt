package ro.cipcirip.data

import androidx.room.*
import ro.cipcirip.geo.GeoHelper
import ro.cipcirip.model.Language
import ro.cipcirip.model.Media
import ro.cipcirip.model.Place

@Dao
abstract class PlaceDao {
    @Query("select exists (select 1 from Place where id = :id)")
    abstract fun exists(id: Long): Boolean

    @Insert
    abstract fun insert(place: Place): Long

    @Update
    abstract fun update(place: Place)

    @Query("select * from Place where id = :id")
    abstract fun get(id: Long): Place

    @Query("select * from Place order by name asc")
    abstract fun list(): List<Place>

    @Query("""
        select *
        from Place
        order by (latitude - :latitude) * (latitude - :latitude) + (longitude - :longitude) * (longitude - :longitude) * :fudge asc
        limit :max
        """)
    abstract fun listNearest(latitude: Double, longitude: Double, fudge: Double, max: Int): List<Place>

    fun listNearest(latitude: Double, longitude: Double): List<Place> {
        val fudge = GeoHelper.calculateFudge(latitude)
        return listNearest(latitude, longitude, fudge, 10)
    }

    @Query("select Place.* from Place inner join PlaceFTS4 on Place.id = PlaceFTS4.docid where PlaceFTS4 match :searchText order by name asc")
    abstract fun find(searchText: String): List<Place>

    fun findWildcard(searchText: String): List<Place> = find("*$searchText*")
}
