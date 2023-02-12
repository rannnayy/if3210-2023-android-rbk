package com.example.majika.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.R
import com.example.majika.model.Menu

class MenuItemAdapter (private val menus: List<Menu>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_CARD = 1
        const val VIEW_TEXT = 2
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
    }
    class TextViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val menuNameText: TextView = view.findViewById(R.id.item_name_menu)
        val menuPriceText: TextView = view.findViewById(R.id.item_price_menu)
        val menuSoldText: TextView = view.findViewById(R.id.item_num_sold_menu)
        val menuDescText: TextView = view.findViewById(R.id.item_desc_menu)
        val menuCard: CardView = view.findViewById(R.id.cardViewMenu)
        val menuBtPlus: Button = view.findViewById(R.id.item_bt_menu_plus)
        val menuBuyText: TextView = view.findViewById(R.id.item_num_buy_menu)
        val menuBtMin: Button = view.findViewById(R.id.item_bt_menu_min)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_CARD) {
            val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.card_menu_model, parent, false)
            return ItemViewHolder(adapterLayout)
        } else {
            val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.card_menu_model, parent, false)
            return TextViewHolder(adapterLayout)
        }
    }

    override fun getItemCount(): Int {
        return menus.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (menus[position].type === VIEW_CARD) {
            (holder as ItemViewHolder).menuNameText.text = menus[position].nameMenu
            (holder as ItemViewHolder).menuPriceText.text = menus[position].priceMenu.toString()
            (holder as ItemViewHolder).menuSoldText.text = menus[position].numSoldMenu.toString()
            (holder as ItemViewHolder).menuDescText.text = menus[position].descMenu
            (holder as ItemViewHolder).menuBuyText.text = menus[position].numBuyMenu.toString()
            (holder as ItemViewHolder).menuBtPlus.setOnClickListener { v: View ->
                Toast.makeText(
                    v.context,
                    "Adding 1 " + menus[position].nameMenu,
                    Toast.LENGTH_SHORT
                ).show()
                holder.menuBuyText.text = (menus[position].numBuyMenu.toInt() + 1).toString()
            }
            (holder as ItemViewHolder).menuBtMin.setOnClickListener { v: View ->
                Toast.makeText(
                    v.context,
                    "Removing 1 " + menus[position].nameMenu,
                    Toast.LENGTH_SHORT
                ).show()
                (holder as ItemViewHolder).menuBuyText.text = (menus[position].numBuyMenu.toInt() - 1).toString()
            }
        }
        else {
            (holder as TextViewHolder).menuCard.layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            (holder as TextViewHolder).menuNameText.text = menus[position].nameMenu
            (holder as TextViewHolder).menuNameText.setTextColor(Color.BLACK)
            (holder as TextViewHolder).menuNameText.setTypeface((holder as TextViewHolder).menuNameText.typeface, Typeface.BOLD)
            (holder as TextViewHolder).menuPriceText.visibility = View.GONE
            (holder as TextViewHolder).menuSoldText.visibility = View.GONE
            (holder as TextViewHolder).menuDescText.visibility = View.GONE
            (holder as TextViewHolder).menuBuyText.visibility = View.GONE
            (holder as TextViewHolder).menuBtPlus.visibility = View.GONE
            (holder as TextViewHolder).menuBtMin.visibility = View.GONE
            (holder as TextViewHolder).menuCard.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return menus[position].type
    }
}