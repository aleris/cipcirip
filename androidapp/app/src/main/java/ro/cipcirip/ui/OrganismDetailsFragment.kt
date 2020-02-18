package ro.cipcirip.ui

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import ro.cipcirip.R
import ro.cipcirip.SingleSongPlayer
import ro.cipcirip.model.MediaWithAttribution


class OrganismDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = OrganismDetailsFragment()
    }

    private lateinit var viewModel: OrganismDetailsViewModel
    private val singleSongPlayer by inject<SingleSongPlayer>()

    val args: OrganismDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this).get(OrganismDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.organism_details_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val organismId = args.organismId
        viewModel.getOrganism().observe(viewLifecycleOwner, Observer { organism ->
            (activity as AppCompatActivity?)?.supportActionBar?.title = organism.nameRom
            view.findViewById<TextView>(R.id.name).text = organism.nameRom
            view.findViewById<TextView>(R.id.name_lat).text = organism.nameLat
            val image = view.findViewById<ImageView>(R.id.image)
            Glide
                .with(image.context)
                .load(
                    Uri.parse("file:///android_asset/media/paintings/${organism.code}.jpg")
                )
                .placeholder(
                    ColorDrawable(
                        ContextCompat.getColor(image.context, R.color.placeholderColor)
                    )
                )
                .into(image)
            view.findViewById<TextView>(R.id.description).text = organism.descriptionRom

            showAsPlaying(view, organism == singleSongPlayer.currentPlayingOrganism)
            view.findViewById<ImageButton>(R.id.play).setOnClickListener {
                singleSongPlayer.play(organism, {
                    showAsPlaying(view, true)
                }, {
                    showAsPlaying(view, false)
                })
            }
            view.findViewById<ImageButton>(R.id.stop).setOnClickListener {
                singleSongPlayer.stop()
                showAsPlaying(view, false)
            }
        })

        viewModel.getMediaPaintWithAttribution().observe(viewLifecycleOwner, Observer { mediaList ->
            mediaList.firstOrNull().let {
                view.findViewById<TextView>(R.id.image_attribution).text = getAttributionText(it)
            }
        })

        viewModel.getMediaSoundWithAttribution().observe(viewLifecycleOwner, Observer { mediaList ->
            mediaList.firstOrNull().let {
                view.findViewById<TextView>(R.id.sound_attribution).text = getAttributionText(it)
            }
        })

        viewModel.getMediaTextWithAttribution().observe(viewLifecycleOwner, Observer { mediaList ->
            mediaList.firstOrNull().let {
                view.findViewById<TextView>(R.id.description_attribution).text = getAttributionText(it)
            }
        })

        viewModel.setOrganismId(organismId)
    }

    private fun showAsPlaying(view: View, playing: Boolean) {
        view.findViewById<ImageButton>(R.id.play).visibility = if (playing) View.GONE else View.VISIBLE
        view.findViewById<ImageButton>(R.id.stop).visibility = if (playing) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        menu.findItem(R.id.action_search)?.isVisible = false
    }

    private fun getAttributionText(it: MediaWithAttribution?): String {
        return it?.let { "${it.description} (${it.source})" }.orEmpty()
    }
}
