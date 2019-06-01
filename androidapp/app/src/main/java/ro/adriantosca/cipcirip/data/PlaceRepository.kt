package ro.adriantosca.cipcirip.data

import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.adriantosca.cipcirip.model.*

class PlaceRepository: KoinComponent {
    private val attributionDao by inject<AttributionDao>()
    private val placeDao by inject<PlaceDao>()
    private val placeAttributionDao by inject<PlaceAttributionDao>()

    fun insert(place: Place,
               photoAttribution: Attribution
    ) {
        placeDao.insert(place)
        insertAttribution(photoAttribution, place.id, Place.MediaCode.photo)
    }

    private fun insertAttribution(attribution: Attribution, placeId: Long, mediaCode: String) {
        attributionDao.insert(attribution)
        val placeAttribution = PlaceAttribution(placeId, mediaCode, attribution.id)
        placeAttributionDao.insert(placeAttribution)
    }
}
