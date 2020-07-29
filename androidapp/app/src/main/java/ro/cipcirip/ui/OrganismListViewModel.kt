package ro.cipcirip.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.cipcirip.data.OrganismRepository
import ro.cipcirip.model.Language

class OrganismListViewModel: ViewModel(), KoinComponent {
    private val organismRepository by inject<OrganismRepository>()

    private val searchTextLiveData = MutableLiveData<String>("")

    fun getFilteredOrganisms(language: Language) = Transformations.switchMap(searchTextLiveData) {
        if (it.isNullOrBlank()) {
            organismRepository.allOrganisms(language)
        } else {
            organismRepository.find("$it*", language)
        }
    }

    fun setOrganismsFilterQuery(query: String) {
        this.searchTextLiveData.postValue(query)
    }
}
