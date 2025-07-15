package io.github.danielsevillano.donaciones.data.local

import androidx.room.Entity
import androidx.room.Ignore
import io.github.danielsevillano.donaciones.domain.Provincia
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(
    primaryKeys = ["fecha", "lugar", "hora"]
)
data class Colecta(
    val provincia: Provincia,
    val lugar: String,
    val municipio: String,
    val fecha: LocalDate,
    val hora: String
) {
    @OptIn(ExperimentalTime::class)
    @Ignore
    val diasRestantes =
        Clock.System.todayIn(TimeZone.Companion.currentSystemDefault()).daysUntil(other = fecha)
}