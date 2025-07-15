package io.github.danielsevillano.donaciones

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import io.github.danielsevillano.donaciones.data.local.ColectaDatabase
import io.github.danielsevillano.donaciones.data.local.Conversores
import io.github.danielsevillano.donaciones.data.local.DonacionDatabase
import io.github.danielsevillano.donaciones.domain.Provincia
import io.github.danielsevillano.donaciones.ui.components.BarraNavegacion
import io.github.danielsevillano.donaciones.ui.screens.Navegacion
import io.github.danielsevillano.donaciones.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private val colectas by lazy {
        Room.databaseBuilder(
            context = applicationContext,
            klass = ColectaDatabase::class.java,
            name = "colectas"
        ).build()
    }

    private val donaciones by lazy {
        Room.databaseBuilder(
            context = applicationContext,
            klass = DonacionDatabase::class.java,
            name = "donaciones"
        ).build()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                var provinciaSeleccionada: Provincia by rememberSaveable { mutableStateOf(value = Provincia.Malaga) }
                val colectaDao = colectas.dao()
                val donacionDao = donaciones.dao()

                val navController = rememberNavController()
                val viewModel = viewModel<AppViewModel>()

                LaunchedEffect(key1 = Unit) {
                    val nombreProvincia = viewModel.obtenerDato(
                        dataStore = dataStore,
                        key = "provincia"
                    ) ?: Provincia.Malaga.nombre

                    val provincia = Conversores().nombreAProvincia(nombre = nombreProvincia)
                    provinciaSeleccionada = provincia
                }

                Scaffold(
                    bottomBar = {
                        BarraNavegacion(navController = navController)
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Navegacion(
                            navController = navController,
                            viewModel = viewModel,
                            colectaDao = colectaDao,
                            donacionDao = donacionDao,
                            provincia = provinciaSeleccionada,
                            modificarProvincia = { provincia ->
                                provinciaSeleccionada = provincia
                                viewModel.colectas = null
                            }
                        )
                    }
                }
            }
        }
    }
}

val Context.dataStore by preferencesDataStore(name = "preferences")