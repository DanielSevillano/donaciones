package io.github.danielsevillano.donaciones.ui.screens

import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import io.github.danielsevillano.donaciones.AppViewModel
import io.github.danielsevillano.donaciones.data.local.ColectaDao
import io.github.danielsevillano.donaciones.data.local.DonacionDao
import io.github.danielsevillano.donaciones.dataStore
import io.github.danielsevillano.donaciones.domain.Provincia
import io.github.danielsevillano.donaciones.ui.navigation.Rutas
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@Composable
fun Navegacion(
    backStack: NavBackStack<NavKey>,
    viewModel: AppViewModel,
    colectaDao: ColectaDao,
    donacionDao: DonacionDao,
    provincia: Provincia,
    modificarProvincia: (Provincia) -> Unit,
    municipio: String?,
    modificarMunicipio: (String?) -> Unit,
    scaffoldPadding: PaddingValues
) {
    val context = LocalContext.current
    val dataStore = context.dataStore

    LaunchedEffect(key1 = provincia) {
        viewModel.obtenerColectas(
            provincia = provincia,
            dao = colectaDao
        )
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        transitionSpec = {
            materialFadeThroughIn() togetherWith materialFadeThroughOut()
        },
        popTransitionSpec = {
            materialFadeThroughIn() togetherWith materialFadeThroughOut()
        },
        predictivePopTransitionSpec = {
            materialFadeThroughIn() togetherWith materialFadeThroughOut()
        },
        entryProvider = entryProvider {
            entry<Rutas.Inicio> {
                Inicio(
                    colectas = viewModel.colectas,
                    abrirColectas = {
                        backStack.add(Rutas.Colectas)
                        if (backStack.size == 3) backStack.removeAt(index = 1)
                    },
                    modificarMunicipio = modificarMunicipio,
                    cargando = viewModel.cargando,
                    error = viewModel.error,
                    recargar = {
                        viewModel.obtenerColectas(
                            provincia = provincia,
                            dao = colectaDao
                        )
                    },
                    scaffoldPadding = scaffoldPadding
                )
            }

            entry<Rutas.Colectas> {
                Colectas(
                    colectas = viewModel.colectas,
                    municipioSeleccionado = municipio,
                    modificarMunicipio = modificarMunicipio,
                    cargando = viewModel.cargando,
                    error = viewModel.error,
                    recargar = {
                        viewModel.obtenerColectas(
                            provincia = provincia,
                            dao = colectaDao
                        )
                    },
                    scaffoldPadding = scaffoldPadding
                )
            }

            entry<Rutas.Perfil> {
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
                    modificarProvincia = modificarProvincia,
                    scaffoldPadding = scaffoldPadding
                )
            }
        }
    )
}