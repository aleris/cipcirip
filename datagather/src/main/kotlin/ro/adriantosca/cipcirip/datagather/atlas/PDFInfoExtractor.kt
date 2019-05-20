package ro.adriantosca.cipcirip.datagather.atlas

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import ro.adriantosca.cipcirip.datagather.BirdInfo

class PDFInfoExtractor {
    companion object {
        private val BIRD_INFO_PATTERN =
            "\\d+\\n(.+) - (.+)\\n(.+)\\nRegnul:\\s+(.+)\\nClasa:\\s+(.+)\\nORdinul:\\s+(.+)\\nFamilia:\\s+(.+)\\ngenul:\\s+(.+)\\nspeCia:\\s+(.+)\\n"
    }

    private val textStripper = PDFTextStripper()

    fun extractAllBirdInfos(document: PDDocument): Map<String, BirdInfo> {
        val map = hashMapOf<String, BirdInfo>()
        document.pages.forEachIndexed {index, _ ->
            val birdInfo = findBirdInfoInPage(document,index + 1)
            if (null != birdInfo) {
                map[birdInfo.code] = birdInfo
            }
        }
        return map
    }

    private fun findBirdInfoInPage(document: PDDocument, pageNumber: Int): BirdInfo? {
        textStripper.startPage = pageNumber
        textStripper.endPage = pageNumber

        val pageText = textStripper.getText(document)

        val matchResult = Regex(
            BIRD_INFO_PATTERN,
            setOf(RegexOption.MULTILINE, RegexOption.IGNORE_CASE)).find(pageText) ?: return null
        return createBirdInfoFromPageMatch(pageNumber, matchResult)
    }

    private fun createBirdInfoFromPageMatch(pageNumber: Int, matchResult: MatchResult): BirdInfo {
        val nameRom = matchResult.groupValues[1].trim().toLowerCase().split("(").joinToString("(") { it.capitalize() }
        val nameEng = toTitleCase(matchResult.groupValues[2].trim())
        val nameLat = matchResult.groupValues[3].trim()
        val regnum = matchResult.groupValues[4].trim()
        var phylum = ""
        val classis = matchResult.groupValues[5].trim()
        val ordo = matchResult.groupValues[6].trim()
        val familia = matchResult.groupValues[7].trim()
        val genus = matchResult.groupValues[8].trim()
        val species = matchResult.groupValues[9].trim()
        val code = code(nameLat)

        return BirdInfo(
            pageNumber,
            code,
            nameRom,
            nameEng,
            nameLat,
            regnum,
            phylum,
            classis,
            ordo,
            familia,
            genus,
            species
        )
    }

    private fun toTitleCase(text: String) =
        text
            .trim()
            .split(" ").joinToString(" ") {
                it.toLowerCase().capitalize()
            }
            .split("-").joinToString("-") {
                it.capitalize()
            }

    private fun code(name: String): String {
        val p = name.indexOf("(")
        val t = if (p != -1) {
            name.substring(0, p).trim()
        } else {
            name
        }
        return t.toLowerCase().split(" ").joinToString("_")
    }
}
