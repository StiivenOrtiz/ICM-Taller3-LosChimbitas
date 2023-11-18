package com.loschimbitas.icm_taller3_loschimbitas.globales

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

object UsuarioAcual {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("usuarios")
    private lateinit var usuario: Usuario

    fun setUsuario(user: Usuario) {
        this.usuario = user
    }

    fun getUsuario(): Usuario {
        return this.usuario
    }

    fun obtenerInformacionUsuarioActual(userID: String, context: Context) {
        databaseReference.child(userID).get().addOnSuccessListener {
            if (it.exists()) {
                val user = Usuario(
                    it.child("numeroAutenticacion").value.toString(),
                    it.child("nombreUsuario").value.toString(),
                    it.child("nombre").value.toString(),
                    it.child("apellido").value.toString(),
                    it.child("correo").value.toString(),
                    it.child("imagenContacto").value.toString(),
                    it.child("estado").value.toString().toBoolean(),
                    it.child("latitud").value.toString().toDouble(),
                    it.child("longitud").value.toString().toDouble()
                )

                setUsuario(user)

                Log.i("UsuarioActual", "nombre usuario: ${getUsuario().nombreUsuario}")
                Log.i("UsuarioActual", "nombre: ${getUsuario().nombre}")

                Log.i("UsuarioActual", "Información del usuario obtenida correctamente.")

                Toast.makeText(
                    context,
                    "Bienvenido de nuevo ${getUsuario().nombreUsuario}",
                    Toast.LENGTH_LONG
                ).show()
            } else
                Log.e("UsuarioActual", "No se encontró información del usuario.")
        }.addOnFailureListener {
            Log.e("UsuarioActual", "Error al obtener información del usuario.")
        }
    }
}