package io.github.danielsevillano.donaciones.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.danielsevillano.donaciones.AppViewModel
import io.github.danielsevillano.donaciones.Destino
import io.github.danielsevillano.donaciones.data.local.ColectaDao
import io.github.danielsevillano.donaciones.data.local.DonacionDao
import io.github.danielsevillano.donaciones.dataStore
import io.github.danielsevillano.donaciones.domain.Provincia
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@Composable
fun Navegacion(
    navController: NavHostController,
    viewModel: AppViewModel,
    colectaDao: ColectaDao,
    donacionDao: DonacionDao,
    provincia: Provincia,
    modificarProvincia: (Provincia) -> Unit
) {
    val context = LocalContext.current
    val dataStore = context.dataStore

    LaunchedEffect(key1 = provincia) {
        viewModel.obtenerColectas(
            provincia = provincia,
            dao = colectaDao
        )
    }

    NavHost(
        navController = navController,
        startDestination = Destino.entries.first().ruta,
        enterTransition = { materialFadeThroughIn() },
        exitTransition = { materialFadeThroughOut() }
    ) {
        composable(route = Destino.Inicio.ruta) {
            Inicio(colectas = viewModel.colectas)
        }

        composable(route = Destino.Colectas.ruta) {
            Colectas(colectas = viewModel.colectas)
        }

        composable(route = Destino.Perfil.ruta) {
            Perfil(
                obtenerDato = { key ->
                    viewModel.obtenerDato(
                        dataStore = dataStore,
                        key = key
                    )
                },
                guardarDato = { key, value ->
                    viewModel.guardarDato(
                        dataStore = dataStore,
                        key = key,
                        value = value
                    )
                },
                flujoDonaciones = donacionDao.obtenerDonaciones(),
                insertarDonacion = { donacion ->
                    donacionDao.insertarDonacion(donacion = donacion)
                },
                eliminarDonacion = { donacion ->
                    donacionDao.eliminarDonacion(donacion = donacion)
                },
                modificarProvincia = modificarProvincia
            )
        }
    }
}