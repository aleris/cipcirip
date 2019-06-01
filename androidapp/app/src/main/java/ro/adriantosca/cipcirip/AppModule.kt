package ro.adriantosca.cipcirip

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ro.adriantosca.cipcirip.data.AppDatabase
import ro.adriantosca.cipcirip.data.DataImporter
import ro.adriantosca.cipcirip.data.Migration1to2
import ro.adriantosca.cipcirip.data.OrganismRepository

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "cipcirip.db"
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    GlobalScope.launch(Dispatchers.IO) {
                        val dataImporter by inject<DataImporter>()
                        dataImporter.importOrganisms(1)
                    }
                }
            })
            .addMigrations(
                Migration1to2()
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().attributionDao() }
    single { get<AppDatabase>().organismDao() }
    single { get<AppDatabase>().organismAttributionDao() }
    single { get<AppDatabase>().placeDao() }
    single { get<AppDatabase>().placeAttributionDao() }
    single { OrganismRepository() }
}
