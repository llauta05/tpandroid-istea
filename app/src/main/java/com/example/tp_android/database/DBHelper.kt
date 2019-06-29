package com.example.tp_android.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tp_android.Ropa
import com.example.tp_android.database.DBConstants.Companion.ID
import com.example.tp_android.database.DBConstants.Companion.MARCA_ROPA
import com.example.tp_android.database.DBConstants.Companion.NOMBRE_ROPA
import com.example.tp_android.database.DBConstants.Companion.PRECIO_ROPA
import com.example.tp_android.database.DBConstants.Companion.TABLA_ROPA

class DBHelper(context: Context) :
        SQLiteOpenHelper(context, DBConstants.DBName, null, DBConstants.DBVersion){

 override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion){
            db.execSQL("drop table $TABLA_ROPA")
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table if not exists $TABLA_ROPA ($ID integer primary key, $NOMBRE_ROPA text, $PRECIO_ROPA text, $MARCA_ROPA text)")
    }

    fun insertarRopa(ropa: Ropa): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOMBRE_ROPA, ropa.nombre)
        contentValues.put(PRECIO_ROPA, ropa.precio)
        contentValues.put(MARCA_ROPA, ropa.marca)

        val result = db.insert(TABLA_ROPA, null, contentValues).toInt()
        db.close()

        return result != -1
    }
    fun getRopas(): List<Ropa> {
        val ropas: ArrayList<Ropa> = ArrayList()
        val db = this.readableDatabase
        var cursor = db.rawQuery("select * from $TABLA_ROPA", null)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val id = cursor.getInt(cursor.getColumnIndex(ID))
                val nombre = cursor.getString(cursor.getColumnIndex(NOMBRE_ROPA))
                val precio = cursor.getString(cursor.getColumnIndex(PRECIO_ROPA))
                val marca = cursor.getString(cursor.getColumnIndex(MARCA_ROPA))

                ropas.add(Ropa(id, nombre, "$$precio",marca))
                cursor.moveToNext()
            }
        }

        cursor.close()
        db.close()

        return ropas
    }
    fun modificarRopa(ropa: Ropa) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NOMBRE_ROPA, ropa.nombre)
        contentValues.put(PRECIO_ROPA, ropa.precio)
        contentValues.put(MARCA_ROPA, ropa.marca)

        db.update(TABLA_ROPA, contentValues, "$ID = ?", arrayOf(ropa.id.toString()))
        db.close()
    }

    fun eliminarRopa(ropa: Ropa) {
        val db = this.writableDatabase

        db.delete(TABLA_ROPA, "$ID = ?", arrayOf(ropa.id.toString()))

        db.close()
    }
}