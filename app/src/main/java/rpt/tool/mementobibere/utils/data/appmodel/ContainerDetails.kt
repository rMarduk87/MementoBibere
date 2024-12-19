package rpt.tool.mementobibere.utils.data.appmodel

import androidx.annotation.Keep
import rpt.tool.mementobibere.utils.data.AppModel
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.database.models.ContainerDetailsModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.container.ContainerDetailsToContainerDetailsModel
import java.io.Serializable

@Keep
class ContainerDetails (
    var containerID: String? = null,
    var id: String? = null,
    var containerMeasure: String? = null,
    var containerValue: String? = null,
    var containerValueOZ: String? = null,
    var isOpen: String? = null,
    var isCustom: String? = null
)
    : AppModel(), Serializable {

    init {
        addMapper(ContainerDetailsToContainerDetailsModel())
    }


    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == ContainerDetailsModel::class.java }.map(this) as T
    }
}