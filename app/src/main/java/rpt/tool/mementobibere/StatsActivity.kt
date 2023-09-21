package rpt.tool.mementobibere

import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import rpt.tool.mementobibere.databinding.ActivityStatsBinding
import rpt.tool.mementobibere.ui.libraries.chart.data.LineData
import rpt.tool.mementobibere.ui.libraries.chart.data.LineDataSet
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.chart.ChartXValueFormatter
import rpt.tool.mementobibere.utils.extensions.toCalculatedValue
import rpt.tool.mementobibere.utils.extensions.toCalculatedValueStats
import rpt.tool.mementobibere.utils.extensions.toMainTheme
import rpt.tool.mementobibere.utils.helpers.SqliteHelper
import kotlin.math.max


class StatsActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var sqliteHelper: SqliteHelper
    private var totalPercentage: Float = 0f
    private var totalGlasses: Float = 0f
    private lateinit var binding: ActivityStatsBinding
    private var themeInt : Int = 0
    private var unit : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPref = getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        themeInt = sharedPref.getInt(AppUtils.THEME_KEY,0)
        unit = sharedPref.getInt(AppUtils.UNIT_KEY,0)
        setTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBackGround()

        sqliteHelper = SqliteHelper(this)

        binding.btnBack.setOnClickListener {
            finish()
        }


        val entries = ArrayList<rpt.tool.mementobibere.ui.libraries.chart.data.Entry>()
        val dateArray = ArrayList<String>()

        val cursor: Cursor = sqliteHelper.getAllStats()

        if (cursor.moveToFirst()) {

            for (i in 0 until cursor.count) {
                dateArray.add(cursor.getString(1))
                val percent = cursor.getFloat(2).toCalculatedValueStats(
                    AppUtils.extractIntConversion(cursor.getString(4)),unit) /
                        cursor.getFloat(3).toCalculatedValueStats(
                            AppUtils.extractIntConversion(cursor.getString(4)),unit) * 100
                totalPercentage += percent
                totalGlasses += cursor.getInt(2)
                entries.add(
                    rpt.tool.mementobibere.ui.libraries.chart.data.Entry(
                        i.toFloat(),
                        percent
                    )
                )
                cursor.moveToNext()
            }

        } else {
            Toast.makeText(this, "Empty", Toast.LENGTH_LONG).show()
        }

        if (entries.isNotEmpty()) {
            if(dateArray.size > 1){
                binding.chart.description.isEnabled = false
                binding.chart.animateY(1000, rpt.tool.mementobibere.ui.libraries.chart.animation.Easing.Linear)
                binding.chart.viewPortHandler.setMaximumScaleX(1.5f)
                binding.chart.xAxis.setDrawGridLines(false)
                binding.chart.xAxis.position = rpt.tool.mementobibere.ui.libraries.chart.components.XAxis.XAxisPosition.TOP
                binding.chart.xAxis.isGranularityEnabled = true
                binding.chart.legend.isEnabled = false
                binding.chart.fitScreen()
                binding.chart.isAutoScaleMinMaxEnabled = true
                binding.chart.scaleX = 1f
                binding.chart.setPinchZoom(true)
                binding.chart.isScaleXEnabled = true
                binding.chart.isScaleYEnabled = false
                binding.chart.axisLeft.textColor = Color.BLACK
                binding.chart.xAxis.textColor = Color.BLACK
                binding.chart.axisLeft.setDrawAxisLine(false)
                binding.chart.xAxis.setDrawAxisLine(false)
                binding.chart.setDrawMarkers(false)
                binding.chart.xAxis.labelCount = 5

                val leftAxis = binding.chart.axisLeft
                leftAxis.axisMinimum = 0f // always start at zero
                val maxObject: rpt.tool.mementobibere.ui.libraries.chart.data.Entry = entries.maxBy { it.y }!! // entries is not empty here
                leftAxis.axisMaximum = max(a = maxObject.y, b = 100f) + 15f // 15% margin on top
                val targetLine =
                    rpt.tool.mementobibere.ui.libraries.chart.components.LimitLine(
                        100f,
                        ""
                    )
                targetLine.enableDashedLine(5f, 5f, 0f)
                leftAxis.addLimitLine(targetLine)

                val rightAxis = binding.chart.axisRight
                rightAxis.setDrawGridLines(false)
                rightAxis.setDrawZeroLine(false)
                rightAxis.setDrawAxisLine(false)
                rightAxis.setDrawLabels(false)

                val dataSet =
                    LineDataSet(
                        entries,
                        "Label"
                    )
                dataSet.setDrawCircles(false)
                dataSet.lineWidth = 2.5f
                dataSet.color = ContextCompat.getColor(this, R.color.colorSecondaryDark)
                dataSet.setDrawFilled(true)
                dataSet.fillDrawable = setDrawable()
                dataSet.setDrawValues(false)
                dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

                val lineData =
                    LineData(dataSet)
                binding.chart.xAxis.valueFormatter = (ChartXValueFormatter(dateArray))
                binding.chart.data = lineData
                binding.chart.invalidate()
                val layoutParams: ConstraintLayout.LayoutParams = binding.textView14.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.bottomToTop = binding.chart.id
                binding.textView14.layoutParams = layoutParams
                binding.chart.visibility = VISIBLE
                binding.noData.visibility = GONE
            }
            else{
                val layoutParams: ConstraintLayout.LayoutParams = binding.textView14.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.bottomToTop = binding.noData.id
                binding.textView14.layoutParams = layoutParams
                binding.chart.visibility = GONE
                binding.noData.visibility = VISIBLE
            }

            val remaining = sharedPref.getFloat(
                AppUtils.TOTAL_INTAKE_KEY,
                0f
            ) - sqliteHelper.getIntook(AppUtils.getCurrentOnlyDate()!!)

            if (remaining > 0) {
                binding.remainingIntake.text = "$remaining " + sharedPref.getString(AppUtils.UNIT_STRING,"ml")
            } else {
                binding.remainingIntake.text = "0 " + sharedPref.getString(AppUtils.UNIT_STRING,"ml")
            }

            binding.targetIntake.text = "${sharedPref.getFloat(
                AppUtils.TOTAL_INTAKE_KEY,
                0f
            )
            } " + sharedPref.getString(AppUtils.UNIT_STRING,"ml")

            val percentage = sqliteHelper.getIntook(
                AppUtils.getCurrentOnlyDate()!!
            ) * 100 / sharedPref.getFloat(
                AppUtils.TOTAL_INTAKE_KEY,
                0f
            )
            binding.waterLevelView.centerTitle = "$percentage%"
            binding.waterLevelView.progressValue = percentage.toInt()

        }
    }

    private fun setDrawable(): Drawable? {
        when(themeInt){
            0-> return getDrawable(R.drawable.graph_fill_gradiant)
            1-> getDrawable(R.drawable.graph_fill_gradiant_dark)
        }
        return getDrawable(R.drawable.graph_fill_gradiant)
    }

    private fun setTheme() {
        val theme = themeInt.toMainTheme()
        setTheme(theme)
    }

    private fun setBackGround() {
        when(themeInt){
            0->binding.layout.background = getDrawable(R.drawable.ic_app_bg)
            1->binding.layout.background = getDrawable(R.drawable.ic_app_bg_dark)
        }
    }
}


