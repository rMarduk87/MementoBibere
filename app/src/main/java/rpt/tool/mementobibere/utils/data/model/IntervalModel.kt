package rpt.tool.mementobibere.utils.data.model

class IntervalModel {
    var id: Int = 0
    var name: String? = null
    var isSelected: Boolean = false
        private set

    fun isSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }
}