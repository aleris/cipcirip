package ro.adriantosca.cipcirip.datagather

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.File

class CsvPrinter {
    fun printCsv(birdInfoList: List<BirdInfo>, path: String) {
        File(path).writer().use { writer ->
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
                    "descriptionRom",
                    "descriptionEng",
                    "hasSound",
                    "soundLink",
                    "soundAttribution",
                    "hasImagePaint",
                    "imagePaintAttribution",
                    "hasImageMale",
                    "imageMaleAttribution",
                    "hasImageFemale",
                    "imageFemaleAttribution"
                )
            )
            birdInfoList.forEach {
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
                    it.descriptionRom,
                    it.descriptionEng,
                    if (it.soundAttribution.isEmpty()) 0 else 1,
                    it.soundLink,
                    it.soundAttribution,
                    1,
                    "Ministerul Mediului, Apelor și Pădurilor – direcția Biodiversitate,\n" +
                            "Programul Operațional Sectorial – Mediu,\n" +
                            "Proiect: 36586 SMIS-CSNR „Sistemul naţional de gestiune şi monitorizare a speciilor de păsări din România în baza articolului 12 din Directiva Păsări”\n" +
                            "Proiect co-finanţat din Fondul European de Dezvoltare Regională prin Programul Operațional Sectorial Mediu.",
                    0,
                    "",
                    0,
                    ""
                )
            }
        }
    }
}
