package ro.adriantosca.cipcirip.datagather

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.pdfbox.pdmodel.PDDocument
import ro.adriantosca.cipcirip.datagather.atlas.PDFInfoExtractor
import ro.adriantosca.cipcirip.datagather.atlas.PaintExtractor
import ro.adriantosca.cipcirip.datagather.canto.SongDownloader
import java.io.File

class Extractor {
    private val dataDirectoryPath = "/Users/at/Projects/CipCirip/datagather/data"

    fun extract() {
        val file = File("$dataDirectoryPath/Atlasul-Pasarilor-2015.pdf")
        PDDocument.load(file).use { document ->
            print("Extracting bird info ...")
            val mapInfo = PDFInfoExtractor().extractAllBirdInfos(document)

            println(" OK")
//            mapInfo.values.forEach { println(it) }

            println("Extracting paintings:")
            PaintExtractor().extract(document, mapInfo, "$dataDirectoryPath/paintings")
            println("OK")

            println("Finding songs:")
            SongDownloader().findAndDownloadSongs(mapInfo, "$dataDirectoryPath/songs")
            println("OK")

            print("Printing CSV ...")
            printCsv(mapInfo)
            println(" OK")

        }
    }

    private fun printCsv(mapInfo: Map<String, BirdInfo>) {
        File("$dataDirectoryPath/info.csv").writer().use { writer ->
            val printer = CSVPrinter(
                writer, CSVFormat.DEFAULT.withHeader(
                    "code",
                    "nameLat",
                    "regnum",
                    "phylum",
                    "classis",
                    "ordo",
                    "familia",
                    "genus",
                    "species",
                    "nameRom",
                    "nameEng",
                    "description",
                    "hasSound",
                    "soundAttribution",
                    "hasImagePaint",
                    "imagePaintAttribution",
                    "hasImageMale",
                    "imageMaleAttribution",
                    "hasImageFemale",
                    "imageFemaleAttribution"
                )
            )
            mapInfo.values.forEach {
                printer.printRecord(
                    it.code,
                    it.nameLat,
                    it.regnum,
                    it.phylum,
                    it.classis,
                    it.ordo,
                    it.familia,
                    it.genus,
                    it.species,
                    it.nameRom,
                    it.nameEng,
                    it.description,
                    if (it.soundRecordist.isEmpty()) 0 else 1,
                    it.soundRecordist,
                    1,
                    "Ministerul Mediului, Apelor și Pădurilor – direcția Biodiversitate,",
                    0,
                    "",
                    0,
                    ""
                )
            }
        }
    }
}
