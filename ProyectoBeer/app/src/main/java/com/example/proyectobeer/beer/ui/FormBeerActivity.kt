package com.example.proyectobeer.beer.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectobeer.MainActivity
import com.example.proyectobeer.R
import com.example.proyectobeer.beer.data.BeerDTO
import com.example.proyectobeer.beer.data.BeerDataBaseHelper
import com.example.proyectobeer.databinding.ActivityFormBeerBinding

class FormBeerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormBeerBinding
    private lateinit var bd: BeerDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityFormBeerBinding.inflate(layoutInflater)
        this.bd = BeerDataBaseHelper(this)
        setContentView(binding.root)

        val operationType = intent.getStringExtra("action_type")
        if (operationType.equals("UPDATE")) {
            val beerSerial = intent.getIntExtra("beer_serial_number", -1)

            if (beerSerial == -1) {
                finish()
            }

            val beer = bd.getBeerById(beerSerial)

            this.binding.inputBrand.setText(beer.brand)
            this.binding.inputTypew.setText(beer.type)
            this.binding.inputABV.setText(beer.ABV.toString())
            this.binding.inputIBU.setText(beer.IBU.toString())
            this.binding.inputProvider.setText(beer.provider)
            this.binding.inputPrice.setText(beer.price.toString())
            this.binding.inputQuantity.setText(beer.quantity.toString())

            this.binding.idTitleForm.text = getString(R.string.FormBeer_Title_UPDATE)
            this.binding.tvSubtitle.text = getString(R.string.FormBeer_Subtitle_UPDATE)

        }

        this.binding.buttonBack.setOnClickListener {
            if (operationType.equals("CREATE")) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ShowBeerActivity::class.java)
                startActivity(intent)
            }
        }


        this.binding.buttonAccept.setOnClickListener {
            if (operationType.equals("CREATE")) {

                val brand = this.binding.inputBrand.text.toString()
                val type = this.binding.inputTypew.text.toString()
                val abv = this.binding.inputABV.text.toString()
                val ibu = this.binding.inputIBU.text.toString()
                val provider = this.binding.inputProvider.text.toString()
                val price = this.binding.inputPrice.text.toString()
                val quantity = this.binding.inputQuantity.text.toString()

                // Validar campos vacíos
                if (brand.isEmpty() || type.isEmpty() || abv.isEmpty() || ibu.isEmpty() ||
                    provider.isEmpty() || price.isEmpty() || quantity.isEmpty()
                ) {
                    Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val beerDTO = BeerDTO(
                    0,
                    brand,
                    type,
                    abv,
                    ibu,
                    provider,
                    price.toInt(),
                    quantity.toInt()
                )

                // Insertar cerveza
                this.bd.insertBeer(beerDTO)
                Toast.makeText(this, "¡Cerveza registrada!", Toast.LENGTH_SHORT).show()

                // Limpiar campos
                this.binding.inputBrand.setText("")
                this.binding.inputTypew.setText("")
                this.binding.inputABV.setText("")
                this.binding.inputIBU.setText("")
                this.binding.inputProvider.setText("")
                this.binding.inputPrice.setText("")
                this.binding.inputQuantity.setText("")

            } else {
                val beerSerial = intent.getIntExtra("beer_serial_number", -1)
                val brand = this.binding.inputBrand.text.toString()
                val type = this.binding.inputTypew.text.toString()
                val abv = this.binding.inputABV.text.toString()
                val ibu = this.binding.inputIBU.text.toString()
                val provider = this.binding.inputProvider.text.toString()
                val price = this.binding.inputPrice.text.toString()
                val quantity = this.binding.inputQuantity.text.toString()

                if (brand.isEmpty() || type.isEmpty() || abv.isEmpty() || ibu.isEmpty() ||
                    provider.isEmpty() || price.isEmpty() || quantity.isEmpty()
                ) {
                    Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Crear DTO con el ID de la cerveza
                val beerDTO = BeerDTO(
                    beerSerial,
                    brand,
                    type,
                    abv,
                    ibu,
                    provider,
                    price.toInt(),
                    quantity.toInt()
                )

                // Actualizar registro
                this.bd.updateBeer(beerDTO)
                Toast.makeText(this, "¡Cerveza actualizada!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, ShowBeerActivity::class.java)
                startActivity(intent)
            }
        }


    }
}