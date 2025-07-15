package io.github.danielsevillano.donaciones.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DonacionDao {
    @Upsert
    suspend fun insertarDonacion(donacion: Donacion)

    @Delete
    suspend fun eliminarDonacion(donacion: Donacion)

    @Query("SELECT * FROM donacion ORDER BY fecha DESC")
    fun obtenerDonaciones(): Flow<List<Donacion>>
}