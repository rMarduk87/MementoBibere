package rpt.tool.mementobibere.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.drink.DrinkDetailsModelToDrinkDetails

@Keep
@Entity(tableName = "drink_details")
class DrinkDetailsModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "ContainerValue")
    val ContainerValue: String,
    @ColumnInfo(name = "ContainerMeasure")
    val ContainerMeasure: String,
    @ColumnInfo(name = "ContainerValueOz")
    val ContainerValueOz: String,
    @ColumnInfo(name = "DrinkDate")
    val DrinkDate: String,
    @ColumnInfo(name = "DrinkDateTime")
    val DrinkDateTime: String,
    @ColumnInfo(name = "TodayGoal")
    val TodayGoal: String,
    @ColumnInfo(name = "TodayGoalOZ")
    val TodayGoalOZ: String
) : DbModel() {

    init {
        addMapper(DrinkDetailsModelToDrinkDetails())
    }
}