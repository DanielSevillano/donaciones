package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TarjetaProvincia(
    modifier: Modifier = Modifier,
    provincia: String
) {
    Card(
        modifier = modifier.aspectRatio(ratio = 1F),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "provincia",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp),
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = provincia,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 20.dp),
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}