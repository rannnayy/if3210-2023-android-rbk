package com.example.majika.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
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
        val buttonEmail: Button = view.findViewById(R.id.item_bt_email)
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
        holder.locTelText.text = locs[position].locTel
        holder.buttonCard.setOnClickListener{v: View ->
            Toast.makeText(v.context, "Opening Google Maps...", Toast.LENGTH_SHORT).show()
            val gmmIntentUri = Uri.parse("geo:"+locs[position].locLatitude+","+locs[position].locLongitude)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            v.context.startActivity(mapIntent)
        }
        holder.buttonEmail.setOnClickListener{v: View ->
            Toast.makeText(v.context, "Opening Gmail...", Toast.LENGTH_SHORT).show()
            val mailIntentUri = Uri.fromParts("mailto", locs[position].locEmail, null)
            val mailIntent = Intent(Intent.ACTION_SENDTO, mailIntentUri)
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback our restaurant")
            v.context.startActivity(mailIntent)
        }
    }
}