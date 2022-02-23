package com.example.morningalarm.android.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.morningalarm.android.R
import com.example.morningalarm.android.databinding.AlarmListRowItemBinding
import com.example.morningalarm.android.ui.uistate.AlarmListItemUiState

class AlarmListAdapter : ListAdapter<AlarmListItemUiState, AlarmListAdapter.ViewHolder>(AlarmListCallback()) {

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
        holder.timeTextView.text = getItem(position).timeText
    }

    internal class AlarmListCallback : DiffUtil.ItemCallback<AlarmListItemUiState>() {

        override fun areItemsTheSame(oldItem: AlarmListItemUiState, newItem: AlarmListItemUiState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlarmListItemUiState, newItem: AlarmListItemUiState): Boolean {
            return oldItem == newItem
        }
    }
}
