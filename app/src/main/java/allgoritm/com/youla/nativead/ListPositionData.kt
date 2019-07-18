package allgoritm.com.youla.nativead

data class ListPositionData(
        val firstVisiblePosition: Int,
        val lastVisiblePosition: Int,
        val fastScrolling : Boolean = false
)