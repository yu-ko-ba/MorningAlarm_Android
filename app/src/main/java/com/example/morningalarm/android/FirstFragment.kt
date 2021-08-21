package com.example.morningalarm.android

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.morningalarm.android.databinding.FragmentFirstBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = this.requireContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding.alarmsRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        val keys = mutableListOf<String>()
        for (key in MorningAlarmManager.getKeys()) {
            keys.add(key)
        }
        getSwipeActionHelper(AlarmsAdapter).attachToRecyclerView(binding.alarmsRecyclerView)
        binding.alarmsRecyclerView.adapter = AlarmsAdapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshItems()
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun refreshItems() {
        MorningAlarmManager.get(
            {
                binding.swipeRefreshLayout.isRefreshing = false
                CoroutineScope(Dispatchers.Main).launch {
                    AlarmsAdapter.notifyDataSetChanged()
                }
            },
            {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        )
    }


    @SuppressLint("CutPasteId", "NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val view = this.layoutInflater.inflate(R.layout.dialog_settings, null)
                view.findViewById<EditText>(R.id.serverAddress).hint = MorningAlarmManager.serverAddress
                view.findViewById<EditText>(R.id.portNumber).hint = MorningAlarmManager.portNumber

                AlertDialog.Builder(this.requireContext())
                    .setTitle(getString(R.string.setting_dialog_title))
                    .setView(view)
                    .setPositiveButton(getString(R.string.ok)) { _, _ ->
                        binding.swipeRefreshLayout.isRefreshing = true

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

                        MorningAlarmManager.get(
                            {
                                binding.swipeRefreshLayout.isRefreshing = false
                                CoroutineScope(Dispatchers.Main).launch {
                                    AlarmsAdapter.notifyDataSetChanged()
                                }
                            },
                            {
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                        )
                        MorningAlarmManager.get {
                        }
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


    private fun getSwipeActionHelper(adapter: AlarmsAdapter) =
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
                        dialog.setOnTimeSetListener { hourOfDay, minute ->
                            MorningAlarmManager.change(MorningAlarmManager.getKeys()[position], hourOfDay, minute) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    adapter.notifyItemChanged(position)
                                }
                            }
                        }
                        dialog.show(childFragmentManager)

                        adapter.notifyDataSetChanged()
                    }
                    ItemTouchHelper.RIGHT -> {
                        // アラームを削除する
                        MorningAlarmManager.delete(MorningAlarmManager.getKeys()[position]) {
                            CoroutineScope(Dispatchers.Main).launch {
                                adapter.notifyItemRemoved(position)
                            }
                        }
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

                    val changeIcon = AppCompatResources.getDrawable(this@FirstFragment.requireContext(), R.drawable.ic_baseline_settings_24)!!
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

                    val deleteIcon = AppCompatResources.getDrawable(this@FirstFragment.requireContext(), R.drawable.ic_baseline_delete_24)!!
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