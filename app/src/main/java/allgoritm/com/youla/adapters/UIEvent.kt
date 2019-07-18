package allgoritm.com.youla.adapters

const val MAIN_TAB_EVENT_ID = -10_16
const val SEARCH_TAB_EVENT_ID = -10_17
const val ADD_PRODUCT_TAB_EVENT_ID = -10_18
const val ADD_PRODUCT_TAB_PROMO_EVENT_ID= -10_18_1
const val CHAT_TAB_EVENT_ID = -10_19
const val FAV_TAB_EVENT_ID = -10_20

interface UIEvent {
    val id: Int
}