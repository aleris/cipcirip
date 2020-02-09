package ro.adriantosca.cipcirip.ui.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ro.adriantosca.cipcirip.model.Organism

class OrganismItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var item: Organism? = null
        set(value) {
            field = value
            // view.
        }
}