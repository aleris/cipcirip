package ro.adriantosca.cipcirip

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Organism::class, Place::class, OrganismToPlace::class],
    version = AppDatabase.VERSION
)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun organismDao() : OrganismDao
}
