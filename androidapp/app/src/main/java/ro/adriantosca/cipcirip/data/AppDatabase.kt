package ro.adriantosca.cipcirip.data

import androidx.room.Database
import androidx.room.RoomDatabase
import ro.adriantosca.cipcirip.model.*

@Database(
    entities = [
        Organism::class,
        OrganismFTS4::class,
        Place::class,
        PlaceFTS4::class,
        OrganismPlace::class,
        Attribution::class,
        OrganismAttribution::class,
        PlaceAttribution::class
    ],
    version = AppDatabase.VERSION
)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun attributionDao() : AttributionDao
    abstract fun organismDao() : OrganismDao
    abstract fun organismAttributionDao() : OrganismAttributionDao
    abstract fun placeDao() : PlaceDao
    abstract fun placeAttributionDao() : PlaceAttributionDao
}
