package com.example.morningalarm.android.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.morningalarm.android.MorningAlarmManager
import com.example.morningalarm.android.R
import com.example.morningalarm.android.databinding.AlarmListRowItemBinding

object AlarmListAdapter :RecyclerView.Adapter<AlarmListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView = view.findViewById(R.id.timeTextView)
    }


    private lateinit var binding: AlarmListRowItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_list_row_item, parent, false)

        binding = AlarmListRowItemBinding.bind(view)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.timeTextView.text = MorningAlarmManager.getData()
            .getString(MorningAlarmManager.getKeys()[position])
    }


    override fun getItemCount(): Int {
        println("item count: ${MorningAlarmManager.getKeys().size}")
        return MorningAlarmManager.getKeys().size
    }
}
