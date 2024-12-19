package rpt.tool.mementobibere.utils.data.mappers.container

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.ContainerDetails
import rpt.tool.mementobibere.utils.data.database.models.ContainerDetailsModel

class ContainerDetailsModelToContainerDetails  : ModelMapper<ContainerDetailsModel, ContainerDetails> {
    override val destination: Class<ContainerDetails> = ContainerDetails::class.java

    override fun map(source: ContainerDetailsModel): ContainerDetails {
        return ContainerDetails(
            id = source.id.toString(),
            containerID = source.ContainerID.toString(),
            containerValue = source.ContainerValue,
            containerMeasure = source.ContainerMeasure,
            containerValueOZ = source.ContainerValueOz,
            isOpen = source.IsOpen,
            isCustom = source.IsCustom
        )
    }
}