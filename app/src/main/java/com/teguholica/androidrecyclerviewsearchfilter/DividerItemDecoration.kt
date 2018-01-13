package com.teguholica.androidrecyclerviewsearchfilter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View


class DividerItemDecoration(
        private val context: Context,
        private val orientation: Int = VERTICAL_LIST,
        private val margin: Int
): RecyclerView.ItemDecoration() {

    private val a = context.obtainStyledAttributes(listOf(android.R.attr.listDivider).toIntArray())
    private val divider = a.getDrawable(0)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        when(orientation) {
            VERTICAL_LIST -> drawVertical(c, parent)
            else -> drawHorizontal(c, parent)
        }
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        when(orientation) {
            VERTICAL_LIST -> outRect?.set(0, 0, 0, divider.intrinsicHeight)
            else -> outRect?.set(0, 0, divider.intrinsicWidth, 0)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left + margin.dpToPx(), top, right, bottom)
            divider.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + divider.intrinsicHeight
            divider.setBounds(left, top + margin.dpToPx(), right, bottom - margin.dpToPx())
            divider.draw(c)
        }
    }

    private fun Int.dpToPx(): Int {
        val r = context.resources
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), r.displayMetrics))
    }

    companion object {

        const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        const val VERTICAL_LIST = LinearLayoutManager.VERTICAL

    }

}