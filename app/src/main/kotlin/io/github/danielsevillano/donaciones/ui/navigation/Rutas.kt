package io.github.danielsevillano.donaciones.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Rutas {
    @Serializable
    data object Inicio : NavKey

    @Serializable
    data object Colectas : NavKey

    @Serializable
    data object Perfil : NavKey
}