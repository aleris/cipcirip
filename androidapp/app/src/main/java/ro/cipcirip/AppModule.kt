package ro.cipcirip

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ro.cipcirip.data.AppDatabase
import ro.cipcirip.data.DataImporter
import ro.cipcirip.data.OrganismRepository

val appModule = module {
    single { DataImporter(androidApplication()) }
    single {
//        AppDatabase.deleteDatabase(androidApplication())
        AppDatabase.buildDatabase(androidApplication(), get())
    }
    single { get<AppDatabase>().attributionDao() }
    single { get<AppDatabase>().mediaDao() }
    single { get<AppDatabase>().organismDao() }
    single { get<AppDatabase>().organismMediaDao() }
    single { OrganismRepository() }
    single { SingleSongPlayer(androidApplication())}
}
