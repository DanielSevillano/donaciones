package io.github.danielsevillano.donaciones.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity
data class Donacion(
    @PrimaryKey val fecha: LocalDate,
    val lugar: String,
    val nota: String? = null
)