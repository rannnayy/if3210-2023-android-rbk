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
import com.example.majika.model.Menu

class MenuItemAdapter (private val menus: List<Menu>) : RecyclerView.Adapter<MenuItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val menuNameText: TextView = view.findViewById(R.id.item_name_menu)
        val menuPriceText: TextView = view.findViewById(R.id.item_price_menu)
        val menuSoldText: TextView = view.findViewById(R.id.item_num_sold_menu)
        val menuDescText: TextView = view.findViewById(R.id.item_desc_menu)
        val menuCard: CardView = view.findViewById(R.id.cardViewMenu)
        val menuBtPlus: Button = view.findViewById(R.id.item_bt_menu_plus)
        val menuBuyText: TextView = view.findViewById(R.id.item_num_buy_menu)
        val menuBtMin: Button = view.findViewById(R.id.item_bt_menu_min)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.card_menu_model, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.menuNameText.text = menus[position].nameMenu
        holder.menuPriceText.text = menus[position].priceMenu.toString()
        holder.menuSoldText.text = menus[position].numSoldMenu.toString()
        holder.menuDescText.text = menus[position].descMenu.toString()
        holder.menuBuyText.text = menus[position].numBuyMenu.toString()
        holder.menuBtPlus.setOnClickListener{v: View ->
            Toast.makeText(v.context, "Adding 1 "+menus[position].nameMenu, Toast.LENGTH_SHORT).show()
            holder.menuBuyText.text = (menus[position].numBuyMenu.toInt()+1).toString()
        }
        holder.menuBtMin.setOnClickListener{v: View ->
            Toast.makeText(v.context, "Removing 1 "+menus[position].nameMenu, Toast.LENGTH_SHORT).show()
            holder.menuBuyText.text = (menus[position].numBuyMenu.toInt()-1).toString()
        }

    }
}