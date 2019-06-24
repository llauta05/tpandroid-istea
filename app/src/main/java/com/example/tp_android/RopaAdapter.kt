package com.example.tp_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RopaAdapter(var ropas: List<Ropa>, var listener: OnRopaClickListener)
    : RecyclerView.Adapter<RopaAdapter.RopasViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RopasViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_ropa, viewGroup, false)
        return RopasViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ropas.size
    }

    override fun onBindViewHolder(holder: RopasViewHolder, position: Int) {
        holder.txtNombre.text = ropas[position].nombre
        holder.txtPrecio.text = ropas[position].precio
        holder.itemView.setOnClickListener {
            listener.onItemClick(ropas[position])
        }

    }

    class RopasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtNombre: TextView = view.findViewById(R.id.txtNombre)
        var txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
    }

}