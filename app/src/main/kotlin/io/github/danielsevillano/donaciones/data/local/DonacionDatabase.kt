package io.github.danielsevillano.donaciones.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Donacion::class],
    version = 1
)
@TypeConverters(Conversores::class)
abstract class DonacionDatabase : RoomDatabase() {
    abstract fun dao(): DonacionDao
}