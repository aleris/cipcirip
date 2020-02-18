package ro.cipcirip.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1to2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("ALTER TABLE OrganismFTS CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci")
//        database.execSQL("create virtual table if not exists PlaceFts using FTS4(content = `Place`, nameRom, nameEng, regionRom, regionEng, descriptionRom, descriptionEng)")
//        database.execSQL("insert into PlaceFTS4(PlaceFts) values ('rebuild')")
//
//        database.execSQL("create virtual table if not exists OrganismFts using FTS4(content = `Organism`, nameLat, nameRon, nameEng, descriptionRon, descriptionEng)")
//        database.execSQL("insert into OrganismFTS(OrganismFts) values ('rebuild')")
    }
}
