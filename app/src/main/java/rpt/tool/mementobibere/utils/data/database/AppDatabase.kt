package rpt.tool.mementobibere.utils.data.database

import android.content.Context
import androidx.room.*
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.data.database.dao.AlarmDetailsDao
import rpt.tool.mementobibere.utils.data.database.dao.AlarmSubDetailsDao
import rpt.tool.mementobibere.utils.data.database.dao.BloodDonorDao
import rpt.tool.mementobibere.utils.data.database.dao.ContainerDetailsDao
import rpt.tool.mementobibere.utils.data.database.dao.DrinkDetailsDao
import rpt.tool.mementobibere.utils.data.database.dao.GoalReachedDao
import rpt.tool.mementobibere.utils.data.database.models.AlarmDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.AlarmSubDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.BloodDonorModel
import rpt.tool.mementobibere.utils.data.database.models.ContainerDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.DrinkDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.GoalReachedModel

@Database(
    entities = [
        ContainerDetailsModel::class,
        DrinkDetailsModel::class,
        AlarmDetailsModel::class,
        AlarmSubDetailsModel::class,
        BloodDonorModel::class,
        GoalReachedModel::class,
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun containerDetails(): ContainerDetailsDao
    abstract fun drinkDetailsDao(): DrinkDetailsDao
    abstract fun alarmDetailsDao(): AlarmDetailsDao
    abstract fun alarmSubDetailsDao(): AlarmSubDetailsDao
    abstract fun bloodDonorDao(): BloodDonorDao
    abstract fun goalReachedDao(): GoalReachedDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppUtils.databaseName
        )
            .build()
    }
}