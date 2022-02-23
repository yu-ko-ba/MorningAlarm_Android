package com.example.morningalarm.android.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import com.example.morningalarm.android.MorningAlarmManager
import com.example.morningalarm.android.R
import com.example.morningalarm.android.databinding.ActivityMorningAlarmBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MorningAlarmActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMorningAlarmBinding


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMorningAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.addButton.setOnClickListener {
            val dialog = TimePickerDialogFragment(this, 7, 0, true)
            dialog.setOnTimeSetListener { hourOfDay, minute ->
                MorningAlarmManager.add(hourOfDay, minute) {
                    CoroutineScope(Dispatchers.Main).launch {
                        //AlarmListAdapter.notifyItemInserted(MorningAlarmManager.getKeys().size - 1)
                    }
                }
            }
            dialog.show(supportFragmentManager)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
