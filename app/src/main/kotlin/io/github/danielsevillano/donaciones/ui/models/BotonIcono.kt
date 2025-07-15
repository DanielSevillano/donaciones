package io.github.danielsevillano.donaciones.ui.models

import androidx.compose.ui.graphics.vector.ImageVector

class BotonIcono(
    val accion: () -> Unit,
    val descripcion: String,
    val icono: ImageVector,
    val distintivo: Boolean = false
)