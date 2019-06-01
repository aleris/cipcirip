package ro.adriantosca.cipcirip.data

import androidx.room.*
import ro.adriantosca.cipcirip.geo.GeoHelper
import ro.adriantosca.cipcirip.model.Attribution
import ro.adriantosca.cipcirip.model.Language
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.model.Place

@Dao
abstract class PlaceDao {
    @Query("select exists (select 1 from Place where id = :id)")
    abstract fun exists(id: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insert(place: Place): Long

    @Update
    abstract fun update(place: Place)

    @Query("select * from Place where id = :id")
    abstract fun get(id: Long): Place

    @Query("select * from Place order by :order asc")
    abstract fun list(order: String = Place.Contract.name(Language.Default)): List<Place>

    @Query("select * from Place order by (latitude - :latitude) * (latitude - :latitude) + (longitude - :longitude) * (longitude - :longitude) * :fudge asc limit :max")
    abstract fun listNearest(latitude: Double, longitude: Double, fudge: Double, max: Int): List<Place>

    fun listNearest(latitude: Double, longitude: Double): List<Place> {
        val fudge = GeoHelper.calculateFudge(latitude)
        return listNearest(latitude, longitude, fudge, 10)
    }

    @Query("select Attribution.* from Attribution inner join PlaceAttribution on Attribution.id = PlaceAttribution.attributionId inner join Place on PlaceAttribution.placeId = Place.id where Place.id = :placeId and PlaceAttribution.mediaCode = :mediaCode")
    abstract fun getAttribution(placeId: Long, mediaCode: String): Attribution

    @Query("select Place.* from Place inner join PlaceFTS4 on Place.id = PlaceFTS4.docid where PlaceFTS4 match :searchText order by :order asc")
    abstract fun find(searchText: String, order: String): List<Place>

    fun findWildcard(searchText: String, order: String = Place.Contract.name(Language.Default)): List<Place> = find("*$searchText*", order)
}