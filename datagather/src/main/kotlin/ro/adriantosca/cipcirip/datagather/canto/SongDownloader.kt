package ro.adriantosca.cipcirip.datagather.canto

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import ro.adriantosca.cipcirip.datagather.BirdInfo
import java.io.File

class SongDownloader {

    fun findAndDownloadSongs(birdMapInfo: Map<String, BirdInfo>, directoryPath: String) {
        File(directoryPath).mkdirs()
        birdMapInfo.values.forEach { birdInfo ->
            val file = File("$directoryPath/${birdInfo.code}.mp3")
            if (!file.exists()) {
                val found = findSong(birdInfo)
                if (null == found) {
                    println("NOT found: ${birdInfo.nameLat}")
                } else {
                    val recordist = found.recordist
                    birdInfo.soundRecordist = recordist
                    val link = "http:${found.link}"
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
            } else {
                println("Exists: $file, skipping.")
            }
        }
    }

    private fun findSong(birdInfo: BirdInfo): SongInfo? {
        val songList = arrayListOf<SongInfo>()
        val queryNameLat = firstNameLat(birdInfo).replace(" ", "+")
        val pages = IntRange(1, 2)
        extractListMultiPage(birdInfo, queryNameLat, pages, songList)
        if (songList.isEmpty()) {
            // empty? try second latin name if has one
            val queryNameLatSecond = secondNameLat(birdInfo)
            if (null != queryNameLatSecond) {
                extractListMultiPage(birdInfo, queryNameLatSecond, pages, songList)
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
        return take3
    }

    private fun firstNameLat(birdInfo: BirdInfo): String {
        val nameLat = birdInfo.nameLat
        if (nameLat.contains('(')) {
            return nameLat.substringBefore('(')
        }
        return nameLat
    }

    private fun secondNameLat(birdInfo: BirdInfo): String? {
        val nameLat = birdInfo.nameLat
        if (nameLat.contains('(')) {
            return nameLat.substringAfter('(').substringBefore(')')
        }
        return null
    }

    private fun extractListMultiPage(
        birdInfo: BirdInfo,
        queryName: String,
        pages: IntRange,
        songList: ArrayList<SongInfo>
    ) {
        for (page in pages) {
            val listPage = extractList(birdInfo.code, queryName, 1)
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
                val link = row.selectFirst("td div div").attr("data-xc-filepath")
                songList.add(SongInfo(birdCode, length, recordist, link))
            }
        }

        return songList
    }

    private fun isKnown(cols: Elements) = !cols[1].text().contains("?")
}
