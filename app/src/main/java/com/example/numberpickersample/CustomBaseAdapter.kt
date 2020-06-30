package com.example.numberpickersample

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomBaseAdapter (context: Context?, private val resourcedId: Int, private val items: Array<String>) : BaseAdapter() {
    private val inflater: LayoutInflater
    private lateinit var holder: ViewHolder

    internal class ViewHolder {
        var numberText: TextView? = null
    }

    init {
        this.inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun isEnabled(position: Int): Boolean {
        return false
    }

    // Viewの生成
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (view == null) {
            view = inflater.inflate(resourcedId, null)
            holder = ViewHolder()
        } else {
            holder = view.tag as ViewHolder
        }

        holder.numberText = view?.findViewById<TextView>(R.id.number_text)?.apply {
            this.text = items[position]
            if (items[position] == "5") {
                this.setTextColor(Color.RED)
            } else {
                this.setTextColor(Color.BLACK)
            }
        }

        view!!.tag = holder


        return view
    }


    // ListViewの数
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}