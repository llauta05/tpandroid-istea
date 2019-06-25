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
                    createNotificationChannel()
                    mostrarNotificacion()
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
        btnAccion = findViewById(R.id.btnAccion)

        if (modalidad == MainActivity.AGREGAR) {
            btnAccion.text = getString(R.string.agregar)
        } else {
            btnAccion.text = getString(R.string.modificar)
            etNombre.setText(ropa?.nombre)
            etPrecio.setText(ropa?.precio)
        }
    }
    private fun mostrarNotificacion() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Ropas")
            .setContentText("Se agrego un ropa")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this@AgregarRopaActivity,
            0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        val managerCompat = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        managerCompat.notify(1234, builder.build())
    }

    private fun modificarRopa() {
        val db = DBHelper(this)
        ropa?.nombre = etNombre.text.toString()
        ropa?.precio = etPrecio.text.toString()
        db.modificarRopa(ropa!!)
    }

    private fun datosCompletos(): Boolean {
        return !etNombre.text.toString().isEmpty() &&
                !etPrecio.text.toString().isEmpty()
    }

    private fun insertarRopa() {
        val db = DBHelper(this)
        val nombre = etNombre.text.toString()
        val precio = etPrecio.text.toString()

        db.insertarRopa(Ropa(0, nombre, precio))
    }

    private val CHANNEL_ID: String = "Agregar Ropa"

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Agregar ropa"
            val descriptionText = "Se notificara en el momento que se agrego un ropa"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}