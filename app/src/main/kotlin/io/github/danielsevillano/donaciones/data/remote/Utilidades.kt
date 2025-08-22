package io.github.danielsevillano.donaciones.data.remote

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.todayIn
import java.time.format.DateTimeParseException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object Utilidades {
    @OptIn(ExperimentalTime::class)
    fun obtenerFecha(fecha: String): LocalDate {
        return try {
            LocalDate.parse(
                input = fecha,
                format = LocalDate.Format {
                    day(); char(value = '/'); monthNumber(); char(value = '/'); year()
                }
            )
        } catch (_: DateTimeParseException) {
            Clock.System.todayIn(TimeZone.currentSystemDefault())
        }
    }
}