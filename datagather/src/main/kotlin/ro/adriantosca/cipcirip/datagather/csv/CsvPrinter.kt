package ro.adriantosca.cipcirip.datagather.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import ro.adriantosca.cipcirip.datagather.BirdInfo
import ro.adriantosca.cipcirip.model.OrganismMedia
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class CsvPrinter {
    private val atlasPaintsAttributionDescription =
        """
            Ministerul Mediului, Apelor și Pădurilor – direcția Biodiversitate,
            Programul Operațional Sectorial – Mediu,
            Proiect: 36586 SMIS-CSNR „Sistemul naţional de gestiune şi monitorizare a speciilor de păsări din România în baza articolului 12 din Directiva Păsări”
            Proiect co-finanţat din Fondul European de Dezvoltare Regională prin Programul Operațional Sectorial Mediu.""".trimIndent()

    private val atlasPaintsAttributionSource = "http://monitorizareapasarilor.cndd.ro/atlasul_pasarilor.html"

    private val wikipediaAttributionDescription = "Wikipedia, The Free Encyclopedia".trimIndent()

    private val wikipediaAttributionSource = "https://wikipedia.org"

    fun print(birdInfoList: List<BirdInfo>, pathToDirectory: String) {
        val attributions = ArrayList<Attribution>()
        val medias = ArrayList<Media>()
        val organisms = ArrayList<Organism>()
        val organimsMedias = ArrayList<OrganismMedia>()

        birdInfoList.forEach { birdInfo ->
            val soundAttribution = Attribution(nextId(attributions), birdInfo.soundAttribution, birdInfo.soundLink)
            attributions.add(soundAttribution)

            val soundMedia = Media(nextId(medias), MediaType.Sound, MediaProperty.None, false, birdInfo.soundLink, soundAttribution.id)
            medias.add(soundMedia)

            val paintAttribution = Attribution(nextId(attributions), atlasPaintsAttributionDescription, atlasPaintsAttributionSource)
            attributions.add(paintAttribution)

            val paintMedia = Media(nextId(medias), MediaType.Paint, MediaProperty.None, true, null, paintAttribution.id)
            medias.add(paintMedia)

            val wikiAttribution = Attribution(nextId(attributions), wikipediaAttributionDescription, wikipediaAttributionSource)
            attributions.add(wikiAttribution)

            val wikiMedia = Media(nextId(medias), MediaType.Text, MediaProperty.None, true, null, wikiAttribution.id)
            medias.add(wikiMedia)

            val organism = Organism(
                nextId(organisms),
                birdInfo.code,
                birdInfo.nameLat,
                birdInfo.nameRom,
                birdInfo.nameEng,
                birdInfo.regnum,
                birdInfo.phylum,
                birdInfo.classis,
                birdInfo.ordo,
                birdInfo.familia,
                birdInfo.genus,
                birdInfo.species,
                birdInfo.descriptionRom,
                birdInfo.descriptionEng,
                0)
            organisms.add(organism)

            organimsMedias.add(OrganismMedia(organism.id, soundMedia.id))
            organimsMedias.add(OrganismMedia(organism.id, paintMedia.id))
            organimsMedias.add(OrganismMedia(organism.id, wikiMedia.id))
        }

        printCsv(attributions, pathToDirectory)
        printCsv(medias, pathToDirectory)
        printCsv(organisms, pathToDirectory)
        printCsv(organimsMedias, pathToDirectory)
    }

    private fun <E> nextId(list: List<E>) = list.size.toLong() + 1

    fun printCsv(list: List<Any>, pathToDirectory: String) {

        if (list.isEmpty()) {
            return
        }

        val header = getHeaderColumnsFromDataClass(list[0]::class) ?: return

        val name = list[0].javaClass.simpleName
        val pathToFile = "$pathToDirectory/$name.csv"
        val file = File(pathToFile)
        file.parentFile.mkdirs()

        file.writer().use { writer ->
            val printer = CSVPrinter(
                writer, CSVFormat.DEFAULT
            )
            printer.printRecord(header)
            list.forEach {
                val values = getValuesAsList(header, it)
                printer.printRecord(values)
            }
        }
    }

    private fun getHeaderColumnsFromDataClass(kClass: KClass<out Any>) = kClass
        .primaryConstructor
        ?.parameters
        ?.map { it.name } as ArrayList<String>

    private fun getValuesAsList(header: List<String>, instance: Any): List<String> {
        val list = arrayListOf<String>()
        for (name in header) {
            val v = readInstanceProperty(instance, name)
            list.add(v)
        }
        return list
    }

    @Suppress("UNCHECKED_CAST")
    private fun readInstanceProperty(instance: Any, propertyName: String): String {
        val property = instance::class.memberProperties
            .first { it.name == propertyName } as KProperty1<Any, *>
        return property.get(instance)?.toString() ?: ""
    }

//    fun printCsv(birdInfoList: List<BirdInfo>, path: String) {
//        File(path).writer().use { writer ->
//            val printer = CSVPrinter(
//                writer, CSVFormat.DEFAULT.withHeader(
//                    "code",
//                    "nameLat",
//                    "regnum",
//                    "phylum",
//                    "classis",
//                    "ordo",
//                    "familia",
//                    "genus",
//                    "species",
//                    "nameRom",
//                    "nameEng",
//                    "descriptionRom",
//                    "descriptionEng",
//                    "hasSound",
//                    "soundLink",
//                    "soundAttribution",
//                    "hasImagePaint",
//                    "imagePaintAttribution",
//                    "hasImageMale",
//                    "imageMaleAttribution",
//                    "hasImageFemale",
//                    "imageFemaleAttribution"
//                )
//            )
//            birdInfoList.forEach {
//                printer.printRecord(
//                    it.code,
//                    it.nameLat,
//                    it.regnum,
//                    it.phylum,
//                    it.classis,
//                    it.ordo,
//                    it.familia,
//                    it.genus,
//                    it.species,
//                    it.nameRom,
//                    it.nameEng,
//                    it.descriptionRom,
//                    it.descriptionEng,
//                    if (it.soundAttribution.isEmpty()) 0 else 1,
//                    it.soundLink,
//                    it.soundAttribution,
//                    1,
//                    "Ministerul Mediului, Apelor și Pădurilor – direcția Biodiversitate,\n" +
//                            "Programul Operațional Sectorial – Mediu,\n" +
//                            "Proiect: 36586 SMIS-CSNR „Sistemul naţional de gestiune şi monitorizare a speciilor de păsări din România în baza articolului 12 din Directiva Păsări”\n" +
//                            "Proiect co-finanţat din Fondul European de Dezvoltare Regională prin Programul Operațional Sectorial Mediu.",
//                    0,
//                    "",
//                    0,
//                    ""
//                )
//            }
//        }
//    }
}
