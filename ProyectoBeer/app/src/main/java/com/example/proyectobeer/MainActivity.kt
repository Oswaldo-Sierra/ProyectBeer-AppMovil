package com.example.proyectobeer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectobeer.Sales.ui.FormSalesActivity
import com.example.proyectobeer.Sales.ui.ShowSalesActivity
import com.example.proyectobeer.beer.ui.FormBeerActivity
import com.example.proyectobeer.beer.ui.ShowBeerActivity
import com.example.proyectobeer.databinding.ActivityMainBinding
import com.example.proyectobeer.login.gui.FormUserActivity
import com.example.proyectobeer.login.gui.ui.ShowUsersActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main) // ← Muestra el diseño principal
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(this.binding.root)

        this.binding.idCardShowUsers.setOnClickListener {
            val intent = Intent(this, ShowUsersActivity::class.java)
            startActivity(intent)
        }

        this.binding.idCardCreateUser.setOnClickListener {
            val intent = Intent(this, FormUserActivity::class.java).apply {
                putExtra("action_type","CREATE")
            }
            startActivity(intent)
        }

        this.binding.idCardShowBeer.setOnClickListener {
            val  intent = Intent(this, ShowBeerActivity::class.java)
            startActivity(intent)
        }

        this.binding.idcardCreateBeer.setOnClickListener {
            val intent = Intent(this, FormBeerActivity::class.java).apply {
                putExtra("action_type","CREATE")
            }
            startActivity(intent)
        }

        this.binding.idCardCreateSales.setOnClickListener {
            val intent = Intent(this, FormSalesActivity::class.java).apply {
                putExtra("action_type","CREATE")
            }
            startActivity(intent)
        }

        this.binding.idCardShowSalesOfBeer.setOnClickListener {
            val intent = Intent(this, ShowSalesActivity::class.java).apply {
                putExtra("action_type","CREATE")
            }
            startActivity(intent)
        }

        this.binding.idCardInfo.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java).apply {
            }
            startActivity(intent)
        }

        this.binding.idCardLogout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cerrar sesion")
            builder.setMessage("¿Esta seguro de Cerrar Sesion?")

            builder.setPositiveButton("Sí"){ dialog, _ ->
                Toast.makeText(this, "¡Sesion Cerrada!", Toast.LENGTH_SHORT)
                    .show()
                val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("logged_user_name", "")
                val intent = Intent(this, IntroActivity::class.java).apply {
                    putExtra("action_type","CREATE")
                }
                startActivity(intent)
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()


        }

        
    }

}
