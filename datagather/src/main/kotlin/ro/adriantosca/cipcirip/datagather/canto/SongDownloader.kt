package ro.adriantosca.cipcirip.datagather.canto

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import ro.adriantosca.cipcirip.datagather.BirdInfo
import ro.adriantosca.cipcirip.datagather.NameUtils
import java.io.File

class SongDownloader {

    fun findAndDownloadSongs(
        birdInfoList: List<BirdInfo>,
        directoryPath: String,
        skipNotExisting: Boolean
    ) {
        File(directoryPath).mkdirs()
        birdInfoList.forEach { birdInfo ->
            val infoFile = File("$directoryPath/${birdInfo.code}.txt")
            val soundFile = File("$directoryPath/${birdInfo.code}.mp3")
            if (!infoFile.exists()) {
                if (skipNotExisting) {
                    println("Does not exists: $infoFile, but skipping.")
                } else {
                    infoFile.parentFile.mkdirs()
                    val songInfo = findSong(birdInfo)
                    if (null == songInfo) {
                        println("NOT found: ${birdInfo.nameLat}")
                    } else {
                        val infoText = """
${songInfo.id}
${songInfo.recordist}
${songInfo.downloadLink}
${songInfo.length}
                    """.trimIndent()
                        infoFile.writeText(infoText)
                        birdInfo.soundDownloadLink = songInfo.downloadLink
                        birdInfo.soundRecordist = songInfo.recordist
                        saveSoundFileIfNotExists(soundFile, songInfo.downloadLink, birdInfo)
                    }
                }
            } else {
                val infoTextLines = infoFile.readText().lines()
                birdInfo.soundId = infoTextLines[0]
                birdInfo.soundRecordist = infoTextLines[1]
                birdInfo.soundDownloadLink = infoTextLines[2]
                println("Exists: $infoFile, skipping.")
            }
        }
    }

    private fun saveSoundFileIfNotExists(
        file: File,
        link: String,
        birdInfo: BirdInfo
    ) {
        if (!file.exists()) {
            saveSoundFile(link, file, birdInfo)
        } else {
            println("Exists: $file, skipping.")
        }
    }

    private fun saveSoundFile(
        link: String,
        file: File,
        birdInfo: BirdInfo
    ) {
        try {
            val bytes = Jsoup
                .connect(link)
                .ignoreContentType(true)
                .execute()
                .bodyAsBytes()
            file.writeBytes(bytes)
            println("$file OK")
        } catch (e: Exception) {
            println("ERR download: ${birdInfo.code}: ${e.message}")
        }
    }

    private fun findSong(birdInfo: BirdInfo): SongInfo? {
        val songList = arrayListOf<SongInfo>()
        val queryNameLat = NameUtils.queryName(NameUtils.firstNameLat(birdInfo))
        val pages = IntRange(1, 2)
        extractListMultiPage(birdInfo, queryNameLat, pages, songList)
        if (songList.isEmpty()) {
            // empty? try second latin name if has one
            val queryNameLatSecond = NameUtils.secondNameLat(birdInfo)
            if (null != queryNameLatSecond) {
                extractListMultiPage(birdInfo, NameUtils.queryName(queryNameLatSecond), pages, songList)
            }
            // still empty? try english name
            if (songList.isEmpty()) {
                val queryNameEng = birdInfo.nameEng.replace(" ", "+")
                extractListMultiPage(birdInfo, queryNameEng, pages, songList)
            }
        }
        val sortedByLength = songList.sortedBy { it.length }
        val take1 = sortedByLength.firstOrNull { it.length.startsWith("0:1") }
        if (null != take1) return take1
        val take2 = sortedByLength.firstOrNull { it.length.startsWith("0:2") }
        if (null != take2) return take2
        val take3 = sortedByLength.firstOrNull { it.length.startsWith("0:3") }
        if (null != take3) return take3
        return sortedByLength.firstOrNull()
    }

    private fun extractListMultiPage(
        birdInfo: BirdInfo,
        queryName: String,
        pages: IntRange,
        songList: ArrayList<SongInfo>
    ) {
        for (page in pages) {
            val listPage = extractList(birdInfo.code, queryName, page)
            songList.addAll(listPage)
        }
    }

    private fun extractList(birdCode: String, queryName: String, page: Int): List<SongInfo> {
        val songList = arrayListOf<SongInfo>()
        val doc = Jsoup.connect("https://www.xeno-canto.org/explore?query=$queryName&pg=$page").get()
        if (0 < doc.select(".error").count()) {
            return songList
        }

        val rows = doc.select(".results tr")
        for (row in rows) {
            val cols = row.select("td")
            if (0 < cols.size && isKnown(cols)) {
                val length = cols[2].text()
                val recordist = cols[3].text()
                val linkPath = row.selectFirst("td div div").attr("data-xc-filepath")
                val downloadLink = "http:$linkPath"
                val id = cols.last().select("a").attr("href").substring(1)
                songList.add(SongInfo(birdCode, length, recordist, downloadLink, id))
            }
        }

        return songList
    }

    private fun isKnown(cols: Elements) = !cols[1].text().contains("?")
}
