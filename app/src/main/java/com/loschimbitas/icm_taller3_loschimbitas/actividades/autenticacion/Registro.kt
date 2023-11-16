package com.loschimbitas.icm_taller3_loschimbitas.actividades.autenticacion

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityRegistroBinding
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

class Registro : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val database = FirebaseDatabase.getInstance()
    private lateinit var myRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

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
            myRef = database.getReference("usuarios/${auth.currentUser?.uid}")
            val nombre = binding.editTextNombre.text.toString()
            val apellido = binding.editTextApellido.text.toString()
            val correo = binding.editTextCorreo.text.toString()
            val contrasena = binding.editTextContrasena.text.toString()
            val usuario = Usuario(nombre = nombre, apellido = apellido, correo = correo, contrasena = contrasena)
            myRef.setValue(usuario)

//            registrarUSuario(correo, contrasena)

//            val intent = Intent(this, AgregarFotoPerfil::class.java)
//            startActivity(intent)
        }
    }

//    private fun registrarUSuario(email: String, password: String) {
//        auth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful)
//                    val user = auth.currentUser
//                    if (user != null) {
//// Update user info
//                        val upcrb = UserProfileChangeRequest.Builder()
//                        upcrb.setDisplayName(binding.editTextNombre.text.toString() + " " + binding.editTextApellido.text.toString())
//                        upcrb.setPhotoUri(Uri.parse("path/to/pic")) //fake uri, use Firebase Storage
//                        user.updateProfile(upcrb.build())
//                        updateUI(user)
//                    }
//                } else {
//                    Toast.makeText(this, "createUserWithEmail:Failure: " + task.exception.toString(),
//                        Toast.LENGTH_SHORT).show()
//                    task.exception?.message?.let { Log.e(TAG, it) }
//                }
//            }
//    }
//
//    private fun updateUI(user: FirebaseUser) {
//        val intent = Intent(this, AgregarFotoPerfil::class.java)
//        intent.putExtra("user", user)
//        startActivity(intent)
//    }


}