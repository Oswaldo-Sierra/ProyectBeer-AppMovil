package com.example.proyectobeer.login.gui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectobeer.MainActivity
import com.example.proyectobeer.R
import com.example.proyectobeer.databinding.ActivityFormUserBinding
import com.example.proyectobeer.login.data.UserDTO
import com.example.proyectobeer.login.data.UserDataHelper
import com.example.proyectobeer.login.gui.ui.ShowUsersActivity

class FormUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormUserBinding
    private lateinit var bd: UserDataHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityFormUserBinding.inflate(layoutInflater)
        this.bd = UserDataHelper(this)
        setContentView(binding.root)

        /* Se obtiene el tipo de operación que se va a ejecutar en la operación */
        val operationType = intent.getStringExtra("action_type")
        if (operationType.equals("UPDATE")) {
            /* Se obtiene el identificador de user ID recibido por parámetro */
            val userId = intent.getIntExtra("user_id", -1)
            if (userId == -1) {
                finish()
            }

            /* Se consulta la información de usuario */
            val user = bd.getUserById(userId)

            /* Se asigna la información a los componentes visuales */
            this.binding.inputUsername.setText(user.nameuser)
            this.binding.inputPassword.setText(user.passwordUser)
            this.binding.inputConfirmPassword.setText(user.passwordUser)

            /* Se cambian titulos o textos */
            this.binding.idTitleForm.text = getString(R.string.FormUser_Title_UPDATE)
            this.binding.tvSubtitle.text = getString(R.string.FormUser_Subtitle_UPDATE)
            this.binding.buttonAccept.text = getString(R.string.FormUser_Button_UPDATE)
        }

        this.binding.buttonBack.setOnClickListener {
            if (operationType.equals("CREATE")) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ShowUsersActivity::class.java)
                startActivity(intent)
            }
        }



        this.binding.buttonAccept.setOnClickListener {
            if (operationType.equals("CREATE")) {
                val username = this.binding.inputUsername.text.toString()
                val password = this.binding.inputPassword.text.toString()
                val confirmationPassword = this.binding.inputConfirmPassword.text.toString()
                // Validar campos vacíos
                if (username.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty()) {
                    Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Validar contraseña
                if (password != confirmationPassword) {
                    Toast.makeText(this, "La contraseña no coincide", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                /* Validar si el nombre de usuario ya existe */
                if (this.bd.getUserByUsername(username) != null) {
                    /* El nombre de usuario ya está tomado */
                    Toast.makeText(
                        this, "¡El nombre de usuario ya existe!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    /* Se agrega el registro en BD*/
                    val userDTO = UserDTO(0, username, password)
                    this.bd.insertUser(userDTO)
                    Toast.makeText(
                        this, "¡Registro de usuario agregado!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                /* Se limpian los datos */
                this.binding.inputUsername.setText("")
                this.binding.inputPassword.setText("")
                this.binding.inputConfirmPassword.setText("")

            } else {
                /* Se obtiene el identificador de user ID recibido por parámetro */
                val userId = intent.getIntExtra("user_id", -1)
                val username = this.binding.inputUsername.text.toString()
                val password = this.binding.inputPassword.text.toString()
                val confirmationPassword = this.binding.inputPassword.text.toString()

                if (username.isEmpty() || password.isEmpty() || confirmationPassword.isEmpty()) {
                    Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (password != confirmationPassword) {
                    Toast.makeText(this, "La contraseña no coincide", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val userDTO = UserDTO(userId, username, password)
                this.bd.updateUser(userDTO)
                Toast.makeText(
                    this, "¡Registro de usuario actualizado!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, ShowUsersActivity::class.java)
                startActivity(intent)
            }
        }
    }
}