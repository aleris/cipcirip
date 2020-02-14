package ro.adriantosca.cipcirip

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.ui.OrganismFragment
import ro.adriantosca.cipcirip.ui.OrganismViewModel

class MainActivity : AppCompatActivity(), OrganismFragment.OnOrganismListFragmentInteractionListener {

    private lateinit var organismViewModel: OrganismViewModel

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


//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                println("Search $query")
            }
        }

        organismViewModel = ViewModelProvider(this).get(OrganismViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if(searchView.isIconified) {
                    return true
                }
                organismViewModel.setOrganismsFilterQuery(query)
                return true
            }
        })

        return true
    }

    override fun onListFragmentInteraction(item: Organism?) {
        println("Interaction $item")
    }

//    override fun onBackPressed() { // close search view on back button pressed
//        if (!searchView.isIconified()) {
//            searchView.setIconified(true)
//            return
//        }
//        super.onBackPressed()
//    }
}
