package com.loschimbitas.icm_taller3_loschimbitas.modelo

data class Usuario(
    val numeroAutenticacion: String? = null,
    var nombreUsuario: String? = null,
    var nombre: String? = null,
    var apellido: String? = null,
    var correo: String? = null,
    var imagenContacto: String? = null,
    var estado: Boolean? = null,
    var latitud: Double? = null,
    var longitud: Double? = null,
)