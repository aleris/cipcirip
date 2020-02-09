package ro.adriantosca.cipcirip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.ui.OrganismFragment

class MainActivity : AppCompatActivity(), OrganismFragment.OnOrganismListFragmentInteractionListener {

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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun onListFragmentInteraction(item: Organism?) {
        println("Interaction $item")
    }
}
