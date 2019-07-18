package allgoritm.com.youla.utils.ktx

import org.json.JSONException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

enum class ErrorType {
    DATA, NETWORK, TIMEOUT, UNKNOWN
}

fun Throwable.doOnType(function: (ErrorType, Throwable) -> Unit) {
    returnOnType(function)
}

fun <T> Throwable.returnOnType(function: (ErrorType, Throwable) -> T) : T {
    return if (this is UnknownHostException) {
        function(ErrorType.NETWORK, this)
    } else if (this is SocketTimeoutException || this is TimeoutException) {
        function(ErrorType.TIMEOUT, this)
    } else if (this is JSONException) {
        function(ErrorType.DATA, this)
    } else {
        function(ErrorType.UNKNOWN, this)
    }
}