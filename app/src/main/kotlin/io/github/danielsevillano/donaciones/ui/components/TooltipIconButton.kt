package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipIconButton(
    description: String,
    onClick: () -> Unit,
    icon: ImageVector,
    badge: Boolean = false,
    inTopBar: Boolean = false
) {
    TooltipBox(
        positionProvider = rememberTooltipPosition(inTopBar = inTopBar),
        tooltip = {
            PlainTooltip {
                Text(text = description)
            }
        },
        state = rememberTooltipState()
    ) {
        IconButton(onClick = onClick) {
            BadgedBox(
                badge = {
                    if (badge) Badge()
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = description
                )
            }
        }
    }
}

@Composable
fun rememberTooltipPosition(inTopBar: Boolean): PopupPositionProvider {
    val tooltipAnchorSpacing = with(receiver = LocalDensity.current) { 4.dp.roundToPx() }
    return remember(key1 = tooltipAnchorSpacing) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val x = anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
                val y =
                    if (inTopBar) anchorBounds.bottom + tooltipAnchorSpacing else anchorBounds.top - popupContentSize.height - tooltipAnchorSpacing
                return IntOffset(x, y)
            }
        }
    }
}