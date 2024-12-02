package rpt.tool.mementobibere.utils.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.equalsIgnoreCase(other: String) =
    (this as java.lang.String).equalsIgnoreCase(other)

fun String.toReal(): String {
    if (this == "0z US") {
        return "fl oz"
    }
    return "ml"
}

fun Int.toMigration(): Int {
    when(this){
        0-> return 3
        2-> return 1
        3-> return 0
    }
    return 0
}

fun Long.toData(): Date {
   return Date(this)
}

fun Date.toStringDate(): String {
    val simpleDateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return simpleDateFormat.format(this)
}

fun Date.toStringHour(): String {
    val simpleDateFormat = SimpleDateFormat("hh", Locale.getDefault())
    return simpleDateFormat.format(this)
}

fun Date.toStringMinute(): String {
    val simpleDateFormat = SimpleDateFormat("mm", Locale.getDefault())
    return simpleDateFormat.format(this)
}