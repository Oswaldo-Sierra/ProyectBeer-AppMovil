package com.example.proyectobeer.Sales.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SalesDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "sales.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "sales"
        private const val COLUMN_SALES_ID = "salesId"
        private const val COLUMN_SERIAL_OF_BEER = "numberSerialofBeer"
        private const val COLUMN_NUMBER_SOLD = "numberofBeerSold"
        private const val COLUMN_PRICE_TOTAL = "priceTotal"
        private const val COLUMN_DATE = "dataofSale"
        private const val COLUMN_USER_NAME = "userName"
        private const val COLUMN_CUSTOMER_NAME = "customerName"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // 1. Crear la tabla
        val queryCreator = """
            CREATE TABLE $TABLE_NAME(
                $COLUMN_SALES_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SERIAL_OF_BEER INTEGER,
                $COLUMN_NUMBER_SOLD INTEGER,
                $COLUMN_PRICE_TOTAL INTEGER,
                $COLUMN_DATE TEXT,
                $COLUMN_USER_NAME TEXT,
                $COLUMN_CUSTOMER_NAME TEXT
            )
        """.trimIndent()
        db?.execSQL(queryCreator)

        // 2. Crear una venta por defecto
        val queryInsert = """
            INSERT INTO $TABLE_NAME(
                $COLUMN_SERIAL_OF_BEER, $COLUMN_NUMBER_SOLD, $COLUMN_PRICE_TOTAL, 
                $COLUMN_DATE, $COLUMN_USER_NAME, $COLUMN_CUSTOMER_NAME
            ) VALUES (1, 10, 25000, '2024-01-15', 'Juan Pérez', 'Cliente Ejemplo')
        """.trimIndent()
        db?.execSQL(queryInsert)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // 1. Elimina la tabla anterior
        val sqlDrop = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(sqlDrop)
        // 2. Crea de nuevo la tabla
        this.onCreate(db)
    }

    /* Insertar venta */
    fun insertSale(sale: SalesDTO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SERIAL_OF_BEER, sale.numberSerialofBeer)
            put(COLUMN_NUMBER_SOLD, sale.numberofBeerSold)
            put(COLUMN_PRICE_TOTAL, sale.priceTotal)
            put(COLUMN_DATE, sale.dataofSale)
            put(COLUMN_USER_NAME, sale.userName)
            put(COLUMN_CUSTOMER_NAME, sale.customerName)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    /* Actualizar venta */
    fun updateSale(sale: SalesDTO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SERIAL_OF_BEER, sale.numberSerialofBeer)
            put(COLUMN_NUMBER_SOLD, sale.numberofBeerSold)
            put(COLUMN_PRICE_TOTAL, sale.priceTotal)
            put(COLUMN_DATE, sale.dataofSale)
            put(COLUMN_USER_NAME, sale.userName)
            put(COLUMN_CUSTOMER_NAME, sale.customerName)
        }
        val whereClause = "$COLUMN_SALES_ID = ?"
        val whereArgs = arrayOf(sale.salesId.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    /* Eliminar venta por ID */
    fun deleteSaleById(salesId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_SALES_ID = ?"
        val whereArgs = arrayOf(salesId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    /* Obtener venta por ID */
    fun getSaleById(salesId: Int): SalesDTO {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_SALES_ID = $salesId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SALES_ID))
        val serialOfBeer = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERIAL_OF_BEER))
        val numberSold = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_SOLD))
        val priceTotal = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE_TOTAL))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
        val userName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME))
        val customerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME))

        cursor.close()
        db.close()

        return SalesDTO(id, serialOfBeer, numberSold, priceTotal, date, userName, customerName)
    }

    /* Obtener todas las ventas */
    fun getAllSales(): List<SalesDTO> {
        val salesList = mutableListOf<SalesDTO>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val salesId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SALES_ID))
            val serialOfBeer = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERIAL_OF_BEER))
            val numberSold = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_SOLD))
            val priceTotal = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE_TOTAL))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val userName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME))
            val customerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME))

            val sale = SalesDTO(salesId, serialOfBeer, numberSold, priceTotal, date, userName, customerName)
            salesList.add(sale)
        }

        cursor.close()
        db.close()
        return salesList
    }

    /* Buscar ventas por nombre de cliente */
    fun getSalesByCustomerName(customerName: String): List<SalesDTO> {
        val salesList = mutableListOf<SalesDTO>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_CUSTOMER_NAME LIKE ?"
        val cursor = db.rawQuery(query, arrayOf("%$customerName%"))

        while (cursor.moveToNext()) {
            val salesId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SALES_ID))
            val serialOfBeer = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERIAL_OF_BEER))
            val numberSold = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_SOLD))
            val priceTotal = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE_TOTAL))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val userName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME))
            val customer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME))

            val sale = SalesDTO(salesId, serialOfBeer, numberSold, priceTotal, date, userName, customer)
            salesList.add(sale)
        }

        cursor.close()
        db.close()
        return salesList
    }

    /* Buscar ventas por número de serie de cerveza */
    fun getSalesByBeerSerial(serialNumber: Int): List<SalesDTO> {
        val salesList = mutableListOf<SalesDTO>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_SERIAL_OF_BEER = ?"
        val cursor = db.rawQuery(query, arrayOf(serialNumber.toString()))

        while (cursor.moveToNext()) {
            val salesId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SALES_ID))
            val serialOfBeer = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERIAL_OF_BEER))
            val numberSold = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NUMBER_SOLD))
            val priceTotal = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE_TOTAL))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val userName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME))
            val customerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME))

            val sale = SalesDTO(salesId, serialOfBeer, numberSold, priceTotal, date, userName, customerName)
            salesList.add(sale)
        }

        cursor.close()
        db.close()
        return salesList
    }
}