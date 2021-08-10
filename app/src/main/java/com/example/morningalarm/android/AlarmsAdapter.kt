package com.example.morningalarm.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class AlarmsAdapter(private val keys: List<String>, private val alarmList: JSONObject) :RecyclerView.Adapter<AlarmsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView

        init {
            timeTextView = view.findViewById(R.id.timeTextView)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarms_row_item, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.timeTextView.text = alarmList.getString(keys[position])
    }


    override fun getItemCount(): Int {
        return keys.size
    }
}