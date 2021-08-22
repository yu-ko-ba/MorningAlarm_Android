package com.example.morningalarm.android

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

class TimePickerDialogFragment(private val parentsContext: Context, private val hourOfDay: Int, private val minute: Int, private val is24HourView: Boolean) : DialogFragment() {

    private var onTimeSetListener: (hourOfDay: Int, minute: Int) -> Unit = { _, _ -> }
    private var onCancelListener: () -> Unit = {}


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = TimePickerDialog(parentsContext, hourOfDay, minute, is24HourView)
        dialog.setOnPositiveButtonClickListener(onTimeSetListener)
        dialog.setOnNegativeButtonClickListener(onCancelListener)
        return dialog
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)

        onCancelListener()
    }


    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, parentsContext.packageName)
    }


    fun setOnTimeSetListener(listener: (hourOfDay: Int, minute: Int) -> Unit) {
        onTimeSetListener = listener
    }


    fun setOnCancelListener(listener: () -> Unit) {
        onCancelListener = listener
    }
}