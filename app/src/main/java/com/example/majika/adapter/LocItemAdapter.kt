package com.example.majika.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.Loc

class LocItemAdapter (private val locs: List<Loc>) : RecyclerView.Adapter<LocItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val locTitleText: TextView = view.findViewById(R.id.item_title_loc)
        val locDescText: TextView = view.findViewById(R.id.item_desc_loc)
        val locTelText: TextView = view.findViewById(R.id.item_telp_loc)
        val locCard: CardView = view.findViewById(R.id.cardViewLoc)
        val buttonCard: Button = view.findViewById(R.id.item_bt_loc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.card_loc_model, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return locs.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.locTitleText.text = locs[position].locTitle
        holder.locDescText.text = locs[position].locDesc
        holder.locTelText.text = locs[position].locTel.toString()
        holder.buttonCard.setOnClickListener{v: View ->
            Toast.makeText(v.context, "Opening", Toast.LENGTH_SHORT).show()
        }
    }
}