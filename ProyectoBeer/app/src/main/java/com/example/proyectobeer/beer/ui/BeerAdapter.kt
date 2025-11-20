package com.example.proyectobeer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectobeer.beer.data.BeerDTO
import com.example.proyectobeer.beer.data.BeerDataBaseHelper
import com.example.proyectobeer.beer.ui.FormBeerActivity
import com.google.android.material.button.MaterialButton


class BeerAdapter(private var beers: List<BeerDTO>, private var context: Context) :
    RecyclerView.Adapter<BeerAdapter.BeerViewHolder>() {

    private val db: BeerDataBaseHelper = BeerDataBaseHelper(context)

    class BeerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val serialNumber = itemView.findViewById<TextView>(R.id.idSerialNumber)
        val brand = itemView.findViewById<TextView>(R.id.idBrand)
        val updateButton = itemView.findViewById<MaterialButton>(R.id.buttonUpdate)
        val deleteButton = itemView.findViewById<MaterialButton>(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BeerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_beer, parent, false)
        return BeerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BeerViewHolder,
        position: Int
    ) {
        val beer = this.beers[position]
        holder.serialNumber.text = beer.serialNumber.toString()
        holder.brand.text = beer.brand

        // Click en la marca para ver más información
        holder.brand.setOnClickListener {
            showBeerDetailsDialog(beer)
        }

        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setTitle("Confirmar acción")
            builder.setMessage("¿Estás seguro de que quieres eliminar esta cerveza?")

            builder.setPositiveButton("Sí") { dialog, _ ->
                db.deleteBeerById(beer.serialNumber)
                this.refreshData(db.getAllBeers())
                Toast.makeText(holder.itemView.context, "¡Cerveza eliminada!", Toast.LENGTH_SHORT)
                    .show()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, FormBeerActivity::class.java).apply {
                putExtra("action_type", "UPDATE")
                putExtra("beer_serial_number", beer.serialNumber)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return beers.size
    }

    fun refreshData(newBeers: List<BeerDTO>) {
        this.beers = newBeers
        notifyDataSetChanged()
    }

    private fun showBeerDetailsDialog(beer: BeerDTO) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Detalles de ${beer.brand}")

        val message = """
            Número de Serie: ${beer.serialNumber}
            Marca: ${beer.brand}
            Tipo: ${beer.type}
            ABV: ${beer.ABV}
            IBU: ${beer.IBU}
            Proveedor: ${beer.provider}
            Precio: ${beer.price}
            Cantidad: ${beer.quantity}
        """.trimIndent()

        builder.setMessage(message)
        builder.setPositiveButton("Cerrar") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}