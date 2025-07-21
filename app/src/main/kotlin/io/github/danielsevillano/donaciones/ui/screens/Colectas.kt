package io.github.danielsevillano.donaciones.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.ui.components.Encabezado
import io.github.danielsevillano.donaciones.ui.components.FiltrosColectas
import io.github.danielsevillano.donaciones.ui.components.GrupoColectasDiarias
import io.github.danielsevillano.donaciones.ui.components.MensajeError
import io.github.danielsevillano.donaciones.ui.models.BotonIcono
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Colectas(
    colectas: List<Colecta>?,
    cargando: Boolean,
    error: Boolean,
    recargar: suspend () -> Unit
) {
    val municipios = colectas?.map { it.municipio }?.distinct() ?: emptyList()
    var municipioSeleccionado: String? by rememberSaveable { mutableStateOf(value = null) }

    val colectasFiltradas =
        if (municipioSeleccionado == null) colectas
            ?: emptyList() else colectas?.filter { it.municipio == municipioSeleccionado }
            ?: emptyList()
    val listaColectasPorDia = colectasFiltradas.groupBy { it.fecha.dayOfYear }.values.toList()

    var mostrarFiltros by rememberSaveable { mutableStateOf(value = false) }
    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = cargando,
        onRefresh = {
            scope.launch { recargar() }
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            item(key = "encabezado") {
                Encabezado(
                    titulo = "Colectas",
                    boton = BotonIcono(
                        accion = { mostrarFiltros = true },
                        descripcion = "Filtros",
                        icono = Icons.Outlined.FilterList,
                        distintivo = municipioSeleccionado != null
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (!colectas.isNullOrEmpty()) {
                items(
                    items = listaColectasPorDia,
                    key = { it.first().fecha }
                ) { colectasDiarias ->
                    GrupoColectasDiarias(
                        modifier = Modifier.animateItem(),
                        colectasDiarias = colectasDiarias
                    )
                }
            } else if (error) {
                item(key = "error") {
                    MensajeError(
                        recargar = {
                            scope.launch { recargar() }
                        }
                    )
                }
            }
        }
    }

    if (mostrarFiltros) {
        FiltrosColectas(
            municipios = municipios.sorted(),
            municipioSeleccionado = municipioSeleccionado,
            seleccionarMunicipo = { municipio -> municipioSeleccionado = municipio },
            cerrarFiltros = { mostrarFiltros = false }
        )
    }
}