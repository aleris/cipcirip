package ro.adriantosca.cipcirip.data

import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.adriantosca.cipcirip.model.Attribution
import ro.adriantosca.cipcirip.model.Media
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.model.OrganismMedia

class OrganismRepository: KoinComponent {
    private val organismDao by inject<OrganismDao>()
    private val mediaDao by inject<MediaDao>()
    private val attributionDao by inject<AttributionDao>()
    private val organismMediaDao by inject<OrganismMediaDao>()

    fun insert(organism: Organism) = organismDao.insert(organism)

    fun insert(attribution: Attribution) = attributionDao.insert(attribution)

    fun insert(media: Media) = mediaDao.insert(media)

    fun insert(organismMedia: OrganismMedia) = organismMediaDao.insert(organismMedia)


    fun exists(organism: Organism) = organismDao.exists(organism.id)

    fun exists(attribution: Attribution) = attributionDao.exists(attribution.id)

    fun exists(media: Media) = mediaDao.exists(media.id)

    fun exists(organismMedia: OrganismMedia) = organismMediaDao.exists(organismMedia.organismId, organismMedia.mediaId)


    fun update(organism: Organism) = organismDao.update(organism)

    fun update(attribution: Attribution) = attributionDao.update(attribution)

    fun update(media: Media) = mediaDao.update(media)
}
