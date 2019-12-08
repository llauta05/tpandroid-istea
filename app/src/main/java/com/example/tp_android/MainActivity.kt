package com.example.tp_android

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tp_android.database.DBHelper

class MainActivity : AppCompatActivity() {

    private lateinit var userName: String
    private var cambiarColor: Boolean = false

    lateinit var rvRopa: RecyclerView
    lateinit var lyMain: LinearLayout
    lateinit var toolbar: Toolbar

    lateinit var ropasAdapter: RopaAdapter

    companion object {
        val ROPA = "ROPA"
        val MODALIDAD = "MODALIDAD"
        val AGREGAR = 1
        val MODIFICAR = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        asignarValoresSettings()

        setupUI()
        setupToolbar()
        Toast.makeText(this, "$userName", Toast.LENGTH_LONG).show()
        if (cambiarColor) {
            lyMain = findViewById(R.id.mainLayout)
            lyMain.setBackgroundColor(Color.BLACK)
            toolbar.setBackgroundColor(Color.GRAY)
            rvRopa.setBackgroundColor(Color.GRAY)
        }
    }



    private fun asignarValoresSettings() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        userName = pref.getString("etUserName", "Algo").orEmpty()
        cambiarColor = pref.getBoolean("chkColor", false)
    }
    override fun onResume() {
        super.onResume()
        actualizarRopas()
    }

    private fun actualizarListaRopas(ropas: List<Ropa>) {
        ropasAdapter.ropas = ropas
        ropasAdapter.notifyDataSetChanged()
    }

    private fun setupToolbar() {
       setSupportActionBar(toolbar)
       supportActionBar?.title = "Sport Shop"
    }

    private fun setupUI() {
        rvRopa = findViewById(R.id.rvRopa)
        toolbar = findViewById(R.id.toolbar)

        ropasAdapter = RopaAdapter(ArrayList(), object : OnRopaClickListener {
            override fun onItemClick(ropa: Ropa) {
                ConfirmarCompra(ropa)
            }
            override fun onItemClickEdit(ropa: Ropa) {

                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle(ropa.nombre)
                    .setPositiveButton(Html.fromHtml("<font color='#000000'>Ok</font>"), { dialog, _ -> dialog.dismiss() })
                    .setNeutralButton(Html.fromHtml("<font color='#000000'>Modificar</font>"), { dialog, _ -> goToModificar(ropa) })
                    .setNegativeButton(Html.fromHtml("<font color='#000000'>Eliminar</font>"), { dialog, _ -> eliminarRopa(ropa) })
                    .show()
            }

        })
        rvRopa.adapter = ropasAdapter
    }

    private fun ConfirmarCompra(ropa: Ropa) {

        val intent = Intent(this, ConfimarCompraActivity::class.java).apply {
            putExtra(MODALIDAD, MODIFICAR)
            putExtra(ROPA, ropa)
        }
        startActivity(intent)

    }

    private fun eliminarRopa(ropa: Ropa) {
        val db = DBHelper(this)
        db.eliminarRopa(ropa)
        actualizarRopas()
    }

    private fun goToModificar(ropa: Ropa) {
        val intent = Intent(this, AgregarRopaActivity::class.java)
        intent.putExtra(MODALIDAD, MODIFICAR)
        intent.putExtra(ROPA, ropa)
        startActivity(intent)
    }


    private fun actualizarRopas() {
        ObtenerRopas().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.addRopa) {
            val intent = Intent(this, AgregarRopaActivity::class.java)
            intent.putExtra(MODALIDAD, AGREGAR)
            startActivity(intent)
        }

        if (item.itemId == R.id.btnSettings) {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    inner class ObtenerRopas : AsyncTask<Unit, Unit, List<Ropa>>() {

        override fun doInBackground(vararg params: Unit?): List<Ropa> {
            val db = DBHelper(this@MainActivity)
            return db.getRopas()
        }

        override fun onPostExecute(result: List<Ropa>) {
            super.onPostExecute(result)
            actualizarListaRopas(result)
        }

    }

    inner class BorrarRopa : AsyncTask<Ropa, Unit, Unit>() {

        override fun doInBackground(vararg params: Ropa) {
            val ropa = params[0]
            val db = DBHelper(this@MainActivity)
            db.eliminarRopa(ropa)
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)
            actualizarRopas()
        }

    }
}
