package rpt.tool.mementobibere.data.models

class SoundModel {
    var id = 0
    var name: String? = null
    var isSelected = false
        private set

    fun isSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }
}