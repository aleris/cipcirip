package ro.cipcirip.ui

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
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
import ro.cipcirip.model.*
import java.util.regex.Pattern


data class OrganismPlayDescriptor (
    var id: Long,
    var code: String
) {
    constructor(organism: Organism): this(organism.id, organism.code)
    constructor(organism: OrganismCodeAndNameOnly): this(organism.id, organism.code)
}

class OrganismDetailsFragment : Fragment() {
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
        viewModel.setOrganismId(args.organismId)
        loadIntoView(view)

        val supportActionBar = (activity as AppCompatActivity?)?.supportActionBar ?: return
        supportActionBar.hide()
    }

    private fun loadIntoView(view: View) {
        viewModel.getOrganism().observe(viewLifecycleOwner, Observer { organism ->
            organism?.let {
                loadOrganismIntoView(view, it)
            }

        })

        viewModel.getInformationWithAttribution()
            .observe(viewLifecycleOwner, Observer { informationWithAttribution ->
                informationWithAttribution?.let {
                    loadInformationIntoView(it, view)
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
    }

    private fun loadInformationIntoView(
        it: InformationWithAttribution,
        view: View
    ) {
        (activity as AppCompatActivity?)?.supportActionBar?.title =
            it.name
        view.findViewById<TextView>(R.id.name).text = it.name
        view.findViewById<TextView>(R.id.description).text =
            processTextParagraphs(it.description)
        view.findViewById<TextView>(R.id.description_attribution).text =
            getAttributionText(it)
    }

    private fun loadOrganismIntoView(view: View, organism: Organism) {
        view.findViewById<TextView>(R.id.name_lat).text = organism.nameLat
        val image = view.findViewById<ImageView>(R.id.image)
        Glide
            .with(image.context)
            .load(
                Uri.parse("file:///android_asset/media/paintings/${organism.code}.jpg")
            )
            .placeholder(
                ColorDrawable(
                    ContextCompat.getColor(image.context, R.color.colorPlaceholder)
                )
            )
            .into(image)

        showAsPlaying(view, organism.id == singleSongPlayer.currentPlayingOrganism?.id)
        view.findViewById<ImageButton>(R.id.play).setOnClickListener {
            singleSongPlayer.play(OrganismPlayDescriptor(organism), {
                showAsPlaying(view, true)
            }, {
                showAsPlaying(view, false)
            })
        }
        view.findViewById<ImageButton>(R.id.stop).setOnClickListener {
            singleSongPlayer.stop()
            showAsPlaying(view, false)
        }
    }

    private fun processTextParagraphs(text: String): SpannableString {
        val formattedText = text.replace("\n", "\n\n")
        val spannableString = SpannableString(formattedText)

        val matcher = Pattern.compile("\n\n").matcher(formattedText)
        while (matcher.find()) {
            spannableString.setSpan(
                AbsoluteSizeSpan(8, true),
                matcher.start() + 1,
                matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return spannableString
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
        return it?.let { "• ${it.description} (${it.source})" }.orEmpty()
    }

    private fun getAttributionText(it: InformationWithAttribution?): String {
        return it?.let { "• ${it.attributionDescription} (${it.attributionSource})" }.orEmpty()
    }
}
