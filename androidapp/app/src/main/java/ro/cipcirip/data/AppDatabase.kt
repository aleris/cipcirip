package ro.cipcirip.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ro.cipcirip.model.*
import java.io.File

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
        private const val DATABASE_NAME = "cipcirip.db"
        const val VERSION = 1

        fun buildDatabase(context: Context, dataImporter: DataImporter): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        GlobalScope.launch(Dispatchers.IO) {
                            dataImporter.import(db, 1)
                        }
                    }
                })
//                .addMigrations(
//                    Migration1to2()
//                )
                .fallbackToDestructiveMigration()
                .build()
        }

        fun deleteDatabase(context: Context) {
            val databases = File(context.applicationInfo.dataDir + "/databases")
            val db = File(databases, DATABASE_NAME)
            if (db.delete())
                println("Database $db deleted")
            else
                println("Failed to delete $db database")

            val journal = File(databases, "$DATABASE_NAME-journal")
            if (journal.exists()) {
                if (journal.delete())
                    println("Database journal $journal deleted")
                else
                    println("Failed to delete $journal database journal")
            }
        }
    }

    abstract fun attributionDao() : AttributionDao
    abstract fun mediaDao() : MediaDao
    abstract fun organismDao() : OrganismDao
    abstract fun organismMediaDao() : OrganismMediaDao
}
