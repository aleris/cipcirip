package ro.adriantosca.cipcirip.data

import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.adriantosca.cipcirip.model.Attribution
import ro.adriantosca.cipcirip.model.Organism
import ro.adriantosca.cipcirip.model.OrganismAttribution

class OrganismRepository: KoinComponent {
    private val attributionDao by inject<AttributionDao>()
    private val organismDao by inject<OrganismDao>()
    private val organismAttributionDao by inject<OrganismAttributionDao>()

    fun insert(organism: Organism,
               soundAttribution: Attribution,
               paintAttribution: Attribution,
               photoMaleAttribution: Attribution,
               photoFemaleAttribution: Attribution
    ) {
        organismDao.insert(organism)
        insertAttribution(soundAttribution, organism.id, Organism.MediaCode.sound)
        insertAttribution(paintAttribution, organism.id, Organism.MediaCode.paint)
        insertAttribution(photoMaleAttribution, organism.id, Organism.MediaCode.photoMale)
        insertAttribution(photoFemaleAttribution, organism.id, Organism.MediaCode.photoFemale)
    }

    private fun insertAttribution(attribution: Attribution, organismId: Long, mediaCode: String) {
        attributionDao.insert(attribution)
        val organismAttribution = OrganismAttribution(organismId, mediaCode, attribution.id)
        organismAttributionDao.insert(organismAttribution)
    }
}
