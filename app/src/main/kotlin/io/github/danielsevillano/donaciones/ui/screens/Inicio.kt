package io.github.danielsevillano.donaciones.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.ui.components.ElementoColecta
import io.github.danielsevillano.donaciones.ui.components.MensajeError
import io.github.danielsevillano.donaciones.ui.components.Subencabezado
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(
    ExperimentalTime::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun Inicio(
    colectas: List<Colecta>?,
    abrirColectas: () -> Unit,
    modificarMunicipio: (String?) -> Unit,
    cargando: Boolean,
    error: Boolean,
    recargar: suspend () -> Unit,
    scaffoldPadding: PaddingValues
) {
    val colectasAgrupadas =
        colectas?.groupBy { it.municipio + it.lugar + it.fecha.dayOfYear }?.values?.toList()
            ?: emptyList()
    val colectasHoy = colectasAgrupadas.filter { it.first().diasRestantes == 0 }

    val municipios = colectas?.map { it.municipio }?.distinct()?.sorted() ?: emptyList()

    val scope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Inicio")
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
            modifier = Modifier.padding(paddingValues = paddingValues),
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.LoadingIndicator(
                    state = pullToRefreshState,
                    isRefreshing = cargando,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(space = 2.dp)
            ) {
                if (!colectas.isNullOrEmpty()) {
                    if (colectasHoy.isNotEmpty()) {
                        item(key = "tituloDonacionesHoy") {
                            Subencabezado(
                                titulo = "Donaciones hoy",
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                        itemsIndexed(
                            items = colectasHoy,
                            key = { _, grupo -> "${grupo.first().lugar} (${grupo.first().municipio})" }
                        ) { indice, grupo ->
                            ElementoColecta(
                                colecta = grupo.first(),
                                horas = grupo
                                    .map { it.hora }
                                    .filter { it.isNotBlank() }
                                    .sorted(),
                                indice = indice,
                                total = colectasHoy.size
                            )
                        }
                    }

                    if (municipios.isNotEmpty()) {
                        item(key = "tituloMunicipiosProgramados") {
                            Subencabezado(
                                titulo = "Municipios con colectas programadas",
                                modifier = Modifier.padding(
                                    top = if (colectasHoy.isNotEmpty()) 22.dp else 0.dp,
                                    bottom = 10.dp
                                )
                            )
                        }

                        itemsIndexed(
                            items = municipios,
                            key = { _, municipio -> municipio }
                        ) { indice, municipio ->
                            SegmentedListItem(
                                onClick = {
                                    modificarMunicipio(municipio)
                                    abrirColectas()
                                },
                                shapes = ListItemDefaults.segmentedShapes(
                                    index = indice,
                                    count = municipios.size,
                                    defaultShapes = if (municipios.size == 1) ListItemDefaults.shapes(
                                        shape = MaterialTheme.shapes.large
                                    ) else ListItemDefaults.shapes()
                                ),
                                colors = ListItemDefaults.segmentedColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                                )
                            ) {
                                Text(text = municipio)
                            }
                        }
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
    }
}