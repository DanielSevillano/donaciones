package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.danielsevillano.donaciones.ui.models.BotonIcono

@Composable
fun Subencabezado(
    modifier: Modifier = Modifier,
    titulo: String,
    boton: BotonIcono? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleLarge
        )

        boton?.let {
            TooltipIconButton(
                description = boton.descripcion,
                onClick = boton.accion,
                icon = boton.icono
            )
        }
    }
}