package rpt.tool.mementobibere.utils.data.model

class NextReminderModel(val millisecond: Long, val time: String) : Comparable<NextReminderModel> {

    override fun compareTo(other: NextReminderModel): Int {
        return if (millisecond > other.millisecond) {
            1
        } else if (millisecond < other.millisecond) {
            -1
        } else {
            0
        }
    }

    override fun toString(): String {
        return this.time
    }
}
