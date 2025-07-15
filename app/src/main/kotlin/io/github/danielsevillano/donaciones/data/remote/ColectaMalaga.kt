package io.github.danielsevillano.donaciones.data.remote

import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.domain.Provincia
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.todayIn
import java.time.format.DateTimeParseException
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

data class ColectaMalaga(
    var fecha: String = "",
    var municipio: String = "",
    var datos: String = ""
) {
    private fun separarDatos(texto: String): Pair<String, String>? {
        val regex = Regex(pattern = """^(.*?) DE (\d+(,\d+)? A \d+(,\d+)?) H""")
        val matchResult = regex.find(input = texto)

        return if (matchResult != null) {
            val lugar = matchResult.groupValues[1].trim()
            val hora = matchResult.groupValues[2].trim()
            Pair(first = lugar, second = hora)
        } else null
    }

    @OptIn(ExperimentalTime::class)
    private fun obtenerFecha(fecha: String): LocalDate {
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

    val lugar = separarDatos(texto = datos)?.first ?: ""
    val hora = separarDatos(texto = datos)?.second
        ?.lowercase()
        ?.replace(oldValue = ",", newValue = ":") ?: ""

    val colecta = Colecta(
        provincia = Provincia.Malaga,
        lugar = Formato().formatear(texto = lugar),
        municipio = Formato().formatear(texto = municipio),
        fecha = obtenerFecha(fecha),
        hora = hora
    )
}