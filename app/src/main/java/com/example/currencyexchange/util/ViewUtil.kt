package com.example.currencyexchange.util

import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView

fun noNothingSelectOnItemSelectedListener(onItemSelected: (AdapterView<*>, View, Int, Long) -> Unit): AdapterView.OnItemSelectedListener {
    return object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            onItemSelected(parent, view, position, id)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

fun afterTextChangedWatcher(afterTextChanged: (String) -> Unit): TextWatcher {
    return object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s.toString())
        }
    }
}

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.left = space
        outRect.right = space
        outRect.bottom = space

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition != 0) {
            outRect.left = space
        }
        if (itemPosition == parent.adapter?.itemCount?.minus(1)) {
            outRect.right = space
        }
    }
}