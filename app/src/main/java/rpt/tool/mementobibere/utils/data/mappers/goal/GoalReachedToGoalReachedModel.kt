package rpt.tool.mementobibere.utils.data.mappers.goal

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.DrinkDetails
import rpt.tool.mementobibere.utils.data.appmodel.GoalReached
import rpt.tool.mementobibere.utils.data.database.models.DrinkDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.GoalReachedModel

class GoalReachedToGoalReachedModel : ModelMapper<GoalReached, GoalReachedModel> {
    override val destination: Class<GoalReachedModel> = GoalReachedModel::class.java

    override fun map(source: GoalReached): GoalReachedModel {
        return GoalReachedModel(
            id = source.id!!.toInt(),
            ContainerValue = source.containerValue!!,
            ContainerValueOz = source.containerValueOZ!!,
            Date = source.date!!
        )
    }
}