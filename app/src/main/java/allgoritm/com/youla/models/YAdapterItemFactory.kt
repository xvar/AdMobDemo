package allgoritm.com.youla.models

import allgoritm.com.youla.adapters.EmptyDummyItem
import allgoritm.com.youla.admob.demo.R
import allgoritm.com.youla.utils.ResourceProvider
import allgoritm.com.youla.utils.ktx.ErrorType
import allgoritm.com.youla.utils.ktx.returnOnType
import javax.inject.Inject

class YAdapterItemFactory @Inject constructor(val rp: ResourceProvider) {

    fun getErrorItem(t: Throwable) : YAdapterItem {
        return t.returnOnType { type, _ -> getErrorItem(type) }
    }

    fun getEmptyItem(t: Throwable) : EmptyDummyItem {
        return t.returnOnType { type, _ -> getEmptyItem(type)}
    }

    private fun getEmptyItem(type: ErrorType) : EmptyDummyItem {
        return when (type) {
            ErrorType.TIMEOUT, ErrorType.NETWORK ->
                EmptyDummyItem(
                        R.drawable.pic_offline,
                        rp.getString(R.string.no_connection),
                        rp.getString(R.string.no_connection_description),
                        rp.getString(R.string.try_again)
                )
            else ->
                EmptyDummyItem(
                        R.drawable.pic_error,
                        rp.getString(R.string.other_error_dummy_title),
                        rp.getString(R.string.other_error_description),
                        rp.getString(R.string.try_again)
                )
        }
    }

    private fun getErrorItem(type: ErrorType) : YAdapterItem {
        return when (type) {
            ErrorType.TIMEOUT, ErrorType.NETWORK ->
                YAdapterItem.ErrorItem(
                        R.drawable.pic_offline,
                        R.string.no_connection,
                        R.string.no_connection_description,
                        R.string.try_again)
            else ->
                YAdapterItem.ErrorItem(
                        R.drawable.pic_error,
                        R.string.other_error_dummy_title,
                        R.string.other_error_description,
                        R.string.try_again)
        }
    }

}