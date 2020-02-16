package ro.adriantosca.cipcirip.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.adriantosca.cipcirip.data.OrganismRepository
import ro.adriantosca.cipcirip.model.MediaType

class OrganismDetailsViewModel : ViewModel(), KoinComponent {
    private val organismRepository by inject<OrganismRepository>()

    private val organismIdLiveData = MutableLiveData<Int>()

    fun getOrganism() = Transformations.switchMap(organismIdLiveData) {
        organismRepository.get(it)
    }

    fun getMediaPaintWithAttribution() = Transformations.switchMap(organismIdLiveData) {
        organismRepository.findMediaWithAttributionWithMediaType(it, MediaType.Paint)
    }

    fun getMediaSoundWithAttribution() = Transformations.switchMap(organismIdLiveData) {
        organismRepository.findMediaWithAttributionWithMediaType(it, MediaType.Sound)
    }

    fun getMediaTextWithAttribution() = Transformations.switchMap(organismIdLiveData) {
        organismRepository.findMediaWithAttributionWithMediaType(it, MediaType.Text)
    }

    fun setOrganismId(organismId: Int) {
        organismIdLiveData.postValue(organismId)
    }
}
