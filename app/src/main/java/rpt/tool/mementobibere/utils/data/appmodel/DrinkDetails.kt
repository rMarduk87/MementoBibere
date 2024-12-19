package rpt.tool.mementobibere.utils.data.appmodel

import androidx.annotation.Keep
import rpt.tool.mementobibere.utils.data.AppModel
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.database.models.DrinkDetailsModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.drink.DrinkDetailsToDrinkDetailsModel
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
@Keep
class DrinkDetails (
    var drinkDateTime: String? = null,
    var drinkTime: String? = null,
    var id: String? = null,
    var drinkDate: String? = null,
    var containerMeasure: String? = null,
    var containerValue: String? = null,
    var containerValueOZ: String? = null,
    var todayGoal: String? = null,
    var todayGoalOZ: String? = null
)
    : AppModel(), Serializable {

    init {
        addMapper(DrinkDetailsToDrinkDetailsModel())
    }


    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == DrinkDetailsModel::class.java }.map(this) as T
    }
}