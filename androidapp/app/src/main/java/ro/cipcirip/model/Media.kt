package ro.cipcirip.model

import androidx.room.*


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Media::class,
            parentColumns = [Attribution.Contract.id],
            childColumns = [Media.Contract.attributionId]
        )
    ],
    indices = [Index(Media.Contract.attributionId)]
)
data class Media(
    @PrimaryKey
    var id: Int,
    var type: MediaType,
    var property: MediaProperty,
    var isLocal: Boolean,
    var externalLink: String?,
    var attributionId: Int
) {
    object Contract {
        const val id = "id"
        const val attributionId = "attributionId"
    }
}

data class MediaWithAttribution(
    var id: Int,
    var type: MediaType,
    var property: MediaProperty,
    var isLocal: Boolean,
    var externalLink: String?,
    var attributionId: Int,
    var description: String,
    var source: String
)

enum class MediaType {
    Sound,
    Paint,
    Photo,
    Text
}

enum class MediaProperty {
    None,
    Male,
    Female
}


class MediaTypeConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromMediaType(value: MediaType) = value.ordinal

        @TypeConverter
        @JvmStatic
        fun toMediaType(value: Int) = enumValues<MediaType>()[value]
    }
}

class MediaPropertyConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromMediaProperty(value: MediaProperty) = value.ordinal

        @TypeConverter
        @JvmStatic
        fun toMediaProperty(value: Int) = enumValues<MediaProperty>()[value]
    }
}
