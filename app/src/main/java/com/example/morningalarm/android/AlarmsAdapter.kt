package com.example.morningalarm.android

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.morningalarm.android.databinding.AlarmsRowItemBinding
import org.json.JSONObject

object AlarmsAdapter :RecyclerView.Adapter<AlarmsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val timeTextView: TextView

        init {
            timeTextView = view.findViewById(R.id.timeTextView)
        }
    }

    private lateinit var binding: AlarmsRowItemBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarms_row_item, parent, false)

        binding = AlarmsRowItemBinding.bind(view)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.timeTextView.text = MorningAlarmManager.data.getString(MorningAlarmManager.getKeys()[position])
    }


    fun changeItem(context: Context, position: Int) {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            MorningAlarmManager.change(MorningAlarmManager.getKeys()[position], hourOfDay, minute)
            notifyItemChanged(position)
        }

        TimePickerDialog(context, timeSetListener, 7, 0, true).show()
    }


    fun deleteItem(position: Int) {
        MorningAlarmManager.firstFragment?.setAdapter(MorningAlarmManager.delete(MorningAlarmManager.getKeys()[position]).getJSONObject("data"))
    }


    override fun getItemCount(): Int {
        println("item count: ${MorningAlarmManager.getKeys().size}")
        return MorningAlarmManager.getKeys().size
    }
}