package ro.adriantosca.cipcirip.ui

import androidx.lifecycle.ViewModel
import ro.adriantosca.cipcirip.data.OrganismRepository

class OrganismViewModel(val organismRepository: OrganismRepository): ViewModel() {
    val allOrganisms by lazy {
        organismRepository.allOrganisms()
    }
}
