package rpt.tool.mementobibere.utils.data.mappers.goal

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.DrinkDetails
import rpt.tool.mementobibere.utils.data.appmodel.GoalReached
import rpt.tool.mementobibere.utils.data.database.models.DrinkDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.GoalReachedModel

class GoalReachedModelToGoalReached  : ModelMapper<GoalReachedModel, GoalReached> {
    override val destination: Class<GoalReached> = GoalReached::class.java

    override fun map(source: GoalReachedModel): GoalReached {
        return GoalReached(
            id = source.id.toString(),
            date = source.Date,
            containerValue = source.ContainerValue,
            containerValueOZ = source.ContainerValueOz,
        )
    }
}