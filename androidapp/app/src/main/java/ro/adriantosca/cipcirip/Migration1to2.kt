package ro.adriantosca.cipcirip

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1to2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("create virtual table if not exists PlaceFts using fts4(content = 'Place', name, region)")
        database.execSQL("insert into PlaceFts(PlaceFts) values ('rebuild')")

        database.execSQL("create virtual table if not exists OrganismFts using fts4(content = 'Organism', nameLat, nameEng, nameRon, description)")
        database.execSQL("insert into OrganismFts(OrganismFts) values ('rebuild')")
    }
}
