package io.github.danielsevillano.donaciones.data.remote

import io.github.danielsevillano.donaciones.data.local.Colecta
import io.github.danielsevillano.donaciones.domain.Provincia
import it.skrape.core.htmlDocument
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.extractIt
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.td

class Scrape {
    suspend fun obtenerColectas(provincia: Provincia): ArrayList<Colecta> {
        val colectas = skrape(AsyncFetcher) {
            request { url = provincia.url }

            extractIt<ArrayList<Colecta>> {
                htmlDocument {
                    when (provincia) {
                        Provincia.Malaga -> ".donde-donar-malaga tr" {
                            findAll {
                                forEachIndexed { index, element ->
                                    if (index > 0) {
                                        val colectaLocal = ColectaMalaga(
                                            fecha = element.td { findFirst { text } },
                                            municipio = element.td { findSecond { text } },
                                            datos = element.td { findThird { text } }
                                        )

                                        it.add(colectaLocal.colecta)
                                    }
                                }
                            }
                        }

                        Provincia.Almeria, Provincia.Granada -> ".node tr" {
                            findAll {
                                forEachIndexed { index, element ->
                                    if (index > 0) {
                                        val colectaLocal = ColectaAlmeriaGranada(
                                            provincia = provincia,
                                            fecha = element.td { findFirst { text } },
                                            municipio = element.td { findSecond { text } },
                                            datos = element.td { findThird { text } }
                                        )

                                        it.add(colectaLocal.colecta)
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        return colectas
    }
}