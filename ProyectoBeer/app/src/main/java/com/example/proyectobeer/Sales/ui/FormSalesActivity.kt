package com.example.proyectobeer.Sales.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectobeer.MainActivity
import com.example.proyectobeer.R
import com.example.proyectobeer.Sales.data.SalesDTO
import com.example.proyectobeer.Sales.data.SalesDataBaseHelper
import com.example.proyectobeer.beer.data.BeerDataBaseHelper
import com.example.proyectobeer.databinding.ActivityFormSalesBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormSalesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormSalesBinding
    private lateinit var salesDb: SalesDataBaseHelper
    private lateinit var beerDb: BeerDataBaseHelper
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.binding = ActivityFormSalesBinding.inflate(layoutInflater)
        this.salesDb = SalesDataBaseHelper(this)
        this.beerDb = BeerDataBaseHelper(this)

        // SharedPreferences para obtener el nombre del usuario logueado
        this.sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        setContentView(binding.root)

        val operationType = intent.getStringExtra("action_type")

        if (operationType.equals("UPDATE")) {
            val salesId = intent.getIntExtra("sales_id", -1)

            if (salesId == -1) {
                finish()
            }

            val sale = salesDb.getSaleById(salesId)

            this.binding.inputSerialNumber.setText(sale.numberSerialofBeer.toString())
            this.binding.inputNumberOfBeerSold.setText(sale.numberofBeerSold.toString())
            this.binding.inputCustomerName.setText(sale.customerName)

            this.binding.idTitleForm.text = getString(R.string.FormSales_Title_UPDATE)
            this.binding.tvSubtitle.text = getString(R.string.FormSales_Subtitle_UPDATE)
        }

        this.binding.buttonBack.setOnClickListener {
            if (operationType.equals("CREATE")) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, ShowSalesActivity::class.java)
                startActivity(intent)
            }
        }

        this.binding.buttonAccept.setOnClickListener {
            if (operationType.equals("CREATE")) {
                createSale()
            } else {
                updateSale()
            }
        }
    }

    private fun createSale() {
        val beerSerialStr = this.binding.inputSerialNumber.text.toString()
        val quantityStr = this.binding.inputNumberOfBeerSold.text.toString()
        val customerName = this.binding.inputCustomerName.text.toString()

        // Validar campos vacíos
        if (beerSerialStr.isEmpty() || quantityStr.isEmpty() || customerName.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val beerSerial = beerSerialStr.toIntOrNull()
        val quantity = quantityStr.toIntOrNull()

        if (beerSerial == null || quantity == null) {
            Toast.makeText(this, "Ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar que la cantidad sea positiva
        if (quantity <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar que la cerveza existe
        val beer = try {
            beerDb.getBeerById(beerSerial)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "No existe una cerveza con ese número de serie",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Verificar que hay suficiente cantidad en inventario
        if (beer.quantity < quantity) {
            Toast.makeText(
                this,
                "Stock insuficiente. Disponible: ${beer.quantity}, Solicitado: $quantity",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Calcular precio total
        val priceTotal = beer.price * quantity

        // Obtener nombre del usuario logueado
        val userName = sharedPreferences.getString("logged_user_name", "Usuario Desconocido")
            ?: "Usuario Desconocido"

        // Obtener fecha actual
        val currentDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        // Mostrar diálogo de confirmación
        showConfirmationDialog(
            beer.brand,
            quantity,
            priceTotal,
            customerName,
            userName,
            currentDate,
            beerSerial
        )
    }

    private fun showConfirmationDialog(
        beerBrand: String,
        quantity: Int,
        priceTotal: Int,
        customerName: String,
        userName: String,
        currentDate: String,
        beerSerial: Int
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmar Venta")

        val message = """
            Cerveza: $beerBrand
            Cantidad: $quantity unidades
            Precio Total: $$priceTotal
            Cliente: $customerName
            Vendedor: $userName
            Fecha: $currentDate
            
            ¿Desea confirmar esta venta?
        """.trimIndent()

        builder.setMessage(message)

        builder.setPositiveButton("Confirmar") { dialog, _ ->
            // Crear el DTO de venta
            val salesDTO = SalesDTO(
                0,
                beerSerial,
                quantity,
                priceTotal,
                currentDate,
                userName,
                customerName
            )

            // Insertar la venta
            salesDb.insertSale(salesDTO)

            // Actualizar el inventario de la cerveza
            val beer = beerDb.getBeerById(beerSerial)
            beer.quantity -= quantity
            beerDb.updateBeer(beer)

            Toast.makeText(this, "¡Venta registrada exitosamente!", Toast.LENGTH_SHORT).show()

            // Limpiar campos
            this.binding.inputSerialNumber.setText("")
            this.binding.inputNumberOfBeerSold.setText("")
            this.binding.inputCustomerName.setText("")

            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun updateSale() {
        val salesId = intent.getIntExtra("sales_id", -1)
        val beerSerialStr = this.binding.inputSerialNumber.text.toString()
        val quantityStr = this.binding.inputNumberOfBeerSold.text.toString()
        val customerName = this.binding.inputCustomerName.text.toString()

        // Validar campos vacíos
        if (beerSerialStr.isEmpty() || quantityStr.isEmpty() || customerName.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val beerSerial = beerSerialStr.toIntOrNull()
        val quantity = quantityStr.toIntOrNull()

        if (beerSerial == null || quantity == null) {
            Toast.makeText(this, "Ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show()
            return
        }

        if (quantity <= 0) {
            Toast.makeText(this, "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show()
            return
        }

        // Verificar que la cerveza existe
        val beer = try {
            beerDb.getBeerById(beerSerial)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "No existe una cerveza con ese número de serie",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Obtener la venta original para restaurar el inventario
        val originalSale = salesDb.getSaleById(salesId)
        val originalBeer = beerDb.getBeerById(originalSale.numberSerialofBeer)

        // Restaurar cantidad original al inventario
        originalBeer.quantity += originalSale.numberofBeerSold
        beerDb.updateBeer(originalBeer)

        // Verificar stock para la nueva venta
        if (beer.quantity < quantity) {
            Toast.makeText(
                this,
                "Stock insuficiente. Disponible: ${beer.quantity}, Solicitado: $quantity",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Calcular nuevo precio total
        val priceTotal = beer.price * quantity

        // Obtener nombre del usuario y fecha de la venta original
        val userName = originalSale.userName
        val currentDate = originalSale.dataofSale

        // Crear DTO actualizado
        val salesDTO = SalesDTO(
            salesId,
            beerSerial,
            quantity,
            priceTotal,
            currentDate,
            userName,
            customerName
        )

        // Actualizar venta
        salesDb.updateSale(salesDTO)

        // Actualizar inventario de cerveza
        beer.quantity -= quantity
        beerDb.updateBeer(beer)

        Toast.makeText(this, "¡Venta actualizada!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, ShowSalesActivity::class.java)
        startActivity(intent)
    }
}