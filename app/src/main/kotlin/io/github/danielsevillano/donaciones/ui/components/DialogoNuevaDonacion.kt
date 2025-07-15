package io.github.danielsevillano.donaciones.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.danielsevillano.donaciones.data.local.Conversores
import io.github.danielsevillano.donaciones.data.local.Donacion
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun DialogoNuevaDonacion(
    insertarDonacion: (Donacion) -> Unit,
    cerrarDialogo: () -> Unit
) {
    var seleccionFecha by rememberSaveable { mutableStateOf(value = true) }
    var milisegundosFecha: Long by rememberSaveable {
        mutableLongStateOf(value = Clock.System.now().toEpochMilliseconds())
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= Clock.System.now().toEpochMilliseconds()
            }
        }
    )

    var lugar by rememberSaveable { mutableStateOf(value = "") }
    var nota by rememberSaveable { mutableStateOf(value = "") }

    if (seleccionFecha) {
        DatePickerDialog(
            onDismissRequest = cerrarDialogo,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { milisegundos ->
                            seleccionFecha = false
                            milisegundosFecha = milisegundos
                        }
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text(text = "Guardar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    } else {
        val fecha = Conversores().milisegundosAFecha(milisegundos = milisegundosFecha)

        AlertDialog(
            onDismissRequest = cerrarDialogo,
            confirmButton = {
                TextButton(
                    onClick = {
                        cerrarDialogo()
                        insertarDonacion(
                            Donacion(
                                fecha = fecha,
                                lugar = lugar,
                                nota = nota.ifEmpty { null }
                            )
                        )
                    },
                    enabled = lugar.isNotEmpty()
                ) {
                    Text(text = "Guardar")
                }
            },
            title = {
                Text(text = "Nueva donación")
            },
            text = {
                val fechaFormateada = fecha.format(format = LocalDate.Format {
                    day(); char(value = '/'); monthNumber(); char(value = '/'); year()
                })

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = fechaFormateada)

                        IconButton(
                            onClick = { seleccionFecha = true }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = "Abrir calendario"
                            )
                        }
                    }

                    OutlinedTextField(
                        value = lugar,
                        onValueChange = { lugar = it },
                        label = {
                            Text(text = "Lugar")
                        },
                        isError = lugar.isEmpty(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = nota,
                        onValueChange = { nota = it },
                        label = {
                            Text(text = "Nota (opcional)")
                        },
                        singleLine = true
                    )
                }
            }
        )
    }
}