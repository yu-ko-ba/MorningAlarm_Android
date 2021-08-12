package com.example.morningalarm.android

import android.app.TimePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.morningalarm.android.databinding.AlarmsRowItemBinding
import org.json.JSONObject

class AlarmsAdapter(private val keys: List<String>, private val alarmList: JSONObject) :RecyclerView.Adapter<AlarmsAdapter.ViewHolder>() {

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
        holder.timeTextView.text = alarmList.getString(keys[position])

        binding.changeButton.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                MorningAlarmManager.firstFragment?.setAdapter(MorningAlarmManager.change(keys[position], hourOfDay, minute).getJSONObject("data"))
            }

            TimePickerDialog(it.context, timeSetListener, 7, 0, true).show()
        }

        binding.deleteButton.setOnClickListener {
            println(keys[position].padStart(2, '0'))
            MorningAlarmManager.firstFragment?.setAdapter(MorningAlarmManager.delete(keys[position].padStart(2, '0')).getJSONObject("data"))
        }
    }


    override fun getItemCount(): Int {
        return keys.size
    }
}