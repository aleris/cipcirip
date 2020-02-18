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
            logger.error { "Error on import databse $ex" }
            FirebaseCrashlytics.getInstance().recordException(ex)
        }

    }

    private fun recordToAttribution(record: CSVRecord) = Attribution(
        record[0].toInt(),
        record[1],
        record[2]
    )

    private fun recordToMedia(record: CSVRecord) = Media(
        record[0].toInt(),
        MediaType.valueOf(record[1]),
        MediaProperty.valueOf(record[2]),
        record[3].toBoolean(),
        record[4],
        record[5].toInt()
    )

    private fun recordToOrganism(record: CSVRecord) = Organism(
        record[0].toInt(),
        record[1],
        record[2],
        record[3],
        record[4],
        record[5],
        record[6],
        record[7],
        record[8],
        record[9],
        record[10],
        record[11],
        record[12],
        record[13],
        record[14].toLong()
    )

    private fun recordToOrganismMedia(record: CSVRecord) = OrganismMedia(
        record[0].toInt(),
        record[1].toInt()
    )
}
