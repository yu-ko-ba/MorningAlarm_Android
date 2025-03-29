package com.example.morningalarm.android.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.morningalarm.android.MorningAlarmManager
import com.example.morningalarm.android.R
import com.example.morningalarm.android.data.datasource.impl.LocalDataSource
import com.example.morningalarm.android.data.repository.AlarmRepository
import com.example.morningalarm.android.databinding.FragmentAlarmListBinding
import com.example.morningalarm.android.domain.usecase.addalarm.AddAlarmUseCase
import com.example.morningalarm.android.domain.usecase.fetchalarmlist.FetchAlarmListUseCase
import com.example.morningalarm.android.ui.uistate.SyncListUiState
import com.example.morningalarm.android.ui.viewmodel.AlarmListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AlarmListFragment : Fragment() {

    private var _binding: FragmentAlarmListBinding? = null

    // This property is only valid between onCreate and
    // onDestroyView.
    private lateinit var binding: FragmentAlarmListBinding

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var viewModel: AlarmListViewModel

    private val alarmListAdapter = AlarmListAdapter()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val localDataSource = LocalDataSource()
        val alarmRepository = AlarmRepository(localDataSource)
        viewModel = ViewModelProvider(
            this,
            AlarmListViewModel.Factory(
                fetchAlarmListUseCase = FetchAlarmListUseCase(alarmRepository),
                addAlarmUseCase = AddAlarmUseCase(alarmRepository)
            )
        )[AlarmListViewModel::class.java]

        binding = FragmentAlarmListBinding.inflate(layoutInflater)

        sharedPreferences = this.requireContext().getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE)

        sharedPreferences.getString(getString(R.string.server_address_key), getString(R.string.default_server_address))?.let {
            MorningAlarmManager.serverAddress = it
        }
        sharedPreferences.getString(getString(R.string.port_number_key), getString(R.string.default_port_number))?.let {
            MorningAlarmManager.portNumber = it
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.alarmListItemUiState.collect { uiState ->
                    alarmListAdapter.submitList(uiState)
                }
            }
        }

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.syncListUiState.collect {
                    binding.swipeRefreshLayout.isRefreshing = it.isRefreshing

                    when(it){
                        SyncListUiState.Success -> {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.sync_successful),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        SyncListUiState.Failure -> {
                            Snackbar.make(
                                binding.root,
                                getString(R.string.sync_failed),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        else -> {
                            // noop
                        }
                    }
                }
            }
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAlarmListBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding.alarmListRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        val keys = mutableListOf<String>()
        for (key in MorningAlarmManager.getKeys()) {
            keys.add(key)
        }
        getSwipeActionHelper(alarmListAdapter).attachToRecyclerView(binding.alarmListRecyclerView)
        binding.alarmListRecyclerView.adapter = alarmListAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchAlarmList()
        }

        binding.addButton.setOnClickListener {
            val dialog = TimePickerDialogFragment(requireContext(), 7, 0, true)
            dialog.setOnTimeSetListener { input ->
                viewModel.addAlarm(input)
            }
            dialog.show(childFragmentManager)
        }
    }

    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val view = this.layoutInflater.inflate(R.layout.dialog_settings, null)
                view.findViewById<EditText>(R.id.serverAddress).hint =
                    MorningAlarmManager.serverAddress
                view.findViewById<EditText>(R.id.portNumber).hint = MorningAlarmManager.portNumber

                AlertDialog.Builder(this.requireContext())
                    .setTitle(getString(R.string.settings))
                    .setView(view)
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->

                        val serverAddress =
                            view.findViewById<EditText>(R.id.serverAddress).text.toString()
                        val portNumber =
                            view.findViewById<EditText>(R.id.portNumber).text.toString()

                        if (serverAddress != "") {
                            MorningAlarmManager.serverAddress = serverAddress
                            sharedPreferences.edit()
                                .putString(getString(R.string.server_address_key), serverAddress)
                                .apply()
                        }
                        if (portNumber != "") {
                            MorningAlarmManager.portNumber = portNumber
                            sharedPreferences.edit()
                                .putString(getString(R.string.port_number_key), portNumber).apply()
                        }
                        viewModel.fetchAlarmList()
                    }
                    .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun getSwipeActionHelper(adapter: AlarmListAdapter) =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        // 時間を変更する
                        val dialog = TimePickerDialogFragment(requireContext(), 7, 0, true)
                        dialog.setOnTimeSetListener { input ->
                            MorningAlarmManager.change(
                                MorningAlarmManager.getKeys()[position],
                                input.hourOfDay,
                                input.minute,
                                {
                                    runBlocking {
                                        adapter.notifyDataSetChanged()
                                        delay(200)
                                    }
                                    CoroutineScope(Dispatchers.Main).launch {
                                        adapter.notifyItemChanged(position)
                                    }
                                },
                                {
                                    CoroutineScope(Dispatchers.Main).launch {
                                        adapter.notifyItemChanged(position)
                                    }
                                }
                            )
                        }
                        dialog.setOnCancelListener {
                            CoroutineScope(Dispatchers.Main).launch {
                                adapter.notifyItemChanged(position)
                            }
                        }
                        dialog.show(childFragmentManager)
                    }
                    ItemTouchHelper.RIGHT -> {
                        // アラームを削除する
                        MorningAlarmManager.delete(
                            MorningAlarmManager.getKeys()[position],
                            {
                                CoroutineScope(Dispatchers.Main).launch {
                                    adapter.notifyItemRemoved(position)
                                }
                            },
                            {
                                CoroutineScope(Dispatchers.Main).launch {
                                    adapter.notifyItemChanged(position)
                                }
                            }
                        )
                    }
                }

            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                if (dX < 0) {
                    // 変更するとき
                    val background = ColorDrawable(Color.LTGRAY)
                    background.setBounds(
                        itemView.left + dX.toInt(),
                        itemView.top,
                        itemView.right,
                        itemView.bottom
                    )

                    val changeIcon = AppCompatResources.getDrawable(this@AlarmListFragment.requireContext(),
                        R.drawable.ic_baseline_settings_24
                    )!!
                    val iconMargin = (itemView.height - changeIcon.intrinsicHeight) / 2
                    changeIcon.setBounds(
                        itemView.right - iconMargin - changeIcon.intrinsicWidth,
                        itemView.top + iconMargin,
                        itemView.right - iconMargin,
                        itemView.bottom - iconMargin
                    )

                    background.draw(c)
                    changeIcon.draw(c)
                } else {
                    // 削除するとき
                    val background = ColorDrawable(Color.RED)
                    background.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.right + dX.toInt(),
                        itemView.bottom
                    )

                    val deleteIcon = AppCompatResources.getDrawable(this@AlarmListFragment.requireContext(),
                        R.drawable.ic_baseline_delete_24
                    )!!
                    val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2
                    deleteIcon.setBounds(
                        itemView.left + iconMargin,
                        itemView.top + iconMargin,
                        itemView.left + iconMargin + deleteIcon.intrinsicWidth,
                        itemView.bottom - iconMargin
                    )

                    background.draw(c)
                    deleteIcon.draw(c)
                }

            }
        })


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
