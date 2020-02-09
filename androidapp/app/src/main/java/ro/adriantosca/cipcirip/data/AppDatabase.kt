package ro.adriantosca.cipcirip.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ro.adriantosca.cipcirip.model.*

@Database(
    entities = [
        Attribution::class,
        Media::class,
        Organism::class,
        OrganismFTS::class,
        OrganismMedia::class,
        Place::class,
        PlaceFTS4::class,
        PlaceMedia::class,
        OrganismPlace::class
    ],
    version = AppDatabase.VERSION
)
@TypeConverters(MediaTypeConverter::class, MediaPropertyConverter::class)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun attributionDao() : AttributionDao
    abstract fun mediaDao() : MediaDao
    abstract fun organismDao() : OrganismDao
    abstract fun organismMediaDao() : OrganismMediaDao
}
