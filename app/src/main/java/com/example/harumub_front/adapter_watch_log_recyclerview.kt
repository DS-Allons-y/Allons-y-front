package com.example.harumub_front

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adapter_watch_log_recyclerview : RecyclerView.Adapter<adapter_watch_log_recyclerview.ViewHolder>(){
    var items = ArrayList<logData>()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        fun setItem(item : logData){
            itemView.findViewById<TextView>(R.id.log_time).text = item.logData_time
            itemView.findViewById<TextView>(R.id.log_emotion).text = item.logData_emotion
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapter_watch_log_recyclerview.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_log, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: adapter_watch_log_recyclerview.ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }
}