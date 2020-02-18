package ro.cipcirip.ui

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
import kotlinx.android.synthetic.main.organism_list_item_fragment.view.*
import ro.cipcirip.R
import ro.cipcirip.SingleSongPlayer
import ro.cipcirip.model.Organism
import ro.cipcirip.ui.OrganismListFragment.OnOrganismListItemClick

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnOrganismListItemClick].
 */
class OrganismRecyclerViewAdapter(
    private val mSingleSongPlayer: SingleSongPlayer,
    private val mOrganismItemOnListItemClick: OnOrganismListItemClick?
) : RecyclerView.Adapter<OrganismRecyclerViewAdapter.ViewHolder>() {

    private val mValues = ArrayList<Organism>()
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Organism
            mOrganismItemOnListItemClick?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.organism_list_item_fragment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val organism = mValues[position]

        showAsPlaying(holder, organism == mSingleSongPlayer.currentPlayingOrganism)

        holder.mNameView.text = organism.nameRom // "${item.code} ${item.nameRom}"
        Glide
            .with(holder.mView.context)
            .load(
                Uri.parse("file:///android_asset/media/paintings/${organism.code}.jpg")
            )
            .placeholder(
                ColorDrawable(
                    ContextCompat.getColor(holder.mImageView.context, R.color.placeholderColor)
                )
            )
            .into(holder.mImageView)

        with(holder.mView) {
            tag = organism
            setOnClickListener(mOnClickListener)
        }

//        holder.mPlayButton.visibility = item. View.INVISIBLE
        holder.mPlayButton.setOnClickListener {
            holder.mPlayButton.visibility = View.INVISIBLE
            mSingleSongPlayer.play(
                organism,
                {
                    showAsPlaying(holder, true)
                },
                {
                    showAsPlaying(holder, false)
                }
            )
        }

        holder.mStopButton.setOnClickListener {
            mSingleSongPlayer.stop()
            showAsPlaying(holder, false)
        }
    }

    private fun showAsPlaying(holder: ViewHolder, playing: Boolean) {
        with (holder) {
            mPlayButton.visibility = if (playing) View.GONE else View.VISIBLE
            mStopButton.visibility = if (playing) View.VISIBLE else View.GONE
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
}
