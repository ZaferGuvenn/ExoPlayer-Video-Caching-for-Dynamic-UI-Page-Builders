package com.siro.android.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerViewAdapter<H : BaseViewHolder<M>, M>() : RecyclerView.Adapter<H>() {

    private var isLastChild : Boolean = false
    var lastItemPosition = -1
    var listItems: ArrayList<M> = arrayListOf()
        set(value) = run {
            field = value
            notifyDataSetChanged()
        }
    var itemClickListener: ((M,  position: Int) -> Unit)? = null
    abstract var layout: (viewType: Int) -> Int
    abstract fun viewHolder(view: View, viewType: Int): H
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        val view = LayoutInflater.from(parent.context).inflate(layout(viewType), parent, false)
        val viewHolder = viewHolder(view, viewType)

        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener?.invoke(listItems[position], position)
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.onBind(listItems[position], position)
    }

    fun getList(): List<M> = listItems



}