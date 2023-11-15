package com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityRegistroBinding

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inicializar()
    }

    private fun inicializar() {
        iniciarListeners()
    }

    private fun iniciarListeners() {
        iniciarListenerBtnRegistrar()
    }

    private fun iniciarListenerBtnRegistrar() {
        binding.buttonRegistrarse.setOnClickListener {
            val intent = Intent(this, AgregarFotoPerfil::class.java)
            startActivity(intent)
        }
    }
}