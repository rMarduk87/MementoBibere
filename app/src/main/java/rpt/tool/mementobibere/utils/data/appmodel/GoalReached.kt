package rpt.tool.mementobibere.utils.data.appmodel

import androidx.annotation.Keep
import rpt.tool.mementobibere.utils.data.AppModel
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.database.models.DrinkDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.GoalReachedModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.drink.DrinkDetailsToDrinkDetailsModel
import rpt.tool.mementobibere.utils.data.mappers.goal.GoalReachedToGoalReachedModel
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
@Keep
class GoalReached (
    var id: String? = null,
    var date: String? = null,
    var containerValue: String? = null,
    var containerValueOZ: String? = null
)
    : AppModel(), Serializable {

    init {
        addMapper(GoalReachedToGoalReachedModel())
    }


    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == GoalReachedModel::class.java }.map(this) as T
    }
}