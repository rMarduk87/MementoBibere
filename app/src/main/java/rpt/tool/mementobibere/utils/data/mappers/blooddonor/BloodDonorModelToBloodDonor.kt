package rpt.tool.mementobibere.utils.data.mappers.blooddonor

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.AlarmSubDetails
import rpt.tool.mementobibere.utils.data.appmodel.BloodDonor
import rpt.tool.mementobibere.utils.data.database.models.AlarmSubDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.BloodDonorModel

class BloodDonorModelToBloodDonor  : ModelMapper<BloodDonorModel, BloodDonor> {
    override val destination: Class<BloodDonor> = BloodDonor::class.java

    override fun map(source: BloodDonorModel): BloodDonor {
        return BloodDonor(
            id = source.id.toString(),
            date = source.BloodDonorDate

        )
    }
}