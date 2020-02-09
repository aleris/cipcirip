package ro.adriantosca.cipcirip

import android.content.Context
import java.io.File

object TestUtils {
    fun deleteDatabaseFile(context: Context, databaseName: String) {
        val databases = File(context.applicationInfo.dataDir + "/databases")
        val db = File(databases, databaseName)
        if (db.delete())
            println("Database $db deleted")
        else
            println("Failed to delete $db database")

        val journal = File(databases, "$databaseName-journal")
        if (journal.exists()) {
            if (journal.delete())
                println("Database journal $journal deleted")
            else
                println("Failed to delete $journal database journal")
        }
    }
}