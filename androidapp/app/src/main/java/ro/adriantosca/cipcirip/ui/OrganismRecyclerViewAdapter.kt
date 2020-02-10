package ro.adriantosca.cipcirip.ui

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


import ro.adriantosca.cipcirip.ui.OrganismFragment.OnOrganismListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_organism.view.*
import ro.adriantosca.cipcirip.R
import ro.adriantosca.cipcirip.model.Organism

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnOrganismListFragmentInteractionListener].
 */
class OrganismRecyclerViewAdapter(
    private val mValues: List<Organism>,
    private val mListenerOrganism: OnOrganismListFragmentInteractionListener?
) : RecyclerView.Adapter<OrganismRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

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
            .placeholder(ColorDrawable(holder.mImageView.getResources().getColor(R.color.design_default_color_background)))
            .into(holder.mImageView)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNameView: TextView = mView.name
        val mImageView: ImageView = mView.image

        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
