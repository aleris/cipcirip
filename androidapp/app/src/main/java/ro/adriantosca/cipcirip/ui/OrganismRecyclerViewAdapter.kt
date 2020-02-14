package ro.adriantosca.cipcirip.ui

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_organism.view.*
import ro.adriantosca.cipcirip.R
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.ui.OrganismFragment.OnOrganismListFragmentInteractionListener

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnOrganismListFragmentInteractionListener].
 */
class OrganismRecyclerViewAdapter(
    private val mListenerOrganism: OnOrganismListFragmentInteractionListener?
) : RecyclerView.Adapter<OrganismRecyclerViewAdapter.ViewHolder>() {

    private val mValues = ArrayList<Organism>()
    private val mOnClickListener: View.OnClickListener
    private var onNothingFound: (() -> Unit)? = null

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Organism
            mListenerOrganism?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_organism, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mNameView.text = item.nameRom
        Glide
            .with(holder.mView.context)
            .load(Uri.parse("file:///android_asset/img/${item.code}.jpg"))
            .placeholder(ColorDrawable(ContextCompat.getColor(holder.mImageView.context, R.color.colorPlaceholder)))
            .into(holder.mImageView)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    fun update(list: List<Organism>) {
        this.mValues.clear()
        this.mValues.addAll(list)
        this.notifyDataSetChanged()

//        val diffCallback = RatingDiffCallback(ratings, newRating)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        ratings.clear()
//        ratings.addAll(rating)
//        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mImageView: ImageView = mView.image

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            private val filterResults = FilterResults()
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//
//                if (constraint.isNullOrBlank()) {
//                    searchableList.addAll(originalList)
//                } else {
//                    val searchResults = originalList.filter { it.getSearchCriteria().contains(constraint) }
//                    searchableList.addAll(searchResults)
//                }
//                return if (constraint.isNullOrBlank()) {
//                    filterResults.also {
//                        it.values =
//                    }
//                } else {
//
//                }
//            }
//
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                // no need to use "results" filtered list provided by this method.
//                if (searchableList.isNullOrEmpty()) {
//                    onNothingFound?.invoke()
//                }
//                notifyDataSetChanged()
//            }
//        }
//    }
}
