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

class ConfimarCompraActivity : AppCompatActivity() {
    var modalidad: Int = MainActivity.AGREGAR
    var ropa: Ropa? = null
    lateinit var etMail: EditText
    lateinit var btnComprar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiy_confirmar_compra)

        modalidad = intent.extras!!.getInt(MainActivity.MODALIDAD)
        ropa = intent.extras!!.getSerializable(MainActivity.ROPA) as Ropa?

        setupUI()
        initializeBtnAgregar()
    }
    private fun initializeBtnAgregar() {
        btnComprar.setOnClickListener {
            if (datosCompletos()) {

                createNotificationChannel()
                mostrarNotificacion()

                finish()
            } else {
                //Cambiar por otra notificacion
                Toast.makeText(this, "Completar datos", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun setupUI() {
        etMail = findViewById(R.id.etMail)
        btnComprar = findViewById(R.id.btnComprar)
    }
    private fun mostrarNotificacion() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.mountain)
            .setContentTitle("Muchas Gracias")
            .setContentText("¡Tu ${ropa!!.nombre} ${ropa!!.marca} te esta esperando!")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this@ConfimarCompraActivity,
            0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)

        val managerCompat = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        managerCompat.notify(1234, builder.build())
    }


    private fun datosCompletos(): Boolean {
        return !etMail.text.toString().isEmpty()
    }


    private val CHANNEL_ID: String = "Compra realizada"

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Compra realizada"
            val descriptionText = "Se notificara en el momento de realizar una compra"
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