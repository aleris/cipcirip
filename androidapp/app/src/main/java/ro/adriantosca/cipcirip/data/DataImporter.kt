package ro.adriantosca.cipcirip.data

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.koin.core.KoinComponent
import org.koin.core.inject

class DataImporter: KoinComponent {
    fun importOrganisms(index: Int) {
        val organismRepository by inject<OrganismRepository>()

        this.javaClass.getResourceAsStream("data/organisms_$index.csv")
            ?.bufferedReader().use {
                CSVParser(it, CSVFormat.DEFAULT).records.forEach { record ->
                    // organismRepository.
                }
            }
    }
}