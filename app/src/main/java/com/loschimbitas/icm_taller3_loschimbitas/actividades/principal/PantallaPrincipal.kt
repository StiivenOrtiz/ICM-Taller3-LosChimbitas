package com.loschimbitas.icm_taller3_loschimbitas.actividades.principal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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

class PantallaPrincipal : AppCompatActivity() {

    val listaUsuarios = UsuariosConectados.obtenerUsuarios()
    private lateinit var binding: ActivityPantallaPrincipalBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPantallaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        val usuariosConectados = UsuariosConectados.obtenerUsuarios()

        // imprimir en LOG los usuarios conectados
        usuariosConectados.forEach {
            Log.i("UsuarioActual", "nombre usuario: ${it.nombreUsuario}")
        }
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
                if (UsuarioAcual.getEstadoUsuarioActual() == true)
                    UsuarioAcual.setEstadoUsuarioActual(false)
                else if (UsuarioAcual.getEstadoUsuarioActual() == false)
                    UsuarioAcual.setEstadoUsuarioActual(true)
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
}