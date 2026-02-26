package io.github.danielsevillano.donaciones.data.remote

object Formato {
    private val diccionario = mapOf(
        "Aguila" to "Águila",
        "Albeniz" to "Albéniz",
        "Alboran" to "Alborán",
        "Alcantara" to "Alcántara",
        "Alhaurin" to "Alhaurín",
        "Alora" to "Álora",
        "Andalucia" to "Andalucía",
        "Benahavis" to "Benahavís",
        "Benalmadena" to "Benalmádena",
        "Cafeteria" to "Cafetería",
        "Cartama" to "Cártama",
        "Coin" to "Coín",
        "Cofradia" to "Cofradía",
        "Competa" to "Cómpeta",
        "Concepcion" to "Concepción",
        "Conserjeria" to "Conserjería",
        "Constitucion" to "Constitución",
        "Economicas" to "Económicas",
        "Educacion" to "Educación",
        "Gaucin" to "Gaucín",
        "Ingles" to "Inglés",
        "Malaga" to "Málaga",
        "Martin" to "Martín",
        "Maritimo" to "Marítimo",
        "Medico" to "Médico",
        "Mediterraneo" to "Mediterráneo",
        "Movil" to "Móvil",
        "Multiple" to "Múltiple",
        "Ojen" to "Ojén",
        "Publica" to "Pública",
        "Rincon" to "Rincón",
        "Salon" to "Salón",
        "Sanguinea" to "Sanguínea",
        "Trafico" to "Tráfico",
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
        diccionario.forEach { (old, new) ->
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