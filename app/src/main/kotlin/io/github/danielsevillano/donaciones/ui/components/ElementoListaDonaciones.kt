package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.danielsevillano.donaciones.data.local.Donacion
import io.github.danielsevillano.donaciones.ui.models.Posicion
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

@Composable
fun ElementoListaDonaciones(
    modifier: Modifier = Modifier,
    donacion: Donacion,
    indice: Int,
    total: Int,
    eliminarDonacion: () -> Unit
) {
    ListItemExpressive(
        headlineContent = {
            Text(text = donacion.lugar)
        },
        modifier = modifier,
        overlineContent = {
            Text(
                text = donacion.fecha.format(format = LocalDate.Format {
                    day(); char(value = '/'); monthNumber(); char(value = '/'); year()
                })
            )
        },
        supportingContent = {
            donacion.nota?.let { texto ->
                Text(text = texto)
            }
        },
        trailingContent = {
            IconButton(
                onClick = eliminarDonacion
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Eliminar"
                )
            }
        },
        position = if (total == 1) Posicion.Unica
        else if (indice == 0) Posicion.Inicial
        else if (indice == total - 1) Posicion.Final
        else Posicion.Media
    )
}