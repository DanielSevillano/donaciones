package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.danielsevillano.donaciones.Destino

@Composable
fun BarraNavegacion(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destinoActual = navBackStackEntry?.destination

    NavigationBar {
        Destino.entries.forEach { destino ->
            val destinoSeleccionado =
                destinoActual?.hierarchy?.any { it.route == destino.ruta } == true

            NavigationBarItem(
                selected = destinoSeleccionado,
                onClick = {
                    if (!destinoSeleccionado) {
                        navController.navigate(route = destino.ruta) {
                            popUpTo(id = navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (destinoSeleccionado) destino.iconoSeleccionado else destino.icono,
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