package rpt.tool.mementobibere.migration.ui.weight

import rpt.tool.mementobibere.BaseBottomSheetDialog
import rpt.tool.mementobibere.databinding.SelectWeightBottomSheetFragmentBinding

class SelectWeightBottomSheetFragment:
    BaseBottomSheetDialog<SelectWeightBottomSheetFragmentBinding>(SelectWeightBottomSheetFragmentBinding::inflate) {

    /*private var themeInt: Int = 0
    private var weightUnit : Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        themeInt = SharedPreferencesManager.themeInt
        initBottomBars()
        setBackGround()
        binding.btnUpdate.setOnClickListener {

            SharedPreferencesManager.weightUnit = weightUnit
            val weight = SharedPreferencesManager.weight.toCalculateWeight(weightUnit)
            SharedPreferencesManager.weight = weight
            val totalIntake = rpt.tool.mementobibere.utils.AppUtils.calculateIntake(
                weight,
                SharedPreferencesManager.workType,
                weightUnit,
                SharedPreferencesManager.gender,
                SharedPreferencesManager.climate,
                SharedPreferencesManager.current_unitInt,
                SharedPreferencesManager.new_unitInt
            )
            val df = DecimalFormat("#")
            df.roundingMode = RoundingMode.CEILING
            SharedPreferencesManager.totalIntake = df.format(totalIntake).toFloat()
            SharedPreferencesManager.setWeight = true
            dismiss()
        }

    }

    private fun initBottomBars() {
        var colorString = when (themeInt) {
            0 -> {
                "#41B279"
            }
            1 -> {
                "#29704D"
            }
            else -> {
                "#29704D"
            }
        }
        val menu = binding.weightSystemBottomBar.menu


        for (i in rpt.tool.mementobibere.utils.AppUtils.listIdsWeightSystem.indices) {
            menu.add(
                MenuItemDescriptor.Builder(
                    requireContext(),
                    rpt.tool.mementobibere.utils.AppUtils.listIdsWeightSystem[i],
                    rpt.tool.mementobibere.utils.AppUtils.listWeightSystem[i],
                    rpt.tool.mementobibere.utils.AppUtils.listStringWeightSystem[i],
                    Color.parseColor(colorString)
                )
                    .build()
            )
        }

        setWeightUnit()


        binding.weightSystemBottomBar.onItemSelectedListener = { _, i, _ ->
            when (i.id) {
                R.id.icon_kg -> weightUnit = 0
                R.id.icon_lbl -> weightUnit = 1
            }

            setWeightUnit()

        }
    }

    private fun setWeightUnit() {
        SharedPreferencesManager.weightUnit = weightUnit
    }

    private fun setBackGround() {
        when(themeInt){
            0->toLightTheme()
            1->toDarkTheme()
        }
    }

    private fun toDarkTheme() {
        setBackgroundColor(requireContext().getColor(R.color.darkGreen))
        binding.textView7.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.btnUpdate.setTextColor(requireContext().getColor(R.color.colorBlack))
        binding.view2.
        setBackgroundColor(requireContext().getColor(R.color.gray_btn_bg_pressed_color))
        binding.weightSystemBottomBar.setBackgroundColorRes(R.color.gray_btn_bg_pressed_color)
    }

    private fun toLightTheme() {
        setBackgroundColor(requireContext().getColor(R.color.colorSecondaryDark))
        binding.textView7.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.btnUpdate.setTextColor(requireContext().getColor(R.color.colorWhite))
        binding.view2.
        setBackgroundColor(requireContext().getColor(R.color.colorWhite))
        binding.weightSystemBottomBar.setBackgroundColorRes(R.color.colorWhite)
    }

    private fun setBackgroundColor(color: Int) {
        binding.bottomSheetParent.setBackgroundColor(color)
    }*/
}