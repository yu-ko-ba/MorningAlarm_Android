package com.example.morningalarm.android

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.morningalarm.android.databinding.DialogTimePickerBinding

class TimePickerDialog(context: Context, private val hourOfDay: Int, private val minute: Int, private val is24HourView: Boolean) : Dialog(context) {

    private lateinit var binding: DialogTimePickerBinding

    private var onPositiveButtonClickListener: (hourOfDay: Int, minute: Int) -> Unit = { _, _ ->  }
    private var onNegativeButtonClickListener: () -> Unit = {}


    fun setOnPositiveButtonClickListener(listener: (hourOfDay: Int, minute: Int) -> Unit) {
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
            onPositiveButtonClickListener(binding.timePicker.hour, binding.timePicker.minute)

            dismiss()
        }

        binding.negativeButton.setOnClickListener {
            onNegativeButtonClickListener()

            cancel()
        }
    }

}