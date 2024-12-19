package rpt.tool.mementobibere.utils.data.database.models

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.blooddonor.BloodDonorModelToBloodDonor

@Keep
@Entity(tableName = "blood_donor")
class BloodDonorModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "BloodDonorDate")
    val BloodDonorDate: String

) : DbModel() {

    init {
        addMapper(BloodDonorModelToBloodDonor())
    }
}