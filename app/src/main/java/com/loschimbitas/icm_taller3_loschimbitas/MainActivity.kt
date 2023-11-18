package com.loschimbitas.icm_taller3_loschimbitas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion.IniciarSesion
import com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion.Registro
import com.loschimbitas.icm_taller3_loschimbitas.actividades.principal.PantallaPrincipal
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityMainBinding
import com.loschimbitas.icm_taller3_loschimbitas.globales.UsuarioAcual
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UsuarioAcual.setUsuario(Usuario())
        auth = FirebaseAuth.getInstance()

        inicializar()
    }

    private fun inicializar() {
        iniciarListeners()
    }

    private fun iniciarListeners() {
        iniciarListenerBtnIniciarSesion()
        iniciarListenerBtnRegistrarse()
    }

    private fun iniciarListenerBtnIniciarSesion() {
        binding.btnIniciarSesion.setOnClickListener {
            startActivity(Intent(this, IniciarSesion::class.java))
        }
    }

    private fun iniciarListenerBtnRegistrarse() {
        binding.btnRegistrarse.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null)
            updateUI(auth.currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, PantallaPrincipal::class.java)

            UsuarioAcual.obtenerInformacionUsuarioActual(currentUser.uid, baseContext)

            intent.putExtra("user", currentUser)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}