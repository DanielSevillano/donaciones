package io.github.danielsevillano.donaciones.domain

enum class Provincia(
    val nombre: String,
    val url: String
) {
    Almeria(
        nombre = "Almería",
        url = "http://transfusion.granada-almeria.org/donar/proximas-colectas-en-almeria"
    ),
    Granada(
        nombre = "Granada",
        url = "http://transfusion.granada-almeria.org/donar/proximas-colectas-en-granada"
    ),
    Malaga(
        nombre = "Málaga",
        url = "http://www.donantesmalaga.org/donar/proximas-colectas-en-malaga"
    )
}