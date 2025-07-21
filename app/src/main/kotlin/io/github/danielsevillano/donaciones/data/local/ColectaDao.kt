package io.github.danielsevillano.donaciones.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import io.github.danielsevillano.donaciones.domain.Provincia
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface ColectaDao {
    @Upsert
    suspend fun guardarColectas(colectas: List<Colecta>)

    @Query("DELETE FROM colecta WHERE fecha < :fecha")
    suspend fun eliminarColectasPasadas(fecha: LocalDate)

    @Query("SELECT * FROM colecta WHERE provincia == :provincia ORDER BY fecha ASC")
    fun obtenerColectas(provincia: Provincia): Flow<List<Colecta>>
}