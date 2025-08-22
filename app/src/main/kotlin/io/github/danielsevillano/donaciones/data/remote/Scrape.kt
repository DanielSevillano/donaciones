package io.github.danielsevillano.donaciones.data.remote

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.network.parseGetRequest
import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.domain.Provincia
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Scrape {
    suspend fun obtenerColectas(provincia: Provincia): List<Colecta> {
        val colectas = mutableListOf<Colecta>()

        when (provincia) {
            Provincia.Malaga -> {
                val documento = Ksoup.parseGetRequest(url = provincia.url)
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
                val documento = Ksoup.parseGetRequest(url = provincia.url)
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

            Provincia.Cordoba -> {
                val cliente = HttpClient {
                    install(plugin = ContentNegotiation) {
                        json(
                            Json {
                                ignoreUnknownKeys = true
                            }
                        )
                    }
                }

                try {
                    val respuesta = cliente
                        .get(urlString = provincia.url)
                        .body<RespuestaColectaCordoba>()

                    respuesta.colectas.forEach { colectaLocal ->
                        if (colectaLocal.fecha.isNotBlank()) colectas.add(colectaLocal.colecta)
                    }
                } catch (e: Exception) {
                    println("Error: $e")
                    return emptyList()
                }
            }
        }

        return colectas
    }
}