package com.example.proyectobeer.beer.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BeerDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "beer.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "beer"
        private const val COLUMN_SERIAL_NUMBER = "serialNumber"
        private const val COLUMN_BRAND = "brand"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_ABV = "abv"
        private const val COLUMN_IBU = "ibu"
        private const val COLUMN_PROVIDER = "provider"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // 1. Crear la tabla
        val queryCreator = """
            CREATE TABLE $TABLE_NAME(
                $COLUMN_SERIAL_NUMBER INTEGER PRIMARY KEY,
                $COLUMN_BRAND TEXT,
                $COLUMN_TYPE TEXT,
                $COLUMN_ABV TEXT,
                $COLUMN_IBU TEXT,
                $COLUMN_PROVIDER TEXT,
                $COLUMN_PRICE INTEGER,
                $COLUMN_QUANTITY INTEGER
            )
        """.trimIndent()
        db?.execSQL(queryCreator)

        // 2. Crear una cerveza por defecto
        val queryInsert = """
            INSERT INTO $TABLE_NAME(
                $COLUMN_BRAND, $COLUMN_TYPE, $COLUMN_ABV, $COLUMN_IBU, 
                $COLUMN_PROVIDER, $COLUMN_PRICE, $COLUMN_QUANTITY
            ) VALUES ('Corona', 'Lager', '4.5%', '18', 'Distribuidora Central', 2500, 100)
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

    /* Insertar cerveza */
    fun insertBeer(beer: BeerDTO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BRAND, beer.brand)
            put(COLUMN_TYPE, beer.type)
            put(COLUMN_ABV, beer.ABV)
            put(COLUMN_IBU, beer.IBU)
            put(COLUMN_PROVIDER, beer.provider)
            put(COLUMN_PRICE, beer.price)
            put(COLUMN_QUANTITY, beer.quantity)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    /* Actualizar cerveza */
    fun updateBeer(beer: BeerDTO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BRAND, beer.brand)
            put(COLUMN_TYPE, beer.type)
            put(COLUMN_ABV, beer.ABV)
            put(COLUMN_IBU, beer.IBU)
            put(COLUMN_PROVIDER, beer.provider)
            put(COLUMN_PRICE, beer.price)
            put(COLUMN_QUANTITY, beer.quantity)
        }
        val whereClause = "$COLUMN_SERIAL_NUMBER = ?"
        val whereArgs = arrayOf(beer.serialNumber.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    /* Eliminar cerveza por ID */
    fun deleteBeerById(serialNumber: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_SERIAL_NUMBER = ?"
        val whereArgs = arrayOf(serialNumber.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }

    /* Obtener cerveza por ID */
    fun getBeerById(serialNumber: Int): BeerDTO {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE ${COLUMN_SERIAL_NUMBER} = $serialNumber"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERIAL_NUMBER))
        val brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND))
        val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
        val abv = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ABV))
        val ibu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IBU))
        val provider = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVIDER))
        val price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
        val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))

        cursor.close()
        db.close()

        return BeerDTO(id, brand, type, abv, ibu, provider, price, quantity)
    }


    /* Obtener todas las cervezas */
    fun getAllBeers(): List<BeerDTO> {
        val beerList = mutableListOf<BeerDTO>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val serialNumber = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERIAL_NUMBER))
            val brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
            val abv = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ABV))
            val ibu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IBU))
            val provider = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVIDER))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))

            val beer = BeerDTO(serialNumber, brand, type, abv, ibu, provider, price, quantity)
            beerList.add(beer)
        }

        cursor.close()
        db.close()
        return beerList
    }

    /* Buscar cervezas por marca */
    fun getBeersByBrand(brand: String): List<BeerDTO> {
        val beerList = mutableListOf<BeerDTO>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_BRAND LIKE ?"
        val cursor = db.rawQuery(query, arrayOf("%$brand%"))

        while (cursor.moveToNext()) {
            val serialNumber = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SERIAL_NUMBER))
            val brandName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND))
            val type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE))
            val abv = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ABV))
            val ibu = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IBU))
            val provider = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROVIDER))
            val price = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY))

            val beer = BeerDTO(serialNumber, brandName, type, abv, ibu, provider, price, quantity)
            beerList.add(beer)
        }

        cursor.close()
        db.close()
        return beerList
    }
}