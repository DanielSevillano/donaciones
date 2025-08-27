package io.github.danielsevillano.donaciones.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Bloodtype
import androidx.compose.material.icons.outlined.Home

object DestinosNavegacion {
    val lista = listOf(
        DestinoNavegacion(
            ruta = Rutas.Inicio,
            nombre = "Inicio",
            icono = Icons.Outlined.Home,
            iconoSeleccionado = Icons.Filled.Home
        ),
        DestinoNavegacion(
            ruta = Rutas.Colectas,
            nombre = "Colectas",
            icono = Icons.Outlined.Bloodtype,
            iconoSeleccionado = Icons.Filled.Bloodtype
        ),
        DestinoNavegacion(
            ruta = Rutas.Perfil,
            nombre = "Perfil",
            icono = Icons.Outlined.AccountCircle,
            iconoSeleccionado = Icons.Filled.AccountCircle
        )
    )
}