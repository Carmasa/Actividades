package com.example.loginsimple

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "ecomerce.db"
        const val DATABASE_VERSION = 1

        const val TABLE_USERS = "usuarios"
        const val COL_USERNAME = "username"
        const val COL_EMAIL = "email"
        const val COL_PASSWORD = "password"

        const val TABLE_ALUMNOS = "alumnos"
        const val TABLE_PROFESORES = "profesores"
        const val COL_NOMBRE = "nombre"
        const val COL_APELLIDO = "apellido"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USERNAME TEXT,
                $COL_EMAIL TEXT,
                $COL_PASSWORD TEXT
            )
        """.trimIndent()

        val createAlumnosTable = """
            CREATE TABLE $TABLE_ALUMNOS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT,
                $COL_APELLIDO TEXT
            )
        """.trimIndent()

        val createProfesoresTable = """
            CREATE TABLE $TABLE_PROFESORES (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT,
                $COL_APELLIDO TEXT
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createAlumnosTable)
        db.execSQL(createProfesoresTable)

        seedData(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        // Insertar profesores
        insertProfesor(db, "Juan", "Pérez")
        insertProfesor(db, "María", "García")

        // Insertar alumnos
        insertAlumno(db, "Lucas", "Rodríguez")
        insertAlumno(db, "Ana", "Martínez")
        insertAlumno(db, "Pedro", "Sánchez")
        insertAlumno(db, "Elena", "Gómez")
        insertAlumno(db, "Marcos", "López")
    }

    private fun insertProfesor(db: SQLiteDatabase, nombre: String, apellido: String) {
        val values = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_APELLIDO, apellido)
        }
        db.insert(TABLE_PROFESORES, null, values)
    }

    private fun insertAlumno(db: SQLiteDatabase, nombre: String, apellido: String) {
        val values = ContentValues().apply {
            put(COL_NOMBRE, nombre)
            put(COL_APELLIDO, apellido)
        }
        db.insert(TABLE_ALUMNOS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALUMNOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PROFESORES")
        onCreate(db)
    }
}
