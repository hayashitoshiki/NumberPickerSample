package com.example.numberpickersample

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.widget.ListView
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var picker: NumberPicker
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_number_picker)


        val data = arrayListOf<String>()
        var oldY = 0F
        var oldPosition = 0
        for (i in 1..30){
            data += i.toString()
        }
        val listView: ListView = findViewById(R.id.list_view)
        val adapter = CustomBaseAdapter(this, R.layout.custom_list_item, data.toTypedArray())


        listView.adapter = adapter
        listView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    oldPosition = listView.firstVisiblePosition
                    oldY = event.y}
                MotionEvent.ACTION_UP -> {
                    // 自動修正
                    val newPosition = ((event.y - oldY) / 50).toInt()
                    val position = oldPosition - newPosition
                    when ((event.y - oldY) % 50) {
                        in -50 until -25 ->{timerDelayRunForScroll(listView,listView.firstVisiblePosition + 1)}
                        in -25 until 0 ->{timerDelayRunForScroll(listView,listView.firstVisiblePosition)}
                        in 0 until 25 ->{timerDelayRunForScroll(listView,listView.firstVisiblePosition + 1)}
                        in 26 until 50 -> {timerDelayRunForScroll(listView,listView.firstVisiblePosition)}
                    }
                }
            }
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        }



        picker = findViewById<NumberPicker>(R.id.number_piker)
        picker.minValue = 0
        picker.maxValue = 10
    }

    // 自動補正
    private fun timerDelayRunForScroll(lstView: ListView, position: Int) {
        val handler = Handler()
        handler.postDelayed( {
            try {
                lstView.smoothScrollToPositionFromTop(position,0)
            } catch (e: Exception) {}
        }, 50)
    }

}