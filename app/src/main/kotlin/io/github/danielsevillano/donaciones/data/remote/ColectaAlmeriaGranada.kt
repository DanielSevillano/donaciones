package io.github.danielsevillano.donaciones.data.remote

import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.domain.Provincia

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

    private fun obtenerHora(): String {
        val textoHora = separarDatos(texto = datos)?.second ?: ""
        val textoFormateado = textoHora
            .uppercase()
            .replace(oldValue = ".", newValue = ",")
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

        return ""
    }

    val lugar = separarDatos(texto = datos)?.first

    val colecta = Colecta(
        provincia = provincia,
        lugar = if (lugar.isNullOrBlank()) {
            Formato.formatear(texto = municipio)
        } else Formato.formatear(texto = lugar),
        municipio = Formato.formatear(texto = municipio),
        fecha = Utilidades.obtenerFecha(fecha = fecha),
        hora = obtenerHora()
    )
}