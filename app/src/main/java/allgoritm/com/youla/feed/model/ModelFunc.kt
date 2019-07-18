package allgoritm.com.youla.feed.model

inline fun <reified T> Any.handle(block: (T) -> Unit){
    if(this is T) {
        block(this)
    }
}