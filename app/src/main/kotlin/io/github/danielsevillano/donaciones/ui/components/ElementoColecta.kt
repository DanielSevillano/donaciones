package io.github.danielsevillano.donaciones.ui.components

import android.content.Intent
import android.provider.CalendarContract
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.ui.models.Posicion
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ElementoColecta(
    colecta: Colecta,
    horas: List<String>,
    indice: Int,
    total: Int
) {
    val contexto = LocalContext.current

    ListItemExpressive(
        headlineContent = {
            Text(text = colecta.lugar)
        },
        overlineContent = {
            Text(
                text = colecta.municipio,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        },
        supportingContent = {
            if (horas.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                    horas.forEach { hora ->
                        AssistChip(
                            onClick = {
                                val milisegundos = colecta.fecha
                                    .atTime(hour = 12, minute = 0)
                                    .toInstant(TimeZone.currentSystemDefault())
                                    .toEpochMilliseconds()

                                val intent = Intent(Intent.ACTION_INSERT)
                                    .setData(CalendarContract.Events.CONTENT_URI)
                                    .putExtra(CalendarContract.Events.TITLE, "Colecta de sangre")
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, milisegundos)
                                    .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
                                    .putExtra(
                                        CalendarContract.Events.EVENT_LOCATION,
                                        "${colecta.lugar}, ${colecta.municipio}"
                                    )
                                contexto.startActivity(intent)
                            },
                            label = { Text(text = hora) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95F),
                                labelColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        },
        position = if (total == 1) Posicion.Unica
        else if (indice == 0) Posicion.Inicial
        else if (indice == total - 1) Posicion.Final
        else Posicion.Media
    )
}