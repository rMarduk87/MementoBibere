package rpt.tool.mementobibere.migration.utils.helpers

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Database_Helper
import rpt.tool.mementobibere.utils.AppUtils
import java.io.File


class SqliteHelper(val context: Context,val actvity: Activity) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    var dh: Database_Helper = Database_Helper(context, actvity)
    var create = true

    companion object {
        private const val DATABASE_VERSION = 8
        private const val DATABASE_NAME = "RptBibere"
        private const val TABLE_STATS = "stats"
        private const val TABLE_INTOOK_COUNTER = "intook_count"
        private const val TABLE_REACHED = "intake_reached"
        private const val TABLE_AVIS = "avis_day"
        private const val KEY_ID = "id"
        private const val KEY_DATE = "date"
        private const val KEY_INTOOK = "intook"
        private const val KEY_TOTAL_INTAKE = "totalintake"
        private const val KEY_UNIT = "unit"
        private const val KEY_INTOOK_COUNT = "intook_count"
        private const val KEY_QTA = "qta"
        private const val KEY_MONTH = "month"
        private const val KEY_YEAR = "year"
        private const val KEY_N_INTOOK = "n_intook"
        private const val KEY_N_TOTAL_INTAKE = "n_totalintake"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        if(create){
            addFirstTable(db)

            addNewTables(db)

            addAvisTable(db)
        }

        deleteDB(db)
    }

    private fun deleteDB(db: SQLiteDatabase?) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_STATS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INTOOK_COUNTER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REACHED")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_AVIS")
        val data: File = Environment.getDataDirectory()
        val currentDBPath = "/data/rpt.tool.mementobibere/databases/$DATABASE_NAME"
        val currentDB = File(data, currentDBPath)
        val deleted = SQLiteDatabase.deleteDatabase(currentDB)
    }

    private fun addFirstTable(db: SQLiteDatabase?) {
        val createStatTable = ("CREATE TABLE " + TABLE_STATS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT UNIQUE,"
                + KEY_INTOOK + " INT," + KEY_TOTAL_INTAKE + " INT," + KEY_UNIT +
                " VARCHAR(200),"+ KEY_MONTH +
                " VARCHAR(200)," + KEY_YEAR + " VARCHAR(200)," + KEY_N_INTOOK + " FLOAT,"
                + KEY_N_TOTAL_INTAKE + " FLOAT "+")")
        db?.execSQL(createStatTable)
    }

    private fun addNewTables(db: SQLiteDatabase?) {
        val createIntookCounterTable = ("CREATE TABLE " + TABLE_INTOOK_COUNTER + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT,"
                + KEY_INTOOK + " INT," + KEY_INTOOK_COUNT + " INT)")
        db?.execSQL(createIntookCounterTable)

        val createReachedTable = ("CREATE TABLE " + TABLE_REACHED + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE +
                " TEXT UNIQUE," + KEY_QTA + " FLOAT,"
                + KEY_UNIT +
                " VARCHAR(200))")
        db?.execSQL(createReachedTable)
    }

    private fun addAvisTable(db: SQLiteDatabase?) {
        val createAvisTable = ("CREATE TABLE " + TABLE_AVIS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT)")
        db?.execSQL(createAvisTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) =
        if(newVersion > oldVersion){
            var counter = oldVersion
            if (counter<8){
                counter += 1
                migrate(db)
                deleteDB(db)
            }else {
            }
        }
        else{
            db!!.execSQL("DROP TABLE IF EXISTS $TABLE_STATS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_INTOOK_COUNTER")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_REACHED")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_AVIS")
        }

    private fun migrate(db: SQLiteDatabase?) {
        //aggiorna le unità
        var selectQuery = "SELECT * FROM $TABLE_STATS ORDER BY $KEY_DATE DESC"
        db!!.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    var unit = it.getString(4)
                    var intook = it.getFloat(7)
                    var total = it.getFloat(8)
                    val id = it.getInt(0)
                    if(unit=="0z UK"){
                        intook = AppUtils.ozUSToozUK(intook)
                        total = AppUtils.ozUSToozUK(total)
                        unit = "fl oz"
                        db.execSQL("UPDATE $TABLE_STATS set $KEY_N_INTOOK = $intook, $KEY_N_TOTAL_INTAKE = $total," +
                                "$KEY_UNIT = '$unit' WHERE $KEY_ID = $id")
                    }
                    else if(unit=="0z US"){
                        unit = "fl oz"
                        db.execSQL("UPDATE $TABLE_STATS set $KEY_UNIT = '$unit' WHERE $KEY_ID = $id")
                    }
                    it.moveToNext()
                }
            }
        }
        selectQuery = "SELECT * FROM $TABLE_REACHED ORDER BY $KEY_DATE DESC"
        db.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    var unit = it.getString(3)
                    var qta = it.getFloat(2)
                    val id = it.getInt(0)
                    if(unit=="0z UK"){
                        qta = AppUtils.ozUSToozUK(qta)
                        unit = "fl oz"
                        db.execSQL("UPDATE $TABLE_REACHED set $KEY_QTA = $qta," +
                                "$KEY_UNIT = '$unit' WHERE $KEY_ID = $id")
                    }
                    else if(unit=="0z US"){
                        unit = "fl oz"
                        db.execSQL("UPDATE $TABLE_REACHED set $KEY_UNIT = '$unit' " +
                                "WHERE $KEY_ID = $id")
                    }
                    it.moveToNext()
                }
            }
        }
        //migra le tabelle
        insertDataToNewDb(db)
    }

    private fun insertDataToNewDb(db: SQLiteDatabase) {
        insertDrinkDetails(db)
        insertReached(db)
        insertAvis(db)
    }

    private fun insertDrinkDetails(db: SQLiteDatabase) {
        val selectQuery = "SELECT * FROM $TABLE_STATS ORDER BY $KEY_DATE DESC"
        db.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    val date = it.getString(1)
                    val unit = it.getString(4)
                    val intook = it.getFloat(7)
                    val total = it.getFloat(8)
                    when(unit){
                        "fl oz"->{
                            val containerValue = AppUtils.ozUKToMl(intook)
                            val containerValueOZ = intook
                            val containerMeasure = intook
                            val drinkDate = date
                            val drinkTime = "00:00"
                            val drinkDateTime = "$date $drinkTime"
                            val today = AppUtils.ozUKToMl(total)
                            val todayOZ = total

                            val initialValues = ContentValues()
                            initialValues.put("ContainerValue", "" + containerValue)
                            initialValues.put("ContainerValueOZ", "" + containerValueOZ)
                            initialValues.put("ContainerMeasure", "" + containerMeasure)
                            initialValues.put("DrinkDate", "" + drinkDate)
                            initialValues.put("DrinkTime", "" + drinkTime)
                            initialValues.put("DrinkDateTime", "" + drinkDateTime)
                            initialValues.put("TodayGoal", "" + today)
                            initialValues.put("TodayGoalOZ", "" + todayOZ)

                            dh.INSERT("tbl_drink_details", initialValues)
                        }
                        "ml"->{
                            val containerValue = intook
                            val containerValueOZ = AppUtils.mlToOzUK(intook)
                            val containerMeasure = intook
                            val drinkDate = date
                            val drinkTime = "00:00"
                            val drinkDateTime = "$date $drinkTime"
                            val today = total
                            val todayOZ = AppUtils.mlToOzUK(total)

                            val initialValues = ContentValues()
                            initialValues.put("ContainerValue", "" + containerValue)
                            initialValues.put("ContainerValueOZ", "" + containerValueOZ)
                            initialValues.put("ContainerMeasure", "" + containerMeasure)
                            initialValues.put("DrinkDate", "" + drinkDate)
                            initialValues.put("DrinkTime", "" + drinkTime)
                            initialValues.put("DrinkDateTime", "" + drinkDateTime)
                            initialValues.put("TodayGoal", "" + today)
                            initialValues.put("TodayGoalOZ", "" + todayOZ)

                            dh.INSERT("tbl_drink_details", initialValues)
                        }
                    }
                    it.moveToNext()
                }
            }
        }
    }

    private fun insertReached(db: SQLiteDatabase) {
        val selectQuery = "SELECT * FROM $TABLE_REACHED ORDER BY $KEY_DATE DESC"
        db.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    val unit = it.getString(3)
                    val date = it.getString(1)
                    val qta = it.getFloat(2)
                    when(unit){
                        "fl oz"->{
                            val containerValue = AppUtils.ozUKToMl(qta)
                            val containerValueOZ = qta
                            val drinkDate = date

                            val initialValues = ContentValues()
                            initialValues.put("ContainerValue", "" + containerValue)
                            initialValues.put("ContainerValueOZ", "" + containerValueOZ)
                            initialValues.put("Date", "" + drinkDate)

                            dh.INSERT("tbl_goal_reached", initialValues)
                        }
                        "ml"->{
                            val containerValue = qta
                            val containerValueOZ = AppUtils.mlToOzUK(qta)
                            val drinkDate = date

                            val initialValues = ContentValues()
                            initialValues.put("ContainerValue", "" + containerValue)
                            initialValues.put("ContainerValueOZ", "" + containerValueOZ)
                            initialValues.put("Date", "" + drinkDate)

                            dh.INSERT("tbl_goal_reached", initialValues)
                        }
                    }
                    it.moveToNext()
                }
            }
        }
    }

    private fun insertAvis(db: SQLiteDatabase) {
        val selectQuery = "SELECT * FROM $TABLE_AVIS ORDER BY $KEY_DATE DESC"
        db.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    val date = it.getString(1)
                    val initialValues = ContentValues()
                    initialValues.put("BloodDonorDate", "" + date)

                    dh.INSERT("tbl_blood_donor", initialValues)
                    it.moveToNext()
                }
            }
        }
    }



    fun getAllReached(orderBy: Boolean = false ,db: SQLiteDatabase? = null) : Cursor{
        var selectQuery = "SELECT * FROM $TABLE_REACHED"
        if(orderBy){
            selectQuery += " ORDER BY datetime(substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) || ' ' || substr(date, 12, 8)) ASC"
        }
        val dbToUse = db ?: this.readableDatabase
        return dbToUse.rawQuery(selectQuery, null)
    }

    fun start(boolean: Boolean):Boolean {
        create = boolean
        try {
            val db = this.writableDatabase
            return true
        }
        catch (e:Exception){
            return false
        }
    }
}