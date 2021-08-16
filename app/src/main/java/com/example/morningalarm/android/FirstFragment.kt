package com.example.morningalarm.android

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.morningalarm.android.databinding.FragmentFirstBinding
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MorningAlarmManager.firstFragment = this

        setAdapter()
    }

    fun setAdapter(alarmList: JSONObject=MorningAlarmManager.get().getJSONObject("data")) {
        MorningAlarmManager.firstFragment = this

        binding.alarmsRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        val keys = mutableListOf<String>()
        for (key in alarmList.keys()) {
            keys.add(key)
        }
        val adapter = AlarmsAdapter()

        getSwipeActionHelper(adapter).attachToRecyclerView(binding.alarmsRecyclerView)

        binding.alarmsRecyclerView.adapter = adapter

        println("setAdapter")
    }


    private fun getSwipeActionHelper(adapter: AlarmsAdapter) =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.layoutPosition
                println(position)
                println(MorningAlarmManager.getKeys()[position])
                adapter.deleteItem(position)
                setAdapter()
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
                    itemView.right + iconMargin + deleteIcon.intrinsicWidth,
                    itemView.left + iconMargin
                )

                background.draw(c)
                deleteIcon.draw(c)
            }
        })


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}