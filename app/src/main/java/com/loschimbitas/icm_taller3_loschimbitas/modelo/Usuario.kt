package com.loschimbitas.icm_taller3_loschimbitas.modelo

data class Usuario(
    val numeroAutenticacion: Long,
    var nombreUsuario: String,
    var nombre: String,
    var apellido: String,
    var correo: String,
    var contrasena: String,
    var imagenContacto: String,
    var latitud: String,
    var longitud: String
)
