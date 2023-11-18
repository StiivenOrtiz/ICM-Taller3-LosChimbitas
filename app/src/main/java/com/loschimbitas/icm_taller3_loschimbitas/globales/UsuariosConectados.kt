package com.loschimbitas.icm_taller3_loschimbitas.globales

import android.util.Log
import com.google.firebase.database.*
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

object UsuariosConectados {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("usuarios")
    private val usuarios = mutableListOf<Usuario>()
    private var childEventListener: ChildEventListener? = null

    fun getUsuarios(): List<Usuario> {
        return usuarios.toList()
    }

    init {
        usuarios.clear()
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
                Log.i("UsuariosConectados1", "nombre usuario: ${usuario?.nombreUsuario}")
                notificarObservadores()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val usuario = snapshot.getValue(Usuario::class.java)
                // Si el estado del usuario cambio a desconectado, eliminarlo de la lista
                if (usuario?.estado == false) {
                    usuarios.removeIf { usuarioLista ->
                        usuarioLista.numeroAutenticacion == usuario.numeroAutenticacion
                    }

                    Log.i("UsuariosConectados2",
                        "Desconectado nombre usuario: ${usuario?.nombreUsuario}")
                } else {
                    // Si el estado del usuario cambio a conectado, agregarlo a la lista
                    usuarios.add(usuario!!)
                    Log.i("UsuariosConectados2",
                        "Conectado nombre usuario: ${usuario?.nombreUsuario}")
                }
                notificarObservadores()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // Usuario eliminado
                val usuario = snapshot.getValue(Usuario::class.java)
                usuarios.removeIf() { usuarioLista ->
                    usuarioLista.numeroAutenticacion == usuario?.numeroAutenticacion
                }
                notificarObservadores()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
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

    private val observadores = mutableListOf<UsuariosConectadosObserver>()

    interface UsuariosConectadosObserver {
        fun onUsuariosActualizados()
    }

    fun agregarObserver(observer: UsuariosConectadosObserver) {
        observadores.add(observer)
    }

    fun quitarObserver(observer: UsuariosConectadosObserver) {
        observadores.remove(observer)
    }

    private fun notificarObservadores() {
        Log.i("UsuariosConectados", "Notificando observadores")
        observadores.forEach {
            it.onUsuariosActualizados()
        }
    }
}
