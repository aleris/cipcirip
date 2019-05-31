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

    fun fillInto(birdInfoList: List<BirdInfo>, directoryPath: String) {
        birdInfoList.forEach { birdInfo ->
            val fileRom = Paths.get("$directoryPath/${birdInfo.code}.ro.txt").toFile()
            val fileEng = Paths.get("$directoryPath/${birdInfo.code}.eng.txt").toFile()
            if (!fileRom.exists()) {
                fileRom.parentFile.mkdirs()
                birdInfo.descriptionRom = scrapDescription(birdInfo, "ro")
                birdInfo.descriptionEng = scrapDescription(birdInfo, "en")
                fileRom.writeText(birdInfo.descriptionRom)
                fileEng.writeText(birdInfo.descriptionEng)
                println("${birdInfo.code}.txt OK.")
            } else {
                birdInfo.descriptionRom = fileRom.readText()
                birdInfo.descriptionEng = fileEng.readText()
                println("Exists: ${birdInfo.code}.txt, skipping.")
            }
        }
    }

    private fun scrapDescription(birdInfo: BirdInfo, language: String): String {
        val result = scrapInfo(birdInfo, language)
        val pageid = result["pageid"]
        val doc = Jsoup.connect("https://$language.wikipedia.org/?curid=$pageid").get()
        val text = extractDescription(doc)
        println("${birdInfo.code}($language):")
        println(text)
        println()
        return text
    }

    private fun scrapInfo(
        birdInfo: BirdInfo,
        language: String
    ): Map<*, *> {
        val queryNameLat = NameUtils.firstNameLat(birdInfo).replace(" ", "+")
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
            .take(3)
            .filter { !it.text().endsWith(":") }
            .joinToString("\n") { it.text() }
            .replace(Regex("\\[\\d+]"), "")
        return text
    }
}
