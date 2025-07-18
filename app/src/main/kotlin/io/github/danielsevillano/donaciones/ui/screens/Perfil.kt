package io.github.danielsevillano.donaciones.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.danielsevillano.donaciones.data.local.Conversores
import io.github.danielsevillano.donaciones.data.local.Donacion
import io.github.danielsevillano.donaciones.domain.GrupoSanguineo
import io.github.danielsevillano.donaciones.domain.Provincia
import io.github.danielsevillano.donaciones.ui.components.DialogoEdicionPerfil
import io.github.danielsevillano.donaciones.ui.components.DialogoNuevaDonacion
import io.github.danielsevillano.donaciones.ui.components.ElementoListaDonaciones
import io.github.danielsevillano.donaciones.ui.components.Encabezado
import io.github.danielsevillano.donaciones.ui.components.Subencabezado
import io.github.danielsevillano.donaciones.ui.components.TarjetaDiasParaDonar
import io.github.danielsevillano.donaciones.ui.components.TarjetaGrupoSanguineo
import io.github.danielsevillano.donaciones.ui.components.TarjetaNumeroDonaciones
import io.github.danielsevillano.donaciones.ui.components.TarjetaProvincia
import io.github.danielsevillano.donaciones.ui.models.BotonIcono
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Perfil(
    obtenerDato: suspend (String) -> String?,
    guardarDato: suspend (String, String) -> Unit,
    flujoDonaciones: Flow<List<Donacion>>,
    insertarDonacion: suspend (Donacion) -> Unit,
    eliminarDonacion: suspend (Donacion) -> Unit,
    modificarProvincia: (Provincia) -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf(value = "") }
    var provincia by rememberSaveable { mutableStateOf(value = Provincia.Malaga.nombre) }
    var grupoSanguineo: GrupoSanguineo by rememberSaveable { mutableStateOf(value = GrupoSanguineo.Desconocido) }
    var modoEdicion by rememberSaveable { mutableStateOf(value = false) }
    var nuevaDonacion by rememberSaveable { mutableStateOf(value = false) }
    var donaciones: List<Donacion> by rememberSaveable { mutableStateOf(value = emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        nombre = obtenerDato("nombre") ?: ""
        provincia = obtenerDato("provincia") ?: Provincia.Malaga.nombre
        val codigoSanguineo = obtenerDato("grupo")

        grupoSanguineo = GrupoSanguineo.entries.find { grupo ->
            grupo.codigo == codigoSanguineo
        } ?: GrupoSanguineo.Desconocido

        flujoDonaciones.collect { lista ->
            donaciones = lista
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(space = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
    ) {
        fun LazyGridItemScope.modificadorTarjeta(): Modifier {
            return Modifier
                .padding(vertical = 7.dp)
                .animateItem()
        }

        item(
            key = "encabezadoPerfil",
            span = { GridItemSpan(currentLineSpan = maxLineSpan) }
        ) {
            Encabezado(
                modifier = Modifier.padding(bottom = 7.dp),
                titulo = nombre.ifEmpty { "Perfil" },
                boton = BotonIcono(
                    accion = { modoEdicion = true },
                    icono = Icons.Outlined.Edit,
                    descripcion = "Editar perfil"
                )
            )
        }

        item(key = "tarjetaNumeroDonaciones") {
            TarjetaNumeroDonaciones(
                modifier = modificadorTarjeta(),
                numeroDonaciones = donaciones.size
            )
        }

        if (donaciones.isNotEmpty()) {
            item(key = "tarjetaDiasParaDonar") {
                TarjetaDiasParaDonar(
                    modifier = modificadorTarjeta(),
                    ultimaFecha = donaciones.first().fecha
                )
            }
        }

        if (grupoSanguineo != GrupoSanguineo.Desconocido) {
            item(key = "tarjetaGrupoSanguineo") {
                TarjetaGrupoSanguineo(
                    modifier = modificadorTarjeta(),
                    grupoSanguineo = grupoSanguineo
                )
            }
        }

        item(key = "tarjetaProvincia") {
            TarjetaProvincia(
                modifier = modificadorTarjeta(),
                provincia = provincia
            )
        }

        item(
            key = "encabezadoDonaciones",
            span = { GridItemSpan(currentLineSpan = maxLineSpan) }
        ) {
            Subencabezado(
                modifier = Modifier.padding(top = 16.dp),
                titulo = "Mis donaciones",
                boton = BotonIcono(
                    accion = { nuevaDonacion = true },
                    icono = Icons.Outlined.Add,
                    descripcion = "Añadir donación"
                )
            )
        }

        itemsIndexed(
            items = donaciones,
            key = { indice, donacion -> Conversores().fechaAMilisegundos(date = donacion.fecha) },
            span = { indice, donacion -> GridItemSpan(currentLineSpan = maxLineSpan) }
        ) { indice, donacion ->
            ElementoListaDonaciones(
                modifier = Modifier.animateItem(),
                donacion = donacion,
                indice = indice,
                total = donaciones.size,
                eliminarDonacion = {
                    scope.launch { eliminarDonacion(donacion) }
                }
            )
        }
    }

    if (modoEdicion) {
        DialogoEdicionPerfil(
            nombre = nombre,
            provincia = provincia,
            grupoSanguineo = grupoSanguineo,
            cerrarDialogo = { modoEdicion = false },
            actualizarNombre = { nombreDialogo ->
                nombre = nombreDialogo
                scope.launch {
                    guardarDato("nombre", nombreDialogo)
                }
            },
            actualizarProvincia = { provinciaDialogo ->
                provincia = provinciaDialogo
                scope.launch {
                    guardarDato("provincia", provinciaDialogo)
                }

                modificarProvincia(
                    Provincia.entries.find { it.nombre == provinciaDialogo } ?: Provincia.Malaga
                )
            },
            actualizarGrupoSanguineo = { grupoDialogo ->
                grupoSanguineo = grupoDialogo
                scope.launch {
                    guardarDato("grupo", grupoDialogo.codigo)
                }
            }
        )
    }

    if (nuevaDonacion) {
        DialogoNuevaDonacion(
            insertarDonacion = { donacion ->
                scope.launch { insertarDonacion(donacion) }
            },
            cerrarDialogo = { nuevaDonacion = false }
        )
    }
}