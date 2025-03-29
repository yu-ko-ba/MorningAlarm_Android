package com.example.morningalarm.android.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.morningalarm.android.databinding.DialogTimePickerBinding
import com.example.morningalarm.android.ui.uistate.TimePickerInputUiState

class TimePickerDialog(context: Context, private val hourOfDay: Int, private val minute: Int, private val is24HourView: Boolean) : Dialog(context) {

    private lateinit var binding: DialogTimePickerBinding

    private var onPositiveButtonClickListener: (input: TimePickerInputUiState) -> Unit = { _ ->  }
    private var onNegativeButtonClickListener: () -> Unit = {}


    fun setOnPositiveButtonClickListener(listener: (input: TimePickerInputUiState) -> Unit) {
        onPositiveButtonClickListener = listener
    }


    fun setOnNegativeButtonClickListener(listener: () -> Unit) {
        onNegativeButtonClickListener = listener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogTimePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.timePicker.hour = hourOfDay
        binding.timePicker.minute = minute
        binding.timePicker.setIs24HourView(is24HourView)

        binding.positiveButton.setOnClickListener {
            val input = TimePickerInputUiState(binding.timePicker.hour, binding.timePicker.minute)
            onPositiveButtonClickListener(input)

            dismiss()
        }

        binding.negativeButton.setOnClickListener {
            onNegativeButtonClickListener()

            cancel()
        }
    }

}
