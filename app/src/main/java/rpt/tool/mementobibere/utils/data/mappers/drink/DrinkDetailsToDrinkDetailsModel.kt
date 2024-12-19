package rpt.tool.mementobibere.utils.data.mappers.drink

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.ContainerDetails
import rpt.tool.mementobibere.utils.data.appmodel.DrinkDetails
import rpt.tool.mementobibere.utils.data.database.models.ContainerDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.DrinkDetailsModel

class DrinkDetailsToDrinkDetailsModel : ModelMapper<DrinkDetails, DrinkDetailsModel> {
    override val destination: Class<DrinkDetailsModel> = DrinkDetailsModel::class.java

    override fun map(source: DrinkDetails): DrinkDetailsModel {
        return DrinkDetailsModel(
            id = source.id!!.toInt(),
            DrinkDate = source.drinkDate!!,
            DrinkDateTime = source.drinkDateTime!!,
            TodayGoal = source.todayGoal!!,
            TodayGoalOZ = source.todayGoalOZ!!,
            ContainerValue = source.containerValue!!,
            ContainerMeasure = source.containerMeasure!!,
            ContainerValueOz = source.containerValueOZ!!,
        )
    }
}