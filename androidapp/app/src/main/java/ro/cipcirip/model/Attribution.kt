package ro.cipcirip.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Attribution(
    @PrimaryKey
    var id: Int,
    var description: String,
    var source: String
) {
    object Contract {
        const val id = "id"
    }
}
