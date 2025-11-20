package com.example.proyectobeer.login.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlin.toString

class UserDataHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private var DATABASE_NAME = "user.db"
        private var DATABASE_VERSION = 1
        private var TABLA_NAME = "user"
        private var COLUMN_ID = "id"
        private var COLUMM_USERNAME = "username"
        private var COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //1. Crear la tabla
        val queryCreator =
            "CREATE TABLE $TABLA_NAME(" +
                    "$COLUMN_ID INTEGER PRIMARY KEY ," +
                    " $COLUMM_USERNAME TEXT UNIQUE , $COLUMN_PASSWORD TEXT)"
        db?.execSQL(queryCreator)
        //2.Crear un usuario por defecto
        //val queryInsert = "INSERT INTO $TABLA_NAME($COLUMM_USERNAME, $COLUMN_PASSWORD), VALUES ('javier' , 5'123')"
        val queryInsert =
            "INSERT INTO $TABLA_NAME($COLUMM_USERNAME, $COLUMN_PASSWORD) VALUES ('javier', '123')"
        db?.execSQL(queryInsert)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        //1.Elimina la tabla anterior
        val sqlDrop = "DROP TABLE IF EXISTS $TABLA_NAME"
        db?.execSQL(sqlDrop)
        //2. Crea de nuevo la tabla
        this.onCreate(db)
    }

    /*1. Valida si un usuario existe en la tabla */
    fun validatUser(user: UserDTO): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLA_NAME WHERE $COLUMM_USERNAME =? AND $COLUMN_PASSWORD =?"
        val cursor = db.rawQuery(query, arrayOf(user.nameuser, user.passwordUser))
        val exists = cursor.count > 0
        db.close()
        cursor.close()
        return exists
    }

    fun insertUser(user: UserDTO): Boolean {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COLUMM_USERNAME, user.nameuser)
                put(COLUMN_PASSWORD, user.passwordUser)
            }
            val result = db.insert(TABLA_NAME, null, values)
            db.close()
            result != -1L // Retorna true si se insertó correctamente
        } catch (e: Exception) {
            db.close()
            false // Retorna false si hubo un error (ej: usuario duplicado)
        }
    }

    fun updateUser(user: UserDTO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMM_USERNAME, user.nameuser)
            put(COLUMN_PASSWORD, user.passwordUser)
        }
        val whereClause = "${COLUMN_ID} = ?"
        val whereArgs = arrayOf(user.id.toString())
        db.update(TABLA_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun deleteUserById(nodeId: Int) {
        val db = writableDatabase
        val whereClause = "${COLUMN_ID} = ?"
        val whereArgs = arrayOf(nodeId.toString())
        db.delete(TABLA_NAME, whereClause, whereArgs)
        db.close()
    }

    fun getUserById(userId: Int): UserDTO {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLA_NAME WHERE ${COLUMN_ID} = $userId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMM_USERNAME))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
        cursor.close()
        db.close()
        return UserDTO(id, title, content)
    }

    /* Obtener usuarios*/
    fun getUser(): List<UserDTO> {
        val userList = mutableListOf<UserDTO>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLA_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMM_USERNAME))
            val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            val user: UserDTO = UserDTO(id, username, password)
            userList.add(user)
        }

        cursor.close()
        db.close()
        return userList
    }

    fun getUserByUsername(username: String): UserDTO? {
        val db = readableDatabase
        val cursor = db.query(
            TABLA_NAME,
            null,
            "$COLUMM_USERNAME = ?",
            arrayOf(username),
            null,
            null,
            null
        )

        var user: UserDTO? = null
        if (cursor.moveToFirst()) {
            user = UserDTO(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMM_USERNAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
            )
        }
        cursor.close()
        return user
    }
}
