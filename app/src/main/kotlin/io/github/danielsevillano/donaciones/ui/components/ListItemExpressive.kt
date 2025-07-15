package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import io.github.danielsevillano.donaciones.ui.models.Posicion

@Composable
fun ListItemExpressive(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    position: Posicion
) {
    ListItem(
        headlineContent = headlineContent,
        modifier = modifier.clip(
            shape = when (position) {
                Posicion.Unica -> MaterialTheme.shapes.large
                Posicion.Media -> MaterialTheme.shapes.extraSmall
                Posicion.Inicial -> MaterialTheme.shapes.large.copy(
                    bottomStart = MaterialTheme.shapes.extraSmall.bottomStart,
                    bottomEnd = MaterialTheme.shapes.extraSmall.bottomEnd
                )

                Posicion.Final -> MaterialTheme.shapes.large.copy(
                    topStart = MaterialTheme.shapes.extraSmall.topStart,
                    topEnd = MaterialTheme.shapes.extraSmall.topEnd
                )
            }
        ),
        overlineContent = overlineContent,
        supportingContent = supportingContent,
        leadingContent = leadingContent,
        trailingContent = trailingContent,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            headlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
            leadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            overlineColor = MaterialTheme.colorScheme.onSecondaryContainer,
            supportingColor = MaterialTheme.colorScheme.onSecondaryContainer,
            trailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )
}