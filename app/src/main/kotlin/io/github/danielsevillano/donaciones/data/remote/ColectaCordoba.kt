package io.github.danielsevillano.donaciones.data.remote

import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.domain.Provincia
import kotlinx.serialization.Serializable

@Serializable
class ColectaCordoba(
    val fecha: String,
    val localidad: String,
    val lugar: String,
    val horario: String
) {
    val colecta
        get() = Colecta(
            provincia = Provincia.Cordoba,
            lugar = lugar,
            municipio = Formato.formatear(texto = localidad),
            fecha = Utilidades.obtenerFecha(fecha = fecha),
            hora = horario.replace(
                oldValue = "-",
                newValue = " a "
            )
        )
}