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
import ro.adriantosca.cipcirip.data.OrganismRepository

const val DATABASE_NAME = "cipcirip.db"

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    GlobalScope.launch(Dispatchers.IO) {
                        val dataImporter by inject<DataImporter>()
                        dataImporter.import(db, 1)
                    }
                }
            })
//            .addMigrations(
//                Migration1to2()
//            )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().attributionDao() }
    single { get<AppDatabase>().mediaDao() }
    single { get<AppDatabase>().organismDao() }
    single { get<AppDatabase>().organismMediaDao() }
    single { DataImporter(androidApplication()) }
    single { OrganismRepository() }
}
