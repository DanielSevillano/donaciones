package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.danielsevillano.donaciones.data.local.Donacion
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
    SegmentedListItem(
        shapes = ListItemDefaults.segmentedShapes(
            index = indice,
            count = total,
            defaultShapes = if (total == 1) ListItemDefaults.shapes(
                shape = MaterialTheme.shapes.large
            ) else ListItemDefaults.shapes()
        ),
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
        colors = ListItemDefaults.segmentedColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Text(text = donacion.lugar)
    }
}