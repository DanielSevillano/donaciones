package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoneOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun TarjetaDiasParaDonar(
    modifier: Modifier = Modifier,
    ultimaFecha: LocalDate
) {
    val diasDesdeDonacion = ultimaFecha
        .daysUntil(other = Clock.System.todayIn(TimeZone.currentSystemDefault()))

    Card(
        modifier = modifier.aspectRatio(ratio = 1F),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(elevation = 1.dp),
            contentColor = if (diasDesdeDonacion >= 60) MaterialTheme.colorScheme.onSecondaryContainer else CardDefaults.cardColors().contentColor
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.extraLarge)
                    .fillMaxWidth()
                    .fillMaxHeight(fraction = if (diasDesdeDonacion < 60) diasDesdeDonacion / 60F else 1F)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .align(Alignment.BottomCenter)
            )

            Text(
                text = if (diasDesdeDonacion >= 60) "ya puedes donar" else "días para donar",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp),
                style = MaterialTheme.typography.labelMedium
            )

            if (diasDesdeDonacion < 60) {
                Text(
                    text = "${60 - diasDesdeDonacion}",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 20.dp),
                    style = MaterialTheme.typography.displayLarge
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.DoneOutline,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(size = 80.dp)
                        .padding(top = 20.dp)
                )
            }
        }
    }
}