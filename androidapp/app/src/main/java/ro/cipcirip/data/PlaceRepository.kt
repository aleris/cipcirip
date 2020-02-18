package ro.cipcirip.data

import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.cipcirip.model.*

class PlaceRepository: KoinComponent {
    private val mediaDao by inject<MediaDao>()
    private val placeDao by inject<PlaceDao>()
    private val placeMediaDao by inject<PlaceMediaDao>()

    fun insert(place: Place,
               photoMedia: Media
    ) {
        placeDao.insert(place)
        insertMedia(photoMedia, place.id)
    }

    private fun insertMedia(media: Media, placeId: Int) {
        mediaDao.insert(media)
        val placeMedia = PlaceMedia(placeId, media.id)
        placeMediaDao.insert(placeMedia)
    }
}
