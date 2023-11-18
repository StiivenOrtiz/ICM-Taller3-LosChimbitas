package com.loschimbitas.icm_taller3_loschimbitas.actividades.principal

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.loschimbitas.icm_taller3_loschimbitas.R
import com.loschimbitas.icm_taller3_loschimbitas.modelo.Usuario

class UserListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val listViewUsers: ListView = findViewById(R.id.listViewUsers)

        // Supongamos que tienes una lista de usuarios
        val userList = listOf(
            Usuario(
                "0",
                "Parchado",
                "John",
                "Doe",
                "john@example.com",
                "imagen",
                false,
                0.0,
                37.7749,
            ),
        )

        // Configurar un adaptador personalizado para la ListView
        val adapter = UserListAdapter(this, userList)
        listViewUsers.adapter = adapter
    }
}