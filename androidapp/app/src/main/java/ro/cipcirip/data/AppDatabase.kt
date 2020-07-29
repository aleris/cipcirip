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
import mu.KotlinLogging
import ro.cipcirip.model.*
import java.io.File

private val logger = KotlinLogging.logger {}

@Database(
    entities = [
        Attribution::class,
        Media::class,
        Organism::class,
        OrganismFTS::class,
        OrganismInformation::class,
        Information::class,
        InformationFTS::class,
        OrganismMedia::class,
        Place::class,
        PlaceMedia::class,
        OrganismPlace::class
    ],
    version = AppDatabase.VERSION
)
@TypeConverters(MediaTypeConverter::class, MediaPropertyConverter::class, LanguageConverter::class)
abstract class AppDatabase: RoomDatabase() {
    companion object {
        private const val DATABASE_NAME = "cipcirip.db"
        const val VERSION = 1

        fun buildDatabase(context: Context, dataImporter: DataImporter): AppDatabase {
            logger.info { "Building database, data directory: ${context.applicationContext.applicationInfo.dataDir}" }

            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        logger.info { "Database created at ${db.path}" }

                        GlobalScope.launch(Dispatchers.IO) {
                            dataImporter.import(db, 1)
                        }
                    }
                })
//                .addMigrations(
//                    Migration1to2()
//                )
                .build()
        }

        fun deleteDatabase(context: Context) {
            val databases = File(context.applicationInfo.dataDir + "/databases")
            val db = File(databases, DATABASE_NAME)
            if (db.exists()) {
                if (db.delete()) {
                    logger.info {  "Database $db deleted" }
                } else {
                    logger.info { "Failed to delete $db database" }
                }
            } else {
                logger.info { "Database file $db does not exists" }
            }

            val journal = File(databases, "$DATABASE_NAME-journal")
            if (journal.exists()) {
                if (journal.delete()) {
                    logger.info { "Database journal $journal deleted" }
                } else {
                    logger.info { "Failed to delete $journal database journal" }
                }
            } else {
                logger.info { "Database journal file $journal does not exists" }
            }
        }
    }

    abstract fun attributionDao() : AttributionDao
    abstract fun mediaDao() : MediaDao
    abstract fun organismDao() : OrganismDao
    abstract fun informationDao() : InformationDao
    abstract fun organismInformationDao() : OrganismInformationDao
    abstract fun organismMediaDao() : OrganismMediaDao
}
