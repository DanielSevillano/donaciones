package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosColectas(
    municipios: List<String>,
    municipioSeleccionado: String?,
    seleccionarMunicipo: (String?) -> Unit,
    cerrarFiltros: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = cerrarFiltros,
        sheetState = rememberBottomSheetState(
            initialValue = Hidden,
            enabledValues = setOf(Hidden, Expanded)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            Subencabezado(
                titulo = "Filtrar por localidades"
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                FilterChip(
                    selected = municipioSeleccionado == null,
                    onClick = { seleccionarMunicipo(null) },
                    label = { Text(text = "Todos") }
                )

                municipios.forEach { municipio ->
                    FilterChip(
                        selected = municipioSeleccionado == municipio,
                        onClick = { seleccionarMunicipo(municipio) },
                        label = { Text(text = municipio) }
                    )
                }
            }
        }
    }
}