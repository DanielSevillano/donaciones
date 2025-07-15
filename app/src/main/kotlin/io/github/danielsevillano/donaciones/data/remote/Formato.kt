package io.github.danielsevillano.donaciones.data.remote

class Formato {
    private val diccionario = mapOf(
        "Aguila" to "Águila",
        "Albeniz" to "Albéniz",
        "Alboran" to "Alborán",
        "Alcantara" to "Alcántara",
        "Andalucia" to "Andalucía",
        "Cafeteria" to "Cafetería",
        "Cartama" to "Cártama",
        "Coin" to "Coín",
        "Concepcion" to "Concepción",
        "Ingles" to "Inglés",
        "Malaga" to "Málaga",
        "Martin" to "Martín",
        "Maritimo" to "Marítimo",
        "Medico" to "Médico",
        "Mediterraneo" to "Mediterráneo",
        "Movil" to "Móvil",
        "Ojen" to "Ojén",
        "Salon" to "Salón",
        "Sanguinea" to "Sanguínea",
        "Transfusion" to "Transfusión",
        "Tranvia" to "Tranvía",
        "Velazquez" to "Velázquez",
        "Vestibulo" to "Vestíbulo"
    )

    private fun String.formatearEspacios(): String {
        return this
            .replace(regex = Regex(pattern = """(\w+)\."""), replacement = "$1. ")
            .replace(regex = Regex(pattern = """(\w+)\("""), replacement = "$1 (")
            .replace(regex = Regex(pattern = """(\w+)-"""), replacement = "$1 - ")
    }

    private fun String.formatearCaracteres(): String {
        return this
            .replace(oldValue = "\u0091", newValue = "")
            .replace(oldValue = "Ã", newValue = "ñ")
    }

    private fun String.formatearMayusculas(): String {
        val regex = Regex(pattern = """\b\w""")
        return regex.replace(input = this.lowercase()) { it.value.uppercase() }
    }

    private fun String.formatearTildes(): String {
        var textoFormateado = this
        diccionario.forEach { old, new ->
            textoFormateado = textoFormateado.replace(oldValue = old, newValue = new)
        }

        return textoFormateado
    }

    fun formatear(texto: String): String {
        return texto
            .formatearEspacios()
            .formatearCaracteres()
            .formatearMayusculas()
            .formatearTildes()
    }
}