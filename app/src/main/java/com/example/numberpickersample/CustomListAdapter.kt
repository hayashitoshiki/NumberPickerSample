package com.example.numberpickersample

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomListAdapter internal constructor(myDataset: ArrayList<String>) : RecyclerView.Adapter<CustomListAdapter.ViewHolder>() {
    private var dataset = arrayListOf<String>()

    // 参照するviewの定義
    open class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTextView: TextView

        init {
            mTextView = v.findViewById(R.id.number_text)
        }
    }

    // Viewの親となるレイアウトの定義
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.custom_list_item, parent, false)
        return ViewHolder(view)
    }

    // 中身のデータの定義　(invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.text = dataset[position]
        if (dataset[position] == "5") {
            holder.mTextView.setTextColor(Color.RED)
        } else {
            holder.mTextView.setTextColor(Color.BLACK)
        }
    }

    // データのサイズを返す (invoked by the layout manager)
    override fun getItemCount(): Int {
        return dataset.size
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    init {
        dataset = myDataset
    }
}