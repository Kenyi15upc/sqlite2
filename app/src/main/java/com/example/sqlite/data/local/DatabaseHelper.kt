package com.example.sqlite.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.sqlite.data.model.Employee

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "talent_track.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_EMPLOYEES = "employees"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NOMBRE = "nombre"
        private const val COLUMN_APELLIDOS = "apellidos"
        private const val COLUMN_EDAD = "edad"
        private const val COLUMN_PROFESION = "profesion"
        private const val COLUMN_CARGO = "cargo"
        private const val COLUMN_EXPERIENCIA = "experiencia"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_TELEFONO = "telefono"
        private const val COLUMN_DIRECCION = "direccion"
        private const val COLUMN_DISTRITO = "distrito"
        private const val COLUMN_CIUDAD = "ciudad"
        private const val COLUMN_PAIS = "pais"
        private const val COLUMN_ESTADO_CIVIL = "estado_civil"
        private const val COLUMN_GENERO = "genero"
        private const val COLUMN_SALARIO = "salario"
        private const val COLUMN_DEPARTAMENTO = "departamento"
        private const val COLUMN_FOTO = "foto"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_EMPLOYEES (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_APELLIDOS TEXT,
                $COLUMN_EDAD INTEGER,
                $COLUMN_PROFESION TEXT,
                $COLUMN_CARGO TEXT,
                $COLUMN_EXPERIENCIA INTEGER,
                $COLUMN_EMAIL TEXT,
                $COLUMN_TELEFONO TEXT,
                $COLUMN_DIRECCION TEXT,
                $COLUMN_DISTRITO TEXT,
                $COLUMN_CIUDAD TEXT,
                $COLUMN_PAIS TEXT,
                $COLUMN_ESTADO_CIVIL TEXT,
                $COLUMN_GENERO TEXT,
                $COLUMN_SALARIO REAL,
                $COLUMN_DEPARTAMENTO TEXT,
                $COLUMN_FOTO TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EMPLOYEES")
        onCreate(db)
    }

    fun insertEmployee(employee: Employee): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, employee.id)
            put(COLUMN_NOMBRE, employee.nombre)
            put(COLUMN_APELLIDOS, employee.apellidos)
            put(COLUMN_EDAD, employee.edad)
            put(COLUMN_PROFESION, employee.profesion)
            put(COLUMN_CARGO, employee.cargo)
            put(COLUMN_EXPERIENCIA, employee.experiencia)
            put(COLUMN_EMAIL, employee.email)
            put(COLUMN_TELEFONO, employee.telefono)
            put(COLUMN_DIRECCION, employee.direccion)
            put(COLUMN_DISTRITO, employee.distrito)
            put(COLUMN_CIUDAD, employee.ciudad)
            put(COLUMN_PAIS, employee.pais)
            put(COLUMN_ESTADO_CIVIL, employee.estadoCivil)
            put(COLUMN_GENERO, employee.genero)
            put(COLUMN_SALARIO, employee.salario)
            put(COLUMN_DEPARTAMENTO, employee.departamento)
            put(COLUMN_FOTO, employee.foto)
        }

        val result = db.insertWithOnConflict(
            TABLE_EMPLOYEES,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
        return result != -1L
    }

    fun getAllEmployees(): List<Employee> {
        val employeeList = mutableListOf<Employee>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_EMPLOYEES", null)

        if (cursor.moveToFirst()) {
            do {
                val employee = Employee(
                    id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)) ?: "",
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOMBRE)) ?: "",
                    apellidos = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_APELLIDOS)) ?: "",
                    edad = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EDAD)),
                    profesion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PROFESION)) ?: "",
                    cargo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARGO)) ?: "",
                    experiencia = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPERIENCIA)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)) ?: "",
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEFONO)) ?: "",
                    direccion = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIRECCION)) ?: "",
                    distrito = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISTRITO)) ?: "",
                    ciudad = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CIUDAD)) ?: "",
                    pais = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAIS)) ?: "",
                    estadoCivil = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ESTADO_CIVIL)) ?: "",
                    genero = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENERO)) ?: "",
                    salario = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_SALARIO)),
                    departamento = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTAMENTO)) ?: "",
                    foto = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOTO)) ?: ""
                )
                employeeList.add(employee)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return employeeList
    }

    fun updateEmployee(employee: Employee): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE, employee.nombre)
            put(COLUMN_APELLIDOS, employee.apellidos)
            put(COLUMN_EDAD, employee.edad)
            put(COLUMN_PROFESION, employee.profesion)
            put(COLUMN_CARGO, employee.cargo)
            put(COLUMN_EXPERIENCIA, employee.experiencia)
            put(COLUMN_EMAIL, employee.email)
            put(COLUMN_TELEFONO, employee.telefono)
            put(COLUMN_DIRECCION, employee.direccion)
            put(COLUMN_DISTRITO, employee.distrito)
            put(COLUMN_CIUDAD, employee.ciudad)
            put(COLUMN_PAIS, employee.pais)
            put(COLUMN_ESTADO_CIVIL, employee.estadoCivil)
            put(COLUMN_GENERO, employee.genero)
            put(COLUMN_SALARIO, employee.salario)
            put(COLUMN_DEPARTAMENTO, employee.departamento)
            put(COLUMN_FOTO, employee.foto)
        }

        val rowsAffected = db.update(
            TABLE_EMPLOYEES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(employee.id)
        )
        db.close()
        return rowsAffected > 0
    }

    fun deleteEmployee(id: String): Boolean {
        val db = writableDatabase
        val rowsAffected = db.delete(TABLE_EMPLOYEES, "$COLUMN_ID = ?", arrayOf(id))
        db.close()
        return rowsAffected > 0
    }

    fun calculateTotalSalaries(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_SALARIO) FROM $TABLE_EMPLOYEES", null)
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        db.close()
        return total
    }
}
