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
import com.example.majika.model.MenuRecyclerViewItem
import com.example.majika.model.Title

class MenuItemAdapter (private val menus: List<MenuRecyclerViewItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_CARD = 1
        const val VIEW_TEXT = 2
    }
    override fun getItemViewType(position: Int): Int {
        if (menus[position] is Menu) {
            return VIEW_CARD
        } else {
            return VIEW_TEXT
        }
    }
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val menuNameText: TextView = view.findViewById(R.id.item_name_menu)
        val menuPriceText: TextView = view.findViewById(R.id.item_price_menu)
        val menuSoldText: TextView = view.findViewById(R.id.item_num_sold_menu)
        val menuDescText: TextView = view.findViewById(R.id.item_desc_menu)
        val menuCard: CardView = view.findViewById(R.id.cardViewMenu)
        val menuBtPlus: Button = view.findViewById(R.id.item_bt_menu_plus)
        val menuBuyText: TextView = view.findViewById(R.id.item_num_buy_menu)
        val menuBtMin: Button = view.findViewById(R.id.item_bt_menu_min)
        
        fun bind(item: Menu) {
            menuNameText.text = item.nameMenu
            menuPriceText.text = item.priceMenu.toString()
            menuSoldText.text = item.numSoldMenu.toString()
            menuDescText.text = item.descMenu
            menuBuyText.text = item.numBuyMenu.toString()
            menuBtPlus.setOnClickListener { v: View ->
                Toast.makeText(
                    v.context,
                    "Adding 1 " + item.nameMenu,
                    Toast.LENGTH_SHORT
                ).show()
                menuBuyText.text = (item.numBuyMenu + 1).toString()
            }
            menuBtMin.setOnClickListener { v: View ->
                Toast.makeText(
                    v.context,
                    "Removing 1 " + item.nameMenu,
                    Toast.LENGTH_SHORT
                ).show()
                menuBuyText.text = (item.numBuyMenu - 1).toString()
            }
        }
    }
    class TextViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val menuNameText: TextView = view.findViewById(R.id.item_text)

        fun bind(item: Title) {
            menuNameText.text = item.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_CARD) {
            return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_menu_model, parent, false))
        } else {
            return TextViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_title_model, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = menus[position]
        if (holder is ItemViewHolder && item is Menu) {
            holder.bind(item)
        }
        if (holder is TextViewHolder && item is Title) {
            holder.bind(item)
        }
    }
}