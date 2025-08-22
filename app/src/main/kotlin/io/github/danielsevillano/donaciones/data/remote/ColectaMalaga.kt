package io.github.danielsevillano.donaciones.data.remote

import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.domain.Provincia

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

    val lugar = separarDatos(texto = datos)?.first ?: ""
    val hora = separarDatos(texto = datos)?.second
        ?.lowercase()
        ?.replace(oldValue = ",", newValue = ":") ?: ""

    val colecta = Colecta(
        provincia = Provincia.Malaga,
        lugar = Formato.formatear(texto = lugar),
        municipio = Formato.formatear(texto = municipio),
        fecha = Utilidades.obtenerFecha(fecha),
        hora = hora
    )
}