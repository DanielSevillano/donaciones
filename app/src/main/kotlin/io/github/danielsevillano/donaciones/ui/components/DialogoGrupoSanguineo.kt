package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.danielsevillano.donaciones.domain.GrupoSanguineo

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DialogoGrupoSanguineo(
    grupoSanguineo: GrupoSanguineo,
    cerrarDialogo: () -> Unit,
    actualizarGrupoSanguineo: (GrupoSanguineo) -> Unit
) {
    var valorGrupoSanguineo by rememberSaveable { mutableStateOf(value = grupoSanguineo) }

    AlertDialog(
        onDismissRequest = cerrarDialogo,
        confirmButton = {
            TextButton(
                onClick = {
                    actualizarGrupoSanguineo(valorGrupoSanguineo)
                    cerrarDialogo()
                }
            ) {
                Text(text = "Guardar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = cerrarDialogo
            ) {
                Text(text = "Cancelar")
            }
        },
        title = {
            Text(text = "Grupo sanguíneo")
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(space = 2.dp)
            ) {
                GrupoSanguineo.entries.forEachIndexed { indice, grupo ->
                    SegmentedListItem(
                        onClick = { valorGrupoSanguineo = grupo },
                        shapes = ListItemDefaults.segmentedShapes(
                            index = indice,
                            count = GrupoSanguineo.entries.size
                        ),
                        selected = valorGrupoSanguineo == grupo,
                        colors = ListItemDefaults.segmentedColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(text = grupo.codigo)
                    }
                }
            }
        }
    )
}