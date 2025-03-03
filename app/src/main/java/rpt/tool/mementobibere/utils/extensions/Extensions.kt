package rpt.tool.mementobibere.utils.extensions

import rpt.tool.mementobibere.utils.AppUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.jvm.isAccessible

fun Float.toCalculatedValue(current: Int, newValue: Int) : Float{
    if(current == 0 && newValue == 1) {
        return AppUtils.mlToOzUK(this)
    }
    if(current == 0 && newValue == 2){
        return AppUtils.mlToOzUS(this)
    }
    if(current == 1 && newValue == 0) {
        return AppUtils.ozUKToMl(this)
    }
    if(current == 1 && newValue == 2) {
        return AppUtils.ozUKToOzUS(this)
    }
    if(current == 2 && newValue == 1) {
        return AppUtils.ozUSToozUK(this)
    }
    if(current == 2 && newValue == 0) {
        return AppUtils.ozUSToMl(this)
    }
    return this
}

fun Int.toPrincipalUnit(weightUnit: Int): Int {
    if(weightUnit == 1) {
        return AppUtils.lblToKg(this)
    }
    return this
}


fun Float.length(): Int {
    val string = this.toString()
    return string.length - (string.indexOf('.') + 1)
}


fun String.toYear(): String {
    val split = this.split("-")
    return split[2]
}

fun String.toMonth(): String {
    val split = this.split("-")
    if(split[1].startsWith("0")){
        return split[1].removePrefix("0")
    }
    return split[1]
}

inline fun <reified T> T.callPrivateFunc(name: String, vararg args: Any?): Any? =
    T::class
        .declaredMemberFunctions
        .firstOrNull { it.name == name }
        ?.apply { isAccessible = true }
        ?.call(this, *args)
