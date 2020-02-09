package ro.adriantosca.cipcirip.ui.list

import androidx.lifecycle.ViewModel
import ro.adriantosca.cipcirip.data.OrganismDao

class OrganismListViewModel(private val organismDao: OrganismDao): ViewModel() {
    val all
        get() = organismDao.all()
}
