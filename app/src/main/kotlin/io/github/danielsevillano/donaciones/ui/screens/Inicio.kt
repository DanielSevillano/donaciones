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
import androidx.compose.material3.Scaffold
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
import io.github.danielsevillano.donaciones.ui.components.GrupoColectasDiarias
import io.github.danielsevillano.donaciones.ui.components.MensajeError
import io.github.danielsevillano.donaciones.ui.components.Subencabezado
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(
    ExperimentalTime::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun Inicio(
    colectas: List<Colecta>?,
    cargando: Boolean,
    error: Boolean,
    recargar: suspend () -> Unit,
    scaffoldPadding: PaddingValues
) {
    val colectasAgrupadas =
        colectas?.groupBy { it.municipio + it.lugar + it.fecha.dayOfYear }?.values?.toList()
            ?: emptyList()
    val colectasHoy = colectasAgrupadas.filter { it.first().diasRestantes == 0 }

    val colectasSemana = colectas?.filter {
        (it.diasRestantes <= 7) && (it.fecha.dayOfWeek > Clock.System.todayIn(
            TimeZone.currentSystemDefault()
        ).dayOfWeek)
    }?.groupBy { it.fecha.dayOfYear }?.values?.toList() ?: emptyList()

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
                        item(key = "donacionesHoy") {
                            Subencabezado(
                                titulo = "Donaciones hoy",
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                        itemsIndexed(
                            items = colectasHoy,
                            key = { indice, grupo -> "${grupo.first().lugar} (${grupo.first().municipio})" }
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

                    if (colectasSemana.isNotEmpty()) {
                        item(key = "donacionesSemana") {
                            Subencabezado(
                                titulo = "Donaciones esta semana",
                                modifier = Modifier.padding(top = 22.dp, bottom = 10.dp)
                            )
                        }

                        itemsIndexed(
                            items = colectasSemana,
                            key = { indice, colectasDiarias -> colectasDiarias.first().fecha }
                        ) { indice, colectasDiarias ->
                            GrupoColectasDiarias(
                                modifier = Modifier.padding(
                                    bottom = if (indice == colectasSemana.size - 1) 0.dp else 16.dp
                                ),
                                colectasDiarias = colectasDiarias
                            )
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