package ro.adriantosca.cipcirip.datagather.wiki

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import ro.adriantosca.cipcirip.datagather.BirdInfo
import ro.adriantosca.cipcirip.datagather.NameUtils
import java.net.URL
import java.nio.file.Paths

class DescriptionScrapper {
    private val mapper = jacksonObjectMapper()

    fun fillInto(
        birdInfoList: List<BirdInfo>,
        directoryPath: String,
        skipNotExisting: Boolean
    ) {
        birdInfoList.forEach { birdInfo ->
            val fileRom = Paths.get("$directoryPath/${birdInfo.code}.ro.txt").toFile()
            val fileEng = Paths.get("$directoryPath/${birdInfo.code}.eng.txt").toFile()
            if (!fileRom.exists()) {
                if (skipNotExisting) {
                    println("Does not exists: ${birdInfo.code}.txt, but skipping.")
                } else {
                    fileRom.parentFile.mkdirs()
                    val scrappedRO = scrapDescription(birdInfo, "ro")
                    birdInfo.descriptionRom = scrappedRO.first
                    birdInfo.descriptionRomLink = scrappedRO.second
                    val scrappedEN = scrapDescription(birdInfo, "en")
                    birdInfo.descriptionEng = scrappedEN.first
                    birdInfo.descriptionEngLink = scrappedEN.second
                    fileRom.writeText("${birdInfo.descriptionRomLink}\n${birdInfo.descriptionRom}")
                    fileEng.writeText("${birdInfo.descriptionEngLink}\n${birdInfo.descriptionEng}")
                    println("${birdInfo.code}.txt OK.")
                }
            } else {
                fileRom.useLines {
                    birdInfo.descriptionRomLink = it.first()
                }
                fileRom.useLines {
                    birdInfo.descriptionRom = it.drop(1).joinToString("\n")
                }
                fileEng.useLines {
                    birdInfo.descriptionEngLink = it.first()
                }
                fileEng.useLines {
                    birdInfo.descriptionEng = it.drop(1).joinToString("\n")
                }
                println("Exists: ${birdInfo.code}.txt, skipping.")
            }
        }
    }

    private fun scrapDescription(birdInfo: BirdInfo, language: String): Pair<String, String> {
        val result = scrapInfo(birdInfo, language)
        val pageid = result["pageid"]
        val link = "https://$language.wikipedia.org/?curid=$pageid"
        val doc = Jsoup.connect(link).get()
        val text = extractDescription(doc)
        println("${birdInfo.code}($language):")
        println(link)
        println(text)
        println()
        return Pair(text, link)
    }

    private fun scrapInfo(
        birdInfo: BirdInfo,
        language: String
    ): Map<*, *> {
        val queryNameLat = NameUtils.firstNameLat(birdInfo)
            .replace(" ", "+")
            .replace(" ", "+")
        val link =
            "https://$language.wikipedia.org/w/api.php?action=query&list=search&srsearch=$queryNameLat&utf8=&format=json"
        val url = URL(link)
        val map = mapper.readValue(url, HashMap::class.java)
        val result = ((map["query"] as Map<*, *>)["search"] as ArrayList<*>)[0] as Map<*, *>
        return result
    }

    private fun extractDescription(doc: Document): String {
        val contentText = doc.select("#mw-content-text")
        //        contentText.select("div>table, div>div").remove()
        val nodes = contentText.select("div>p")
        val text = nodes
            .filter { it.text().isNotBlank() }
            .take(20)
            .filter { !it.text().endsWith(":") }
            .joinToString("\n") { it.text() }
            .replace(Regex("\\[\\d+]"), "")
        return correctCedilaDiacritics(text)
    }

    private fun correctCedilaDiacritics(s: String): String {
        return s
            .replace('ş', 'ș')
            .replace('Ş', 'Ș')
            .replace('ţ', 'ț')
            .replace('Ţ', 'Ț')
    }

}
