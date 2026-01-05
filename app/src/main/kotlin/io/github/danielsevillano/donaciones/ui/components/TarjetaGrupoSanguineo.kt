package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.danielsevillano.donaciones.domain.GrupoSanguineo

@Composable
fun TarjetaGrupoSanguineo(
    modifier: Modifier = Modifier,
    grupoSanguineo: GrupoSanguineo
) {
    Card(
        modifier = modifier.aspectRatio(ratio = 1F),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = if (grupoSanguineo == GrupoSanguineo.Desconocido) {
                CardDefaults.cardColors().containerColor
            } else MaterialTheme.colorScheme.errorContainer,
            contentColor = if (grupoSanguineo == GrupoSanguineo.Desconocido) {
                CardDefaults.cardColors().contentColor
            } else MaterialTheme.colorScheme.onErrorContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "grupo sanguíneo",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp),
                style = MaterialTheme.typography.labelMedium
            )

            if (grupoSanguineo == GrupoSanguineo.Desconocido) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(size = 80.dp)
                        .padding(top = 20.dp)
                )
            } else {
                Text(
                    text = grupoSanguineo.codigo,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 20.dp),
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }
    }
}