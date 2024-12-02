package rpt.tool.mementobibere.utils.data

class BackUpFileModel {
    var name: String = ""
    var path: String = ""
    var lastmodify: Long = 0
    var isSelected: Boolean = false

    fun isSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }
}
