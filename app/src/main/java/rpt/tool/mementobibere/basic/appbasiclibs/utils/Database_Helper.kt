package rpt.tool.mementobibere.basic.appbasiclibs.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import androidx.core.database.getStringOrNull
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.channels.FileChannel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Database_Helper
@SuppressLint("WrongConstant") constructor(var mContext: Context, var act: Activity) {
    var uf: Utility_Function = Utility_Function(mContext, act)
    var ah: Alert_Helper
    var sh: String_Helper

    init {
        uf.permission_StrictMode()

        ah = Alert_Helper(mContext)
        sh = String_Helper(mContext, act)

        Constant.SDB = mContext.openOrCreateDatabase(
            Constant.DATABASE_NAME,
            SQLiteDatabase.CREATE_IF_NECESSARY,
            null
        )
    }

    fun CREATE_TABLE(table_name: String, fields: HashMap<String, String>) {
        var query = "CREATE TABLE IF NOT EXISTS $table_name("

        val myVeryOwnIterator: Iterator<*> = fields.keys.iterator()
        while (myVeryOwnIterator.hasNext()) {
            val key = myVeryOwnIterator.next() as String
            val value = fields[key]

            query += "$key $value,"
        }

        query = query.substring(0, query.length - 1)

        query = "$query);"

        println("CREAT QUERY : $query")

        FIRE(query)
    }

    fun INSERT(table_name: String?, fields: HashMap<String?, String?>) {
        /*String query="INSERT INTO "+table_name+" ( ";
		String column_list="",column_value_list="";

		Iterator myVeryOwnIterator = fields.keySet().iterator();
		while(myVeryOwnIterator.hasNext())
		{
			String key=(String)myVeryOwnIterator.next();
			String value=fields.get(key);

			column_list+="\""+key+"\",";
			column_value_list+="\""+value+"\",";
		}

		column_list=column_list.substring(0,column_list.length()-1);
		column_value_list=column_value_list.substring(0,column_value_list.length()-1);

		query+=column_list;

		query+=") VALUES(";

		query+=column_value_list;

		query+=")";

		System.out.println("QUERY : "+ query);

		FIRE(query);*/

        val initialValues = ContentValues()

        val myVeryOwnIterator: Iterator<*> = fields.keys.iterator()
        while (myVeryOwnIterator.hasNext()) {
            val key = myVeryOwnIterator.next() as String
            val value = fields[key]
            initialValues.put(key, value)
        }

        Constant.SDB!!.insert(table_name, null, initialValues)
    }

    fun INSERT(table_name: String?, fields: ContentValues?) {
        Constant.SDB!!.insert(table_name, null, fields)
    }

    fun UPDATE(table_name: String?, fields: HashMap<String?, String?>, where_con: String?) {
        /*String query="UPDATE "+table_name+" SET ";

		Iterator myVeryOwnIterator = fields.keySet().iterator();
		while(myVeryOwnIterator.hasNext())
		{
			String key=(String)myVeryOwnIterator.next();
			String value=fields.get(key);

			query+=key+"=\""+value+"\",";
		}

		query=query.substring(0,query.length()-1);

		query+=" WHERE "+where_con;

		System.out.println("UPDATE QUERY : "+ query);

		FIRE(query);*/

        val initialValues = ContentValues()

        val myVeryOwnIterator: Iterator<*> = fields.keys.iterator()
        while (myVeryOwnIterator.hasNext()) {
            val key = myVeryOwnIterator.next() as String
            val value = fields[key]
            initialValues.put(key, value)
        }

        Constant.SDB!!.update(table_name, initialValues, where_con, null)
    }

    fun UPDATE(table_name: String?, fields: ContentValues?, where_con: String?) {
        Constant.SDB!!.update(table_name, fields, where_con, null)
    }

    fun GET_LOGIN_USER_DETAILS() {
        try {
            //Constant.user_id="";

            val cur = Constant.SDB!!.rawQuery("select * from tbl_user_login", null)
            while (cur.moveToNext()) {
                //Constant.user_id=cur.getString(cur.getColumnIndex("user_id"));
            }
        } catch (e: Exception) {
        }
    }

    fun getdataquery(query: String): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        val c = Constant.SDB!!.rawQuery(query, null)

        println("SELECT QUERY : $query")

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i)
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    fun getdata(table_name: String): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        val query = "SELECT * FROM $table_name"

        val c = Constant.SDB!!.rawQuery(query, null)

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    if(c.getStringOrNull(i) != null){
                        map[c.getColumnName(i)] = c.getString(i)
                    }
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    fun getdata(table_name: String, where_con: String): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " where $where_con"

        val c = Constant.SDB!!.rawQuery(query, null)

        println("SELECT QUERY : $query")

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    if(c.getStringOrNull(i) != null){
                        map[c.getColumnName(i)] = c.getString(i)
                    }
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    fun getdata(
        table_name: String,
        order_field: String,
        order_by: Int
    ): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(order_field)) {
            query += if (order_by == 0) " ORDER BY $order_field ASC"
            else " ORDER BY $order_field DESC"
        }

        val c = Constant.SDB!!.rawQuery(query, null)

        println("DESC QUERY:$query")

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    if(c.getStringOrNull(i) != null){
                        map[c.getColumnName(i)] = c.getString(i)
                    }
                }
                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    fun getdata(
        table_name: String,
        where_con: String,
        order_field: String,
        order_by: Int
    ): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " WHERE $where_con"

        if (!sh.check_blank_data(order_field)) {
            query += if (order_by == 0) " ORDER BY $order_field ASC"
            else " ORDER BY $order_field DESC"
        }

        val c = Constant.SDB!!.rawQuery(query, null)

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i)
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    fun getdata(
        field_name: String,
        table_name: String,
        where_con: String
    ): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT $field_name FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " where $where_con"

        print("JOIN QUERY:$query")


        //ah.Show_Alert_Dialog("JOIN QUERY:"+query);
        val c = Constant.SDB!!.rawQuery(query, null)


        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i)
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    fun getdata(
        field_name: String,
        table_name: String,
        where_con: String,
        order_field: String,
        order_by: Int
    ): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT $field_name FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " where $where_con"

        if (!sh.check_blank_data(order_field)) {
            query += if (order_by == 0) " ORDER BY $order_field ASC"
            else " ORDER BY $order_field DESC"
        }

        println("HISTORY JOIN QUERY:$query")

        val c = Constant.SDB!!.rawQuery(query, null)

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i)
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    fun REMOVE(table_name: String) {
        val query = "DELETE FROM $table_name"
        FIRE(query)
    }

    fun REMOVE(table_name: String, where_con: String) {
        var query = "DELETE FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " WHERE $where_con"

        FIRE(query)
    }

    fun TOTAL_ROW(table_name: String): Int {
        val query = "SELECT * FROM $table_name"
        val c = Constant.SDB!!.rawQuery(query, null)

        return c.count
    }

    fun TOTAL_ROW(table_name: String, where_con: String): Int {
        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " WHERE $where_con"

        val c = Constant.SDB!!.rawQuery(query, null)

        val count = c!!.count

        if (c != null) c.close()

        return count
    }

    fun IS_EXISTS(table_name: String, where_con: String): Boolean {
        var query = "SELECT * FROM $table_name"

        if (!sh.check_blank_data(where_con)) query += " WHERE $where_con"

        val c = Constant.SDB!!.rawQuery(query, null)

        return c.count > 0
    }

    fun GET_LAST_ID(table_name: String): String {
        val query = "SELECT id FROM $table_name"

        val c = Constant.SDB!!.rawQuery(query, null)

        if (c.moveToLast()) return "" + c.getString(0)

        return "0"
    }

    /*	public long NO_OF_AFFECTED_ROWS()
	{
		long affectedRowCount=0;

		Cursor cursor = Constant.SDB.rawQuery("SELECT changes() AS affected_row_count", null);
		if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
			affectedRowCount = cursor.getLong(cursor.getColumnIndex("affected_row_count"));

		return affectedRowCount;
	}*/
    fun FIRE(query: String?) {
        Constant.SDB!!.execSQL(query)
    }

    fun MD5(md5: String): String? {
        try {
            val md = MessageDigest.getInstance("MD5")
            val array = md.digest(md5.toByteArray())
            val sb = StringBuffer()
            for (i in array.indices) {
                sb.append(Integer.toHexString((array[i].toInt() and 0xFF) or 0x100).substring(1, 3))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return null
    }

    fun exportDB(): Boolean {
        var result = false
        val sd = File(Environment.getExternalStorageDirectory().absolutePath, "Databackup")

        if (!sd.exists()) {
            sd.mkdirs()
        }
        //File sd = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Databackup");
        val data = Environment.getDataDirectory()
        var source: FileChannel? = null
        var destination: FileChannel? = null
        val currentDBPath =
            "/data/" + "com.appname.appnamebasic" + "/databases/" + Constant.DATABASE_NAME
        val backupDBPath = Constant.DATABASE_NAME
        val currentDB = File(data, currentDBPath)
        val backupDB = File(sd, backupDBPath)
        try {
            source = FileInputStream(currentDB).channel
            destination = FileOutputStream(backupDB).channel
            destination.transferFrom(source, 0, source.size())
            source.close()
            destination.close()
            result = true
            //Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (e: Exception) {
            result = false
            e.printStackTrace()
        }
        return result
    }
}
