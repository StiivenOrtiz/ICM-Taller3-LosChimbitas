package com.loschimbitas.icm_taller3_loschimbitas.globales

import android.util.Log
import com.google.firebase.database.*
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

object UsuariosConectados {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("usuarios")
    private val usuarios = mutableListOf<Usuario>()
    private var childEventListener: ChildEventListener? = null

    init {
        configurarChildEventListener()
    }

    private fun configurarChildEventListener() {
        childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // Usuario agregado
                val usuario = snapshot.getValue(Usuario::class.java)
                usuario?.let {
                    usuarios.add(it)
                }
                Log.i("UsuariosConectados", "nombre usuario: ${usuario?.nombreUsuario}")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // Usuario cambiado
                val usuario = snapshot.getValue(Usuario::class.java)
                usuario?.let {
                    val index = usuarios.indexOfFirst {
                        it.numeroAutenticacion == usuario.numeroAutenticacion }
                    if (index != -1) {
                        usuarios[index] = it
                    }
                }
                Log.i("UsuariosConectados", "nombre usuario: ${usuario?.nombreUsuario}")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Usuario eliminado
                val usuario = snapshot.getValue(Usuario::class.java)
                usuario?.let {
                    usuarios.remove(it)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Usuario movido (no es necesario para este caso)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }
        }

        databaseReference.addChildEventListener(childEventListener!!)
    }

    fun detenerObtencionUsuarios() {
        // Detener la escucha de eventos cuando sea necesario (por ejemplo, en onDestroy de la actividad)
        childEventListener?.let {
            databaseReference.removeEventListener(it)
        }
    }

    fun obtenerUsuarios(): List<Usuario> {
        return usuarios.toList()
    }
}
