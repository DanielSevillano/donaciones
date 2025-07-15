package io.github.danielsevillano.donaciones.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Colecta::class],
    version = 1
)
@TypeConverters(Conversores::class)
abstract class ColectaDatabase : RoomDatabase() {
    abstract fun dao(): ColectaDao
}