package ro.adriantosca.cipcirip.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.adriantosca.cipcirip.data.OrganismRepository

class OrganismViewModel: ViewModel(), KoinComponent {
    private val organismRepository by inject<OrganismRepository>()

    private val searchTextLiveData = MutableLiveData<String>("")

    fun getFilteredOrganisms() = Transformations.switchMap(searchTextLiveData) {
        if (it.isNullOrBlank()) {
            organismRepository.allOrganisms()
        } else {
            organismRepository.find("$it*")
        }
    }

    fun setOrganismsFilterQuery(query: String) {
        this.searchTextLiveData.postValue(query)
    }
}
