package io.github.danielsevillano.donaciones

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Bloodtype
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destino(
    val ruta: String,
    val nombre: String,
    val icono: ImageVector,
    val iconoSeleccionado: ImageVector
) {
    Inicio(
        ruta = "inicio",
        nombre = "Inicio",
        icono = Icons.Outlined.Home,
        iconoSeleccionado = Icons.Filled.Home
    ),
    Colectas(
        ruta = "colectas",
        nombre = "Colectas",
        icono = Icons.Outlined.Bloodtype,
        iconoSeleccionado = Icons.Filled.Bloodtype
    ),
    Perfil(
        ruta = "perfil",
        nombre = "Perfil",
        icono = Icons.Outlined.AccountCircle,
        iconoSeleccionado = Icons.Filled.AccountCircle
    )
}