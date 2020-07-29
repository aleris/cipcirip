package ro.cipcirip.data

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.koin.core.KoinComponent
import org.koin.core.inject
import ro.cipcirip.model.*

private val logger = KotlinLogging.logger {}

class DataImporter(private val context: Context) : KoinComponent {
    fun import(dbsql: SupportSQLiteDatabase, index: Int) {
        try {
            val db by inject<AppDatabase>()

            logger.info { "Importing Organism..." }
            context.assets.open("data/$index/Organism.csv")
                .bufferedReader()
                .use { reader ->
                    val organismList = CSVParser(reader, CSVFormat.DEFAULT).records
                        .drop(1)
                        .map { record -> recordToOrganism(record) }

                    organismList.forEach { organism ->
                        if (db.organismDao().exists(organism.id)) {
                            db.organismDao().update(organism)
                        } else {
                            db.organismDao().insert(organism)
                        }
                    }
                }

            logger.info { "Importing Attribution..." }
            context.assets.open("data/$index/Attribution.csv")
                .bufferedReader()
                .use { reader ->
                    val attributionList = CSVParser(reader, CSVFormat.DEFAULT).records
                        .drop(1)
                        .map { record -> recordToAttribution(record) }

                    attributionList.forEach { attribution ->
                        if (db.attributionDao().exists(attribution.id)) {
                            db.attributionDao().update(attribution)
                        } else {
                            db.attributionDao().insert(attribution)
                        }
                    }
                }

            logger.info { "Importing Information..." }
            context.assets.open("data/$index/Information.csv")
                .bufferedReader()
                .use { reader ->
                    val informationList = CSVParser(reader, CSVFormat.DEFAULT).records
                        .drop(1)
                        .map { record -> recordToInformation(record) }

                    informationList.forEach { information ->
                        if (db.informationDao().exists(information.id)) {
                            db.informationDao().update(information)
                        } else {
                            db.informationDao().insert(information)
                        }
                    }
                }

            logger.info { "Importing OrganismInformation..." }
            context.assets.open("data/$index/OrganismInformation.csv")
                .bufferedReader()
                .use { reader ->
                    val organismInformationList = CSVParser(reader, CSVFormat.DEFAULT).records
                        .drop(1)
                        .map { record -> recordToOrganismInformation(record) }

                    organismInformationList.forEach { organismInformation ->
                        if (!db.organismInformationDao().exists(
                                organismInformation.organismId,
                                organismInformation.informationId
                            )
                        ) {
                            db.organismInformationDao().insert(organismInformation)
                        }
                    }
                }

            logger.info { "Importing Media..." }
            context.assets.open("data/$index/Media.csv")
                .bufferedReader()
                .use { reader ->
                    val mediaList = CSVParser(reader, CSVFormat.DEFAULT).records
                        .drop(1)
                        .map { record -> recordToMedia(record) }

                    mediaList.forEach { media ->
                        if (db.mediaDao().exists(media.id)) {
                            db.mediaDao().update(media)
                        } else {
                            db.mediaDao().insert(media)
                        }
                    }
                }

            logger.info { "Importing OrganismMedia..." }
            context.assets.open("data/$index/OrganismMedia.csv")
                .bufferedReader()
                .use { reader ->
                    val organismMediaList = CSVParser(reader, CSVFormat.DEFAULT).records
                        .drop(1)
                        .map { record -> recordToOrganismMedia(record) }

                    organismMediaList.forEach { organismMedia ->
                        if (!db.organismMediaDao().exists(
                                organismMedia.organismId,
                                organismMedia.mediaId
                            )
                        ) {
                            db.organismMediaDao().insert(organismMedia)
                        }
                    }
                }

            logger.info { "Import done." }
        } catch (ex: Exception) {
            logger.error { "Error on import database $ex" }
            FirebaseCrashlytics.getInstance().recordException(ex)
        }

    }

    private fun recordToAttribution(record: CSVRecord) = Attribution(
        record[0].toLong(),
        record[1],
        record[2]
    )

    private fun recordToMedia(record: CSVRecord) = Media(
        record[0].toLong(),
        MediaType.valueOf(record[1]),
        MediaProperty.valueOf(record[2]),
        record[3].isNullOrBlank(),
        emptyOrBlankToNull(record[3]),
        record[4].toLong()
    )

    private fun recordToOrganism(record: CSVRecord) = Organism(
        record[0].toLong(),
        record[1],
        record[2],
        record[3],
        record[4],
        record[5],
        record[6],
        record[7],
        record[8],
        record[9],
        record[10].toLong()
    )

    private fun recordToInformation(record: CSVRecord) = Information(
        record[0].toLong(),
        record[1].toLong(),
        Language.valueOf(record[2]),
        record[3],
        record[4],
        emptyOrBlankToNull(record[5]),
        record[6].toLong()
    )

    private fun recordToOrganismMedia(record: CSVRecord) = OrganismMedia(
        record[0].toLong(),
        record[1].toLong()
    )

    private fun recordToOrganismInformation(record: CSVRecord) = OrganismInformation(
        record[0].toLong(),
        record[1].toLong()
    )

    private fun emptyOrBlankToNull(v: String?): String? = if (v.isNullOrBlank()) null else v
}
