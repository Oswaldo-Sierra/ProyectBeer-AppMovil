package com.example.proyectobeer

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Size
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyectobeer.databinding.ActivityIntroBinding
import com.example.proyectobeer.login.gui.LoginActivity
import com.example.proyectobeer.login.gui.SingUpUserActivity


class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.binding.idButtonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        this.binding.idButtonSignUp.setOnClickListener {
            val intent = Intent(this, SingUpUserActivity::class.java)
            startActivity(intent)
        }


    }
}