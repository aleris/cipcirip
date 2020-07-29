package ro.adriantosca.cipcirip.datagather

import org.apache.pdfbox.pdmodel.PDDocument
import ro.adriantosca.cipcirip.datagather.atlas.PDFInfoExtractor
import ro.adriantosca.cipcirip.datagather.atlas.PaintExtractor
import ro.adriantosca.cipcirip.datagather.canto.SongDownloader
import ro.adriantosca.cipcirip.datagather.canto.SongOptimizer
import ro.adriantosca.cipcirip.datagather.csv.CsvPrinter
import ro.adriantosca.cipcirip.datagather.wiki.DescriptionScrapper
import java.io.File

class Extractor {
    private val dataDirectoryPath = "/Users/adrian.tosca/Adi/cipcirip/datagather/data"
    private val genDirectoryPath = "$dataDirectoryPath/gen"

    fun extract(skipNotExisting: Boolean) {
        val file = File("$dataDirectoryPath/Atlasul-Pasarilor-2015.pdf")
        PDDocument.load(file).use { document ->
            print("Extracting bird info ...")
            val birdInfoMap = PDFInfoExtractor().extractAllBirdInfos(document)

            println(" OK")
//            mapInfo.values.forEach { println(it) }

            val birdInfoList = birdInfoMap.values.toList()

            println("Getting descriptions:")
            DescriptionScrapper().fillInto(birdInfoList, "$genDirectoryPath/descriptions", skipNotExisting)
            println("OK")

            println("Extracting paintings:")
            PaintExtractor().extract(document, birdInfoList, "$genDirectoryPath/paintings", skipNotExisting)
            println("OK")

            println("Finding songs:")
            SongDownloader().findAndDownloadSongs(birdInfoList, "$genDirectoryPath/songs", skipNotExisting)
            println("OK")

            print("Printing CSV ...")
            CsvPrinter().print(birdInfoList, "$genDirectoryPath/out")
            println(" OK")
        }
    }

    fun optimizeSongs() {
        println("Optimizing songs:")
        SongOptimizer().optimize("$genDirectoryPath/songs", "$genDirectoryPath/songs-optimized")
        println("OK")
    }
}
