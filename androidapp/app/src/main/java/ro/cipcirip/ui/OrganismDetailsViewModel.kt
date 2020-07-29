package ro.cipcirip.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.cipcirip.data.OrganismRepository
import ro.cipcirip.model.Language
import ro.cipcirip.model.MediaType

class OrganismDetailsViewModel : ViewModel(), KoinComponent {
    private val organismRepository by inject<OrganismRepository>()

    private val organismIdLiveData = MutableLiveData<Long>()

    fun getOrganism() = Transformations.switchMap(organismIdLiveData) {
        organismRepository.get(it)
    }

    fun getMediaPaintWithAttribution() = Transformations.switchMap(organismIdLiveData) {
        organismRepository.findMediaWithAttributionWithMediaType(it, MediaType.Paint)
    }

    fun getMediaSoundWithAttribution() = Transformations.switchMap(organismIdLiveData) {
        organismRepository.findMediaWithAttributionWithMediaType(it, MediaType.Sound)
    }

    fun getInformationWithAttribution() = Transformations.switchMap(organismIdLiveData) {
        organismRepository.getInformationWithAttribution(it, Language.Rom)
    }

    fun setOrganismId(organismId: Long) {
        organismIdLiveData.postValue(organismId)
    }
}
