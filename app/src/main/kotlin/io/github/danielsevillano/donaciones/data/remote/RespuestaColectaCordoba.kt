package io.github.danielsevillano.donaciones.data.remote

import kotlinx.serialization.Serializable

@Serializable
class RespuestaColectaCordoba(
    val colectas: List<ColectaCordoba>
)