package ro.adriantosca.cipcirip.datagather

import org.apache.pdfbox.pdmodel.PDDocument
import ro.adriantosca.cipcirip.datagather.atlas.PDFInfoExtractor
import ro.adriantosca.cipcirip.datagather.atlas.PaintExtractor
import ro.adriantosca.cipcirip.datagather.canto.SongDownloader
import ro.adriantosca.cipcirip.datagather.wiki.DescriptionScrapper
import java.io.File

class Extractor {
    private val dataDirectoryPath = "/Users/at/Projects/CipCirip/datagather/data"

    fun extract() {
        val file = File("$dataDirectoryPath/Atlasul-Pasarilor-2015.pdf")
        PDDocument.load(file).use { document ->
            print("Extracting bird info ...")
            val birdInfoMap = PDFInfoExtractor().extractAllBirdInfos(document)

            println(" OK")
//            mapInfo.values.forEach { println(it) }

            val birdInfoList = birdInfoMap.values.toList()

            val genDirectoryPath = "$dataDirectoryPath/gen"

            println("Getting descriptions:")
            DescriptionScrapper().fillInto(birdInfoList, "$genDirectoryPath/descriptions")
            println("OK")

            println("Extracting paintings:")
            PaintExtractor().extract(document, birdInfoList, "$genDirectoryPath/paintings")
            println("OK")

            println("Finding songs:")
            SongDownloader().findAndDownloadSongs(birdInfoList, "$genDirectoryPath/songs")
            println("OK")

            print("Printing CSV ...")
            CsvPrinter().printCsv(birdInfoList, "$genDirectoryPath/organisms.csv")
            println(" OK")
        }
    }
}
