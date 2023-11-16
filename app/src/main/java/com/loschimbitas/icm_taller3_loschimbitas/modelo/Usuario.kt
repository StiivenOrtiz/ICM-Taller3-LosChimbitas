package com.loschimbitas.icm_taller3_loschimbitas.modelo

data class Usuario(
    val numeroAutenticacion: Long? = null,
    var nombreUsuario: String? = null,
    var nombre: String? = null,
    var apellido: String? = null,
    var correo: String? = null,
    var contrasena: String? = null,
    var imagenContacto: String? = null,
    var latitud: String? = null,
    var longitud: String? = null
)
