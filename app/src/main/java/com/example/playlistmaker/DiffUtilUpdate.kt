package com.example.playlistmaker

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class DiffUtilUpdate<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val adapter: RecyclerView.Adapter<*>,
    private val areItemsTheSame: (T, T) -> Boolean,
    private val areContentsTheSame: (T, T) -> Boolean,
    private val updateData: (List<T>) -> Unit
) {
    fun dispatch() {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        updateData(newList) // обновляем внутренний список
        diffResult.dispatchUpdatesTo(adapter)
    }
}
