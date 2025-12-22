package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.danielsevillano.donaciones.ui.navigation.DestinosNavegacion
import io.github.danielsevillano.donaciones.ui.navigation.Rutas

@Composable
fun BarraNavegacion(backStack: NavBackStack<NavKey>) {

    NavigationBar {
        DestinosNavegacion.lista.forEach { destino ->
            val seleccionado = backStack.last() == destino.ruta

            NavigationBarItem(
                selected = seleccionado,
                onClick = {
                    if (!seleccionado) {
                        if (destino.ruta == Rutas.Inicio) backStack.removeLastOrNull()
                        else {
                            backStack.add(destino.ruta)
                            if (backStack.size == 3) backStack.removeAt(index = 1)
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (seleccionado) destino.iconoSeleccionado else destino.icono,
                        contentDescription = destino.nombre
                    )
                },
                label = {
                    Text(text = destino.nombre)
                }
            )
        }
    }
}