package ro.adriantosca.cipcirip

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class PlaceDao {
    @Query("select * from Place order by name asc")
    abstract fun listPlacesAlphabetical(): List<Place>

    @Query("select * from Place order by (latitude - :latitude) * (latitude - :latitude) + (longitude - :longitude) * (longitude - :longitude) * :fudge asc")
    abstract fun listPlacesNearest(latitude: Double, longitude: Double, fudge: Double): List<Place>

    fun listPlacesNearest(latitude: Double, longitude: Double): List<Place> {
        val fudge = Math.pow(Math.cos(Math.toRadians(latitude)), 2.0)
        return listPlacesNearest(latitude, longitude, fudge)
    }

    @Query("select * from Place where id = :placeId")
    abstract fun getPlace(placeId: Long): Place
}