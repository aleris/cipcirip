package ro.adriantosca.cipcirip.datagather

data class BirdInfo(var pageNumber: Int, // 1-based
                    var code: String,
                    var nameRom: String,
                    var nameEng: String,
                    var nameLat: String,
                    var regnum: String,
                    var phylum: String,
                    var classis: String,
                    var ordo: String,
                    var familia: String,
                    var genus: String,
                    var species: String,
                    var descriptionRom: String = "",
                    var descriptionEng: String = "",
                    var paintAttribution: String = "",
                    var soundLink: String = "",
                    var soundAttribution: String = ""
)
