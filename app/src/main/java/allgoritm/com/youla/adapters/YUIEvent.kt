package allgoritm.com.youla.adapters

import allgoritm.com.youla.nativead.INativeAd

const val UNKNOWN_EVENT_ID = -404

fun isUnknownEvent(eventId: Int) = UNKNOWN_EVENT_ID == eventId

const val CHOOSE_DELIVERY_EVENT_ID = -11
const val DISCOUNT_PRODUCT_EVENT_ID = -12
const val DISCOUNT_INFO_BLOCK_EVENT_ID = -13
const val RETRY = -14
const val PROFILE_PAYMENT_EVENT_ID = -15
const val SEARCH_EVENT_ID = -16
const val PAYMENT_CARD_EVENT_ID = -17
const val OPEN_URL = -18

const val FAVORITE_TOGGLE_CLICK = -20
const val INFOBLOCK_CLOSE = -21
const val INFOBLOCK_OPEN_URL = -22
const val FILTER_FAB_CLICK = -23
const val PRODUCT_CALL = -26
const val NATIVE_ADVERT_EVENT = -27
const val NATIVE_ADVERT_RECYCLED = -28
const val BUNDLE_CLICK = -31
const val PRODUCT_CLICK = -32
const val CATEGORY_CLICk = -33
const val TO_REALTY_MAP_CLICK = -34
const val INFOBLOCK_SHOW = -35
const val HYPERLINK_CLICK = -36
const val DELIVERY_DATA_SAVE_CLICK = -37
const val INIT = -38
const val DESTROY = -39
const val START_SCROLL = -42
const val STOP_SCROLL = -43
const val SUBSCRIBE_CLICK = -44
const val UNSUBSCRIBE_CLICK = -45
const val UNSUBSCRIBE_SHOW_DIALOG_CLICK = -46
const val SUBSCRIBE_FIRST_TIME_SHOW_DIALOG_CLICK = -47
const val FEED_UPDATE = -48
const val REFRESH = -49
const val GONE = -50
const val ADVERT_REFRESH = -51
const val PRODUCT_PAGE_BECOME_VISIBLE = -52
const val ACTIONS_BOTTOM_SHEET_CLICK = -53
const val LOCAL_IMAGES_PICKED = -54
const val RETRY_SNACKBAR = -55
const val RESUME = -56
const val CATEGORIES_BOTTOM_SHEET_CLICK = -57
const val PRODUCT_RESPOND = -59
const val SHOW_DUMMY = -58

const val LOADING = -10_10
const val PROFILE = -10_21
const val BACK = -10_22
const val SEARCH = -10_23
const val CLEAR_SEARCH = -10_24
const val DEBUG_UI_TEST = -10_25
const val RESET_FILTER_EVENT_ID = -10_26
const val CHANGE_VISIBILITY_EVENT_ID = -10_27
const val COUNTERS_EVENT_ID = -10_28
const val RESET_ALL_FILTERS_EVENT_ID = -10_30
const val CAROUSEL_HIDE = -10_31
const val CAROUSEL_SHOW_MORE = -10_32
const val DO_NOTHING = -10_401
const val UNKNOWN_MENU_ITEM = -10_404
const val SHOW_USER = -10_405
const val SUBSCRIPTION_SHOW_PROFILE = -10_406

const val TOP_SCROLL = -10_111

const val SEARCH_ADDRESS = -11_01
const val DELIVERY_PROMO = -11_02
const val OPEN_DELIVERY_DATA_FOR_CHANGE = -11_03

// ORDER ACTIONS

const val ORDER_TIMER_FINISH = -55_0
const val ORDER_PAY_CLICK = -55_1
const val ORDER_ACCEPT_TRANSFER = -55_2
const val ORDER_PROLONG = -55_3
const val ORDER_ON_ACCEPT_RECEIVE = -55_4
const val ORDER_ON_OPEN_DISPUTE = -55_5
const val ORDER_ON_DISPUTE_HISTORY = -55_6
const val ORDER_PAYMENT_TRY_AGAIN = -55_7
const val ORDER_PAYMENT_BIND_ANOTHER_CARD = -55_8
const val ORDER_ON_LINK_CLICK = -55_9
const val ORDER_ACCEPT = -55_10
const val ORDER_CARDS_EVENT_ID = -55_11
const val ORDER_ADD_NEW_CARD = -55_12
const val ORDER_CHOOSE_CARD = -55_13
const val ORDER_CLICK_CARD_EVENT_ID = -55_14
const val ORDER_OPEN_CHAT = -55_15
const val ORDER_REVIEW_CLICK= -55_16

//CHAT_SIMILAR_PRODUCTS_ACTION
const val CHAT_SIMILAR_SHOW_CLICK = -41_0
const val CHAT_SIMILAR_SEND_MESSAGE = -41_1
const val CHAT_SIMILAR_PAGE_CHANGED = -41_2
const val CHAT_SIMILAR_BUTTON_SHOWED = -41_4
const val CHAT_SIMILAR_HIDE_CLICK = -41_5
const val CHAT_SIMILAR_DIALOG_HIDE = -41_6

//CHAT_ACTION
const val CHAT_HISTORY_FIRST_LOAD = -42_0

const val TAP = -999

sealed class YUIEvent(override val id: Int) : UIEvent {

    class Base(id: Int): YUIEvent(id)
    class BaseParam(id: Int, val param: String): YUIEvent(id)

    sealed class Click(id: Int) : YUIEvent(id) {
        //cut out
    }

    class NativeAdvertEvent (val nativeAd: INativeAd?, val action: Int, val productId: String? = null) : YUIEvent(
        NATIVE_ADVERT_EVENT
    )
    class RecycleNativeAdvert(val nativeAd: INativeAd) : YUIEvent(NATIVE_ADVERT_RECYCLED)
    /*class Loading(val isLoading: Boolean): YUIEvent(LOADING) {

        fun show(a: YActivity){
           if(isLoading){
               a.showFullScreenLoading()
           } else {
               a.hideFullScreenLoading()
           }
        }

    }*/

    //cut out

}