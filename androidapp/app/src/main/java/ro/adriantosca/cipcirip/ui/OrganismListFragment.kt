package ro.adriantosca.cipcirip.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.adriantosca.cipcirip.R
import ro.adriantosca.cipcirip.SingleSongPlayer
import ro.adriantosca.cipcirip.model.Organism

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [OrganismListFragment.OnOrganismListItemClick] interface.
 */
class OrganismListFragment : Fragment(), KoinComponent {

    private val singleSongPlayer by inject<SingleSongPlayer>()
    private lateinit var organismListViewModel: OrganismListViewModel

    private var columnCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        activity?.let {
            organismListViewModel = ViewModelProvider(it).get(OrganismListViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.organism_list_fragment, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val viewAdapter = OrganismRecyclerViewAdapter(
                singleSongPlayer,
                object: OnOrganismListItemClick {
                    override fun onListFragmentInteraction(organism: Organism) {
                        view.findNavController().navigate(
                            OrganismListFragmentDirections
                                .actionOrganismFragmentToOrganismDetailsFragment(organism.id)
                        )
                    }
                }
            )
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                organismListViewModel.getFilteredOrganisms().observe(viewLifecycleOwner, Observer { list ->
                    viewAdapter.update(list)
                    if (adapter != viewAdapter) {
                        adapter = viewAdapter
                    }
                })
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as AppCompatActivity?)?.supportActionBar?.title = resources.getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.action_search)?.isVisible = true

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if(searchView.isIconified) {
                    return true
                }
                organismListViewModel.setOrganismsFilterQuery(query)
                return true
            }
        })
    }

    interface OnOrganismListItemClick {
        fun onListFragmentInteraction(organism: Organism)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            OrganismListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
