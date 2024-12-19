package rpt.tool.mementobibere.utils.data.mappers.container

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.ContainerDetails
import rpt.tool.mementobibere.utils.data.database.models.ContainerDetailsModel

class ContainerDetailsToContainerDetailsModel : ModelMapper<ContainerDetails, ContainerDetailsModel> {
    override val destination: Class<ContainerDetailsModel> = ContainerDetailsModel::class.java

    override fun map(source: ContainerDetails): ContainerDetailsModel {
        return ContainerDetailsModel(
            id = source.id!!.toInt(),
            ContainerID = source.containerID!!.toInt(),
            ContainerValue = source.containerValue!!,
            ContainerMeasure = source.containerMeasure!!,
            ContainerValueOz = source.containerValueOZ!!,
            IsOpen = source.isOpen!!,
            IsCustom = source.isCustom!!
        )
    }
}