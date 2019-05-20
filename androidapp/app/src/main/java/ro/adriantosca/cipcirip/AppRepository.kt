package ro.adriantosca.cipcirip

class AppRepository(private val organismDao: OrganismDao) {
    fun getOrganism(organismId: Long): Organism {
        return organismDao.getOrganism(organismId)
    }
}
