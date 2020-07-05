package com.example.numberpickersample
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rowHeight = resources.getDimensionPixelSize(R.dimen.data_picker_item)
        val listHeight = resources.getDimensionPixelSize(R.dimen.data_picker_size)
        val customList = findViewById<CustomNumberPicker>(R.id.custom_number_picker)
        val data = arrayListOf<String>()
        for( i in 1 .. (listHeight/rowHeight / 2)) {
            data += ""
        }
        for (i in 1..30){
            data += i.toString()
        }
        for( i in 1 .. (listHeight/rowHeight) / 2) {
            data += ""
        }
        val adapter = CustomBaseAdapter(this, R.layout.custom_list_item, data.toTypedArray())
        customList.adapter = adapter
    }



}