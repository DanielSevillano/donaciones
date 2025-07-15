package io.github.danielsevillano.donaciones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.data.local.ColectaDao
import io.github.danielsevillano.donaciones.data.remote.Scrape
import io.github.danielsevillano.donaciones.domain.Provincia
import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AppViewModel() : ViewModel() {
    var colectas: List<Colecta>? by mutableStateOf(value = null)

    @OptIn(ExperimentalTime::class)
    suspend fun obtenerColectas(
        provincia: Provincia,
        dao: ColectaDao
    ) {
        colectas = try {
            dao.obtenerColectas(provincia = provincia).first()
        } catch (_: Exception) {
            emptyList()
        }

        val respuesta = try {
            Scrape().obtenerColectas(provincia).filter { it.diasRestantes >= 0 }
        } catch (_: Exception) {
            emptyList()
        }

        if (respuesta.isNotEmpty()) {
            colectas = respuesta
            dao.guardarColectas(colectas = respuesta)
        }

        dao.eliminarColectasPasadas(fecha = Clock.System.todayIn(TimeZone.Companion.currentSystemDefault()))
    }

    suspend fun obtenerDato(
        dataStore: DataStore<Preferences>,
        key: String
    ): String? {
        val dataStoreKey = stringPreferencesKey(name = key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }

    suspend fun guardarDato(
        dataStore: DataStore<Preferences>,
        key: String,
        value: String
    ) {
        val dataStoreKey = stringPreferencesKey(name = key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }
}