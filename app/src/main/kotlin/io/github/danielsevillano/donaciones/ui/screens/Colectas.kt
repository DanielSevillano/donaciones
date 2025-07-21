package io.github.danielsevillano.donaciones.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.ui.components.FiltrosColectas
import io.github.danielsevillano.donaciones.ui.components.GrupoColectasDiarias
import io.github.danielsevillano.donaciones.ui.components.MensajeError
import io.github.danielsevillano.donaciones.ui.components.TooltipIconButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Colectas(
    colectas: List<Colecta>?,
    cargando: Boolean,
    error: Boolean,
    recargar: suspend () -> Unit,
    scaffoldPadding: PaddingValues
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Colectas")
                },
                actions = {
                    TooltipIconButton(
                        description = "Filtros",
                        onClick = { mostrarFiltros = true },
                        icon = Icons.Outlined.FilterList,
                        badge = municipioSeleccionado != null,
                        inTopBar = true
                    )
                },
                scrollBehavior = scrollBehavior
            )
        },
        contentWindowInsets = WindowInsets(bottom = scaffoldPadding.calculateBottomPadding())
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = cargando,
            onRefresh = {
                scope.launch { recargar() }
            },
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
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
}