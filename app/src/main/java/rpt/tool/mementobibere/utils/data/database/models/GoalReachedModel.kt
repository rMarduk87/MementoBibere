package rpt.tool.mementobibere.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.goal.GoalReachedModelToGoalReached

@Keep
@Entity(tableName = "goal_reached")
class GoalReachedModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "AlarmTime")
    val Date: String,
    @ColumnInfo(name = "ContainerValue")
    val ContainerValue: String,
    @ColumnInfo(name = "ContainerValueOz")
    val ContainerValueOz: String

) : DbModel() {

    init {
        addMapper(GoalReachedModelToGoalReached())
    }
}