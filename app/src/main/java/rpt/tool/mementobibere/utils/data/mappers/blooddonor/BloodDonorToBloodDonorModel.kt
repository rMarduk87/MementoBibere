package rpt.tool.mementobibere.utils.data.mappers.blooddonor

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.BloodDonor
import rpt.tool.mementobibere.utils.data.database.models.BloodDonorModel


class BloodDonorToBloodDonorModel : ModelMapper<BloodDonor, BloodDonorModel> {
    override val destination: Class<BloodDonorModel> = BloodDonorModel::class.java

    override fun map(source: BloodDonor): BloodDonorModel {
        return BloodDonorModel(
            id = source.id!!.toInt(),
            BloodDonorDate = source.date!!
        )
    }
}