package rpt.tool.mementobibere.basic.appbasiclibs.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class Preferences_Helper {
    var mContext: Context
    var act: Activity?

    var sharedPreferences: SharedPreferences


    constructor(mContext: Context, act: Activity?) {
        this.mContext = mContext
        this.act = act

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    constructor(mContext: Context) {
        this.mContext = mContext
        this.act = null

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    fun savePreferences(key: String?, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun savePreferences(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun savePreferences(key: String?, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    fun savePreferences(key: String?, value: Float) {
        val editor = sharedPreferences.edit()
        editor.putFloat(key, value)
        editor.commit()
    }

    fun savePreferences(key: String?, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.commit()
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    fun getBoolean(name: String?): Boolean {
        return sharedPreferences.getBoolean(name, false)
    }

    fun getString(name: String?): String? {
        return sharedPreferences.getString(name, "")
    }

    fun getInt(name: String?): Int {
        return sharedPreferences.getInt(name, 0)
    }

    fun getFloat(name: String?): Float {
        return sharedPreferences.getFloat(name, 0f)
    }

    fun getLong(name: String?): Long {
        return sharedPreferences.getLong(name, 0)
    } /*
    public void loadSavedPreferences()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        //Constant.UserLabel=sharedPreferences.getString(Constant.perfUserLabel, "");
    }*/
}
