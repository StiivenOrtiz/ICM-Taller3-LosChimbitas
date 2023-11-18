package com.loschimbitas.icm_taller3_loschimbitas.globales

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

object UsuariosConectados {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("usuarios")
    private val usuarios = mutableListOf<Usuario>()
    private val childEventListener: ChildEventListener

    fun obtenerUsuarios() : List<Usuario> {
        return usuarios.toList()
    }

    init {
        childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val usuario = snapshot.getValue(Usuario::class.java)
                if (usuario?.estado == true) {
                    usuarios.add(usuario)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val usuario = snapshot.getValue(Usuario::class.java)
                if (usuario?.estado == true) {
                    usuarios.removeIf { it.numeroAutenticacion == usuario.numeroAutenticacion }
                    usuarios.add(usuario)
                } else {
                    usuarios.removeIf { it.numeroAutenticacion == usuario?.numeroAutenticacion }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val usuario = snapshot.getValue(Usuario::class.java)
                usuarios.removeIf { it.numeroAutenticacion == usuario?.numeroAutenticacion }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // No es necesario implementar en este caso
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error, si es necesario
            }
        }

        databaseReference.addChildEventListener(childEventListener)
    }
}
