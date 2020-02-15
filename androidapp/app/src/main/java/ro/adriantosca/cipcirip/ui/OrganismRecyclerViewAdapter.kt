package ro.adriantosca.cipcirip.ui

import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
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
import java.io.FileNotFoundException

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

    private var currentMediaPlayer: MediaPlayer? = null
    private var currentHolder: ViewHolder? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mNameView.text = item.nameRom // "${item.code} ${item.nameRom}"
        Glide
            .with(holder.mView.context)
            .load(
                Uri.parse("file:///android_asset/media/paintings/${item.code}.jpg")
            )
            .placeholder(
                ColorDrawable(
                    ContextCompat.getColor(holder.mImageView.context, R.color.colorPlaceholder)
                )
            )
            .into(holder.mImageView)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }

//        holder.mPlayButton.visibility = item. View.INVISIBLE
        holder.mPlayButton.setOnClickListener {
            currentHolder?.also {
                it.mPlayButton.visibility = View.VISIBLE
                it.mStopButton.visibility = View.GONE
            }
            currentMediaPlayer?.also {
                if (it.isPlaying) {
                    it.stop()
                }
            }
            currentHolder = holder
            with(holder) {
                mPlayButton.visibility = View.INVISIBLE
                try {
                    val afd = mPlayButton.context.assets.openFd("media/songs/${item.code}.mp3")
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                    mediaPlayer.setOnCompletionListener {
                        currentHolder?.also {
                            it.mPlayButton.visibility = View.VISIBLE
                            it.mStopButton.visibility = View.GONE
                        }
                    }
                    mediaPlayer.setOnPreparedListener {
                        it.start()
                        mPlayButton.visibility = View.GONE
                        mStopButton.visibility = View.VISIBLE
                    }
                    mediaPlayer.prepareAsync()
                    currentMediaPlayer = mediaPlayer
                } catch (ex: FileNotFoundException) {
                    println(ex)
                }
            }
        }

        holder.mStopButton.setOnClickListener {
            with(holder) {
                mPlayButton.visibility = View.VISIBLE
                mStopButton.visibility = View.GONE
            }
            currentMediaPlayer?.also {
                if (it.isPlaying) {
                    it.stop()
                }
            }
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
        val mPlayButton: ImageView = mView.play
        val mStopButton: ImageView = mView.stop

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
