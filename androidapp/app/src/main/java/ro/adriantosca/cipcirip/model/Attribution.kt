package ro.adriantosca.cipcirip.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Attribution(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var source: String,
    var text: String
) {
    object Contract {
        const val id = "id"
    }
}
