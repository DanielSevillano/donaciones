package io.github.danielsevillano.donaciones.ui.navigation

import kotlinx.serialization.Serializable

sealed class Rutas {
    @Serializable
    data object Inicio

    @Serializable
    data object Colectas

    @Serializable
    data object Perfil
}