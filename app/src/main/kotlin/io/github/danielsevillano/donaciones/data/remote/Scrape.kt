package io.github.danielsevillano.donaciones.data.remote

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.domain.Provincia

object Scrape {
    suspend fun obtenerColectas(provincia: Provincia): List<Colecta> {
        val documento = Ksoup.parseGetRequest(url = provincia.url)
        val colectas = mutableListOf<Colecta>()

        when (provincia) {
            Provincia.Malaga -> {
                val filas = documento.select(cssQuery = ".donde-donar-malaga tr")
                filas.forEachIndexed { indice, fila ->
                    if (indice > 0) {
                        val celdas = fila.select(cssQuery = "td")
                        val colectaLocal = ColectaMalaga(
                            fecha = celdas[0].text(),
                            municipio = celdas[1].text(),
                            datos = celdas[2].text()
                        )

                        colectas.add(colectaLocal.colecta)
                    }
                }
            }

            Provincia.Almeria, Provincia.Granada -> {
                val filas = documento.select(cssQuery = ".node tr")
                filas.forEachIndexed { indice, fila ->
                    if (indice > 0) {
                        val celdas = fila.select(cssQuery = "td")
                        val colectaLocal = ColectaAlmeriaGranada(
                            provincia = provincia,
                            fecha = celdas[0].text(),
                            municipio = celdas[1].text(),
                            datos = celdas[2].text()
                        )

                        colectas.add(colectaLocal.colecta)
                    }
                }
            }
        }

        return colectas
    }
}