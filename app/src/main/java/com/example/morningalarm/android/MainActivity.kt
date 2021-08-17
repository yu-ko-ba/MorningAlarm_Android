package com.example.morningalarm.android

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.TimePickerDialog
//import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.morningalarm.android.databinding.ActivityMainBinding
import com.example.morningalarm.android.databinding.DialogSettingsBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE)
        sharedPreferences.getString(getString(R.string.server_address_key), "192.168.128.207")?.let {
            MorningAlarmManager.serverAddress = it
        }
        sharedPreferences.getString(getString(R.string.port_number_key), "5000")?.let {
            MorningAlarmManager.portNumber = it
        }

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.addButton.setOnClickListener { view ->
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                MorningAlarmManager.add(hourOfDay, minute)
                AlarmsAdapter.notifyItemInserted(MorningAlarmManager.getKeys().size - 1)
            }
            TimePickerDialog(this, timeSetListener, 7, 0, true).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    @SuppressLint("CutPasteId")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_reload -> {
                MorningAlarmManager.firstFragment?.setAdapter(MorningAlarmManager.get().getJSONObject("data"))
                applicationContext
                true
            }
            R.id.action_settings -> {
                val view = this.layoutInflater.inflate(R.layout.dialog_settings, null)
                view.findViewById<EditText>(R.id.serverAddress).hint = MorningAlarmManager.serverAddress
                view.findViewById<EditText>(R.id.portNumber).hint = MorningAlarmManager.portNumber

                val dialog = AlertDialog.Builder(this)
                    .setTitle("設定")
                    .setView(view)
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        val serverAddress = view.findViewById<EditText>(R.id.serverAddress).text.toString()
                        val portNumber = view.findViewById<EditText>(R.id.portNumber).text.toString()

                        if (serverAddress != "") {
                            MorningAlarmManager.serverAddress = serverAddress
                            sharedPreferences.edit().putString(getString(R.string.server_address_key), serverAddress).apply()
                        }
                        if (portNumber != "") {
                            MorningAlarmManager.portNumber = portNumber
                            sharedPreferences.edit().putString(getString(R.string.port_number_key), portNumber).apply()
                        }

                        MorningAlarmManager.firstFragment?.setAdapter()
                    })
                    .setNegativeButton("Chancel", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}