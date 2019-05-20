package ro.adriantosca.cipcirip

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "cipcirip.db"
        )
            .addMigrations(Migration1to2(), Migration2to3())
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().organismDao() }
    single { AppRepository(get()) }
}
