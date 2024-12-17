package rpt.tool.mementobibere.ui.statistics.history

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import rpt.tool.mementobibere.BaseFragment
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant
import rpt.tool.mementobibere.databinding.FragmentHistoryBinding
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.data.History
import rpt.tool.mementobibere.utils.log.i
import rpt.tool.mementobibere.utils.navigation.safeNavController
import rpt.tool.mementobibere.utils.navigation.safeNavigate
import rpt.tool.mementobibere.utils.view.adapters.HistoryAdapter


class HistoryFragment:BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {

    var histories: ArrayList<History> = ArrayList()
    var adapter: HistoryAdapter? = null
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

        binding.historyRecyclerView.isNestedScrollingEnabled = false

        adapter = HistoryAdapter(requireActivity(), histories, object : HistoryAdapter.CallBack {
            override fun onClickSelect(history: History?, position: Int) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onClickRemove(history: History?, position: Int) {
                val dialog: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
                    .setMessage(sh!!.get_string(R.string.str_history_remove_confirm_message))
                    .setPositiveButton(
                        sh!!.get_string(R.string.str_yes)
                    ) { dialog, _ ->
                        dh!!.REMOVE("tbl_drink_details", "id=" + history!!.id)
                        getReachedAndRemove(history.drinkDate)
                        page = 0
                        isLoading = true
                        histories.clear()
                        loadHistory(false)

                        adapter!!.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        sh!!.get_string(R.string.str_no)
                    ) { dialog, _ -> dialog.dismiss() }

                dialog.show()
            }
        })

        binding.historyRecyclerView.setLayoutManager(
            LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
        )

        binding.historyRecyclerView.setAdapter(adapter)

        loadHistory(false)

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

                    loadHistory(true)
                }
            }
        })
    }

    private fun getReachedAndRemove(drinkDate: String?) {
        val mes_unit = URLFactory.WATER_UNIT_VALUE
        val arr_data2 =
            dh!!.getdata("tbl_drink_details", "DrinkDate ='" +
                    drinkDate + "'")

        var tot = 0f

        for (j in arr_data2.indices) {
            if (mes_unit.equals(
                    "ml",
                    ignoreCase = true
                )
            ){
                val x = arr_data2[j]["ContainerValue"]!!.toFloat()
                tot += x
            }
            else{
                val y = arr_data2[j]["ContainerValueOZ"]!!.toFloat()
                tot += y
            }
        }

        if(URLFactory.DAILY_WATER_VALUE > tot){
            dh!!.REMOVE("tbl_goal_reached", "Date=$drinkDate")
        }
    }

    private fun finish() {
        safeNavController?.safeNavigate(
            HistoryFragmentDirections.actionHistoryFragmentToDrinkFragment())
    }

    @SuppressLint("Recycle", "NotifyDataSetChanged")
    private fun loadHistory(closeLoader: Boolean) {
        val start_idx = page * perPage

        val query =
            "SELECT * FROM tbl_drink_details ORDER BY " +
                    "datetime(substr(DrinkDateTime, 7, 4) || '-' || substr(DrinkDateTime, 4, 2) || " +
                    "'-' || substr(DrinkDateTime, 1, 2) || ' ' || substr(DrinkDateTime, 12, 8))" +
                    " DESC limit $start_idx,$perPage"

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

        val mes_unit = URLFactory.WATER_UNIT_VALUE

        for (k in arr_data.indices) {
            val history = History()
            history.id = arr_data[k]["id"]

            history.containerMeasure = mes_unit
            history.containerValue = "" + (arr_data[k]["ContainerValue"]!!.toDouble())
            history.containerValueOZ = "" + (arr_data[k]["ContainerValueOZ"]!!.toDouble())

            history.drinkDate = arr_data[k]["DrinkDate"]
            history.drinkTime =
                dth!!.FormateDateFromString("HH:mm", "hh:mm a",
                    arr_data[k]["DrinkTime"])

            val arr_data2 =
                dh!!.getdata("tbl_drink_details", "DrinkDate ='" +
                        arr_data[k]["DrinkDate"] + "'")

            var tot = 0f

            for (j in arr_data2.indices) {
                if (mes_unit.equals(
                        "ml",
                        ignoreCase = true
                    )
                ){
                    val x = arr_data2[j]["ContainerValue"]!!.toFloat()
                    tot += x
                }
                else{
                    val y = arr_data2[j]["ContainerValueOZ"]!!.toFloat()
                    tot += y
                }
            }
            history.totalML = "" + (tot).toInt() + " " + mes_unit

            histories.add(history)
        }

        afterLoad = histories.size

        isLoading = if (afterLoad == 0) false
        else if (afterLoad > beforeLoad) true
        else false

        if (histories.size > 0)
            binding.lblNoRecordFound.visibility = View.GONE
        else binding.lblNoRecordFound.visibility = View.VISIBLE

        adapter!!.notifyDataSetChanged()
    }


}