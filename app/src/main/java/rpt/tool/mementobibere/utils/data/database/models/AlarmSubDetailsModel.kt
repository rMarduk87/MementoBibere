package rpt.tool.mementobibere.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper

@Keep
@Entity(tableName = "alarm_sub_details")
class AlarmSubDetailsModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "AlarmTime")
    val AlarmTime: String,
    @ColumnInfo(name = "AlarmId")
    val AlarmId: String,
    @ColumnInfo(name = "SuperId")
    val SuperId: String,

) : DbModel() {

    init {
        addMapper(AlarmSubDetailsModelToAlarmSubDetails())
    }
}