package ro.adriantosca.cipcirip.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import ro.adriantosca.cipcirip.R
import ro.adriantosca.cipcirip.model.Organism

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [OrganismFragment.OnOrganismListFragmentInteractionListener] interface.
 */
class OrganismFragment : Fragment(), KoinComponent {

    private lateinit var organismViewModel: OrganismViewModel

    // TODO: Customize parameters
    private var columnCount = 2

    private var listenerOrganism: OnOrganismListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        activity?.let {
            organismViewModel = ViewModelProvider(it).get(OrganismViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_organism_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = OrganismRecyclerViewAdapter(listenerOrganism)
                organismViewModel.getFilteredOrganisms().observe(viewLifecycleOwner, Observer { list ->
                    (adapter as OrganismRecyclerViewAdapter).update(list)
                })
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnOrganismListFragmentInteractionListener) {
            listenerOrganism = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerOrganism = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnOrganismListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: Organism?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            OrganismFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
