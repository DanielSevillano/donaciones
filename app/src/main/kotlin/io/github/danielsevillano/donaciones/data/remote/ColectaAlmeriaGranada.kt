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

class ColectaAlmeriaGranada(
    val provincia: Provincia,
    val fecha: String = "",
    val municipio: String = "",
    val datos: String = ""
) {
    private fun separarDatos(texto: String): Pair<String, String>? {
        val regex = Regex(pattern = """^LUGAR: (.*) HORARIO: (.*)$""")
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

    private fun obtenerHora(): String? {
        val textoHora = separarDatos(texto = datos)?.second ?: ""
        val textoFormateado = textoHora
            .uppercase()
            .replace(oldValue = "'", newValue = ",")
            .replace(oldValue = "-", newValue = " A ")

        val regexes = listOf(
            Regex(pattern = """DE (\d+(,\d+)? A \d+(,\d+)?) H"""),
            Regex(pattern = """DE (\d+ A \d+ Y \d+ A \d+) H"""),
            Regex(pattern = """(\d+(,\d+)? A \d+(,\d+)?) H""")
        )

        regexes.forEach { regex ->
            val matchResult = regex.find(input = textoFormateado)
            if (matchResult != null) return matchResult.groupValues[1]
                .lowercase()
                .replace(oldValue = ",", newValue = ":")
        }

        return null
    }

    val lugar = separarDatos(texto = datos)?.first ?: ""

    val colecta = Colecta(
        provincia = provincia,
        lugar = Formato().formatear(texto = lugar),
        municipio = Formato().formatear(texto = municipio),
        fecha = obtenerFecha(fecha = fecha),
        hora = obtenerHora() ?: ""
    )
}