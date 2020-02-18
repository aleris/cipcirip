package ro.cipcirip

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject
import ro.cipcirip.data.AppDatabase
import ro.cipcirip.ui.OrganismListViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var organismListViewModel: OrganismListViewModel

    private val db by inject<AppDatabase>()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
//            R.id.navigation_home -> {
//                message.setText(R.string.title_home)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_dashboard -> {
//                message.setText(R.string.title_dashboard)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_notifications -> {
//                message.setText(R.string.title_notifications)
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        AppDatabase.deleteDatabase(this)

//        println("»»»»»»»»»»»»» findMediaWithAttributionWithMediaType:")
//        db.mediaDao().all().observeForever { println(it) }
//        db.organismMediaDao().all().observeForever { println(it) }
//        db.organismMediaDao().mediaByType(1, MediaType.Paint).observeForever { println(it) }
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        organismListViewModel = ViewModelProvider(this).get(OrganismListViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

}
