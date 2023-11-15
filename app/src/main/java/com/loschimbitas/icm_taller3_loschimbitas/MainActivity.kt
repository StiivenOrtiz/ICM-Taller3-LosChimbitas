package com.loschimbitas.icm_taller3_loschimbitas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion.IniciarSesion
import com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion.Registro
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}