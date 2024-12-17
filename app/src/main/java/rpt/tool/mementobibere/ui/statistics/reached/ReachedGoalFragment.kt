package rpt.tool.mementobibere.ui.statistics.reached

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant
import rpt.tool.mementobibere.databinding.FragmentReachedBinding
import rpt.tool.mementobibere.utils.data.ReachedGoal
import rpt.tool.mementobibere.utils.log.i
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate
import rpt.tool.mementobibere.utils.view.adapters.ReachedAdapter

class ReachedGoalFragment:BaseFragment<FragmentReachedBinding>(FragmentReachedBinding::inflate) {

    var reacheds: ArrayList<ReachedGoal> = ArrayList()
    var adapter: ReachedAdapter? = null
    var isLoading: Boolean = true
    var perPage: Int = 20
    var page: Int = 0
    var beforeLoad: Int = 0
    var afterLoad: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        body()
    }

    private fun body() {
        binding.include1.lblToolbarTitle.text = sh!!.get_string(R.string.str_drink_history)
        binding.include1.leftIconBlock.setOnClickListener{ finish() }
        binding.include1.rightIconBlock.visibility = View.GONE

        binding.reachedGoalRecyclerView.isNestedScrollingEnabled = false

        adapter = ReachedAdapter(requireActivity(), reacheds, object : ReachedAdapter.CallBack {
            override fun onClickSelect(reached: ReachedGoal?, position: Int) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onClickRemove(reached: ReachedGoal?, position: Int) {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                    .setMessage(sh!!.get_string(R.string.str_reached_remove_confirm_message))
                    .setPositiveButton(
                        sh!!.get_string(R.string.str_yes)
                    ) { dialog, _ ->
                        dh!!.REMOVE("tbl_goal_reached", "id=" + reached!!.id)
                        page = 0
                        isLoading = true
                        reacheds.clear()
                        loadReached(false)

                        adapter!!.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        sh!!.get_string(R.string.str_no)
                    ) { dialog, _ -> dialog.dismiss() }

                dialog.show()
            }
        })

        binding.reachedGoalRecyclerView.setLayoutManager(
            LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        binding.reachedGoalRecyclerView.setAdapter(adapter)

        loadReached(false)

        binding.nestedScrollView
            .setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                    val TAG = "nested_sync"
                    if (scrollY > oldScrollY) {
                        i(TAG, "Scroll DOWN")
                    }
                    if (scrollY < oldScrollY) {
                        i(TAG, "Scroll UP")
                    }

                    if (scrollY == 0) {
                        i(TAG, "TOP SCROLL")
                    }
                    if (scrollY == (v.getChildAt(0).measuredHeight - v.measuredHeight)) {
                        i(TAG, "BOTTOM SCROLL")

                        if (isLoading) {
                            isLoading = false

                            page++

                            loadReached(true)
                        }
                    }
                })
    }


    private fun finish() {
        safeNavController?.safeNavigate(
            ReachedGoalFragmentDirections.actionReachedFragmentToDrinkFragment())
    }

    @SuppressLint("Recycle", "NotifyDataSetChanged")
    private fun loadReached(closeLoader: Boolean) {
        val start_idx = page * perPage

        val query =
            "SELECT * FROM tbl_goal_reached ORDER BY datetime(substr(Date, 7, 4) ||" +
                    " '-' || substr(Date, 4, 2) || '-' || substr(Date, 1, 2) || " +
                    "' ' || substr(Date, 12, 8)) ASC " +
                    " limit $start_idx,$perPage"

        val c = Constant.SDB!!.rawQuery(query, null)

        val arr_data = ArrayList<HashMap<String, String>>()

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i)
                }

                arr_data.add(map)
            } while (c.moveToNext())
        }

        for (k in arr_data.indices) {
            val goal = ReachedGoal()
            goal.id = arr_data[k]["id"]

            goal.containerValue = "" + (arr_data[k]["ContainerValue"]!!.toDouble())
            goal.containerValueOZ = "" + (arr_data[k]["ContainerValueOZ"]!!.toDouble())

            goal.date = arr_data[k]["Date"]

            reacheds.add(goal)
        }

        afterLoad = reacheds.size

        isLoading = if (afterLoad == 0) false
        else if (afterLoad > beforeLoad) true
        else false

        if (reacheds.size > 0)
            binding.lblNoRecordFound.visibility = View.GONE
        else binding.lblNoRecordFound.visibility = View.VISIBLE

        adapter!!.notifyDataSetChanged()
    }
}