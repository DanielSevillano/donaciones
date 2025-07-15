package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.danielsevillano.donaciones.domain.GrupoSanguineo
import io.github.danielsevillano.donaciones.domain.Provincia

@Composable
fun DialogoEdicionPerfil(
    nombre: String,
    provincia: String,
    grupoSanguineo: GrupoSanguineo,
    cerrarDialogo: () -> Unit,
    actualizarNombre: (String) -> Unit,
    actualizarProvincia: (String) -> Unit,
    actualizarGrupoSanguineo: (GrupoSanguineo) -> Unit
) {
    var nombreDialogo by rememberSaveable { mutableStateOf(value = nombre) }
    var provinciaDialogo by rememberSaveable { mutableStateOf(value = provincia) }
    var grupoDialogo by rememberSaveable { mutableStateOf(value = grupoSanguineo) }

    AlertDialog(
        onDismissRequest = cerrarDialogo,
        confirmButton = {
            TextButton(
                onClick = {
                    actualizarNombre(nombreDialogo)
                    actualizarProvincia(provinciaDialogo)
                    actualizarGrupoSanguineo(grupoDialogo)
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
            Text(text = "Modificar información")
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(state = rememberScrollState())
            ) {
                OutlinedTextField(
                    value = nombreDialogo,
                    onValueChange = { nombreDialogo = it },
                    label = {
                        Text(text = "Nombre")
                    },
                    isError = nombre.isEmpty(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(height = 16.dp))

                Text(
                    text = "Provincia",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                Provincia.entries.forEach { claseProvincia ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { provinciaDialogo = claseProvincia.nombre },
                        horizontalArrangement = Arrangement.spacedBy(space = ButtonDefaults.IconSpacing),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = provinciaDialogo == claseProvincia.nombre,
                            onClick = { provinciaDialogo = claseProvincia.nombre }
                        )

                        Text(text = claseProvincia.nombre)
                    }
                }

                Spacer(modifier = Modifier.height(height = 16.dp))

                Text(
                    text = "Grupo sanguíneo",
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                GrupoSanguineo.entries.forEach { grupo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { grupoDialogo = grupo },
                        horizontalArrangement = Arrangement.spacedBy(space = ButtonDefaults.IconSpacing),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = grupoDialogo == grupo,
                            onClick = { grupoDialogo = grupo }
                        )

                        Text(text = grupo.codigo)
                    }
                }
            }
        }
    )
}