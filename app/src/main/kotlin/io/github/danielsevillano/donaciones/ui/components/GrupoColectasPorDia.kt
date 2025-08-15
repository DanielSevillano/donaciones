package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.danielsevillano.donaciones.data.local.Colecta
import kotlinx.datetime.number
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun GrupoColectasDiarias(
    modifier: Modifier = Modifier,
    colectasDiarias: List<Colecta>
) {
    val fecha = colectasDiarias.first().fecha
    val mes = Month.of(fecha.month.number).getDisplayName(
        TextStyle.SHORT,
        Locale.Builder()
            .setLanguage("es")
            .setRegion("ES")
            .build()
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 40.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = AnnotatedString.fromHtml(htmlString = "<b>${fecha.day}</b><br>$mes"),
                modifier = Modifier.padding(bottom = 2.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(lineHeight = 12.sp)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(space = 2.dp)
        ) {
            val colectasPorLugar = colectasDiarias
                .groupBy { it.municipio + it.lugar }.values.toList()

            colectasPorLugar.forEachIndexed { indice, grupo ->
                ElementoColecta(
                    colecta = grupo.first(),
                    horas = grupo
                        .map { it.hora }
                        .filter { it.isNotBlank() }
                        .sorted(),
                    indice = indice,
                    total = colectasPorLugar.size
                )
            }
        }
    }
}