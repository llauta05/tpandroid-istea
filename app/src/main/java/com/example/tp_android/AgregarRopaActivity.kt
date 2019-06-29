package com.example.tp_android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.tp_android.database.DBHelper

class AgregarRopaActivity : AppCompatActivity(){

    lateinit var etNombre: EditText
    lateinit var etPrecio: EditText
    lateinit var etMarca: EditText
    lateinit var btnAccion: Button
    var modalidad: Int = MainActivity.AGREGAR
    var ropa: Ropa? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_agregar_ropa)

        modalidad = intent.extras!!.getInt(MainActivity.MODALIDAD)
        ropa = intent.extras!!.getSerializable(MainActivity.ROPA) as Ropa?

        setupUI()
        initializeBtnAgregar()
    }

    private fun initializeBtnAgregar() {
        btnAccion.setOnClickListener {
            if (datosCompletos()) {
                if (modalidad == MainActivity.AGREGAR) {
                    insertarRopa()

                } else {
                    modificarRopa()
                }

                finish()
            } else {
                //Cambiar por otra notificacion
                Toast.makeText(this, "Completar datos", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun setupUI() {
        etNombre = findViewById(R.id.etNombre)
        etPrecio = findViewById(R.id.etPrecio)
        etMarca = findViewById(R.id.etMarca)
        btnAccion = findViewById(R.id.btnAccion)

        if (modalidad == MainActivity.AGREGAR) {
            btnAccion.text = getString(R.string.agregar)
        } else {
            btnAccion.text = getString(R.string.modificar)
            etNombre.setText(ropa?.nombre)
            etPrecio.setText(ropa?.precio)
            etMarca.setText(ropa?.marca)
        }
    }

    private fun modificarRopa() {
        val db = DBHelper(this)
        ropa?.nombre = etNombre.text.toString()
        ropa?.precio = etPrecio.text.toString()
        ropa?.marca = etMarca.text.toString()
        db.modificarRopa(ropa!!)
    }

    private fun datosCompletos(): Boolean {
        return !etNombre.text.toString().isEmpty() &&
                !etPrecio.text.toString().isEmpty() &&
                !etMarca.text.toString().isEmpty()
    }

    private fun insertarRopa() {
        val db = DBHelper(this)
        val nombre = etNombre.text.toString()
        val precio = etPrecio.text.toString()
        val marca = etMarca.text.toString()

        db.insertarRopa(Ropa(0, nombre, precio, marca))
    }


}