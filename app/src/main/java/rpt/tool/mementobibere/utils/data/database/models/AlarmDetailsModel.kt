package rpt.tool.mementobibere.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.alarm.AlarmDetailsModelToAlarmDetails
import rpt.tool.mementobibere.utils.data.mappers.container.ContainerDetailsModelToContainerDetails

@Keep
@Entity(tableName = "alarm_details")
class AlarmDetailsModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "AlarmTime")
    val AlarmTime: String,
    @ColumnInfo(name = "AlarmId")
    val AlarmId: String,
    @ColumnInfo(name = "AlarmType")
    val AlarmType: String,
    @ColumnInfo(name = "AlarmInterval")
    val AlarmInterval: String,
    @ColumnInfo(name = "Sunday", defaultValue = "0")
    val Sunday: Int,
    @ColumnInfo(name = "Monday", defaultValue = "1")
    val Monday: Int,
    @ColumnInfo(name = "Tuesday", defaultValue = "1")
    val Tuesday: Int,
    @ColumnInfo(name = "Wednesday", defaultValue = "1")
    val Wednesday: Int,
    @ColumnInfo(name = "Thursday", defaultValue = "1")
    val Thursday: Int,
    @ColumnInfo(name = "Friday", defaultValue = "1")
    val Friday: Int,
    @ColumnInfo(name = "Saturday", defaultValue = "1")
    val Saturday: Int,
    @ColumnInfo(name = "SundayAlarmId")
    val SundayAlarmId: String,
    @ColumnInfo(name = "MondayAlarmId")
    val MondayAlarmId: String,
    @ColumnInfo(name = "TuesdayAlarmId")
    val TuesdayAlarmId: String,
    @ColumnInfo(name = "WednesdayAlarmId")
    val WednesdayAlarmId: String,
    @ColumnInfo(name = "ThursdayAlarmId")
    val ThursdayAlarmId: String,
    @ColumnInfo(name = "FridayAlarmId")
    val FridayAlarmId: String,
    @ColumnInfo(name = "SaturdayAlarmId")
    val SaturdayAlarmId: String,
) : DbModel() {

    init {
        addMapper(AlarmDetailsModelToAlarmDetails())
    }
}