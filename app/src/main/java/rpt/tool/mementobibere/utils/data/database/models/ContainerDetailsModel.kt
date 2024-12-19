package rpt.tool.mementobibere.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.container.ContainerDetailsModelToContainerDetails

@Keep
@Entity(tableName = "container_details")
class ContainerDetailsModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "ContainerID", defaultValue = "0")
    val ContainerID: Int,
    @ColumnInfo(name = "ContainerValue")
    val ContainerValue: String,
    @ColumnInfo(name = "ContainerMeasure")
    val ContainerMeasure: String,
    @ColumnInfo(name = "ContainerValueOz")
    val ContainerValueOz: String,
    @ColumnInfo(name = "IsOpen")
    val IsOpen: String,
    @ColumnInfo(name = "IsCustom")
    val IsCustom: String
) : DbModel() {

    init {
        addMapper(ContainerDetailsModelToContainerDetails())
    }
}