package rpt.tool.mementobibere.utils.helpers

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
        addFirstTable(db)

        addNewTables(db)

        addAvisTable(db)

        deleteDB(db)
    }

    private fun deleteDB(db: SQLiteDatabase?) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_STATS")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_INTOOK_COUNTER")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_REACHED")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_AVIS")
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
            db!!.execSQL("DROP TABLE IF EXISTS $TABLE_INTOOK_COUNTER")
            db!!.execSQL("DROP TABLE IF EXISTS $TABLE_REACHED")
            db!!.execSQL("DROP TABLE IF EXISTS $TABLE_AVIS")
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
                    var id = it.getInt(0)
                    if(unit=="0z UK"){
                        intook = AppUtils.ozUSToozUK(intook)
                        total = AppUtils.ozUSToozUK(total)
                        unit = "fl oz"
                        db!!.execSQL("UPDATE $TABLE_STATS set $KEY_N_INTOOK = $intook, $KEY_N_TOTAL_INTAKE = $total," +
                                "$KEY_UNIT = '$unit' WHERE $KEY_ID = $id")
                    }
                    else if(unit=="0z US"){
                        unit = "fl oz"
                        db!!.execSQL("UPDATE $TABLE_STATS set $KEY_UNIT = '$unit' WHERE $KEY_ID = $id")
                    }
                    it.moveToNext()
                }
            }
        }
        selectQuery = "SELECT * FROM $TABLE_REACHED ORDER BY $KEY_DATE DESC"
        db!!.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    var unit = it.getString(3)
                    var qta = it.getFloat(2)
                    var id = it.getInt(0)
                    if(unit=="0z UK"){
                        qta = AppUtils.ozUSToozUK(qta)
                        unit = "fl oz"
                        db!!.execSQL("UPDATE $TABLE_REACHED set $KEY_QTA = $qta," +
                                "$KEY_UNIT = '$unit' WHERE $KEY_ID = $id")
                    }
                    else if(unit=="0z US"){
                        unit = "fl oz"
                        db!!.execSQL("UPDATE $TABLE_REACHED set $KEY_UNIT = '$unit' " +
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
        var selectQuery = "SELECT * FROM $TABLE_STATS ORDER BY $KEY_DATE DESC"
        db!!.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    var date = it.getString(1)
                    var unit = it.getString(4)
                    var intook = it.getFloat(7)
                    var total = it.getFloat(8)
                    when(unit){
                        "fl oz"->{
                            var containerValue = AppUtils.ozUKToMl(intook)
                            var containerValueOZ = intook
                            var containerMeasure = intook
                            var drinkDate = date
                            var drinkTime = "00:00"
                            var drinkDateTime = "$date $drinkTime"
                            var today = AppUtils.ozUKToMl(total)
                            var todayOZ = total

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
                            var containerValue = intook
                            var containerValueOZ = AppUtils.mlToOzUK(intook)
                            var containerMeasure = intook
                            var drinkDate = date
                            var drinkTime = "00:00"
                            var drinkDateTime = "$date $drinkTime"
                            var today = total
                            var todayOZ = AppUtils.mlToOzUK(total)

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
        var selectQuery = "SELECT * FROM $TABLE_REACHED ORDER BY $KEY_DATE DESC"
        db!!.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    var unit = it.getString(3)
                    var date = it.getString(1)
                    var qta = it.getFloat(2)
                    when(unit){
                        "fl oz"->{
                            var containerValue = AppUtils.ozUKToMl(qta)
                            var containerValueOZ = qta
                            var drinkDate = date

                            val initialValues = ContentValues()
                            initialValues.put("ContainerValue", "" + containerValue)
                            initialValues.put("ContainerValueOZ", "" + containerValueOZ)
                            initialValues.put("Date", "" + drinkDate)

                            dh.INSERT("tbl_goal_reached", initialValues)
                        }
                        "ml"->{
                            var containerValue = qta
                            var containerValueOZ = AppUtils.mlToOzUK(qta)
                            var drinkDate = date

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
        var selectQuery = "SELECT * FROM $TABLE_AVIS ORDER BY $KEY_DATE DESC"
        db!!.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    var date = it.getString(1)
                    val initialValues = ContentValues()
                    initialValues.put("BloodDonorDate", "" + date)

                    dh.INSERT("tbl_blood_donor", initialValues)
                    it.moveToNext()
                }
            }
        }
    }

    private fun updateValuesOfStats(db: SQLiteDatabase) {
        val selectQuery = "SELECT * FROM $TABLE_STATS ORDER BY $KEY_DATE DESC"
        db.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    var month = it.getString(1)
                    var year = it.getString(1)
                    var id = it.getInt(0)
                    db!!.execSQL("UPDATE $TABLE_STATS set $KEY_MONTH = $month, $KEY_YEAR = $year WHERE $KEY_ID = $id")
                    it.moveToNext()
                }
            }
        }
    }

    private fun updateValuesOfCounter(db: SQLiteDatabase) {
        getAllReached(db = db).use{ reached ->
            if (reached.moveToFirst()) {
                for (i in 0 until reached.count) {
                    val selectQuery = "SELECT $KEY_INTOOK FROM $TABLE_INTOOK_COUNTER WHERE $KEY_DATE = ?"
                    db.rawQuery(selectQuery, arrayOf(reached.getString(1))).use {
                        if (!it.moveToFirst()) {
                            var data = reached.getString(1)
                            var script ="INSERT INTO $TABLE_INTOOK_COUNTER ($KEY_DATE, $KEY_INTOOK, $KEY_INTOOK_COUNT) VALUES (\"$data\", 6,1)"
                            db!!.execSQL(script)
                        }
                    }
                    reached.moveToNext()
                }
            }
        }
    }

    private fun updateValuesOfIntake(db: SQLiteDatabase) {
        val selectQuery = "SELECT * FROM $TABLE_STATS ORDER BY $KEY_DATE DESC"
        db.rawQuery(selectQuery, null).use{ it ->
            if (it.moveToFirst()) {
                for (i in 0 until it.count) {
                    var intook = it.getInt(2)
                    var total = it.getInt(3)
                    var id = it.getInt(0)
                    db!!.execSQL("UPDATE $TABLE_STATS set $KEY_INTOOK = -1, $KEY_TOTAL_INTAKE = -1,$KEY_N_INTOOK = $intook, $KEY_N_TOTAL_INTAKE = $total WHERE $KEY_ID = $id")
                    it.moveToNext()
                }
            }
        }
    }

    private fun checkExistance(date: String, query: String): Int {
        val db = this.readableDatabase
        db.rawQuery(query, arrayOf(date)).use {
            if (it.moveToFirst()) {
                return it.count
            }
        }
        return 0
    }

    fun getAllReached(orderBy: Boolean = false ,db: SQLiteDatabase? = null) : Cursor{
        var selectQuery = "SELECT * FROM $TABLE_REACHED"
        if(orderBy){
            selectQuery += " ORDER BY datetime(substr(date, 7, 4) || '-' || substr(date, 4, 2) || '-' || substr(date, 1, 2) || ' ' || substr(date, 12, 8)) ASC"
        }
        val dbToUse = db ?: this.readableDatabase
        return dbToUse.rawQuery(selectQuery, null)
    }

    fun start() {
        val db = this.writableDatabase
    }
}