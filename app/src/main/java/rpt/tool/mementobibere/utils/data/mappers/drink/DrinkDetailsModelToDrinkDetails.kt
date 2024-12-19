package rpt.tool.mementobibere.utils.data.mappers.drink

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.ContainerDetails
import rpt.tool.mementobibere.utils.data.appmodel.DrinkDetails
import rpt.tool.mementobibere.utils.data.database.models.ContainerDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.DrinkDetailsModel

class DrinkDetailsModelToDrinkDetails  : ModelMapper<DrinkDetailsModel, DrinkDetails> {
    override val destination: Class<DrinkDetails> = DrinkDetails::class.java

    override fun map(source: DrinkDetailsModel): DrinkDetails {
        return DrinkDetails(
            id = source.id.toString(),
            drinkDate = source.DrinkDate,
            drinkDateTime = source.DrinkDateTime,
            todayGoal = source.TodayGoal,
            todayGoalOZ = source.TodayGoalOZ,
            containerValue = source.ContainerValue,
            containerMeasure = source.ContainerMeasure,
            containerValueOZ = source.ContainerValueOz,
        )
    }
}