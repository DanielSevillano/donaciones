package io.github.danielsevillano.donaciones.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.ui.components.ElementoColecta
import io.github.danielsevillano.donaciones.ui.components.Encabezado
import io.github.danielsevillano.donaciones.ui.components.GrupoColectasDiarias
import io.github.danielsevillano.donaciones.ui.components.MensajeError
import io.github.danielsevillano.donaciones.ui.components.Subencabezado
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun Inicio(
    colectas: List<Colecta>?,
    cargando: Boolean,
    error: Boolean,
    recargar: suspend () -> Unit
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

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 2.dp)
    ) {
        item(key = "encabezado") {
            Encabezado(
                titulo = "¡Bienvenido!",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (colectas != null) {
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
                        horas = grupo.map { it.hora }.sorted(),
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

                items(
                    items = colectasSemana,
                    key = { it.first().fecha }
                ) { colectasDiarias ->
                    GrupoColectasDiarias(
                        modifier = Modifier.padding(bottom = 16.dp),
                        colectasDiarias = colectasDiarias
                    )
                }
            }
        }

        if (cargando) {
            item(key = "carga") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
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