package io.github.danielsevillano.donaciones.domain

enum class GrupoSanguineo(
    val codigo: String
) {
    APositivo(codigo = "A+"),
    ANegativo(codigo = "A-"),
    BPositivo(codigo = "B+"),
    BNegativo(codigo = "B-"),
    ABPositivo(codigo = "AB+"),
    ABNegativo(codigo = "AB-"),
    OPositivo(codigo = "0+"),
    ONegativo(codigo = "0-"),
    Desconocido(codigo = "Desconocido")
}