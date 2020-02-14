package ro.adriantosca.cipcirip

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import ro.adriantosca.cipcirip.data.AppDatabase

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseImportTest: AutoCloseKoinTest() {
    private val db by inject<AppDatabase>()

    @Before
    fun before() {
        startKoin {
            androidLogger()
            val androidContext = ApplicationProvider.getApplicationContext<Context>()
            androidContext(androidContext)
//            AppDatabase.deleteDatabaseFile(androidContext, DATABASE_NAME)
            modules(appModule)
        }
        db.organismDao().all()
        Thread.sleep(3000)
    }

    @Test
    fun dataWasImported() {
        assertTrue(db.organismDao().all().value?.isNotEmpty() ?: false)
    }

    @Test
    fun findWildcard() {
        println("aaaaaa>>>>>>>>")
        println(db.organismDao().find("v*", "code").value)
        println(db.organismDao().all().value)
        assertTrue(db.organismDao().find("vrabia", "code").value.isNullOrEmpty())
    }

    @After
    fun after() {
        stopKoin()
    }
}
