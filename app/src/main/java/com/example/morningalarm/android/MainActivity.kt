package com.example.morningalarm.android

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import com.example.morningalarm.android.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        sharedPreferences = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        sharedPreferences.getString(getString(R.string.server_address_key), "192.168.128.207")?.let {
            MorningAlarmManager.serverAddress = it
        }
        sharedPreferences.getString(getString(R.string.port_number_key), "5000")?.let {
            MorningAlarmManager.portNumber = it
        }

        MorningAlarmManager.setOnFailedListener {
            Snackbar.make(binding.addButton, "データの取得に失敗しました", Snackbar.LENGTH_LONG)
                .show()
        }

        MorningAlarmManager.get {
            CoroutineScope(Dispatchers.Main).launch {
                AlarmsAdapter.notifyDataSetChanged()
            }
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.addButton.setOnClickListener {
            val dialog = TimePickerDialogFragment(this, 7, 0, true)
            dialog.setOnTimeSetListener { hourOfDay, minute ->
                MorningAlarmManager.add(hourOfDay, minute) {
                    CoroutineScope(Dispatchers.Main).launch {
                        AlarmsAdapter.notifyItemInserted(MorningAlarmManager.getKeys().size - 1)
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