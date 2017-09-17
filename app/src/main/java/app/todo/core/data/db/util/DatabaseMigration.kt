package app.todo.core.data.db.util

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

object DatabaseMigration {

    val MIGRATION: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // TODO
        }
    }

}