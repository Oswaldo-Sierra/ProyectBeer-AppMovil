package com.example.proyectobeer.login.gui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectobeer.IntroActivity
import com.example.proyectobeer.MainActivity
import com.example.proyectobeer.RecoverPasswordActivity
import com.example.proyectobeer.databinding.ActivityLoginBinding
import com.example.proyectobeer.login.data.UserDTO
import com.example.proyectobeer.login.data.UserDataHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: UserDataHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityLoginBinding.inflate(layoutInflater)
        this.db = UserDataHelper(this)
        setContentView(binding.root)

        this.binding.buttonLogin.setOnClickListener {
            /* Manejo del evento del click del boton*/
            val userNamer = this.binding.InputuserName.text.toString()
            val password = this.binding.inputPasswordUser.text.toString()

            if (userNamer.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Complete todos los campos" , Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userInput = UserDTO(0, userNamer, password)

            if (this.db.validatUser(userInput)){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("logged_user_name", userNamer)
                editor.apply()
                finish()
            }else{
                Toast.makeText(this,"EL Usuario ingreaso no existe" , Toast.LENGTH_SHORT).show()
            }
        }

        this.binding.lblForgetPassword.setOnClickListener {
            val intent = Intent(this, RecoverPasswordActivity::class.java)
            startActivity(intent)
        }

        this.binding.buttonBack.setOnClickListener {
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

    }
}