package allgoritm.com.youla.pagination

interface PaginationViewModel {

    fun loadFirst()
    fun loadNext()
    fun reload()

    fun getPageSize() : Int
}