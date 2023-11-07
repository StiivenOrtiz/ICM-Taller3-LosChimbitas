package com.loschimbitas.icm_taller3_loschimbitas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.loschimbitas.icm_taller3_loschimbitas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}