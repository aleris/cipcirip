package ro.cipcirip

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CipCiripApplication: MultiDexApplication() {
    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@CipCiripApplication)
            modules(appModule)
        }
    }
}