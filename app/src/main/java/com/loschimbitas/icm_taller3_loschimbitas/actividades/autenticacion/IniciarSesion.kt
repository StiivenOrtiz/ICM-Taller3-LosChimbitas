package com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.actividades.principal.PantallaPrincipal
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityIniciarSesionBinding

class IniciarSesion : AppCompatActivity() {

    private lateinit var binding: ActivityIniciarSesionBinding

//    Variables para la autenticación
    private lateinit var auth:FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIniciarSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        inicializar()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, PantallaPrincipal::class.java)
            intent.putExtra("user", currentUser)
            startActivity(intent)
        } else {
            binding.editTextUsuario.setText("")
            binding.editTextContrasena.setText("")
        }

    }

    private fun inicializar() {
        iniciarListeners()
    }

    private fun iniciarListeners() {
        iniciarListenerBtnIniciarSesion()
    }

    private fun iniciarListenerBtnIniciarSesion() {
        binding.btnIniciarSesionMenu.setOnClickListener {
            signInUser(binding.editTextUsuario.text.toString(), binding.editTextContrasena.text.toString())
        }
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email = binding.editTextUsuario.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.editTextUsuario.error = "Required."
            valid = false
        } else {
            binding.editTextUsuario.error = null
        }
        val password = binding.editTextContrasena.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.editTextContrasena.error = "Required."
            valid = false
        } else {
            binding.editTextContrasena.error = null
        }
        return valid
    }

    private fun signInUser(email: String, password: String){
        if(validateForm()){
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
// Sign in success, update UI
                        Log.d(TAG, "signInWithEmail:success:")
                        updateUI(auth.currentUser)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
    }


}