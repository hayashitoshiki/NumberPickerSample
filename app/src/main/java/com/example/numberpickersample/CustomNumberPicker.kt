package com.example.numberpickersample

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.ListView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet



class CustomNumberPicker: LinearLayout {

    private lateinit var layout:ConstraintLayout
    private lateinit var listView: ListView
    private lateinit var filterTop: View
    private lateinit var filterBottom: View
    private lateinit var partitionTop: View
    private lateinit var partitionBottom: View

    private val rowHeight = resources.getDimension(R.dimen.data_picker_item)
    private val listHeight = resources.getDimension(R.dimen.data_picker_size)
    private val paddingHeight: Int = (rowHeight * 2.6).toInt()
    private val filterHeight = (listHeight - rowHeight) / 2
    private val constraintSet = ConstraintSet()
    private var offSetY = 0
    private var first = true

    var adapter: ListAdapter? = null
        set(value) {
            field = value
            listView.adapter = value
        }

    constructor(context: Context) : super(context) { init(null, 0) }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) { init(attrs, 0) }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) { init(attrs, defStyle) }


    private fun init(attrs: AttributeSet?, defStyle: Int) {
        layout = ConstraintLayout(context)
        layout.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )
        listView = ListView(context).apply {
            this.id = View.generateViewId()
            this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, listHeight.toInt())
            this.isVerticalScrollBarEnabled = false
        }
        filterTop = View(context).apply {
            this.id = View.generateViewId()
            this.layoutParams = ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, filterHeight.toInt())
            this.background = resources.getDrawable(R.drawable.gradation_white_top,null)
        }
        filterBottom = View(context).apply {
            this.id = View.generateViewId()
            this.layoutParams = ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, filterHeight.toInt())
            this.background = resources.getDrawable(R.drawable.gradation_white_bottom,null)
        }
        partitionTop = View(context).apply {
            this.id = View.generateViewId()
            this.layoutParams = ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1)
            this.setBackgroundColor(resources.getColor(android.R.color.darker_gray,null))
        }
        partitionBottom = View(context).apply {
            this.id = View.generateViewId()
            this.layoutParams = ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1)
            this.setBackgroundColor(resources.getColor(android.R.color.darker_gray,null))
        }
        layout.addView(listView)
        layout.addView(filterTop)
        layout.addView(filterBottom)
        layout.addView(partitionTop)
        layout.addView(partitionBottom)

        constraintSet.clone(layout)
        constraintSet.connect(filterTop.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        constraintSet.connect(filterBottom.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        constraintSet.connect(partitionTop.id, ConstraintSet.TOP, filterTop.id, ConstraintSet.BOTTOM, 1)
        constraintSet.connect(partitionBottom.id, ConstraintSet.BOTTOM, filterBottom.id, ConstraintSet.TOP, 1)
        constraintSet.applyTo(layout)
        this.addView(layout)

        // スクロール操作
        listView.setOnScrollListener(object : AbsListView.OnScrollListener {
            var positionOffset = 0

            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
                isScrollCompleted(scrollState)
            }
            override fun onScroll(
                view: AbsListView, firstVisibleItem: Int,
                visibleItemCount: Int, totalItemCount: Int
            ) {
                val startView = listView.getChildAt(0)
                positionOffset = if (startView == null) 0 else startView.top - listView.paddingTop
            }
            //現在地判定
            private fun isScrollCompleted(scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (listView.lastVisiblePosition == listView.adapter.count -1) {
                        // 最下部
                        timerDelayRunForScroll(listView, listView.adapter.count - 1 - (dpFromPx(listHeight) / dpFromPx(rowHeight)).toInt())
                    } else  if (positionOffset in -(paddingHeight / 2)..0) {
                        // 下へスクロール
                        timerDelayRunForScroll(listView, listView.firstVisiblePosition)
                    } else if (positionOffset in -paddingHeight..-(paddingHeight / 2)) {
                        if (listView.firstVisiblePosition == 0) {
                            // 最上部
                            timerDelayRunForScroll(listView, 0)
                        } else {
                            // 上へスクロール
                            timerDelayRunForScroll(listView, listView.firstVisiblePosition + 1)
                        }
                    }
                }
            }
            // 自動補正
            private fun timerDelayRunForScroll(lstView: ListView, position: Int) {
                val handler = Handler()
                handler.postDelayed( {
                    try {
                        lstView.smoothScrollToPositionFromTop(position, -offSetY,500)
                    } catch (e: Exception) {}
                }, 0)
            }
        })
    }


    // レイアウトファイル割り当て後設定
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (first) {
            constraintSet.constrainHeight(listView.id, this.height)
            constraintSet.constrainHeight(filterTop.id, (this.height - rowHeight).toInt() / 2)
            constraintSet.constrainHeight(filterBottom.id, (this.height - rowHeight).toInt() / 2)
            constraintSet.applyTo(layout)
            if((dpFromPx(listHeight) / dpFromPx(rowHeight) % 2).toInt() == 0) {
                offSetY += pxFromDp((dpFromPx(rowHeight) / 2) + (dpFromPx(listHeight) % dpFromPx(rowHeight) / 2 ))
            } else {
                offSetY += pxFromDp((dpFromPx(listHeight) % dpFromPx(rowHeight) / 2 ))
            }
            listView.scrollBy(0,-offSetY)
            first = false
        }
    }

    private fun pxFromDp(dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun dpFromPx(px: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (px / scale)
    }

}