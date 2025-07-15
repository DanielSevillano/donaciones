package io.github.danielsevillano.donaciones.data.local

import androidx.room.TypeConverter
import io.github.danielsevillano.donaciones.domain.Provincia
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class Conversores {
    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun milisegundosAFecha(milisegundos: Long): LocalDate {
        return Instant.fromEpochMilliseconds(epochMilliseconds = milisegundos)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun fechaAMilisegundos(date: LocalDate): Long {
        return LocalDateTime(
            date = date,
            time = LocalTime(hour = 0, minute = 0, second = 0, nanosecond = 0)
        )
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    }

    @TypeConverter
    fun provinciaANombre(provincia: Provincia): String {
        return provincia.nombre
    }

    @TypeConverter
    fun nombreAProvincia(nombre: String): Provincia {
        return Provincia.entries.find { it.nombre == nombre } ?: Provincia.Malaga
    }
}