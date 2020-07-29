package ro.cipcirip.data

import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.cipcirip.model.*

class OrganismRepository: KoinComponent {
    private val organismDao by inject<OrganismDao>()
    private val mediaDao by inject<MediaDao>()
    private val attributionDao by inject<AttributionDao>()
    private val organismMediaDao by inject<OrganismMediaDao>()
    private val informationDao by inject<InformationDao>()

    fun insert(organism: Organism) = organismDao.insert(organism)

    fun insert(attribution: Attribution) = attributionDao.insert(attribution)

    fun insert(media: Media) = mediaDao.insert(media)

    fun insert(organismMedia: OrganismMedia) = organismMediaDao.insert(organismMedia)


    fun exists(organism: Organism) = organismDao.exists(organism.id)

    fun exists(attribution: Attribution) = attributionDao.exists(attribution.id)

    fun exists(media: Media) = mediaDao.exists(media.id)

    fun exists(organismMedia: OrganismMedia) = organismMediaDao.exists(
        organismMedia.organismId,
        organismMedia.mediaId
    )

    fun update(organism: Organism) = organismDao.update(organism)

    fun update(attribution: Attribution) = attributionDao.update(attribution)

    fun update(media: Media) = mediaDao.update(media)

    fun allOrganisms(language: Language) = organismDao.all(language)

    fun find(searchText: String, language: Language) = organismDao.find(searchText, language)

    fun get(id: Long) = organismDao.get(id)

    fun findMediaWithAttributionWithMediaType(organismId: Long, mediaType: MediaType) =
        organismMediaDao.findMediaWithAttributionWithMediaType(organismId, mediaType)

    fun getInformationWithAttribution(organismId: Long, language: Language) =
        informationDao.getWithAttribution(organismId, language)
}
