package com.loschimbitas.icm_taller3_loschimbitas.actividades.principal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.loschimbitas.icm_taller3_loschimbitas.MainActivity
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityPantallaPrincipalBinding
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuarioAcual
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuariosConectados
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

class PantallaPrincipal : AppCompatActivity(), UsuariosConectados.UsuariosConectadosObserver {

    private lateinit var binding: ActivityPantallaPrincipalBinding
    private lateinit var auth: FirebaseAuth
    var usuariosConectados = UsuariosConectados.getUsuarios()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        UsuariosConectados.agregarObserver(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        supportActionBar?.title = "Los Chimbitas"
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menuCerrarSesion -> {
                auth.signOut()
                UsuarioAcual.setUsuario(Usuario())

                val intent = Intent(this, MainActivity::class.java)

                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)

                return true
            }

            R.id.menuEstado -> {
                if (UsuarioAcual.getEstadoUsuarioActual() == true) {
                    UsuarioAcual.setEstadoUsuarioActual(false)
                    Toast.makeText(
                        this,
                        "Estado cambiado a desconectado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if (UsuarioAcual.getEstadoUsuarioActual() == false) {
                    UsuarioAcual.setEstadoUsuarioActual(true)
                    Toast.makeText(
                        this,
                        "Estado cambiado a conectado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }

            R.id.menuListarUsuarios -> {
                // Manejar la acción de la lista de usuarios disponibles
                val intent = Intent(this, UserListActivity::class.java)
                startActivity(intent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onUsuariosActualizados() {
        // Mostrar un Toast para cada nuevo usuario
        usuariosConectados = UsuariosConectados.getUsuarios()
        Toast.makeText(
            this,
            "Usuarios actualizados: ${usuariosConectados.size}",
            Toast.LENGTH_SHORT
        ).show()

        usuariosConectados.forEach() {
            Log.i("UsuariosConectados", "nombre usuario: ${it.nombreUsuario}")
        }
    }
}